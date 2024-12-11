package _2024._10;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <a href="https://adventofcode.com">Advent of Code</a>
 * <a href="https://adventofcode.com/2024">2024</a>
 * <a href="https://adventofcode.com/2024/day/10">Day 10: Hoof It</a>
 */
public class HoofIt {
    static final String FILE = "src/main/resources/_2024/_10/input.txt";

    static final List<Pos> DIRS = List.of(new Pos(-1, 0), new Pos(0, 1), new Pos(1, 0), new Pos(0, -1));
    static Map<Pos, Integer> grid;

    static Set<List<Pos>> dfs(Pos curr, Map<Pos, Set<List<Pos>>> pathSets) {
        if (pathSets.containsKey(curr)) return pathSets.get(curr); // if visited return paths
        if (grid.get(curr) == 9) return Set.of(Collections.singletonList(curr));
        Set<List<Pos>> pathSet = new HashSet<>();
        pathSets.put(curr, pathSet); // mark visited
        for (Pos next : DIRS.stream().map(curr::next).toList()) {
            if (grid.containsKey(next) && grid.get(next) - grid.get(curr) == 1) {
                for (List<Pos> adjPath : dfs(next, pathSets)) {
                    List<Pos> path = new LinkedList<>(adjPath);
                    path.addFirst(curr);
                    pathSet.add(path);
                }
            }
        }
        return pathSet;
    }

    public static void main(String[] args) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE))) {
            grid = new HashMap<>();
            int row = 0;
            String line;
            while ((line = reader.readLine()) != null) {
                int col = 0;
                for (char digit : line.toCharArray()) grid.put(new Pos(row, col++), digit - '0');
                row++;
            }
        }

        List<Set<List<Pos>>> pathSets = grid.keySet().stream().filter(p -> grid.get(p) == 0)
                .map(head -> dfs(head, new HashMap<>())).toList();

        int answer1 = pathSets.stream().mapToInt(set -> set.stream().map(List::getLast).collect(Collectors.toSet()).size()).sum();
        System.out.println("Part Two: " + answer1);

        int answer2 = pathSets.stream().mapToInt(Set::size).sum();
        System.out.println("Part Two: " + answer2);
    }

    record Pos(int r, int c) {
        Pos next(Pos dir) { return new Pos(this.r + dir.r, this.c + dir.c); }
    }
}
