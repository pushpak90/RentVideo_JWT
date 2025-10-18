package com.crio.rentvideo.Controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import com.crio.rentvideo.Dto.AuthRequest;
import com.crio.rentvideo.Dto.AuthResponse;
import com.crio.rentvideo.Dto.RegisterRequest;
import com.crio.rentvideo.Entity.Role;
import com.crio.rentvideo.Security.JwtUtil;
import com.crio.rentvideo.Service.UserDetailsServiceImpl;
import com.crio.rentvideo.Service.UserService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    private final AuthenticationManager authenticationManager;
    @Autowired
    private UserService userService;
    @Autowired
    private UserDetailsServiceImpl userDetailsService;
    @Autowired
    private JwtUtil jwtUtil;

    AuthController(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/register")
    public ResponseEntity<RegisterRequest> register(@RequestBody RegisterRequest registerRequest) {
        if(registerRequest.getRole() == null){
            registerRequest.setRole(Role.CUSTOMER);
        }
        return ResponseEntity.ok(userService.createUser(registerRequest));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest request) {
        authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

        final UserDetails userDetails = userDetailsService.loadUserByUsername(request.getEmail());
        final String token = jwtUtil.generateToken(userDetails);
        Optional<RegisterRequest> req = userService.getUserByEmail(request.getEmail().toString());
        Role role = req.map(RegisterRequest::getRole).orElse(Role.CUSTOMER);

        return ResponseEntity.ok(new AuthResponse(token, role));
    }

}
