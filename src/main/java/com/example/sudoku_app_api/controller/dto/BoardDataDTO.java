package com.example.sudoku_app_api.controller.dto;

import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public record BoardDataDTO(
    Long id, 
    String name, 
    @NotNull @Valid List<CellDataDTO> cells) {

}
