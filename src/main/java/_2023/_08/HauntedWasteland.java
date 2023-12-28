package _2023._08;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class HauntedWasteland {

    static long findSteps(Map<String, List<String>> network, String inst, String start, Search search) {
        String node = start;
        int steps = 0;
        while (!search.isEnd(node)) {
            char dir = inst.charAt(steps++ % inst.length());
            node = network.get(node).get(dir == 'L' ? 0 : 1);
        }
        return steps;
    }

    interface Search {
        boolean isEnd(String node);
    }

    public static void main(String[] args) throws IOException {
        String[] text = Files.readString(Path.of("src/main/resources/_2023/_08/input.txt")).split("\n\n");

        String inst = text[0];

        Map<String, List<String>> network = new HashMap<>();

        for (String line : text[1].split("\n")) {
            String p = line.split(" ")[0];
            String l = line.split("\\(")[1].split(",")[0];
            String r = line.split(",\\s")[1].split("\\)")[0];
            network.put(p, List.of(l, r));
        }

        System.out.printf("Part One: %d\n", findSteps(network, inst, "AAA", (node) -> node.charAt(2) == 'Z'));

        System.out.printf("Part Two: %s\n", network.keySet().stream()
                .filter(k -> k.charAt(2) == 'A')
                .mapToLong(start -> findSteps(network, inst, start, (node) -> node.charAt(2) == 'Z'))
                .reduce(HauntedWasteland::lcm).orElseThrow());

    }

    static long gcd(long n1, long n2) {
        if (n1 == 0 || n2 == 0) return n1 + n2;
        long bigger = Math.max(n1, n2), smaller = Math.min(n1, n2);
        return gcd(bigger % smaller, smaller);
    }

    static long lcm(long n1, long n2) {
        if (n1 == 0 || n2 == 0) return 0;
        return Math.abs(n1 * n2) / gcd(n1, n2);
    }
}
