package _2023._01;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <a href="https://adventofcode.com">Advent of Code</a>
 * <a href="https://adventofcode.com/2023">2023</a>
 * <a href="https://adventofcode.com/2023/day/1">Day 1: Trebuchet?!</a>
 */
public class Trebuchet {
    static final String FILE = "src/main/resources/_2023/_01/input.txt";

    static final Map<String, String> wordToInt = Map.of(
            "one", "1",
            "two", "2",
            "three", "3",
            "four", "4",
            "five", "5",
            "six", "6",
            "seven", "7",
            "eight", "8",
            "nine", "9"
    );

    static List<Integer> getCalibrationValues(List<String> lines, Pattern pattern) {
        return lines.stream().map(line -> {
            Matcher m = pattern.matcher(line);
            if (!m.find()) throw new RuntimeException("Unable to parse line \"%s\"".formatted(line));
            String first = m.group(1), last = first;
            for (int i = m.start(1) + 1; i < line.length(); ++i) {
                if (m.find(i)) {
                    last = m.group(1);
                    i = m.start(1);
                }
            }
            return Integer.parseInt(wordToInt.getOrDefault(first, first) + wordToInt.getOrDefault(last, last));
        }).toList();
    }

    static List<String> parseInput() throws IOException {
        try (BufferedReader reader = Files.newBufferedReader(Paths.get(FILE))) {
            return reader.lines().toList();
        }
    }

    public static void main(String[] args) throws IOException {
        List<String> lines = parseInput();

        Pattern pattern1 = Pattern.compile("(\\d)");
        System.out.printf("Part One: %s%n", getCalibrationValues(lines, pattern1).stream().reduce(0, Integer::sum));

        Pattern pattern2 = Pattern.compile("(\\d|one|two|three|four|five|six|seven|eight|nine)");
        System.out.printf("Part Two: %s%n", getCalibrationValues(lines, pattern2).stream().reduce(0, Integer::sum));
    }
}
