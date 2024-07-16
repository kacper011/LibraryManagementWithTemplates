package com.example.library.service;

import com.example.library.model.User;
import org.springframework.security.core.userdetails.UserDetails;


public interface UserService {

    void saveUser(User user);

    UserDetails loadUserByName(String name);
}
