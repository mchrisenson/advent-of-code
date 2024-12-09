package _2024._05;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.stream.IntStream;

/**
 * <a href="https://adventofcode.com">Advent of Code</a>
 * <a href="https://adventofcode.com/2024">2024</a>
 * <a href="https://adventofcode.com/2024/day/5">Day 5: Print Queue</a>
 */
public class PrintQueue {
    static final String FILE = "src/main/resources/_2024/_05/input.txt";

    public static void main(String[] args) throws IOException {
        Set<List<String>> rules = new HashSet<>();
        List<List<String>> prints = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(FILE))) {
            String line;
            while ((line = reader.readLine()) != null && !line.isEmpty()) {
                rules.add(Arrays.stream(line.split("\\|")).toList());
            }
            while ((line = reader.readLine()) != null) {
                prints.add(Arrays.stream(line.split(",")).toList());
            }
        }

        int answer = prints.stream().filter(pages ->
                IntStream.range(0, pages.size() - 1).mapToObj(i -> pages.subList(i, i + 2)).allMatch(rules::contains)
        ).map(pages -> pages.get(pages.size() / 2)).mapToInt(Integer::parseInt).sum();
        System.out.println("Part One: " + answer);

        List<List<String>> sorted = prints.stream().map(pages ->
                pages.stream().sorted((p1, p2) -> rules.contains(Arrays.asList(p1, p2)) ? -1 : 1).toList()
        ).toList();
        answer = IntStream.range(0, prints.size()).filter(i -> !prints.get(i).equals(sorted.get(i)))
                .mapToObj(sorted::get).map(pages -> pages.get(pages.size() / 2)).mapToInt(Integer::parseInt).sum();
        System.out.println("Part Two: " + answer);
    }
}
