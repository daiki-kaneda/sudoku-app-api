package com.example.sudoku_app_api.controller.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

public record CellInputRequest(
    @Min(0)
    @Max(8)
    int row, 
    @Min(0)
    @Max(8)
    int column, 
    @Min(1)
    @Max(9)
    int inputNumber) {
    
}
