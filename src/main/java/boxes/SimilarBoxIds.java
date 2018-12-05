package boxes;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.toList;

class SimilarBoxIds {
    private static final String WILDCARD = "@";

    public static void main(String[] args) {
        SimilarBoxIds similarBoxIds = new SimilarBoxIds();
        String fileName = "./src/main/resources/day2_box_ids.txt";

        try {
            String commonIdPart = similarBoxIds.commonIdPart(Files.readAllLines(Paths.get(fileName)));
            System.out.println(commonIdPart);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    String commonIdPart(List<String> boxIds) {
        Set<String> idPatterns = new HashSet<>();
        for (String boxId : boxIds) {
            List<String> derivedIdPatterns = deriveIdPatterns(boxId);
            for (String idPattern : derivedIdPatterns) {
                boolean patternAlreadyVisited = !idPatterns.add(idPattern);
                if (patternAlreadyVisited) {
                    return idPattern.replace(WILDCARD, "");
                }
            }
        }
        return "";
    }

    private List<String> deriveIdPatterns(String boxId) {
        return IntStream.range(0, boxId.length())
                .mapToObj(i -> boxId.substring(0, i) + WILDCARD + boxId.substring(i + 1))
                .collect(toList());
    }
}
