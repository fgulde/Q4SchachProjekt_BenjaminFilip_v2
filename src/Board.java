import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.image.BufferedImage;

public class Board {
    private final Tile[][] tiles = new Tile[8][8]; // Die Felder werden in einem 8x8-Array gespeichert
    private JPanel chessBoard; // Das Schachbrett mit allen GUI-Elementen. Wird in Main dem JFrame hinzugefügt.

    public Board() {
        createBoard(); // Erstellt Schachbrett mit Feldern
        initializeBoard(); // Füllt Schachbrett mit Figuren
    }

    public void createBoard() {
        chessBoard = new JPanel(new GridLayout(8, 8)); // Erstellt ein Swing JPanel mit Grid-Layout
        chessBoard.setBorder(new LineBorder(Color.BLACK)); // Setzt Randfarbe der GUI

        /*
        Erstellt einzeln alle Felder mit zugehörigem Button. Die äußere for-Schleife fährt von oben nach unten, die
        innere von links nach rechts.
         */
        for (int y = 0; y < tiles.length; y++) {
            for (int x = 0; x < tiles[y].length; x++) {
                JButton bTile = new JButton(); // Erstellt den zugehörigen Button für das Feld
                ImageIcon icon = new ImageIcon(
                        new BufferedImage(64, 64, BufferedImage.TYPE_INT_ARGB)); // Größe des Buttons
                bTile.setIcon(icon);
                boolean tWhite; // Wird genutzt, um Angabe über Farbe des Feldes zwischenzuspeichern

                /*
                Berechnet, ob das Feld Weiß oder Schwarz sein muss. Wenn die Summe der Koordinaten eine gerade Zahl ist,
                d. h. Rest = 0, dann ist es weiß, ist die Summe der Koordinaten eine ungerade Zahl, d. h. Rest = 1, dann
                ist es schwarz.
                 */
                if ((x + y) % 2 == 0){
                    bTile.setBackground(new Color(255, 206, 158));
                    tWhite = true;
                }
                else{
                    bTile.setBackground(new Color(209,139,71));
                    tWhite = false;
                }

                tiles[x][y] = new Tile(x, y, bTile, tWhite); // Erstellt die Felder für das "tiles"-Array
            }
        }

        // Fügt alle Felder des "tiles"-Arrays zum Schachbrett hinzu
        for (int y = 0; y < 8; y++) {
            for (int x = 0; x < 8; x++) {
                chessBoard.add(tiles[x][y].getbTile());
            }
        }
    }

    // Getter vom Schachbrett
    public JPanel getChessBoard() {
        return chessBoard;
    }

    // Füllt das Schachbrett mit den Anfangspositionen aller Figuren
    public void initializeBoard() {
        for (int x = 0; x < 8; x++) {
            // Erstellt weiße Bauern in der zweiten Reihe
            Pawn whitePawn = new Pawn(true, false, tiles[x][1]);
            tiles[x][1].setPiece(whitePawn);
            // Gibt dem Button des Felds ein neues Icon
            ImageIcon whitePawnIcon = new ImageIcon("src/pics/PawnBlack.png");
            tiles[x][1].getbTile().setIcon(whitePawnIcon);

            // Erstellt schwarze Bauern in der siebten Reihe
            Pawn blackPawn = new Pawn(false, false, tiles[x][6]);
            tiles[x][6].setPiece(blackPawn);
            // Gibt dem Button des Felds ein neues Icon
            ImageIcon blackPawnIcon = new ImageIcon("src/pics/PawnWhite.png");
            tiles[x][6].getbTile().setIcon(blackPawnIcon);
        }


    }
}