package _2023._05;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class IfYouGiveASeedAFertilizer {

    static final String FILE = "src/main/resources/_2023/_05/input.txt";

    static Almanac parseInput() throws IOException {
        List<String> categories = List.of(Files.readString(Path.of(FILE)).split("\n\n"));

        return new Almanac(
                Stream.of(categories.getFirst().split(":")[1].trim().split("\\s"))
                        .map(Long::valueOf).toList(),
                categories.stream().skip(1).map(category -> {
                    NavigableMap<Long, Long> tree = new TreeMap<>(Map.of(Long.MAX_VALUE, 0L));
                    Stream.of(category.split("\n")).skip(1)
                            .forEach(range -> {
                                List<Long> sdr = Stream.of(range.split("\\s")).map(Long::valueOf).toList();
                                Long sourceRangeFloor = sdr.get(1), sourceRangeCeiling = sdr.get(1) + sdr.get(2) - 1L, sourceDestinationDiff = sdr.get(0) - sdr.get(1);
                                if (!tree.containsKey(sourceRangeFloor - 1L))
                                    tree.put(sourceRangeFloor - 1L, 0L);
                                tree.put(sourceRangeCeiling, sourceDestinationDiff);
                            });
                    return tree;
                }).toList());
    }

    record Almanac(List<Long> seeds, List<NavigableMap<Long, Long>> maps) {
    }

    record Interval(long lower, long upper) implements Comparable<Interval> {
        @Override
        public int compareTo(Interval that) {
            return this.lower > that.upper ? 1 : this.upper < that.lower ? -1 : 0;
        }
    }

    static void addToTree(NavigableSet<Interval> tree, long lower, long upper) {
        Interval n = new Interval(lower, upper);
        NavigableSet<Interval> overlapping = tree.subSet(n, true, n, true);
        for (Interval e : overlapping) {
            lower = Math.min(e.lower, lower);
            upper = Math.max(e.upper, upper);
        }
        tree.removeAll(overlapping);
        tree.add(new Interval(lower, upper));
    }

    public static void main(String[] args) throws IOException {
        Almanac almanac = parseInput();

        System.out.printf("Part One: %s\n", almanac.seeds.stream()
                .mapToLong(seed -> almanac.maps.stream().reduce(seed, (s, m) -> s + m.ceilingEntry(s).getValue(), (s1, s2) -> s1))
                .min().orElseThrow());

        System.out.printf("Part Two: %s\n", almanac.maps.stream().reduce(IntStream.range(0, almanac.seeds.size() / 2).map(i -> i * 2)
                .mapToObj(i -> new Interval(almanac.seeds.get(i), almanac.seeds.get(i) + almanac.seeds.get(i + 1) - 1))
                .collect(Collectors.toCollection(TreeSet::new)), (prev, map) -> {
            TreeSet<Interval> curr = new TreeSet<>();
            for (Interval p : prev) {
                long lower = p.lower;
                do {
                    Map.Entry<Long, Long> mappingCeiling = map.ceilingEntry(lower);
                    long upper = Math.min(p.upper, mappingCeiling.getKey());
                    addToTree(curr, lower + mappingCeiling.getValue(), upper + mappingCeiling.getValue());
                    curr.add(new Interval(lower + mappingCeiling.getValue(), upper + mappingCeiling.getValue()));
                    lower = upper + 1;
                } while (p.upper > lower);
            }
            return curr;
        }, (o1, o2) -> o1).first().lower);
    }
}
