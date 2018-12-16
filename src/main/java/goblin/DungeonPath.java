package goblin;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
class DungeonPath {
    private List<Position> steps;
    private Position nextStep;

    private DungeonPath(List<Position> steps) {
        this.steps = steps;
    }

    static DungeonPath fromStartingPoint(Position start) {
        ArrayList<Position> steps = new ArrayList<>();
        steps.add(start);
        return new DungeonPath(steps);
    }

    static DungeonPath branchOffPath(DungeonPath path, Position step) {
        ArrayList<Position> steps = new ArrayList<>(path.getSteps());
        steps.add(step);
        return new DungeonPath(steps);
    }

    Position nextStep() {
        return nextStep;
    }

    Position lastStep() {
        return steps.get(steps.size() - 1);
    }

    DungeonPath reverse() {
        nextStep = steps.get(steps.size() - 2);
        return this;
    }
}
