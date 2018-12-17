package programming;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

class OpcodeSamplesTest {
    private OpcodeSamples opcodeSamples = new OpcodeSamples();

    @ParameterizedTest
    @CsvSource(value = {
            "Before: [3; 2; 1; 1]" +
                    "9 2 1 2" +
                    "After:  [3; 2; 2; 1]," +
                    "3",
    })
    void behavesLikeNumberOfOpcodes(String sample, int expected) {
        int actual = opcodeSamples.behavesLikeNumberOfOpcodes(sample.replace(';', ','));
        assertEquals(expected, actual);
    }
}