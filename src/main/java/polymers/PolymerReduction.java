package polymers;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

class PolymerReduction {
    private static final int CASE_DIFF = 32;

    public static void main(String[] args) {
        PolymerReduction polymerReduction = new PolymerReduction();
        String fileName = "./src/main/resources/day5_polymers.txt";
        try {
            int firstDuplicate = polymerReduction.reducedPolymerLength(Files.readAllLines(Paths.get(fileName)).get(0));
            System.out.println(firstDuplicate);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    int reducedPolymerLength(String polymer) {
        String maximallyReducedPolymer = reducePolymer(polymer);
        return maximallyReducedPolymer.length();
    }

    private String reducePolymer(String polymer) {
        StringBuilder reducedPolymer = new StringBuilder();
        char previousChar = polymer.charAt(0);
        boolean reductionMade = false;
        for (int i = 1; i < polymer.length(); i++) {
            char currentChar = polymer.charAt(i);
            if (Math.abs(previousChar - currentChar) == CASE_DIFF) {
                reductionMade = true;
                if (i == polymer.length() - 2) {
                    reducedPolymer.append(polymer.charAt(i + 1));
                    break;
                } else if (i == polymer.length() - 1) {
                    break;
                }
                previousChar = polymer.charAt(i + 1);
                i++;
            } else {
                reducedPolymer.append(previousChar);
                if (i == polymer.length() - 1) {
                    reducedPolymer.append(currentChar);
                }
                previousChar = currentChar;
            }
        }
        String reduced = reducedPolymer.toString();
        if (reductionMade) {
            return reducePolymer(reduced);
        } else {
            return reduced;
        }
    }
}
