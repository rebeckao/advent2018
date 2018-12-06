package coordinates;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Getter
@AllArgsConstructor
@EqualsAndHashCode
class Coordinate {
    private static final Pattern COORDINATE = Pattern.compile("([0-9]+), ([0-9]+)");

    private int x;
    private int y;

    static Coordinate parse(String representation) {
        Matcher matcher = COORDINATE.matcher(representation);
        if (!matcher.matches()) {
            throw new IllegalStateException("Failed to parse " + representation);
        }
        return new Coordinate(Integer.parseInt(matcher.group(1)), Integer.parseInt(matcher.group(2)));
    }
}
