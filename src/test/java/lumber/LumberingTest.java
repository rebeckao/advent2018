package lumber;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.Arrays;

import static common.Util.toCharMatrix;
import static org.junit.jupiter.api.Assertions.assertTrue;

class LumberingTest {
    private Lumbering lumbering = new Lumbering();

    @ParameterizedTest
    @CsvSource({
            "1," +
                    ".......%%.;" +
                    "......|%%%;" +
                    ".|..|...%.;" +
                    "..|%||...%;" +
                    "..%%||.|%|;" +
                    "...%||||..;" +
                    "||...|||..;" +
                    "|||||.||.|;" +
                    "||||||||||;" +
                    "....||..|.",

            "2," +
                    ".......%..;" +
                    "......|%..;" +
                    ".|.|||....;" +
                    "..%%|||..%;" +
                    "..%%%|||%|;" +
                    "...%|||||.;" +
                    "|||||||||.;" +
                    "||||||||||;" +
                    "||||||||||;" +
                    ".|||||||||",

            "3," +
                    ".......%..;" +
                    "....|||%..;" +
                    ".|.||||...;" +
                    "..%%%|||.%;" +
                    "...%%|||%|;" +
                    ".||%%|||||;" +
                    "||||||||||;" +
                    "||||||||||;" +
                    "||||||||||;" +
                    "||||||||||",

            "4," +
                    ".....|.%..;" +
                    "...||||%..;" +
                    ".|.%||||..;" +
                    "..%%%||||%;" +
                    "...%%%||%|;" +
                    "|||%%|||||;" +
                    "||||||||||;" +
                    "||||||||||;" +
                    "||||||||||;" +
                    "||||||||||",

            "5," +
                    "....|||%..;" +
                    "...||||%..;" +
                    ".|.%%||||.;" +
                    "..%%%%|||%;" +
                    ".|.%%%||%|;" +
                    "|||%%%||||;" +
                    "||||||||||;" +
                    "||||||||||;" +
                    "||||||||||;" +
                    "||||||||||",

            "6," +
                    "...||||%..;" +
                    "...||||%..;" +
                    ".|.%%%|||.;" +
                    "..%.%%|||%;" +
                    "|||%.%%|%|;" +
                    "|||%%%||||;" +
                    "||||%|||||;" +
                    "||||||||||;" +
                    "||||||||||;" +
                    "||||||||||",

            "7," +
                    "...||||%..;" +
                    "..||%|%%..;" +
                    ".|.%%%%||.;" +
                    "||%..%%||%;" +
                    "||%%.%%|%|;" +
                    "|||%%%%|||;" +
                    "|||%%%||||;" +
                    "||||||||||;" +
                    "||||||||||;" +
                    "||||||||||",

            "8," +
                    "..||||%%..;" +
                    "..|%%%%%..;" +
                    "|||%%%%%|.;" +
                    "||%...%%|%;" +
                    "||%%..%%%|;" +
                    "||%%.%%%||;" +
                    "|||%%%%|||;" +
                    "||||%|||||;" +
                    "||||||||||;" +
                    "||||||||||",

            "9," +
                    "..||%%%...;" +
                    ".||%%%%%..;" +
                    "||%%...%%.;" +
                    "||%....%%%;" +
                    "|%%....%%|;" +
                    "||%%..%%%|;" +
                    "||%%%%%%||;" +
                    "|||%%%||||;" +
                    "||||||||||;" +
                    "||||||||||",

            "10," +
                    ".||%%.....;" +
                    "||%%%.....;" +
                    "||%%......;" +
                    "|%%.....%%;" +
                    "|%%.....%%;" +
                    "|%%....%%|;" +
                    "||%%.%%%%|;" +
                    "||%%%%%|||;" +
                    "||||%|||||;" +
                    "||||||||||"
    })
    void afterMinutes(int minutes, String expectedString) {
        String originalString = ".%.%...|%.;" +
                ".....%|%%|;" +
                ".|..|...%.;" +
                "..|%.....%;" +
                "%.%|||%|%|;" +
                "...%.||...;" +
                ".|....|...;" +
                "||...%|.%|;" +
                "|.||||..|.;" +
                "...%.|..|.";
        char[][] original = toCharMatrix(originalString);
        char[][] actual = lumbering.afterMinutes(original, minutes);
        assertTrue(Arrays.deepEquals(toCharMatrix(expectedString), actual));
    }
}