package _2024._18;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.IntStream;

/**
 * <a href="https://adventofcode.com">Advent of Code</a>
 * <a href="https://adventofcode.com/2024">2024</a>
 * <a href="https://adventofcode.com/2024/day/18">Day 18: RAM Run</a>
 */
public class RAMRun {
    static final String FILE = "src/main/resources/_2024/_18/input.txt";

    static final List<Pos> DIRS = List.of(new Pos(1, 0), new Pos(0, 1), new Pos(-1, 0), new Pos(0, -1));
    static final int N = 71;
    static List<Pos> bytes = new ArrayList<>();
    static Set<Pos> grid;
    static Pos end = new Pos(N - 1, N - 1);;

    static int dijkstra() {
        Queue<QE> pq = new PriorityQueue<>(Comparator.comparingInt(QE::dist));
        Map<Pos, Integer> dist = new HashMap<>();
        pq.offer(new QE(new Pos(0, 0), 0));

        while (!pq.isEmpty()) {
            QE u = pq.poll();
            if (u.p.equals(end)) return dist.get(end);
            for (Pos dir : DIRS) {
                QE v = new QE(u.p.next(dir), u.dist + 1);
                if (!grid.contains(v.p) || v.dist >= dist.getOrDefault(v.p, Integer.MAX_VALUE)) continue;
                dist.put(v.p, v.dist);
                pq.offer(v);
            }
        }
        return Integer.MAX_VALUE;
    }

    public static void main(String[] args) throws IOException {
        Matcher matcher = Pattern.compile("(\\d{1,2}),(\\d{1,2})").matcher(Files.readString(Path.of(FILE)));
        while (matcher.find()) {
            bytes.add(new Pos(Integer.parseInt(matcher.group(1)), Integer.parseInt(matcher.group(2))));
        }
        grid = new HashSet<>();
        IntStream.range(0, N).forEach(y -> IntStream.range(0, N).forEach(x -> grid.add(new Pos(x, y))));

        bytes.subList(0, 1024).forEach(grid::remove);
        System.out.println("Part One: " + dijkstra());

        Pos firstBlocking = bytes.subList(1024, bytes.size()).stream().peek(grid::remove)
                .filter(b -> dijkstra() == Integer.MAX_VALUE).findFirst().orElseThrow();
        System.out.println("Part Two: " + String.format("%d,%d", firstBlocking.x, firstBlocking.y));
    }

    record QE(Pos p, int dist) {
    }

    record Pos(int x, int y) {
        Pos next(Pos move) { return new Pos(this.x + move.x, this.y + move.y); }
    }
}
