package coordinates;

import org.junit.jupiter.api.Test;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class SafeZoneTest {
    private SafeZone safeZone = new SafeZone();

    @Test
    void sizeOfSafeArea() {
        int actual = safeZone.sizeOfSafeArea(Stream.of(
                "1, 1",
                "1, 6",
                "8, 3",
                "3, 4",
                "5, 5",
                "8, 9"
        ), 32);
        assertEquals(16, actual);
    }
}