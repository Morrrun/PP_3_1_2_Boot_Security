package ru.kata.spring.boot_security.demo.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.user.UserServiceImpl;

@Component
public class UserValidator implements Validator {

    private final UserServiceImpl userServiceImpl;

    @Autowired
    public UserValidator(UserServiceImpl userServiceImpl) {
        this.userServiceImpl = userServiceImpl;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return User.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        User userObj = (User) target;

        User user = userServiceImpl.loadUserByEmail(userObj.getEmail());

        if (userObj.getFirstName().isEmpty()) {
            errors.rejectValue("firstName", "", "Укажите имя!");

        } else if (userObj.getFirstName().length() > 30 || userObj.getFirstName().length() < 2) {
            errors.rejectValue("firstName", "",
                    "Имя должно быть от 2-х до 30 символов длинной");
        }


        if (userObj.getLastName().isEmpty()) {
            errors.rejectValue("lastName", "", "Укажите фамилию!");

        } else if (userObj.getLastName().length() > 30 || userObj.getLastName().length() < 2) {
            errors.rejectValue("lastName", "",
                    "Фамилия должна быть от 2-х до 30 символов длинной");
        }


        if (userObj.getEmail().isEmpty()) {
            errors.rejectValue("email", "", "Укажите email!");

        } else if (user != null) {
            errors.rejectValue("email", "",
                    "Человек с таким email уже зарегестрирован!");

        } else if (userObj.getEmail().length() < 10 || userObj.getEmail().length() > 30 ) {
            errors.rejectValue("email", "",
                    "Email должнен быть от 10 до 30 символов длинной");
        }


        if (userObj.getPassword().isEmpty()) {
            errors.rejectValue("password", "", "Укажите пароль!");

        } else if (userObj.getPassword().length() > 100 || userObj.getPassword().length() < 5) {
            errors.rejectValue("password", "",
                    "Пароль должен быть от 5 до 100 символов длинной");
        }


    }
}
