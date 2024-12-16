package _2024._15;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.IntStream;

/**
 * <a href="https://adventofcode.com">Advent of Code</a>
 * <a href="https://adventofcode.com/2024">2024</a>
 * <a href="https://adventofcode.com/2024/day/15">Day 15: Warehouse Woes</a>
 */
public class WarehouseWoes {
    static final String FILE = "src/main/resources/_2024/_15/input.txt";
    static final Map<Character, Pos> DIRS = Map.of('^', new Pos(-1, 0), '>', new Pos(0, 1), 'v', new Pos(1, 0), '<', new Pos(0, -1));
    static final Map<Character, String> inMap = Map.of('#', "##", 'O', "[]", '.', "..", '@', "@.");
    static Map<Pos, Character> grid;
    static Pos robot;
    static char[] moves;

    static int shift() {
        for (char move : moves) {
            if (check(robot, move)) robot = shift(robot, move);
        }
        return grid.entrySet().stream().filter(e -> e.getValue() == 'O' || e.getValue() == '[')
                .map(Map.Entry::getKey).mapToInt(p -> p.r * 100 + p.c).sum();
    }

    static boolean check(Pos curr, char move) {
        Pos next = curr.next(DIRS.get(move));
        return switch (grid.get(next)) {
            case '#' -> false;
            case 'O' -> check(next, move);
            case '[' -> switch (move) {
                case '^', 'v' -> check(next, move) && check(next.next(DIRS.get('>')), move);
                default -> check(next, move);
            };
            case ']' -> switch (move) {
                case '^', 'v' -> check(next, move) && check(next.next(DIRS.get('<')), move);
                default -> check(next, move);
            };
            default -> true; // '.'
        };
    }

    static Pos shift(Pos curr, char move) {
        Pos next = curr.next(DIRS.get(move));
        char d = grid.get(next);
        if (d != '.') shift(next, move); // 'O', '[', ']'
        if (d == '[' && (move == '^' || move == 'v')) shift(next.next(DIRS.get('>')), move);
        else if (d == ']' && (move == '^' || move == 'v')) shift(next.next(DIRS.get('<')), move);
        grid.put(next, grid.put(curr, '.'));
        return next;
    }

    static void init(int part) throws IOException {
        List<String> lines = Files.readAllLines(Path.of(FILE));
        int rows = IntStream.range(0, lines.size()).filter(i -> lines.get(i).isEmpty()).findFirst().orElseThrow();
        int cols = lines.getFirst().length();
        grid = new HashMap<>();
        IntStream.range(0, rows).forEach(r -> IntStream.range(0, cols).forEach(c -> {
            if (part == 1) {
                grid.put(new Pos(r, c), lines.get(r).charAt(c));
            } else {
                IntStream.range(0, 2).forEach(i -> grid.put(new Pos(r, c * 2 + i), inMap.get(lines.get(r).charAt(c)).charAt(i)));
            }
        }));
        moves = String.join("", lines.subList(rows + 1, lines.size())).toCharArray();
        robot = grid.keySet().stream().filter(p -> grid.get(p) == '@').findFirst().orElseThrow();
    }

    public static void main(String[] args) throws IOException {
        init(1);
        System.out.println("Part One: " + shift());

        init(2);
        System.out.println("Part Two: " + shift());
    }

    record Pos(int r, int c) {
        Pos next(Pos move) { return new Pos(this.r + move.r, this.c + move.c); }
    }
}
