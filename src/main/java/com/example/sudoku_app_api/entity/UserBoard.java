package com.example.sudoku_app_api.entity;

import java.io.Serializable;
import java.time.Instant;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserBoard extends BaseEntity<UserBoard.UserBoardId> {
    @EmbeddedId
    private UserBoardId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("uid")
    @JoinColumn(name = "uid")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("boardId")
    @JoinColumn(name = "board_id")
    private Board board;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private UserBoardStatus status;

    private Instant lastHintUsed;

    public static UserBoard create(User user, Board board) {
        UserBoardId id = UserBoardId.create(user.getUid(), board.getId());
        UserBoard userBoard = new UserBoard();
        userBoard.id = id;
        userBoard.user = user;
        userBoard.board = board;
        userBoard.status = UserBoardStatus.IN_PROGRESS;
        return userBoard;
    }

    @Embeddable
    @EqualsAndHashCode
    public static class UserBoardId implements Serializable {
        @Column(name = "uid")
        private String uid;
        @Column(name = "board_id")
        private Long boardId;

        public static UserBoardId create(String uid, Long boardId) {
            UserBoardId userBoardId = new UserBoardId();
            userBoardId.uid = uid;
            userBoardId.boardId = boardId;
            return userBoardId;
        }
    }

    public static enum UserBoardStatus {
        IN_PROGRESS, COMPLETED;
    }
}
