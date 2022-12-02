package _2022._01;

import java.io.*;
import java.util.*;

/**
 * Advent of Code 2022 <a href="https://adventofcode.com/2022/day/1">Day 1: Calorie Counting</a>
 */
public class CalorieCounting {

    static class PartOne {
        static int countCalories(List<List<Integer>> elves) {
            return elves.stream()
                    .mapToInt(elf -> elf.stream().reduce(0, Integer::sum))
                    .max().orElseThrow();
        }
    }

    static class PartTwo {
        static int countCalories(List<List<Integer>> elves) {
            Queue<Integer> heap = new PriorityQueue<>();
            for (List<Integer> elf : elves) {
                heap.add(elf.stream().reduce(0, Integer::sum));
                if (heap.size() > 3) heap.remove();
            }
            return heap.stream().reduce(0, Integer::sum);
        }
    }

    static List<List<Integer>> preprocess(String file) throws IOException {
        List<List<Integer>> lists = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            List<Integer> nums = new ArrayList<>();
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.length() != 0) {
                    nums.add(Integer.parseInt(line));
                } else {
                    lists.add(nums);
                    nums = new ArrayList<>();
                }
            }
        }
        return lists;
    }

    public static void main(String[] args) throws IOException {
        List<List<Integer>> input = preprocess("src/main/resources/_2022/_01/input.txt");

        System.out.println(PartOne.countCalories(input));
        System.out.println(PartTwo.countCalories(input));
    }
}
