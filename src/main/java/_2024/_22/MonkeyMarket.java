package _2024._22;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.IntStream;

/**
 * <a href="https://adventofcode.com">Advent of Code</a>
 * <a href="https://adventofcode.com/2024">2024</a>
 * <a href="https://adventofcode.com/2024/day/22">Day 22: Monkey Market</a>
 */

public class MonkeyMarket {
    static final String FILE = "src/main/resources/_2024/_22/input.txt";

    static int part2(List<Long> nums) {
        Map<List<Integer>, Integer> map = new HashMap<>();
        for (long num : nums) {
            List<Integer> sec = secrets(num).stream().map(n -> (int) (n % 10L)).toList();
            List<Integer> deltas = IntStream.range(1, sec.size()).mapToObj(i -> sec.get(i) - sec.get(i - 1)).toList();
            Set<List<Integer>> visited = new HashSet<>();
            for (int i = 4; i < deltas.size(); i++) {
                List<Integer> seq = new ArrayList<>(deltas.subList(i - 4, i));
                if (visited.add(seq)) map.merge(seq, sec.get(i), Integer::sum);
            }
        }
        return map.values().stream().mapToInt(t -> t).max().orElseThrow();
    }

    static List<Long> secrets(long num) {
        List<Long> result = new ArrayList<>();
        result.add(num);
        for (int i = 0; i < 2000; i++) {
            result.add(num = nextSecret(num));
        }
        return result;
    }

    static long nextSecret(long num) {
        num = (num ^ (num << 6)) % 16777216;
        num = (num ^ (num >> 5)) % 16777216;
        return (num ^ (num << 11)) % 16777216;
    }

    public static void main(String[] args) throws IOException {
        List<Long> nums = Files.readAllLines(Path.of(FILE)).stream().map(Long::parseLong).toList();
        System.out.println("Part One: " + nums.stream().map(MonkeyMarket::secrets).mapToLong(List::getLast).sum());
        System.out.println("Part Two: " + part2(nums));
    }
}