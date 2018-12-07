package sleigh;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

class ParallellizedSteps {
    private static final int CHARACTER_OFFSET = 64;
    private InstructionOrder instructionOrder = new InstructionOrder();

    public static void main(String[] args) {
        try (Stream<String> stream = Files.lines(Paths.get("./src/main/resources/day7_instructions.txt"))) {
            System.out.println(new ParallellizedSteps().totalAssemblyTime(stream, 5, 60));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    int totalAssemblyTime(Stream<String> instructions, int numberOfWorkers, int baseTimePerStep) {
        Map<String, Step> steps = instructionOrder.parseSteps(instructions);
        List<String> completedSteps = new ArrayList<>();

        List<Worker> workers = IntStream.range(0, numberOfWorkers)
                .mapToObj(i -> new Worker())
                .collect(toList());

        int assemblyTime = 0;

        while (!steps.isEmpty() || stillWorking(workers)) {
            workers.forEach(Worker::work);
            for (Worker worker : workers) {
                if (!worker.isWorking()) {
                    Optional.ofNullable(worker.getStep())
                            .ifPresent(completedStep -> {
                                completedSteps.add(completedStep);
                                worker.assignBreak();
                            });
                    instructionOrder.nextStep(steps, completedSteps)
                            .ifPresent(nextStep -> {
                                worker.assignWork(nextStep, resolveTimeRequired(nextStep, baseTimePerStep));
                                steps.remove(nextStep);
                            });
                }
            }
            assemblyTime++;
        }

        return assemblyTime - 1;
    }

    int resolveTimeRequired(String step, int baseTimePerStep) {
        return step.charAt(0) - CHARACTER_OFFSET + baseTimePerStep;
    }

    private boolean stillWorking(List<Worker> workers) {
        return workers.stream().anyMatch(Worker::isWorking);
    }
}
