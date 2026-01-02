package com.example.sudoku_app_api.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

import com.example.sudoku_app_api.entity.Board;
import java.util.Optional;


public interface BoardQueryRepository extends Repository<Board, Long> {
    @Query("SELECT b FROM Board b WHERE b.name LIKE %:name%")
    Page<Board> findByName(String name, Pageable pageable);
    Optional<Board> findById(Long id);
}
