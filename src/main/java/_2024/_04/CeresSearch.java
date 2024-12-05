package _2024._04;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class CeresSearch {
    static final String FILE = "src/main/resources/_2024/_04/input.txt";

    public static void main(String[] args) throws IOException {
        char[][] grid;
        try (Stream<String> lines = Files.lines(Path.of(FILE))) {
            grid = lines.map(String::toCharArray).toArray(char[][]::new);
        }
        char[] xmas = "XMAS".toCharArray();
        int[][] dirs = {{1, 0}, {0, 1}, {-1, 0}, {0, -1}, {1, 1}, {-1, -1}, {1, -1}, {-1, 1}};

        long answer = IntStream.range(0, grid.length).mapToLong(row -> IntStream.range(0, grid[0].length)
                .filter(col -> grid[row][col] == 'X').mapToLong(col -> Arrays.stream(dirs).filter(dir -> {
                    for (int i = 1, r = row + dir[0], c = col + dir[1]; i < xmas.length; i++, r += dir[0], c += dir[1]) {
                        if (r < 0 || c < 0 || r >= grid.length || c >= grid[0].length || grid[r][c] != xmas[i])
                            return false;
                    }
                    return true;
                }).count()).sum()).sum();
        System.out.println("Part One: " + answer);

        answer = IntStream.range(1, grid.length - 1).mapToLong(r -> IntStream.range(1, grid[0].length - 1)
                .filter(c -> grid[r][c] == 'A').filter(c -> {
                    char[] x = {grid[r - 1][c - 1], grid[r - 1][c + 1], grid[r + 1][c + 1], grid[r + 1][c - 1]};
                    return IntStream.range(0, 4).anyMatch(i -> x[i] == 'M' && x[(i + 1) % 4] == 'M' && x[(i + 2) % 4] == 'S' && x[(i + 3) % 4] == 'S');
                }).count()).sum();
        System.out.println("Part Two: " + answer);
    }
}
