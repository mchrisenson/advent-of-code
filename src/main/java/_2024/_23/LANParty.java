package _2024._23;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * <a href="https://adventofcode.com">Advent of Code</a>
 * <a href="https://adventofcode.com/2024">2024</a>
 * <a href="https://adventofcode.com/2024/day/23">Day 23: LAN Party</a>
 */
public class LANParty {
    static final String FILE = "src/main/resources/_2024/_23/input.txt";

    static Map<String, List<String>> adjList = new HashMap<>();

    static long part1() {
        Deque<QE> queue = new ArrayDeque<>();
        Set<Set<String>> sets = new HashSet<>();

        for (String v : adjList.keySet()) queue.offer(new QE(Set.of(v), new HashSet<>(adjList.get(v))));

        while (!queue.isEmpty()) {
            QE u = queue.poll();
            for (String w : u.adj) {
                Set<String> vSet = new HashSet<>(u.set);
                vSet.add(w);
                if (!sets.add(vSet) || vSet.size() == 3) continue;
                QE v = new QE(vSet, new HashSet<>(u.adj));
                v.adj.retainAll(adjList.get(w));
                queue.offer(v);
            }
        }
        return sets.stream().filter(s -> s.size() == 3).filter(l -> l.stream().anyMatch(c -> c.charAt(0) == 't')).count();
    }

    static String part2() {
        Queue<QE> queue = new PriorityQueue<>(Comparator.<QE>comparingInt(e -> e.set.size() + e.adj.size()).reversed());
        Set<Set<String>> visited = new HashSet<>();

        for (String v : adjList.keySet()) queue.offer(new QE(Set.of(v), new HashSet<>(adjList.get(v))));

        while (!queue.isEmpty()) {
            QE u = queue.poll();
            if (u.adj.isEmpty()) return u.set.stream().sorted().collect(Collectors.joining(","));
            for (String w : u.adj) {
                Set<String> vSet = new HashSet<>(u.set);
                vSet.add(w);
                if (!visited.add(vSet)) continue;
                QE v = new QE(vSet, new HashSet<>(u.adj));
                v.adj.retainAll(adjList.get(w));
                queue.offer(v);
            }
        }
        return "";
    }

    public static void main(String[] args) throws IOException {
        long ts = System.currentTimeMillis();
        Matcher matcher = Pattern.compile("(\\w\\w)-(\\w\\w)").matcher(Files.readString(Path.of(FILE)));
        while (matcher.find()) {
            String u = matcher.group(1), v = matcher.group(2);
            adjList.computeIfAbsent(u, k -> new ArrayList<>()).add(v);
            adjList.computeIfAbsent(v, k -> new ArrayList<>()).add(u);
        }
        System.out.println("Part One: " + part1());
        System.out.println("Part Two: " + part2());
        System.out.printf("Duration: %dms\n", (System.currentTimeMillis() - ts));
    }

    record QE(Set<String> set, Set<String> adj) {}
}