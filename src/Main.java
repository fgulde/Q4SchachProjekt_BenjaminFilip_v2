import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        // Neues Schachbrett, wobei createBoard() und initializeBoard() durch Konstruktor ausgeführt werden
        Board board = new Board();

        // Setzt Status auf Weiß
        Board.setStatus(GameStatus.WHITEMOVE);
        Board.lStatus.setText(Board.status.toString());

        // Neuer JFrame
        JFrame fBoard = new JFrame();

        // Fügt Schachbrett dem JFrame hinzu
        fBoard.add(board.getGUI());

        // Weitere Einstellungen am JFrame
        fBoard.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        fBoard.setLocationByPlatform(true);
        fBoard.pack();
        fBoard.setMinimumSize(fBoard.getSize());
        fBoard.setSize(new Dimension(500, 500));
        fBoard.setTitle("Schachbrett");
        fBoard.setIconImage(new ImageIcon("src/pics/Chess.png").getImage());
        fBoard.setResizable(false);
        fBoard.setVisible(true);
    }
}
