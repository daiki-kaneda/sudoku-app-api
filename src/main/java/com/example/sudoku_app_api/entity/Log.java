package com.example.sudoku_app_api.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinColumns;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Log extends BaseEntity<Long> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumns({
            @JoinColumn(name = "uid", referencedColumnName = "uid"),
            @JoinColumn(name = "board_id", referencedColumnName = "board_id") })
    private UserBoard userBoard;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumns({
            @JoinColumn(name = "board_id", referencedColumnName = "board_id", insertable = false, updatable = false),
            @JoinColumn(name = "row_idx", referencedColumnName = "row_idx", insertable = false, updatable = false),
            @JoinColumn(name = "col_idx", referencedColumnName = "col_idx", insertable = false, updatable = false) })
    private Cell cell;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private LogStatus status; // solved,failed,hintUsed

    private Integer inputNumber;

    public static Log create(
            UserBoard userBoard,
            Cell cell,
            Integer inputValue,
            LogStatus status) {
        Log log = new Log();
        log.userBoard = userBoard;
        log.cell = cell;
        log.inputNumber = inputValue;
        log.status = status;
        return log;
    }

    public static enum LogStatus {
        SOLVED,
        FAILED,
        HINT_USED;
    }

}
