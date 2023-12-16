package _2022._06;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Advent of Code <a href="https://adventofcode.com/2022/day/6">Day 6: Tuning Trouble</a>
 */
public class TuningTrouble {

    static int detectStart(String data, int distinct) {
        int n = data.length(), r = 0;
        Map<Character, Integer> multiset = new HashMap<>();

        for (; r < distinct; ++r)
            multiset.compute(data.charAt(r), (k, v) -> v != null ? v + 1 : 1);

        for (int l = 0; r < n; ++r, ++l) {
            if (multiset.keySet().size() == distinct) break;

            multiset.compute(data.charAt(r), (k, v) -> v != null ? v + 1 : 1);
            multiset.compute(data.charAt(l), (k, v) -> v != null && v > 1 ? v - 1 : null);
        }
        return r;
    }

    static String preprocess(String file) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            return reader.readLine();
        }
    }

    public static void main(String[] args) throws IOException {
        String data = preprocess("src/main/resources/_2022/_06/input.txt");

        System.out.println("Part One: " + detectStart(data, 4));
        System.out.println("Part Two: " + detectStart(data, 14));
    }
}
