import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class OverlappingClaims {
    private static final Pattern CLAIM = Pattern.compile("#.* @ ([0-9]+),([0-9]+): ([0-9]+)x([0-9]+)");

    public static void main(String[] args) {
        OverlappingClaims overlappingClaims = new OverlappingClaims();
        String fileName = "./src/main/resources/day3_fabric_claims.txt";
        try {
            int overlaps = overlappingClaims.overlappingSquareInches(Files.readAllLines(Paths.get(fileName)));
            System.out.println(overlaps);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    int overlappingSquareInches(List<String> claims) {
        int[][] fabric = new int[1000][1000];
        int numberOfOverlaps = 0;
        for (String claim : claims) {
            Matcher matcher = CLAIM.matcher(claim);
            if (matcher.matches()) {
                int x1 = Integer.parseInt(matcher.group(1));
                int y1 = Integer.parseInt(matcher.group(2));
                int x2 = Integer.parseInt(matcher.group(3));
                int y2 = Integer.parseInt(matcher.group(4));

                for (int x = x1; x < x1 + x2; x++) {
                    for (int y = y1; y < y1 + y2; y++) {
                        fabric[x][y]++;
                        if (fabric[x][y] == 2) {
                            numberOfOverlaps++;
                        }
                    }
                }
            } else {
                throw new RuntimeException(String.format("Claim %s does not match pattern!", claim));
            }
        }

        return numberOfOverlaps;
    }
}
