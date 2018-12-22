package cave;

import common.Util;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CaveTest {
    private Cave cave = new Cave();

    @Test
    void riskLevel() {
        int actual = cave.riskLevel(510, 10, 10);
        assertEquals(114, actual);
    }

    @Test
    void regions() {
        int[][] erosionMap = cave.buildErosionMap(510, 10, 10, 11, 11);
        char[][] actual = cave.buildRegionCharMap(cave.buildRegionMap(erosionMap));
        char[][] expected = Util.toCharMatrix(List.of(
                ".=.|=.|.|=.",
                ".|=|=|||..|",
                ".==|....||=",
                "=.|....|.==",
                "=|..==...=.",
                "=||.=.=||=|",
                "|.=.===|||.",
                "|..==||=.|=",
                ".=..===..=|",
                ".======|||=",
                ".===|=|===."
        ));
        Util.display(actual);
        assertTrue(Arrays.deepEquals(expected, actual));
    }

    @Test
    void regionsLarger() {
        int[][] erosionMap = cave.buildErosionMap(510, 10, 10, 16, 16);
        char[][] actual = cave.buildRegionCharMap(cave.buildRegionMap(erosionMap));
        String expected =
                ".=.|=.|.|=.|=|=.\n" +
                ".|=|=|||..|.=...\n" +
                ".==|....||=..|==\n" +
                "=.|....|.==.|==.\n" +
                "=|..==...=.|==..\n" +
                "=||.=.=||=|=..|=\n" +
                "|.=.===|||..=..|\n" +
                "|..==||=.|==|===\n" +
                ".=..===..=|.|||.\n" +
                ".======|||=|=.|=\n" +
                ".===|=|===.===||\n" +
                "=|||...|==..|=.|\n" +
                "=.=|=.=..=.||==|\n" +
                "||=|=...|==.=|==\n" +
                "|=.=||===.|||===\n" +
                "||.|==.|.|.||=||"
        ;
        Util.display(actual);
        String actualString = Arrays.stream(actual)
                .map(String::valueOf)
                .collect(Collectors.joining("\n"));
        assertEquals(expected, actualString);
    }
}