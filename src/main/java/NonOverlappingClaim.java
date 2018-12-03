import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NonOverlappingClaim {
    private static final Pattern CLAIM = Pattern.compile("#(.*) @ ([0-9]+),([0-9]+): ([0-9]+)x([0-9]+)");

    public static void main(String[] args) {
        NonOverlappingClaim overlappingClaims = new NonOverlappingClaim();
        String fileName = "./src/main/resources/day3_fabric_claims.txt";
        try {
            String nonOverlap = overlappingClaims.nonOverlappingClaim(Files.readAllLines(Paths.get(fileName)));
            System.out.println(nonOverlap);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    String nonOverlappingClaim(List<String> claims) {
        int[][] fabric = stakeClaims(claims);

        for (String claim : claims) {
            Matcher matcher = match(claim);
            String id = matcher.group(1);
            int x1 = Integer.parseInt(matcher.group(2));
            int y1 = Integer.parseInt(matcher.group(3));
            int x2 = Integer.parseInt(matcher.group(4));
            int y2 = Integer.parseInt(matcher.group(5));

            boolean overlap = false;

            for (int x = x1; x < x1 + x2; x++) {
                for (int y = y1; y < y1 + y2; y++) {
                    if (fabric[x][y] > 1) {
                        overlap = true;
                    }
                }
            }
            if (!overlap) {
                return id;
            }
        }

        return "";
    }

    private int[][] stakeClaims(List<String> claims) {
        int[][] fabric = new int[1000][1000];
        for (String claim : claims) {
            Matcher matcher = match(claim);
            int x1 = Integer.parseInt(matcher.group(2));
            int y1 = Integer.parseInt(matcher.group(3));
            int x2 = Integer.parseInt(matcher.group(4));
            int y2 = Integer.parseInt(matcher.group(5));

            for (int x = x1; x < x1 + x2; x++) {
                for (int y = y1; y < y1 + y2; y++) {
                    fabric[x][y]++;
                }
            }
        }
        return fabric;
    }

    private Matcher match(String claim) {
        Matcher matcher = CLAIM.matcher(claim);
        if (!matcher.matches()) {
            throw new RuntimeException(String.format("Claim %s does not match pattern!", claim));
        }
        return matcher;
    }
}
