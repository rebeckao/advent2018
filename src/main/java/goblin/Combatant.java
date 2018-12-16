package goblin;

import lombok.Getter;
import lombok.Setter;

@Getter
class Combatant {

    private int healthPoints = 200;
    @Setter
    private int attackPower = 3;
    @Setter
    private Position position;
    private char type;

    void reduceHp(int attackPoints) {
        healthPoints -= attackPoints;
    }

    boolean isDead() {
        return healthPoints <= 0;
    }

    Combatant(int row, int column, char type) {
        this.position = new Position(row, column);
        this.type = type;
    }

    @Override
    public String toString() {
        return String.valueOf(type);
    }
}
