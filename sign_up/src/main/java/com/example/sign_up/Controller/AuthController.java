package com.example.sign_up.Controller;

import com.example.sign_up.Config.JwtUtil;
import com.example.sign_up.Dto.LoginRequest;
import com.example.sign_up.Dto.SignupRequest;
import com.example.sign_up.Entity.User;
import com.example.sign_up.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private final RestTemplate restTemplate = new RestTemplate(); // ✅ used to call CMS service

    // Signup
    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody SignupRequest signupRequest) {
        if (userRepository.findByUsername(signupRequest.getUsername()) != null) {
            return ResponseEntity.badRequest().body("Username already exists");
        }
        if (userRepository.findByEmail(signupRequest.getEmail()) != null) {
            return ResponseEntity.badRequest().body("Email already exists");
        }

        // ✅ save new user in SwiftTrack DB
        User user = new User();
        user.setUsername(signupRequest.getUsername());
        user.setEmail(signupRequest.getEmail());
        user.setPassword(passwordEncoder.encode(signupRequest.getPassword()));

        User savedUser = userRepository.save(user);

        // ✅ call CMS microservice to insert into clients table
        try {
            String cmsUrl = "http://localhost:8081/cms/client/create"
                    + "?clientId=" + savedUser.getId()
                    + "&clientName=" + savedUser.getUsername()
                    + "&email=" + savedUser.getEmail();

            restTemplate.postForObject(cmsUrl, null, String.class);
        } catch (Exception e) {
            e.printStackTrace();
            // ⚠️ You may choose to rollback user creation if CMS fails, or just log error
            return ResponseEntity.status(500).body("User created but failed to register client in CMS");
        }

        return ResponseEntity.ok("User registered successfully!");
    }

    // Login
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        User user = userRepository.findByUsername(loginRequest.getUsername());
        if (user == null) return ResponseEntity.status(401).body(Map.of("error", "User not found"));

        if (passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            String token = JwtUtil.generateToken(user.getUsername());

            // Return JSON with token + clientId
            return ResponseEntity.ok(Map.of(
                    "token", token,
                    "clientId", user.getId()  // assuming User entity has getId()
            ));
        } else {
            return ResponseEntity.status(401).body(Map.of("error", "Invalid password"));
        }
    }
}
