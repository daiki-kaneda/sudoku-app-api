package com.example.sudoku_app_api.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;

@Component
@Data
@ConfigurationProperties(prefix = "sudoku.game")
public class SudokuGameProperties {
    /** 同時に進行可能な数独の最大数 */
    private int maxInProgress = 3;
    private int hintIntervalMinutes = 1;
}
