import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;


public class Board {
    public static Tile[][] tiles = new Tile[8][8]; // Die Felder werden in einem 8x8-Array gespeichert
    public JPanel chessBoard; // Das Schachbrett mit allen GUI-Elementen. Wird in Main dem JFrame hinzugefügt.

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
                JPanel pTile = new JPanel(new GridBagLayout()); // Erstellt den zugehörigen Button für das Feld

                //pTile.setSize(64,64);
                pTile.setName(String.valueOf(x+y));
                boolean tWhite; // Wird genutzt, um Angabe über Farbe des Feldes zwischenzuspeichern

                /*
                Berechnet, ob das Feld Weiß oder Schwarz sein muss. Wenn die Summe der Koordinaten eine gerade Zahl ist,
                d. h. Rest = 0, dann ist es weiß, ist die Summe der Koordinaten eine ungerade Zahl, d. h. Rest = 1, dann
                ist es schwarz.
                 */
                if ((x + y) % 2 == 0){
                    pTile.setBackground(new Color(255, 206, 158));
                    tWhite = true;
                }
                else{
                    pTile.setBackground(new Color(209,139,71));
                    tWhite = false;
                }

                tiles[x][y] = new Tile(x, y, pTile, tWhite, false); // Erstellt die Felder für das "tiles"-Array

            }
        }

        // Fügt alle Felder des "tiles"-Arrays zum Schachbrett hinzu
        for (int y = 0; y < 8; y++) {
            for (int x = 0; x < 8; x++) {
                chessBoard.add(tiles[x][y].getpTile());
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
            tiles[x][1].setOccupyingPiece(whitePawn);
            JButton pwButton = whitePawn.createPieceButton();
            tiles[x][1].getpTile().add(pwButton);

            // Erstellt schwarze Bauern in der siebten Reihe
            Pawn blackPawn = new Pawn(false, false, tiles[x][6]);
            tiles[x][6].setOccupyingPiece(blackPawn);
            JButton pbButton = blackPawn.createPieceButton();
            tiles[x][6].getpTile().add(pbButton);
        }
    }
}