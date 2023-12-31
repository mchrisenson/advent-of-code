package _2023._11;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class CosmicExpansion {

    static char[][] grid;
    static long[] expRows, expCols;
    static boolean[][] visited;

    record Tile(int r, int c, long exp) {
        List<Tile> adj() {
            List<Tile> tiles = new ArrayList<>();
            if (r > 0) tiles.add(new Tile(r - 1, c, expRows[r]));
            if (r < grid.length - 1) tiles.add(new Tile(r + 1, c, expRows[r]));
            if (c > 0) tiles.add(new Tile(r, c - 1, expCols[c]));
            if (c < grid[0].length - 1) tiles.add(new Tile(r, c + 1, expCols[c]));
            return tiles;
        }
    }

    record QE(Tile tile, long steps) {
    }

    static long findGalaxyLengths(long expansion) throws IOException {
        List<String> lines = Files.readAllLines(Path.of("src/main/resources/_2023/_11/input.txt"));
        int rows = lines.size(), cols = lines.getFirst().length();
        grid = new char[rows][];
        IntStream.range(0, rows).forEach(r -> grid[r] = lines.get(r).toCharArray());

        expRows = new long[rows];
        expCols = new long[rows];
        Stream.of(expRows, expCols).forEach(arr -> Arrays.fill(arr, expansion));
        IntStream.range(0, rows).forEach(r -> IntStream.range(0, cols).forEach(c -> {
            if (grid[r][c] == '#') expCols[c] = expRows[r] = 1L;
        }));

        Deque<QE> queue = new ArrayDeque<>();
        visited = new boolean[rows][cols];

        long sum = 0;
        for (int r = 0; r < rows; ++r) {
            for (int c = 0; c < cols; ++c) {
                if (grid[r][c] != '#') continue;
                grid[r][c] = '.';
                queue.offer(new QE(new Tile(r, c, 0), 0));
                visited[r][c] = true;

                while (!queue.isEmpty()) {
                    QE e = queue.poll();
                    if (grid[e.tile.r][e.tile.c] == '#') sum += e.steps;

                    for (Tile adj : e.tile.adj()) {
                        if (visited[adj.r][adj.c]) continue;
                        queue.add(new QE(adj, e.steps + adj.exp));
                        visited[adj.r][adj.c] = true;
                    }
                }
                Arrays.stream(visited).forEach(row -> Arrays.fill(row, false));
            }
        }
        return sum;
    }

    public static void main(String[] args) throws IOException {
        System.out.printf("Part One: %s\n", findGalaxyLengths(2L));
        System.out.printf("Part Two: %s\n", findGalaxyLengths(1_000_000L));
    }
}
