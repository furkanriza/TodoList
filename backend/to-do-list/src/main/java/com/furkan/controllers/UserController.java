package com.furkan.controllers;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
@CrossOrigin(origins = "http://localhost:3000/")
public class UserController {

    @GetMapping("/")
    public String helloUserController(){
        return "User access level";
    }

    // not implemented for registration system yet.
    /*
    @GetMapping("/")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        for (User user:users) {
            System.out.println("---returned:    "+user.toString());
        }
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Integer id) {
        Optional<User> user = userService.getUserById(id);
        if (user.isPresent()){
            User returnedUser = user.get();
            System.out.println("---returned:    "+returnedUser);
        }
        return user.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/")
    public ResponseEntity<User> createUser(@RequestBody User user) {
        User createdUser = userService.createUser(user);
        System.out.println("---posted:    "+ createdUser.toString());
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Integer id, @RequestBody User user) {
        try {
            User updatedUser = userService.updateUser(id, user);
            System.out.println("---updated:    "+user.toString() + "  replaced with   >>>   "+ updatedUser.toString());
            return ResponseEntity.ok(updatedUser);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Integer id) {
        Optional<User> user = userService.getUserById(id);

        if (user.isPresent()) {
            System.out.println("here  "+ id);
            User deletedUser = user.get();
            userService.deleteUser(id);
            System.out.println("---deleted:    " + deletedUser);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            System.out.println("not here  "+ id);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    //not tested
    @PatchMapping("/{id}")
    public ResponseEntity<User> patchUser(@PathVariable Integer id, @RequestBody User updatedUser) {
        Optional<User> existingUser = userService.getUserById(id);
        if (existingUser.isPresent()) {
            User user = existingUser.get();
            if (updatedUser.getUsername() != null) {
                user.setUsername(updatedUser.getUsername());
            }
            if (updatedUser.getPassword() != null) {
                user.setPassword(updatedUser.getPassword());
            }

            User updated = userService.updateUser(id, user);
            System.out.println("---updated    "+ updatedUser +"  to   >>>   "+updated.toString());
            return new ResponseEntity<>(updated, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<User> login(@RequestBody User user) {
        System.out.println("*************************************************************"+user.getId());
        System.out.println("---Received:    " + user);
        Optional<User> authenticatedUser = userService.authenticateUser(user.getUsername(), user.getPassword());
        return authenticatedUser.map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
    }*/
}
