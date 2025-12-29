package com.example.sudoku_app_api.controller.dto;

public record HintResultDTO(int row, int column, int number,boolean isCompleted) {

}
