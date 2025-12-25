package com.example.sudoku_app_api.repository;

import org.springframework.data.repository.Repository;

import com.example.sudoku_app_api.entity.UserBoard;
import java.util.Optional;


public interface UserBoardQueryRepository extends Repository<UserBoard,UserBoard.UserBoardId>{
    Optional<UserBoard> findById(UserBoard.UserBoardId id);

    long countByIdUidAndStatus(String uid,UserBoard.UserBoardStatus status);
}
