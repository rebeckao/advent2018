package elfcode;

import programming.Opcode;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

class ElfCode {
    private static final Pattern INSTRUCTION = Pattern.compile("([a-z]+) (\\d+) (\\d+) (\\d+)");
    private static final Map<String, Opcode> OPCODES = Map.ofEntries(
            Map.entry("addr", (a, b, registry) -> registry[a] + registry[b]),
            Map.entry("addi", (a, b, registry) -> registry[a] + b),
            Map.entry("mulr", (a, b, registry) -> registry[a] * registry[b]),
            Map.entry("muli", (a, b, registry) -> registry[a] * b),
            Map.entry("banr", (a, b, registry) -> registry[a] & registry[b]),
            Map.entry("bani", (a, b, registry) -> registry[a] & b),
            Map.entry("borr", (a, b, registry) -> registry[a] | registry[b]),
            Map.entry("bori", (a, b, registry) -> registry[a] | b),
            Map.entry("setr", (a, b, registry) -> registry[a]),
            Map.entry("seti", (a, b, registry) -> a),
            Map.entry("gtir", (a, b, registry) -> a > registry[b] ? 1 : 0),
            Map.entry("gtri", (a, b, registry) -> registry[a] > b ? 1 : 0),
            Map.entry("gtrr", (a, b, registry) -> registry[a] > registry[b] ? 1 : 0),
            Map.entry("eqir", (a, b, registry) -> a == registry[b] ? 1 : 0),
            Map.entry("eqri", (a, b, registry) -> registry[a] == b ? 1 : 0),
            Map.entry("eqrr", (a, b, registry) -> registry[a] == registry[b] ? 1 : 0)
    );

    public static void main(String[] args) {
        try {
            List<String> instructions = Files.readAllLines(Paths.get("./src/main/resources/day19_programming.txt"));
            System.out.println(new ElfCode().valueInRegistry0AfterProgram(instructions, 0));
            System.out.println(new ElfCode().valueInRegistry0AfterProgram(instructions, 1));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    int valueInRegistry0AfterProgram(List<String> lines, int initialValueOfRegistry0) {
        int pointerRegister = Integer.parseInt(lines.get(0).substring(4));
        List<Instruction> instructions = lines.stream()
                .map(INSTRUCTION::matcher)
                .filter(Matcher::matches)
                .map(matcher -> Instruction.builder()
                        .opcode(OPCODES.get(matcher.group(1)))
                        .a(Integer.parseInt(matcher.group(2)))
                        .b(Integer.parseInt(matcher.group(3)))
                        .c(Integer.parseInt(matcher.group(4)))
                        .build()
                ).collect(Collectors.toList());
        int[] registers = new int[6];
        registers[0] = initialValueOfRegistry0;
        int pointerValue = registers[pointerRegister];

        while (true) {
            registers[pointerRegister] = pointerValue;
            Instruction instruction = instructions.get(pointerValue);
//            System.out.println(String.format("ip=%d %s %d %d %d",
//                    registers[pointerRegister],
//                    Arrays.toString(registers),
//                    instruction.getA(),
//                    instruction.getB(),
//                    instruction.getC()
//            ));
            registers[instruction.getC()] = instruction.getOpcode().compute(
                    instruction.getA(),
                    instruction.getB(),
                    registers
            );
//            System.out.println("Registers after: " + Arrays.toString(registers));
            pointerValue = registers[pointerRegister];
            pointerValue++;
            if (pointerValue >= instructions.size()) {
                return registers[0];
            }
        }
    }
}
