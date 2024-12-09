package _2024._08;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Stream;

/**
 * <a href="https://adventofcode.com">Advent of Code</a>
 * <a href="https://adventofcode.com/2024">2024</a>
 * <a href="https://adventofcode.com/2024/day/8">Day 8: Resonant Collinearity</a>
 */
public class ResonantCollinearity {
    static final String FILE = "src/main/resources/_2024/_08/input.txt";

    public static void main(String[] args) throws IOException {
        char[][] grid;
        try (Stream<String> lines = Files.lines(Path.of(FILE))) {
            grid = lines.map(String::toCharArray).toArray(char[][]::new);
        }
        int rows = grid.length, cols = grid[0].length;
        Map<Character, List<Pos>> map = new HashMap<>();

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                if (grid[r][c] != '.') map.computeIfAbsent(grid[r][c], __ -> new ArrayList<>()).add(new Pos(r, c));
            }
        }

        Set<Pos> part1 = new HashSet<>();
        for (char c : map.keySet()) {
            for (Pos u : map.get(c)) {
                for (Pos v : map.get(c)) {
                    if (v.equals(u)) continue;
                    Pos a = new Pos(u.r + (u.r - v.r), u.c + (u.c - v.c));
                    if (a.r >= 0 && a.c >= 0 && a.r < rows && a.c < cols) part1.add(a);
                }
            }
        }
        System.out.println("Part One: " + part1.size());

        Set<Pos> part2 = new HashSet<>();
        for (char c : map.keySet()) {
            for (Pos u : map.get(c)) {
                for (Pos v : map.get(c)) {
                    if (v.equals(u)) continue;
                    Pos delta = new Pos(u.r - v.r, u.c - v.c);
                    for (Pos a = u; a.r >= 0 && a.c >= 0 && a.r < rows && a.c < cols; a = a.next(delta)) {
                        part2.add(a);
                    }
                }
            }
        }
        System.out.println("Part Two: " + part2.size());
    }

    record Pos(int r, int c) {
        Pos next(Pos d) { return new Pos(this.r + d.r, this.c + d.c); }
    }
}
