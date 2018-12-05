package sleeppredicition;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

class GuardSleepPredictionStrategy2 {

    public static void main(String[] args) {
        GuardSleepPredictionStrategy2 sleepPrediction = new GuardSleepPredictionStrategy2();
        String fileName = "./src/main/resources/day4_guard_sleep_observations.txt";

        try (Stream<String> stream = Files.lines(Paths.get(fileName))) {
            int mostSleepyMinute = sleepPrediction.mostSleepyMinute(stream);
            System.out.println(mostSleepyMinute);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    int mostSleepyMinute(Stream<String> logEntries) {
        List<String> sortedEntries = logEntries.sorted().collect(toList());
        List<GuardShift> guardShifts = GuardShift.resolveGuardShifts(sortedEntries);
        GuardSleepMinute mostSleepyGuardMinute = resolveMostSleepyGuardMinute(guardShifts);
        return mostSleepyGuardMinute.getGuardId() * mostSleepyGuardMinute.getMinute();
    }

    private GuardSleepMinute resolveMostSleepyGuardMinute(List<GuardShift> guardShifts) {
        return guardShifts.stream()
                .flatMap(shift -> IntStream.range(0, GuardShift.MAX_MINUTE)
                        .mapToObj(minute -> new GuardSleepMinute(
                                shift.getGuardId(),
                                minute,
                                shift.getMinutesAsleep()[minute]))
                )
                .collect(toMap(GuardSleepMinute::guardMinute, Function.identity(), GuardSleepMinute::merge))
                .values()
                .stream()
                .max(Comparator.comparing(GuardSleepMinute::getSleep))
                .orElseThrow(() -> new RuntimeException("No sleepiest minute found"));
    }

}
