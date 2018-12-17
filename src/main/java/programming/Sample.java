package programming;

import lombok.Getter;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Getter
class Sample {
    private static final Pattern SAMPLE = Pattern.compile("Before: \\[(\\d+), (\\d+), (\\d+), (\\d+)]" +
            "(\\d+) ([0-3]+) ([0-3]+) ([0-3]+)" +
            "After: *\\[(\\d+), (\\d+), (\\d+), (\\d+)]");
    private int[] registryBefore;
    private int[] registryAfter;
    private int a;
    private int b;
    private int c;
    private int opcodeNumber;

    Sample(String sample) {
        Matcher matcher = SAMPLE.matcher(sample);
        if (!matcher.matches()) {
            throw new IllegalArgumentException("Does not compute: " + sample);
        }
        registryBefore = new int[]{
                Integer.parseInt(matcher.group(1)),
                Integer.parseInt(matcher.group(2)),
                Integer.parseInt(matcher.group(3)),
                Integer.parseInt(matcher.group(4))
        };
        opcodeNumber = Integer.parseInt(matcher.group(5));
        a = Integer.parseInt(matcher.group(6));
        b = Integer.parseInt(matcher.group(7));
        c = Integer.parseInt(matcher.group(8));
        registryAfter = new int[]{
                Integer.parseInt(matcher.group(9)),
                Integer.parseInt(matcher.group(10)),
                Integer.parseInt(matcher.group(11)),
                Integer.parseInt(matcher.group(12))
        };
    }
}
