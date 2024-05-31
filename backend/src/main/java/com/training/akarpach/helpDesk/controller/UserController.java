package com.training.akarpach.helpDesk.controller;

import com.training.akarpach.helpDesk.enums.Role;
import com.training.akarpach.helpDesk.model.User;
import com.training.akarpach.helpDesk.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<Role> getAllUserTickets() {

        User user = userService.getCurrentUser();

        return ResponseEntity.status(OK).body(user.getRole());
    }

}
