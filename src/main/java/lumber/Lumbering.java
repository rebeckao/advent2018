package lumber;

import common.Util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import static java.util.stream.Collectors.toList;

class Lumbering {
    private static final char OPEN = '.';
    private static final char TREE = '|';
    private static final char LUMBERYARD = '%';

    public static void main(String[] args) {
        String fileName = "./src/main/resources/day18_lumber.txt";
        try {
            List<String> lines = Files.lines(Paths.get(fileName))
                    .map(line -> line.replace('#', '%'))
                    .collect(toList());
            Lumbering lumbering = new Lumbering();
            char[][] initial = Util.toCharMatrix(lines);
            char[][] after = lumbering.afterMinutes(initial, 10);
//            char[][] after = lumbering.afterMinutes(initial, 1000000000);
            System.out.println(lumbering.resourceValue(after));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private int resourceValue(char[][] map) {
        int trees = 0;
        int lumberyards = 0;
        for (char[] aMap : map) {
            for (char current : aMap) {
                if (current == TREE) {
                    trees++;
                } else if (current == LUMBERYARD) {
                    lumberyards++;
                }
            }
        }
        return trees*lumberyards;
    }

    char[][] afterMinutes(char[][] state, int minutes) {
        for (int i = 0; i < minutes; i++) {
//            Util.display(state);
            char[][] newState = Util.clone(state);
            int rows = state.length;
            int columns = state[0].length;
            for (int row = 0; row < rows; row++) {
                for (int column = 0; column < columns; column++) {
                    char current = state[row][column];
                    int trees = 0;
                    int lumberyards = 0;
                    for (int adjRow = Math.max(0, row - 1); adjRow <= Math.min(row + 1, rows - 1); adjRow++) {
                        for (int adjCol = Math.max(0, column - 1); adjCol <= Math.min(column + 1, columns - 1); adjCol++) {
                            if (adjRow == row && adjCol == column) {
                                continue;
                            }
                            char adjecent = state[adjRow][adjCol];
                            if (adjecent == TREE) {
                                trees++;
                            } else if (adjecent == LUMBERYARD) {
                                lumberyards++;
                            }
                        }
                    }
                    if (current == OPEN) {
                        newState[row][column] = trees >= 3 ? TREE : OPEN;
                    } else if (current == TREE) {
                        newState[row][column] = lumberyards >= 3 ? LUMBERYARD : TREE;
                    } else {
                        newState[row][column] = lumberyards >= 1 && trees >= 1 ? LUMBERYARD : OPEN;
                    }
                }
            }
            state = newState;
        }
        return state;
    }
}
