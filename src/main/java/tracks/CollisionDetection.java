package tracks;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

class CollisionDetection {

    public static void main(String[] args) {
        try {
            List<String> lines = Files.readAllLines(Paths.get("./src/main/resources/day13_tracks.txt"));
            System.out.println(new CollisionDetection().coordinatesOfFirstCollision(lines));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    String coordinatesOfFirstCollision(List<String> initialTracks) {
        int rows = initialTracks.size();
        int columns = initialTracks.get(0).length();
        char[][] tracks = new char[rows][columns];
        Cart[][] carts = new Cart[rows][columns];
        // Construct tracks and place carts
        for (int row = 0; row < rows; row++) {
            String initialRow = initialTracks.get(row);
            for (int col = 0; col < columns; col++) {
                Character c = initialRow.charAt(col);
                boolean isTrack = "-|/\\+".indexOf(c) > 0;
                if (isTrack) {
                    tracks[row][col] = c;
                }
                boolean isCart = "<>^v".indexOf(c) > 0;
                if (isCart) {
                    Cart cart = new Cart(c);
                    carts[row][col] = cart;
                    tracks[row][col] = underlyingTrack(c);
                }
            }
        }

        while(true) {
            Cart[][] cartsNextRount = new Cart[rows][columns];
            for (int row = 0; row < rows; row++) {
                for (int col = 0; col < columns; col++) {
                    Cart currentCart = carts[row][col];
                    if (currentCart == null) {
                        continue;
                    }
                    int nextRow = row;
                    int nextCol = col;
                    switch (currentCart.resolveDirection(tracks[row][col])) {
                        case '^': nextRow--; break;
                        case '>': nextCol++; break;
                        case 'v': nextRow++; break;
                        case '<': nextCol--; break;
                    }

                    if (carts[nextRow][nextCol] != null || cartsNextRount[nextRow][nextCol] != null) {
                        // Crash!
                        return nextCol + "," + nextRow;
                    }

                    cartsNextRount[nextRow][nextCol] = currentCart;
                    carts[row][col] = null;
                }
            }
            carts = cartsNextRount;
        }
    }

    private char underlyingTrack(Character c) {
        switch (c) {
            case '<':
            case '>': return '-';
            case '^':
            case 'v': return '|';
            default: throw  new IllegalStateException("No underlying track: " + c);
        }
    }
}
