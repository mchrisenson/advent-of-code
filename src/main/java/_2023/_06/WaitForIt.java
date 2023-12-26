package _2023._06;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class WaitForIt {

    static final String FILE = "src/main/resources/_2023/_06/input.txt";

    record Race(long time, long dist) {
        long ways() {
            // quadratic formula: x = (-b +- sqrt(b * b + 4 * a * c)) / 2 * a
            double a = -1, b = time, c = -(dist + 1);
            long x = (long) Math.ceil((-b + Math.sqrt(b * b - 4 * a * c)) / 2 * a);
            return time - 2L * x + 1;
        }
    }

    static List<Race> parseInputPart1() throws IOException {
        List<List<Long>> lists = Stream.of(Files.readString(Path.of(FILE)).split("\n"))
                .map(s -> Stream.of(s.split("\\s+")).skip(1).map(Long::parseLong).toList()).toList();
        return IntStream.range(0, lists.getFirst().size())
                .mapToObj(i -> new Race(lists.get(0).get(i), lists.get(1).get(i))).toList();
    }

    static Race parseInputPart2() throws IOException {
        List<Long> longs = Stream.of(Files.readString(Path.of(FILE)).split("\n"))
                .map(s -> Stream.of(s.split("\\s+")).skip(1).collect(Collectors.joining()))
                .map(Long::parseLong).toList();
        return new Race(longs.getFirst(), longs.getLast());
    }

    public static void main(String[] args) throws IOException {
        System.out.printf("Part One: %d\n", parseInputPart1().stream().mapToLong(Race::ways).reduce(1, (a, n) -> a * n));
        System.out.printf("Part Two: %d\n", parseInputPart2().ways());
    }
}
