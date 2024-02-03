import javax.swing.*;

// Spielfigur: Pawn / Bauer
public class Pawn extends Piece {

    public Pawn(boolean isWhite, boolean killed, Tile position) {
        super(isWhite, killed, position);
    }

    @Override
    public void calculateNewPos() {
        if (!isMoved()) {
            // Wenn der Bauer noch nicht bewegt wurde, hat er die Option, um zwei Felder zu ziehen
            int direction = isWhite() ? 1 : -1;  // Richtung hängt von der Farbe des Bauern ab
            Tile currentTile = getPosition();
            int newX = currentTile.getX();
            int newY = currentTile.getY() + (2 * direction);
            Tile newTile = Board.tiles[newX][newY];
            int newY1 = currentTile.getY() + direction;
            Tile newTile1 = Board.tiles[newX][newY1];

            // Überprüfen, ob die neuen Positionen innerhalb des Spielbretts liegen
            if (isValidMove(newX, newY)) {
                JButton newButton = createFieldButton(newTile);
                Board.tiles[newX][newY].getpTile().add(newButton);
                Board.tiles[newX][newY].getpTile().updateUI();
                tryKill(newX, newY);
            }
            if (isValidMove(newX, newY1)) {
                JButton newButton1 = createFieldButton(newTile1);
                Board.tiles[newX][newY1].getpTile().add(newButton1);
                Board.tiles[newX][newY].getpTile().updateUI();
                tryKill(newX, newY);
            }
        } else {
            // Falls der Bauer bereits bewegt wurde, kann er nur noch ein Feld ziehen
            int direction = isWhite() ? 1 : -1;  // Richtung hängt von der Farbe des Bauern ab
            Tile currentTile = getPosition();
            int newX = currentTile.getX();
            int newY = currentTile.getY() + direction;
            Tile newTile = Board.tiles[newX][newY];

            // Überprüfen, ob die neue Position innerhalb des Spielbretts liegt
            if (isValidMove(newX, newY)) {
                JButton newButton = createFieldButton(newTile);
                Board.tiles[newX][newY].getpTile().add(newButton);
                Board.tiles[newX][newY].getpTile().updateUI();
                tryKill(newX,newY);
            } else {
                tryKill(newX, newY);
            }
        }
    }

    /* Methode zum Überprüfen, ob die gewünschte Position innerhalb des Spielbretts liegt, ob das neue Tile bereits
     besetzt ist.
     */
    private boolean isValidMove(int x, int y) {
        if (Board.tiles[x][y].getOccupyingPiece() != null) {
            return false;
        } else {
            return x >= 0 && x < 8 && y >= 0 && y < 8;
        }
    }

    private boolean canKill(int x, int y) {
        if (Board.tiles[x][y].getOccupyingPiece() != null){
            if ((Board.tiles[x][y].getOccupyingPiece().isWhite() && isWhite())
                    || (!Board.tiles[x][y].getOccupyingPiece().isWhite() && !isWhite())) {
                return false;
            } else {
                return x >= 0 && x < 8 && y >= 0 && y < 8;
            }
        } else {
            return false;
        }
    }

    public void tryKill(int x, int y){
        x += 1;
        int newX1 = x - 2;
        Tile newTile = Board.tiles[x][y];
        Tile newTile1 = Board.tiles[newX1][y];
            if (x <= 7 && canKill(x, y)) {
                JButton newButton = createFieldButton(newTile);
                newButton.setIcon(new ImageIcon("src/pics/KillTarget.png"));
                Board.tiles[x][y].getpTile().add(newButton);
                Board.tiles[x][y].getpTile().updateUI();
            }
            if (newX1 >= 0 && canKill(newX1, y)) {
                JButton newButton = createFieldButton(newTile1);
                newButton.setIcon(new ImageIcon("src/pics/KillTarget.png"));
                Board.tiles[newX1][y].getpTile().add(newButton);
                Board.tiles[newX1][y].getpTile().updateUI();
            }
    }

    // Methode zum Umwandeln eines Bauern
    public void promote () {
        // Implementiere die Logik zur Umwandlung des Bauern
    }


    @Override
    protected ImageIcon getIconPath () {
        return new ImageIcon(isWhite() ? "src/pics/PawnBlack.png" : "src/pics/PawnWhite.png");
    }
}
