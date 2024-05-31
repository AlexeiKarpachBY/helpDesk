package com.training.akarpach.helpDesk.service;

import com.training.akarpach.helpDesk.enums.Role;
import com.training.akarpach.helpDesk.model.User;

import java.util.List;

public interface UserService {

    User getCurrentUser();

    User findUserByEmail(String email);

    List<User> getAllUserByRole(Role role);

    User findByEmailAndPassword(String email, String password);

}
