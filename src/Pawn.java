import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeListener;

// Spielfigur: Pawn / Bauer
public class Pawn extends Piece {
    boolean moved = false; // Checkt ob der Pawn schon bewegt wurde, da er im ersten Zug zwei Felder voran kann

    public Pawn(boolean white, boolean killed, Tile position) {
        super(white, killed, position);
    }

    @Override
    public void move() {

    }

    // Methode zum Umwandeln eines Bauern
    public void promote() {

    }
}
