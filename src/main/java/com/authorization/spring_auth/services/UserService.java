package com.authorization.spring_auth.services;

import com.authorization.spring_auth.dtos.AuthRequest;
import com.authorization.spring_auth.dtos.AuthResponse;
import com.authorization.spring_auth.models.User;
import com.authorization.spring_auth.repositories.UserRepository;
import com.authorization.spring_auth.security.JWTUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JWTUtil jwtUtil;

    public ResponseEntity<String> register(AuthRequest request){
        if(userRepository.findByEmail(request.getEmail()).isPresent()){
            return ResponseEntity.badRequest().body("User already exist");
        }
        User user=User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(request.getRole())
                .build();
        userRepository.save(user);
        return ResponseEntity.ok("User created");
    }

    public ResponseEntity<AuthResponse> login(AuthRequest request){
        Optional<User> optionalUser=userRepository.findByEmail(request.getEmail());
        if(optionalUser.isEmpty()){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
        User user=optionalUser.get();
        if(!passwordEncoder.matches(request.getPassword(),user.getPassword())){
            return ResponseEntity.badRequest().body(null);
        }
        String accessToken=jwtUtil.generateAccessToken(user.getEmail());
        String refreshToken=jwtUtil.generateRefreshToken(user.getEmail());
        return ResponseEntity.ok(new AuthResponse(accessToken,refreshToken));
    }

    public ResponseEntity<AuthResponse> refresh(String refreshToken){
        if(!jwtUtil.validateToken(refreshToken)){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
        String email=jwtUtil.extractEmail(refreshToken);
        Optional<User> user=userRepository.findByEmail(email);
        if(user.isEmpty()){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
        String newAccessToken=jwtUtil.generateAccessToken(email);
        return ResponseEntity.ok(new AuthResponse(newAccessToken,refreshToken));
    }

    public ResponseEntity<List<User>> getAllUsers(){
        return ResponseEntity.ok(userRepository.findAll());
    }

}
