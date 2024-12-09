package _2024._01;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * <a href="https://adventofcode.com">Advent of Code</a>
 * <a href="https://adventofcode.com/2024">2024</a>
 * <a href="https://adventofcode.com/2024/day/1">Day 1: Historian Hysteria</a>
 */
public class HistorianHysteria {
    static final String FILE = "src/main/resources/_2024/_01/input.txt";

    public static void main(String[] args) throws IOException {
        List<String> lines = Files.readAllLines(Path.of(FILE));
        List<Integer> left = lines.stream().map(l -> l.split(" {3}")[0]).map(Integer::parseInt).sorted().toList();
        List<Integer> right = lines.stream().map(l -> l.split(" {3}")[1]).map(Integer::parseInt).sorted().toList();

        int answer = IntStream.range(0, left.size()).map(i -> Math.abs(left.get(i) - right.get(i))).sum();
        System.out.println("Part One: " + answer);

        Map<Integer, Integer> rightCount = right.stream()
                .collect(Collectors.toMap(Function.identity(), v -> 1, Integer::sum));

        answer = left.stream().mapToInt(num -> num * rightCount.getOrDefault(num, 0)).sum();
        System.out.println("Part Two: " + answer);
    }
}
