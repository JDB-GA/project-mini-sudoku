package com.sudoku.system.service;

import com.sudoku.system.constant.ErrorConstants;
import com.sudoku.system.constant.FileConstants;
import com.sudoku.system.constant.SudokuConstants;
import com.sudoku.system.exception.InvalidSudokuException;
import org.springframework.stereotype.Service;

@Service
public class SudokuService {

    private record Cell(int row, int col) {
    }

    private final SudokuStorageService storageService;

    public SudokuService(SudokuStorageService storageService) {
        this.storageService = storageService;
    }

    public void solve(String id) {
        int[][] grid = storageService.loadGrid(id, FileConstants.QUESTION_SUFFIX);

        if (!isValidGrid(grid)) {
            throw new InvalidSudokuException(ErrorConstants.INVALID_SUDOKU_RULES);
        }

        if (!solveGrid(grid)) {
            throw new InvalidSudokuException(ErrorConstants.UNSOLVABLE_SUDOKU);
        }

        storageService.saveGrid(id, FileConstants.ANSWER_SUFFIX, grid);
    }

    private boolean solveGrid(int[][] grid) {
        Cell emptyCell = findEmptyCell(grid);

        if (emptyCell == null) {
            return true;
        }

        for (int val = SudokuConstants.MIN_VALUE; val <= SudokuConstants.MAX_VALUE; val++) {
            if (isValid(grid, emptyCell.row(), emptyCell.col(), val)) {
                grid[emptyCell.row()][emptyCell.col()] = val;

                if (solveGrid(grid)) {
                    return true;
                }

                grid[emptyCell.row()][emptyCell.col()] = SudokuConstants.EMPTY;
            }
        }

        return false;
    }

    private Cell findEmptyCell(int[][] grid) {
        for (int i = SudokuConstants.ZERO; i < SudokuConstants.GRID_SIZE; i++) {
            for (int j = SudokuConstants.ZERO; j < SudokuConstants.GRID_SIZE; j++) {
                if (grid[i][j] == SudokuConstants.EMPTY) {
                    return new Cell(i, j);
                }
            }
        }

        return null;
    }

    private boolean isValid(int[][] grid, int row, int col, int val) {
        for (int j = SudokuConstants.ZERO; j < SudokuConstants.GRID_SIZE; j++) {
            if (grid[row][j] == val) {
                return false;
            }
        }

        for (int i = SudokuConstants.ZERO; i < SudokuConstants.GRID_SIZE; i++) {
            if (grid[i][col] == val) {
                return false;
            }
        }

        int boxRow = (row / SudokuConstants.BOX_SIZE) * SudokuConstants.BOX_SIZE;
        int boxCol = (col / SudokuConstants.BOX_SIZE) * SudokuConstants.BOX_SIZE;

        for (int i = boxRow; i < boxRow + SudokuConstants.BOX_SIZE; i++) {
            for (int j = boxCol; j < boxCol + SudokuConstants.BOX_SIZE; j++) {
                if (grid[i][j] == val) {
                    return false;
                }
            }
        }

        return true;
    }

    private boolean isValidGrid(int[][] grid) {
        for (int row = SudokuConstants.ZERO; row < SudokuConstants.GRID_SIZE; row++) {
            for (int col = SudokuConstants.ZERO; col < SudokuConstants.GRID_SIZE; col++) {
                int val = grid[row][col];

                if (val == SudokuConstants.EMPTY) {
                    continue;
                }

                grid[row][col] = SudokuConstants.EMPTY;
                boolean valid = isValid(grid, row, col, val);
                grid[row][col] = val;

                if (!valid) {
                    return false;
                }
            }
        }
        return true;
    }

}
