package _2024._13;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <a href="https://adventofcode.com">Advent of Code</a>
 * <a href="https://adventofcode.com/2024">2024</a>
 * <a href="https://adventofcode.com/2024/day/13">Day 13: Claw Contraption</a>
 * <code>
 * <p>
 * Equations:<br/>
 * a * x1 + b * x2 = x3,  a * y1 + b * y2 = y3<br/>
 * y2 * (a * x1 + b * x2) = y2 * x3,  x2 * (a * y1 + b * y2) = x2 * y3<br/>
 * a * x1 * y2 + b * x2 * y2 = y2 * x3,  a * x2 * y1 + b * x2 * y2 = x2 * y3<br/>
 * a * x1 * y2 + b * x2 * y2 - y2 * x3 = 0,  a * x2 * y1 + b * x2 * y2 - x2 * y3 = 0<br/>
 * a * x1 * y2 + b * x2 * y2 - y2 * x3 = a * x2 * y1 + b * x2 * y2 - x2 * y3<br/>
 * a * x1 * y2 - y2 * x3 = a * x2 * y1 - x2 * y3<br/>
 * a * x1 * y2 - a * x2 * y1 = y2 * x3 - x2 * y3<br/>
 * a * (x1 * y2 - x2 * y1) = y2 * x3 - x2 * y3<br/>
 * a = (y2 * x3 - x2 * y3) / (x1 * y2 - x2 * y1)<br/>
 * <br/>
 * a * x1 + b * x2 = x3<br/>
 * b = (x3 - a * x1) / x2;
 * </p>
 * </code>
 */
public class ClawContraption {
    static final String FILE = "src/main/resources/_2024/_13/input.txt";

    static long tokens(Machine m) {
        long x1 = m.a.x, x2 = m.b.x, x3 = m.prize.x, y1 = m.a.y, y2 = m.b.y, y3 = m.prize.y;
        long a = (y2 * x3 - x2 * y3) / (x1 * y2 - x2 * y1);
        long b = (x3 - a * x1) / x2;
        return a * x1 + b * x2 == x3 && a * y1 + b * y2 == y3 ? a * 3 + b : 0L;
    }

    public static void main(String[] args) throws IOException {
        List<Machine> machines = new ArrayList<>();
        Matcher matcher = Pattern.compile("Button A: X\\+(?<ax>\\d{2}), Y\\+(?<ay>\\d{2})\n" +
                "Button B: X\\+(?<bx>\\d{2}), Y\\+(?<by>\\d{2})\n" +
                "Prize: X=(?<px>\\d+), Y=(?<py>\\d+)").matcher(Files.readString(Path.of(FILE)));
        while (matcher.find()) {
            long ax = Long.parseLong(matcher.group("ax")), ay = Long.parseLong(matcher.group("ay"));
            long bx = Long.parseLong(matcher.group("bx")), by = Long.parseLong(matcher.group("by"));
            long px = Long.parseLong(matcher.group("px")), py = Long.parseLong(matcher.group("py"));
            machines.add(new Machine(new Pos(ax, ay), new Pos(bx, by), new Pos(px, py)));
        }

        System.out.println("Part One: " + machines.stream().mapToLong(ClawContraption::tokens).sum());

        System.out.println("Part Two: " + machines.stream()
                .map(m -> new Machine(m.a, m.b, m.prize.next(new Pos(10_000_000_000_000L, 10_000_000_000_000L))))
                .mapToLong(ClawContraption::tokens).sum());
    }

    record Machine(Pos a, Pos b, Pos prize) {}

    record Pos(long x, long y) {
        Pos next(Pos move) { return new Pos(this.x + move.x, this.y + move.y); }
    }
}
