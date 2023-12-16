package _2022._13;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Advent of Code <a href="https://adventofcode.com/2022/day/13">Day 13: Distress Signal</a>
 */
public class DistressSignal {

    static int checkOrder(List<String> packets) {
        int n = packets.size(), sum = 0;
        for (int i = 0, pairing = 1; i < n; i += 3, ++pairing) {
            Object p1 = parsePacket(packets.get(i)), p2 = parsePacket(packets.get(i + 1));
            if (packetComparator.compare(p1, p2) < 1)
                sum += pairing;
        }
        return sum;
    }

    static int sort(List<String> packetsInput, String divider1Input, String divider2Input) {
        List<List<Object>> packets = packetsInput.stream()
                .filter(s -> s.length() != 0)
                .map(DistressSignal::parsePacket).collect(Collectors.toList());

        List<Object> divider1 = parsePacket(divider1Input), divider2 = parsePacket(divider2Input);

        packets.add(divider1);
        packets.add(divider2);

        packets.sort(packetComparator);

        int index1 = Collections.binarySearch(packets, divider1, packetComparator) + 1;
        int index2 = Collections.binarySearch(packets, divider2, packetComparator) + 1;

        return index1 * index2;
    }

    @SuppressWarnings("rawtypes")
    static final Comparator<Object> packetComparator = new Comparator<>() {
        @Override
        public int compare(Object oL, Object oR) {
            if (oL instanceof List lList && oR instanceof List rList) {
                Iterator lItr = lList.iterator(), rItr = rList.iterator();
                while (lItr.hasNext() && rItr.hasNext()) {
                    int comp = compare(lItr.next(), rItr.next());
                    if (comp != 0) return comp;
                }
                return (rItr.hasNext()) ? -1 : (lItr.hasNext()) ? 1 : 0;
            }
            if (oL instanceof List lList && oR instanceof Integer rInt)
                return compare(lList, List.of(rInt));
            if (oL instanceof Integer lInt && oR instanceof List rList)
                return compare(List.of(lInt), rList);
            if (oL instanceof Integer lInt && oR instanceof Integer rInt)
                return lInt.compareTo(rInt);

            return 0;
        }
    };

    static List<Object> parsePacket(String s) {
        List<Object> list = new ArrayList<>();

        if (s.charAt(0) != '[' || s.charAt(s.length() - 1) != ']')
            throw new IllegalStateException("Enclosing bracket character not found in String \"%s\"".formatted(s));

        String content = s.substring(1, s.length() - 1);
        for (int i = 0; i < content.length(); ++i) {
            char c = content.charAt(i);
            switch (c) {
                case '[' -> {
                    StringBuilder subList = new StringBuilder().append('[');
                    for (int open = 1; open > 0; ) {
                        subList.append((c = content.charAt(++i)));
                        if (c == ']') --open;
                        else if (c == '[') ++open;
                    }
                    list.add(parsePacket(subList.toString()));
                }
                case '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' -> {
                    int num = c - '0';
                    while (i + 1 < content.length() && Character.isDigit(content.charAt(i + 1)))
                        num *= 10 + content.charAt(++i) - '0';
                    list.add(num);
                }
                case ',' -> {
                }
                default ->
                        throw new IllegalStateException("Unexpected character \"%c\" found in String \"%s\"".formatted(c, content));
            }
        }
        return list;
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
        List<String> lines = parseInput("src/main/resources/_2022/_13/input.txt");

        System.out.printf("Part One: %d%n", checkOrder(lines));

        System.out.printf("Part Two: %d%n", sort(lines, "[[2]]", "[[6]]"));
    }

}
