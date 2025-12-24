package com.example.sudoku_app_api.entity;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Cell extends BaseEntity<Cell.CellId> {
    @EmbeddedId
    private CellId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("boardId")
    @JoinColumn(name = "board_id")
    private Board board;

    @Min(value = 1)
    @Max(value = 9)
    private Integer initialValue;
    @Min(value = 1)
    @Max(value = 9)
    private Integer correctValue;

    @Data
    @Embeddable
    public static class CellId implements Serializable {
        @Column(name = "board_id")
        private Long boardId;

        @Column(name = "row_idx")
        @Min(value = 0)
        @Max(value = 8)
        private int row;

        @Column(name = "col_idx")
        @Min(value = 0)
        @Max(value = 8)
        private int column;

        public static CellId create(Long boardId, int row, int column) {
            CellId id = new CellId();
            id.boardId = boardId;
            id.row = row;
            id.column = column;
            return id;
        }
    }
}
