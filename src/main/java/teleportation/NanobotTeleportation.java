package teleportation;

import lombok.Builder;
import lombok.Getter;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class NanobotTeleportation {
    private static final Pattern NANOBOT = Pattern.compile("pos=<(-?\\d+),(-?\\d+),(-?\\d+)>, r=(\\d+)");

    public static void main(String[] args) {
        String fileName = "./src/main/resources/day23_nanobots.txt";
        try (Stream<String> stream = Files.lines(Paths.get(fileName))) {
            System.out.println(new NanobotTeleportation().botsInRangeOfStrongestNanobot(stream));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    long botsInRangeOfStrongestNanobot(Stream<String> botLines) {
        List<Nanobot> nanobots = botLines.map(NANOBOT::matcher)
                .filter(Matcher::matches)
                .map(match -> Nanobot.builder()
                        .x(Integer.parseInt(match.group(1)))
                        .y(Integer.parseInt(match.group(2)))
                        .z(Integer.parseInt(match.group(3)))
                        .r(Integer.parseInt(match.group(4)))
                        .build())
                .collect(Collectors.toList());
        Nanobot strongest = nanobots.stream()
                .max(Comparator.comparing(Nanobot::getR))
                .orElseThrow(() -> new IllegalStateException("Unable to comply"));
        return nanobots.stream()
                .map(bot -> bot.distanceFrom(strongest))
                .filter(distance -> distance <= strongest.getR())
                .count();
    }

    @Builder
    @Getter
    private static class Nanobot {
        private int x;
        private int y;
        private int z;
        private int r;

        int distanceFrom(Nanobot other) {
            return Math.abs(x - other.getX()) + Math.abs(y - other.getY()) + Math.abs(z - other.getZ());
        }
    }
}
