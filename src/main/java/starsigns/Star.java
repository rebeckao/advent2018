package starsigns;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
class Star {
    private int xPos, yPos, xVel, yVel;

    void update() {
        fastForward(1);
    }

    void fastForward(int second) {
        xPos += xVel * second;
        yPos += yVel * second;
    }
}
