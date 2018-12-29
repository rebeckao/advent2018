package immunesystem;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

@Builder
@Getter
class Squad {
    private static final Pattern SQUAD = Pattern.compile(
            "(\\d+) units each with (\\d+) hit points ?(\\(.*\\))? " +
                    "with an attack that does (\\d+) ([a-z]*) damage at initiative (\\d+)");
    private static final Pattern MODIFIERS = Pattern.compile("\\((?:(weak to (?:[a-z, ]*))|(?:; )|(immune to (?:[a-z, ]*)))+\\)");

    private int number;
    private ArmyType armyType;
    @Setter
    private int units;
    private int hitPoints;
    private Set<DamageType> immuneTo;
    private Set<DamageType> weakTo;
    private DamageType damageType;
    @Setter
    private int damageAmount;
    private int initiative;

    int effectivePower() {
        return Math.max(0, units * damageAmount);
    }

    int damageFrom(Squad attacker) {
        if (immuneTo.contains(attacker.getDamageType())) {
            return 0;
        } else if (weakTo.contains(attacker.getDamageType())) {
            return 2 * attacker.effectivePower();
        } else {
            return attacker.effectivePower();
        }
    }

    static Squad parse(ArmyType armyType, int number, String description) {
        Matcher groupMatcher = SQUAD.matcher(description);
        if (!groupMatcher.matches()) {
            throw new IllegalArgumentException("Does not compute: " + description);
        }
        int units = Integer.parseInt(groupMatcher.group(1));
        int hitPoints = Integer.parseInt(groupMatcher.group(2));
        Set<DamageType> immuneTo = new HashSet<>();
        Set<DamageType> weakTo = new HashSet<>();
        int damageAmount;
        DamageType damageType;
        int initiative;
        if (groupMatcher.group(3) != null) {
            String modifiers = groupMatcher.group(3);
            Matcher modifierMatcher = MODIFIERS.matcher(modifiers);
            if (!modifierMatcher.matches()) {
                throw new IllegalArgumentException("Does not compute: " + modifiers);
            }
            for (int group = 1; group <= modifierMatcher.groupCount(); group++) {
                String match = modifierMatcher.group(group);
                if (match == null) {
                    continue;
                }
                if (match.startsWith("weak to ")) {
                    Stream.of(match.substring(8).split(", "))
                            .map(DamageType::valueOf)
                            .forEach(weakTo::add);
                }
                if (match.startsWith("immune to ")) {
                    Stream.of(match.substring(10).split(", "))
                            .map(DamageType::valueOf)
                            .forEach(immuneTo::add);
                }
            }
        }
        damageAmount = Integer.parseInt(groupMatcher.group(4));
        damageType = DamageType.valueOf(groupMatcher.group(5));
        initiative = Integer.parseInt(groupMatcher.group(6));
        return Squad.builder()
                .number(number)
                .armyType(armyType)
                .units(units)
                .hitPoints(hitPoints)
                .immuneTo(immuneTo)
                .weakTo(weakTo)
                .damageType(damageType)
                .damageAmount(damageAmount)
                .initiative(initiative)
                .build();
    }
}
