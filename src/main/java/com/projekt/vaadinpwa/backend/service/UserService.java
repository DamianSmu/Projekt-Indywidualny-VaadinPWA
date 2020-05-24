package com.projekt.vaadinpwa.backend.service;

import com.projekt.vaadinpwa.backend.entity.UserEntity;
import com.projekt.vaadinpwa.backend.repository.UserRepository;
import com.projekt.vaadinpwa.backend.security.SecurityUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, @Lazy PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<UserEntity> findAll() {
        return userRepository.findAll();
    }

    public Optional<UserEntity> findByUserName(String userName) {
        return userRepository.findByUserName(userName);
    }

    public Optional<UserEntity> findByUserEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public void saveUser(String userName, String email, String password) {
        UserEntity newUser = new UserEntity();
        newUser.setUserName(userName);
        newUser.setEmail(email);
        newUser.setPassword(passwordEncoder.encode(password));
        userRepository.save(newUser);
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<UserEntity> optUser = userRepository.findByUserName(username);
        UserEntity user;
        if (optUser.isPresent()) {
            user = optUser.get();
        } else {
            throw new UsernameNotFoundException("User: " + username + " not found");
        }
        Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
        grantedAuthorities.add(new SimpleGrantedAuthority("USER"));
        return new User(user.getUserName(), user.getPassword(), grantedAuthorities);
    }

    public Optional<UserEntity> getLoggedUser() {
        return findByUserName(SecurityUtils.getLoggedUserName());
    }
}
