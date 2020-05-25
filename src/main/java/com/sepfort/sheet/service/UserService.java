package com.sepfort.sheet.service;

import com.sepfort.sheet.domain.User;
import org.springframework.ui.Model;

public interface UserService {
    boolean addUser(User user, Model model);
}
