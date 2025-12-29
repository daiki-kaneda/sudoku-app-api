package com.example.sudoku_app_api.service;

import java.time.Duration;
import java.time.Instant;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.sudoku_app_api.entity.Cell;
import com.example.sudoku_app_api.entity.Log;
import com.example.sudoku_app_api.entity.UserBoard;
import com.example.sudoku_app_api.repository.LogQueryRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LogService {
    private final LogQueryRepository logQueryRepository;

    public Map<Cell.CellId, Log> successLogs(String uid, Long boardId) {
        return logQueryRepository.findByUidAndBoardIdAndStatus(
                uid,
                boardId,
                Log.LogStatus.SOLVED)
                .stream()
                .collect(
                        Collectors.toMap(
                                l -> l.getCell().getId(),
                                Function.identity(),
                                (a, b) -> (a.getCreatedAt().isAfter(b.getCreatedAt())) ? a : b));
    }

    public long getUniqueSolvedCellsCount(String uid, Long boardId) {
        return logQueryRepository.countUniqueSolvedCells(uid, boardId);
    }

    public boolean hasRecentHint(String uid, Long boardId) {
        UserBoard.UserBoardId id = UserBoard.UserBoardId.create(uid, boardId);
        Instant oneMinuteBefore = Instant.now().minus(Duration.ofMinutes(1));

        return logQueryRepository.existsRecentHint(id, oneMinuteBefore);
    }
}
