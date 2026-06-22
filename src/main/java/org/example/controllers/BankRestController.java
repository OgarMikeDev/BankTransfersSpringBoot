package org.example.controllers;

import org.example.dto.UserDto;
import org.example.model.User;
import org.example.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class BankRestController {
    private final UserService userService;

    public BankRestController(UserService userService) {
        this.userService = userService;
    }

    //TODO http://localhost:9002/save_user
    @PostMapping("/save_user")
    public ResponseEntity<UserDto> saveUser(@RequestBody User user) {
        try {
            UserDto userDto = userService.saveUser(user);
            return ResponseEntity.status(HttpStatus.CREATED).body(userDto);
        } catch (Exception ex) {
            return ResponseEntity.badRequest().build();
        }
    }

    //TODO http://localhost:9002/get_user/{numberPhone}
    @GetMapping("get_user/{numberPhone}")
    public ResponseEntity<UserDto> getUser(@PathVariable("numberPhone") Long phoneNumber) {
        try {
            System.out.println("Пришли данные: " + phoneNumber);
            UserDto userDto = userService.getUser(phoneNumber);
            return ResponseEntity.status(HttpStatus.CREATED).body(userDto);
        } catch (Exception ex) {
            return ResponseEntity.badRequest().build();
        }
    }

}
