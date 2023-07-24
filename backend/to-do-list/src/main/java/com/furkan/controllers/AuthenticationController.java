package com.furkan.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.furkan.models.ApplicationUser;
import com.furkan.models.LoginResponseDTO;
import com.furkan.models.RegistrationDTO;
import com.furkan.services.AuthenticationService;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "http://localhost:3000/")
public class AuthenticationController {

    @Autowired
    private AuthenticationService authenticationService;

    @GetMapping("/")
    public String emptyString (){
        return "register";
    }

    @PostMapping("/register")
    public ApplicationUser registerUser(@RequestBody RegistrationDTO body){
        return authenticationService.registerUser(body.getUsername(), body.getPassword());
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> loginUser(@RequestBody RegistrationDTO body) {
        LoginResponseDTO response = authenticationService.loginUser(body.getUsername(), body.getPassword());
        if (response.getUser() == null) {
            // If login credentials are invalid, return 401 Unauthorized
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        System.out.println("auth");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}   
