package _2022._08;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Advent of code <a href="https://adventofcode.com/2022/day/8">Day 8: Treetop Tree House</a>
 */
public class TreetopTreeHouse {

    static class PartOne {
        static int visibleTrees(int[][] grid) {
            int m = grid.length, n = grid[0].length;

            int[][] top = new int[n][m], bottom = new int[n][m], left = new int[n][m], right = new int[n][m];

            for (int i = 0; i < m; ++i) {
                for (int j = 0; j < n; ++j) {
                    top[i][j] = i != 0 ? Math.max(top[i - 1][j], grid[i - 1][j]) : -1;
                    bottom[m - i - 1][j] = i != 0 ? Math.max(bottom[m - i][j], grid[m - i][j]) : -1;
                    left[i][j] = j != 0 ? Math.max(left[i][j - 1], grid[i][j - 1]) : -1;
                    right[i][n - j - 1] = j != 0 ? Math.max(right[i][n - j], grid[i][n - j]) : -1;
                }
            }

            int count = 0;
            for (int i = 0; i < m; ++i) {
                for (int j = 0; j < n; ++j) {
                    int curr = grid[i][j];
                    if (curr > top[i][j] || curr > bottom[i][j] || curr > left[i][j] || curr > right[i][j])
                        ++count;
                }
            }
            return count;
        }
    }

    static class PartTwo {
        static int maxScenicScore(int[][] grid) {
            int maxScenicScore = 0;
            int m = grid.length, n = grid[0].length;
            for (int i = 0; i < m; ++i) {
                for (int j = 0; j < n; ++j) {
                    int curr = grid[i][j];
                    int score = 1, k = 0;
                    while (i - (k + 1) >= 0 && grid[i - ++k][j] < curr) ;
                    score *= k;
                    k = 0;
                    while (i + (k + 1) < m && grid[i + ++k][j] < curr) ;
                    score *= k;
                    k = 0;
                    while (j - (k + 1) >= 0 && grid[i][j - ++k] < curr) ;
                    score *= k;
                    k = 0;
                    while (j + (k + 1) < n && grid[i][j + ++k] < curr) ;
                    score *= k;

                    maxScenicScore = Math.max(score, maxScenicScore);
                }
            }
            return maxScenicScore;
        }
    }

    static int[][] parseInput(String file) throws IOException {
        List<String> lines = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null)
                lines.add(line);
        }
        int m = lines.size(), n = lines.get(0).length();
        int[][] grid = new int[n][m];

        for (int i = 0; i < m; ++i) {
            for (int j = 0; j < n; ++j)
                grid[i][j] = lines.get(i).charAt(j) - '0';
        }
        return grid;
    }

    public static void main(String[] args) throws IOException {
        int[][] grid = parseInput("src/main/resources/_2022/_08/input.txt");

        System.out.println("Part One: " + PartOne.visibleTrees(grid));
        System.out.println("Part Two: " + PartTwo.maxScenicScore(grid));
    }
}
