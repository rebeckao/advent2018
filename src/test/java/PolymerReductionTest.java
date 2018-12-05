import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PolymerReductionTest {
    private PolymerReduction polymerReduction = new PolymerReduction();

    @Test
    void reducedPolymerLength() {
        int length = polymerReduction.reducedPolymerLength("dabAcCaCBAcCcaDA");
        assertEquals(10, length);
    }
    // dabAcCaCBAcCcaDA
    // dabAaCBAcaDA
    // dabCBAcaDA
}