package _2024._06;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.function.Function;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * <a href="https://adventofcode.com">Advent of Code</a>
 * <a href="https://adventofcode.com/2024">2024</a>
 * <a href="https://adventofcode.com/2024/day/6">Day 6: Guard Gallivant</a>
 */
public class GuardGallivant {
    static final String FILE = "src/main/resources/_2024/_06/input.txt";

    static final Pos[] DIRS = {new Pos(-1, 0), new Pos(0, 1), new Pos(1, 0), new Pos(0, -1)};

    static Set<Pos> patrol(char[][] grid, Pos p, Pos o) {
        int d = 0;
        Map<Pos, List<Integer>> map = new HashMap<>();
        // while position and direction haven't repeated
        while (!map.containsKey(p) || !map.get(p).contains(d)) {
            map.computeIfAbsent(p, __ -> new ArrayList<>(4)).add(d);
            Pos n = p.next(DIRS[d]);
            if (n.r < 0 || n.c < 0 || n.r >= grid.length || n.c >= grid[0].length) return map.keySet();
            if (grid[n.r][n.c] == '#' || n.equals(o)) d = (d + 1) % 4;
            else p = n;
        }
        return Collections.emptySet(); // empty set indicates a loop occurs with the given obstruction
    }

    public static void main(String[] args) throws IOException {
        char[][] grid;
        try (Stream<String> lines = Files.lines(Path.of(FILE))) {
            grid = lines.map(String::toCharArray).toArray(char[][]::new);
        }
        Pos start = IntStream.range(0, grid.length).mapToObj(r -> IntStream.range(0, grid[0].length)
                .mapToObj(c -> new Pos(r, c))).flatMap(Function.identity()).filter(p -> grid[p.r][p.c] == '^')
                .findFirst().orElseThrow();

        Set<Pos> route = patrol(grid, start, new Pos(-1, -1));
        System.out.println("Part One: " + route.size());

        long answer = route.stream().map(o -> patrol(grid, start, o)).filter(Set::isEmpty).count();
        System.out.println("Part Two: " + answer);
    }

    record Pos(int r, int c) {
        Pos next(Pos d) { return new Pos(this.r + d.r, this.c + d.c); }
    }
}
