package com.example.sudoku_app_api.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

import com.example.sudoku_app_api.entity.Log;
import com.example.sudoku_app_api.entity.UserBoard.UserBoardId;

import java.time.Instant;
import java.util.List;

public interface LogQueryRepository extends Repository<Log, Long> {
        @Query("SELECT l FROM Log l " +
                        "WHERE l.userBoard.id.uid = :uid " +
                        "AND l.userBoard.id.boardId = :boardId " +
                        "AND l.status = :status " +
                        "ORDER BY l.createdAt ASC")
        List<Log> findByUidAndBoardIdAndStatus(@Param("uid") String uid, @Param("boardId") Long boardId,
                        Log.LogStatus status);

        @Query("SELECT COUNT(l)>0 FROM Log l WHERE l.userBoard.id = :id AND l.status = 'HINT_USED' AND l.createdAt > :since")
        boolean existsRecentHint(@Param("id") UserBoardId id, @Param("since") Instant since);

        @Query("SELECT COUNT(DISTINCT l.cell.id) FROM Log l " +
                        "WHERE l.userBoard.id.uid = :uid " +
                        "AND l.userBoard.id.boardId = :boardId " +
                        "AND l.status = :status")
        long countUniqueSolvedCells(
                        @Param("uid") String uid,
                        @Param("boardId") Long boardId,
                        @Param("status") Log.LogStatus status);

        default long countUniqueSolvedCells(String uid, Long boardId) {
                return countUniqueSolvedCells(uid, boardId, Log.LogStatus.SOLVED);
        }
}
