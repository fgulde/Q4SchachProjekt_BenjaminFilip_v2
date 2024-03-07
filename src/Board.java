import javax.sound.sampled.*;
import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;


public class Board {
    public JPanel GUI;
    public JPanel innerGUI; // Die GUI mit den Zahlen und Buchstaben am Rand
    public static Tile[][] tiles = new Tile[8][8]; // Die Felder werden in einem 8x8-Array gespeichert
    public JPanel chessBoard; // Das Schachbrett
    public JButton newGame = new JButton("Neues Spiel"); // Button zum Resetten des Spiels
    public JButton giveUp = new JButton("Aufgeben"); // Button um anderen Spieler gewinnen zu lassen
    public JButton save = new JButton("Spiel speichern");
    public JButton load = new JButton("Spiel laden");
    public JButton about = new JButton("Info"); // Button um anderen Spieler gewinnen zu lassen
    public static JTextArea txtA = new JTextArea(35, 20); // Verlaufsfeld
    public static int vCounter = 1;
    public static GameStatus status = GameStatus.WHITEMOVE;
    public static JLabel lStatus = new JLabel(status.toString());

    public Board() {
        createBoard(); // Erstellt Schachbrett mit Feldern
        initializeBoard();
    }

    public void createBoard() {
        chessBoard = new JPanel(new GridLayout(8, 8)); // Erstellt ein Swing JPanel mit Grid-Layout
        chessBoard.setBorder(new LineBorder(Color.BLACK)); // Setzt Randfarbe der GUI

        innerGUI = new JPanel(new GridBagLayout());
        GridBagConstraints gc = new GridBagConstraints();
        //Space-Panel
        JPanel space = new JPanel(new GridLayout(1, 1, 100, 0));
        JLabel lspace = new JLabel("", SwingConstants.CENTER);
        lspace.setFont(new Font("Arial", Font.PLAIN, 20));
        space.add(lspace);
        gc.gridx = 0;
        gc.gridy = 0;
        gc.anchor = GridBagConstraints.FIRST_LINE_START;
        innerGUI.add(space, gc);
        // Letter-Row
        JPanel letterRow = new JPanel(new GridLayout(1, 8, 57, 0));
        gc.gridx = 1;
        gc.anchor = GridBagConstraints.PAGE_START;
        innerGUI.add(letterRow, gc);
        // Number-Row
        JPanel numberRow = new JPanel(new GridLayout(8, 1, 0, 50));
        gc.gridx = 0;
        gc.gridy = 1;
        gc.anchor = GridBagConstraints.LINE_START;
        innerGUI.add(numberRow, gc);

        // Erstellt Letter-Row 1
        for (int c = 97; c < 105; c++) {
            JLabel lLetter = new JLabel((char) c + " ", SwingConstants.CENTER);
            lLetter.setFont(new Font("Arial", Font.PLAIN, 20));
            letterRow.add(lLetter);
        }
        // Erstellt Number-Row 1
        for (int i = 8; i > 0; i--) {
            JLabel lNumber = new JLabel(" " + String.valueOf(i) + " ");
            lNumber.setFont(new Font("Arial", Font.PLAIN, 20));
            numberRow.add(lNumber);
        }

        // Schachbrett
        gc.gridx = 1;
        gc.anchor = GridBagConstraints.CENTER;
        innerGUI.add(chessBoard, gc);

        // Letter-Row 2
        JPanel letterRow1 = new JPanel(new GridLayout(1, 8, 57, 0));
        gc.gridx = 1;
        gc.gridy = 2;
        gc.anchor = GridBagConstraints.PAGE_END;
        innerGUI.add(letterRow1, gc);
        // Number-Row 2
        JPanel numberRow1 = new JPanel(new GridLayout(8, 1, 0, 50));
        gc.gridx = 2;
        gc.gridy = 1;
        gc.anchor = GridBagConstraints.LINE_END;
        innerGUI.add(numberRow1, gc);

        // Erstellt Letter-Row 2
        for (int c = 97; c < 105; c++) {
            JLabel lLetter = new JLabel((char) c + " ", SwingConstants.CENTER);
            lLetter.setFont(new Font("Arial", Font.PLAIN, 20));
            letterRow1.add(lLetter);
        }
        // Erstellt Number-Row 2
        for (int i = 8; i > 0; i--) {
            JLabel lNumber = new JLabel(" " + String.valueOf(i) + "       ");
            lNumber.setFont(new Font("Arial", Font.PLAIN, 20));
            numberRow1.add(lNumber);
        }

        // Verlaufsfeld
        txtA.setEditable(false);

        JScrollPane scr = new JScrollPane(txtA);
        scr.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        JToolBar vLabelBar = new JToolBar();
        vLabelBar.setFloatable(false);
        JLabel vLabel = new JLabel("Verlauf:");
        vLabelBar.add(vLabel);
        vLabelBar.setToolTipText("");

        JPanel pVerlauf = new JPanel(new BorderLayout(2, 2));
        pVerlauf.add(vLabelBar, BorderLayout.NORTH);
        pVerlauf.add(scr, BorderLayout.CENTER);

        gc.gridheight = 2;
        gc.gridx = 4;
        gc.gridy = 0;
        innerGUI.add(pVerlauf, gc);

        //Lücke neben Verlaufsfeld
        JPanel spaceRow = new JPanel(new GridLayout(8, 1, 0, 50));
        gc.gridx = 5;
        gc.gridy = 0;
        gc.anchor = GridBagConstraints.LINE_END;
        innerGUI.add(spaceRow, gc);
        for (int i = 8; i > 0; i--) {
            JLabel lSpace1 = new JLabel("     ");
            lSpace1.setFont(new Font("Arial", Font.PLAIN, 20));
            spaceRow.add(lSpace1);
        }

        // Fügt Toolbar hinzu
        GUI = new JPanel(new BorderLayout(2, 2));
        JToolBar tools = new JToolBar();
        tools.setFloatable(false);
        GUI.add(tools, BorderLayout.PAGE_START);

        tools.add(newGame);
        newGame.addActionListener(new NewGameButtonListener());
        newGame.setToolTipText("Setzt das Spiel auf den Startzustand zurück.");

        tools.addSeparator();
        tools.add(giveUp);
        giveUp.addActionListener(new GiveUpButtonListener());
        giveUp.setToolTipText("Lässt den Gegner gewinnen.");

        tools.addSeparator();
        tools.add(save);
        save.addActionListener(new SaveButtonListener());
        save.setToolTipText("Speichere den aktuellen Spielstand als Textdatei.");

        tools.addSeparator();
        tools.add(load);
        load.addActionListener(new LoadButtonListener());
        load.setToolTipText("Lade einen Spielstand.");

        tools.addSeparator();
        tools.add(new JLabel("Status:"));
        tools.addSeparator();
        tools.add(lStatus);

        tools.addSeparator(new Dimension(400, 5));
        tools.add(about);
        about.addActionListener(new AboutButtonListener());
        about.setToolTipText("Informationen über das Projekt.");

        GUI.add(innerGUI);

        /*
        Erstellt einzeln alle Felder mit zugehörigem Button. Die äußere for-Schleife fährt von oben nach unten, die
        innere von links nach rechts.
         */
        for (int y = 0; y < tiles.length; y++) {
            for (int x = 0; x < tiles[y].length; x++) {
                JPanel pTile = new JPanel(new GridBagLayout());

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
                        vCounter = 1;
                        txtA.setText("");
            }
        }
    }

