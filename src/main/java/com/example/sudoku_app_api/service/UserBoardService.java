package com.example.sudoku_app_api.service;

import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.sudoku_app_api.config.properties.SudokuGameProperties;
import com.example.sudoku_app_api.entity.Board;
import com.example.sudoku_app_api.entity.User;
import com.example.sudoku_app_api.entity.UserBoard;
import com.example.sudoku_app_api.repository.BoardQueryRepository;
import com.example.sudoku_app_api.repository.LogRepository;
import com.example.sudoku_app_api.repository.UserBoardQueryRepository;
import com.example.sudoku_app_api.repository.UserBoardRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserBoardService {

    private final LogRepository logRepository;
    private final UserService userService;
    private final BoardQueryRepository bQueryRepository;
    private final UserBoardRepository ubRepository;
    private final UserBoardQueryRepository ubQueryRepository;
    private final SudokuGameProperties sudokuGameProperties;

    @Transactional
    public void addNewBoardByUser(
            Jwt jwt,
            Long boardId) {
        User user = userService.getOrCreateUser(jwt);
        Board board = bQueryRepository.findById(boardId)
                .orElseThrow(() -> new EntityNotFoundException("ボードが見つかりません。ID: " + boardId));
        String uid = user.getUid();
        if (existsTooManyUserBoardInProgress(uid)) {
            throw new IllegalArgumentException("同時に取り組める数独は" + sudokuGameProperties.getMaxInProgress() + "つまでです。");
        }
        UserBoard ub = UserBoard.create(user, board);
        ubRepository.save(ub);
    }

    @Transactional
    public void deleteBoardByUser(
            Jwt jwt, Long boardId) {
        User user = userService.getOrCreateUser(jwt);
        UserBoard.UserBoardId id = UserBoard.UserBoardId.create(user.getUid(), boardId);

        logRepository.deleteByUserBoardId(id);
        ubRepository.deleteById(id);
    }

    private boolean existsTooManyUserBoardInProgress(String uid) {
        return ubQueryRepository.countByIdUidAndStatus(
                uid,
                UserBoard.UserBoardStatus.IN_PROGRESS) >= sudokuGameProperties.getMaxInProgress();
    }
}
