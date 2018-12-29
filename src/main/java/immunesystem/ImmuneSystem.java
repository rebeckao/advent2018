package immunesystem;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;

import static java.util.Comparator.comparing;

class ImmuneSystem {
    private Set<Squad> armies = new HashSet<>();
    @Setter
    private boolean infectionWinAllowed = true;

    public static void main(String[] args) {
        String fileName = "./src/main/resources/day24_immune_system.txt";
        try {
            List<String> lines = Files.readAllLines(Paths.get(fileName));
            System.out.println(new ImmuneSystem(lines).unitsOfWinningArmy());
            System.out.println(unitsOfWinningArmyAfterBoost(lines));
        } catch (IOException | InfectionWarException e) {
            e.printStackTrace();
        }
    }

    ImmuneSystem(List<String> lines) {
        parse(lines);
    }

    private static int unitsOfWinningArmyAfterBoost(List<String> lines) {
        int boost = 0;
        while (true) {
            ImmuneSystem immuneSystem = new ImmuneSystem(lines);
            immuneSystem.setInfectionWinAllowed(false);
            immuneSystem.boost(boost);
            try {
                return immuneSystem.unitsOfWinningArmy();
            } catch (InfectionWarException e) {
                System.out.println("Boost too low: " + boost);
                boost++;
            }
        }
    }

    private void boost(int boost) {
        armies.stream()
                .filter(squad -> squad.getArmyType().equals(ArmyType.IMMUNE_SYSTEM))
                .forEach(squad -> squad.setDamageAmount(squad.getDamageAmount() + boost));
    }

    int unitsOfWinningArmy() throws InfectionWarException {
        while (hasUnits(ArmyType.IMMUNE_SYSTEM) && hasUnits(ArmyType.INFECTION)) {
            Set<Squad> targets = new HashSet<>(armies);
            Set<Attack> attacks = new HashSet<>();
            armies.stream()
                    .sorted(
                            comparing(Squad::effectivePower).reversed()
                                    .thenComparing(comparing(Squad::getInitiative).reversed())
                    )
                    .forEach(attacker ->
                            chooseTarget(targets, attacks, attacker)
                    );

            AtomicInteger totalKills = new AtomicInteger();
            attacks.stream()
                    .sorted(comparing(Attack::getInitiative).reversed())
                    .forEach(attack -> {
                        Squad target = attack.getTarget();
                        Squad attacker = attack.getAttacker();
                        int kills = target.damageFrom(attacker) / target.getHitPoints();
                        totalKills.addAndGet(kills);
                        int remainingUnits = target.getUnits() - kills;
                        target.setUnits(remainingUnits);
                        if (remainingUnits <= 0) {
                            armies.remove(target);
                        }
                    });
            if (totalKills.get() == 0) {
                throw new InfectionWarException();
            }
        }
        if (!infectionWinAllowed && armies.stream().anyMatch(squad -> squad.getArmyType().equals(ArmyType.INFECTION))) {
            throw new InfectionWarException();
        }
        return armies.stream()
                .mapToInt(Squad::getUnits)
                .sum();
    }

    private void chooseTarget(Set<Squad> targets, Set<Attack> attacks, Squad attacker) {
        targets.stream()
                .filter(target1 -> !target1.getArmyType().equals(attacker.getArmyType()))
                .filter(target -> target.damageFrom(attacker) > 0)
                .max(
                        comparing((Function<Squad, Integer>) target -> target.damageFrom(attacker))
                                .thenComparing(Squad::effectivePower)
                                .thenComparing(Squad::getInitiative)
                )
                .ifPresent(target -> {
                    targets.remove(target);
                    attacks.add(new Attack(attacker, target, attacker.getInitiative()));
                });
    }

    private boolean hasUnits(ArmyType type) {
        return armies.stream()
                .anyMatch(squad -> squad.getArmyType().equals(type));
    }

    private void parse(List<String> lines) {
        ArmyType currentArmy = ArmyType.IMMUNE_SYSTEM;
        int number = 1;
        for (String line : lines) {
            if (line.equals("")) {
                continue;
            }
            if (line.equals("Immune System:")) {
                currentArmy = ArmyType.IMMUNE_SYSTEM;
                number = 1;
                continue;
            }
            if (line.equals("Infection:")) {
                currentArmy = ArmyType.INFECTION;
                number = 1;
                continue;
            }
            armies.add(Squad.parse(currentArmy, number++, line));
        }
    }

    @AllArgsConstructor
    @Getter
    private static class Attack {
        private Squad attacker;
        private Squad target;
        private int initiative;

        @Override
        public String toString() {
            return String.format("%s group %d attacks defending group %d with %d damage",
                    attacker.getArmyType(), attacker.getNumber(), target.getNumber(), target.damageFrom(attacker));
        }
    }

    static class InfectionWarException extends Exception {

    }
}
