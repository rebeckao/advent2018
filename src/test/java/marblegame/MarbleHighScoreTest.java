package marblegame;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;

class MarbleHighScoreTest {
    private MarbleHighScore marbleHighScore = new MarbleHighScore();

    @ParameterizedTest
    @CsvSource({
            "9 players; last marble is worth 25 points, 32",
            "10 players; last marble is worth 1618 points, 8317",
            "13 players; last marble is worth 7999 points, 146373",
            "17 players; last marble is worth 1104 points, 2764",
            "21 players; last marble is worth 6111 points, 54718",
            "30 players; last marble is worth 5807 points, 37305"
    })
    void highScore(String conditions, int expected) {
        long actual = marbleHighScore.highScore(conditions, 1);
        assertEquals(expected, actual);
    }
}