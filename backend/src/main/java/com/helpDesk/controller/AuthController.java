package com.helpDesk.controller;

import com.helpDesk.model.security.AuthRequest;
import com.helpDesk.model.security.SecurityToken;
import com.helpDesk.service.UserService;
import com.helpDesk.security.jwt.JwtProvider;
import com.helpDesk.model.User;
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
