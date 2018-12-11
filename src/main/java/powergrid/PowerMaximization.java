package powergrid;

class PowerMaximization {

    public static void main(String[] args) {
        PowerMaximization powerMaximization = new PowerMaximization();
        int serialNumber = 5093;
        int[][] grid = powerMaximization.constructGrid(serialNumber);
        System.out.println(powerMaximization.mostHighPoweredSubGridOfSize3(grid));
        System.out.println(powerMaximization.mostHighPoweredSubGridOfAnySize(grid));
    }

    String mostHighPoweredSubGridOfSize3(int[][] grid) {
        int highestPowerLevel = -50;
        String bestSubGrid = "";
        for (int y = 1; y < grid.length - 1; y++) {
            for (int x = 1; x < grid[y - 1].length - 1; x++) {
                int currentPowerLevel = grid[y - 1][x - 1] + grid[y - 1][x] + grid[y - 1][x + 1]
                        + grid[y][x - 1] + grid[y][x] + grid[y][x + 1]
                        + grid[y + 1][x - 1] + grid[y + 1][x] + grid[y + 1][x + 1];
                if (currentPowerLevel > highestPowerLevel) {
                    highestPowerLevel = currentPowerLevel;
                    bestSubGrid = x + "," + y;
                }
            }
        }

        return bestSubGrid;
    }

    String mostHighPoweredSubGridOfAnySize(int[][] grid) {
        int highestPowerLevel = -5;
        String bestSubGrid = "";
        int bestSize = 0;
        for (int size = 1; size <= 300; size++) {
            int gridSide = 300;
            for (int y = 1; y < gridSide - (size - 2); y++) {
                for (int x = 1; x < gridSide - (size - 2); x++) {
                    int currentPowerLevel = powerLevelForSubGrid(grid, size, x, y);
                    if (currentPowerLevel > highestPowerLevel) {
                        highestPowerLevel = currentPowerLevel;
                        bestSubGrid = x + "," + y;
                        bestSize = size;
                    }
                }
            }
        }

        return bestSubGrid + "," + bestSize;
    }

    int powerLevelForSubGrid(int[][] grid, int size, int x, int y) {
        int currentPowerLevel = 0;
        for (int suby = y; suby < y + size; suby++) {
            for (int subx = x; subx < x + size; subx++) {
                int cellPowerLevel = grid[suby - 1][subx - 1];
                currentPowerLevel += cellPowerLevel;
            }
        }
        return currentPowerLevel;
    }

    int[][] constructGrid(int serialNumber) {
        int[][] grid = new int[300][300];
        for (int y = 1; y <= grid.length; y++) {
            for (int x = 1; x <= grid[y - 1].length; x++) {
                grid[y - 1][x - 1] = powerLevelOfCell(x, y, serialNumber);
            }
        }
        return grid;
    }

    int powerLevelOfCell(int x, int y, int serialNumber) {
        int rackId = x + 10;
        int intermediatePowerLevel = rackId * y;
        intermediatePowerLevel += serialNumber;
        intermediatePowerLevel *= rackId;
        return (intermediatePowerLevel / 100) % 10 - 5;
    }
}
