package _2022._07;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

/**
 * Advent of code <a href="https://adventofcode.com/2022/day/7">Day 7: No Space Left On Device</a>
 */
public class NoSpaceLeftOnDevice {

    static void traverse(File file, SpaceFunction sf) {
        if (file.children.size() == 0) return;
        sf.offer(file);
        file.children.forEach(f -> traverse(f, sf));
    }

    static File buildTree(List<String> output) {
        File root = new File(null, "", 0), pwd = root;

        for (String line : output) {
            String[] args = line.split(" ");
            switch (args[0]) {
                case "$" -> {
                    switch (args[1]) {
                        case "cd" -> pwd = switch (args[2]) {
                            case "..":
                                yield pwd.parent;// != null ? pwd.parent : root;
                            case "/":
                                yield root;
                            default:
                                yield pwd.getFile(args[2]);
                        };
                        case "ls" -> {
                        }
                    }
                }
                case "dir" -> pwd.addFile(args[1], 0);
                default -> pwd.addFile(args[1], Integer.parseInt(args[0]));
            }
        }
        return root;
    }

    static class File {
        final File parent;
        final String name;
        Integer size;
        final Set<File> children = new HashSet<>();

        File(File parent, String name, Integer size) {
            this.parent = parent;
            this.name = name;
            this.size = size;
        }

        File getFile(String name) {
            return this.children.stream().filter(f -> f.name.equals(name)).findAny().orElse(null);
        }

        void addFile(String name, Integer size) {
            if (getFile(name) != null) return;

            File file = new File(this, name, size);
            children.add(file);

            for (File p = this; p != null; p = p.parent)
                p.size += size;
        }
    }

    static List<String> parseInput(String file) throws IOException {
        List<String> lines = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null)
                lines.add(line);
        }
        return lines;
    }

    interface SpaceFunction {
        void offer(File file);
    }

    public static void main(String[] args) throws IOException {
        List<String> output = parseInput("src/main/resources/_2022/_07/input.txt");

        File root = buildTree(output);

        int[] lt100k = new int[1];
        traverse(root, file -> {
            if (file.size <= 100_000) lt100k[0] += file.size;
        });
        System.out.println("Part One: " + lt100k[0]);

        int neededSpace = 30_000_000 - (70_000_000 - root.size);
        Queue<Integer> heap = new PriorityQueue<>();
        traverse(root, file -> {
            if (file.size >= neededSpace) heap.add(file.size);
        });

        System.out.println("Part Two: " + heap.peek());
    }
}
