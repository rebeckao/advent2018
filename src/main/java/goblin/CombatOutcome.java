package goblin;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.toList;

class CombatOutcome {
    private List<Combatant> combatants = new ArrayList<>();
    private char[][] dungeon;

    public static void main(String[] args) {
        try {
            List<String> lines = Files.readAllLines(Paths.get("./src/main/resources/day15_goblin_war.txt"));
            CombatOutcome combatOutcome = new CombatOutcome(lines);
            System.out.println(combatOutcome.outcome());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    CombatOutcome(List<String> map) {
        parseMap(map);
    }

    int outcome() {
        int completedTurns = 0;
        while (true) {
            List<Combatant> turnOrder = turnOrder();
            for (Combatant combatant : turnOrder) {
                if (combatant.isDead()) {
                    continue;
                }
                List<Combatant> possibleEnemies = possibleEnemies(combatant);
                if (possibleEnemies.isEmpty()) {
                    System.out.println("Victory for the " + combatant + "!" +
                            " Health: " + healthOfSurvivors() + ", turns: " + completedTurns);
                    return completedTurns * healthOfSurvivors();
                }
                if (attackIfPossible(combatant, possibleEnemies)) {
                    continue;
                }

                List<Position> possibleGoalPositions = possibleEnemies.stream()
                        .filter(enemy -> !enemy.isDead())
                        .map(Combatant::getPosition)
                        .flatMap(this::adjecentPositions)
                        .filter(this::positionIsUnoccupied)
                        .collect(toList());

                Optional<DungeonPath> bestPath = Optional.ofNullable(new Pathfinder(this)
                        .shortestPath(combatant.getPosition(), possibleGoalPositions));
                if (!bestPath.isPresent()) {
                    continue;
                }
                combatant.setPosition(bestPath.get().nextStep());
                attackIfPossible(combatant, possibleEnemies);
            }
            completedTurns++;
        }
    }

    private int healthOfSurvivors() {
        return combatants.stream()
                .filter(combatant -> !combatant.isDead())
                .mapToInt(Combatant::getHealthPoints)
                .sum();
    }

    private boolean attackIfPossible(Combatant combatant, List<Combatant> possibleEnemies) {
        Optional<Combatant> reachableEnemy = possibleEnemies.stream()
                .filter(enemy -> !enemy.isDead())
                .filter(enemy -> directlyReachableFrom(enemy, combatant.getPosition()))
                .min(comparing(Combatant::getHealthPoints).thenComparing(Combatant::getPosition));
        if (reachableEnemy.isPresent()) {
            Combatant target = reachableEnemy.get();
            attack(combatant, target);
            if (target.isDead()) {
                combatants.remove(target);
            }
            return true;
        }
        return false;
    }

    private void attack(Combatant attacker, Combatant target) {
        target.reduceHp(attacker.getAttackPower());
    }

    private boolean directlyReachableFrom(Combatant enemy, Position position) {
        Position enemyPosition = enemy.getPosition();
        return position.isNextTo(enemyPosition);
    }

    private List<Combatant> possibleEnemies(Combatant individual) {
        return combatants.stream()
                .filter(combatant -> combatant.getType() != individual.getType())
                .collect(toList());
    }

    boolean positionIsUnoccupied(Position position) {
        return combatants.stream()
                .filter(combatant -> !combatant.isDead())
                .noneMatch(certainCombatant -> certainCombatant.getPosition().equals(position));
    }

    Stream<Position> adjecentPositions(Position position) {
        int enemyRow = position.getRow();
        int enemyColumn = position.getColumn();
        return Stream.of(
                new Position(enemyRow - 1, enemyColumn),
                new Position(enemyRow, enemyColumn - 1),
                new Position(enemyRow, enemyColumn + 1),
                new Position(enemyRow + 1, enemyColumn)
        ).filter(pos -> dungeon[pos.getRow()][pos.getColumn()] == '.');
    }

    private List<Combatant> turnOrder() {
        return combatants.stream()
                .filter(combatant -> !combatant.isDead())
                .sorted(comparing(Combatant::getPosition))
                .collect(toList());
    }

    private void parseMap(List<String> map) {
        int rows = map.size();
        int columns = map.get(0).length();
        dungeon = new char[rows][columns];
        for (int row = 0; row < dungeon.length; row++) {
            String currentRow = map.get(row);
            for (int column = 0; column < currentRow.length(); column++) {
                char currentCharacter = currentRow.charAt(column);
                if (currentCharacter == 'G' || currentCharacter == 'E') {
                    combatants.add(new Combatant(row, column, currentCharacter));
                    currentCharacter = '.';
                }
                dungeon[row][column] = currentCharacter;
            }
        }
    }

    private void display() {
        for (int row = 0; row < dungeon.length; row++) {
            for (int column = 0; column < dungeon[row].length; column++) {
                Position currentPosition = new Position(row, column);
                Optional<Combatant> combatant = combatants.stream()
                        .filter(goblin -> goblin.getPosition().equals(currentPosition))
                        .findFirst();
                if (combatant.isPresent()) {
                    System.out.print(combatant.get());
                } else {
                    System.out.print(dungeon[row][column]);
                }
            }
            System.out.println();
        }
    }
}
