import javax.swing.*;

// Spielfigur: Pawn / Bauer
public class Pawn extends Piece {
    private boolean moved = false;

    public Pawn(boolean isWhite, boolean killed, Tile position) {
        super(isWhite, killed, position);
    }

    @Override
    public void move() {
        if (!moved) {
            // Wenn der Bauer noch nicht bewegt wurde, hat er die Option, um zwei Felder zu ziehen
            int direction = isWhite() ? -1 : 1;  // Richtung hängt von der Farbe des Bauern ab
            Tile currentTile = getPosition();
            int newX = currentTile.getX();
            int newY = currentTile.getY() + (2 * direction);

            // Überprüfen, ob die neuen Positionen innerhalb des Spielbretts liegen
            if (isValidMove(newX, newY)) {
                // Bewege den Bauer auf das neue Feld
                Tile newTile = Board.tiles[newX][newY];
                getPosition().setOccupied(false);
                newTile.setOccupied(true);
                newTile.setOccupyingPiece(this);
                setPosition(newTile);
                moved = true;  // Setze moved auf true, da der Bauer jetzt bewegt wurde
            }
        } else {
            // Falls der Bauer bereits bewegt wurde, kann er nur noch ein Feld ziehen
            int direction = isWhite() ? -1 : 1;  // Richtung hängt von der Farbe des Bauern ab
            Tile currentTile = getPosition();
            int newX = currentTile.getX();
            int newY = currentTile.getY() + direction;

            // Überprüfen, ob die neue Position innerhalb des Spielbretts liegt
            if (isValidMove(newX, newY)) {
                // Bewege den Bauer auf das neue Feld
                Tile newTile = Board.tiles[newX][newY];
                getPosition().setOccupied(false);
                newTile.setOccupied(true);
                newTile.setOccupyingPiece(this);
                setPosition(newTile);
            }
        }
    }

    // Methode zum Überprüfen, ob die gewünschte Position innerhalb des Spielbretts liegt
    private boolean isValidMove(int x, int y) {
        return x >= 0 && x < 8 && y >= 0 && y < 8;
    }

    // Methode zum Umwandeln eines Bauern
    public void promote() {
        // Implementiere die Logik zur Umwandlung des Bauern
    }

    @Override
    protected ImageIcon getIconPath() {
        return new ImageIcon(isWhite() ? "src/pics/PawnWhite.png" : "src/pics/PawnBlack.png");
    }
}
