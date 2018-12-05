package sleeppredicition;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.IntStream;

@Getter
class GuardShift {
    private static final Pattern SHIFT_START = Pattern.compile("Guard #([0-9]+) begins shift");
    private static final String FALL_ASLEEP = "falls asleep";
    private static final String WAKES_UP = "wakes up";
    static final int MAX_MINUTE = 60;

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

    int totalMinutesAsleep() {
        return IntStream.of(minutesAsleep).sum();
    }

    static List<GuardShift> resolveGuardShifts(List<SleepObservation> observations) {
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
}