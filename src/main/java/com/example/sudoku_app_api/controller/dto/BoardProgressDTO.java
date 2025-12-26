package com.example.sudoku_app_api.controller.dto;

import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public record BoardProgressDTO(
        @NotNull @Valid List<CellProgressDTO> cells) {

}
