package coordinates;

import org.junit.jupiter.api.Test;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DangerZonesTest {
    private DangerZones dangerZones = new DangerZones();

    @Test
    void sizeOfLargestArea() {
        int actual = dangerZones.sizeOfLargestArea(Stream.of(
                "1, 1",
                "1, 6",
                "8, 3",
                "3, 4",
                "5, 5",
                "8, 9"
        ));
        assertEquals(17, actual);
    }
}