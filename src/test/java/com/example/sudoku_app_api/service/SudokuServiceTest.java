package com.example.sudoku_app_api.service;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.oauth2.jwt.Jwt;

import com.example.sudoku_app_api.entity.Board;
import com.example.sudoku_app_api.entity.User;
import com.example.sudoku_app_api.entity.UserBoard;
import com.example.sudoku_app_api.repository.LogRepository;
import com.example.sudoku_app_api.repository.UserBoardQueryRepository;

@ExtendWith(MockitoExtension.class)
class SudokuServiceTest {

    @InjectMocks
    private SudokuService sudokuService;

    @Mock
    private LogService logService;
    @Mock
    private UserBoardQueryRepository ubQueryRepository;
    @Mock
    private LogRepository logRepository;
    @Mock
    private UserService userService;

@Test
@DisplayName("ヒント：1分以内に使用された場合、例外が投げられること")
void getHint_ShouldThrowException_WhenHintUsedWithinOneMinute() {
    // 1. 準備 (Arrange)
    String uid = "user-123";
    Long boardId = 1L;
    Jwt jwt = mock(Jwt.class);

    User mockUser = mock(User.class);
    when(mockUser.getUid()).thenReturn(uid);
    when(userService.getOrCreateUser(jwt)).thenReturn(mockUser);
    when(logService.hasRecentHint(uid, boardId)).thenReturn(true);

    // 2. 実行と検証 (Act & Assert)
    assertThrows(IllegalStateException.class, () -> {
        sudokuService.getHint(jwt, boardId);
    });
}

@Test
@DisplayName("完了判定：全ての空きマスが埋まった場合、ステータスが COMPLETED に更新されること")
void checkAndSetComplete_ShouldSetStatusToCompleted_WhenAllCellsAreSolved() {
    // 1. 準備 (Arrange)
    String uid = "user-123";
    Long boardId = 1L;

    // UserBoard, User, Board のモック作成
    UserBoard userBoard = mock(UserBoard.class);
    User user = mock(User.class);
    Board board = mock(Board.class);
    
    when(userBoard.getUser()).thenReturn(user);
    when(user.getUid()).thenReturn(uid);
    when(userBoard.getBoard()).thenReturn(board);
    when(board.getId()).thenReturn(boardId);

    when(board.getTotalEmptyCells()).thenReturn(2L);

    when(logService.getUniqueSolvedCellsCount(uid, boardId)).thenReturn(2L);

    // 2. 実行 (Act)
    boolean result = sudokuService.checkAndSetComplete(userBoard);

    // 3. 検証 (Assert)
    assertTrue(result, "完了した場合は true が返ること");
    
    verify(userBoard).setStatus(UserBoard.UserBoardStatus.COMPLETED);
}
}
