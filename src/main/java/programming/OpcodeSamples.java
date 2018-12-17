package programming;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

class OpcodeSamples {
    private static final List<Opcode> OPCODES = List.of(
            (a, b, registry) -> registry[a] + registry[b],
            (a, b, registry) -> registry[a] + b,
            (a, b, registry) -> registry[a] * registry[b],
            (a, b, registry) -> registry[a] * b,
            (a, b, registry) -> registry[a] & registry[b],
            (a, b, registry) -> registry[a] & b,
            (a, b, registry) -> registry[a] | registry[b],
            (a, b, registry) -> registry[a] | b,
            (a, b, registry) -> registry[a],
            (a, b, registry) -> a,
            (a, b, registry) -> a > registry[b] ? 1 : 0,
            (a, b, registry) -> registry[a] > b ? 1 : 0,
            (a, b, registry) -> registry[a] > registry[b] ? 1 : 0,
            (a, b, registry) -> a == registry[b] ? 1 : 0,
            (a, b, registry) -> registry[a] == b ? 1 : 0,
            (a, b, registry) -> registry[a] == registry[b] ? 1 : 0
    );

    public static void main(String[] args) {
        try {
            List<String> lines = Files.readAllLines(Paths.get("./src/main/resources/day16_pt1_programming.txt"));
            System.out.println(new OpcodeSamples().numberThatBehavesLikeThreeOrMoreOpcodes(lines));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private int numberThatBehavesLikeThreeOrMoreOpcodes(List<String> sampleLines) {
        int likeThreeOrMore = 0;
        for (int i = 0; i < sampleLines.size(); i += 4) {
            String sample = sampleLines.get(i) + sampleLines.get(i + 1) + sampleLines.get(i + 2);
            int numberOfOpcodes = behavesLikeNumberOfOpcodes(sample);
            System.out.println("Sample: " + sample + " behaves like " + numberOfOpcodes + " opcodes");
            if (numberOfOpcodes >= 3) {
                likeThreeOrMore++;
            }
        }
        return likeThreeOrMore;
    }

    int behavesLikeNumberOfOpcodes(String sampleString) {
        Sample sample = new Sample(sampleString);
        int opcodesMatching = 0;
        for (Opcode opcode : OPCODES) {
            int computedC = opcode.compute(sample.getA(), sample.getB(), sample.getRegistryBefore());
            if (computedC == sample.getRegistryAfter()[sample.getC()]) {
                opcodesMatching++;
            }
        }
        return opcodesMatching;
    }
}
