import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.image.BufferedImage;

public class Board {

    private Tile[][] tiles = new Tile[8][8];
    private JPanel chessBoard;

    public Board(){
        createBoard();
    }

    public void createBoard(){
        chessBoard = new JPanel(new GridLayout(0, 9));
        chessBoard.setBorder(new LineBorder(Color.BLACK));

        for (int i = 0; i < tiles.length; i++) {
            for (int j = 0; j < tiles[i].length; j++) {
                JButton bTile = new JButton();
                ImageIcon icon = new ImageIcon(
                        new BufferedImage(64, 64, BufferedImage.TYPE_INT_ARGB));
                bTile.setIcon(icon);
                boolean tWhite;
                if ((i % 2 == 1 && j % 2 == 1) || (i % 2 == 0 && j % 2 == 0)){
                    bTile.setBackground(Color.WHITE);
                    tWhite = true;
                }
                else{
                    bTile.setBackground(Color.BLACK);
                    tWhite = false;
                }

                tiles[j][i] = new Tile(j, i, bTile, tWhite);
            }
        }

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                chessBoard.add(tiles[j][i].getbTile());
            }
        }


    }

    public JPanel getChessBoard() {
        return chessBoard;
    }

    public void setChessBoard(JPanel chessBoard) {
        this.chessBoard = chessBoard;
    }

    private void createUIComponents() {
        JPanel chessBoard2;
    }
}
