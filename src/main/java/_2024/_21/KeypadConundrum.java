package _2024._21;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

/**
 * <a href="https://adventofcode.com">Advent of Code</a>
 * <a href="https://adventofcode.com/2024">2024</a>
 * <a href="https://adventofcode.com/2024/day/21">Day 21: Keypad Conundrum</a>
 */
public class KeypadConundrum {
    static final String FILE = "src/main/resources/_2024/_21/input.txt";

    static final Map<Character, B> numPad = new HashMap<>(), dirPad = new HashMap<>();
    static final Map<String, List<String>> numAdj = new HashMap<>(), dirAdj = new HashMap<>();
    static final Map<K, Long> cache = new HashMap<>();

    static {
        String[] n = {"789", "456", "123", " 0A"};
        for (int y = 0; y < n.length; y++) {
            for (int x = 0; x < n[0].length(); x++) {
                numPad.put(n[y].charAt(x), new B(x, y));
            }
        }
        String[] d = {" ^A", "<v>"};
        for (int y = 0; y < d.length; y++) {
            for (int x = 0; x < d[0].length(); x++) {
                dirPad.put(d[y].charAt(x), new B(x, y));
            }
        }
        buildAdjList(numPad, numAdj);
        buildAdjList(dirPad, dirAdj);
    }

    static long push(List<String> codes, int depth) {
        long result = 0L;
        for (String code : codes) {
            result += push(numAdj, code, depth) * Long.parseLong(code.substring(0, code.length() - 1));
        }
        return result;
    }

    static long push(Map<String, List<String>> adj, String code, int depth) {
        if (depth == 0) return code.length();
        long result = 0L;
        char p = 'A';
        for (char c : code.toCharArray()) {
            long moves = Long.MAX_VALUE;
            K k = new K("" + p + c, depth);
            if (cache.containsKey(k)) moves = cache.get(k);
            else {
                for (String m : adj.get(k.e)) {
                    moves = Math.min(push(dirAdj, m, depth - 1), moves);
                }
                cache.put(k, moves);
            }
            result += moves;
            p = c;
        }
        return result;
    }

    static void buildAdjList(Map<Character, B> pad, Map<String, List<String>> adj) {
        for (char p : pad.keySet()) {
            for (char c : pad.keySet()) {
                B pb = pad.get(p), cb = pad.get(c), diff = pad.get(p).diff(pad.get(c));
                String y = diff.y < 0 ? "v".repeat(-diff.y) : "^".repeat(diff.y);
                String x = diff.x < 0 ? ">".repeat(-diff.x) : "<".repeat(diff.x);
                List<String> m = new ArrayList<>();
                if (!new B(cb.x, pb.y).equals(pad.get(' '))) m.add(x + y + 'A');
                if (!x.isEmpty() && !y.isEmpty() && !new B(pb.x, cb.y).equals(pad.get(' '))) m.add(y + x + 'A');
                adj.put("" + p + c, m);
            }
        }
    }

    public static void main(String[] args) throws IOException {
        List<String> codes = Files.readAllLines(Path.of(FILE));

        System.out.println("Part One: " + push(codes, 3));
        System.out.println("Part Two: " + push(codes, 26));
    }

    record B(int x, int y) {
        B diff(B p) { return new B(this.x - p.x, this.y - p.y); }
    }

    record K(String e, int d) {}
}