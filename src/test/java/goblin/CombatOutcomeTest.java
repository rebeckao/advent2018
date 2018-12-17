package goblin;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CombatOutcomeTest {

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
                    "$$$$$$$$$,18740",
            "$$$$$$$;" +
                    "$.E..G$;" +
                    "$.$$$$$;" +
                    "$G$$$$$;" +
                    "$$$$$$$, 10234",
            "$$$$$$$$$$$$$$$$;" +
                    "$.......G......$;" +
                    "$G.............$;" +
                    "$..............$;" +
                    "$....$$$$$$$$$$$;" +
                    "$....$$$$$$$$$$$;" +
                    "$.......EG.....$;" +
                    "$$$$$$$$$$$$$$$$, 18468"
    })
    void outcome(String map, int expected) {
        int actual = CombatOutcome.outcomeWithoutWeapons(List.of(map.split(";")));
        assertEquals(expected, actual);
    }

    @ParameterizedTest
    @CsvSource({
            "$$$$$$$;" +
                    "$.G...$;" +
                    "$...EG$;" +
                    "$.$.$G$;" +
                    "$..G$E$;" +
                    "$.....$;" +
                    "$$$$$$$,4988",
            "$$$$$$$;" +
                    "$E..EG$;" +
                    "$.$G.E$;" +
                    "$E.$$E$;" +
                    "$G..$.$;" +
                    "$..E$.$;" +
                    "$$$$$$$,31284",
            "$$$$$$$;" +
                    "$E.G$.$;" +
                    "$.$G..$;" +
                    "$G.$.G$;" +
                    "$G..$.$;" +
                    "$...E.$;" +
                    "$$$$$$$,3478",
            "$$$$$$$;" +
                    "$.E...$;" +
                    "$.$..G$;" +
                    "$.$$$.$;" +
                    "$E$G$G$;" +
                    "$...$G$;" +
                    "$$$$$$$,6474",
            "$$$$$$$$$;" +
                    "$G......$;" +
                    "$.E.$...$;" +
                    "$..$$..G$;" +
                    "$...$$..$;" +
                    "$...$...$;" +
                    "$.G...G.$;" +
                    "$.....G.$;" +
                    "$$$$$$$$$,1140"
    })
    void outcomeWithWeapons(String map, int expected) {
        int actual = CombatOutcome.outcomeWithWeapons(List.of(map.split(";")));
        assertEquals(expected, actual);
    }
}