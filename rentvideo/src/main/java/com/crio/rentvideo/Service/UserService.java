package com.crio.rentvideo.Service;

import java.util.Optional;

import com.crio.rentvideo.Dto.RegisterRequest;

public interface UserService {
    public RegisterRequest createUser(RegisterRequest registerRequest);
    public Optional<RegisterRequest> getUserByEmail(String email);
}
