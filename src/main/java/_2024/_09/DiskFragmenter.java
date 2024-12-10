package _2024._09;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.IntStream;

/**
 * <a href="https://adventofcode.com">Advent of Code</a>
 * <a href="https://adventofcode.com/2024">2024</a>
 * <a href="https://adventofcode.com/2024/day/9">Day 9: Disk Fragmenter</a>
 */
public class DiskFragmenter {
    static final String FILE = "src/main/resources/_2024/_09/input.txt";
    static final Mem UNAVAILABlE = new Mem(-1, Collections.emptyList());

    static char[] line;
    static List<Integer> disk;
    static List<SortedSet<Integer>> freeMap;

    static long part1() {
        List<Integer> disk = new ArrayList<>();

        for (int i = 0; i < line.length; i++) {
            int size = line[i] - '0';
            if ((i & 1) == 0) for (int j = size; j > 0; j--) disk.add(i / 2 + (i & 1));
            else for (int j = size; j > 0; j--) disk.add(0);
        }
        for (int l = line[0] - '0', r = disk.size() - 1; l < r; l++) {
            if (disk.get(l) != 0) continue;
            while (disk.get(r) == 0) r--;
            if (l < r) {
                disk.set(l, disk.get(r));
                disk.set(r--, 0);
            }
        }
        return IntStream.range(0, disk.size()).mapToLong(pos -> (long) pos * disk.get(pos)).sum();
    }

    static long part2() {
        disk = new ArrayList<>();
        freeMap = new ArrayList<>();
        for (int i = 0; i < 10; i++) freeMap.add(new TreeSet<>());

        for (int i = 0; i < line.length; i++) {
            int size = line[i] - '0';
            if ((i & 1) == 0) { // files
                for (int j = size; j > 0; j--) disk.add(i / 2 + (i & 1));
            } else { // free space
                if (size > 0) freeMap.get(size).add(disk.size());
                for (int j = size; j > 0; j--) disk.add(0);
            }
        }
        for (FileIterator it = new FileIterator(disk); it.hasNext(); ) {
            Mem file = it.next(), free = malloc(file.blocks.size(), file.idx);
            if (free.equals(UNAVAILABlE)) continue;
            Collections.copy(free.blocks, file.blocks);
            Collections.fill(file.blocks, 0);
        }
        return IntStream.range(0, disk.size()).mapToLong(pos -> (long) pos * disk.get(pos)).sum();
    }

    static class FileIterator implements Iterator<Mem> {
        List<Integer> disk;
        int l, r, prev;

        FileIterator(List<Integer> disk) {
            this.disk = disk;
            l = disk.size() - 1;
            prev = disk.getLast() + 1;
            findNext();
        }

        public boolean hasNext() { return r > 0; }

        public Mem next() {
            Mem mem = new Mem(l + 1, disk.subList(l + 1, r + 1));
            prev = disk.get(r);
            findNext();
            return mem;
        }

        void findNext() {
            r = l;
            while (r > 0 && (disk.get(r) == 0 || disk.get(r) > prev)) r--;
            l = r - 1;
            while (l > 0 && disk.get(l).equals(disk.get(r))) l--;
        }
    }

    static Mem malloc(int size, int end) {
        Optional<Integer> lowSize = IntStream.range(size, freeMap.size()).filter(s -> freeMap.get(s).getFirst() < end)
                .boxed().min(Comparator.comparingInt(s -> freeMap.get(s).getFirst()));
        if (lowSize.isEmpty()) return UNAVAILABlE;
        int idx = freeMap.get(lowSize.get()).removeFirst();
        if (lowSize.get() - size > 0) freeMap.get(lowSize.get() - size).add(idx + size);
        return new Mem(idx, disk.subList(idx, idx + size));
    }

    public static void main(String[] args) throws IOException {
        line = Files.readString(Path.of(FILE)).toCharArray();

        System.out.println("Part One: " + part1());
        System.out.println("Part Two: " + part2());
    }

    record Mem(int idx, List<Integer> blocks) {}
}
