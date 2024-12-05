package _2024._03;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MullItOver {
    static final String FILE = "src/main/resources/_2024/_03/input.txt";

    static final Pattern mulPattern = Pattern.compile("mul\\((?<x>\\d{1,3}),(?<y>\\d{1,3})\\)");

    static int exec(String line) {
        Matcher matcher = mulPattern.matcher(line);
        return Stream.iterate(0, matcher::find, start -> matcher.end())
                .mapToInt(s -> Integer.parseInt(matcher.group("x")) * Integer.parseInt(matcher.group("y")))
                .sum();
    }

    public static void main(String[] args) throws IOException {
        String line;
        try (Stream<String> lines = Files.lines(Path.of(FILE))) {
            line = lines.collect(Collectors.joining());
        }

        System.out.printf("Part One: %d\n", exec(line));

        int answer = 0;
        Matcher doMatch = Pattern.compile("do\\(\\)").matcher(line);
        Matcher dontMatch = Pattern.compile("don't\\(\\)").matcher(line);
        for (int start = 0, end; start < line.length(); start = doMatch.find(end) ? doMatch.end() : line.length()) {
            answer += exec(line.substring(start, end = dontMatch.find(start) ? dontMatch.end() : line.length()));
        }
        System.out.printf("Part Two: %s\n", answer);
    }
}
