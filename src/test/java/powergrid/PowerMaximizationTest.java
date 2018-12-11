package powergrid;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PowerMaximizationTest {
    private PowerMaximization powerMaximization = new PowerMaximization();

    @ParameterizedTest
    @CsvSource({
            "18, 33, 45",
            "42, 21, 61",
    })
    void mostHighPoweredSubGrid(int serialNumber, int expectedX, int expectedY) {
        int[][] grid = powerMaximization.constructGrid(serialNumber);
        String actual = powerMaximization.mostHighPoweredSubGridOfSize3(grid);
        assertEquals(expectedX + "," + expectedY, actual);
    }

    @ParameterizedTest
    @CsvSource({
            "18, 90, 269, 16",
            "42, 232, 251, 12",
    })
    void mostHighPoweredSubGridOfAnySize(int serialNumber, int expectedX, int expectedY, int expectedSize) {
        int[][] grid = powerMaximization.constructGrid(serialNumber);
        String actual = powerMaximization.mostHighPoweredSubGridOfAnySize(grid);
        assertEquals(expectedX + "," + expectedY + "," + expectedSize, actual);
    }

    @ParameterizedTest
    @CsvSource({
            "18, 90, 269, 16, 113",
            "42, 232, 251, 12, 119",
    })
    void powerLevelForSubGrid(int serialNumber, int x, int y, int size, int expected) {
        int[][] grid = powerMaximization.constructGrid(serialNumber);
        int actual = powerMaximization.powerLevelForSubGrid(grid, size, x, y);
        assertEquals(expected, actual);
    }

    @ParameterizedTest
    @CsvSource({
            "3, 5, 8, 4",
            "122, 79, 57, -5",
            "217, 196, 39, 0",
            "101, 153, 71, 4"
    })
    void powerLevelOfCell(int x, int y, int serialNumber, int expected) {
        int actual = powerMaximization.powerLevelOfCell(x, y, serialNumber);
        assertEquals(expected, actual);
    }
}