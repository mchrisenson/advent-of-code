package _2023._09;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class MirageMaintenance {


    public static void main(String[] args) throws IOException {
        List<List<Long>> report = Stream.of(Files.readString(Path.of("src/main/resources/_2023/_09/input.txt")).split("\n"))
                .map(line -> Stream.of(line.split("\\s")).map(Long::parseLong).collect(Collectors.toList()))
                .toList();

        class Evaporate {
            static long f(List<Long> l) {
                List<Long> diffs = IntStream.range(0, l.size() - 1).mapToObj(i -> l.get(i + 1) - l.get(i)).toList();
                return !diffs.isEmpty() ? l.getLast() + f(diffs) : 0L;
            }
        }
        System.out.printf("Part One: %s\n", report.stream().mapToLong(Evaporate::f).sum());
        System.out.printf("Part Two: %s\n", report.stream().map(List::reversed).mapToLong(Evaporate::f).sum());
    }
}
