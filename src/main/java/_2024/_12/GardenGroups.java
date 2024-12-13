package _2024._12;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

/**
 * <a href="https://adventofcode.com">Advent of Code</a>
 * <a href="https://adventofcode.com/2024">2024</a>
 * <a href="https://adventofcode.com/2024/day/12">Day 12: Garden Groups</a>
 */
public class GardenGroups {
    static final String FILE = "src/main/resources/_2024/_12/input.txt";
    static Map<Character, Plot> SIDES = Map.of('R', new Plot(0, 1), 'D', new Plot(1, 0), 'L', new Plot(0, -1), 'U', new Plot(-1, 0));
    static Map<Plot, Character> grid = new HashMap<>();

    static Map<Plot, Set<Character>> findRegion(Plot curr, Set<Plot> visited, Map<Plot, Set<Character>> region) {
        if (!visited.add(curr)) return region;
        region.put(curr, new HashSet<>(SIDES.keySet()));
        for (char side : SIDES.keySet()) {
            Plot next = curr.next(SIDES.get(side));
            if (!grid.containsKey(next) || !grid.get(next).equals(grid.get(curr))) continue;
            region.get(curr).remove(side);
            findRegion(next, visited, region);
        }
        return region;
    }

    static int countSides(Map<Plot, Set<Character>> region) {
        Map<Character, Map<Integer, SortedSet<Integer>>> intervals = new HashMap<>();
        int count = 0;
        for (Plot p : region.keySet()) {
            for (char side : region.get(p)) {
                switch (side) {
                    case 'U', 'D' ->
                            intervals.computeIfAbsent(side, k -> new HashMap<>()).computeIfAbsent(p.r, k -> new TreeSet<>()).add(p.c);
                    case 'L', 'R' ->
                            intervals.computeIfAbsent(side, k -> new HashMap<>()).computeIfAbsent(p.c, k -> new TreeSet<>()).add(p.r);
                }
            }
        }
        // For example: a grouping of plots with the same 'U' side in the same row 1 with column numbers 1,2,4,5,6,8,9 would make 3 sides since there are two gaps
        for (char side : intervals.keySet()) {
            for (int rc : intervals.get(side).keySet()) {
                int prev = -1;
                for (int cr : intervals.get(side).get(rc)) {
                    if (prev == -1 || cr > prev + 1) count++;
                    prev = cr;
                }
            }
        }
        return count;
    }

    public static void main(String[] args) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE))) {
            int r = 0;
            String line;
            while ((line = reader.readLine()) != null) {
                for (int c = 0; c < line.length(); c++) grid.put(new Plot(r, c), line.charAt(c));
                r++;
            }
        }
        Set<Plot> visited = new HashSet<>();
        List<Map<Plot, Set<Character>>> regions = grid.keySet().stream().map(plot -> findRegion(plot, visited, new HashMap<>())).toList();

        int answer1 = regions.stream().mapToInt(region -> region.size() * region.values().stream().mapToInt(Set::size).sum()).sum();
        System.out.println("Part One: " + answer1);

        int answer2 = regions.stream().mapToInt(region -> region.size() * countSides(region)).sum();
        System.out.println("Part Two: " + answer2);
    }

    record Plot(int r, int c) {
        Plot next(Plot dir) { return new Plot(this.r + dir.r, this.c + dir.c); }
    }
}
