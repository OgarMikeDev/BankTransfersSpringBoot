package org.example.services;

import jakarta.transaction.Transactional;
import org.example.dto.UserDto;
import org.example.model.User;
import org.example.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserDto saveUser(User user) {
        userRepository.save(user);
        User getUser = userRepository.findByPhoneNumber(user.getPhoneNumber());
        return fromUserToUserDto(getUser);
    }

    public UserDto getUser(long phoneNumber) {
        User user = userRepository.findByPhoneNumber(phoneNumber);
        return fromUserToUserDto(user);
    }

    public UserDto fromUserToUserDto(User user) {
        UserDto userDto = new UserDto();
        userDto.setName(user.getName());
        userDto.setPhone(user.getPhoneNumber());
        userDto.setBlock(user.isBlock());
        userDto.setSum(user.getSum());
        return userDto;
    }

    /*
    TODO
     Благодаря @Transactional 2 этапа(отправка денег с одного счёта и зачисление на другой)
     засчитываются в 1 транзакцию,
     но если появляется исключение на любом из этапов,
     напр-р, после списания и зачисления денег обнаруживается ошибка,
     связанная с тем, что пользователь заблокирован,
     все списания и зачисления откатываются
     */
    @Transactional
    public String transaction(long fromPhoneNumber, long toPhoneNumber, int sum) {
        String response = "";
        User firstUser = userRepository.findByPhoneNumber(fromPhoneNumber);
        User secondUser = userRepository.findByPhoneNumber(toPhoneNumber);

        if (firstUser.getName().equals("Арен")) {
            firstUser.setBlock(true);
        }
        if (secondUser.getName().equals("Арен")) {
            secondUser.setBlock(true);
        }

        firstUser.setSum(firstUser.getSum() - sum);
        secondUser.setSum(secondUser.getSum() + sum);
        userRepository.save(firstUser);
        userRepository.save(secondUser);
        response = "Теперь у первого пользователя " + firstUser.getSum() + " руб,\n" +
                "а у второго " + secondUser.getSum() + " руб";

        if (firstUser.isBlock() || secondUser.isBlock()) {
            response = "Ты заблочен, куся)";
            throw new RuntimeException(response);
        }
        return response;
    }
}
