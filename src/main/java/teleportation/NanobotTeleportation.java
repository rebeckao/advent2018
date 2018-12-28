package teleportation;

import lombok.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class NanobotTeleportation {
    private static final Pattern NANOBOT = Pattern.compile("pos=<(-?\\d+),(-?\\d+),(-?\\d+)>, r=(\\d+)");
    private List<Nanobot> nanobots;

    public static void main(String[] args) {
        String fileName = "./src/main/resources/day23_nanobots.txt";
        try (Stream<String> stream = Files.lines(Paths.get(fileName))) {
            NanobotTeleportation nanobotTeleportation = new NanobotTeleportation(stream);
            System.out.println(nanobotTeleportation.botsInRangeOfStrongestNanobot());
            System.out.println(nanobotTeleportation.distanceToPositionInRangeOfMostNanobots());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    NanobotTeleportation(Stream<String> botLines) {
        nanobots = botLines.map(NANOBOT::matcher)
                .filter(Matcher::matches)
                .map(match -> Nanobot.builder()
                        .position(Position.builder()
                                .x(Integer.parseInt(match.group(1)))
                                .y(Integer.parseInt(match.group(2)))
                                .z(Integer.parseInt(match.group(3)))
                                .build()
                        )
                        .r(Integer.parseInt(match.group(4)))
                        .build())
                .collect(Collectors.toList());
    }

    long botsInRangeOfStrongestNanobot() {
        Nanobot strongest = nanobots.stream()
                .max(Comparator.comparing(Nanobot::getR))
                .orElseThrow(() -> new IllegalStateException("Unable to comply"));
        return nanobots.stream()
                .map(Nanobot::getPosition)
                .map(distance -> distance.distanceFrom(strongest.getPosition()))
                .filter(distance -> distance <= strongest.getR())
                .count();
    }

    int distanceToPositionInRangeOfMostNanobots() {
        PriorityQueue<Cuboid> queue = new PriorityQueue<>(
                Comparator.comparing(Cuboid::getInRange)
                        .reversed()
                        .thenComparing(Cuboid::getMinCorner)
        );
        queue.add(firstCuboid());

        while (!queue.isEmpty()) {
            Cuboid current = queue.remove();
            Position minCorner = current.getMinCorner();
            Position maxCorner = current.getMaxCorner();
            if (maxCorner.getX() - minCorner.getX() == 1
                    && maxCorner.getY() - minCorner.getY() == 1
                    && maxCorner.getZ() - minCorner.getZ() == 1) {
                int distance = minCorner.distanceFrom(new Position(0, 0, 0));
                System.out.println("Best position found: " + minCorner + ", in range of " + botsInRange(minCorner) + " bots, distance: " + distance);
                return distance;
            }
            int halfX = Math.max(1, (maxCorner.getX() - minCorner.getX()) / 2 + minCorner.getX());
            int halfY = Math.max(1, (maxCorner.getY() - minCorner.getY()) / 2 + minCorner.getY());
            int halfZ = Math.max(1, (maxCorner.getZ() - minCorner.getZ()) / 2 + minCorner.getZ());
            resolveSubCuboid(queue, minCorner, new Position(halfX, halfY, halfZ));
            resolveSubCuboid(queue, new Position(minCorner.getX(), minCorner.getY(), halfZ), new Position(halfX, halfY, maxCorner.getZ()));
            resolveSubCuboid(queue, new Position(minCorner.getX(), halfY, minCorner.getZ()), new Position(halfX, maxCorner.getY(), halfZ));
            resolveSubCuboid(queue, new Position(halfX, minCorner.getY(), minCorner.getZ()), new Position(maxCorner.getX(), halfY, halfZ));
            resolveSubCuboid(queue, new Position(halfX, halfY, minCorner.getZ()), new Position(maxCorner.getX(), maxCorner.getY(), halfZ));
            resolveSubCuboid(queue, new Position(minCorner.getX(), halfY, halfZ), new Position(halfX, maxCorner.getY(), maxCorner.getZ()));
            resolveSubCuboid(queue, new Position(halfX, minCorner.getY(), halfZ), new Position(maxCorner.getX(), halfY, maxCorner.getZ()));
            resolveSubCuboid(queue, new Position(halfX, halfY, halfZ), maxCorner);
        }
        return 0;
    }

    private void resolveSubCuboid(PriorityQueue<Cuboid> queue, Position minCorner, Position newMax) {
        Cuboid next = new Cuboid(minCorner, newMax);
        Integer inRangeOfNext = resolveInRange(next);
        if (inRangeOfNext > 0) {
            next.setInRange(inRangeOfNext);
            queue.add(next);
        }
    }

    private Integer resolveInRange(Cuboid cuboid) {
        return (int) nanobots.stream()
                .filter(bot -> inRange(cuboid, bot))
                .count();
    }

    private boolean inRange(Cuboid cuboid, Nanobot bot) {
        Position position = bot.getPosition();
        Position minCorner = cuboid.getMinCorner();
        Position maxCorner = cuboid.getMaxCorner();
        boolean botIsInsideCuboid = (position.getX() >= minCorner.getX() && position.getX() < maxCorner.getX())
                && (position.getY() >= minCorner.getY() && position.getY() < maxCorner.getY())
                && (position.getZ() >= minCorner.getZ() && position.getZ() < maxCorner.getZ());
        if (botIsInsideCuboid) {
            return true;
        }
        if (position.getX() + bot.getR() < minCorner.getX()) {
            return false;
        }
        if (position.getY() + bot.getR() < minCorner.getY()) {
            return false;
        }
        if (position.getZ() + bot.getR() < minCorner.getZ()) {
            return false;
        }
        if (position.getX() - bot.getR() >= maxCorner.getX()) {
            return false;
        }
        if (position.getY() - bot.getR() >= maxCorner.getY()) {
            return false;
        }
        if (position.getZ() - bot.getR() >= maxCorner.getZ()) {
            return false;
        }
        List<Position> corners = List.of(
                minCorner,
                new Position(minCorner.getX(), minCorner.getY(), maxCorner.getZ() - 1),
                new Position(minCorner.getX(), maxCorner.getY() - 1, minCorner.getZ()),
                new Position(maxCorner.getX() - 1, minCorner.getY(), minCorner.getZ()),
                new Position(maxCorner.getX() - 1, maxCorner.getY() - 1, minCorner.getZ()),
                new Position(maxCorner.getX() - 1, minCorner.getY(), maxCorner.getZ() - 1),
                new Position(minCorner.getX(), maxCorner.getY() - 1, maxCorner.getZ() - 1),
                new Position(maxCorner.getX() - 1, maxCorner.getY() - 1, maxCorner.getZ() - 1)
        );
        for (Position corner : corners) {
            if (inRange(corner, bot)) {
                return true;
            }
        }
        if (position.getX() >= minCorner.getX() && position.getX() < maxCorner.getX()
                && position.getY() >= minCorner.getY() && position.getY() < maxCorner.getY()
        ) {
            return true;
        }
        if (position.getZ() >= minCorner.getZ() && position.getZ() < maxCorner.getZ()
                && position.getY() >= minCorner.getY() && position.getY() < maxCorner.getY()
        ) {
            return true;
        }
        if (position.getX() >= minCorner.getX() && position.getX() < maxCorner.getX()
                && position.getZ() >= minCorner.getZ() && position.getZ() < maxCorner.getZ()
        ) {
            return true;
        }
        return false;
    }

    private boolean inRange(Position position, Nanobot nanobot) {
        return nanobot.getPosition().distanceFrom(position) <= nanobot.getR();
    }

    private Cuboid firstCuboid() {
        int minX = nanobots.stream().map(Nanobot::getPosition).mapToInt(Position::getX).min().orElseThrow();
        int minY = nanobots.stream().map(Nanobot::getPosition).mapToInt(Position::getY).min().orElseThrow();
        int minZ = nanobots.stream().map(Nanobot::getPosition).mapToInt(Position::getZ).min().orElseThrow();
        int maxX = nanobots.stream().map(Nanobot::getPosition).mapToInt(Position::getX).max().orElseThrow();
        int maxY = nanobots.stream().map(Nanobot::getPosition).mapToInt(Position::getY).max().orElseThrow();
        int maxZ = nanobots.stream().map(Nanobot::getPosition).mapToInt(Position::getZ).max().orElseThrow();
        return new Cuboid(new Position(minX, minY, minZ), new Position(maxX + 1, maxY + 1, maxZ + 1), nanobots.size());
    }

    private int botsInRange(Position position) {
        return (int) nanobots.stream()
                .filter(bot -> bot.getPosition().distanceFrom(position) <= bot.getR())
                .count();
    }

    @AllArgsConstructor
    @Getter
    private static class Cuboid implements Comparable {
        private Position minCorner;
        private Position maxCorner;
        @Setter
        private Integer inRange;

        Cuboid(Position minCorner, Position maxCorner) {
            this.minCorner = minCorner;
            this.maxCorner = maxCorner;
        }

        @Override
        public int compareTo(Object o) {
            return inRange.compareTo(((Cuboid) o).inRange);
        }
    }

    @Builder
    @Getter
    private static class Nanobot {
        private Position position;
        private int r;
    }

    @Builder
    @Getter
    @EqualsAndHashCode
    @ToString
    static class Position implements Comparable {
        private int x;
        private int y;
        private int z;

        int distanceFrom(Position other) {
            return Math.abs(x - other.getX()) + Math.abs(y - other.getY()) + Math.abs(z - other.getZ());
        }

        @Override
        public int compareTo(Object o) {
            Position other = (Position) o;
            return Integer.compare(x + y + z, other.x + other.y + other.z);
        }
    }
}
