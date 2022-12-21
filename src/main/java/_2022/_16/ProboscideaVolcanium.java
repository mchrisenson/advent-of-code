package _2022._16;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Advent of Code <a href="https://adventofcode.com/2022/day/16">Day 16: Proboscidea Volcanium</a>
 */
public class ProboscideaVolcanium {
    static final String FILE = "src/main/resources/_2022/_16/input.txt";

    static int findMaxPressurePath(Map<String, Valve> graph, Valve src, Set<String> visited, int duration) {
        return backtrack(graph.keySet().size(), graph, visited, src, duration, src.flowRate * duration);
    }

    static int backtrack(int n, Map<String, Valve> graph, Set<String> visited, Valve u, int time, int pressure) {
        if (visited.size() == n) return pressure;

        int max = pressure;
        for (Map.Entry<String, Integer> e : u.adj.entrySet()) {
            Valve v = graph.get(e.getKey());
            if (visited.contains(v.id)) continue;
            int nextTime = time - e.getValue();
            if (nextTime < 1) continue;

            visited.add(v.id);
            max = Math.max(max, backtrack(n, graph, visited, v, nextTime, pressure + v.flowRate * nextTime));
            visited.remove(v.id);
        }
        return max;
    }

    static Map<String, Valve> getPipeNetworkGraph() {
        Map<String, Valve> graph;
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE))) {
            graph = reader.lines().map(line -> {
                Matcher matcher = valvePattern.matcher(line);
                if (!matcher.find()) throw new RuntimeException("Unable to parse sensor data \"%s\"".formatted(line));
                return matcher;
            }).collect(Collectors.toMap(
                    matcher -> matcher.group("id"),
                    matcher -> new Valve(matcher.group("id"),
                            Integer.parseInt(matcher.group("flowRate")),
                            Arrays.stream(matcher.group("adjacent").split(", "))
                                    .collect(Collectors.toMap(id -> id, __ -> 1))) // 1 minute to move to adjacent valve
            ));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        completeGraph(graph);
        removeZeroFlowRateValves(graph);
        // increment the distances in each adjacent mapping by 1 for the time to open a valve
        graph.values().stream().map(Valve::adj).forEach(adj ->
                adj.keySet().forEach(id -> adj.compute(id, (k, v) -> (v != null) ? v + 1 : null)));
        return graph;
    }

    static final Pattern valvePattern = Pattern.compile("Valve (?<id>\\w\\w) has flow rate=(?<flowRate>\\d+); tunnels? leads? to valves? (?<adjacent>.+)");

    static void completeGraph(Map<String, Valve> graph) {
        // iterate over vertices and add edges by BFS
        graph.values().forEach(valve -> {
            Queue<Map.Entry<String, Integer>> heap = new PriorityQueue<>(Comparator.comparingInt(Map.Entry::getValue));
            // initialize heap with children of children
            valve.adj.keySet().stream()
                    .map(id -> graph.get(graph.get(id).id).adj.entrySet()).flatMap(Collection::stream)
                    .filter(e -> !e.getKey().equals(valve.id))
                    .forEach(e -> heap.offer(new AbstractMap.SimpleEntry<>(e.getKey(), e.getValue() + 1)));

            while (!heap.isEmpty()) {
                Map.Entry<String, Integer> v = heap.poll();
                if (valve.adj.putIfAbsent(v.getKey(), v.getValue()) == null) {
                    graph.get(v.getKey()).adj.forEach((uId, uDist) -> {
                        if (!uId.equals(valve.id) && !valve.adj.containsKey(uId) && uDist == 1)
                            heap.offer(new AbstractMap.SimpleEntry<>(uId, v.getValue() + 1));
                    });
                }
            }
        });
    }

    static void removeZeroFlowRateValves(Map<String, Valve> graph) {
        graph.values().stream().filter(v -> v.flowRate == 0 && !v.id.equals("AA")).map(valve -> {
            graph.values().stream().map(v -> v.adj).forEach(adj -> adj.remove(valve.id));
            return valve.id;
        }).toList().forEach(graph::remove);
    }

    static List<List<Set<String>>> getComplementSetPairs(Set<String> idSet) {
        final List<String> idList = idSet.stream().filter(v -> !v.equals("AA")).toList();
        int n = idList.size(), k = n / 2;
        List<Set<String>> subsets = new ArrayList<>();

        getCombinations(n, k, idList, subsets, new ArrayDeque<>(), 0);
        // if set length is an even, then remove half of the generated combinations to avoid duplicate pairs
        return ((n % 2 == 0) ? subsets.subList(0, subsets.size() / 2) : subsets).stream().map(sub1 ->
                Stream.of(sub1, idList.stream().filter(id -> !sub1.contains(id)).collect(Collectors.toSet()))
                        .peek(sub -> sub.add("AA")).toList()).toList();
    }

    static void getCombinations(int n, int k, List<String> set, List<Set<String>> subsets, Deque<String> curr, int index) {
        if (curr.size() == k)
            subsets.add(new HashSet<>(curr));

        for (int i = index; i < n; ++i) {
            curr.addLast(set.get(i));
            getCombinations(n, k, set, subsets, curr, i + 1);
            curr.removeLast();
        }
    }

    record Valve(String id, int flowRate, Map<String, Integer> adj) {
    }

    public static void main(String[] args) {
        Map<String, Valve> pipeNetwork = getPipeNetworkGraph();
        Valve src = pipeNetwork.get("AA");

        System.out.printf("Part One: %d%n", findMaxPressurePath(pipeNetwork, src, new HashSet<>(), 30));

        System.out.printf("Part Two: %d%n", getComplementSetPairs(pipeNetwork.keySet()).stream().parallel().mapToInt(complements ->
                complements.stream().mapToInt(complement ->
                        findMaxPressurePath(pipeNetwork, src, complement, 26)).reduce(Integer::sum).orElseThrow()
        ).max().orElseThrow());
    }
}
