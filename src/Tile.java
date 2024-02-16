import javax.swing.*;
import java.awt.*;


/*
Mit der Tile Klasse werden die Felder des Schachbretts erstellt.
 */

public class Tile {
    private int x;
    private int y;
    private JPanel pTile; // Die Felder werden in Form von Buttons dargestellt. "b" wegen "Button"!!!
    private boolean white; //true = weißes Feld, false = schwarzes Feld
    private Piece occupyingPiece; // Das Piece, das sich auf diesem Feld befindet
    public boolean occupied;


    public Tile(int x, int y, JPanel pTile, boolean white, boolean occupied) {
        this.x = x;
        this.y = y;
        this.pTile = pTile;
        this.white = white;
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

    public void setpTile(JPanel pTile) {
        this.pTile = pTile;
    }

    public boolean isWhite() {
        return white;
    }

    public void setWhite(boolean tWhite) {
        this.white = white;
    }

    public int getY() {
        return y;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public boolean isOccupied() {
        return (this.occupyingPiece != null);
    }
    public void setOccupied(boolean occ){
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

    public boolean hasButton() {
        return getButton() != null;
    }

}

