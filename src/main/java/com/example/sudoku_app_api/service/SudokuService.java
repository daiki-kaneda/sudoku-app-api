package com.example.sudoku_app_api.service;

import java.util.List;
import java.util.Map;

import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.sudoku_app_api.controller.dto.BoardProgressDTO;
import com.example.sudoku_app_api.controller.dto.CellInputResultDTO;
import com.example.sudoku_app_api.controller.dto.CellProgressDTO;
import com.example.sudoku_app_api.controller.dto.HintResultDTO;
import com.example.sudoku_app_api.entity.Board;
import com.example.sudoku_app_api.entity.Cell;
import com.example.sudoku_app_api.entity.Log;
import com.example.sudoku_app_api.entity.User;
import com.example.sudoku_app_api.entity.UserBoard;
import com.example.sudoku_app_api.entity.Log.LogStatus;
import com.example.sudoku_app_api.entity.UserBoard.UserBoardId;
import com.example.sudoku_app_api.repository.LogRepository;
import com.example.sudoku_app_api.repository.UserBoardQueryRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SudokuService {
    private final LogRepository logRepository;
    private final UserService userService;
    private final LogService logService;

    private final UserBoardQueryRepository ubQueryRepository;

    @Transactional
    public CellInputResultDTO tryInputToCell(
            Jwt jwt, Long boardId, int row, int column, int inputNumber) {
        User user = userService.getOrCreateUser(jwt);
        UserBoardId ubId = UserBoard.UserBoardId.create(user.getUid(), boardId);
        UserBoard userBoard = ubQueryRepository.findById(ubId)
                .orElseThrow(() -> new IllegalStateException("この数独はまだ開始されていません。"));
        Board board = userBoard.getBoard();
        Cell cell = board.cellAt(row, column);

        boolean isCorrect = board.tryValue(row, column, inputNumber);
        Log log = Log.create(
                userBoard,
                cell,
                inputNumber,
                isCorrect ? Log.LogStatus.SOLVED : Log.LogStatus.FAILED);
        logRepository.save(log);

        boolean isCompleted = false;
        if (isCorrect) {
            isCompleted = checkAndSetComplete(userBoard);
        }
        return new CellInputResultDTO(isCorrect, isCompleted);
    }

    @Transactional
    public BoardProgressDTO getGameProgress(Jwt jwt, Long boardId) {
        User user = userService.getOrCreateUser(jwt);
        Map<Cell.CellId, Log> successLogs = logService.successLogs(user.getUid(), boardId);

        return new BoardProgressDTO(
                successLogs
                        .values()
                        .stream()
                        .map(l -> CellProgressDTO.from(l))
                        .toList());
    }

    @Transactional
    public boolean checkAndSetComplete(
            UserBoard userBoard) {
        long uniqueSuccessCellsCount = logService.getUniqueSolvedCellsCount(userBoard.getUser().getUid(),
                userBoard.getBoard().getId());
        long emptyCellCount = userBoard.getBoard().getTotalEmptyCells();

        if (emptyCellCount == uniqueSuccessCellsCount) {
            userBoard.setStatus(UserBoard.UserBoardStatus.COMPLETED);
            return true;
        }
        return false;
    }

    @Transactional
    public HintResultDTO getHint(
            Jwt jwt, Long boardId) {
        User user = userService.getOrCreateUser(jwt);
        String uid = user.getUid();
        if (logService.hasRecentHint(uid, boardId)) {
            throw new IllegalStateException("ヒントは１分１回までです。");
        }

        Map<Cell.CellId,Log> successLogs = logService.successLogs(uid,boardId);

        UserBoard.UserBoardId id = UserBoard.UserBoardId.create(uid, boardId);
        UserBoard userBoard = ubQueryRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("ゲームが開始されていません。"));
        Cell emptyUnSolvedCells = userBoard.getBoard().getEmptyCells().stream() //元々からの空きマス
                .filter(c -> !successLogs.containsKey(c.getId())) // かつ成功していないマス
                .findAny()
                .orElseThrow(()->new IllegalStateException("ヒントを出せるマスがありません。"))
        ;

        Log hintUsedLog = Log.create(userBoard, emptyUnSolvedCells, null, LogStatus.HINT_USED);
        Log successLog = Log.create(userBoard, emptyUnSolvedCells, emptyUnSolvedCells.getCorrectValue(),
                LogStatus.SOLVED);

        logRepository.saveAll(List.of(hintUsedLog, successLog));

        boolean isCompleted = checkAndSetComplete(userBoard);

        return new HintResultDTO(
                emptyUnSolvedCells.getId().getRow(),
                emptyUnSolvedCells.getId().getColumn(),
                emptyUnSolvedCells.getCorrectValue(),
                isCompleted);
    }
}
