package com.example.library.service;

import com.example.library.model.User;


public interface UserService {

    void saveUser(User user);

    void updateEmail(Long id, String newEmail);
    void updateUsername(Long id, String newUsername);
    void updatePassword(Long id, String newPassword);
    void updateProfil(Long id, String newUsername, String newEmail, String newPassword);
    void deleteUserById(Long id);
}
