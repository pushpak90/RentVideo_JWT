package com.crio.rentvideo.Service;

import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.crio.rentvideo.Dto.RegisterRequest;
import com.crio.rentvideo.Entity.User;
import com.crio.rentvideo.Repository.UserRepository;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    ModelMapper modelMapper = new ModelMapper();

    @Override
    public RegisterRequest createUser(RegisterRequest registerRequest) {
        registerRequest.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        User user = modelMapper.map(registerRequest, User.class);
        User saved = userRepository.save(user);
        return modelMapper.map(saved, RegisterRequest.class);
    }

    @Override
    public Optional<RegisterRequest> getUserByEmail(String email) {
        Optional<User> user = userRepository.findByEmail(email);
        return user.map(u -> modelMapper.map(u, RegisterRequest.class));
    }

}
