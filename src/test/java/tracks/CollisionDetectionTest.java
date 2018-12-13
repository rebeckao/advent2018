package tracks;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.List;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;
import static org.junit.jupiter.api.Assertions.assertEquals;

class CollisionDetectionTest {
    private CollisionDetection collisionDetection = new CollisionDetection();

    @ParameterizedTest
    @CsvSource({
            "|;" +
                    "v;" +
                    "|;" +
                    "|;" +
                    "|;" +
                    "^;" +
                    "|," +
                    "0,3",
            "/->-\\        ;" +
                    "|   |  /----\\;" +
                    "| /-+--+-\\  |;" +
                    "| | |  | v  |;" +
                    "\\-+-/  \\-+--/;" +
                    "  \\------/   ;," +
                    "7,3"
    })
    void coordinatesOfFirstCollision(String initialTracks, int expectedX, int expectedY) {
        List<String> split = Stream.of(initialTracks.split(";")).filter(s -> !s.equals("")).collect(toList());
        String actual = collisionDetection.coordinatesOfFirstCollision(split);
        assertEquals(expectedX + "," + expectedY, actual);
    }
}