import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SimilarBoxIdsTest {
    private SimilarBoxIds similarBoxIds = new SimilarBoxIds();

    @Test
    void commonIdPart() {
        String actual = similarBoxIds.commonIdPart(List.of(
                "abcde",
                "fghij",
                "klmno",
                "pqrst",
                "fguij",
                "axcye",
                "wvxyz"
        ));
        assertEquals("fgij", actual);
    }
}