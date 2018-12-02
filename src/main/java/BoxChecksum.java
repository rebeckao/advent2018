import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

class BoxChecksum {

    public static void main(String[] args) {
        BoxChecksum boxChecksum = new BoxChecksum();
        String fileName = "./src/main/resources/day2_box_ids.txt";

        try (Stream<String> stream = Files.lines(Paths.get(fileName))) {
            int checksum = boxChecksum.resolveChecksum(stream);
            System.out.println(checksum);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    int resolveChecksum(Stream<String> boxIds) {
        AtomicInteger twoLetters = new AtomicInteger();
        AtomicInteger threeLetters = new AtomicInteger();

        boxIds
                .map(this::resolveLetterCounts)
                .forEach(letterCounts -> {
                            if (hasCount(letterCounts, 2)) {
                                twoLetters.incrementAndGet();
                            }
                            if (hasCount(letterCounts, 3)) {
                                threeLetters.incrementAndGet();
                            }
                        }
                );

        return twoLetters.get() * threeLetters.get();
    }

    private boolean hasCount(Collection<Integer> letterCounts, int targetCount) {
        return letterCounts.contains(targetCount);
    }

    private Collection<Integer> resolveLetterCounts(String boxId) {
        Map<Integer, Integer> letterCounts = new HashMap<>();
        boxId.chars().forEach(letter -> letterCounts.merge(letter, 1, (a, b) -> a + 1));
        return letterCounts.values();
    }
}
