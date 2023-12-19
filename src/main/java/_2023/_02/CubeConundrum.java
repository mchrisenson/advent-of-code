package _2023._02;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class CubeConundrum {

    static final String FILE = "src/main/resources/_2023/_02/input.txt";

    record Game(int id, List<Subset> subsets) {
        public Game(int id) {
            this(id, new ArrayList<>());
        }
    }

    record Subset(int red, int green, int blue) {
    }

    static List<Game> parseInput() throws IOException {
        try (Stream<String> lines = Files.lines(Path.of(FILE))) {
            return lines.map(line -> {
                String[] gameSplit = line.split(":");
                Game game = new Game(Integer.parseInt(gameSplit[0].split(" ")[1]));
                for (String subsetSplit : gameSplit[1].split(";")) {
                    int red = 0, green = 0, blue = 0;
                    for (String cube : subsetSplit.split(",")) {
                        String[] cubeSplit = cube.trim().split(" ");
                        switch (cubeSplit[1]) {
                            case "red" -> red = Integer.parseInt(cubeSplit[0]);
                            case "green" -> green = Integer.parseInt(cubeSplit[0]);
                            case "blue" -> blue = Integer.parseInt(cubeSplit[0]);
                        }
                    }
                    game.subsets.add(new Subset(red, green, blue));
                }
                return game;
            }).toList();
        }
    }

    public static void main(String[] args) throws IOException {
        List<Game> games = parseInput();

        Subset limits = new Subset(12, 13, 14);

        System.out.printf("Part One: %s\n", games.stream()
                .filter(g -> g.subsets.stream().mapToInt(Subset::red).max().orElseThrow() <= limits.red)
                .filter(g -> g.subsets.stream().mapToInt(Subset::green).max().orElseThrow() <= limits.green)
                .filter(g -> g.subsets.stream().mapToInt(Subset::blue).max().orElseThrow() <= limits.blue)
                .mapToInt(Game::id).sum());

        System.out.printf("Part Two: %s\n", games.stream()
                .mapToInt(g -> g.subsets.stream().mapToInt(Subset::red).max().orElseThrow()
                        * g.subsets.stream().mapToInt(Subset::green).max().orElseThrow()
                        * g.subsets.stream().mapToInt(Subset::blue).max().orElseThrow()).sum());
    }
}
