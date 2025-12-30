package com.example.sudoku_app_api.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.sudoku_app_api.controller.dto.BoardProgressDTO;
import com.example.sudoku_app_api.controller.dto.CellInputRequest;
import com.example.sudoku_app_api.controller.dto.CellInputResultDTO;
import com.example.sudoku_app_api.controller.dto.HintResultDTO;
import com.example.sudoku_app_api.service.SudokuService;
import com.example.sudoku_app_api.service.UserBoardService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequestMapping("/api/sudoku")
@RequiredArgsConstructor
public class SudokuPlayController {
    private final UserBoardService userBoardService;
    private final SudokuService sudokuService;

    // 自分が取り組む数独を追加
    @PostMapping("/{boardId}/start")
    public ResponseEntity<Void> start(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable Long boardId) {
        userBoardService.addNewBoardByUser(jwt, boardId);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    // 数独の途中経過を復元
    @GetMapping("/{boardId}/progress")
    public ResponseEntity<BoardProgressDTO> getProgress(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable Long boardId) {
        return ResponseEntity.ok(sudokuService.getGameProgress(jwt, boardId));
    }

    // 数独に数字を入力
    @PostMapping("/{boardId}/input")
    public ResponseEntity<CellInputResultDTO> input(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable Long boardId,
            @RequestBody @Valid CellInputRequest request) {
        return ResponseEntity.ok(
                sudokuService.tryInputToCell(
                        jwt,
                        boardId,
                        request.row(),
                        request.column(),
                        request.inputNumber()));
    }

    // 数独のヒントを求める（乱用防止のため1分間のインターバル）
    @PostMapping("/{boardId}/hint")
    public ResponseEntity<HintResultDTO> getHint(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable Long boardId) {
        return ResponseEntity.ok(
                sudokuService.getHint(jwt, boardId));
    }

    // 自分が取り組む数独を削除（進捗・状態をリセット）
    @DeleteMapping("/{boardId}")
    public ResponseEntity<Void> deleteBoard(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable Long boardId) {
        userBoardService.deleteBoardByUser(jwt, boardId);
        return ResponseEntity.noContent().build();
    }
}
