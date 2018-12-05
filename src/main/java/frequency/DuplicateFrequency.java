package frequency;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

class DuplicateFrequency {

    public static void main(String[] args) {
        DuplicateFrequency frequencyCalibration = new DuplicateFrequency();
        String fileName = "./src/main/resources/day1_frequencies.txt";
        try {
            int firstDuplicate = frequencyCalibration.firstDuplicateFrequency(Files.readAllLines(Paths.get(fileName)));
            System.out.println(firstDuplicate);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    int firstDuplicateFrequency(List<String> frequencyAdjustments) {
        Integer currentFrequency = 0;
        Set<Integer> visitedFrequencies = new HashSet<>();
        visitedFrequencies.add(currentFrequency);
        List<Integer> adjustments = frequencyAdjustments.stream()
                .map(Integer::parseInt)
                .collect(Collectors.toList());
        while (true) {
            for (Integer adjustment : adjustments) {
                currentFrequency += adjustment;
                boolean frequencyAlreadyVisited = !visitedFrequencies.add(currentFrequency);
                if (frequencyAlreadyVisited) {
                    return currentFrequency;
                }
            }
        }
    }
}
