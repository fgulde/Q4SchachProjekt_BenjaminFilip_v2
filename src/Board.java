import javax.sound.sampled.*;
import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.io.File;
import java.io.IOException;


public class Board {
    public JPanel GUI;
    public static Tile[][] tiles = new Tile[8][8]; // Die Felder werden in einem 8x8-Array gespeichert
    public JPanel chessBoard; // Das Schachbrett mit allen GUI-Elementen. Wird in Main dem JFrame hinzugefügt.
    public JButton newGame = new JButton("Neues Spiel"); // Button zum Resetten des Spiels
    public JButton giveUp = new JButton("Aufgeben"); // Button um anderen Spieler gewinnen zu lassen
    public static GameStatus status = GameStatus.READY;
    public static JLabel lStatus = new JLabel(status.toString());

    public Board() {
        createBoard(); // Erstellt Schachbrett mit Feldern
        initializeBoard();
    }

    public void createBoard() {
        chessBoard = new JPanel(new GridLayout(8, 8)); // Erstellt ein Swing JPanel mit Grid-Layout
        chessBoard.setBorder(new LineBorder(Color.BLACK)); // Setzt Randfarbe der GUI

        // Fügt GUI um Schachbrett herum hinzu
        GUI = new JPanel(new BorderLayout(2, 2));
        JToolBar tools = new JToolBar();
        tools.setFloatable(false);
        GUI.add(tools, BorderLayout.PAGE_START);
        tools.add(newGame); // TO-DO - "Spiel zurücksetzen" implementieren
        tools.addSeparator();
        tools.add(giveUp);
        tools.addSeparator();
        tools.add(new JLabel("Status:"));
        tools.addSeparator();
        tools.add(lStatus);
        GUI.add(chessBoard);

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

    private void createAndAddPiece(Piece piece, Tile tile, boolean black) {
        tile.setOccupyingPiece(piece);
        JButton button = piece.createPieceButton();
        if (black){
            button.setEnabled(false);
        }
        tile.getpTile().add(button);
    }


    // Füllt das Schachbrett mit den Anfangspositionen aller Figuren
    public void initializeBoard() {
        String startSfx = "src/sfx/start.wav";
        try {
            AudioInputStream ais = AudioSystem.getAudioInputStream(new File(startSfx));
            Clip clip = AudioSystem.getClip();
            clip.open(ais);
            clip.setFramePosition(0);
            clip.start();
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException ex) {
            throw new RuntimeException(ex);
        }

        for (int x = 0; x < 8; x++) {
            // Erstellt weiße Bauern in der zweiten Reihe
            Pawn whitePawn = new Pawn(true, false, tiles[x][6]);
            createAndAddPiece(whitePawn, tiles[x][6], false);

            // Erstellt schwarze Bauern in der siebten Reihe
            Pawn blackPawn = new Pawn(false, false, tiles[x][1]);
            createAndAddPiece(blackPawn, tiles[x][1], true);
        }
        Bishop whiteBishop1 = new Bishop(true,false,tiles[2][7]);
        createAndAddPiece(whiteBishop1, tiles[2][7], false);
        Bishop whiteBishop2 = new Bishop(true,false,tiles[5][7]);
        createAndAddPiece(whiteBishop2, tiles[5][7], false);
        Bishop blackBishop1 = new Bishop(false,false,tiles[2][0]);
        createAndAddPiece(blackBishop1, tiles[2][0], true);
        Bishop blackBishop2 = new Bishop(false,false,tiles[5][0]);
        createAndAddPiece(blackBishop2, tiles[5][0], true);


        Rook whiteRook1 = new Rook(true,false,tiles[0][7]);
        createAndAddPiece(whiteRook1, tiles[0][7], false);
        Rook whiteRook2 = new Rook(true,false,tiles[7][7]);
        createAndAddPiece(whiteRook2, tiles[7][7], false);
        Rook blackRook1 = new Rook(false,false,tiles[0][0]);
        createAndAddPiece(blackRook1, tiles[0][0], true);
        Rook blackRook2 = new Rook(false,false,tiles[7][0]);
        createAndAddPiece(blackRook2, tiles[7][0], true);


        Knight whiteKnight1 = new Knight(true,false,tiles[1][7]);
        createAndAddPiece(whiteKnight1, tiles[1][7], false);
        Knight whiteKnight2 = new Knight(true,false,tiles[6][7]);
        createAndAddPiece(whiteKnight2, tiles[6][7], false);
        Knight blackKnight1 = new Knight(false,false,tiles[1][0]);
        createAndAddPiece(blackKnight1, tiles[1][0], true);
        Knight blackKnight2 = new Knight(false,false,tiles[6][0]);
        createAndAddPiece(blackKnight2, tiles[6][0], true);


        Queen whiteQueen = new Queen(true, false, tiles[3][7]);
        createAndAddPiece(whiteQueen, tiles[3][7], false);
        Queen blackQueen = new Queen(false, false, tiles[3][0]);
        createAndAddPiece(blackQueen, tiles[3][0], true);


        King whiteKing = new King(true, false, tiles[4][7]);
        createAndAddPiece(whiteKing, tiles[4][7], false);
        King blackKing = new King(false, false, tiles[4][0]);
        createAndAddPiece(blackKing, tiles[4][0], true);

    }

    public static void changeButtonsEnabled(boolean white){
        for (int y = 0; y < 8; y++) {
            for (int x = 0; x < 8; x++) {
                if (tiles[x][y].getOccupyingPiece() != null){
                    if (tiles[x][y].getOccupyingPiece().isWhite() && white) {
                        tiles[x][y].getpTile().getComponent(0).setEnabled(false);
                    } else if (tiles[x][y].getOccupyingPiece().isWhite() && !white) {
                        tiles[x][y].getpTile().getComponent(0).setEnabled(true);
                    } else if (!tiles[x][y].getOccupyingPiece().isWhite() && !white) {
                        tiles[x][y].getpTile().getComponent(0).setEnabled(false);
                    } else if (!tiles[x][y].getOccupyingPiece().isWhite() && white) {
                        tiles[x][y].getpTile().getComponent(0).setEnabled(true);
                    }
                }
            }
        }
    }

    public static Tile[][] getTiles() {
        return tiles;
    }

    // Wird in Main() benötigt
    public JPanel getGUI() {
        return this.GUI;
    }

    public static void setStatus(GameStatus status) {
        Board.status = status;
    }
}