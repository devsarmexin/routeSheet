package com.sepfort.sheet.service;

import com.sepfort.sheet.domain.User;
import com.sepfort.sheet.exception.UserException;

public interface UserService {
    void addUser(User user) throws UserException;
}
