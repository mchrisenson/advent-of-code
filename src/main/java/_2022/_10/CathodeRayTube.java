package _2022._10;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class CathodeRayTube {

    static void process(List<String> instructions, CycleFunction cf) {
        Iterator<String> itr = instructions.iterator();
        Integer addExe = null;

        for (int cycle = 1, x = 1, inc = 0; itr.hasNext() || addExe != null; ++cycle, x += inc, inc = 0) {
            if (addExe != null) {
                inc = addExe;
                addExe = null;
            } else {
                String[] i = itr.next().split(" ");
                switch (i[0]) {
                    case "addx" -> addExe = Integer.parseInt(i[1]);
                    case "noop" -> {
                    }
                }
            }
            cf.execute(cycle, x);
        }
    }

    interface CycleFunction {
        void execute(int cycle, int x);
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

    public static void main(String[] args) throws IOException {
        List<String> instructions = parseInput("src/main/resources/_2022/_10/input.txt");

        final int[] sumOfSignalStrength = new int[1];
        process(instructions, (cycle, x) -> {
            if ((cycle - 20) % 40 == 0)
                sumOfSignalStrength[0] += cycle * x;
        });
        System.out.println("Part One: " + sumOfSignalStrength[0]);

        final StringBuilder message = new StringBuilder();
        process(instructions, (cycle, x) -> {
            int cycleMod = cycle % 40;
            message.append(cycleMod >= x && cycleMod <= x + 2 ? '#' : ' ');
            if (cycleMod == 0) message.append('\n');
        });
        System.out.println("Part Two:\n" + message);
    }

}
