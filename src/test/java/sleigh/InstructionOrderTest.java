package sleigh;

import org.junit.jupiter.api.Test;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class InstructionOrderTest {
    private InstructionOrder instructionOrder = new InstructionOrder();

    @Test
    void correctOrder() {
        String actual = instructionOrder.correctOrder(Stream.of(
                "Step C must be finished before step A can begin.",
                "Step C must be finished before step F can begin.",
                "Step A must be finished before step B can begin.",
                "Step A must be finished before step D can begin.",
                "Step B must be finished before step E can begin.",
                "Step D must be finished before step E can begin.",
                "Step F must be finished before step E can begin."
        ));
        assertEquals("CABDFE", actual);
    }
}