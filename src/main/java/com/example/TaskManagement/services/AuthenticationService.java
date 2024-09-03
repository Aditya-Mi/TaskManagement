package com.example.TaskManagement.services;

import com.example.TaskManagement.exception.DatabaseOperationException;
import com.example.TaskManagement.exception.RegistrationFailedException;
import com.example.TaskManagement.exception.UserAlreadyExistsException;
import com.example.TaskManagement.models.auth.*;

import com.example.TaskManagement.repositories.UserRepository;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthenticationService {
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse signup(RegisterRequest request) {
        try{
            if(userRepository.findByUsername(request.getUsername()).isPresent()) {
                throw new UserAlreadyExistsException("User with this username already exists");
            }
            var user = User.builder()
                    .username(request.getUsername())
                    .password(passwordEncoder.encode(request.getPassword()))
                    .role(Role.USER)
                    .build();
            userRepository.save(user);
            var jwtToken = jwtService.generateToken(user);
            return AuthenticationResponse.builder()
                    .token(jwtToken)
                    .build();
        } catch (DataAccessException e){
            throw new DatabaseOperationException("Error occurred while saving user", e);
        } catch (Exception e) {
            log.error("An unexpected error occurred during registration", e);
            throw new RegistrationFailedException("An unexpected error occurred during registration", e);
        }

    }

    public AuthenticationResponse login(AuthenticationRequest request) {
        try{
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getUsername(),
                            request.getPassword()
                    )
            );
            var user = userRepository.findByUsername(request.getUsername())
                    .orElseThrow(() -> new UsernameNotFoundException("User not found: " + request.getUsername()));
            var jwtToken = jwtService.generateToken(user);
            return AuthenticationResponse
                    .builder()
                    .token(jwtToken)
                    .expiresIn(jwtService.getExpirationTime())
                    .build();
        } catch (DataAccessException e){
            throw new DatabaseOperationException("Error occurred while fetching user", e);
        } catch (Exception e) {
            throw new RegistrationFailedException("An unexpected error occurred during login", e);
        }
    }
}
