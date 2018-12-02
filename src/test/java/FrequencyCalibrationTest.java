import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FrequencyCalibrationTest {
    private FrequencyCalibration frequencyCalibration = new FrequencyCalibration();

    @ParameterizedTest
    @CsvSource({
            "+1 +1 +1, 3",
            "+1 +1 -2, 0",
            "-1 -2 -3, -6"
    })
    void testFrquencyCalibration(String frequencies, int expected) {
        int actual = frequencyCalibration.calibrateFrequency(Stream.of(frequencies.split(" ")));
        assertEquals(expected, actual);
    }

}