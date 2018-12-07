package sleigh;

import lombok.Getter;

class Worker {
    @Getter
    private String step;
    private int timeRemaining;

    void work() {
        timeRemaining--;
    }

    boolean isWorking() {
        return timeRemaining > 0;
    }

    void assignWork(String step, int timeRequired) {
        this.step = step;
        this.timeRemaining = timeRequired;
    }

    void assignBreak() {
        step = null;
        timeRemaining = 0;
    }
}
