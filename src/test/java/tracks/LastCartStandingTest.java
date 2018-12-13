package tracks;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class LastCartStandingTest {
    private LastCartStanding lastCartStanding = new LastCartStanding();

    @Test
    void coordinatesOfLastCart() {
        String actual = lastCartStanding.coordinatesOfLastCart(List.of(
                "/>-<\\  ",
                "|   |  ",
                "| /<+-\\",
                "| | | v",
                "\\>+</ |",
                "  |   ^",
                "  \\<->/"
        ));
        assertEquals("6,4", actual);
    }
}