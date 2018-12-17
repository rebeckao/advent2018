package goblin;

import java.util.*;

class Pathfinder {
    private CombatOutcome combatOutcome;

    Pathfinder(CombatOutcome combatOutcome) {
        this.combatOutcome = combatOutcome;
    }

    DungeonPath shortestPath(Position currentPosition, List<Position> goals) {
        Set<Position> visited = new HashSet<>();
        Queue<DungeonPath> queue = new LinkedList<>();
        goals.stream()
                .sorted()
                .map(DungeonPath::fromStartingPoint)
                .forEach(queue::add);
        while(!queue.isEmpty()) {
            DungeonPath current = queue.remove();
            if (current.lastStep().equals(currentPosition)) {
                return current.reverse();
            }
            else {
                combatOutcome.adjecentPositions(current.lastStep())
                        .filter(pos -> pos.equals(currentPosition) || combatOutcome.positionIsUnoccupied(pos))
                        .filter(pos -> !visited.contains(pos))
                        .map(pos -> DungeonPath.branchOffPath(current, pos))
                        .forEach(path -> {
                            queue.add(path);
                            visited.add(path.lastStep());
                        });
            }
        }
        return null;
    }
}
