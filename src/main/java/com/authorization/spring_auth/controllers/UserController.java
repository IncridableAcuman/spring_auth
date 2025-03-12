package com.authorization.spring_auth.controllers;
import com.authorization.spring_auth.dtos.AuthRequest;
import com.authorization.spring_auth.dtos.AuthResponse;
import com.authorization.spring_auth.models.User;
import com.authorization.spring_auth.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody AuthRequest request){
       return userService.register(request);
    }
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest request){
        return userService.login(request);
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refresh(@RequestParam String refreshToken){
        return userService.refresh(refreshToken);
    }
    @GetMapping("/all-users")
    public ResponseEntity<List<User>> getAllUsers(){
        return userService.getAllUsers();
    }
}
