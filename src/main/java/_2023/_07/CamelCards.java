package _2023._07;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class CamelCards {

    static Gson gson = new GsonBuilder().serializeNulls().setPrettyPrinting().create();

    static class Hand implements Comparable<Hand> {

        final long bid;
        Type type;
        long highCard;

        enum Type {HIGH_CARD, ONE_PAIR, TWO_PAIR, THREE_OF_A_KIND, FULL_HOUSE, FOUR_OF_A_KIND, FiVE_OF_A_KIND;}

        static Map<Character, Integer> cardOrder1 = new HashMap<>(), cardOrder2 = new HashMap<>();

        static {
            String withOutJokers = "23456789TJQKA", withJokers = "J23456789TQKA";
            for (int i = 0; i < withOutJokers.length(); ++i) cardOrder1.put(withOutJokers.charAt(i), i + 1);
            for (int i = 0; i < withJokers.length(); ++i) cardOrder2.put(withJokers.charAt(i), i + 1);
        }

        Hand(String cards, long bid, boolean jokers) {
            this.bid = bid;

            List<Character> cardsList = new ArrayList<>();
            for (char card : cards.toCharArray()) cardsList.add(card);

            Map<Character, Integer> matches = cardsList.stream().collect(Collectors.toMap(Function.identity(), (c) -> 1, Integer::sum));

            int jokerCount = jokers && matches.containsKey('J') ? matches.remove('J') : 0;

            List<Map.Entry<Character, Integer>> top2 = matches.entrySet().stream()
                    .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder())).limit(2).collect(Collectors.toList());

            if (jokers && jokerCount > 0) {
                if (top2.isEmpty()) top2.add(Map.entry('A', 5));
                else top2.getFirst().setValue(top2.getFirst().getValue() + jokerCount);
            }

            type = switch (top2.getFirst().getValue()) {
                case 5 -> Type.FiVE_OF_A_KIND;
                case 4 -> Type.FOUR_OF_A_KIND;
                case 3 -> top2.get(1).getValue() == 2 ? Type.FULL_HOUSE : Type.THREE_OF_A_KIND;
                case 2 -> top2.get(1).getValue() == 2 ? Type.TWO_PAIR : Type.ONE_PAIR;
                default -> Type.HIGH_CARD;
            };

            Map<Character, Integer> cardOrder = jokers ? cardOrder2 : cardOrder1;
            highCard = cardsList.stream().mapToLong(cardOrder::get).reduce(0L, (a, i) -> (a << 4) + i);
        }

        @Override
        public int compareTo(@NonNull Hand that) {
            int cmp = this.type.compareTo(that.type);
            return cmp != 0 ? cmp : Long.compare(this.highCard, that.highCard);
        }
    }

    static List<Hand> parseInput(boolean jokers) throws IOException {
        try (Stream<String> lines = Files.lines(Path.of("src/main/resources/_2023/_07/input.txt"))) {
            return lines.map(line -> {
                String[] split = line.split("\\s+");
                return new Hand(split[0], Long.parseLong(split[1]), jokers);
            }).toList();
        }
    }

    public static void main(String[] args) throws IOException {
        List<Hand> part1Hands = parseInput(false).stream().sorted().toList();
        System.out.printf("Part One: %s\n", IntStream.range(0, part1Hands.size()).mapToLong(i -> (i + 1) * part1Hands.get(i).bid).sum());
        List<Hand> part2Hands = parseInput(true).stream().sorted().toList();
        System.out.printf("Part Two: %s, %s\n", gson.toJson(part2Hands), IntStream.range(0, part2Hands.size()).mapToLong(i -> (i + 1) * part2Hands.get(i).bid).sum());
    }
}
