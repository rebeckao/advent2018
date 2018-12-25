package teleportation;

import org.junit.jupiter.api.Test;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class NanobotTeleportationTest {
    private NanobotTeleportation nanobotTeleportation = new NanobotTeleportation();

    @Test
    void botsInRangeOfStrongestNanobot() {
        int actual = (int) nanobotTeleportation.botsInRangeOfStrongestNanobot(Stream.of(
                "pos=<0,0,0>, r=4",
                "pos=<1,0,0>, r=1",
                "pos=<4,0,0>, r=3",
                "pos=<0,2,0>, r=1",
                "pos=<0,5,0>, r=3",
                "pos=<0,0,3>, r=1",
                "pos=<1,1,1>, r=1",
                "pos=<1,1,2>, r=1",
                "pos=<1,3,1>, r=1"
        ));
        assertEquals(7, actual);
    }
}