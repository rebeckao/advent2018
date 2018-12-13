package tracks;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

class LastCartStanding {

    public static void main(String[] args) {
        try {
            List<String> lines = Files.readAllLines(Paths.get("./src/main/resources/day13_tracks.txt"));
            System.out.println(new LastCartStanding().coordinatesOfLastCart(lines));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    String coordinatesOfLastCart(List<String> initialTracks) {
        int rows = initialTracks.size();
        int columns = initialTracks.get(0).length();
        char[][] tracks = new char[rows][columns];
        Cart[][] carts = new Cart[rows][columns];
        int numberOfCarts = 0;
        // Construct tracks and place carts
        for (int row = 0; row < rows; row++) {
            String initialRow = initialTracks.get(row);
            for (int col = 0; col < columns; col++) {
                Character c = initialRow.charAt(col);
                boolean isCart = "<>^v".indexOf(c) >= 0;
                tracks[row][col] = c;
                if (isCart) {
                    Cart cart = new Cart(c);
                    carts[row][col] = cart;
                    tracks[row][col] = underlyingTrack(c);
                    numberOfCarts++;
                }
            }
        }
//        displaySnapshot(tracks, carts);

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
                        numberOfCarts -= 2;
                        carts[row][col] = null;
                        carts[nextRow][nextCol] = null;
                        cartsNextRount[nextRow][nextCol] = null;
                    } else {
                        cartsNextRount[nextRow][nextCol] = currentCart;
                        carts[row][col] = null;
                    }
                }
            }
            carts = cartsNextRount;
//            displaySnapshot(tracks, carts);
            if (numberOfCarts <= 1) {
                return coordinatesOfLastCart(carts);
            }
        }
    }

    private void displaySnapshot(char[][] tracks, Cart[][] carts) {
        StringBuilder snapshot = new StringBuilder();
        for (int row = 0; row < carts.length; row++) {
            for (int col = 0; col < carts[row].length; col++) {
                Cart cart = carts[row][col];
                if (cart != null) {
                    snapshot.append(cart);
                } else {
                    snapshot.append(tracks[row][col]);
                }
            }
            snapshot.append("\n");
        }
        System.out.println(snapshot);
    }

    private String coordinatesOfLastCart(Cart[][] carts) {
        for (int row = 0; row < carts.length; row++) {
            for (int col = 0; col < carts[row].length; col++) {
                if (carts[row][col] != null) {
                    return col + "," + row;
                }
            }
        }
        return "";
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
