package _2022._04;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

/**
 * Advent of Code 2022 <a href="https://adventofcode.com/2022/day/4">Day 4: Camp Cleanup</a>
 */
public class CampCleanup {
    static class PartOne {
        static int cleanup(List<int[][]> pairs) {
            int sum = 0;
            for (int[][] ranges : pairs) {
                int start1 = ranges[0][0], end1 = ranges[0][1], start2 = ranges[1][0], end2 = ranges[1][1];
                sum += start1 <= start2 && end1 >= end2 || start2 <= start1 && end2 >= end1 ? 1 : 0;
            }
            return sum;
        }
    }

    static class PartTwo {
        static int cleanup(List<int[][]> pairs) {
            int sum = 0;
            for (int[][] ranges : pairs) {
                int start1 = ranges[0][0], end1 = ranges[0][1], start2 = ranges[1][0], end2 = ranges[1][1];
                sum += start2 <= end1 && start1 <= end2 ? 1 : 0;
            }
            return sum;
        }
    }

    static List<int[][]> preprocess(String file) throws IOException {
        List<int[][]> lines = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                int[][] ranges = new int[2][2];
                String[] assignments = line.split(",");

                for (int i = 0; i < 2; ++i) {
                    String[] assignment = assignments[i].split("-");

                    for (int j = 0; j < 2; ++j)
                        ranges[i][j] = Integer.parseInt(assignment[j]);
                }
                lines.add(ranges);
            }
        }
        return lines;
    }

    public static void main(String[] args) throws IOException {
        List<int[][]> input = preprocess("src/main/resources/_2022/_04/input.txt");

        System.out.println("Part One: " + CampCleanup.PartOne.cleanup(input));
        System.out.println("Part Two: " + CampCleanup.PartTwo.cleanup(input));
    }
}
