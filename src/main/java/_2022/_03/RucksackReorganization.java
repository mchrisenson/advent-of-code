package _2022._03;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

/**
 * Advent of Code 2022 <a href="https://adventofcode.com/2022/day/3">Day 3: Rucksack Reorganization</a>
 */
public class RucksackReorganization {

    static class PartOne {

        static int organize(List<String> sacks) {
            int sum = 0;
            for (String sack : sacks) {
                Set<Character> first = new HashSet<>(), second = new HashSet<>();

                for (char c : sack.substring(0,sack.length() / 2).toCharArray())
                    first.add(c);
                for (char c : sack.substring(sack.length() / 2).toCharArray())
                    second.add(c);

                first.retainAll(second);

                sum += priority(first.iterator().next());
            }
            return sum;
        }
    }

    static class PartTwo {
        static int organize(List<String> sacks) {
            int sum = 0;

            for (int i = 0; i < sacks.size(); i += 3) {
                Set<Character> intersection = null;

                for (int j = 0; j < 3; ++j) {
                    Set<Character> set = new HashSet<>();

                    for (char c : sacks.get(i + j).toCharArray())
                        set.add(c);

                    if (j == 0) intersection = set;
                    else intersection.retainAll(set);
                }

                sum += priority(intersection.iterator().next());
            }
            return sum;
        }
    }

    private static int priority(char c) {
        return c < 'a' ? c - 'A' + 27 : c - 'a' + 1;
    }

    static List<String> preprocess(String file) throws IOException {
        List<String> strings = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null)
                strings.add(line);
        }

        return strings;
    }

    public static void main(String[] args) throws IOException {
        List<String> input = preprocess("src/main/resources/_2022/_03/input.txt");

        System.out.println(PartOne.organize(input));
        System.out.println(PartTwo.organize(input));
    }
}
