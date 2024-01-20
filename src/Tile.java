import javax.swing.*;
import java.awt.*;

/*
Mit der Tile Klasse werden die Felder des Schachbretts erstellt.
 */

public class Tile {
    private int x;
    private int y;
    private JButton bTile; // Die Tiles werden in Form von Buttons dargestellt.
    private boolean white; //true = weißes Feld, false = schwarzes Feld
    private Piece piece; // Das Piece, das sich auf diesem Tile befindet

    public Tile(int x, int y, JButton bTile, boolean white) {
        this.x = x;
        this.y = y;
        this.bTile = bTile;
        this.white = white;
    }

    // Getter und Setter
    public Piece getPiece() {
        return this.piece;
    }

    public void setPiece(Piece piece) {
        this.piece = piece;
    }
    public JButton getbTile() {
        return bTile;
    }

    public void setbTile(JButton bTile) {
        this.bTile = bTile;
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
}
