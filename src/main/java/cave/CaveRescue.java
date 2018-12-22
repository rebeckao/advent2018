package cave;

import common.Util;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class CaveRescue {
    private char[][] regionTypes;
    private Map<Position, Integer> visited = new HashMap<>();
    private int targetX;
    private int targetY;

    public static void main(String[] args) {
        try {
            List<String> scan = Files.readAllLines(Paths.get("./src/main/resources/day22_scan.txt"));
            int depth = Integer.parseInt(scan.get(0).substring(7));
            Pattern pattern = Pattern.compile("target: (\\d+),(\\d+)");
            Matcher matcher = pattern.matcher(scan.get(1));
            if (!matcher.matches()) {
                throw new IllegalArgumentException("Does not compute: " + scan.get(1));
            }
            int targetX = Integer.parseInt(matcher.group(1));
            int targetY = Integer.parseInt(matcher.group(2));
            System.out.println(new CaveRescue(depth, targetX, targetY).fastestRescueTime());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    enum Tool {TORCH, CLIMBING_GEAR, NEITHER}

    CaveRescue(int depth, int targetX, int targetY) {
        Cave cave = new Cave();
        int[][] erosionMap = cave.buildErosionMap(depth, targetX, targetY, Math.max(100, targetX), Math.max(1000, targetY));
        regionTypes = cave.buildRegionCharMap(cave.buildRegionMap(erosionMap));
        this.targetX = targetX;
        this.targetY = targetY;
    }

    int fastestRescueTime() {
        Queue<Path> queue = new LinkedList<>();
        List<Position> initialStep = new ArrayList<>();
        Position startPosition = new Position(0, 0, Tool.TORCH);
        initialStep.add(startPosition);
        queue.add(new Path(initialStep, 0));
        visited.put(startPosition, 0);
        int fastest = -1;
        while (!queue.isEmpty()) {
            Path current = queue.remove();
            Position position = current.lastPostion();
            if (visited.get(position) < current.getTime()) {
                continue;
            }
            if (position.getX() == targetX && position.getY() == targetY) {
                if (fastest == -1 || current.getTime() <= fastest) {
                    fastest = current.getTime();
                    System.out.println("Path found: " + current);
//                    display(current);
                }
                continue;
            }
            Tool equipped = position.getEquipped();
            int maxTime = fastest;
            adjecentPositions(position)
                    .sorted(Comparator.comparing(pos -> pos.changeTime(equipped)))
                    .map(newPosition -> Path.branchOff(current, newPosition))
                    .filter(path -> maxTime == -1 || path.getTime() < maxTime)
                    .filter(path -> !alreadyVisitedFaster(path))
                    .forEach(queue::add);
        }

        return fastest;
    }

    private void display(Path current) {
        System.out.println(current.getPositions().stream()
                .map(Position::toString)
                .collect(Collectors.joining("\n")));
        for (Position position : current.getPositions()) {
            System.out.println("At " + regionTypes[position.getY()][position.getX()]
                    + " with " + position.getEquipped());
            char[][] snapshot = Util.clone(regionTypes);
            snapshot[position.getY()][position.getX()] = 'X';
            snapshot[targetY][targetX] = 'T';
            snapshot[0][0] = 'M';
            Util.display(snapshot);
        }
    }

    private boolean alreadyVisitedFaster(Path path) {
        Position pos = path.lastPostion();
        if (!visited.containsKey(pos) || visited.get(pos) > path.getTime()) {
            visited.put(pos, path.getTime());
            return false;
        } else {
            return true;
        }
    }

    private Stream<Position> adjecentPositions(Position position) {
        Tool equipped = position.getEquipped();
        int x = position.getX();
        int y = position.getY();
        Set<Tool> currentlyAllowed = allowedTools(x, y);
        return Stream.of(
                List.of(x, y + 1),
                List.of(x + 1, y),
                List.of(x, y - 1),
                List.of(x - 1, y)
        )
                .filter(coord -> coord.get(0) >= 0)
                .filter(coord -> coord.get(0) < regionTypes[0].length)
                .filter(coord -> coord.get(1) >= 0)
                .filter(coord -> coord.get(1) < regionTypes.length)
                .map(coord -> possibleNextSteps(equipped, currentlyAllowed, coord.get(0), coord.get(1)))
                .flatMap(Function.identity());
    }

    private Stream<? extends Position> possibleNextSteps(Tool equipped, Set<Tool> currentlyAllowed, Integer x, Integer y) {
        Set<Tool> allowedToolsInNextRegion = allowedTools(x, y);
        if (allowedToolsInNextRegion.contains(equipped)) {
            return Stream.of(new Position(x, y, equipped));
        } else {
            return allowedToolsInNextRegion.stream()
                    .filter(currentlyAllowed::contains)
                    .map(tool -> new Position(x, y, tool));
        }
    }

    private Set<Tool> allowedTools(Integer x, Integer y) {
        char regionType = regionTypes[y][x];
        if (x == targetX && y == targetY) {
            return Set.of(Tool.TORCH);
        }
        switch (regionType) {
            case '.':
                return Set.of(Tool.TORCH, Tool.CLIMBING_GEAR);
            case '=':
                return Set.of(Tool.CLIMBING_GEAR, Tool.NEITHER);
            case '|':
                return Set.of(Tool.TORCH, Tool.NEITHER);
            default:
                throw new IllegalStateException("Does not compute: " + regionType);
        }
    }

    @AllArgsConstructor
    @Getter
    @ToString
    private static class Path {
        private List<Position> positions;
        private int time;

        static Path branchOff(Path previousPath, Position nextStep) {
            ArrayList<Position> positions = new ArrayList<>(previousPath.getPositions());
            positions.add(nextStep);
            int time = previousPath.getTime() + 1 + nextStep.changeTime(previousPath.lastPostion().getEquipped());
            return new Path(positions, time);
        }

        Position lastPostion() {
            return positions.get(positions.size() - 1);
        }
    }

    @AllArgsConstructor
    @EqualsAndHashCode
    @Getter
    @ToString
    private static class Position {
        private int x;
        private int y;
        private Tool equipped;

        int changeTime(Tool alreadyEquipped) {
            return alreadyEquipped == equipped ? 0 : 7;
        }
    }
}
