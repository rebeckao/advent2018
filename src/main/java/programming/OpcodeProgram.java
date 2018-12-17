package programming;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class OpcodeProgram {
    private static final Pattern INSTRUCTION = Pattern.compile("(\\d+) ([0-3]+) ([0-3]+) ([0-3]+)");
    private OpcodeSamples opcodeSamples = new OpcodeSamples();

    public static void main(String[] args) {
        try {
            List<String> samples = Files.readAllLines(Paths.get("./src/main/resources/day16_pt1_programming.txt"));
            List<String> instructions = Files.readAllLines(Paths.get("./src/main/resources/day16_pt2_programming.txt"));
            System.out.println(new OpcodeProgram().valueInRegistry0AfterProgram(samples, instructions));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private int valueInRegistry0AfterProgram(List<String> sampleLines, List<String> programLines) {
        Map<Integer, Opcode> opcodes = opcodeSamples.resolveOpcodes(sampleLines);

        int[] registry = new int[4];
        for (String programLine : programLines) {
            Matcher matcher = INSTRUCTION.matcher(programLine);
            if (!matcher.matches()) {
                throw  new IllegalArgumentException("Does not compute: " + programLine);
            }
            int opcodeNumber = Integer.parseInt(matcher.group(1));
            int a = Integer.parseInt(matcher.group(2));
            int b = Integer.parseInt(matcher.group(3));
            int c = Integer.parseInt(matcher.group(4));
            registry[c] = opcodes.get(opcodeNumber).compute(a, b, registry);
//            System.out.println("After {" + programLine + "}: " + Arrays.toString(registry));
        }

        return registry[0];
    }

}
