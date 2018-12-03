import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class NonOverlappingClaimTest {
    private NonOverlappingClaim nonOverlappingClaim = new NonOverlappingClaim();

    @Test
    void nonOverlappingClaim() {
        List<String> claims = List.of(
                "#1 @ 1,3: 4x4",
                "#2 @ 3,1: 4x4",
                "#3 @ 5,5: 2x2"
        );
        String claim = nonOverlappingClaim.nonOverlappingClaim(claims);
        assertEquals("3", claim);
    }
}