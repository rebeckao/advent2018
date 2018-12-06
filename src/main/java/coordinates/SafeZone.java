package coordinates;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

class SafeZone {

    public static void main(String[] args) {
        SafeZone dangerZones = new SafeZone();
        String fileName = "./src/main/resources/day6_coordinates.txt";

        try (Stream<String> stream = Files.lines(Paths.get(fileName))) {
            int mostSleepyMinute = dangerZones.sizeOfSafeArea(stream, 10000);
            System.out.println(mostSleepyMinute);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    int sizeOfSafeArea(Stream<String> coordinateStrings, int maxDistance) {
        List<Coordinate> originalCoordinates = coordinateStrings
                .map(Coordinate::parse)
                .collect(toList());
        int minX = originalCoordinates.stream().mapToInt(Coordinate::getX).min().orElseThrow(() -> new IllegalStateException("No coordinate"));
        int minY = originalCoordinates.stream().mapToInt(Coordinate::getY).min().orElseThrow(() -> new IllegalStateException("No coordinate"));
        List<Coordinate> normalizedCoordinates = originalCoordinates.stream()
                .map(coord -> new Coordinate(coord.getX() - minX, coord.getY() - minY))
                .collect(Collectors.toList());

        int maxX = originalCoordinates.stream().mapToInt(Coordinate::getX).max().orElseThrow(() -> new IllegalStateException("No coordinate"));
        int maxY = originalCoordinates.stream().mapToInt(Coordinate::getY).max().orElseThrow(() -> new IllegalStateException("No coordinate"));
        int newMaxX = maxX - minX + 1;
        int newMaxY = maxY - minY + 1;

        List<Integer> xDistances = IntStream.range(-maxDistance, newMaxX + maxDistance)
                .map(x -> normalizedCoordinates.stream()
                        .mapToInt(Coordinate::getX)
                        .map(xCoord -> x - xCoord)
                        .map(Math::abs)
                        .sum())
                .boxed()
                .collect(toList());

        return (int) IntStream.range(-maxDistance, newMaxY + maxDistance)
                .map(y -> normalizedCoordinates.stream()
                        .mapToInt(Coordinate::getY)
                        .map(yCoord -> y - yCoord)
                        .map(Math::abs)
                        .sum())
                .flatMap(yDist -> xDistances.stream().mapToInt(xDist -> xDist + yDist))
                .filter(dist -> dist < maxDistance)
                .count();
    }

}
