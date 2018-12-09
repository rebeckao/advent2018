package marblegame;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.IntStream;

class MarbleHighScore {
    private static final Pattern CONDITIONS = Pattern.compile("(\\d+) players; last marble is worth (\\d+) points");

    public static void main(String[] args) {
        try {
            String line = Files.readAllLines(Paths.get("./src/main/resources/day9_marble_game.txt")).get(0);
            System.out.println(new MarbleHighScore().highScore(line, 1));
            System.out.println(new MarbleHighScore().highScore(line, 100));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    long highScore(String conditions, int multiplier) {
        Matcher matcher = CONDITIONS.matcher(conditions);
        if (!matcher.matches()) {
            throw new IllegalStateException(String.format("%s does not match %s", conditions, CONDITIONS));
        }
        int numberOfPlayers = Integer.parseInt(matcher.group(1));
        int lastMarble = Integer.parseInt(matcher.group(2)) * multiplier;

        return highScore(numberOfPlayers, lastMarble);
    }

    private long highScore(int numberOfPlayers, int lastMarble) {
        Map<Integer, Long> scores = play(numberOfPlayers, lastMarble);

        return scores.values().stream()
                .mapToLong(Long::longValue)
                .max()
                .orElse(0L);
    }

    private Map<Integer, Long> play(int numberOfPlayers, int lastMarble) {
        Deque<Integer> marbleCircle = new ArrayDeque<>();
        marbleCircle.add(0);
        Map<Integer, Long> scores = new HashMap<>();

        for (int playedMarble = 1; playedMarble <= lastMarble; playedMarble ++) {
            if (playedMarble % 23 == 0) {
                int player = playedMarble % numberOfPlayers;
                scores.putIfAbsent(player, 0L);

                rotate(marbleCircle, 7);
                Integer removed = marbleCircle.removeFirst();

                scores.put(player, scores.get(player)
                        + playedMarble
                        + removed);
            } else {
                rotate(marbleCircle, -2);
                marbleCircle.addFirst(playedMarble);
            }
        }
        return scores;
    }

    private void rotate(Deque<Integer> circle, int rotations) {
        if (rotations > 0) {
            IntStream.range(0, rotations).forEach(i -> circle.addFirst(circle.removeLast()));
        } else if (rotations < 0) {
            IntStream.range(rotations, 0).forEach(i -> circle.add(circle.remove()));
        }
    }


}
