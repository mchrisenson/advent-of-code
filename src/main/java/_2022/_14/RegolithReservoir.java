package _2022._14;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class RegolithReservoir {
    static final String FILE = "src/main/resources/_2022/_14/input.txt";
    static final char AIR = '.', ROCK = '#', SAND = 'o';
    static final Point pourPoint = new Point(500, 0);

    static int measurePouringSand(char[][] grid, PourFunction sf) {
        int rows = grid.length, count = 0;
        for (int x = pourPoint.x, y = pourPoint.y, prevY = -1; sf.carryOn(prevY); x = pourPoint.x, y = pourPoint.y) {
            while (y < rows - 1) {
                if (grid[y + 1][x] == AIR) {
                    y += 1;
                } else if (grid[y + 1][x - 1] == AIR) {
                    y += 1;
                    x -= 1;
                } else if (grid[y + 1][x + 1] == AIR) {
                    y += 1;
                    x += 1;
                } else {
                    break;
                }
            }
            grid[y][x] = SAND;
            prevY = y;
            ++count;
        }

        return count;
    }

    interface PourFunction {
        boolean carryOn(int y);
    }

    static char[][] setupGrid(List<List<Point>> pointsInput, int rows) {
        int cols = pourPoint.x + rows;
        char[][] grid = new char[rows][cols];
        for (int r = 0; r < rows; ++r) Arrays.fill(grid[r], '.');

        for (List<Point> points : pointsInput) {
            Point prev = points.get(0), curr;
            for (int i = 1; i < points.size(); ++i) {
                curr = points.get(i);
                if (curr.x == prev.x) {
                    int y = Math.min(prev.y, curr.y), last = Math.max(prev.y, curr.y);
                    for (; y <= last; ++y) grid[y][prev.x] = ROCK;
                } else {
                    int x = Math.min(prev.x, curr.x), last = Math.max(prev.x, curr.x);
                    for (; x <= last; ++x) grid[prev.y][x] = ROCK;
                }
                prev = curr;
            }
        }
        return grid;
    }

    record Point(int x, int y) {
    }

    static List<List<Point>> getPoints() {
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE))) {
            return reader.lines().map(line -> Arrays.stream(line.split(" -> ")).map(s -> {
                String[] arr = s.split(",");
                return new Point(Integer.parseInt(arr[0]), Integer.parseInt(arr[1]));
            }).toList()).toList();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        List<List<Point>> points = getPoints();
        int floor = points.stream().flatMap(Collection::stream).mapToInt(p -> p.y).max().orElseThrow() + 2;

        System.out.println("Part One: " + (measurePouringSand(setupGrid(points, floor), y -> y != floor - 1) - 1));

        System.out.println("Part Two: " + measurePouringSand(setupGrid(points, floor), y -> y != pourPoint.y));
    }
}
