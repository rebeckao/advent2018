package sleeppredicition;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
class GuardSleepMinute {
    private int guardId;
    private int minute;
    private int sleep;

    GuardSleepMinute(int minute, int sleep) {
        this.minute = minute;
        this.sleep = sleep;
    }

    String guardMinute() {
        return guardId + "_" + minute;
    }

    GuardSleepMinute merge(GuardSleepMinute other) {
        return new GuardSleepMinute(guardId, minute, sleep + other.getSleep());
    }
}
