package _2022._09;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

/**
 * Advent of Code <a href="https://adventofcode.com/2022/day/9">Day 9: Rope Bridge</a>
 */
public class RopeBridge {

    static final Map<Character, Position> DIR = Map.of(
            'U', new Position(0, 1),
            'D', new Position(0, -1),
            'R', new Position(1, 0),
            'L', new Position(-1, 0)
    );

    static int tailLocations(List<Move> moves, int knots) {
        int n = knots - 1;

        Position head = new Position(0, 0);
        Position[] tails = new Position[n];
        for (int i = 0; i < n; ++i)
            tails[i] = new Position(0, 0);

        Set<Position> visited = new HashSet<>();
        visited.add(new Position(0, 0));
        int count = 1;

        for (Move move : moves) {
            for (int s = 0; s < move.steps; ++s) {
                Position dir = DIR.get(move.dir);

                head = new Position(head.x + dir.x, head.y + dir.y);

                Position prev = head, curr;
                for (int t = 0; t < n; ++t) {
                    curr = tails[t];
                    if (Math.abs(prev.x - curr.x) <= 1 && Math.abs(prev.y - curr.y) <= 1) break;

                    tails[t] = curr = new Position(
                            curr.x + Integer.compare(prev.x - curr.x, 0),
                            curr.y + Integer.compare(prev.y - curr.y, 0));

                    if (t == n - 1 && visited.add(new Position(curr.x, curr.y)))
                        ++count;

                    prev = curr;
                }
            }
        }
        return count;
    }

    static
    record Move(char dir, int steps) {
    }

    record Position(int x, int y) {
    }

    static List<Move> parseInput(String file) throws IOException {
        List<Move> moves = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] s = line.split(" ");
                moves.add(new Move(s[0].charAt(0), Integer.parseInt(s[1])));
            }
        }
        return moves;
    }

    public static void main(String[] args) throws IOException {
        List<Move> moves = parseInput("src/main/resources/_2022/_09/input.txt");

        System.out.println("Part One: " + tailLocations(moves, 2));
        System.out.println("Part Two: " + tailLocations(moves, 10));
    }
}
