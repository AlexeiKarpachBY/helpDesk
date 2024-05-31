package com.training.akarpach.helpDesk.controller;

import com.training.akarpach.helpDesk.security.jwt.JwtProvider;
import com.training.akarpach.helpDesk.model.User;
import com.training.akarpach.helpDesk.model.security.AuthRequest;
import com.training.akarpach.helpDesk.model.security.SecurityToken;
import com.training.akarpach.helpDesk.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;
    private final JwtProvider jwtProvider;

    @Autowired
    public AuthController(UserService userService, JwtProvider jwtProvider) {
        this.userService = userService;
        this.jwtProvider = jwtProvider;
    }

    @PostMapping
    public ResponseEntity<SecurityToken> auth(@Valid @RequestBody AuthRequest request) {

        User user = userService.findByEmailAndPassword(request.getLogin(), request.getPassword());
        SecurityToken token = new SecurityToken(jwtProvider.generateToken(user.getEmail()));

        return ResponseEntity.ok().body(token);
    }
}
