package constellations;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ConstellationsTest {
    private Constellations constellations = new Constellations();

    @ParameterizedTest
    @CsvSource({
            "0;0;0;0%" +
            "3;0;0;0%" +
            "0;3;0;0%" +
            "0;0;3;0%" +
            "0;0;0;3%" +
            "0;0;0;6%" +
            "9;0;0;0%" +
            "12;0;0;0,2",
            "-1;2;2;0%" +
            "0;0;2;-2%" +
            "0;0;0;-2%" +
            "-1;2;0;0%" +
            "-2;-2;-2;2%" +
            "3;0;2;-1%" +
            "-1;3;2;2%" +
            "-1;0;-1;0%" +
            "0;2;1;-2%" +
            "3;0;0;0,4",
            "1;-1;0;1%" +
            "2;0;-1;0%" +
            "3;2;-1;0%" +
            "0;0;3;1%" +
            "0;0;-1;-1%" +
            "2;3;-2;0%" +
            "-2;2;0;0%" +
            "2;-2;0;-1%" +
            "1;-1;0;-1%" +
            "3;2;0;2,3",
            "1;-1;-1;-2%" +
            "-2;-2;0;1%" +
            "0;2;1;3%" +
            "-2;3;-2;1%" +
            "0;2;3;-2%" +
            "-1;-1;1;-2%" +
            "0;-2;-1;0%" +
            "-2;2;3;-1%" +
            "1;2;2;0%" +
            "-1;-2;0;-2,8"
    })
    void numberOfConstellations(String points, int expected) {
        Stream<String> pointStream = Stream.of(points.split("%"))
                .map(point -> point.replace(";", ","));
        int actual = constellations.numberOfConstellations(pointStream);
        assertEquals(expected, actual);
    }
}