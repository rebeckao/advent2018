import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

class FrequencyCalibration {

    public static void main(String[] args) {
        FrequencyCalibration frequencyCalibration = new FrequencyCalibration();
        String fileName = "./src/main/resources/day1_frequencies.txt";

        try (Stream<String> stream = Files.lines(Paths.get(fileName))) {
            int calibratedFrequency = frequencyCalibration.calibrateFrequency(stream);
            System.out.println(calibratedFrequency);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    int calibrateFrequency(Stream<String> frequencies) {
        return frequencies
                .mapToInt(Integer::parseInt)
                .sum();
    }
}
