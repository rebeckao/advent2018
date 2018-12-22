package cave;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CaveRescueTest {

    @Test
    void fastestRescueTime() {
        CaveRescue caveRescue = new CaveRescue(510, 10, 10);
        int actual = caveRescue.fastestRescueTime();
        assertEquals(45, actual);
    }
}