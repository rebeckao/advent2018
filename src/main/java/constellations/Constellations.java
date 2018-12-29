package constellations;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

class Constellations {

    public static void main(String[] args) {
        String fileName = "./src/main/resources/day25_constellations.txt";
        try (Stream<String> stream = Files.lines(Paths.get(fileName))) {
            System.out.println(new Constellations().numberOfConstellations(stream));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    int numberOfConstellations(Stream<String> pointsStream) {
        List<Point> pointList = pointsStream.map(point -> point.split(","))
                .map(array -> Stream.of(array).map(Integer::parseInt).collect(toList()))
                .map(array -> Point.builder()
                        .x(array.get(0))
                        .y(array.get(1))
                        .z(array.get(2))
                        .t(array.get(3))
                        .connected(new HashSet<>())
                        .build())
                .distinct()
                .collect(Collectors.toList());

        for (Point point : pointList) {
            for (Point otherPoint : pointList) {
                if (point.equals(otherPoint)) {
                    continue;
                }
                if (point.distance(otherPoint) <= 3) {
                    point.connect(otherPoint);
                }
            }
        }

        Set<Point> visited = new HashSet<>();
        int constellations = 0;
        while (!pointList.isEmpty()) {
            constellations++;
            Queue<Point> currentConstellation = new LinkedList<>();
            Point next = pointList.remove(0);
            visited.add(next);
            currentConstellation.add(next);
            while (!currentConstellation.isEmpty()) {
                Point current = currentConstellation.remove();
                current.getConnected().stream()
                        .filter(point -> !visited.contains(point))
                        .forEach(connected -> {
                            currentConstellation.add(connected);
                            visited.add(connected);
                            pointList.remove(connected);
                        });
            }

        }

        return constellations;
    }

    @Builder
    @EqualsAndHashCode(exclude = "connected")
    private static class Point {
        private int x, y, z, t;
        @Getter
        private Set<Point> connected = new HashSet<>();

        int distance(Point other) {
            return Math.abs(x - other.x) + Math.abs(y - other.y) + Math.abs(z - other.z) + Math.abs(t - other.t);
        }

        void connect(Point other) {
            connected.add(other);
            other.connected.add(this);
        }
    }
}
