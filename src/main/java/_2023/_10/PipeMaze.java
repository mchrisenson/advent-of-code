package _2023._10;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static _2023._10.PipeMaze.Dir.*;

public class PipeMaze {

    enum Dir {N, S, E, W;}

    static char[][] grid;

    record Tile(int r, int c) {

        char pipe() {
            return grid[r][c];
        }

        List<Tile> adj() {
            return pipeDirs.get(pipe()).stream().map(this::adj).toList();
        }

        Tile adj(Dir dir) {
            return switch (dir) {
                case N -> new Tile(r - 1, c);
                case S -> new Tile(r + 1, c);
                case E -> new Tile(r, c + 1);
                case W -> new Tile(r, c - 1);
            };
        }
    }

    static Map<Character, Set<Dir>> pipeDirs = Map.of(
            '|', EnumSet.of(N, S),
            '-', EnumSet.of(E, W),
            'L', EnumSet.of(N, E),
            'J', EnumSet.of(N, W),
            'F', EnumSet.of(S, E),
            '7', EnumSet.of(S, W),
            '.', EnumSet.noneOf(Dir.class),
            'S', EnumSet.allOf(Dir.class)
    );

    static void updateStart(Tile start) {
        grid[start.r][start.c] = pipeDirs.entrySet().stream().collect(Collectors.toMap(Map.Entry::getValue, Map.Entry::getKey))
                .get(pipeDirs.get(start.pipe()).stream().collect(Collectors.toMap(Function.identity(), start::adj))
                        .entrySet().stream().filter(e -> e.getValue().adj().contains(start)).map(Map.Entry::getKey)
                        .collect(Collectors.toCollection(() -> EnumSet.noneOf(Dir.class))));
    }

    record QE(Tile tile, int count) {
    }

    public static void main(String[] args) throws IOException {
        List<String> lines = Files.readAllLines(Path.of("src/main/resources/_2023/_10/input.txt"));
        int rows = grid.length, cols = grid[0].length;
        grid = new char[lines.size()][];
        IntStream.range(0, rows).forEach(r -> grid[r] = lines.get(r).toCharArray());

        Deque<QE> queue = new ArrayDeque<>();
        boolean[][] visited = new boolean[rows][cols];

        IntStream.range(0, rows).forEach(r -> IntStream.range(0, cols).forEach(c -> {
            if (grid[r][c] == 'S') {
                Tile start = new Tile(r, c);
                queue.offer(new QE(start, 0));
                updateStart(start);
            }
        }));

        int steps = 0;
        while (!queue.isEmpty()) {
            QE e = queue.poll();
            if (e.count > steps) steps = e.count;

            for (Tile v : e.tile.adj()) {
                if (visited[v.r][v.c]) continue;
                queue.offer(new QE(v, e.count + 1));
                visited[v.r][v.c] = true;
            }
        }

        System.out.printf("Part One: %s\n", steps);

        IntStream.range(0, rows).forEach(r -> IntStream.range(0, cols).forEach(c -> {
            if (!visited[r][c]) grid[r][c] = '.';
        }));

        int area = 0;
        Deque<Character> stack = new ArrayDeque<>();
        for (char[] row : grid) {
            int parity = 0;
            for (int c = 0; c < cols; ++c) {
                switch (row[c]) {
                    case '|' -> ++parity;
                    case 'L' -> stack.push('L');
                    case 'F' -> stack.push('F');
                    case 'J' -> {
                        if (stack.pop() == 'F') ++parity;
                    }
                    case '7' -> {
                        if (stack.pop() == 'L') ++parity;
                    }
                    case '.' -> {
                        if (parity % 2 == 1) ++area;
                    }
                }
            }
        }
        System.out.printf("Part Two: %s\n", area);
    }
}
