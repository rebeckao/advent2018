import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@AllArgsConstructor
@Getter
class SleepObservation {
    private static final Pattern SLEEP_OBSERVATION = Pattern.compile("\\[(.*)] (.*)");
    private static final DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    private LocalDateTime time;
    private String observation;

    static List<SleepObservation> sortObservations(Stream<String> sleepObservations) {
        return sleepObservations
                .map(SleepObservation::match)
                .map(matcher -> {
                    LocalDateTime time = LocalDateTime.parse(matcher.group(1), format);
                    return new SleepObservation(time, matcher.group(2));
                })
                .sorted(Comparator.comparing(SleepObservation::getTime))
                .collect(Collectors.toList());
    }

    private static Matcher match(String observation) {
        Matcher matcher = SLEEP_OBSERVATION.matcher(observation);
        if (!matcher.matches()) {
            throw new RuntimeException(String.format("Observation %s does not match pattern %s!", observation, SLEEP_OBSERVATION));
        }
        return matcher;
    }
}