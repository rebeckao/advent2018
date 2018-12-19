package elfcode;

import lombok.Builder;
import lombok.Getter;
import programming.Opcode;

@Builder
@Getter
class Instruction {
    private Opcode opcode;
    private int a;
    private int b;
    private int c;
}
