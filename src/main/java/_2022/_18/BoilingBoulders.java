package _2022._18;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Advent of Code <a href="https://adventofcode.com/2022/day/18">Day 18: Boiling Boulders</a>
 */
public class BoilingBoulders {
    static final String FILE = "src/main/resources/_2022/_18/input.txt";

    static final Cube[] DIRS = {
            new Cube(1, 0, 0), new Cube(-1, 0, 0),
            new Cube(0, 1, 0), new Cube(0, -1, 0),
            new Cube(0, 0, 1), new Cube(0, 0, -1)
    };

    static int getSurfaceArea(Set<Cube> droplet) {
        return droplet.stream().flatMapToInt(cube ->
                Stream.of(DIRS).mapToInt(d -> !droplet.contains(cube.adj(d)) ? 1 : 0)).sum();
    }

    static int getOuterSurfaceArea(Set<Cube> droplet) {
        Cube min = new Cube(
                droplet.stream().mapToInt(c -> c.x).min().orElseThrow() - 1,
                droplet.stream().mapToInt(c -> c.y).min().orElseThrow() - 1,
                droplet.stream().mapToInt(c -> c.z).min().orElseThrow() - 1);
        Cube max = new Cube(
                droplet.stream().mapToInt(c -> c.x).max().orElseThrow() + 1,
                droplet.stream().mapToInt(c -> c.y).max().orElseThrow() + 1,
                droplet.stream().mapToInt(c -> c.z).max().orElseThrow() + 1);

        Queue<Cube> queue = new ArrayDeque<>();
        Set<Cube> visited = new HashSet<>();

        queue.offer(min);
        visited.add(min);

        int area = 0;
        while (!queue.isEmpty()) {
            Cube u = queue.poll();

            for (Cube d : DIRS) {
                Cube v = u.adj(d);
                if (v.x < min.x || v.x > max.x || v.y < min.y || v.y > max.y || v.z < min.z || v.z > max.z) continue;
                if (visited.contains(v)) continue;
                if (droplet.contains(v)) ++area;
                else {
                    queue.offer(v);
                    visited.add(v);
                }
            }
        }
        return area;
    }

    record Cube(int x, int y, int z) {
        Cube adj(Cube c) {
            return new Cube(this.x + c.x, this.y + c.y, this.z + c.z);
        }
    }

    static final Pattern cubePattern = Pattern.compile(",");

    static Set<Cube> parseCubes() {
        try (Stream<String> lines = Files.newBufferedReader(Paths.get(FILE)).lines()) {
            return lines.map(line -> {
                String[] c = cubePattern.split(line);
                return new Cube(Integer.parseInt(c[0]), Integer.parseInt(c[1]), Integer.parseInt(c[2]));
            }).collect(Collectors.toSet());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        Set<Cube> cubes = parseCubes();
        System.out.printf("Part One: %d%n", getSurfaceArea(cubes));
        System.out.printf("Part Two: %d%n", getOuterSurfaceArea(cubes));
    }
}
