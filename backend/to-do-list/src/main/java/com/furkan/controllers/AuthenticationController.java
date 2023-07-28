package com.furkan.controllers;

import com.furkan.dto.JwtResponse;
import com.furkan.dto.RefreshTokenRequest;
import com.furkan.models.ApplicationUser;
import com.furkan.models.LoginResponseDTO;
import com.furkan.models.RefreshToken;
import com.furkan.models.RegistrationDTO;
import com.furkan.services.AuthenticationService;
import com.furkan.services.RefreshTokenService;

import com.furkan.services.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;



@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "http://localhost:3000/")
public class AuthenticationController {

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private RefreshTokenService refreshTokenService;

    @Autowired
    private TokenService tokenService;

    @GetMapping("/")
    public String emptyString() {
        return "register";
    }

    @PostMapping("/register")
    public ApplicationUser registerUser(@RequestBody RegistrationDTO body) {
        return authenticationService.registerUser(body.getUsername(), body.getPassword());
    }

    @PostMapping("/login")
    public JwtResponse loginUser(@RequestBody RegistrationDTO body) {
        LoginResponseDTO user = authenticationService.loginUser(body.getUsername(), body.getPassword());

        if (user != null) {
            System.out.println("LoginResponseDTO token:  " + user.getJwt());
            String accessToken = user.getJwt();
            RefreshToken refreshToken = refreshTokenService.getOrCreateRefreshToken(body.getUsername());

            return JwtResponse.builder()
                    .accessToken(accessToken)
                    .token(refreshToken.getToken())
                    .build();
        } else {
            throw new UsernameNotFoundException("Invalid username or password!");
        }
    }

    @PostMapping("/refreshToken")
    public JwtResponse refreshToken(@RequestBody RefreshTokenRequest refreshTokenRequest) {
        return refreshTokenService.findByToken(refreshTokenRequest.getToken())
                .map(refreshTokenService::verifyExpiration)
                .map(RefreshToken::getUser)
                .map(user -> {
                    Authentication auth = new UsernamePasswordAuthenticationToken(user.getUsername(), null, user.getAuthorities());
                    String accessToken = tokenService.generateJwt(auth);

                    return JwtResponse.builder()
                            .accessToken(accessToken)
                            .token(refreshTokenRequest.getToken())
                            .build();
                })
                .orElseThrow(() -> new RuntimeException("Refresh token is not in the database!"));
    }
}   
