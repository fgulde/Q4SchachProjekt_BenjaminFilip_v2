import javax.swing.*;
import java.awt.*;

public class Main {
    public static void main(String[] args) {
        // Neues Schachbrett, wobei createBoard() und initializeBoard() durch Konstruktor ausgeführt werden
        Board board = new Board();

        // Neuer JFrame
        JFrame fBoard = new JFrame();

        // Fügt Schachbrett dem JFrame hinzu, Schachbrett enthält alle GUI-Elemente in Form von "chessboard"-JPanel
        fBoard.add(board.getChessBoard());

        // Stellt sicher, dass das Programm beendet wird, wenn das Fenster geschlossen wird
        fBoard.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Positioniert das Fenster an einer geeigneten Position auf dem Bildschirm
        fBoard.setLocationByPlatform(true);

        // Stellt sicher, dass das Fenster nicht zu klein wird und skaliert es - wenn nötig - auf die richtige Größe
        fBoard.pack();

        // Stellt kleinste mögliche Größe des Fensters auf die Größe des Board-Objekts
        fBoard.setMinimumSize(fBoard.getSize());

        // Setzt die Größe des Fensters auf 500x500 Pixel
        fBoard.setSize(new Dimension(500, 500));

        // Verhindert, dass das Fenster skaliert werden kann, da sonst das Schachbrett verzerrt werden würde
        fBoard.setResizable(false);

        // Macht das Fenster sichtbar
        fBoard.setVisible(true);
    }
}
