package sleigh;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class ParallellizedStepsTest {
    private ParallellizedSteps parallellizedSteps = new ParallellizedSteps();

    @Test
    void totalAssemblyTime() {
        int actual = parallellizedSteps.totalAssemblyTime(Stream.of(
                "Step C must be finished before step A can begin.",
                "Step C must be finished before step F can begin.",
                "Step A must be finished before step B can begin.",
                "Step A must be finished before step D can begin.",
                "Step B must be finished before step E can begin.",
                "Step D must be finished before step E can begin.",
                "Step F must be finished before step E can begin."
        ), 2, 0);
        assertEquals(15, actual);
    }

    @ParameterizedTest
    @CsvSource({
            "A, 0, 1",
            "Z, 0, 26",
            "A, 60, 61",
            "Z, 60, 86",
    })
    void resolveTimeRequired(String step, int baseTimePerStep, int expected) {
        int actual = parallellizedSteps.resolveTimeRequired(step, baseTimePerStep);
        assertEquals(expected, actual);
    }
}