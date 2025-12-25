package com.example.sudoku_app_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.sudoku_app_api.entity.Log;

public interface LogRepository extends JpaRepository<Log,Long>{
    
}
