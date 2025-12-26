package com.example.sudoku_app_api.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.sudoku_app_api.controller.dto.BoardDataDTO;
import com.example.sudoku_app_api.controller.dto.CellDataDTO;
import com.example.sudoku_app_api.repository.BoardQueryRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BoardService {
    private final BoardQueryRepository boardQueryRepository;

    public Page<BoardDataDTO> searchByName(String name, Pageable pageable) {
        return boardQueryRepository.findByName(name, pageable)
                .map(b -> new BoardDataDTO(
                        b.getId(),
                        b.getName(),
                        b.getCells()
                                .stream()
                                .map(c -> new CellDataDTO(c.getId().getRow(), c.getId().getColumn(),
                                        c.getInitialValue(), c.getCorrectValue()))
                                .toList()));
    }
}
