import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toMap;

class GuardSleepPrediction {
    private static final Pattern SLEEP_OBSERVATION = Pattern.compile("\\[(.*)] (.*)");
    private static final Pattern SHIFT_START = Pattern.compile("Guard #([0-9]+) begins shift");
    private static final String FALL_ASLEEP = "falls asleep";
    private static final String WAKES_UP = "wakes up";
    private static final int MAX_MINUTE = 60;
    private DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public static void main(String[] args) {
        GuardSleepPrediction sleepPrediction = new GuardSleepPrediction();
        String fileName = "./src/main/resources/day4_guard_sleep_observations.txt";

        try (Stream<String> stream = Files.lines(Paths.get(fileName))) {
            int mostSleepyMinute = sleepPrediction.mostSleepyMinute(stream);
            System.out.println(mostSleepyMinute);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    int mostSleepyMinute(Stream<String> sleepObservations) {
        List<SleepObservation> observations = sortObservations(sleepObservations);
        List<GuardShift> guardShifts = resolveGuardShifts(observations);
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
                .flatMap(minutes -> IntStream.range(0, MAX_MINUTE)
                        .mapToObj(minute -> new SleepMinute(minute, minutes[minute])))
                .collect(toMap(SleepMinute::getMinute, SleepMinute::getSleep, Integer::sum))
                .entrySet()
                .stream()
                .max(Comparator.comparing(Map.Entry::getValue))
                .orElseThrow(() -> new RuntimeException("No sleepiest minute found"))
                .getKey();
    }

    private List<GuardShift> resolveGuardShifts(List<SleepObservation> observations) {
        List<GuardShift> guardShifts = new ArrayList<>();
        GuardShift currentGuardShift = new GuardShift(-1);

        for (SleepObservation sleepObservation : observations) {
            String observation = sleepObservation.getObservation();
            Matcher match = SHIFT_START.matcher(observation);
            int minute = sleepObservation.getTime().getMinute();
            if (match.matches()) {
                currentGuardShift = new GuardShift(Integer.parseInt(match.group(1)));
                guardShifts.add(currentGuardShift);
            } else if (observation.equals(FALL_ASLEEP)) {
                currentGuardShift.registerSleep(minute);
            } else if (observation.equals(WAKES_UP)) {
                currentGuardShift.registerAwake(minute);
            } else {
                throw new RuntimeException(String.format("Failed to parse observation %s", observation));
            }
        }
        return guardShifts;
    }

    private List<SleepObservation> sortObservations(Stream<String> sleepObservations) {
        return sleepObservations
                .map(observation -> match(observation, SLEEP_OBSERVATION))
                .map(matcher -> {
                    LocalDateTime time = LocalDateTime.parse(matcher.group(1), format);
                    return new SleepObservation(time, matcher.group(2));
                })
                .sorted(Comparator.comparing(SleepObservation::getTime))
                .collect(Collectors.toList());
    }

    private Matcher match(String observation, Pattern pattern) {
        Matcher matcher = pattern.matcher(observation);
        if (!matcher.matches()) {
            throw new RuntimeException(String.format("Observation %s does not match pattern %s!", observation, pattern));
        }
        return matcher;
    }

    @AllArgsConstructor
    @Getter
    private class SleepObservation {
        private LocalDateTime time;
        private String observation;
    }

    @Getter
    private class GuardShift {
        private int guardId;
        private int[] minutesAsleep = new int[MAX_MINUTE];

        private GuardShift(int guardId) {
            this.guardId = guardId;
        }

        private void registerSleep(int startMinute) {
            register(startMinute, 1);
        }

        private void registerAwake(int startMinute) {
            register(startMinute, 0);
        }

        private void register(int startMinute, int isSleeping) {
            for (int minute = startMinute; minute < MAX_MINUTE; minute++) {
                minutesAsleep[minute] = isSleeping;
            }
        }

        private int totalMinutesAsleep() {
            return IntStream.of(minutesAsleep).sum();
        }

    }

    @AllArgsConstructor
    @Getter
    private class SleepMinute {
        private int minute;
        private int sleep;
    }
}
