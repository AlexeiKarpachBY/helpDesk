package com.training.akarpach.helpDesk.security;

import com.training.akarpach.helpDesk.model.User;
import com.training.akarpach.helpDesk.service.UserService;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class CustomUserDetailsService implements UserDetailsService {

    private final UserService userService;

    public CustomUserDetailsService(UserService userService) {
        this.userService = userService;
    }

    @Override
    public CustomUserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        User user = userService.findUserByEmail(email);

        return CustomUserDetails.fromUserEntityToCustomUserDetails(user);

    }
}
