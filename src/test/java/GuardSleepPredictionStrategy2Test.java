import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GuardSleepPredictionStrategy2Test {
    private static final String ORDERED_OBSERVATIONS =
            "[1518-11-01 00:00] Guard #10 begins shift;" +
            "[1518-11-01 00:05] falls asleep;" +
            "[1518-11-01 00:25] wakes up;" +
            "[1518-11-01 00:30] falls asleep;" +
            "[1518-11-01 00:55] wakes up;" +
            "[1518-11-01 23:58] Guard #99 begins shift;" +
            "[1518-11-02 00:40] falls asleep;" +
            "[1518-11-02 00:50] wakes up;" +
            "[1518-11-03 00:05] Guard #10 begins shift;" +
            "[1518-11-03 00:24] falls asleep;" +
            "[1518-11-03 00:29] wakes up;" +
            "[1518-11-04 00:02] Guard #99 begins shift;" +
            "[1518-11-04 00:36] falls asleep;" +
            "[1518-11-04 00:46] wakes up;" +
            "[1518-11-05 00:03] Guard #99 begins shift;" +
            "[1518-11-05 00:45] falls asleep;" +
            "[1518-11-05 00:55] wakes up";
    private static final String UNORDERED_OBSERVATIONS =
            "[1518-11-01 00:05] falls asleep;" +
            "[1518-11-05 00:45] falls asleep;" +
            "[1518-11-01 00:30] falls asleep;" +
            "[1518-11-01 00:55] wakes up;" +
            "[1518-11-04 00:02] Guard #99 begins shift;" +
            "[1518-11-01 00:00] Guard #10 begins shift;" +
            "[1518-11-01 23:58] Guard #99 begins shift;" +
            "[1518-11-02 00:40] falls asleep;" +
            "[1518-11-03 00:24] falls asleep;" +
            "[1518-11-05 00:55] wakes up;" +
            "[1518-11-03 00:29] wakes up;" +
            "[1518-11-04 00:36] falls asleep;" +
            "[1518-11-04 00:46] wakes up;" +
            "[1518-11-01 00:25] wakes up;" +
            "[1518-11-05 00:03] Guard #99 begins shift;" +
            "[1518-11-02 00:50] wakes up;" +
            "[1518-11-03 00:05] Guard #10 begins shift";
    private GuardSleepPredictionStrategy2 guardSleepPredictionStrategy2 = new GuardSleepPredictionStrategy2();

    @ParameterizedTest
    @CsvSource({ORDERED_OBSERVATIONS, UNORDERED_OBSERVATIONS})
    void mostSleepyMinute(String observations) {
        int actual = guardSleepPredictionStrategy2.mostSleepyMinute(Stream.of(observations.split(";")));
        assertEquals(4455, actual);
    }
}