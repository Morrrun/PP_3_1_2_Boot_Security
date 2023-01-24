package ru.kata.spring.boot_security.demo.service.user;

import ru.kata.spring.boot_security.demo.model.User;

import java.util.List;

public interface UserService {
    User loadUserByEmail(String email);
    List<User> getAllUsers();
    User getUser(long id);
    void addUser(User user);
    void updateUser(User user);
    void deleteUser(long id);
}
