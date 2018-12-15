package goblin;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CombatOutcomeTest {
    private CombatOutcome combatOutcome = new CombatOutcome();

    @ParameterizedTest
    @CsvSource({
            "$$$$$$$;" +
                    "$.G...$;" +
                    "$...EG$;" +
                    "$.$.$G$;" +
                    "$..G$E$;" +
                    "$.....$;" +
                    "$$$$$$$,27730",
            "$$$$$$$;" +
                    "$G..$E$;" +
                    "$E$E.E$;" +
                    "$G.$$.$;" +
                    "$...$E$;" +
                    "$...E.$;" +
                    "$$$$$$$,36334",
            "$$$$$$$;" +
                    "$E..EG$;" +
                    "$.$G.E$;" +
                    "$E.$$E$;" +
                    "$G..$.$;" +
                    "$..E$.$;" +
                    "$$$$$$$,39514",
            "$$$$$$$;" +
                    "$E.G$.$;" +
                    "$.$G..$;" +
                    "$G.$.G$;" +
                    "$G..$.$;" +
                    "$...E.$;" +
                    "$$$$$$$,27755",
            "$$$$$$$;" +
                    "$.E...$;" +
                    "$.$..G$;" +
                    "$.$$$.$;" +
                    "$E$G$G$;" +
                    "$...$G$;" +
                    "$$$$$$$,28944",
            "$$$$$$$$$;" +
                    "$G......$;" +
                    "$.E.$...$;" +
                    "$..$$..G$;" +
                    "$...$$..$;" +
                    "$...$...$;" +
                    "$.G...G.$;" +
                    "$.....G.$;" +
                    "$$$$$$$$$,18740"
    })
    void outcome(String map, int expected) {
        int actual = combatOutcome.outcome(List.of(map.split(";")));
        assertEquals(expected, actual);
    }
}