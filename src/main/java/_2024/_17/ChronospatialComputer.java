package _2024._17;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

/**
 * <a href="https://adventofcode.com">Advent of Code</a>
 * <a href="https://adventofcode.com/2024">2024</a>
 * <a href="https://adventofcode.com/2024/day/17">Day 17: Chronospatial Computer</a>
 * <blockquote><pre>
 * while (a != 0)
 *     b = a & 7
 *     b = b ^ b
 *     c = a >> b
 *     a = a >> a
 *     b = b ^ c
 *     print b & 7
 *     a = a >> 3
 * </pre></blockquote>
 */
public class ChronospatialComputer {
    static final String FILE = "src/main/resources/_2024/_17/input.txt";
    static List<Integer> prog;

    static long find(long a, int i) {
        if (new Runnable(a, 0, 0).run().equals(prog)) return a;
        if (new Runnable(a, 0, 0).run().equals(prog.subList(i, prog.size())) || i == prog.size()) {
            return LongStream.range(0, 8).map(n -> find(8 * a + n, i - 1)).min().orElse(Long.MAX_VALUE);
        }
        return Long.MAX_VALUE;
    }

    static class Runnable {
        long a, b, c;

        Runnable(long a, long b, long c) {
            this.a = a;
            this.b = b;
            this.c = c;
        }

        List<Integer> run() {
            List<Integer> out = new ArrayList<>();
            for (int i = 0; i < prog.size(); i += 2) {
                int op = prog.get(i + 1);
                switch (prog.get(i)) {
                    case 0 -> a >>= combo(op);
                    case 1 -> b ^= op;
                    case 2 -> b = combo(op) & 7;
                    case 3 -> i = (a != 0 ? op - 2 : i);
                    case 4 -> b = b ^ c;
                    case 5 -> out.add((int) (combo(op) & 7));
                    case 6 -> b = a >> combo(op);
                    case 7 -> c = a >> combo(op);
                }
            }
            return out;
        }

        long combo(int op) {
            return switch (op) {
                case 4 -> a;
                case 5 -> b;
                case 6 -> c;
                default -> op;
            };
        }
    }

    public static void main(String[] args) throws IOException {
        Matcher matcher = Pattern.compile("Register A: (\\d+)\nRegister B: (\\d+)\nRegister C: (\\d+)\n\nProgram: ([\\d,]+\\d)")
                .matcher(Files.readString(Path.of(FILE)));
        if (!matcher.find()) throw new RuntimeException("Unable to parse input!");
        long a = Long.parseLong(matcher.group(1)), b = Long.parseLong(matcher.group(2)), c = Long.parseLong(matcher.group(3));
        prog = Arrays.stream(matcher.group(4).split(",")).map(Integer::parseInt).toList();

        System.out.println("Part One: " + new Runnable(a, b, c).run().stream().map(String::valueOf).collect(Collectors.joining(",")));

        System.out.println("Part Two: " + find(0, prog.size()));
    }
}
