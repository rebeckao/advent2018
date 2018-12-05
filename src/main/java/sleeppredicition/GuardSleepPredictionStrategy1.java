package sleeppredicition;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toMap;

class GuardSleepPredictionStrategy1 {

    public static void main(String[] args) {
        GuardSleepPredictionStrategy1 sleepPrediction = new GuardSleepPredictionStrategy1();
        String fileName = "./src/main/resources/day4_guard_sleep_observations.txt";

        try (Stream<String> stream = Files.lines(Paths.get(fileName))) {
            int mostSleepyMinute = sleepPrediction.mostSleepyMinute(stream);
            System.out.println(mostSleepyMinute);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    int mostSleepyMinute(Stream<String> sleepObservations) {
        List<SleepObservation> observations = SleepObservation.sortObservations(sleepObservations);
        List<GuardShift> guardShifts = GuardShift.resolveGuardShifts(observations);
        Integer mostSleepyGuard = resolveMostSleepyGuard(guardShifts);
        Integer mostSleepyMinute = resolveMostSleepyMinute(guardShifts, mostSleepyGuard);
        return mostSleepyGuard * mostSleepyMinute;
    }

    private Integer resolveMostSleepyGuard(List<GuardShift> guardShifts) {
        return guardShifts.stream()
                .collect(toMap(GuardShift::getGuardId, GuardShift::totalMinutesAsleep, Integer::sum))
                .entrySet()
                .stream()
                .max(Comparator.comparing(Map.Entry::getValue))
                .orElseThrow(() -> new RuntimeException("No sleepiest guard found"))
                .getKey();
    }

    private Integer resolveMostSleepyMinute(List<GuardShift> guardShifts, Integer mostSleepyGuard) {
        return guardShifts.stream()
                .filter(shift -> mostSleepyGuard.equals(shift.getGuardId()))
                .map(GuardShift::getMinutesAsleep)
                .flatMap(minutes -> IntStream.range(0, GuardShift.MAX_MINUTE)
                        .mapToObj(minute -> new GuardSleepMinute(minute, minutes[minute])))
                .collect(toMap(GuardSleepMinute::getMinute, GuardSleepMinute::getSleep, Integer::sum))
                .entrySet()
                .stream()
                .max(Comparator.comparing(Map.Entry::getValue))
                .orElseThrow(() -> new RuntimeException("No sleepiest minute found"))
                .getKey();
    }
}
