package com.example.sudoku_app_api.repository;

import org.springframework.data.repository.Repository;

import com.example.sudoku_app_api.entity.User;
import java.util.Optional;


public interface UserQueryRepository extends Repository<User, String> {
    Optional<User> findById(String id);
}
