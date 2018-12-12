package plants;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

class PlantGrowth {
    private static final Pattern INITIAL_PLANTS = Pattern.compile("initial state: ([.#]*)");
    private static final Pattern PATTERN = Pattern.compile("([.#]{5}) => ([.#])");
    private static final String PLANT = "#";
    private static final String EMPTY = ".";

    public static void main(String[] args) {
        String fileName = "./src/main/resources/day12_plants.txt";
        try {
            List<String> plantInfo = Files.readAllLines(Paths.get(fileName));
            String initialPlants = plantInfo.get(0);
            List<String> patterns = plantInfo.subList(2, plantInfo.size());
            System.out.println(new PlantGrowth().sumOfPlantIndicesAfterGrowth(initialPlants, patterns, 20));
            System.out.println(new PlantGrowth().sumOfPlantIndicesAfterGrowth(initialPlants, patterns, 50000000000L));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    long sumOfPlantIndicesAfterGrowth(String initialPlants, Collection<String> patterns, long generations) {
        String plants = parsePlants(initialPlants);
        Map<String, String> growthPatterns = parsePatterns(patterns);

        long offset = 0;
        for (long generation = 0; generation < generations; generation++) {
            int offsetChange = 0;
//            System.out.println(plants);

            // Pad with empty pots if plants are too close to edges of current representation
            String plantsWithBuffer;
            int firstPlant = plants.indexOf(PLANT);
            if (firstPlant < 4) {
                int requiredLeftBuffer = 4 - firstPlant;
                plantsWithBuffer = emptyPots(requiredLeftBuffer) + plants;
                offsetChange += requiredLeftBuffer;
            } else {
                plantsWithBuffer = plants;
            }
            int lastPlant = plantsWithBuffer.lastIndexOf(PLANT);
            if (lastPlant > plantsWithBuffer.length() - 4) {
                int requiredRightBuffer = 5 - (plantsWithBuffer.length() - lastPlant);
                plantsWithBuffer += emptyPots(requiredRightBuffer);
            }

            int newFirstPlant = plantsWithBuffer.indexOf(PLANT) - 2;
            offsetChange -= (4 - newFirstPlant);

            String nextPlants = growOneGeneration(growthPatterns, plantsWithBuffer, newFirstPlant, lastPlant);

            if (nextPlants.equals(plants)) {
                // Stability achieved! No need to iterate anymore
                offset = offset + offsetChange * (generations - generation);
                break;
            }
            offset += offsetChange;
            plants = nextPlants;
        }
        System.out.println("Final plants: " + plants + " (starting at index: " + -offset + ")");
        return sumIndices(plants, offset);
    }

    private String growOneGeneration(Map<String, String> growthPatterns, String plantsWithBuffer, int start, int lastPlant) {
        StringBuilder nextGeneration = new StringBuilder();
        for (int i = start; i < lastPlant + 2; i++) {
            String plantEnvironment = plantsWithBuffer.substring(i - 2, i + 3);
            String next = growthPatterns.getOrDefault(plantEnvironment, EMPTY);
            nextGeneration.append(next);
        }
        return nextGeneration.toString();
    }

    private long sumIndices(String plants, long bufferOffset) {
        long sum = 0;
        int currentIndex = plants.indexOf(PLANT);
        while (currentIndex >= 0) {
            sum += currentIndex - bufferOffset;
            currentIndex = plants.indexOf(PLANT, currentIndex + 1);
        }
        return sum;
    }

    private Map<String, String> parsePatterns(Collection<String> patterns) {
        return patterns.stream()
                .map(PATTERN::matcher)
                .filter(Matcher::matches)
                .collect(Collectors.toMap(m -> m.group(1), m -> m.group(2)));
    }

    private String emptyPots(long bufferOffset) {
        return LongStream.range(0, bufferOffset)
                .mapToObj(i -> EMPTY)
                .collect(Collectors.joining());
    }

    private String parsePlants(String initialPlants) {
        Matcher matcher = INITIAL_PLANTS.matcher(initialPlants);
        if (!matcher.matches()) {
            throw new IllegalStateException("Does not compute: " + initialPlants);
        }
        return matcher.group(1);
    }
}
