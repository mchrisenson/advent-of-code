package _2023._12;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * <a href="https://adventofcode.com">Advent of Code</a>
 * <a href="https://adventofcode.com/2023">2023</a>
 * <a href="https://adventofcode.com/2023/day/12">Day 12: Hot Springs</a>
 */
public class HotSprings {
    static final String FILE = "src/main/resources/_2023/_12/test.txt";
    static List<Condition> records = new ArrayList<>();

    static int backtrack(char[] spring, int[] check, char prev, int i) {
        return 0;
    }

    public static void main(String[] args) throws IOException {
        for (String line : Files.readAllLines(Path.of(FILE))) {
            String[] s = line.split(" ");
            char[] spring = s[0].toCharArray();
            int[] check = Arrays.stream(s[1].split(",")).mapToInt(Integer::parseInt).toArray();
            records.add(new Condition(spring, check));
        }

        System.out.println("Part One: ");

        System.out.println("Part Two: ");
    }

    record Condition(char[] spring, int[] check) {
    }
}
