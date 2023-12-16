package _2022._11;

import java.io.IOException;
import java.util.*;

/**
 * Advent of code <a href="https://adventofcode.com/2022/day/11">Day 11: Monkey in the Middle</a>
 */
public class MonkeyInTheMiddle {

    static long calculateMonkeyBusiness(List<Monkey> monkeys, int rounds, ReliefFunction rf) {
        int n = monkeys.size();
        long[] inspections = new long[n];

        for (int round = 0; round < rounds; ++round) {
            for (int m = 0; m < n; ++m) {
                Monkey monkey = monkeys.get(m);
                while (!monkey.items.isEmpty()) {
                    ++inspections[m];
                    long item = monkey.items.poll();
                    item = monkey.operation(item);
                    item = rf.getRelief(item);
                    monkeys.get(monkey.test(item)).items.offer(item);
                }
            }
        }
        Arrays.sort(inspections);
        return inspections[n - 1] * inspections[n - 2];
    }

    interface ReliefFunction {
        long getRelief(long item);
    }

    static abstract class Monkey {
        final List<Long> startingItems;
        final Queue<Long> items;
        final Long divisor;
        final int throwTrue;
        final int throwFalse;

        Monkey(Collection<Long> startingItems, long divisor, int throwTrue, int throwFalse) {
            this.startingItems = new ArrayList<>(startingItems);
            this.items = new ArrayDeque<>(startingItems);
            this.divisor = divisor;
            this.throwTrue = throwTrue;
            this.throwFalse = throwFalse;
        }

        abstract long operation(long old);

        int test(long item) {
            return item % divisor == 0L ? throwTrue : throwFalse;
        }

        void reset() {
            items.clear();
            startingItems.forEach(items::offer);
        }
    }

    static final List<Monkey> monkeys = List.of(
            new Monkey(List.of(80L), 2L, 4, 3) { // 0
                public long operation(long old) {
                    return old * 5L;
                }
            }, new Monkey(List.of(75L, 83L, 74L), 7L, 5, 6) { // 1
                public long operation(long old) {
                    return old + 7L;
                }
            }, new Monkey(List.of(86L, 67L, 61L, 96L, 52L, 63L, 73L), 3L, 7, 0) { // 2
                public long operation(long old) {
                    return old + 5L;
                }
            }, new Monkey(List.of(85L, 83L, 55L, 85L, 57L, 70L, 85L, 52L), 17L, 1, 5) { // 3
                public long operation(long old) {
                    return old + 8L;
                }
            }, new Monkey(List.of(67L, 75L, 91L, 72L, 89L), 11L, 3, 1) { // 4
                public long operation(long old) {
                    return old + 4L;
                }
            }, new Monkey(List.of(66L, 64L, 68L, 92L, 68L, 77L), 19L, 6, 2) { // 5
                public long operation(long old) {
                    return old * 2L;
                }
            }, new Monkey(List.of(97L, 94L, 79L, 88L), 5L, 2, 7) { // 6
                public long operation(long old) {
                    return old * old;
                }
            }, new Monkey(List.of(77L, 85L), 13L, 4, 0) { // 7
                public long operation(long old) {
                    return old + 6L;
                }
            }
    );

    public static void main(String[] args) {
        System.out.println("Part One: " + calculateMonkeyBusiness(monkeys, 20, item -> item / 3L));

        monkeys.forEach(Monkey::reset);

        final long cumulativeProduct = monkeys.stream().mapToLong(m -> m.divisor).reduce(1L, (a, d) -> a * d);
        System.out.println("Part Two: " + calculateMonkeyBusiness(monkeys, 10_000, item -> item % cumulativeProduct));
    }
}
