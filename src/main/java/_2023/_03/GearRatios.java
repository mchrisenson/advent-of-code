package _2023._03;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Stream;

public class GearRatios {

    static final String FILE = "src/main/resources/_2023/_03/input.txt";

    static final int[][] DIRS = {{-1, -1}, {0, -1}, {1, -1}, {1, 0}, {1, 1}, {0, 1}, {-1, 1}, {-1, 0}};

    static List<List<Integer>> getPartNumbers(char[][] scheme, PartIdFunction partIdFunction) {
        List<List<Integer>> parts = new ArrayList<>();
        int rows = scheme.length, cols = scheme[0].length;

        for (int row = 0; row < rows; ++row) {
            for (int col = 0; col < cols; ++col) {
                if (!partIdFunction.match(scheme[row][col])) continue;
                List<Integer> numbers = new ArrayList<>();
                for (int[] dir : DIRS) {
                    int c = col + dir[0], r = row + dir[1];
                    if (r < 0 || r > rows - 1 || c < 0 || c > cols - 1) continue;

                    if (Character.isDigit(scheme[r][c])) {
                        StringBuilder digits = new StringBuilder().append(scheme[r][c]);
                        scheme[r][c] = '.';
                        for (int z = c - 1; z >= 0 && Character.isDigit(scheme[r][z]); --z) {
                            digits.insert(0, scheme[r][z]);
                            scheme[r][z] = '.';
                        }
                        for (int z = c + 1; z < cols && Character.isDigit(scheme[r][z]); ++z) {
                            digits.append(scheme[r][z]);
                            scheme[r][z] = '.';
                        }
                        numbers.add(Integer.parseInt(digits.toString()));
                    }
                }
                parts.add(numbers);
            }
        }
        return parts;
    }

    interface PartIdFunction {
        boolean match(char c);
    }

    static char[][] parseInput() throws IOException {
        try (Stream<String> linesStream = Files.lines(Path.of(FILE))) {
            List<String> lines = linesStream.toList();
            char[][] schematic = new char[lines.size()][];
            for (int i = 0; i < lines.size(); ++i) schematic[i] = lines.get(i).toCharArray();
            return schematic;
        }
    }

    public static void main(String[] args) throws Exception {
        char[][] schematic1 = parseInput();
        char[][] schematic2 = new char[schematic1.length][schematic1[0].length];

        for (int i = 0; i < schematic2.length; ++i) {
            System.arraycopy(schematic1[i], 0, schematic2[i], 0, schematic2[i].length);
        }
        System.out.printf("Part One: %s\n", getPartNumbers(schematic1, (char c) -> c != '.' && !(c >= '0' && c <= '9')).stream()
                .flatMap(Collection::stream).reduce(Integer::sum).orElseThrow());

        System.out.printf("Part Two: %s\n", getPartNumbers(schematic2, (char c) -> c == '*').stream()
                .filter(nums -> nums.size() == 2)
                .mapToInt(nums -> nums.getFirst() * nums.getLast()).reduce(Integer::sum).orElseThrow());
    }
}
