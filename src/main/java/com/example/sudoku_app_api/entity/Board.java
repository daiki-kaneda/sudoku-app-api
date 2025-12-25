package com.example.sudoku_app_api.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.hibernate.annotations.BatchSize;

import com.example.sudoku_app_api.entity.UserBoard.UserBoardId;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Board extends BaseEntity<Long> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, orphanRemoval = true)
    @BatchSize(size = 10) // 数独の一覧のページングにおけるN+1問題を軽減
    private List<Cell> cells = new ArrayList<>();

    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserBoard> userBoards = new ArrayList<>();

    public boolean tryValue(int row, int column, int value) {
        if (row < 0 || row > 8 || column < 0 || column > 8 || value < 1 || value > 9) {
            throw new IllegalArgumentException("Must be 0<=row,column<=8, 1<=value<=9");
        }
        Map<Cell.CellId, Cell> cellMap = cells
                .stream()
                .collect(Collectors.toMap(c -> c.getId(), c -> c));

        Cell.CellId targetId = Cell.CellId.create(this.id, row, column);
        Cell cell = Optional.ofNullable(cellMap.get(targetId))
                .orElseThrow(() -> new IllegalStateException("Cell data is missing"));
        return cell.getCorrectValue() == value;
    }

    public UserBoardId createNewUserBoard(
            User user) {
        UserBoard newUserBoard = UserBoard.create(user, this);
        this.userBoards.add(newUserBoard);
        return newUserBoard.getId();
    }
}
