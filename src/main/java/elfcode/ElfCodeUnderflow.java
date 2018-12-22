package elfcode;

import lombok.Builder;
import lombok.Getter;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

class ElfCodeUnderflow {
    private static final Pattern INSTRUCTION = Pattern.compile("([a-z]+) (\\d+) (\\d+) (\\d+)");
    private static final Map<String, Opcode> opcodes = Map.ofEntries(
            Map.entry("addr", (a, b, registry) -> registry[(int) a] + registry[(int) b]),
            Map.entry("addi", (a, b, registry) -> registry[(int) a] + b),
            Map.entry("mulr", (a, b, registry) -> registry[(int) a] * registry[(int) b]),
            Map.entry("muli", (a, b, registry) -> registry[(int) a] * b),
            Map.entry("banr", (a, b, registry) -> registry[(int) a] & registry[(int) b]),
            Map.entry("bani", (a, b, registry) -> registry[(int) a] & b),
            Map.entry("borr", (a, b, registry) -> registry[(int) a] | registry[(int) b]),
            Map.entry("bori", (a, b, registry) -> registry[(int) a] | b),
            Map.entry("setr", (a, b, registry) -> registry[(int) a]),
            Map.entry("seti", (a, b, registry) -> a),
            Map.entry("gtir", (a, b, registry) -> a > registry[(int) b] ? 1 : 0),
            Map.entry("gtri", (a, b, registry) -> registry[(int) a] > b ? 1 : 0),
            Map.entry("gtrr", (a, b, registry) -> registry[(int) a] > registry[(int) b] ? 1 : 0),
            Map.entry("eqir", (a, b, registry) -> a == registry[(int) b] ? 1 : 0),
            Map.entry("eqri", (a, b, registry) -> registry[(int) a] == b ? 1 : 0),
            Map.entry("eqrr", (a, b, registry) -> registry[(int) a] == registry[(int) b] ? 1 : 0)
    );

    interface Opcode {
        long compute(long a, long b, long[] registry);
    }

    public static void main(String[] args) {
        try {
            List<String> instructions = Files.readAllLines(Paths.get("./src/main/resources/day21_programming.txt"));
            System.out.println(new ElfCodeUnderflow().lowestRegister0ValueToMakeProgramHaltFastest(instructions));
            System.out.println(new ElfCodeUnderflow().lowestRegister0ValueToMakeProgramHaltSlowest(instructions));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private long lowestRegister0ValueToMakeProgramHaltFastest(List<String> lines) {
        int pointerRegister = Integer.parseInt(lines.get(0).substring(4));
        List<Instruction> instructions = parseInstructions(lines);
        long[] registers = new long[6];
        return completeFastest(pointerRegister, instructions, registers);
    }

    private long lowestRegister0ValueToMakeProgramHaltSlowest(List<String> lines) {
        int pointerRegister = Integer.parseInt(lines.get(0).substring(4));
        List<Instruction> instructions = parseInstructions(lines);
        long[] registers = new long[6];
        return completeSlowest(pointerRegister, instructions, registers);
    }

    private List<Instruction> parseInstructions(List<String> lines) {
        return lines.stream()
                .map(INSTRUCTION::matcher)
                .filter(Matcher::matches)
                .map(matcher -> Instruction.builder()
                        .opcodeName(matcher.group(1))
                        .opcode(opcodes.get(matcher.group(1)))
                        .a(Integer.parseInt(matcher.group(2)))
                        .b(Integer.parseInt(matcher.group(3)))
                        .c(Integer.parseInt(matcher.group(4)))
                        .build()
                ).collect(Collectors.toList());
    }

    private long completeFastest(int pointerRegister, List<Instruction> instructions, long[] registers) {
        int pointerValue = (int) registers[pointerRegister];
        int numberOfInstructions = 0;
        while (true) {
            registers[pointerRegister] = pointerValue;
            if (pointerValue == 28) {
                return registers[5];
            }
            Instruction instruction = instructions.get(pointerValue);
            long computed = instruction.getOpcode().compute(
                    instruction.getA(),
                    instruction.getB(),
                    registers
            );
            registers[(int) instruction.getC()] = computed;
            pointerValue = (int) registers[pointerRegister];
            numberOfInstructions++;
            pointerValue++;
            if (pointerValue >= instructions.size()) {
                return numberOfInstructions;
            }
        }
    }

    private long completeSlowest(int pointerRegister, List<Instruction> instructions, long[] registers) {
        Set<Long> visitedValues = new HashSet<>();
        long lastValue = 0;
        int pointerValue = (int) registers[pointerRegister];
        int numberOfInstructions = 0;
        while (true) {
            registers[pointerRegister] = pointerValue;
            if (pointerValue == 28) {
//                System.out.println("Hit check against register 5: " + Arrays.toString(registers));
                if (!visitedValues.add(registers[5])) {
                    return lastValue;
                }
                lastValue = registers[5];
            }
            Instruction instruction = instructions.get(pointerValue);
            long computed = instruction.getOpcode().compute(
                    instruction.getA(),
                    instruction.getB(),
                    registers
            );
            registers[(int) instruction.getC()] = computed;
            pointerValue = (int) registers[pointerRegister];
            numberOfInstructions++;
            pointerValue++;
            if (pointerValue >= instructions.size()) {
                return numberOfInstructions;
            }
        }
    }

    @Builder
    @Getter
    private static class Instruction {
        private String opcodeName;
        private Opcode opcode;
        private long a;
        private long b;
        private long c;
    }
}
