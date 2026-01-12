package com.pocketpipo.pocketpipo.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pocketpipo.pocketpipo.dto.CreateUserRequestDTO;
import com.pocketpipo.pocketpipo.service.UserService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/user")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/users")
    public ResponseEntity<Void> createUser(@RequestBody CreateUserRequestDTO userRequestDTO) {
        this.userService.createUser(userRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
    
    @DeleteMapping("")
    public ResponseEntity<Void> deleteCurrentUser() {
        this.userService.deleteCurrentLoggedUser();
        
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
