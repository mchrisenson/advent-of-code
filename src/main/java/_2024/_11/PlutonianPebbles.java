package _2024._11;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Stream;

/**
 * <a href="https://adventofcode.com">Advent of Code</a>
 * <a href="https://adventofcode.com/2024">2024</a>
 * <a href="https://adventofcode.com/2024/day/11">Day 11: Plutonian Pebbles</a>
 */
public class PlutonianPebbles {
    static final String FILE = "src/main/resources/_2024/_11/input.txt";

    static Map<BS, Long> cache = new HashMap<>();

    static long count(BS bs) {
        if (bs.blinks == 0) return 1;
        if (cache.containsKey(bs)) return cache.get(bs);
        long stone;
        String digits;
        if (bs.stone == 0L) {
            stone = count(new BS(bs.blinks - 1, 1L));
        } else if ((digits = Long.valueOf(bs.stone).toString()).length() % 2 == 0) {
            stone = Stream.of(digits.substring(0, digits.length() / 2), digits.substring(digits.length() / 2))
                    .map(Long::valueOf).mapToLong(s -> count(new BS(bs.blinks - 1, s))).sum();
        } else {
            stone = count(new BS(bs.blinks - 1, bs.stone * 2024L));
        }
        cache.put(bs, stone);
        return stone;
    }

    public static void main(String[] args) throws IOException {
        List<Long> stones = Arrays.stream(Files.readString(Path.of(FILE)).split(" ")).map(Long::parseLong).toList();

        long part1 = stones.stream().mapToLong(s -> count(new BS(25, s))).sum();
        System.out.println("Part One: " + part1);

        long part2 = stones.stream().mapToLong(s -> count(new BS(75, s))).sum();
        System.out.println("Part Two: " + part2);
    }

    record BS(int blinks, long stone) {}
}

