package org.example.services;

import org.example.dto.UserDto;
import org.example.model.User;
import org.example.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class AccountService {
    private final UserRepository userRepository;

    public AccountService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserDto saveUser(User user) {
        userRepository.save(user);
        User getUser = userRepository.findByPhoneNumber(user.getPhoneNumber());
        return fromUserToUserDto(getUser);
    }

    public UserDto fromUserToUserDto(User user) {
        UserDto userDto = new UserDto();
        userDto.setName(user.getName());
        userDto.setAccount(user.getNumberAccount());
        userDto.setPhone(user.getPhoneNumber());
        return userDto;
    }
}
