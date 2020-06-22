package com.sepfort.sheet.service.impl;

import com.sepfort.sheet.domain.Role;
import com.sepfort.sheet.domain.User;
import com.sepfort.sheet.repo.UserRepo;
import com.sepfort.sheet.service.UserService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.Collections;

@Service
public class UserServiceImpl implements UserDetailsService, UserService {
    private UserRepo userRepo;
    private PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepo userRepo, PasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepo.findByUsername(username);
    }

    @Override
    public boolean addUser(User user, Model model) {
        User userFromDB = userRepo.findByUsername(user.getUsername());
        if (userFromDB != null) {
            return false;
        }
        user.setActive(true);
        user.setRoles(Collections.singleton(Role.USER));
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepo.save(user);
        return true;
    }
}
