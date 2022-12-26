package _2022._17;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Stream;

/**
 * Advent of Code <a href="https://adventofcode.com/2022/day/17">Day 17: Pyroclastic Flow</a>
 */
public class PyroclasticFlow {
    static final String FILE = "src/main/resources/_2022/_17/input.txt";

    static final Rock H_LINE = new Rock(List.of(new Point(0, 0), new Point(1, 0), new Point(2, 0), new Point(3, 0)));
    static final Rock PLUS = new Rock(List.of(new Point(1, 0), new Point(0, 1), new Point(1, 1), new Point(2, 1), new Point(1, 2)));
    static final Rock L = new Rock(List.of(new Point(0, 0), new Point(1, 0), new Point(2, 0), new Point(2, 1), new Point(2, 2)));
    static final Rock V_LINE = new Rock(List.of(new Point(0, 0), new Point(0, 1), new Point(0, 2), new Point(0, 3)));
    static final Rock SQUARE = new Rock(List.of(new Point(0, 0), new Point(1, 0), new Point(0, 1), new Point(1, 1)));
    static final List<Rock> SHAPES = List.of(H_LINE, PLUS, L, V_LINE, SQUARE);

    static long findTowerHeight(String jetSequence, long rockLimit) {
        int n = jetSequence.length(), shape = 0, height = 0;
        long rockCount = 0, skippedCycleHeight = 0;
        boolean cycleFound = false;

        BitSet bitSet = new BitSet(1 << 18);
        Map<HashKey, Stats> hashMap = new HashMap<>();

        Rock rock = SHAPES.get(shape).shift(2, 3);

        for (int jet = 0; rockCount < rockLimit; jet = ++jet % n) {
            // shift left or right
            int x = jetSequence.charAt(jet) == '<' ? -1 : 1;
            Rock shifted = rock.shift(x, 0);

            if (shifted.points.stream().allMatch(p -> p.x >= 0 && p.x < 7 && !bitSet.get(p.y << 3 | p.x)))
                rock = shifted;
            // shift down
            shifted = rock.shift(0, -1);

            if (shifted.points.stream().allMatch(p -> p.y >= 0 && !bitSet.get(p.y << 3 | p.x))) {
                rock = shifted;
                continue;
            }
            // place rock
            rock.points.forEach(p -> bitSet.set(p.y << 3 | p.x));
            ++rockCount;

            height = (bitSet.length() >> 3) + (((bitSet.length() & 7) == 0) ? 0 : 1);

            if (!cycleFound) {
                int last64BytesHash = bitSet.get(Math.max(bitSet.length() - 512, 0), bitSet.length()).hashCode();
                HashKey hashKey = new HashKey(jet, (byte) shape, last64BytesHash);

                if (!hashMap.containsKey(hashKey))
                    hashMap.put(hashKey, new Stats((int) rockCount, height));
                else {
                    cycleFound = true;
                    Stats lastCycle = hashMap.get(hashKey);
                    long rocksPerCycle = rockCount - lastCycle.count;
                    int heightPerCycle = height - lastCycle.height;
                    long cyclesToSkip = (rockLimit - rockCount) / rocksPerCycle;
                    rockCount += cyclesToSkip * rocksPerCycle;
                    skippedCycleHeight = heightPerCycle * cyclesToSkip;
                }
            }
            // next rock
            shape = ++shape % 5;
            rock = SHAPES.get(shape).shift(2, height + 3);

        }
        return height + skippedCycleHeight;
    }

    record HashKey(int jet, byte shape, int last64BytesHash) {
    }

    record Stats(int count, int height) {
    }

    record Rock(List<Point> points) {

        Rock shift(int x, int y) {
            return new Rock(this.points.stream().map(p -> p.shift(x, y)).toList());
        }
    }

    record Point(int x, int y) {
        Point shift(int x, int y) {
            return new Point(this.x + x, this.y + y);
        }
    }

    static String getJetSequence() {
        try (Stream<String> lines = Files.newBufferedReader(Paths.get(FILE)).lines()) {
            return lines.findFirst().orElseThrow();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        String jetSequence = getJetSequence();
        System.out.printf("Part One: %d%n", findTowerHeight(jetSequence, 2022));
        System.out.printf("Part Two: %d%n", findTowerHeight(jetSequence, 1_000_000_000_000L));
    }
}
