import javax.swing.*;
import java.awt.*;


/*
Mit der Tile Klasse werden die Felder des Schachbretts erstellt.
 */

public class Tile {
    private final int x;
    private final int y;
    private final JPanel pTile; // Die Felder werden in Form von Buttons dargestellt. "b" wegen "Button"!!!
    private Piece occupyingPiece; // Das Piece, das sich auf diesem Feld befindet
    public boolean occupied;


    public Tile(int x, int y, JPanel pTile) {
        this.x = x;
        this.y = y;
        this.pTile = pTile;
        //true = weißes Feld, false = schwarzes Feld
    }

    // Getter und Setter
    public Piece getOccupyingPiece() {
        return this.occupyingPiece;
    }
    public void setOccupyingPiece(Piece occupyingPiece) {
        this.occupyingPiece = occupyingPiece;
    }
    public JPanel getpTile() {
        return pTile;
    }

    public int getY() {
        return y;
    }

    public int getX() {
        return x;
    }

    public boolean isOccupied() {
        return (this.occupyingPiece != null);
    }
    public void setOccupied(boolean occupied){
        this.occupied = occupied;
    }
    public JButton getButton() {
        for (Component component : pTile.getComponents()) {
            if (component instanceof JButton) {
                return (JButton) component;
            }
        }
        return null;  // Wenn kein Button gefunden wurde
    }
}

