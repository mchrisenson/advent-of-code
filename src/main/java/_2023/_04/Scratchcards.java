package _2023._04;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Scratchcards {

    static final String FILE = "src/main/resources/_2023/_04/input.txt";

    static List<List<Set<Integer>>> parseInput() throws IOException {
        try (Stream<String> lines = Files.lines(Path.of(FILE))) {
            return lines.map(line -> Arrays.stream(line.split(":")[1].split("\\|"))
                    .map(numbers -> Arrays.stream(numbers.trim().split("\\s+")).map(Integer::parseInt).collect(Collectors.toSet()))
                    .toList()).toList();
        }
    }

    public static void main(String[] arg) throws IOException {
        System.out.printf("Part One: %s\n", parseInput().stream()
                .map(card -> card.getLast().stream().filter(card.getFirst()::contains)).map(Stream::count)
                .mapToInt(count -> (int) Math.pow(2, count - 1)).sum());

        List<List<Integer>> lists = parseInput().stream()
                .map(card -> card.getLast().stream().filter(card.getFirst()::contains)).map(s -> (int) s.count())
                .map(copies -> Stream.of(copies, 1).collect(Collectors.toList())).toList();

        IntStream.range(0, lists.size() - 1)
                .forEach(i -> IntStream.range(i + 1, Math.min(i + lists.get(i).get(0) + 1, lists.size()))
                        .forEach(j -> lists.get(j).set(1, lists.get(i).get(1) + lists.get(j).get(1))));

        System.out.printf("Part Two: %s\n", lists.stream().mapToInt(List::getLast).sum());
    }
}
