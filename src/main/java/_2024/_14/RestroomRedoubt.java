package _2024._14;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <a href="https://adventofcode.com">Advent of Code</a>
 * <a href="https://adventofcode.com/2024">2024</a>
 * <a href="https://adventofcode.com/2024/day/14">Day 14: Restroom Redoubt</a>
 */
public class RestroomRedoubt {
    static final String FILE = "src/main/resources/_2024/_14/input.txt";
    static final int W = 101, H = 103;
    static List<Robot> robots = new ArrayList<>();

    static int part1() {
        int[] quads = new int[4];
        simulate(100).forEach(p -> {
            if (p.x == W / 2 || p.y == H / 2) return;
            quads[(p.x > W / 2 ? 1 : 0) | (p.y > H / 2 ? 2 : 0)]++;
        });
        return Arrays.stream(quads).reduce(1, (a, i) -> a * i);
    }

    static int part2() {
        int tx = 0, ty = 0;
        double minVarX = W * W, minVarY = H * H, varX, varY;
        // the tree appears when the robots are at their most dense positions relative to one another
        for (int t = 0; t < Math.max(W, H); t++) {
            List<Pos> p = simulate(t);
            if ((varX = variance(p.stream().mapToDouble(Pos::x).toArray())) < minVarX) {
                minVarX = varX;
                tx = t;
            }
            if ((varY = variance(p.stream().mapToDouble(Pos::y).toArray())) < minVarY) {
                minVarY = varY;
                ty = t;
            }
        }
        for (int t = Math.max(tx, ty); t < W * H; t++) {
            if ((t - tx) % W == 0 && (t - ty) % H == 0) return t;
        }
        throw new RuntimeException("Unable to find the tree.");
    }

    static List<Pos> simulate(int seconds) {
        return robots.stream().map(r -> {
            int x = ((r.p.x + r.v.x * seconds) % W + W) % W;
            int y = ((r.p.y + r.v.y * seconds) % H + H) % H;
            return new Pos(x, y);
        }).toList();
    }

    public static void main(String[] args) throws IOException {
        Matcher matcher = Pattern.compile("p=(?<px>\\d{1,3}),(?<py>\\d{1,3}) v=(?<vx>-?\\d{1,3}),(?<vy>-?\\d{1,3})")
                .matcher(Files.readString(Path.of(FILE)));
        while (matcher.find()) {
            int px = Integer.parseInt(matcher.group("px")), py = Integer.parseInt(matcher.group("py"));
            int vx = Integer.parseInt(matcher.group("vx")), vy = Integer.parseInt(matcher.group("vy"));
            robots.add(new Robot(new Pos(px, py), new Pos(vx, vy)));
        }
        System.out.println("Part One: " + part1());

        int answer2 = part2();
        System.out.println("Part Two: " + answer2);
        print(new HashSet<>(simulate(answer2)));
    }

    record Pos(int x, int y) {}

    record Robot(Pos p, Pos v) {}

    static double variance(double[] nums) {
        double avg = Arrays.stream(nums).reduce(0, Double::sum) / nums.length;
        return Arrays.stream(nums).map(d -> Math.pow(d - avg, 2)).sum() / nums.length;
    }

    static void print(Set<Pos> set) {
        for (int y = 0; y < H; y++) {
            for (int x = 0; x < W; x++) {
                Pos p = new Pos(x, y);
                System.out.print(set.contains(p) ? '0' : y == H / 2 || x == W / 2 ? ' ' : '.');
            }
            System.out.println();
        }
        System.out.println();
    }
}
