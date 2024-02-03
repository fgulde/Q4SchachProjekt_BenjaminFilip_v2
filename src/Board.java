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
        Bishop whiteBishop1 = new Bishop(true,false,tiles[2][7]);
        tiles[2][7].setOccupyingPiece(whiteBishop1);
        JButton bwButton1 = whiteBishop1.createPieceButton();
        tiles[2][7].getpTile().add(bwButton1);

        Bishop whiteBishop2 = new Bishop(true,false,tiles[5][7]);
        tiles[5][7].setOccupyingPiece(whiteBishop2);
        JButton bwButton2 = whiteBishop2.createPieceButton();
        tiles[5][7].getpTile().add(bwButton2);

        Bishop blackBishop1 = new Bishop(false,false,tiles[2][0]);
        tiles[2][0].setOccupyingPiece(blackBishop1);
        JButton bbButton1 = blackBishop1.createPieceButton();
        tiles[2][0].getpTile().add(bbButton1);

        Bishop blackBishop2 = new Bishop(false,false,tiles[5][0]);
        tiles[5][0].setOccupyingPiece(blackBishop2);
        JButton bbButton2 = blackBishop2.createPieceButton();
        tiles[5][0].getpTile().add(bbButton2);


        Rook whiteRook1 = new Rook(true,false,tiles[0][7]);
        tiles[0][7].setOccupyingPiece(whiteRook1);
        JButton rwButton1 = whiteRook1.createPieceButton();
        tiles[0][7].getpTile().add(rwButton1);

        Rook whiteRook2 = new Rook(true,false,tiles[7][7]);
        tiles[7][7].setOccupyingPiece(whiteRook2);
        JButton rwButton2 = whiteRook2.createPieceButton();
        tiles[7][7].getpTile().add(rwButton2);

        Rook blackRook1 = new Rook(false,false,tiles[0][0]);
        tiles[0][0].setOccupyingPiece(blackRook1);
        JButton rbButton1 = blackRook1.createPieceButton();
        tiles[0][0].getpTile().add(rbButton1);

        Rook blackRook2 = new Rook(false,false,tiles[7][0]);
        tiles[7][0].setOccupyingPiece(blackRook2);
        JButton rbButton2 = blackRook2.createPieceButton();
        tiles[7][0].getpTile().add(rbButton2);


        Knight whiteKnight1 = new Knight(true,false,tiles[1][7]);
        tiles[1][7].setOccupyingPiece(whiteKnight1);
        JButton kwButton1 = whiteKnight1.createPieceButton();
        tiles[1][7].getpTile().add(kwButton1);

        Knight whiteKnight2 = new Knight(true,false,tiles[6][7]);
        tiles[6][7].setOccupyingPiece(whiteKnight2);
        JButton kwButton2 = whiteKnight2.createPieceButton();
        tiles[6][7].getpTile().add(kwButton2);

        Knight blackKnight1 = new Knight(false,false,tiles[1][0]);
        tiles[1][0].setOccupyingPiece(blackKnight1);
        JButton kbButton1 = blackKnight1.createPieceButton();
        tiles[1][0].getpTile().add(kbButton1);

        Knight blackKnight2 = new Knight(false,false,tiles[6][0]);
        tiles[6][0].setOccupyingPiece(blackKnight2);
        JButton kbButton2 = blackKnight2.createPieceButton();
        tiles[6][0].getpTile().add(kbButton2);


        Queen whiteQueen = new Queen(true, false, tiles[3][7]);
        tiles[3][7].setOccupyingPiece(whiteQueen);
        JButton qwButton = whiteQueen.createPieceButton();
        tiles[3][7].getpTile().add(qwButton);

        // Erstellt schwarze Bauern in der siebten Reihe
        Queen blackQueen = new Queen(false, false, tiles[3][0]);
        tiles[3][0].setOccupyingPiece(blackQueen);
        JButton qbButton = blackQueen.createPieceButton();
        tiles[3][0].getpTile().add(qbButton);


        King whiteKing = new King(true, false, tiles[4][7]);
        tiles[4][7].setOccupyingPiece(whiteKing);
        JButton kwButton = whiteKing.createPieceButton();
        tiles[4][7].getpTile().add(kwButton);

        // Erstellt schwarze Bauern in der siebten Reihe
        King blackKing = new King(false, false, tiles[4][0]);
        tiles[4][0].setOccupyingPiece(blackKing);
        JButton kbButton = blackKing.createPieceButton();
        tiles[4][0].getpTile().add(kbButton);
    }

    public static Tile[][] getTiles() {
        return tiles;
    }
}