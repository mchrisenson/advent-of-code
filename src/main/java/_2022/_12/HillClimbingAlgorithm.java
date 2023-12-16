package _2022._12;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

/**
 * Advent of Code <a href="https://adventofcode.com/2022/day/12">Day 12: Hill Climbing Algorithm</a>
 */
public class HillClimbingAlgorithm {
    static final Point[] DIRS = new Point[]{new Point(0, 1), new Point(1, 0), new Point(0, -1), new Point(-1, 0)};

    static int findShortestPath(char[][] grid, Point start, Point end) {
        int rows = grid.length, cols = grid[0].length;
        int[][] visited = new int[rows][cols];
        for (int r = 0; r < rows; ++r) Arrays.fill(visited[r], -1);
        Queue<Point> queue = new ArrayDeque<>();

        queue.offer(start);
        visited[start.r][start.c] = 0;

        while (!queue.isEmpty()) {
            Point u = queue.poll();

            if (u.r == end.r && u.c == end.c)
                return visited[u.r][u.c];

            for (Point dir : DIRS) {
                Point v = new Point(u.r + dir.r, u.c + dir.c);
                if (v.r < 0 || v.c < 0 || v.r >= rows || v.c >= cols) continue;
                if (visited[v.r][v.c] > -1) continue;
                if (grid[v.r][v.c] - grid[u.r][u.c] > 1) continue;

                queue.add(v);
                visited[v.r][v.c] = visited[u.r][u.c] + 1;
            }
        }
        return Integer.MAX_VALUE;
    }

    record Point(int r, int c) {
    }

    static Point findPointReplaceValue(char[][] grid, char find, char replace) {
        int rows = grid.length, cols = grid[0].length;
        for (int r = 0; r < rows; ++r) {
            for (int c = 0; c < cols; ++c) {
                if (grid[r][c] == find) {
                    grid[r][c] = replace;
                    return new Point(r, c);
                }
            }
        }
        throw new RuntimeException("Unable to find starting point.");
    }

    static List<Point> findPoints(char[][] grid, char find) {
        List<Point> points = new ArrayList<>();

        int rows = grid.length, cols = grid[0].length;
        for (int r = 0; r < rows; ++r) {
            for (int c = 0; c < cols; ++c) {
                if (grid[r][c] == find) points.add(new Point(r, c));
            }
        }
        return points;
    }

    static char[][] parseInput(String file) throws IOException {
        List<char[]> lines = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null)
                lines.add(line.toCharArray());
        }
        int rows = lines.size();
        char[][] grid = new char[rows][];

        for (int r = 0; r < rows; ++r)
            grid[r] = lines.get(r);
        return grid;
    }

    public static void main(String[] args) throws IOException {
        char[][] grid = parseInput("src/main/resources/_2022/_12/input.txt");

        Point start = findPointReplaceValue(grid, 'S', 'a');
        Point end = findPointReplaceValue(grid, 'E', 'z');

        System.out.printf("Day One: %d%n", findShortestPath(grid, start, end));

        System.out.printf("Day Two: %d%n", findPoints(grid, 'a').stream()
                .mapToInt(s -> findShortestPath(grid, s, end)).min().orElseThrow());
    }
}
