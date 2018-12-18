package common;

import java.util.List;

public class Util {
    public static char[][] toCharMatrix(String string) {
        String[] rows = string.split(";");
        char[][] map = new char[rows.length][rows[0].length()];
        for (int i = 0; i < rows.length; i++) {
            map[i] = rows[i].toCharArray();
        }
        return map;
    }

    public static char[][] toCharMatrix(List<String> lines) {
            char[][] map = new char[lines.size()][lines.get(0).length()];
        for (int i = 0; i < lines.size(); i++) {
            map[i] = lines.get(i).toCharArray();
        }
        return map;
    }

    public static char[][] clone(char[][] original) {
        int length = original.length;
        char[][] target = new char[length][original[0].length];
        for (int i = 0; i < length; i++) {
            System.arraycopy(original[i], 0, target[i], 0, original[i].length);
        }
        return target;
    }

    public static void display(char[][] map) {
        for (char[] row : map) {
            for (char square : row) {
                System.out.print(square == 0 ? '.' : square);
            }
            System.out.println();
        }
    }

}
