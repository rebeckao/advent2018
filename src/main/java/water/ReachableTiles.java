package water;

import common.Util;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static common.Util.display;

class ReachableTiles {
    private static final Pattern VERTICAL_RESERVOIR = Pattern.compile("x=(\\d+), y=(\\d+)\\.\\.(\\d+)");
    private static final Pattern HORIZONTAL_RESERVOIR = Pattern.compile("y=(\\d+), x=(\\d+)\\.\\.(\\d+)");
    private static final char CLAY = '%';
    private static final char FLOWING = '|';
    private static final char SETTLED = '~';

    public static void main(String[] args) {
        String fileName = "./src/main/resources/day17_clay.txt";
        try (Stream<String> stream = Files.lines(Paths.get(fileName))) {
            ReachableTiles reachableTiles = new ReachableTiles();
            char[][] filled = reachableTiles.fillWithWater(stream);
            System.out.println("Reached: " + reachableTiles.tilesReachedByWater(filled));
            System.out.println("Filled: " + reachableTiles.tilesFilledByWater(filled));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    char[][] fillWithWater(Stream<String> coordinates) {
        Set<Coordinate> clayCoordinates = coordinates
                .flatMap(this::parseCoordinates)
                .collect(Collectors.toSet());
        int minX = clayCoordinates.stream().mapToInt(Coordinate::getX).min().orElseThrow(() -> new IllegalStateException("Does not compute")) - 1;
        int minY = clayCoordinates.stream().mapToInt(Coordinate::getY).min().orElseThrow(() -> new IllegalStateException("Does not compute"));
        int maxX = clayCoordinates.stream().mapToInt(Coordinate::getX).max().orElseThrow(() -> new IllegalStateException("Does not compute")) + 1;
        int maxY = clayCoordinates.stream().mapToInt(Coordinate::getY).max().orElseThrow(() -> new IllegalStateException("Does not compute"));
        char[][] map = toNormalizedMatrix(clayCoordinates, minX, minY, maxX, maxY);
        int xCoordinateOfSpring = 500 - minX;

        map[0][xCoordinateOfSpring] = FLOWING;
        return fillWithWater(map);
    }

    char[][] fillWithWater(char[][] map) {
        int turns = 0;
        while (true) {
//            System.out.println("After " + turns + " turns:");
//            display(map);
            turns++;
            char[][] newMap = Util.clone(map);
            flow(newMap);
            if (Arrays.deepEquals(map, newMap)) {
                System.out.println(turns);
                display(map);
                return map;
            }
            map = newMap;
        }
    }

    int tilesReachedByWater(char[][] map) {
        int tilesReached = 0;
        for (char[] row : map) {
            for (char square : row) {
                if (square == FLOWING || square == SETTLED) {
                    tilesReached++;
                }
            }
        }
        return tilesReached;
    }

    int tilesFilledByWater(char[][] map) {
        int tilesReached = 0;
        for (char[] row : map) {
            for (char square : row) {
                if (square == SETTLED) {
                    tilesReached++;
                }
            }
        }
        return tilesReached;
    }

    private void flow(char[][] map) {
        for (int y = 0; y < map.length - 1; y++) {
            char[] row = map[y];
            for (int x = 0; x < row.length; x++) {
                char square = row[x];
                if (square == FLOWING) {
                    char below = map[y + 1][x];
                    if (below == FLOWING) {
                        continue;
                    }
                    if (below == 0 || below == '.') {
                        map[y + 1][x] = FLOWING;
                        continue;
                    }
                    int wallToTheLeft = flowLeft(map, x, y);
                    int wallToTheRight = flowRight(map, x, y);
                    if (wallToTheLeft != -1 && wallToTheRight != -1) {
                        for (int fillX = wallToTheLeft + 1; fillX < wallToTheRight; fillX++) {
                            map[y][fillX] = SETTLED;
                        }
                    }
                }
            }
        }
    }

    private char[][] toNormalizedMatrix(Set<Coordinate> clayCoordinates, int minX, int minY, int maxX, int maxY) {
        char[][] map = new char[maxY - minY + 1][maxX - minX + 1];
        clayCoordinates.forEach(coordinate -> map[coordinate.getY() - minY][coordinate.getX() - minX] = CLAY);
        return map;
    }

    private int flowLeft(char[][] map, int x, int y) {
        return flowInDirection(map, x, y, -1);
    }

    private int flowRight(char[][] map, int x, int y) {
        return flowInDirection(map, x, y, 1);
    }

    private int flowInDirection(char[][] map, int x, int y, int direction) {
        int newX = x + direction;
        while (newX >= 0 && newX < map[y].length) {
            char current = map[y][newX];
            char below = map[y + 1][newX];
            if (current == CLAY) {
                if (below == CLAY || below == SETTLED) {
                    return newX; //Bounding wall
                } else {
                    return -1;
                }
            } else {
                map[y][newX] = FLOWING;
                if (below != CLAY && below != SETTLED) {
                    return -1;
                }
            }
            newX += direction;
        }
        return -1;
    }

    private Stream<Coordinate> parseCoordinates(String reservoir) {
        Matcher vertical = VERTICAL_RESERVOIR.matcher(reservoir);
        if (vertical.matches()) {
            int x = Integer.parseInt(vertical.group(1));
            return IntStream.range(
                    Integer.parseInt(vertical.group(2)),
                    Integer.parseInt(vertical.group(3)) + 1
            ).mapToObj(y -> new Coordinate(x, y));
        }
        Matcher horizontal = HORIZONTAL_RESERVOIR.matcher(reservoir);
        if (horizontal.matches()) {
            int y = Integer.parseInt(horizontal.group(1));
            return IntStream.range(
                    Integer.parseInt(horizontal.group(2)),
                    Integer.parseInt(horizontal.group(3)) + 1
            ).mapToObj(x -> new Coordinate(x, y));
        }
        throw new IllegalStateException("Does not compute");
    }

    @Getter
    @AllArgsConstructor
    private class Coordinate {
        private int x;
        private int y;
    }
}
