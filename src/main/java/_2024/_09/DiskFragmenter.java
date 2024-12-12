package _2024._09;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.function.BiFunction;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * <a href="https://adventofcode.com">Advent of Code</a>
 * <a href="https://adventofcode.com/2024">2024</a>
 * <a href="https://adventofcode.com/2024/day/9">Day 9: Disk Fragmenter</a>
 */
public class DiskFragmenter {
    static final String FILE = "src/main/resources/_2024/_09/input.txt";

    static List<Integer> input;
    static List<Integer> disk;
    static List<Deque<Integer>> files;
    static List<Deque<Integer>> frees;

    static long defrag(BiFunction<Integer, Integer, Deque<Integer>> malloc) {
        initDisk();
        files = new ArrayList<>();
        frees = new LinkedList<>();

        for (int i = 1, j = input.getFirst(), size; i < input.size(); i++, j += size) {
            size = input.get(i);
            if ((i & 1) == 0) files.add(IntStream.range(j, j + size).boxed().collect(Collectors.toCollection(ArrayDeque::new)));
            else if (size > 0) frees.add(IntStream.range(j, j + size).boxed().collect(Collectors.toCollection(ArrayDeque::new)));
        }
        for (Deque<Integer> file : files.reversed()) {
            Deque<Integer> free = malloc.apply(file.size(), file.getFirst());
            while (!free.isEmpty()) {
                int fileIdx = file.removeLast();
                disk.set(free.removeFirst(), disk.get(fileIdx));
                disk.set(fileIdx, 0);
            }
        }
        return IntStream.range(0, disk.size()).mapToLong(pos -> (long) pos * disk.get(pos)).sum();
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

        long answer1 = defrag((size, end) -> {
            Deque<Integer> result = new ArrayDeque<>(size);
            for (ListIterator<Deque<Integer>> it = frees.listIterator(); it.hasNext(); ) {
                Deque<Integer> free = it.next();
                while (size > 0 && !free.isEmpty()) {
                    int idx = free.removeFirst();
                    if (idx > end) return result;
                    result.addLast(idx);
                    size--;
                }
                if (free.isEmpty()) it.remove();
                if (size == 0) break;
            }
            return result;
        });
        System.out.println("Part One: " + answer1);

        long answer2 = defrag((size, end) -> {
            Deque<Integer> result = new ArrayDeque<>(size);
            for (ListIterator<Deque<Integer>> it = frees.listIterator(); it.hasNext(); ) {
                Deque<Integer> free = it.next();
                if (free.getFirst() > end) break;
                if (free.size() >= size) {
                    while (size-- > 0) result.add(free.removeFirst());
                    if (free.isEmpty()) it.remove();
                    break;
                }
            }
            return result;
        });
        System.out.println("Part Two: " + answer2);
    }
}
