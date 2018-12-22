package facility;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ElfFacilityTest {
    private ElfFacility elfFacility = new ElfFacility();

    @ParameterizedTest
    @CsvSource({
            "^WNE$, 3",
            "^ENWWW(NEEE|SSE(EE|N))$, 10",
            "^ENNWSWW(NEWS|)SSSEEN(WNSE|)EE(SWEN|)NNN$, 18",
            "^WW(W|NNNWSSS)$, 6"
    })
    void numberOfDoorsToFarthestRoom(String route, int expected) {
        int actual = elfFacility.numberOfDoorsToFarthestRoom(route);
        assertEquals(expected, actual);
    }
}