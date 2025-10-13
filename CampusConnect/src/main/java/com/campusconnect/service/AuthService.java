package com.campusconnect.service;

import java.sql.SQLException;

import com.campusconnect.model.User;
import com.campusconnect.repository.UserRepository;
import com.campusconnect.util.HashUtil;
import com.campusconnect.util.ValidationUtil;

public class AuthService {
    private final UserRepository userRepository = new UserRepository();

    public boolean register(String name, String email, String password, String role) throws SQLException {
        if (!ValidationUtil.isValidEmail(email) || !ValidationUtil.isValidPassword(password)) {
            return false;
        }

        User existing = userRepository.findByEmail(email);
        if (existing != null) {
            return false;
        }

        User user = new User(name, email, HashUtil.hashPassword(password), role);
        userRepository.save(user);
        return true;
    }

    public User login(String email, String password) throws SQLException {
        User user = userRepository.findByEmail(email);
        if (user != null && user.getPassword().equals(HashUtil.hashPassword(password))) {
            return user;
        }
        return null;
    }
    
    public User findUserById(int userId) throws SQLException {
        return userRepository.findById(userId);
    }
}
