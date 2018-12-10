package starsigns;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class LightMessage {
    private static final Pattern STAR = Pattern.compile("position=< *(-?\\d*), *(-?\\d*)> velocity=< *(-?\\d*), *(-?\\d*)>");

    public static void main(String[] args) {
        LightMessage lightMessage = new LightMessage();
        String fileName = "./src/main/resources/day10_stars.txt";

        try {
            List<String> lines = Files.readAllLines(Paths.get(fileName));
            int second = lightMessage.resolveMostPromisingSecond(lines, 100_000);
            System.out.println(second);
            System.out.println(lightMessage.representationAtSecond(lines, second));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    int resolveMostPromisingSecond(List<String> starData, int secondsToTest) {
        Set<Star> stars = resolveStars(starData.stream());
        return resolveMostPromisingSecond(stars, secondsToTest);
    }

    String representationAtSecond(List<String> starData, int second) {
        Set<Star> newStars = resolveStars(starData.stream());
        newStars.forEach(star -> star.fastForward(second));
        return buildSnapshotRepresentation(newStars);
    }

    private String buildSnapshotRepresentation(Set<Star> stars) {
        int minX = stars.stream().map(Star::getXPos).mapToInt(Integer::intValue).min().orElse(0);
        int minY = stars.stream().map(Star::getYPos).mapToInt(Integer::intValue).min().orElse(0);
        int maxX = stars.stream().map(Star::getXPos).mapToInt(Integer::intValue).max().orElse(0);
        int maxY = stars.stream().map(Star::getYPos).mapToInt(Integer::intValue).max().orElse(0);

        Set<String> positions = stars.stream()
                .map(star -> star.getYPos() + " " + star.getXPos())
                .collect(Collectors.toSet());

        StringBuilder snapshotRepresentation = new StringBuilder();
        for (int row = minY; row <= maxY; row++) {
            for (int col = minX; col <= maxX; col++) {
                boolean isThereAStarHere = positions.contains(row + " " + col);
                snapshotRepresentation.append(isThereAStarHere ? "#" : ".");
            }
            snapshotRepresentation.append("\n");
        }
        return snapshotRepresentation.toString();
    }

    private int resolveMostPromisingSecond(Set<Star> stars, int maxSecondsToTest) {
        int mostPromisingSecond = 0;
        long leastSnapshotSize = 0;
        for (int second = 1; second <= maxSecondsToTest; second++) {
            stars.forEach(Star::update);

            long minX = stars.stream().map(Star::getXPos).mapToInt(Integer::intValue).min().orElse(0);
            long minY = stars.stream().map(Star::getYPos).mapToInt(Integer::intValue).min().orElse(0);
            long maxX = stars.stream().map(Star::getXPos).mapToInt(Integer::intValue).max().orElse(0);
            long maxY = stars.stream().map(Star::getYPos).mapToInt(Integer::intValue).max().orElse(0);

            long newSnapshotSize = (maxX - minX) * (maxY - minY);
            if (leastSnapshotSize == 0 || newSnapshotSize < leastSnapshotSize) {
                leastSnapshotSize = newSnapshotSize;
                mostPromisingSecond = second;
            }
        }
        return mostPromisingSecond;
    }

    private Set<Star> resolveStars(Stream<String> starData) {
        return starData
                .map(STAR::matcher)
                .filter(Matcher::matches)
                .map(matcher -> new Star(
                        Integer.parseInt(matcher.group(1)),
                        Integer.parseInt(matcher.group(2)),
                        Integer.parseInt(matcher.group(3)),
                        Integer.parseInt(matcher.group(4))
                ))
                .collect(Collectors.toSet());
    }
}
