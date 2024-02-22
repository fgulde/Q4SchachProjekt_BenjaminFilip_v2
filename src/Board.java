import javax.sound.sampled.*;
import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;


public class Board {
    public JPanel GUI;
    public JPanel innerGUI; // Die GUI mit den Zahlen und Buchstaben am Rand
    public static Tile[][] tiles = new Tile[8][8]; // Die Felder werden in einem 8x8-Array gespeichert
    public JPanel chessBoard; // Das Schachbrett
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

        innerGUI = new JPanel(new BorderLayout());
        // Letter-Row
        JPanel letterRow = new JPanel(new GridLayout(1, 8));
        innerGUI.add(letterRow, BorderLayout.NORTH);
        // Number-Row
        JPanel numberRow = new JPanel(new GridLayout(8, 1));
        innerGUI.add(numberRow, BorderLayout.WEST);
        // Schachbrett
        innerGUI.add(chessBoard, BorderLayout.CENTER);

        // Erstellt Letter-Row
        for (int c = 97; c < 105; c++) {
            JLabel lLetter = new JLabel((char) c + "  ", SwingConstants.CENTER);
            lLetter.setFont(new Font("Arial", Font.PLAIN, 20));
            letterRow.add(lLetter);
        }
        // Erstellt Number-Row
        for (int i = 8; i > 0; i--) {
            JLabel lNumber = new JLabel(" " + String.valueOf(i) + " ");
            lNumber.setFont(new Font("Arial", Font.PLAIN, 20));
            numberRow.add(lNumber);
        }

        // Fügt GUI um Schachbrett herum hinzu
        GUI = new JPanel(new BorderLayout(2, 2));
        JToolBar tools = new JToolBar();
        tools.setFloatable(false);
        GUI.add(tools, BorderLayout.PAGE_START);
        tools.add(newGame);
        newGame.addActionListener(new NewGameButtonListener());
        tools.addSeparator();
        tools.add(giveUp);
        giveUp.addActionListener(new GiveUpButtonListener());
        tools.addSeparator();
        tools.add(new JLabel("Status:"));
        tools.addSeparator();
        tools.add(lStatus);
        GUI.add(innerGUI);

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

    private static void createAndAddPiece(Piece piece, Tile tile, boolean black) {
        tile.setOccupyingPiece(piece);
        JButton button = piece.createPieceButton();
        if (black){
            button.setEnabled(false);
            button.setDisabledIcon(tiles[tile.getX()][tile.getY()].getOccupyingPiece().getIconPath());
        }
        tile.getpTile().add(button);
    }


    // Füllt das Schachbrett mit den Anfangspositionen aller Figuren
    public static void initializeBoard() {
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
                JButton button = Board.tiles[x][y].getButton();
                if (tiles[x][y].getOccupyingPiece() != null){
                    if (tiles[x][y].getOccupyingPiece().isWhite() && white ||
                            !tiles[x][y].getOccupyingPiece().isWhite() && !white) {
                        button.setEnabled(false);
                        button.setDisabledIcon(tiles[x][y].getOccupyingPiece().getIconPath());
                    } else if (tiles[x][y].getOccupyingPiece().isWhite() && !white ||
                            !tiles[x][y].getOccupyingPiece().isWhite() && white) {
                        button.setEnabled(true);
                    }
                }
            }
        }
    }

    public static void disableAllButtons(){
        for (int y = 0; y < 8; y++) {
            for (int x = 0; x < 8; x++) {
                JButton button = Board.tiles[x][y].getButton();
                if (tiles[x][y].getOccupyingPiece() != null){
                    button.setEnabled(false);
                    button.setDisabledIcon(tiles[x][y].getOccupyingPiece().getIconPath());
                }
            }
        }
    }

    public static class NewGameButtonListener implements ActionListener{
        public NewGameButtonListener(){}
        @Override
        public void actionPerformed(ActionEvent e) {
            Piece.FieldActionListener.NotifySound();
            if (JOptionPane.showConfirmDialog(null, "Bist du dir sicher, dass du ein neues Spiel" +
                " starten willst? \n(Hinweis: Das aktuelle Spiel geht dabei verloren.)",
                "WARNUNG", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE,
                    new ImageIcon("src/pics/Warning.png")) == JOptionPane.YES_OPTION) {
                        //Alles entfernen
                        for (int i = 0; i < 8; i++) {
                            for (int j = 0; j < 8; j++) {
                                for (int k = 0; k < Board.tiles[i][j].getpTile().getComponents().length; k++) {
                                    Board.tiles[i][j].getpTile().remove(k);
                                    Board.tiles[i][j].setOccupyingPiece(null);
                                    Board.tiles[i][j].getpTile().updateUI();
                                }
                            }
                        }
                        //Alles neu erstellen
                        setStatus(GameStatus.WHITEMOVE);
                        Board.lStatus.setText(Board.status.toString());
                        initializeBoard();
            }
        }
    }

    public static class GiveUpButtonListener implements ActionListener{
        public GiveUpButtonListener(){

        }
        @Override
        public void actionPerformed(ActionEvent e) {
            Piece.FieldActionListener.NotifySound();
            if (JOptionPane.showConfirmDialog(null, "Bist du dir sicher dass du aufgeben willst?",
                    "WARNUNG", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE,
                    new ImageIcon("src/pics/Warning.png")) == JOptionPane.YES_OPTION) {
                if (Board.status.equals(GameStatus.WHITEMOVE)){
                    Board.setStatus(GameStatus.BLACKWIN);
                    Board.lStatus.setText(Board.status.toString());
                    Board.disableAllButtons();
                    Piece.FieldActionListener.NotifySound();
                    JOptionPane.showMessageDialog(null, "Schwarz hat das Spiel gewonnen!",
                            "Spielausgang", JOptionPane.INFORMATION_MESSAGE, new ImageIcon("src/pics/BlackWin.png"));
                } else if (Board.status.equals(GameStatus.BLACKMOVE)) {
                    Board.setStatus(GameStatus.WHITEWIN);
                    Board.lStatus.setText(Board.status.toString());
                    Board.disableAllButtons();
                    Piece.FieldActionListener.NotifySound();
                    JOptionPane.showMessageDialog(null, "Weiß hat das Spiel gewonnen!",
                            "Spielausgang", JOptionPane.INFORMATION_MESSAGE, new ImageIcon("src/pics/WhiteWin.png"));
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