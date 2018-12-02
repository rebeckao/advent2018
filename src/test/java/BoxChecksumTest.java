import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BoxChecksumTest {
    private BoxChecksum boxChecksum = new BoxChecksum();

    @Test
    void resolveChecksum() {
        List<String> boxIds = List.of(
                "abcdef",
                "bababc",
                "abbcde",
                "abcccd",
                "aabcdd",
                "abcdee",
                "ababab"
        );
        int actual = boxChecksum.resolveChecksum(boxIds.stream());
        assertEquals(12, actual);
    }
}