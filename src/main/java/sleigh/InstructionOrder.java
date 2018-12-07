package sleigh;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

class InstructionOrder {
    private static final Pattern INSTRUCTION = Pattern.compile("Step ([A-Z]) must be finished before step ([A-Z]) can begin\\.");

    public static void main(String[] args) {
        InstructionOrder instructionOrder = new InstructionOrder();
        String fileName = "./src/main/resources/day7_instructions.txt";

        try (Stream<String> stream = Files.lines(Paths.get(fileName))) {
            String correctOrder = instructionOrder.correctOrder(stream);
            System.out.println(correctOrder);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    String correctOrder(Stream<String> instructions) {
        Map<String, Step> steps = parseSteps(instructions);

        List<String> orderedSteps = new ArrayList<>();

        while (!steps.isEmpty()) {
            String nextStep = nextStep(steps, orderedSteps)
                    .orElseThrow(() -> new IllegalStateException("No step"));
            orderedSteps.add(nextStep);
            steps.remove(nextStep);
        }

        return String.join("", orderedSteps);
    }

    Map<String, Step> parseSteps(Stream<String> instructions) {
        Map<String, Step> steps = new HashMap<>();

        instructions.map(INSTRUCTION::matcher)
                .filter(Matcher::matches)
                .forEach(matcher -> {
                    String prerequisiteLabel = matcher.group(1);
                    String label = matcher.group(2);
                    steps.putIfAbsent(prerequisiteLabel, new Step(prerequisiteLabel));
                    steps.putIfAbsent(label, new Step(label));
                    steps.get(label).addDependency(prerequisiteLabel);
                });
        return steps;
    }

    Optional<String> nextStep(Map<String, Step> steps, List<String> alreadyCompletedSteps) {
        return steps.values().stream()
                .filter(step -> alreadyCompletedSteps.containsAll(step.getDependsOn()))
                .map(Step::getLabel)
                .sorted()
                .findFirst();
    }
}
