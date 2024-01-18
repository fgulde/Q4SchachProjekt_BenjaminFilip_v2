import javax.swing.*;
import java.awt.*;

public class Main {
    public static void main(String[] args) {
        // Wir erstellen ein neues Board-Objekt
        Board board = new Board();

        // Wir erstellen ein neues JFrame-Objekt
        JFrame fBoard = new JFrame();

        // Wir fügen das Schachbrett zum JFrame hinzu
        fBoard.add(board.getChessBoard());

        // Wir stellen sicher, dass das Programm beendet wird, wenn das Fenster geschlossen wird
        fBoard.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Wir positionieren das Fenster an einer geeigneten Position auf dem Bildschirm
        fBoard.setLocationByPlatform(true);

        // Wir packen das Fenster, um seine Größe an den Inhalt anzupassen
        fBoard.pack();

        // Wir stellen sicher, dass das Fenster nicht kleiner als seine ursprüngliche Größe gemacht werden kann
        fBoard.setMinimumSize(fBoard.getSize());

        // Wir setzen die Größe des Fensters auf 800x800 Pixel
        fBoard.setSize(new Dimension(800, 800));

        // Wir verhindern, dass das Fenster von Benutzern skaliert wird
        fBoard.setResizable(false);

        // Wir machen das Fenster sichtbar
        fBoard.setVisible(true);
    }
}
