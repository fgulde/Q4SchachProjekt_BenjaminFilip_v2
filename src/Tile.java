import javax.swing.*;

/*
Mit der Tile Klasse werden die Felder des Schachbretts erstellt.
 */

public class Tile {
    private int x;
    private int y;
    private JPanel pTile; // Die Felder werden in Form von Buttons dargestellt. "b" wegen "Button"!!!
    private boolean white; //true = weißes Feld, false = schwarzes Feld
    private Piece piece; // Das Piece, das sich auf diesem Feld befindet


    public Tile(int x, int y, JPanel pTile, boolean white) {
        this.x = x;
        this.y = y;
        this.pTile = pTile;
        this.white = white;
    }

    // Getter und Setter
    public Piece getPiece() {
        return this.piece;
    }

    public void setPiece(Piece piece) {
        this.piece = piece;
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
}
