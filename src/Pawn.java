// Spielfigur: Pawn / Bauer
public class Pawn extends Piece {
    boolean moved = false; // Checkt ob der Pawn schon bewegt wurde, da er im ersten Zug zwei Felder voran kann

    public Pawn(boolean white, boolean killed, Tile position) {
        super(white, killed, position);
    }

    @Override
    public void move() {

    }

    // Methode zum Umwandeln des Bauern
    public void promote() {

    }
}
