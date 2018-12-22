package facility;

import common.Util;
import lombok.Getter;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

class ElfFacility {
    @Getter
    private int roomsAtLeast1000DoorsAway = 0;

    public static void main(String[] args) {
        try {
            ElfFacility elfFacility = new ElfFacility();
            System.out.println(elfFacility.numberOfDoorsToFarthestRoom(
                    Files.readAllLines(Paths.get("./src/main/resources/day20_routes.txt")).get(0)
            ));
            System.out.println(elfFacility.getRoomsAtLeast1000DoorsAway());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    int numberOfDoorsToFarthestRoom(String routes) {
        int length = routes.length();
        char[][] map = new char[length * 2][length * 2];
        int startRow = length;
        int startColumn = length;
        buildMap(routes, map, startRow, startColumn);

        int[] boundaries = resolveNewBoundaries(length, map);
        int minRow = boundaries[0];
        int maxRow = boundaries[1];
        int minCol = boundaries[2];
        int maxCol = boundaries[3];
        startRow -= minRow;
        startColumn -= minCol;
        map = normalize(map, minRow, maxRow, minCol, maxCol);
        fillInWalls(map);

        Util.display(map);

        return numberOfDoorsToFarthestRoom(map, startRow, startColumn);
    }

    private char[][] normalize(char[][] map, int minRow, int maxRow, int minCol, int maxCol) {
        char[][] normalized = new char[maxRow - minRow + 1][maxCol - minCol + 1];
        for (int row = minRow; row <= maxRow; row++) {
            if (maxCol + 1 - minCol >= 0)
                System.arraycopy(map[row], minCol, normalized[row - minRow], 0, maxCol + 1 - minCol);
        }
        return normalized;
    }

    private int[] resolveNewBoundaries(int length, char[][] map) {
        int minRow = length;
        int maxRow = 0;
        int minCol = length;
        int maxCol = 0;
        for (int row = 0; row < map.length; row++) {
            for (int col = 0; col < map[row].length; col++) {
                if (map[row][col] != 0) {
                    minRow = Math.min(row, minRow);
                    maxRow = Math.max(row, maxRow);
                    minCol = Math.min(col, minCol);
                    maxCol = Math.max(col, maxCol);
                }
            }
        }
        minRow--;
        maxRow++;
        minCol--;
        maxCol++;
        return new int[]{minRow, maxRow, minCol, maxCol};
    }

    private void fillInWalls(char[][] map) {
        for (int row = 0; row < map.length; row++) {
            for (int col = 0; col < map[row].length; col++) {
                if (map[row][col] == 0) {
                    map[row][col] = '#';
                }
            }
        }
    }

    private void buildMap(String routes, char[][] map, int startRow, int startColumn) {
        int currentRow = startRow;
        int currentColumn = startColumn;
        map[startRow][startColumn] = 'X';
        Deque<Position> branchingPoints = new ArrayDeque<>();
        Iterator<Character> nextSteps = routes.chars().mapToObj(c -> (char) c).iterator();
        nextSteps.next(); // ^
        while (true) {
            Character next = nextSteps.next();
            if (next == '(') {
                branchingPoints.addFirst(new Position(currentRow, currentColumn));
            } else if (next == '|') {
                Position lastBranchingPoint = branchingPoints.getFirst();
                currentColumn = lastBranchingPoint.getColumn();
                currentRow = lastBranchingPoint.getRow();
            } else if (next == ')') {
                branchingPoints.pop();
            } else if (next == '$') {
                break;
            } else {
                if (next == 'N') {
                    map[currentRow - 1][currentColumn] = '-';
                    map[currentRow - 2][currentColumn] = '.';
                    currentRow -= 2;
                } else if (next == 'S') {
                    map[currentRow + 1][currentColumn] = '-';
                    map[currentRow + 2][currentColumn] = '.';
                    currentRow += 2;
                } else if (next == 'W') {
                    map[currentRow][currentColumn - 1] = '|';
                    map[currentRow][currentColumn - 2] = '.';
                    currentColumn -= 2;
                } else if (next == 'E') {
                    map[currentRow][currentColumn + 1] = '|';
                    map[currentRow][currentColumn + 2] = '.';
                    currentColumn += 2;
                }
            }
        }
    }

    private int numberOfDoorsToFarthestRoom(char[][] map, int startRow, int startColumn) {
        Set<Position> visited = new HashSet<>();
        Queue<List<Position>> queue = new LinkedList<>();
        List<Position> currentPath = new ArrayList<>();
        Position start = new Position(startRow, startColumn);
        currentPath.add(start);
        queue.add(currentPath);
        visited.add(start);
        while (!queue.isEmpty()) {
            currentPath = queue.remove();
            Position pos = currentPath.get(currentPath.size() - 1);
            takeAStep(map, pos, -1, 0, visited, currentPath, queue);
            takeAStep(map, pos, 1, 0, visited, currentPath, queue);
            takeAStep(map, pos, 0, -1, visited, currentPath, queue);
            takeAStep(map, pos, 0, 1, visited, currentPath, queue);
        }
        return currentPath.size() - 1;
    }

    private void takeAStep(char[][] map, Position pos, int rowModifier, int columnModifier, Set<Position> visited, List<Position> currentPath, Queue<List<Position>> queue) {
        char intermediary = map[pos.getRow() + rowModifier][pos.getColumn() + columnModifier];
        if (intermediary == '-' || intermediary == '|') {
            Position newPos = new Position(pos.getRow() + rowModifier * 2, pos.getColumn() + columnModifier * 2);
            if (!visited.contains(newPos)) {
                ArrayList<Position> newPath = new ArrayList<>(currentPath);
                newPath.add(newPos);
                visited.add(newPos);
                queue.add(newPath);
                if (newPath.size() > 1000) {
                    roomsAtLeast1000DoorsAway++;
                }
            }
        }
    }

}
