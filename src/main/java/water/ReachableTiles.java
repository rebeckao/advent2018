package water;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

class ReachableTiles {
    private static final Pattern VERTICAL_RESERVOIR = Pattern.compile("x=(\\d+), y=(\\d+)\\.\\.(\\d+)");
    private static final Pattern HORIZONTAL_RESERVOIR = Pattern.compile("y=(\\d+), x=(\\d+)\\.\\.(\\d+)");

    int tilesReachedByWater(Stream<String> coordinates) {
        Set<Coordinate> clayCoordinates = coordinates
                .flatMap(this::parseCoordinates)
                .collect(Collectors.toSet());
        int minX = clayCoordinates.stream().mapToInt(Coordinate::getX).min().orElseThrow(() -> new IllegalStateException("Does not compute"));
        int minY = clayCoordinates.stream().mapToInt(Coordinate::getY).min().orElseThrow(() -> new IllegalStateException("Does not compute"));
        int maxX = clayCoordinates.stream().mapToInt(Coordinate::getX).max().orElseThrow(() -> new IllegalStateException("Does not compute"));
        int maxY = clayCoordinates.stream().mapToInt(Coordinate::getY).max().orElseThrow(() -> new IllegalStateException("Does not compute"));
        char[][] map = new char[maxY - minY + 1][maxX - minX + 1];
        clayCoordinates.forEach(coordinate -> map[coordinate.getY() - minY][coordinate.getX() - minX] = '#');
        int xCoordinateOfSpring = 500 - minX;

        map[0][xCoordinateOfSpring] = '|';
        char[][] newMap = new char[map.length][map[0].length];
        while (newMap != map) {
            System.arraycopy( map, 0, map, 0, map.length);
            for (int y = 0; y < map.length - 1; y++) {
                char[] row = map[y];
                for (int x = 0; x < row.length; x++) {
                    char square = row[x];
                    if (square == '|') {
                        char below = map[y + 1][x];
                        if (below == '|') {
                            continue;
                        }
                        if (below == 0) {
                            map[y + 1][x] = '|';
                            continue;
                        }
                        int wallToTheLeft = flowLeft(map, x, y);
                        int wallToTheRight = flowRight(map, x, y);
                        if (wallToTheLeft != -1 && wallToTheRight != -1) {
                            for (int fillX = wallToTheLeft + 1; fillX < wallToTheRight; fillX++) {
                                map[y][fillX] = '~';
                            }
                        }
                    }
                }
            }
        }
        display(map);

        return 0;
    }

    private int flowLeft(char[][] map, int x, int y) {
        int stepsLeft = 1;
        while (x - stepsLeft >= 0) {
            char current = map[y][x - stepsLeft];
            if (current == '|') {
                System.out.println("Already flowing");
                return -1;
            }
            char below = map[y + 1][x - stepsLeft];
            if (current != '#') {
                map[y][x - stepsLeft] = '|';
                if (below == '#' || below == '~') {
                    return x - stepsLeft; //Bounding wall
                } else {
                    return -1;
                }
            }
            stepsLeft++;
        }
        return -1;
    }

    private int flowRight(char[][] map, int x, int y) {
        int stepsRight = 1;
        while (x + stepsRight < map[y].length) {
            char current = map[y][x + stepsRight];
            if (current == '|') {
                System.out.println("Already flowing");
                return -1;
            }
            char below = map[y + 1][x + stepsRight];
            if (current != '#') {
                map[y][x + stepsRight] = '|';
                if (below == '#' || below == '~') {
                    return x + stepsRight; //Bounding wall
                } else {
                    return -1;
                }
            }
            stepsRight++;
        }
        return -1;
    }

    private void display(char[][] clayMap) {
        for (char[] row : clayMap) {
            for (char square : row) {
                System.out.print(square == 0 ? '.' : square);
            }
            System.out.println();
        }
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
