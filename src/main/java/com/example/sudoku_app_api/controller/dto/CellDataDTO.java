package com.example.sudoku_app_api.controller.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

public record CellDataDTO(
        @Min(value = 0) @Max(value = 8) int row,
        @Min(value = 0) @Max(value = 8) int column,
        @Min(value = 1) @Max(value = 9) Integer initialValue,
        @Min(value = 1) @Max(value = 9) Integer correctValue) {

}
