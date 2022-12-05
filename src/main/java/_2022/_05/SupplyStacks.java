package _2022._05;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Advent of Code <a href="https://adventofcode.com/2022/day/5">Day 5: Supply Stacks</a>
 */
public class SupplyStacks {
    static String rearrange(List<Deque<Character>> crates, List<Step> steps, StepFunction f) {
        for (Step step : steps)
            f.execute(step, crates.get(step.from), crates.get(step.to));

        return crates.stream().map(c -> !c.isEmpty() ? c.peek().toString() : "").collect(Collectors.joining());
    }

    static List<Deque<Character>> preprocessCrates(String file) throws IOException {
        List<Deque<Character>> crates = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            Deque<String> lineStack = new ArrayDeque<>();

            String line;
            while ((line = reader.readLine()) != null && line.length() != 0)
                lineStack.push(line);

            String[] nums = lineStack.pop().split("\\s+");
            int size = Integer.parseInt(nums[nums.length - 1]);

            for (int i = 0; i <= size; ++i)
                crates.add(new ArrayDeque<>());

            for (String row : lineStack) {
                for (int i = 1, offset = 1; i <= size; ++i, offset += 4) {
                    char c = row.charAt(offset);
                    if (c != ' ') crates.get(i).push(c);
                }
            }

        }
        return crates;
    }

    static List<Step> preprocessSteps(String file) throws IOException {
        List<Step> steps = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null && line.length() != 0) ;
            while ((line = reader.readLine()) != null) {
                String[] split = line.split("move\\s|\\sfrom\\s|\\sto\\s");
                steps.add(new Step(Integer.parseInt(split[1]), Integer.parseInt(split[2]), Integer.parseInt(split[3])));
            }
        }
        return steps;
    }

    record Step(int move, int from, int to) {
    }

    interface StepFunction {
        void execute(Step step, Deque<Character> from, Deque<Character> to);
    }

    public static void main(String[] args) throws IOException {
        String file = "src/main/resources/_2022/_05/input.txt";
        List<Deque<Character>> crates = preprocessCrates(file);
        List<Step> steps = preprocessSteps(file);

        System.out.println("Part One: " + rearrange(crates, steps, (step, from, to) -> {
            for (int i = 0; i < step.move; ++i)
                to.push(from.pop());
        }));

        crates = preprocessCrates(file);

        System.out.println("Part Two: " + rearrange(crates, steps, (step, from, to) -> {
            Deque<Character> between = new ArrayDeque<>();

            for (int i = 0; i < step.move; ++i)
                between.push(from.pop());

            for (Character crate : between)
                to.push(crate);
        }));
    }
}
