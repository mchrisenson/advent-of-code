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

    static class PartOne {
        static int score(List<String> rounds) {
            int score = 0;
            for (String round : rounds) {
                score += switch (round) {
                    case "A Z", "B X", "C Y":
                        yield 0;
                    case "A X", "B Y", "C Z":
                        yield 3;
                    case "A Y", "B Z", "C X":
                        yield 6;
                    default:
                        throw new IllegalArgumentException("Invalid input: " + round);
                };
                score += round.charAt(2) - 'W';
            }
            return score;
        }
    }

    static class PartTwo {
        static int score(List<String> rounds) {
            int score = 0;
            for (String round : rounds) {
                score += switch (round) {
                    case "A Y", "B X", "C Z":
                        yield 1;
                    case "A Z", "B Y", "C X":
                        yield 2;
                    case "A X", "B Z", "C Y":
                        yield 3;
                    default:
                        throw new IllegalArgumentException("Invalid input: " + round);
                };
                score += (round.charAt(2) - 'X') * 3;
            }
            return score;
        }
    }


    static List<String> preprocess(String file) throws IOException {
        List<String> lists = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null)
                lists.add(line);
        }
        return lists;
    }

    public static void main(String[] args) throws IOException {
        List<String> input = preprocess("src/main/resources/_2022/_02/input.txt");

        System.out.println(PartOne.score(input));
        System.out.println(PartTwo.score(input));
    }
}
