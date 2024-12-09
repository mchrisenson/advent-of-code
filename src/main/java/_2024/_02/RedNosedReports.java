package _2024._02;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * <a href="https://adventofcode.com">Advent of Code</a>
 * <a href="https://adventofcode.com/2024">2024</a>
 * <a href="https://adventofcode.com/2024/day/2">Day 2: Red-Nosed Reports</a>
 */
public class RedNosedReports {
    static final String FILE = "src/main/resources/_2024/_02/input.txt";

    static boolean safe(List<Integer> report) {
        boolean sign = report.get(1) - report.get(0) >= 0;
        for (int i = 1; i < report.size(); i++) {
            int diff = report.get(i) - report.get(i - 1), absDiff = Math.abs(diff);
            if (absDiff < 1 || absDiff > 3 || sign && diff < 0 || !sign && diff > 0) return false;
        }
        return true;
    }

    public static void main(String[] args) throws IOException {
        List<List<Integer>> reports;
        try (Stream<String> lines = Files.lines(Path.of(FILE))) {
            reports = lines.map(l -> Arrays.stream(l.split(" ")).map(Integer::parseInt).toList()).toList();
        }

        long answer = reports.stream().filter(RedNosedReports::safe).count();
        System.out.println("Part One: " + answer);

        answer = reports.stream().filter(r -> IntStream.range(0, r.size()).anyMatch(i ->
                safe(IntStream.range(0, r.size()).filter(j -> j != i).mapToObj(r::get).toList())
        )).count();
        System.out.println("Part Two: " + answer);
    }
}