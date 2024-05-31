package com.helpDesk.service.impl;

import com.helpDesk.dao.impl.UserDao;
import com.helpDesk.enums.Role;
import com.helpDesk.exception.UserNotFoundException;
import com.helpDesk.model.User;
import com.helpDesk.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final UserDao userDao;

    private final PasswordEncoder encoder;

    @Autowired
    public UserServiceImpl(UserDao userDao, PasswordEncoder encoder) {
        this.userDao = userDao;
        this.encoder = encoder;
    }

    @Override
    @Transactional
    public List<User> getAllUserByRole(Role role) {

        return userDao.getAllUsersByRole(role);

    }

    @Override
    public User findUserByEmail(String email) {

        return userDao.findUserByEmail(email).orElseThrow();

    }

    @Override
    public User findByEmailAndPassword(String email, String password) {

        User user = findUserByEmail(email);

        if (user != null) {
            if (encoder.matches(password, user.getPassword())) {
                return user;
            } else throw new UserNotFoundException("User not found");
        }

        return null;
    }

    @Override
    public User getCurrentUser() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        return findUserByEmail(email);
    }

}
