package water;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.Arrays;
import java.util.stream.Stream;

import static common.Util.toCharMatrix;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ReachableTilesTest {
    private ReachableTiles reachableTiles = new ReachableTiles();

    @Test
    void tilesReachedByWater() {
        char[][] filled = reachableTiles.fillWithWater(Stream.of(
                "x=495, y=2..7",
                "y=7, x=495..501",
                "x=501, y=3..7",
                "x=498, y=2..4",
                "x=506, y=1..2",
                "x=498, y=10..13",
                "x=504, y=10..13",
                "y=13, x=498..504"
        ));
        int actual = reachableTiles.tilesReachedByWater(filled);
        assertEquals(57, actual);
    }
    @Test
    void tilesFilledByWater() {
        char[][] filled = reachableTiles.fillWithWater(Stream.of(
                "x=495, y=2..7",
                "y=7, x=495..501",
                "x=501, y=3..7",
                "x=498, y=2..4",
                "x=506, y=1..2",
                "x=498, y=10..13",
                "x=504, y=10..13",
                "y=13, x=498..504"
        ));
        int actual = reachableTiles.tilesFilledByWater(filled);
        assertEquals(29, actual);
    }

    @ParameterizedTest
    @CsvSource({
            "......|.......;" +
                    "............%.;" +
                    ".%..%.......%.;" +
                    ".%..%..%......;" +
                    ".%..%..%......;" +
                    ".%.....%......;" +
                    ".%.....%......;" +
                    ".%%%%%%%......;" +
                    "..............;" +
                    "..............;" +
                    "....%.....%...;" +
                    "....%.....%...;" +
                    "....%.....%...;" +
                    "....%%%%%%%...," +

                    "......|.......;" +
                    "......|.....%.;" +
                    ".%..%||||...%.;" +
                    ".%..%~~%|.....;" +
                    ".%..%~~%|.....;" +
                    ".%~~~~~%|.....;" +
                    ".%~~~~~%|.....;" +
                    ".%%%%%%%|.....;" +
                    "........|.....;" +
                    "...|||||||||..;" +
                    "...|%~~~~~%|..;" +
                    "...|%~~~~~%|..;" +
                    "...|%~~~~~%|..;" +
                    "...|%%%%%%%|..",

            "...........|............;" +
                    ".%....................%.;" +
                    ".%....................%.;" +
                    ".%....................%.;" +
                    ".%............%.%.....%.;" +
                    ".%............%.%.....%.;" +
                    ".%............%.%.....%.;" +
                    ".%............%.%.....%.;" +
                    ".%............%%%.....%.;" +
                    ".%....................%.;" +
                    ".%....................%.;" +
                    ".%%%%%%%%%%%%%%%%%%%%%%.," +

                    "||||||||||||||||||||||||;" +
                    "|%~~~~~~~~~~~~~~~~~~~~%|;" +
                    "|%~~~~~~~~~~~~~~~~~~~~%|;" +
                    "|%~~~~~~~~~~~~~~~~~~~~%|;" +
                    "|%~~~~~~~~~~~~%~%~~~~~%|;" +
                    "|%~~~~~~~~~~~~%~%~~~~~%|;" +
                    "|%~~~~~~~~~~~~%~%~~~~~%|;" +
                    "|%~~~~~~~~~~~~%~%~~~~~%|;" +
                    "|%~~~~~~~~~~~~%%%~~~~~%|;" +
                    "|%~~~~~~~~~~~~~~~~~~~~%|;" +
                    "|%~~~~~~~~~~~~~~~~~~~~%|;" +
                    "|%%%%%%%%%%%%%%%%%%%%%%|"
    })
    void fill(String mapString, String expectedString) {
        char[][] expectedMatrix = toCharMatrix(expectedString);
        char[][] actual = reachableTiles.fillWithWater(toCharMatrix(mapString));
        assertTrue(Arrays.deepEquals(expectedMatrix, actual));
    }
}