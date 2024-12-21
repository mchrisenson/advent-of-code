package _2024._20;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.IntStream;

/**
 * <a href="https://adventofcode.com">Advent of Code</a>
 * <a href="https://adventofcode.com/2024">2024</a>
 * <a href="https://adventofcode.com/2024/day/20">Day 20: Race Condition</a>
 */
public class RaceCondition {
    static final String FILE = "src/main/resources/_2024/_20/input.txt";
    static final List<Pos> DIRS = List.of(new Pos(1, 0), new Pos(0, 1), new Pos(-1, 0), new Pos(0, -1));

    static int N;
    static Set<Pos> grid = new HashSet<>();
    static Map<Pos, Integer> dist = new HashMap<>();
    static Pos start, end;

    static int bestCheats(int t) {
        int count = 0;
        for (var p : dist.keySet()) {
            for (var q : dist.keySet()) {
                int d = p.dist(q), i = dist.get(p), j = dist.get(q);
                if (d <= t && j - i - d >= 100) count++;
            }
        }
        return count;
    }

    static void race() {
        dist.put(start, 0);
        Deque<Pos> q = new ArrayDeque<>();
        q.offer(start);
        while (!q.isEmpty()) {
            Pos u = q.poll();
            DIRS.stream().map(u::next).filter(grid::contains).filter(p -> !dist.containsKey(p)).forEach(v -> {
                dist.put(v, dist.get(u) + 1);
                q.offer(v);
            });
        }
    }

    static void parseInput() throws IOException {
        List<String> lines = Files.readAllLines(Path.of(FILE));
        N = lines.size();

        IntStream.range(0, N).forEach(y -> IntStream.range(0, N).forEach(x -> {
            char val = lines.get(y).charAt(x);
            if (val == 'S') start = new Pos(x, y);
            else if (val == 'E') end = new Pos(x, y);
            if (val != '#') grid.add(new Pos(x, y));
        }));
    }

    public static void main(String[] args) throws IOException {
        parseInput();
        race();

        System.out.println("Part One: " + bestCheats(2));

        System.out.println("Part Two: " + bestCheats(20));
    }

    record Pos(int x, int y) {
        Pos next(Pos move) { return new Pos(this.x + move.x, this.y + move.y); }

        int dist(Pos p) { return Math.abs(this.x - p.x) + Math.abs(this.y - p.y); }
    }
}
