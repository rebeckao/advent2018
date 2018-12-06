package coordinates;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.util.Arrays.stream;
import static java.util.stream.Collectors.*;

class DangerZones {

    private static final String NO_COORDINATE = ".";

    public static void main(String[] args) {
        DangerZones dangerZones = new DangerZones();
        String fileName = "./src/main/resources/day6_coordinates.txt";

        try (Stream<String> stream = Files.lines(Paths.get(fileName))) {
            int mostSleepyMinute = dangerZones.sizeOfLargestArea(stream);
            System.out.println(mostSleepyMinute);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    int sizeOfLargestArea(Stream<String> coordinateStrings) {
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

        String[][] grid = constructGrid(normalizedCoordinates, newMaxX, newMaxY);

        coordinatesWithInfiniteAreas(newMaxX, newMaxY, grid);

        return IntStream.range(1, newMaxX - 1)
                .mapToObj(x ->
                        stream(grid[x], 1, newMaxY - 1)
                )
                .flatMap(Function.identity())
                .filter(nearest -> !nearest.equals(NO_COORDINATE))
                .collect(toMap(Function.identity(), a -> 1, (a, b) -> a + 1))
                .values()
                .stream()
                .mapToInt(Integer::intValue)
                .max()
                .orElseThrow(() -> new IllegalStateException("No area"));
    }

    private String[][] constructGrid(List<Coordinate> normalizedCoordinates, int newMaxX, int newMaxY) {
        String[][] grid = new String[newMaxX][newMaxY];
        IntStream.range(0, newMaxX)
                .forEach(x -> IntStream.range(0, newMaxY)
                        .forEach(y ->
                                grid[x][y] = nearestCoordinate(normalizedCoordinates, x, y)
                        )
                );
        return grid;
    }

    private void coordinatesWithInfiniteAreas(int maxX, int maxY, String[][] grid) {
        Set<String> infinites = IntStream.range(0, maxX)
                .mapToObj(x -> Stream.of(grid[x][0], grid[x][maxY - 1]))
                .flatMap(Function.identity())
                .collect(toSet());

        IntStream.range(1, maxY - 2)
                .mapToObj(y -> Stream.of(grid[0][y], grid[maxX - 1][y]))
                .flatMap(Function.identity())
                .distinct()
                .forEach(infinites::add);
    }

    private String nearestCoordinate(List<Coordinate> coordinates, int x, int y) {
        String nearest = NO_COORDINATE;
        Integer distance = null;
        for (int i = 0; i < coordinates.size(); i++) {
            Coordinate coordinate = coordinates.get(i);
            int currentDistance = Math.abs(coordinate.getX() - x) + Math.abs(coordinate.getY() - y);
            if (currentDistance == 0) {
                return String.valueOf(i);
            } else if (distance == null || distance > currentDistance) {
                distance = currentDistance;
                nearest = String.valueOf(i);
            } else if (distance.equals(currentDistance)) {
                nearest = NO_COORDINATE;
            }
        }
        return nearest;
    }
}
