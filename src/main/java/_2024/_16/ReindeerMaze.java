package _2024._16;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * <a href="https://adventofcode.com">Advent of Code</a>
 * <a href="https://adventofcode.com/2024">2024</a>
 * <a href="https://adventofcode.com/2024/day/16">Day 16: Reindeer Maze</a>
 */
public class ReindeerMaze {
    static final String FILE = "src/main/resources/_2024/_16/input.txt";
    static final int N = 0, E = 1, S = 2, W = 3;
    static final Pos[] DIRS = {new Pos(-1, 0), new Pos(0, 1), new Pos(1, 0), new Pos(0, -1)};

    static int rows, cols;
    static Set<Pos> grid;
    static Pos start, end;
    static Map<PD, Integer> dist = new HashMap<>();
    static Map<PD, Set<PD>> prev = new HashMap<>();

    static int dijkstra() {
        Queue<QE> heap = new PriorityQueue<>(Comparator.comparingInt(QE::dist));
        dist.put(new PD(start, E), 0);
        heap.offer(new QE(new PD(start, E), 0));

        while (!heap.isEmpty()) {
            QE u = heap.poll();
            adj(u).forEach(v -> {
                if (v.dist <= dist.getOrDefault(v.pd, Integer.MAX_VALUE))
                    prev.computeIfAbsent(v.pd, p -> new HashSet<>()).add(u.pd);
                if (v.dist < dist.getOrDefault(v.pd, Integer.MAX_VALUE)) {
                    dist.put(v.pd, v.dist);
                    heap.offer(v);
                }
            });
        }
        return Stream.of(new PD(end, N), new PD(end, E)).filter(dist::containsKey).mapToInt(dist::get).min().orElseThrow();
    }

    static Stream<QE> adj(QE u) {
        return IntStream.of(0, -1, 1)
                .mapToObj(d -> new QE(new PD(u.pd.p.next(DIRS[(u.pd.dir + d) & 3]), (u.pd.dir + d) & 3), u.dist + (d == 0 ? 1 : 1001)))
                .filter(v -> grid.contains(v.pd.p));
    }

    static Set<PD> allPaths(Set<PD> set, PD u) {
        for (PD v : prev.getOrDefault(u, Collections.emptySet())) {
            if (set.add(v)) allPaths(set, v);
        }
        return set;
    }

    static void init() throws IOException {
        List<String> lines = Files.readAllLines(Path.of(FILE));
        rows = lines.size();
        cols = lines.getFirst().length();
        grid = new HashSet<>();
        IntStream.range(0, rows).forEach(r -> IntStream.range(0, cols).forEach(c -> {
            char val = lines.get(r).charAt(c);
            if (val == 'S') start = new Pos(r, c);
            else if (val == 'E') end = new Pos(r, c);
            if (val != '#') grid.add(new Pos(r, c));
        }));
    }

    public static void main(String[] args) throws IOException {
        init();
        int pathLength = dijkstra();
        System.out.println("Part One: " + pathLength);

        Set<Pos> tiles = Stream.of(new PD(end, N), new PD(end, E)).filter(dist::containsKey).filter(pd -> dist.get(pd) == pathLength)
                .map(end -> allPaths(new HashSet<>(Set.of(end)), end)).flatMap(Collection::stream).map(PD::p).collect(Collectors.toSet());
        System.out.println("Part Two: " + tiles.size());
//        print(tiles);
    }

    static void print(Set<Pos> paths) {
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                Pos p = new Pos(r, c);
                if (p.equals(start)) System.out.print('S');
                else if (p.equals(end)) System.out.print('E');
                else if (paths.contains(p)) System.out.print('O');
                else System.out.print(grid.contains(p) ? '.' : '#');
            }
            System.out.println();
        }
    }

    record PD(Pos p, int dir) {}

    record QE(PD pd, int dist) {}

    record Pos(int r, int c) {
        Pos next(Pos move) { return new Pos(this.r + move.r, this.c + move.c); }
    }
}
