package com.example.sudoku_app_api.controller.dto;

import com.example.sudoku_app_api.entity.Cell;
import com.example.sudoku_app_api.entity.Log;

public record CellProgressDTO(
        int row, int column, int inputNumber) {

    public static CellProgressDTO from(Log log) {
        Cell cell = log.getCell();
        return new CellProgressDTO(
            cell.getId().getRow(), 
            cell.getId().getColumn(), 
            log.getInputNumber());
    }
}
