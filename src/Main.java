import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        Board board = new Board();
        JFrame fBoard = new JFrame();
        fBoard.add(board.getChessBoard());
        fBoard.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        fBoard.setLocationByPlatform(true);
        fBoard.pack();
        fBoard.setMinimumSize(fBoard.getSize());
        fBoard.setVisible(true);
    }
}
