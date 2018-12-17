package programming;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

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

    int behavesLikeNumberOfOpcodes(String sampleString) {
        Sample sample = new Sample(sampleString);
        return matchingOpcodes(sample).size();
    }

    Map<Integer, Opcode> resolveOpcodes(List<String> sampleLines) {
        List<Sample> samples = parseSamples(sampleLines);
        Map<Integer, Set<Opcode>> possibleOpcodes = resolvePossibleOpcodes(samples);
        Map<Integer, Opcode> mappedOpcodes = new HashMap<>();
        while (!possibleOpcodes.isEmpty()) {
            Map<Integer, Opcode> newlyMapped = possibleOpcodes.entrySet().stream()
                    .filter(entry -> entry.getValue().size() == 1)
                    .collect(Collectors.toMap(Map.Entry::getKey, entry -> new ArrayList<>(entry.getValue()).get(0)));
            mappedOpcodes.putAll(newlyMapped);
            newlyMapped.keySet().forEach(possibleOpcodes::remove);
            newlyMapped.values().forEach(opcode -> possibleOpcodes.values()
                    .forEach(possible -> possible.remove(opcode))
            );
        }
//        mappedOpcodes.forEach((key, value) -> System.out.println("Final mapping: Opcode " + key + " has opcode " + value));
        return mappedOpcodes;
    }

    private int numberThatBehavesLikeThreeOrMoreOpcodes(List<String> sampleLines) {
        List<Sample> samples = parseSamples(sampleLines);
        return (int) samples.stream()
                .map(this::matchingOpcodes)
                .mapToInt(Set::size)
                .filter(size -> size >= 3)
                .count();
    }

    private Set<Opcode> matchingOpcodes(Sample sample) {
        Set<Opcode> opcodesMatching = new HashSet<>();
        for (Opcode opcode : OpcodeSamples.OPCODES) {
            int computedC = opcode.compute(sample.getA(), sample.getB(), sample.getRegistryBefore());
            if (computedC == sample.getRegistryAfter()[sample.getC()]) {
                opcodesMatching.add(opcode);
            }
        }
        return opcodesMatching;
    }

    private Map<Integer, Set<Opcode>> resolvePossibleOpcodes(List<Sample> samples) {
        Map<Integer, Set<Opcode>> possibleOpcodes = new HashMap<>();
        BiFunction<Set<Opcode>, Set<Opcode>, Set<Opcode>> intersection = (Set<Opcode> existingSet, Set<Opcode> newSet) -> {
            existingSet.retainAll(newSet);
            return existingSet;
        };
        for (Sample sample : samples) {
            Set<Opcode> matching = matchingOpcodes(sample);
            possibleOpcodes.merge(sample.getOpcodeNumber(), matching, intersection);
        }
        return possibleOpcodes;
    }

    private List<Sample> parseSamples(List<String> sampleLines) {
        List<Sample> samples = new ArrayList<>();
        for (int i = 0; i < sampleLines.size(); i += 4) {
            String sampleString = sampleLines.get(i) + sampleLines.get(i + 1) + sampleLines.get(i + 2);
            Sample sample = new Sample(sampleString);
            samples.add(sample);
        }
        return samples;
    }
}
