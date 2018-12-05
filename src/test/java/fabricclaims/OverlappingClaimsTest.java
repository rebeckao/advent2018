package fabricclaims;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class OverlappingClaimsTest {
    private OverlappingClaims overlappingClaims = new OverlappingClaims();

    @Test
    void overlappingSquareInches() {
        List<String> claims = List.of(
                "#1 @ 1,3: 4x4",
                "#2 @ 3,1: 4x4",
                "#3 @ 5,5: 2x2"
        );
        int actual = overlappingClaims.overlappingSquareInches(claims);
        assertEquals(4, actual);
    }
}