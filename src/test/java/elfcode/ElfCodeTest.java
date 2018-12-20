package elfcode;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ElfCodeTest {
    private ElfCode elfCode = new ElfCode();

    @Test
    void registerValueAfterExecution() {
        int actual = elfCode.valueInRegistry0AfterProgram(List.of(
                "#ip 0",
                "seti 5 0 1",
                "seti 6 0 2",
                "addi 0 1 0",
                "addr 1 2 3",
                "setr 1 0 0",
                "seti 8 0 4",
                "seti 9 0 5"
        ));
        assertEquals(6, actual);
    }
}