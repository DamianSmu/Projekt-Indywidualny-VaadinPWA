package com.projekt.vaadinpwa.backend.service;

import com.projekt.vaadinpwa.backend.entity.User;
import com.projekt.vaadinpwa.backend.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }
}
