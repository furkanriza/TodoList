package com.furkan.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
@CrossOrigin(origins = "http://localhost:3000/")
public class UserController {

    @GetMapping("/")
    public ResponseEntity<Void> helloUserController(){
        System.out.println("token validated token validated token validated token validated token validated token validated token validated token validated ");
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
