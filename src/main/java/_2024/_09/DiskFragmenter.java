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

    static List<Integer> input;
    static List<Integer> disk;
    static List<Mem> files;
    /* Sets of free memory blocks sorted by disk index. List is indexed by the block sizes the sets contain. */
    static List<SortedSet<Mem>> frees;

    static long part1() {
        initDisk();
        for (int l = input.getFirst(), r = disk.size() - 1; l < r; l++) {
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
        initDisk();
        files = new ArrayList<>();
        frees = new ArrayList<>();
        for (int i = 0; i < 10; i++) frees.add(new TreeSet<>(Comparator.comparingInt(Mem::idx)));

        for (int i = 1, j = input.getFirst(), size; i < input.size(); i++, j += size) {
            size = input.get(i);
            if ((i & 1) == 0) files.add(new Mem(j, disk.subList(j, j + size)));
            else if (size > 0) frees.get(size).add(new Mem(j, disk.subList(j, j + size)));
        }
        for (Mem file : files.reversed()) {
            Mem free = malloc(file.blocks.size(), file.idx);
            if (free.equals(UNAVAILABlE)) continue;
            Collections.copy(free.blocks, file.blocks);
            Collections.fill(file.blocks, 0);
        }
        return IntStream.range(0, disk.size()).mapToLong(pos -> (long) pos * disk.get(pos)).sum();
    }

    /* Find the lowest indexed free space block with capacity for 'size' that occurs before 'end'. O(logN * 10) */
    static Mem malloc(int size, int end) {
        Optional<Integer> freeSize = IntStream.range(size, frees.size()).filter(s -> !frees.get(s).isEmpty())
                .filter(s -> frees.get(s).getFirst().idx < end).boxed()
                .min(Comparator.comparingInt(s -> frees.get(s).getFirst().idx));
        if (freeSize.isEmpty()) return UNAVAILABlE;
        Mem freeMem = frees.get(freeSize.get()).removeFirst();
        if (freeSize.get() - size > 0) { // insert remainder back into frees mapping
            Mem rem = new Mem(freeMem.idx + size, disk.subList(freeMem.idx + size, freeMem.idx + freeSize.get()));
            frees.get(freeSize.get() - size).add(rem);
        }
        return freeMem;
    }

    static void initDisk() {
        disk = new ArrayList<>();
        for (int i = 0; i < input.size(); i++) {
            int size = input.get(i);
            if ((i & 1) == 0) for (int j = size; j > 0; j--) disk.add(i / 2 + (i & 1)); // file
            else for (int j = size; j > 0; j--) disk.add(0); // mem space
        }
    }

    public static void main(String[] args) throws IOException {
        String line = Files.readString(Path.of(FILE));
        input = IntStream.range(0, line.length()).map(i -> line.charAt(i) - '0').boxed().toList();

        System.out.println("Part One: " + part1());
        System.out.println("Part Two: " + part2());
    }

    record Mem(int idx, List<Integer> blocks) {}
}
