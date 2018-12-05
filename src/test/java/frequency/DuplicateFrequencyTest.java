package frequency;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DuplicateFrequencyTest {
    private DuplicateFrequency duplicateFrequency = new DuplicateFrequency();

    @ParameterizedTest
    @CsvSource({
            "+1 -1, 0",
            "+3 +3 +4 -2 -4, 10",
            "-6 +3 +8 +5 -6, 5",
            "+7 +7 -2 -7 -4, 14"
    })
    void testFirstDuplicateFrequency(String frequencies, int expected) {
        int actual = duplicateFrequency.firstDuplicateFrequency(Arrays.asList(frequencies.split(" ")));
        assertEquals(expected, actual);
    }
}