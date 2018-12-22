package cave;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class Cave {

    public static void main(String[] args) {
        try {
            List<String> scan = Files.readAllLines(Paths.get("./src/main/resources/day22_scan.txt"));
            int depth = Integer.parseInt(scan.get(0).substring(7));
            Pattern pattern = Pattern.compile("target: (\\d+),(\\d+)");
            Matcher matcher = pattern.matcher(scan.get(1));
            if (!matcher.matches()) {
                throw new IllegalArgumentException("Does not compute: " + scan.get(1));
            }
            int targetX = Integer.parseInt(matcher.group(1));
            int targetY = Integer.parseInt(matcher.group(2));
            System.out.println(new Cave().riskLevel(depth, targetX, targetY));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    int riskLevel(int depth, int targetX, int targetY) {
        int[][] erosionMap = buildErosionMap(depth, targetX, targetY, targetX + 1, targetY + 1);
        int[][] regions = buildRegionMap(erosionMap);
        int riskValue = 0;
        for (int[] region : regions) {
            for (int aRegion : region) {
                riskValue += aRegion;
            }
        }
        return riskValue;
    }

    int[][] buildErosionMap(int depth, int targetX, int targetY, int maxX, int maxY) {
        int[][] erosionMap = new int[maxY][maxX];
        erosionMap[0][0] = erosionLevel(depth, 0);
        for (int x = 1; x < erosionMap[0].length; x++) {
            int geologicalIndex = x * 16807;
            erosionMap[0][x] = erosionLevel(depth, geologicalIndex);
        }
        for (int y = 1; y < erosionMap.length; y++) {
            int geologicalIndex = y * 48271;
            erosionMap[y][0] = erosionLevel(depth, geologicalIndex);
        }
        for (int y = 1; y < erosionMap.length; y++) {
            for (int x = 1; x < erosionMap[y].length; x++) {
                int geologicalIndex = geologicalIndex(x, y, targetX, targetY, erosionMap);
                erosionMap[y][x] = erosionLevel(depth, geologicalIndex);
            }
        }
        return erosionMap;
    }

    private int geologicalIndex(int x, int y, int targetX, int targetY, int[][] erosionMap) {
        if (x == targetX && y == targetY) {
            return 0;
        }
        return erosionMap[y][x - 1] * erosionMap[y - 1][x];
    }

    int[][] buildRegionMap(int[][] erosionMap) {
        int[][] map = new int[erosionMap.length][erosionMap[0].length];
        for (int y = 0; y < erosionMap.length; y++) {
            for (int x = 0; x < erosionMap[y].length; x++) {
                map[y][x] = erosionMap[y][x] % 3;
            }
        }
        return map;
    }

    char[][] buildRegionCharMap(int[][] regionMap) {
        char[][] map = new char[regionMap.length][regionMap[0].length];
        for (int y = 0; y < regionMap.length; y++) {
            for (int x = 0; x < regionMap[y].length; x++) {
                map[y][x] = resolveRegionType(regionMap[y][x]);
            }
        }
        return map;
    }

    private int erosionLevel(int depth, int geologicalIndex) {
        return (geologicalIndex + depth) % 20183;
    }

    private char resolveRegionType(int erosionLevel) {
        switch (erosionLevel) {
            case 0:
                return '.';
            case 1:
                return '=';
            case 2:
                return '|';
            default:
                throw new IllegalStateException("Does not compute: " + erosionLevel);
        }
    }
}
