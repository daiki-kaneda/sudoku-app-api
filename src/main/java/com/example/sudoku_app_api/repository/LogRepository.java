package com.example.sudoku_app_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.sudoku_app_api.entity.Log;
import com.example.sudoku_app_api.entity.UserBoard;

public interface LogRepository extends JpaRepository<Log, Long> {
    /**
     * UserBoardId(uid,boardId)に紐づくすべてのログを削除する
     */
    @Modifying
    @Query("DELETE FROM Log l WHERE l.userBoard.id = :id")
    void deleteByUserBoardId(@Param("id") UserBoard.UserBoardId id);
}
