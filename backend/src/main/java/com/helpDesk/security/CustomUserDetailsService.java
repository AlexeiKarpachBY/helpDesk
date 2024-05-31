package com.helpDesk.security;

import com.helpDesk.service.UserService;
import com.helpDesk.model.User;
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
