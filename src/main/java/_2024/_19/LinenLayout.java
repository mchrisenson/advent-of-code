package _2024._19;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

/**
 * <a href="https://adventofcode.com">Advent of Code</a>
 * <a href="https://adventofcode.com/2024">2024</a>
 * <a href="https://adventofcode.com/2024/day/19">Day 19: Linen Layout</a>
 */
public class LinenLayout {
    static final String FILE = "src/main/resources/_2024/_19/input.txt";
    static Trie root = new Trie();
    static Map<String, Long> cache = new HashMap<>();

    public static void main(String[] args) throws IOException {
        List<String> lines = Files.readAllLines(Path.of(FILE));
        Arrays.stream(lines.getFirst().split(", ")).forEach(root::insert);
        List<String> designs = lines.subList(2, lines.size());

        designs = designs.stream().filter(root::possible).toList();
        System.out.println("Part One: " + designs.size());

        System.out.println("Part Two: " + designs.stream().mapToLong(root::ways).sum());
    }

    static class Trie {
        Trie[] children = new Trie[26];
        boolean towel;

        boolean possible(String p) {
            if (p.isEmpty()) return towel;
            if (towel && root.possible(p)) return true;
            char c = p.charAt(0);
            return contains(c) && get(c).possible(p.substring(1));
        }

        long ways(String d) {
            if (cache.containsKey(d)) return cache.get(d);
            char c = d.charAt(0);
            long w = contains(c) ? get(c)._ways(d.substring(1)) : 0;
            cache.put(d, w);
            return w;
        }

        long _ways(String d) {
            if (d.isEmpty()) return towel ? 1 : 0;
            char c = d.charAt(0);
            long w = towel ? root.ways(d) : 0;
            if (contains(c)) w += get(c)._ways(d.substring(1));
            return w;
        }

        void insert(String towel) {
            Trie node = this;
            for (char c : towel.toCharArray()) {
                if (!node.contains(c)) node.put(c, new Trie());
                node = node.get(c);
            }
            node.towel = true;
        }

        Trie get(char c) { return children[c - 'a']; }

        void put(char c, Trie node) { children[c - 'a'] = node; }

        boolean contains(char c) { return get(c) != null; }
    }
}
