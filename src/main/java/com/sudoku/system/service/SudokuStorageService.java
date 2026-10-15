package com.sudoku.system.service;

import com.sudoku.system.constant.ErrorConstants;
import com.sudoku.system.constant.FileConstants;
import com.sudoku.system.constant.SudokuConstants;
import com.sudoku.system.exception.InvalidSudokuException;
import com.sudoku.system.exception.SudokuNotFoundException;
import com.sudoku.system.tools.CsvLines;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class SudokuStorageService {

    private final Path dataDir;

    public SudokuStorageService(@Value(FileConstants.DATA_DIR_PROPERTY) String dataDir) {
        this.dataDir = Path.of(dataDir);
    }

    public UUID upload(MultipartFile file) {
        int[][] grid = toGrid(CsvLines.getLines(file));
        UUID id = UUID.randomUUID();
        saveGrid(id.toString(), FileConstants.QUESTION_SUFFIX, grid);

        return id;
    }

    public int[][] loadGrid(String id, String suffix) {
        return toGrid(CsvLines.getLines(getFilePath(id, suffix)));
    }

    public void saveGrid(String id, String suffix, int[][] grid) {
        CsvLines.writeLines(resolvePath(parseId(id), suffix), toLines(grid));
    }

    public Path getFilePath(String id, String suffix) {
        Path path = resolvePath(parseId(id), suffix);

        if (!Files.exists(path)) {
            throw new SudokuNotFoundException(ErrorConstants.SUDOKU_NOT_FOUND);
        }

        return path;
    }

    private int[][] toGrid(List<String[]> lines) {
        if (lines.size() != SudokuConstants.GRID_SIZE) {
            throw new InvalidSudokuException(ErrorConstants.INVALID_SUDOKU_FILE);
        }

        int[][] grid = new int[SudokuConstants.GRID_SIZE][SudokuConstants.GRID_SIZE];

        for (int row = SudokuConstants.ZERO; row < SudokuConstants.GRID_SIZE; row++) {
            String[] values = lines.get(row);

            if (values.length != SudokuConstants.GRID_SIZE) {
                throw new InvalidSudokuException(ErrorConstants.INVALID_SUDOKU_FILE);
            }

            for (int col = SudokuConstants.ZERO; col < SudokuConstants.GRID_SIZE; col++) {
                grid[row][col] = parseCellValue(values[col]);
            }
        }

        return grid;
    }

    private List<String> toLines(int[][] grid) {
        return Arrays.stream(grid)
                .map(row -> Arrays.stream(row)
                        .mapToObj(String::valueOf)
                        .collect(Collectors.joining(FileConstants.COLUMN_SEPARATOR)))
                .toList();
    }

    private int parseCellValue(String value) {
        try {
            int number = Integer.parseInt(value.trim());

            if (number < SudokuConstants.EMPTY || number > SudokuConstants.MAX_VALUE) {
                throw new InvalidSudokuException(ErrorConstants.INVALID_SUDOKU_FILE);
            }

            return number;
        } catch (NumberFormatException e) {
            throw new InvalidSudokuException(ErrorConstants.INVALID_SUDOKU_FILE, e);
        }
    }

    private UUID parseId(String id) {
        try {
            return UUID.fromString(id);
        } catch (IllegalArgumentException e) {
            throw new InvalidSudokuException(ErrorConstants.INVALID_SUDOKU_ID, e);
        }
    }

    private Path resolvePath(UUID id, String suffix) {
        return dataDir.resolve(id + suffix + FileConstants.CSV_EXTENSION);
    }
}
