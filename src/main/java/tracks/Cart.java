package tracks;

import java.util.List;

class Cart {
    private char direction;
    private List<TURN> turns = List.of(TURN.LEFT, TURN.STRAIGHT, TURN.RIGHT);
    private int nextTurn;

    private enum TURN {
        LEFT,
        STRAIGHT,
        RIGHT
    }

    Cart(Character c) {
        direction = c;
    }

    char resolveDirection(char track) {
        direction = resolveTurn(track);
        return direction;
    }

    private char resolveTurn(char track) {
        if (track == '+') {
            return resolveIntersectionDirection();
        }
        if (track == '/') {
            switch (direction) {
                case '^': return '>';
                case '>': return '^';
                case 'v': return '<';
                case '<': return 'v';
            }
        } else if (track == '\\') {
            switch (direction) {
                case '^': return '<';
                case '<': return '^';
                case 'v': return '>';
                case '>': return 'v';
            }
        }
        return direction;
    }

    private char resolveIntersectionDirection() {
        TURN turn = turns.get(nextTurn);
        nextTurn = (nextTurn + 1) % 3;

        String directions = "^>v<";
        if (turn.equals(TURN.RIGHT)) {
            return directions.charAt((directions.indexOf(direction) + 1) % 4);
        }

        if (turn.equals(TURN.LEFT)) {
            return directions.charAt(Math.floorMod(directions.indexOf(direction) - 1, 4));
        }

        return direction;
    }

    @Override
    public String toString() {
        return "" + direction;
    }
}
