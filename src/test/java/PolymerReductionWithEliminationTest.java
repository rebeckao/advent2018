import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PolymerReductionWithEliminationTest {
    private PolymerReductionWithElimination polymerReduction = new PolymerReductionWithElimination();

    @Test
    void mostReducedPolymerLength() {
        int actual = polymerReduction.mostReducedPolymerLength("dabAcCaCBAcCcaDA");
        assertEquals(4, actual);
    }
}