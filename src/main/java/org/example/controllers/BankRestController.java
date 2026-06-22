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
    @GetMapping("/get_user/{numberPhone}")
    public ResponseEntity<UserDto> getUser(@PathVariable("numberPhone") Long phoneNumber) {
        try {
            System.out.println("Пришли данные: " + phoneNumber);
            UserDto userDto = userService.getUser(phoneNumber);
            return ResponseEntity.status(HttpStatus.OK).body(userDto);
        } catch (Exception ex) {
            return ResponseEntity.badRequest().build();
        }
    }

    //http://localhost:9002/transaction?fromPhoneNumber=89676400940&toPhoneNumber=89676400942&sum=99
    //TODO http://localhost:9002/transaction
    @PostMapping("/transaction")
    public ResponseEntity<String> transaction(
            @RequestParam("fromPhoneNumber") long fromPhoneNumber,
            @RequestParam("toPhoneNumber") long toPhoneNumber,
            @RequestParam("sum") int sum) {
        try {
            String response = userService.transaction(fromPhoneNumber, toPhoneNumber, sum);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception ex) {
            return ResponseEntity.badRequest().build();
        }
    }

}
