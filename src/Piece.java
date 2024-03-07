import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

public abstract class Piece {
    private final boolean white; // gibt an, ob die Figur weiß ist oder nicht
    public Tile position; // tile auf dem sich die Figur befindet
    private boolean moved = false; // boolean gibt an, ob die Figur schon bewegt wurde
    public static Piece[] tempPieces; // Zwischenspeicher für die kill-Methode


    public Piece(boolean white, Tile position) {
        this.white = white;
        this.position = position;
        tempPieces = new Piece[0];  // Füge diese Zeile hinzu, um das Array zu initialisieren.
    }

    // getter für das tile auf dem sich die Figur befindet
    public Tile getPosition() {
        return this.position;
    }

    // setter für das tile der Figur
    public void setPosition(Tile position) {
        this.position = position;
    }

    // getter für weiß oder schwarz
    public boolean isWhite() {
        return this.white;
    }

    // abstract class spezifische methode fürs Berechnen der möglichen neuen Positionen
    public abstract void calculateNewPos();


    // Methode, die die Figur auf ein neues Feld bewegt
    public void move(int newX, int newY){
        // fügt move in den Verlauf ein
        Board.txtA.append(Board.vCounter + ". " + (isWhite() ? "Weiß, " : "Schwarz, ") + getClassName() + ": "
                + (char)(97+position.getX()) + (8-position.getY()) + " -> " + (char)(97+newX) + (8-newY) + "\n");
        Board.vCounter++;

        // ändert EnPassant boolean des Bauern zu false, wenn dieselbe Farbe wieder am Zug ist
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++){
                if (Board.tiles[i][j].getOccupyingPiece() instanceof Pawn pawn){
                    if (pawn.isWhite() == Board.tiles[position.getX()][position.getY()].getOccupyingPiece().isWhite()){
                        pawn.setEnPassant(false);
                    }
                }
            }
        }

        /*
            checkt ob der Bauer zwei oder ein Feld nach vorne gegangen ist und setzt ihn, wenn ja, auf EnPassant-able
            isDefaultCapable() == false, wird zum Kennzeichnen von Buttons benutzt, die die EnPassant Regel aktivieren
         */
        if (!Board.tiles[newX][newY].getButton().isDefaultCapable() &&
                Board.tiles[position.getX()][position.getY()].getOccupyingPiece() instanceof Pawn pawn) {
            pawn.setEnPassant(true);
        } else if (Board.tiles[newX][newY].getButton().isDefaultCapable() &&
                Board.tiles[position.getX()][position.getY()].getOccupyingPiece() instanceof Pawn pawn) {
            pawn.setEnPassant(false);
        }

        Board.tiles[position.getX()][position.getY()].getpTile().remove(0);
        getPosition().setOccupied(false); // entfernt die zu bewegende Figur auf ihrem originellen Feld
        Board.tiles[position.getX()][position.getY()].getpTile().updateUI(); // Updatet die GUI
        Tile newTile = Board.tiles[newX][newY]; // Zielposition

        // prüft, ob der König geschlagen wurde und wenn ja, wer gewonnen hat
        if (Board.tiles[newTile.getX()][newTile.getY()].getOccupyingPiece() != null &&
                Board.tiles[newTile.getX()][newTile.getY()].getOccupyingPiece().getClass().equals(King.class)){
            if (Board.tiles[newTile.getX()][newTile.getY()].getOccupyingPiece().isWhite()){
                Board.setStatus(GameStatus.BLACKWIN);
            } else {
                Board.setStatus(GameStatus.WHITEWIN);
            }
            Board.lStatus.setText(Board.status.toString());
        }

        // generiert die Figur an ihrer neuen Position
        newTile.setOccupied(true);
        newTile.setOccupyingPiece(this);
        JButton pwButton = createPieceButton();
         // leert das Feld, falls eine andere Figur schon darauf ist AKA die zu bewegende Figur killt diese Figur
        Board.tiles[newTile.getX()][newTile.getY()].getpTile().remove(0);
        Board.tiles[newTile.getX()][newTile.getY()].getpTile().add(pwButton);
        position.setOccupied(false);
        position.setOccupyingPiece(null);
        setPosition(newTile);


        // Überprüft, ob der Bauer die gegnerische Grundreihe erreicht hat
        checkPromotion();

        moved = true;
    }

    // Methode, die beim castlen ausgeführt wird. Bewegt den richtigen Turm an die Stelle, an der er nach dem castlen sein müsste
    public static void pullRook(Tile destTile){
        if (7 - destTile.getX() < 3) { // bewegt den rechten Turm, wenn der näher am König dran ist
            Rook rook = (Rook) Board.tiles[7][destTile.getY()].getOccupyingPiece();
            rook.move(destTile.getX(), destTile.getY());
        } else if (0 < destTile.getX()) { // bewegt den linken Turm, wenn der näher am König ist
            Rook rook = (Rook) Board.tiles[0][destTile.getY()].getOccupyingPiece();
            rook.move(destTile.getX(), destTile.getY());
        }
    }

    // Prüft, ob der König nach rechts castlen kann
    private static void checkCastleR(Piece piece) {
        if (piece instanceof King king && Board.tiles[7][piece.getPosition().getY()].getOccupyingPiece() instanceof Rook) {
            // Find the rook position for castling
            Tile rookTile = Board.tiles[7][piece.getPosition().getY()];
            king.castle(rookTile);
        }
    }

    // Prüft, ob der König nach links castlen kann
    private static void checkCastleL(Piece piece) {
        if (piece instanceof King king && Board.tiles[0][piece.getPosition().getY()].getOccupyingPiece() instanceof Rook) {
            //Find the rook position for castling
            Tile rookTile = Board.tiles[0][piece.getPosition().getY()];
            king.castle(rookTile);

        }
    }

    // Methode zur Überprüfung, ob der Bauer die gegnerische Grundreihe erreicht hat
    private void checkPromotion() {
        if (this instanceof Pawn) {
            int yPosition = getPosition().getY();
            if ((yPosition == 0 && isWhite()) || (yPosition == 7 && !isWhite())) {
                // Der Bauer hat die gegnerische Grundreihe erreicht
                ((Pawn) this).promote();
            }
        }
    }

    // Erzeugt eine Figur als Button
    public JButton createPieceButton() {
        JButton button = new JButton();
        button.addActionListener(new PieceActionListener(this));
        button.setMargin(new Insets(0, 0, 0, 0));
        button.setIcon(getIconPath());
        button.setOpaque(false);
        button.setContentAreaFilled(false);
        button.setToolTipText(getClassName());
        return button;
    }

    // Erzeugt einen Button, der anzeigt, wohin dich die Figur alles bewegen kann
    public JButton createFieldButton(Tile position) {
        JButton button = new JButton();
        button.addActionListener(new FieldActionListener(position, this));
        button.setMargin(new Insets(0, 0, 0, 0));
        button.setOpaque(false);
        button.setContentAreaFilled(false);
        button.setIcon(new ImageIcon("src/pics/Target.png"));
        button.setSelected(true); // markiert den Button als nicht PieceButton
        button.setToolTipText("Bewege die Figur hierher.");
        return button;
    }

    // Erzeugt einen Button, der anzeigt, dass die Figur eine andere Figur schlagen kann
    public JButton createKillButton(Tile position) {
        JButton button = new JButton();
        button.addActionListener(new FieldActionListener(position, this));
        button.setMargin(new Insets(0, 0, 0, 0));
        button.setOpaque(false);
        button.setContentAreaFilled(false);
        button.setIcon(new ImageIcon("src/pics/KillTarget.png"));
        button.setSelected(true);
        button.setToolTipText("Schlage diese Figur.");
        return button;
    }

    // Erzeugt einen Button, der anzeigt, dass die Figur(König) castlen kann
    public JButton createCastleButton(Tile position) {
        JButton button = new JButton();
        button.addActionListener(new FieldActionListener(position, this));
        button.setMargin(new Insets(0, 0, 0, 0));
        button.setOpaque(false);
        button.setContentAreaFilled(false);
        button.setIcon(new ImageIcon("src/pics/Castling.png"));
        button.setSelected(true);
        button.setRolloverEnabled(false); // markiert den Button als castle-Button
        button.setToolTipText("Rochade ausführen.");
        return button;
    }

    // Erzeugt einen Button, der anzeigt, dass die Figur (Bauer) EnPassant ausführen kann
    public JButton createEnPassantButton(Tile position) {
        JButton button = new JButton();
        button.addActionListener(new FieldActionListener(position, this));
        button.setMargin(new Insets(0, 0, 0, 0));
        button.setOpaque(false);
        button.setContentAreaFilled(false);
        button.setSelected(true);
        button.setToolTipText("EnPassant ausführen.");
        button.setFocusPainted(false); // markiert den Button als EnPassant-Button
        return button;
    }

    // abstrakte getter für die Dateipfade der dazugehörigen Bilder der Figur
    protected abstract ImageIcon getIconPath();
    protected abstract ImageIcon getKillIconPath(boolean white);

    // ActionListener für, wenn Figuren gedrückt werden
    public static class PieceActionListener implements ActionListener {
        private final Piece piece;

        public PieceActionListener(Piece piece) {
            this.piece = piece;
        }


        @Override
        public void actionPerformed(ActionEvent e) {
            // Soundeffekt
            String clickSfx = "src/sfx/click.wav";
            try {
                AudioInputStream ais = AudioSystem.getAudioInputStream(new File(clickSfx));
                Clip clip = AudioSystem.getClip();
                clip.open(ais);
                clip.setFramePosition(0);
                clip.start();
            } catch (UnsupportedAudioFileException | IOException | LineUnavailableException ex) {
                throw new RuntimeException(ex);
            }
            // entfernt alle Buttons, die nur temporär sind, also keine Figuren und fügt gegebenenfalls durch killButtons entfernte Figuren wieder ein
            resetTempPieces(piece.getPosition());
            removeFieldButtons();

            // führt die checkEnPassant()-Methode aus, falls die Figur ein Bauer ist
            if (piece instanceof Pawn pawn) {
                pawn.checkEnPassant(piece.getPosition().getX(), piece.getPosition().getY());
            }

            // ändert alle nicht zu bewegen erlaubten Figuren zu nichtansteuerbar
            if (piece.isWhite() && Board.status.equals(GameStatus.WHITEMOVE)){
                Board.changeButtonsEnabled(false);
            }else if (!piece.isWhite() && Board.status.equals(GameStatus.BLACKMOVE)){
                Board.changeButtonsEnabled(true);
            }

            // ruft die Methode auf, die alle möglichen neuen Positionen der Figur errechnet
            piece.calculateNewPos();
            // checkt, ob die Figur castlen kann
            checkCastleR(piece);
            checkCastleL(piece);


        }
    }

    //ActionListener für Buttons zum Bewegen der Figur
    public static class FieldActionListener implements ActionListener {
        private final Tile newTile;
        private final Piece piece;

        public FieldActionListener(Tile newTile, Piece piece) {
            this.newTile = newTile;
            this.piece = piece;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            //Soundeffekt
            String moveSfx = "src/sfx/move.wav";
            try {
                AudioInputStream ais = AudioSystem.getAudioInputStream(new File(moveSfx));
                Clip clip = AudioSystem.getClip();
                clip.open(ais);
                clip.setFramePosition(0);
                clip.start();
            } catch (UnsupportedAudioFileException | IOException | LineUnavailableException ex) {
                throw new RuntimeException(ex);
            }

            /*
                Führt das castlen aus, wenn der gedrückte Button ein CastleButton ist
                isRolloverEnabled() == false, wird zum Kennzeichnen von CastleButtons genutzt
             */
            if (piece.getPosition().isOccupied()) {
                if (piece instanceof King && ((King) piece).isCastled() && !newTile.getButton().isRolloverEnabled() && !piece.isMoved()) {
                    if (newTile.getX() > piece.getPosition().getX()) {
                        Tile destTile = Board.tiles[newTile.getX() - 1][newTile.getY()];
                        pullRook(destTile);
                    } else if (newTile.getX() < piece.getPosition().getX()) {
                        Tile destTile = Board.tiles[newTile.getX() + 1][newTile.getY()];
                        pullRook(destTile);
                    }

                }

                // führt EnPassant aus, wenn es ein EnPassant-Button ist
                if (!newTile.getButton().isFocusPainted()) {
                    int y = newTile.getY() + (piece.isWhite() ? 1 : -1);
                    Board.tiles[newTile.getX()][y].getpTile().remove(0);
                    Board.tiles[newTile.getX()][y].setOccupyingPiece(null);
                    Board.tiles[newTile.getX()][y].getpTile().updateUI();
                }

                // bewegt die Figur
                piece.move(newTile.getX(), newTile.getY());
                // entfernt alle Buttons, die nur temporär sind, also keine Figuren und fügt gegebenenfalls durch killButtons entfernte Figuren wieder ein
                resetTempPieces(piece.getPosition());
                removeFieldButtons();

            }
            // Macht neuen Spielstatus wirksam (gibt ggf. Ausgabe bei Spielende)
            if (Board.status.equals(GameStatus.WHITEMOVE)) {
                Board.changeButtonsEnabled(true);
                Board.setStatus(GameStatus.BLACKMOVE);
            } else if (Board.status.equals(GameStatus.BLACKMOVE)) {
                Board.changeButtonsEnabled(false);
                Board.setStatus(GameStatus.WHITEMOVE);
            } else if (Board.status.equals(GameStatus.WHITEWIN)) {
                Board.disableAllButtons();
                NotifySound();
                JOptionPane.showMessageDialog(null, "Weiß hat das Spiel gewonnen!", "Spielausgang",
                        JOptionPane.INFORMATION_MESSAGE, new ImageIcon("src/pics/WhiteWin.png"));
            } else if (Board.status.equals(GameStatus.BLACKWIN)) {
                Board.disableAllButtons();
                NotifySound();
                JOptionPane.showMessageDialog(null, "Schwarz hat das Spiel gewonnen!", "Spielausgang",
                        JOptionPane.INFORMATION_MESSAGE, new ImageIcon("src/pics/BlackWin.png"));
            }
            // Ändert Anzeige von Status
            Board.lStatus.setText(Board.status.toString());
        }

        // Soundeffekt beim Erstellen von GUIs. Wird so oft genutzt, dass sich eigene Methode anbot.
        public static void NotifySound() {
            String notifySfx = "src/sfx/notify.wav";
            try {
                AudioInputStream ais = AudioSystem.getAudioInputStream(new File(notifySfx));
                Clip clip = AudioSystem.getClip();
                clip.open(ais);
                clip.setFramePosition(0);
                clip.start();
            } catch (UnsupportedAudioFileException | IOException | LineUnavailableException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    // Entfernt alle Buttons auf dem Feld. for-Schleifen fahren Feld ab und entfernen ggf. den Button.
    public static void removeFieldButtons(){
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++){
                for (int k = 0; k < Board.tiles[i][j].getpTile().getComponents().length; k++) {
                    Component c = Board.tiles[i][j].getpTile().getComponent(k);
                    if (c instanceof JButton){
                        if(((JButton) c).isSelected()){
                            Board.tiles[i][j].getpTile().remove(k);
                            Board.tiles[i][j].getpTile().updateUI();

                        }

                    }
                }
            }
        }
    }

    // fährt das tempPieces-Array ab und erzeugt an den richtigen Stellen die durch die tryKill()-Methode entfernten
    // killbaren Pieces
    public static void resetTempPieces(Tile destination) {
        for (Piece tempPiece : tempPieces) {
            if (tempPiece != null) {

                Tile originalTile = tempPiece.getPosition();

                if (!(originalTile.getpTile() == destination.getpTile())) {

                    originalTile.setOccupyingPiece(tempPiece);

                    JButton button = tempPiece.createPieceButton();
                    originalTile.getpTile().remove(0);
                    originalTile.getpTile().add(button);
                    originalTile.getpTile().updateUI();
                }
            }
        }

        // Nach dem Zurücksetzen leere das tempPieces-Array
        tempPieces = new Piece[0];
    }

    public abstract String getClassName(); // Für ToolTips

    // Getter für "moved"-Attribut
    public boolean isMoved() {
        return moved;
    }

    // Setter für "moved"-Attribut
    public void setMoved(boolean moved) {
        this.moved = moved;
    }

    // prüft, ob das jeweilige Feld im Board liegt und ob da nicht eine andere Figur schon drauf ist
    protected static boolean isValidMove(int x, int y) {
        if (x < 8 && x > -1 && y < 8 && y > -1) {
            return Board.tiles[x][y].getOccupyingPiece() == null;
        } else return false;
    }
}

