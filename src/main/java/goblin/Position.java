package goblin;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import static java.util.Comparator.comparing;

@AllArgsConstructor
@Getter
@EqualsAndHashCode
class Position implements Comparable<Position>  {

    private int row;
    private int column;

    @Override
    public int compareTo(Position o) {
        return comparing(Position::getRow)
                .thenComparing(Position::getColumn)
                .compare(this, o);
    }

    boolean isNextTo(Position enemyPosition) {
        return Math.abs(enemyPosition.getRow() - getRow()) + Math.abs(enemyPosition.getColumn() - getColumn()) == 1;
    }

    @Override
    public String toString() {
        return row + "," + column;
    }
}
