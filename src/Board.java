import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.image.BufferedImage;

public class Board {
    private final Tile[][] tiles = new Tile[8][8]; // Erstellen Sie ein 8x8-Array von Tiles
    private JPanel chessBoard; // Das JPanel, das das Schachbrett darstellt

    public Board() {
        createBoard(); // Rufen Sie die Methode createBoard auf, um das Schachbrett zu erstellen
        initializeBoard();
    }

    public void createBoard() {
        chessBoard = new JPanel(new GridLayout(8, 8));
        chessBoard.setBorder(new LineBorder(Color.BLACK));

        for (int i = 0; i < tiles.length; i++) {
            for (int j = 0; j < tiles[i].length; j++) {
                JButton bTile = new JButton();
                ImageIcon icon = new ImageIcon(
                        new BufferedImage(64, 64, BufferedImage.TYPE_INT_ARGB));
                bTile.setIcon(icon);
                boolean tWhite;

                // Überprüfen Sie, ob die Summe der Zeilen- und Spaltenindizes gerade oder ungerade ist
                if ((i + j) % 2 == 0){
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
        return chessBoard; // Getter für das Schachbrett
    }

    public void setChessBoard(JPanel chessBoard) {
        this.chessBoard = chessBoard; // Setter für das Schachbrett
    }

    public void initializeBoard() {
        // Fülle das Schachbrett mit den Anfangspositionen aller Figuren
        for (int i = 0; i < 8; i++) {
            // Erstelle weiße Bauern an der zweiten Reihe
            Pawn whitePawn = new Pawn(true,false, tiles[i][1]);
            tiles[i][1].setPiece(whitePawn);
            ImageIcon whitePawnIcon = new ImageIcon("src/pics/whitePawn.png");
            tiles[i][1].getbTile().setIcon(whitePawnIcon);

            // Erstelle schwarze Bauern an der siebten Reihe
            Pawn blackPawn = new Pawn(false,false, tiles[i][6]);
            tiles[i][6].setPiece(blackPawn);
            ImageIcon blackPawnIcon = new ImageIcon("src/pics/blackPawn.png");
            tiles[i][6].getbTile().setIcon(blackPawnIcon);
        }


    }
}