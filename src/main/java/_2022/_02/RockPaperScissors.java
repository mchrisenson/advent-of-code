package _2022._02;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Advent of Code 2022 <a href="https://adventofcode.com/2022/day/2">Day 2: Rock Paper Scissors</a>
 */
public class RockPaperScissors {

    /** rows: [A, B, C] cols: [X, Y, Z] */
    static int[][] outcome = new int[][]{{3, 6, 0}, {0, 3, 6}, {6, 0, 3}};
    /** rows: [A, B, C] cols: [X, Y, Z] */
    static int[][] opponent = new int[][]{{3, 1, 2}, {1, 2, 3}, {2, 3, 1}};


    static class PartOne {
        static int score(List<char[]> rounds) {
            return rounds.stream()
                    .mapToInt(r -> outcome[r[0] - 'A'][r[1] - 'X'] + (r[1] - 'W'))
                    .reduce(0, Integer::sum);
        }
    }

    static class PartTwo {
        static int score(List<char[]> rounds) {
            return rounds.stream()
                    .mapToInt(r -> opponent[r[0] - 'A'][r[1] - 'X'] + ((r[1]) - 'X') * 3)
                    .reduce(0, Integer::sum);
        }
    }

    static List<char[]> preprocess(String file) throws IOException {
        List<char[]> lists = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null)
                lists.add(new char[]{line.charAt(0), line.charAt(2)});
        }
        return lists;
    }

    public static void main(String[] args) throws IOException {
        List<char[]> input = preprocess("src/main/resources/_2022/_02/input.txt");

        System.out.println(PartOne.score(input));
        System.out.println(PartTwo.score(input));
    }
}
