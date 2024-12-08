package _2024._07;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

/**
 * <a href="https://adventofcode.com">Advent of Code</a>
 * <a href="https://adventofcode.com/2024">2024</a>
 * <a href="https://adventofcode.com/2024/day/7">Day 7: Bridge Repair</a>
 */
public class BridgeRepair {
    static final String FILE = "src/main/resources/_2024/_07/input.txt";

    static boolean test(long[] nums, long val, int i, boolean concat) {
        if (i == 0) return val == nums[0];
        long quo = val / nums[i], mod = val % nums[i];
        if (mod == 0 && test(nums, quo, i - 1, concat)) return true;
        if (concat) {
            mod = (int) Math.pow(10, Math.floor(Math.log10(nums[i])) + 1);
            if (val % mod == nums[i] && test(nums, val / mod, i - 1, true)) return true;
        }
        return test(nums, val - nums[i], i - 1, concat);
    }

    public static void main(String[] args) throws IOException {
        List<Eq> eqs;
        try (Stream<String> lines = Files.lines(Path.of(FILE))) {
            eqs = lines.map(l -> l.split(": "))
                    .map(s -> new Eq(Long.parseLong(s[0]), Arrays.stream(s[1].split(" ")).mapToLong(Long::parseLong).toArray())).toList();
        }

        long answer = eqs.stream().filter(eq -> test(eq.nums, eq.val, eq.nums.length - 1, false))
                .mapToLong(eq -> eq.val).sum();
        System.out.println("Part One: " + answer);

        answer = eqs.stream().filter(eq -> test(eq.nums, eq.val, eq.nums.length - 1, true))
                .mapToLong(eq -> eq.val).sum();
        System.out.println("Part Two: " + answer);
    }

    record Eq(long val, long[] nums) {}
}
