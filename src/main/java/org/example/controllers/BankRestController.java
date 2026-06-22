package org.example.controllers;

import org.example.dto.UserDto;
import org.example.model.User;
import org.example.services.AccountService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

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


}