    public static class GiveUpButtonListener implements ActionListener{
        public GiveUpButtonListener(){

        }
        @Override
        public void actionPerformed(ActionEvent e) {
            Piece.FieldActionListener.NotifySound();
            if (Board.status.equals(GameStatus.WHITEWIN) || Board.status.equals(GameStatus.BLACKWIN)){
                JOptionPane.showMessageDialog(null, "Du hast bereits verloren!\nStarte ein neues " +
                                "Spiel um fortzufahren.", "Fehler", JOptionPane.INFORMATION_MESSAGE, new ImageIcon("src/pics/Warning.png"));
            }
            else if (JOptionPane.showConfirmDialog(null, "Bist du dir sicher dass du aufgeben willst?",
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

    public static class SaveButtonListener implements ActionListener{
        public SaveButtonListener(){

        }
        @Override
        public void actionPerformed(ActionEvent a){
            // Soundeffekt
            Piece.FieldActionListener.NotifySound();

            // Speichert den Spielstand als String durch Stringbuilder
            StringBuilder str = new StringBuilder();
            // Spielstatus, nächster Bereich wird durch "?" gekennzeichnet
            str.append(status).append("?");
            // for-Schleifen fahren das Feld ab und speichern jedes Piece
            for (int y = 0; y < tiles.length; y++) {
                for (int x = 0; x < tiles[y].length; x++) {
                    // Erste zwei chars sind x- und y-Koordinate
                    if (tiles[x][y].getOccupyingPiece() != null){
                        str.append(x).append(y);
                        // Art des Piece wird durch einen eindeutigen char gekennzeichnet
                        switch (tiles[x][y].getOccupyingPiece().getClassName()){
                            case "Läufer":
                                str.append("L");
                                break;
                            case "König":
                                str.append("K");
                                break;
                            case "Springer":
                                str.append("S");
                                break;
                            case "Dame":
                                str.append("D");
                                break;
                            case "Turm":
                                str.append("T");
                                break;
                            case "Bauer":
                                str.append("B");
                                break;
                        }
                        // Attribut "white"
                        str.append(tiles[x][y].getOccupyingPiece().isWhite() ? "1" : "0");
                        // Attribut "moved"
                        str.append(tiles[x][y].getOccupyingPiece().isMoved() ? "1" : "0");
                        // Attribut castled bei King
                        if (Objects.equals(tiles[x][y].getOccupyingPiece().getClassName(), "König")){
                            King k = (King) tiles[x][y].getOccupyingPiece();
                            str.append(k.isCastled() ? "0" : "1");
                        }
                        // Attribut enPassant bei Pawn
                        if (Objects.equals(tiles[x][y].getOccupyingPiece().getClassName(), "Bauer")){
                            Pawn p = (Pawn) tiles[x][y].getOccupyingPiece();
                            str.append(p.isEnPassant() ? "0" : "1");
                        }
                        // Ende der Informationen über ein Piece wird durch ";" gekennzeichnet
                        str.append(";");
                    }
                }
            }

            /* Nächster Bereich (Verlaufsfeldinhalt) wird durch "#" gekennzeichnet, nächster Bereich (vCounter) wird
            durch "~" gekennzeichnet*/
            str.append("#").append(txtA.getText()).append("~").append(vCounter);

            String saveFile = str.toString();

            // File-Dialog
            JFileChooser fileChooser = new JFileChooser();
            int result = fileChooser.showSaveDialog(null);
            if (result == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();

                // Stellt sicher, dass die Datei mit .txt endet
                String filePath = selectedFile.getAbsolutePath();
                if (!filePath.endsWith(".txt")) {
                    filePath += ".txt";
                    selectedFile = new File(filePath);
                }

                try {
                    // Schreibt den String als File
                    PrintWriter writer = new PrintWriter(selectedFile);
                    writer.print(saveFile);
                    writer.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static class LoadButtonListener implements ActionListener{
        public LoadButtonListener(){

        }
        @Override
        public void actionPerformed(ActionEvent e){
            Piece.FieldActionListener.NotifySound();

            // Lädt die Textdatei
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Wähle eine .txt Datei");
            int result = fileChooser.showOpenDialog(null);
            String fileContent = "";
            if (result == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                String filePath = selectedFile.getAbsolutePath();
                try {
                    fileContent = Files.readString(Path.of(filePath), StandardCharsets.UTF_8);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }

                // Alles entfernen
                for (int i = 0; i < 8; i++) {
                    for (int j = 0; j < 8; j++) {
                        for (int k = 0; k < Board.tiles[i][j].getpTile().getComponents().length; k++) {
                            Board.tiles[i][j].getpTile().remove(k);
                            Board.tiles[i][j].setOccupyingPiece(null);
                            Board.tiles[i][j].getpTile().updateUI();
                        }
                    }
                }

                // Pieces ablesen und erstellen
                int sStart = fileContent.indexOf('?');
                int sEnd = fileContent.indexOf('#');

                String sSubstring = fileContent.substring(sStart + 1, sEnd);
                String[] sParts = sSubstring.split(";");

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

                // Zählt wie viele Pieces es von einer Sorte bereits gibt
                int bishopCounter = 0;
                int kingCounter = 0;
                int knightCounter = 0;
                int queenCounter = 0;
                int rookCounter = 0;
                int pawnCounter = 0;

                // Erstellt die einzelnen Pieces
                for (String sPart : sParts) {
                    char cX = sPart.charAt(0);
                    char cY = sPart.charAt(1);
                    char cPiece = sPart.charAt(2);
                    char cW = sPart.charAt(3);
                    char cM = sPart.charAt(4);
                    char cE = 0;
                    if (!(sPart.charAt(5) == 0)){
                        cE = sPart.charAt(5);
                    }

                    int x = cX - '0';
                    int y = cY - '0';
                    int cWhite = cW - '0';
                    int cMoved = cM - '0';
                    int cExtra = cE - '0';

                    /*Der erste Switch checkt, was für ein Piece gewollt ist. Jedoch sind ein zweiter Switch sowie die
                    Piece-Counter erforderlich, da jedes Objekt anders heißen muss.*/
                    pSw : switch (cPiece){
                        case 'L':
                            switch (bishopCounter){
                                case 0:
                                    Bishop bishop1 = new Bishop((cWhite == 1),false,tiles[x][y]);
                                    createAndAddPiece(bishop1, tiles[x][y], (cWhite == 0));
                                    bishopCounter++;
                                    break pSw;
                                case 1:
                                    Bishop bishop2 = new Bishop((cWhite == 1),false,tiles[x][y]);
                                    createAndAddPiece(bishop2, tiles[x][y], (cWhite == 0));
                                    bishopCounter++;
                                    break pSw;
                                case 2:
                                    Bishop bishop3 = new Bishop((cWhite == 1),false,tiles[x][y]);
                                    createAndAddPiece(bishop3, tiles[x][y], (cWhite == 0));
                                    bishopCounter++;
                                    break pSw;
                                case 3:
                                    Bishop bishop4 = new Bishop((cWhite == 1),false,tiles[x][y]);
                                    createAndAddPiece(bishop4, tiles[x][y], (cWhite == 0));
                                    break pSw;
                            }
                        case 'K':
                            switch (kingCounter){
                                case 0:
                                    King king1 = new King((cWhite == 1),false,tiles[x][y]);
                                    createAndAddPiece(king1, tiles[x][y], (cWhite == 0));
                                    king1.setMoved((cMoved == 1));
                                    king1.setCastled((cExtra == 1));
                                    kingCounter++;
                                    break pSw;
                                case 1:
                                    King king2 = new King((cWhite == 1),false,tiles[x][y]);
                                    createAndAddPiece(king2, tiles[x][y], (cWhite == 0));
                                    king2.setCastled((cExtra == 1));
                                    king2.setMoved((cMoved == 1));
                                    break pSw;
                            }
                        case 'S':
                            switch (knightCounter){
                                case 0:
                                    Knight knight1 = new Knight((cWhite == 1),false,tiles[x][y]);
                                    createAndAddPiece(knight1, tiles[x][y], (cWhite == 0));
                                    knightCounter++;
                                    break pSw;
                                case 1:
                                    Knight knight2 = new Knight((cWhite == 1),false,tiles[x][y]);
                                    createAndAddPiece(knight2, tiles[x][y], (cWhite == 0));
                                    knightCounter++;
                                    break pSw;
                                case 2:
                                    Knight knight3 = new Knight((cWhite == 1),false,tiles[x][y]);
                                    createAndAddPiece(knight3, tiles[x][y], (cWhite == 0));
                                    knightCounter++;
                                    break pSw;
                                case 3:
                                    Knight knight4 = new Knight((cWhite == 1),false,tiles[x][y]);
                                    createAndAddPiece(knight4, tiles[x][y], (cWhite == 0));
                                    break pSw;
                            }
                        case 'D':
                            switch (queenCounter){
                                case 0:
                                    Queen queen1 = new Queen((cWhite == 1),false,tiles[x][y]);
                                    createAndAddPiece(queen1, tiles[x][y], (cWhite == 0));
                                    queenCounter++;
                                    break pSw;
                                case 1:
                                    Queen queen2 = new Queen((cWhite == 1),false,tiles[x][y]);
                                    createAndAddPiece(queen2, tiles[x][y], (cWhite == 0));
                                    break pSw;
                            }
                        case 'T':
                            switch (rookCounter){
                                case 0:
                                    Rook rook1 = new Rook((cWhite == 1),false,tiles[x][y]);
                                    createAndAddPiece(rook1, tiles[x][y], (cWhite == 0));
                                    rookCounter++;
                                    break pSw;
                                case 1:
                                    Rook rook2 = new Rook((cWhite == 1),false,tiles[x][y]);
                                    createAndAddPiece(rook2, tiles[x][y], (cWhite == 0));
                                    rookCounter++;
                                    break pSw;
                                case 2:
                                    Rook rook3 = new Rook((cWhite == 1),false,tiles[x][y]);
                                    createAndAddPiece(rook3, tiles[x][y], (cWhite == 0));
                                    rookCounter++;
                                    break pSw;
                                case 3:
                                    Rook rook4 = new Rook((cWhite == 1),false,tiles[x][y]);
                                    createAndAddPiece(rook4, tiles[x][y], (cWhite == 0));
                                    break pSw;
                            }
                        case 'B':
                            switch (pawnCounter){
                                case 0:
                                    Pawn pawn1 = new Pawn((cWhite == 1),false,tiles[x][y]);
                                    createAndAddPiece(pawn1, tiles[x][y], (cWhite == 0));
                                    pawn1.setMoved((cMoved == 1));
                                    pawn1.setEnPassant((cExtra == 1));
                                    pawnCounter++;
                                    break pSw;
                                case 1:
                                    Pawn pawn2 = new Pawn((cWhite == 1),false,tiles[x][y]);
                                    createAndAddPiece(pawn2, tiles[x][y], (cWhite == 0));
                                    pawn2.setMoved((cMoved == 1));
                                    pawn2.setEnPassant((cExtra == 1));
                                    pawnCounter++;
                                    break pSw;
                                case 2:
                                    Pawn pawn3 = new Pawn((cWhite == 1),false,tiles[x][y]);
                                    createAndAddPiece(pawn3, tiles[x][y], (cWhite == 0));
                                    pawn3.setMoved((cMoved == 1));
                                    pawn3.setEnPassant((cExtra == 1));
                                    pawnCounter++;
                                    break pSw;
                                case 3:
                                    Pawn pawn4 = new Pawn((cWhite == 1),false,tiles[x][y]);
                                    createAndAddPiece(pawn4, tiles[x][y], (cWhite == 0));
                                    pawn4.setMoved((cMoved == 1));
                                    pawn4.setEnPassant((cExtra == 1));
                                    pawnCounter++;
                                    break pSw;
                                case 4:
                                    Pawn pawn5 = new Pawn((cWhite == 1),false,tiles[x][y]);
                                    createAndAddPiece(pawn5, tiles[x][y], (cWhite == 0));
                                    pawn5.setMoved((cMoved == 1));
                                    pawn5.setEnPassant((cExtra == 1));
                                    pawnCounter++;
                                    break pSw;
                                case 5:
                                    Pawn pawn6 = new Pawn((cWhite == 1),false,tiles[x][y]);
                                    createAndAddPiece(pawn6, tiles[x][y], (cWhite == 0));
                                    pawn6.setMoved((cMoved == 1));
                                    pawn6.setEnPassant((cExtra == 1));
                                    pawnCounter++;
                                    break pSw;
                                case 6:
                                    Pawn pawn7 = new Pawn((cWhite == 1),false,tiles[x][y]);
                                    createAndAddPiece(pawn7, tiles[x][y], (cWhite == 0));
                                    pawn7.setMoved((cMoved == 1));
                                    pawn7.setEnPassant((cExtra == 1));
                                    pawnCounter++;
                                    break pSw;
                                case 7:
                                    Pawn pawn8 = new Pawn((cWhite == 1),false,tiles[x][y]);
                                    createAndAddPiece(pawn8, tiles[x][y], (cWhite == 0));
                                    pawn8.setMoved((cMoved == 1));
                                    pawn8.setEnPassant((cExtra == 1));
                                    pawnCounter++;
                                    break pSw;
                                case 8:
                                    Pawn pawn9 = new Pawn((cWhite == 1),false,tiles[x][y]);
                                    createAndAddPiece(pawn9, tiles[x][y], (cWhite == 0));
                                    pawn9.setMoved((cMoved == 1));
                                    pawn9.setEnPassant((cExtra == 1));
                                    pawnCounter++;
                                    break pSw;
                                case 9:
                                    Pawn pawn10 = new Pawn((cWhite == 1),false,tiles[x][y]);
                                    createAndAddPiece(pawn10, tiles[x][y], (cWhite == 0));
                                    pawn10.setMoved((cMoved == 1));
                                    pawn10.setEnPassant((cExtra == 1));
                                    pawnCounter++;
                                    break pSw;
                                case 10:
                                    Pawn pawn11 = new Pawn((cWhite == 1),false,tiles[x][y]);
                                    createAndAddPiece(pawn11, tiles[x][y], (cWhite == 0));
                                    pawn11.setMoved((cMoved == 1));
                                    pawn11.setEnPassant((cExtra == 1));
                                    pawnCounter++;
                                    break pSw;
                                case 11:
                                    Pawn pawn12 = new Pawn((cWhite == 1),false,tiles[x][y]);
                                    createAndAddPiece(pawn12, tiles[x][y], (cWhite == 0));
                                    pawn12.setMoved((cMoved == 1));
                                    pawn12.setEnPassant((cExtra == 1));
                                    pawnCounter++;
                                    break pSw;
                                case 12:
                                    Pawn pawn13 = new Pawn((cWhite == 1),false,tiles[x][y]);
                                    createAndAddPiece(pawn13, tiles[x][y], (cWhite == 0));
                                    pawn13.setMoved((cMoved == 1));
                                    pawn13.setEnPassant((cExtra == 1));
                                    pawnCounter++;
                                    break pSw;
                                case 13:
                                    Pawn pawn14 = new Pawn((cWhite == 1),false,tiles[x][y]);
                                    createAndAddPiece(pawn14, tiles[x][y], (cWhite == 0));
                                    pawn14.setMoved((cMoved == 1));
                                    pawn14.setEnPassant((cExtra == 1));
                                    pawnCounter++;
                                    break pSw;
                                case 14:
                                    Pawn pawn15 = new Pawn((cWhite == 1),false,tiles[x][y]);
                                    createAndAddPiece(pawn15, tiles[x][y], (cWhite == 0));
                                    pawn15.setMoved((cMoved == 1));
                                    pawn15.setEnPassant((cExtra == 1));
                                    pawnCounter++;
                                    break pSw;
                                case 15:
                                    Pawn pawn16 = new Pawn((cWhite == 1),false,tiles[x][y]);
                                    createAndAddPiece(pawn16, tiles[x][y], (cWhite == 0));
                                    pawn16.setMoved((cMoved == 1));
                                    pawn16.setEnPassant((cExtra == 1));
                                    break pSw;
                            }
                    }
                }

                // Lade den Verlauf
                String afterHash = fileContent.substring(fileContent.indexOf("#") + 1);
                String rTxtA = afterHash.substring(0, afterHash.indexOf("~"));
                txtA.setText(rTxtA);

                // Aktualisiere vCounter
                String vcIntegerPart = fileContent.substring(fileContent.indexOf("~") + 1);
                vCounter = Integer.parseInt(vcIntegerPart);


                // Status ablesen und aktualisieren
                char[] cStatus = fileContent.chars().mapToObj(i -> (char) i).takeWhile(c -> c != '?').map(String::valueOf)
                        .collect(Collectors.joining()).toCharArray();
                String sStatus = new String(cStatus);
                setStatus(GameStatus.valueOf(sStatus));
                Board.lStatus.setText(Board.status.toString());

                // Status wirksam machen
                if (status.equals(GameStatus.BLACKMOVE)){
                    changeButtonsEnabled(true);
                } else if (status.equals(GameStatus.WHITEWIN) || status.equals(GameStatus.BLACKWIN)) {
                    Board.disableAllButtons();
                }
            }
        }
    }

    public static class AboutButtonListener implements ActionListener{
        public AboutButtonListener(){}
        @Override
        public void actionPerformed(ActionEvent e){
            Piece.FieldActionListener.NotifySound();
            JOptionPane.showMessageDialog(null, "Dieses Schachspiel ist ein Gemeinschaftsprojekt\n" +
                            "von Benjamin Dembinski und Filip Gulde.\nEs wurde für den Informatikunterricht des 4. Semesters\n" +
                            "an der kath. Theresienschule Berlin programmiert.\n\nMedienquellen" +
                            "\n\nSchachfiguren: commons.wikimedia.org, modifiziert\n"
                            + "Icons: flaticon.com, modifiziert\nSoundeffekte: chess.com",
                    "Über das Projekt", JOptionPane.PLAIN_MESSAGE, new ImageIcon(new ImageIcon("src/pics/chess.png")
                            .getImage().getScaledInstance(48, 48, Image.SCALE_DEFAULT)));
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