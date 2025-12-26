package com.example.sudoku_app_api.service;

import java.util.Objects;

import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.sudoku_app_api.entity.User;
import com.example.sudoku_app_api.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;

    @Transactional
    public User getOrCreateUser(Jwt jwt) {
        String uid = jwt.getSubject();
        String maybeName = jwt.getClaimAsString("name");
        String maybeEmail = jwt.getClaimAsString("email");
        String name = maybeName != null ? maybeName : "Unknown User";

        return userRepository.findById(uid)
                .map(user -> {
                    if (!Objects.equals(user.getName(), name)) {
                        user.setName(name);
                    }
                    if (!Objects.equals(user.getEmail(), maybeEmail)) {
                        user.setEmail(maybeEmail);
                    }
                    return user;
                })
                .orElseGet(() -> userRepository.save(
                        User.create(uid, name, maybeEmail)));
    }
}
