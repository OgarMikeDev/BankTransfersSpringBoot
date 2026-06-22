package org.example.controllers;

import org.example.dto.UserDto;
import org.example.model.User;
import org.example.services.AccountService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class BankRestController {
    private final AccountService accountService;

    public BankRestController(AccountService accountService) {
        this.accountService = accountService;
    }

    //TODO http://localhost:9002/save_user
    @PostMapping("/save_user")
    public ResponseEntity<UserDto> saveUser(@RequestBody User user) {
        try {
            UserDto userDto = accountService.saveUser(user);
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
            UserDto userDto = accountService.getUser(phoneNumber);
            return ResponseEntity.status(HttpStatus.CREATED).body(userDto);
        } catch (Exception ex) {
            return ResponseEntity.badRequest().build();
        }
    }

}
