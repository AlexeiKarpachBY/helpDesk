package com.helpDesk.service;

import com.helpDesk.enums.Role;
import com.helpDesk.model.User;

import java.util.List;

public interface UserService {

    User getCurrentUser();

    User findUserByEmail(String email);

    List<User> getAllUserByRole(Role role);

    User findByEmailAndPassword(String email, String password);

}
