package _2024._24;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.*;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * <a href="https://adventofcode.com">Advent of Code</a>
 * <a href="https://adventofcode.com/2024">2024</a>
 * <a href="https://adventofcode.com/2024/day/24">Day 24: Crossed Wires</a>
 */
public class CrossedWires {
    static final String FILE = "src/main/resources/_2024/_24/input.txt";
    static Map<String, Gate> graph = new HashMap<>();
    static List<String> xVars = new ArrayList<>(), yVars = new ArrayList<>(), zVars = new ArrayList<>();
    static Map<String, Boolean> val = new HashMap<>();

    static long add(Map<String, Gate> graph, List<String> wires, boolean fix) {
        Map<String, List<String>> adj = new HashMap<>();
        for (String w : graph.keySet()) {
            Gate gate = graph.get(w);
            adj.computeIfAbsent(gate.in1, k -> new ArrayList<>()).add(w);
            adj.computeIfAbsent(gate.in2, k -> new ArrayList<>()).add(w);
        }
        Map<String, Integer> inDegree = graph.keySet().stream().collect(Collectors.toMap(Function.identity(), w -> 2));
        Deque<String> curr = Stream.of(xVars, yVars).flatMap(Collection::stream).collect(Collectors.toCollection(ArrayDeque::new));
        List<String> sum = new ArrayList<>(xVars);
        List<List<String>> carry = yVars.stream().<List<String>>map(y -> new LinkedList<>(List.of(y))).toList();
        int i = 1;
        while (!curr.isEmpty()) {
            Deque<String> next = new ArrayDeque<>();
            int j = i / 2, k = i / 2;
            while (!curr.isEmpty()) {
                String u = curr.poll();
                for (String v : adj.getOrDefault(u, Collections.emptyList())) {
                    if (inDegree.merge(v, -1, Integer::sum) != 0) continue;
                    Gate gate = graph.get(v);
                    switch (gate.g) {
                        case XOR -> {
                            if (fix) {
                                if (j == i / 2 && v.charAt(0) != 'z') return swap(graph, v, zVars.get(j), wires);
                                if (!Set.of(gate.in1, gate.in2).contains(sum.get(j))) return swap(graph, sum.get(j), carry.get(j).getLast(), wires);
                            }
                            sum.set(j++, v);
                        }
                        case AND, OR -> carry.get(k++).add(v);
                    }
                    val.put(v, switch (gate.g) {
                        case AND -> val.get(gate.in1) & val.get(gate.in2);
                        case OR -> val.get(gate.in1) | val.get(gate.in2);
                        case XOR -> val.get(gate.in1) ^ val.get(gate.in2);
                    });
                    next.offer(v);
                }
            }
            curr = next.stream().sorted(Comparator.comparing(w -> graph.get(w).g)).collect(Collectors.toCollection(ArrayDeque::new));
            i++;
        }
        return IntStream.range(0, zVars.size()).mapToLong(z -> (val.get(zVars.get(z)) ? 1L : 0L) << z).sum();
    }

    static long swap(Map<String, Gate> graph, String o1, String o2, List<String> wires) {
        wires.add(o1);
        wires.add(o2);
        var t = graph.get(o1);
        graph.put(o1, graph.remove(o2));
        graph.put(o2, t);
        return add(graph, wires, true);
    }

    public static void main(String[] args) throws IOException, ExecutionException, InterruptedException {
        long ts = System.currentTimeMillis();
        String lines = Files.readString(Path.of(FILE));
        parse(lines, 'x', xVars);
        parse(lines, 'y', yVars);
        parse(lines);

        System.out.println("Part Two: " + add(graph, Collections.emptyList(), false));

        List<String> swapped = new ArrayList<>();
        add(graph, swapped, true);
        System.out.println("Part Two: " + swapped.stream().sorted().collect(Collectors.joining(",")));
        System.out.printf("Duration: %dms\n", (System.currentTimeMillis() - ts));
    }

    static void parse(String lines, char var, List<String> vars) {
        Matcher matcher = Pattern.compile("([%c]\\d{2}): ([1|0])".formatted(var)).matcher(lines);
        while (matcher.find()) {
            String k = matcher.group(1);
            boolean b = matcher.group(2).equals("1");
            vars.add(k);
            val.put(k, b);
        }
    }

    static void parse(String lines) {
        Matcher matcher = Pattern.compile("(\\w{3}) (\\w{2,3}) (\\w{3}) -> (\\w{3})").matcher(lines);
        while (matcher.find()) {
            String in1 = matcher.group(1), in2 = matcher.group(3), out = matcher.group(4);
            G g = G.valueOf(matcher.group(2));
            graph.put(out, new Gate(in1, in2, g));
            if (out.charAt(0) == 'z') zVars.add(out);
        }
        Collections.sort(zVars);
    }

    record Gate(String in1, String in2, G g) {
    }

    enum G {XOR, AND, OR}
}