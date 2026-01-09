package com.pocketpipo.pocketpipo.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.pocketpipo.pocketpipo.dto.CreateUserRequestDTO;
import com.pocketpipo.pocketpipo.entity.User;
import com.pocketpipo.pocketpipo.exception.DuplicatedEmailException;
import com.pocketpipo.pocketpipo.repository.UserRepository;

@Service
public class UserService {
    private UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    
    public void createUser(CreateUserRequestDTO userRequestDTO) {
         if (userRepository.findByEmail(userRequestDTO.getEmail()).isPresent()) {
            throw new DuplicatedEmailException("This email is already being used by another user.");
        }
        final String hashedPassword = passwordEncoder.encode(userRequestDTO.getPassword());
        User user = new User(userRequestDTO.getEmail(), userRequestDTO.getUsername(), hashedPassword);
        this.userRepository.save(user);
    }


}
