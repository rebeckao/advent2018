import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

class PolymerReductionWithElimination {
    private PolymerReduction polymerReduction = new PolymerReduction();

    public static void main(String[] args) {
        PolymerReductionWithElimination polymerReduction = new PolymerReductionWithElimination();
        String fileName = "./src/main/resources/day5_polymers.txt";
        try {
            int firstDuplicate = polymerReduction.mostReducedPolymerLength(Files.readAllLines(Paths.get(fileName)).get(0));
            System.out.println(firstDuplicate);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    int mostReducedPolymerLength(String polymer) {
        return polymer.toLowerCase().chars()
                .distinct()
                .mapToObj(c -> (char) c)
                .map(c -> removeCharacter(polymer, c))
                .map(refinedPolymer -> polymerReduction.reducedPolymerLength(refinedPolymer))
                .mapToInt(Integer::intValue)
                .min()
                .orElseThrow(() -> new RuntimeException("No min value found!"));
    }

    private String removeCharacter(String string, Character c) {
        return string
                .replace(c.toString().toUpperCase(), "")
                .replace(c.toString().toLowerCase(), "");
    }

}
