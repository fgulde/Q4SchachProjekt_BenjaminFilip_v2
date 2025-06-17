import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

public abstract class Piece {
    private final boolean white; // gibt an, ob die Figur weiß ist oder nicht
    public Tile position; // tile auf dem sich die Figur befindet
    private boolean moved = false; // boolean gibt an, ob die Figur schon bewegt wurde
    public static ArrayList<Piece> tempPieces = new ArrayList<>(); // Zwischenspeicher für die kill-Methode
    public static ArrayList<Piece> dangerousPieces = new ArrayList<>(); // Zwischenspeicher für Figuren die King bedrohen
    public static ArrayList<Tile> almostSaviourTiles = new ArrayList<>(); // Zwischenspeicher für Tiles, die vom Verteidiger mit dem Angreifer geshared werden
    public static ArrayList<Tile> saviourTiles = new ArrayList<>(); // Speicher für Tiles, auf denen die Figur den König verteidigen kann
    public static ArrayList<Tile> nonSaviourTiles = new ArrayList<>();

    public Piece(boolean white, Tile position) {
        this.white = white;
        this.position = position;
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

    // prüft, ob auf einem Feld eine gegnerische Figur ist
    public boolean canKill(int x, int y) {
        if (Board.tiles[x][y].getOccupyingPiece() != null){
            if ((Board.tiles[x][y].getOccupyingPiece().isWhite() && isWhite())
                    || (!Board.tiles[x][y].getOccupyingPiece().isWhite() && !isWhite())) {
                return false;
            } else {
                return x < 8 && y < 8;
            }
        } else {
            return false;
        }
    }

    // erzeugt, wenn möglich killButtons an Positionen, die die Figur schlagen kann
    public void tryKill(int x, int y) {
        if (x >= 0 && x < 8 && canKill(x, y)) {
            Piece tempPiece = Board.tiles[x][y].getOccupyingPiece();
            tempPieces.add(tempPiece);

            spawnKillButton(Board.tiles[x][y], tempPiece);
        }
    }

    // Methode, die die Figur auf ein neues Feld bewegt
    public void move(int newX, int newY){
        Piece piece = Board.tiles[newX][newY].getOccupyingPiece();
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
            checkt, ob der Bauer zwei oder ein Feld nach vorne gegangen ist und setzt ihn, wenn ja, auf EnPassant-able
            isDefaultCapable() == false, wird zum Kennzeichnen von Buttons benutzt, die die EnPassant Regel aktivieren
         */
        if (piece instanceof Pawn pawn && !tempPieces.contains(piece)) {
            if (!Board.tiles[newX][newY].getButton().isDefaultCapable()) {
                pawn.setEnPassant(true);
            } else if (Board.tiles[newX][newY].getButton().isDefaultCapable()) {
                pawn.setEnPassant(false);
            }
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
        if (Board.tiles[newTile.getX()][newTile.getY()].getpTile().getComponentCount() > 0) {
            Board.tiles[newTile.getX()][newTile.getY()].getpTile().remove(0);
        }
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

    // Erzeugt einen Button, der anzeigt, wohin sich die Figur alles bewegen kann
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
        button.setSelected(true); // markiert den Button als nicht PieceButton
        button.setToolTipText("Schlage diese Figur.");
        return button;
    }

    public void spawnKillButton(Tile tile, Piece piece) {
        JButton newButton = createKillButton(tile);
        newButton.setSelected(true);
        newButton.setIcon(piece.getKillIconPath(piece.isWhite()));

        tile.getpTile().remove(0);
        tile.getpTile().add(newButton);
        tile.getpTile().updateUI();

    }

    // Erzeugt einen Button, der anzeigt, dass die Figur(König) castlen kann
    public JButton createCastleButton(Tile position) {
        JButton button = new JButton();
        button.addActionListener(new FieldActionListener(position, this));
        button.setMargin(new Insets(0, 0, 0, 0));
        button.setOpaque(false);
        button.setContentAreaFilled(false);
        button.setIcon(new ImageIcon("src/pics/Castling.png"));
        button.setSelected(true); // markiert den Button als nicht PieceButton
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
            audioPlay(clickSfx);
            // entfernt alle Buttons, die nur temporär sind, also keine Figuren und fügt gegebenenfalls durch killButtons entfernte Figuren wieder ein
            resetTempPieces(piece.getPosition());
            removeFieldButtons();
            saviourTiles.clear();
            nonSaviourTiles.clear();

            // checks for if the own King is in Check
            if(CheckCheck(piece.isWhite())){
                Board.setKingInCheck(true);
                if (!(piece instanceof King)) {
                    piece.chucklesImInDanger(this.piece);
                } else {
                    piece.safeTheKing(this.piece);
                }
            }

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
            // Wenn König in Check ist, werden alle FieldButtons gelöscht, die nicht in saviourTiles drin sind
            if (Board.isKingInCheck()){
                for (int i = 0; i < 8; i++) {
                    for (int j = 0; j < 8; j++) {
                        if (!saviourTiles.contains(Board.tiles[i][j])) {
                            for (int k = 0; k < Board.tiles[i][j].getpTile().getComponents().length; k++) {
                                Component c = Board.tiles[i][j].getpTile().getComponent(k);
                                if (c instanceof JButton && ((JButton) c).isSelected() && ((JButton) c).getIcon().toString().equals("src/pics/Target.png")) {
                                    Board.tiles[i][j].getpTile().remove(c);
                                    Board.tiles[i][j].getpTile().updateUI();

                                } else if (c instanceof JButton && ((JButton) c).isSelected() && ((JButton) c).getIcon().toString().contains("src/pics/KillTargetIcons")) {
                                    resetThisTempPiece(Board.tiles[i][j]); // work in progress
                                }
                            }
                        }
                    }
                }
            }

            // checkt, ob die Figur castlen kann
            checkCastleR(piece);
            checkCastleL(piece);
        }

        public static void audioPlay(String clickSfx) {
            try {
                AudioInputStream ais = AudioSystem.getAudioInputStream(new File(clickSfx));
                Clip clip = AudioSystem.getClip();
                clip.open(ais);
                clip.setFramePosition(0);
                clip.start();
            } catch (UnsupportedAudioFileException | IOException | LineUnavailableException ex) {
                throw new RuntimeException(ex);
            }
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
            PieceActionListener.audioPlay(moveSfx);

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
                Board.setKingInCheck(false);
                Board.changeButtonsEnabled(true);
                Board.setStatus(GameStatus.BLACKMOVE);
            } else if (Board.status.equals(GameStatus.BLACKMOVE)) {
                Board.setKingInCheck(false);
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
            PieceActionListener.audioPlay(notifySfx);
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

    // fährt das tempPieces-Array ab und erzeugt an den richtigen Stellen die durch die tryKill()-Methode entfernten killbaren Pieces
    public static void resetTempPieces(Tile destination) {
        for (Piece tempPiece : tempPieces) {
            if (tempPiece != null) {

                Tile originalTile = tempPiece.getPosition();

                if (originalTile.getpTile() != destination.getpTile()) {

                    originalTile.setOccupyingPiece(tempPiece);

                    JButton button = tempPiece.createPieceButton();
                    originalTile.getpTile().remove(0);
                    originalTile.getpTile().add(button);
                    originalTile.getpTile().updateUI();
                }
            }
        }

        // Nach dem Zurücksetzen leere das tempPieces-Array
        tempPieces.clear();
    }

    // setzt nur den KillButton zurück, der an derselben Stelle wie destination ist
    public static void resetThisTempPiece(Tile destination) {
        Piece piece = null;
        for (Piece tempPiece : tempPieces) {
            if (tempPiece != null && tempPiece.getPosition() == destination) {
                piece = tempPiece;
                Tile originalTile = tempPiece.getPosition();
                originalTile.setOccupyingPiece(tempPiece);

                JButton button = tempPiece.createPieceButton();
                originalTile.getpTile().remove(0);
                originalTile.getpTile().add(button);
                originalTile.getpTile().updateUI();
                button.setEnabled(false);
                button.setDisabledIcon(tempPiece.getIconPath());
            }
        }
        tempPieces.remove(piece);
    }

    // fährt das tempPieces-Array jedes Tiles ab und erzeugt an den richtigen Stellen die durch die tryKill()-Methode entfernten
    // killbaren Pieces
    //versuchte Lösung für CheckCheck()
    public static void resetAllTempPieces() {
        for (Piece tempPiece : tempPieces) {
            if (tempPiece != null) {
                Tile originalTile = tempPiece.getPosition();

                if (originalTile != null && originalTile.getpTile() != null) {
                    // Only remove if there are components
                    if (originalTile.getpTile().getComponents().length > 0) {
                        originalTile.getpTile().remove(0);
                    }

                    originalTile.setOccupyingPiece(tempPiece);
                    JButton button = tempPiece.createPieceButton();
                    originalTile.getpTile().add(button);
                    originalTile.getpTile().updateUI();
                }
            }
        }
        // Clear the tempPieces array after processing
        tempPieces.clear();
    }

    public static Piece findKingOfActivePieces() {
        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                Piece piece = Board.tiles[x][y].getOccupyingPiece();
                if (piece instanceof King){
                    if (piece.isWhite() && Board.status.equals(GameStatus.WHITEMOVE)) {
                        return piece; // Return the King piece if it's the right color
                    } else if (!piece.isWhite() && Board.status.equals(GameStatus.BLACKMOVE)) {
                        return piece;
                    }
                }
            }
        }
        throw new IllegalStateException("No active King found on the board!");
    }

    //Schaut, ob King im Check steht
    public static boolean CheckCheck(boolean isWhite) {
        Piece kingPiece = findKingOfActivePieces();
        Tile kingTile = kingPiece.getPosition();
        Icon kingIcon = kingPiece.getKillIconPath(isWhite);
        boolean check = false;

        for (int m = 0; m < 8; m++) {
            for (int n = 0; n < 8; n++) {
                Piece piece = Board.tiles[m][n].getOccupyingPiece();
                if (piece == null || piece.isWhite() == isWhite) continue;

                piece.calculateNewPos();

                if (isKingThreatenedOnTile(kingTile, kingIcon)) {
                    if (!dangerousPieces.contains(piece)) {
                        dangerousPieces.add(piece);
                    }
                    resetAllTempPieces();
                    removeFieldButtons();
                    check = true;
                }

                resetAllTempPieces();
                removeFieldButtons();
            }
        }
        return check;
    }

    private static boolean isKingThreatenedOnTile(Tile kingTile, Icon kingIcon) {
        JPanel kingPanel = Board.tiles[kingTile.getX()][kingTile.getY()].getpTile();
        for (Component c : kingPanel.getComponents()) {
            if (c instanceof JButton button) {
                if (Objects.equals(button.getIcon().toString(), kingIcon.toString())) {
                    return true;
                }
            }
        }
        return false;
    }
    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    public static boolean stillGeneratingKillButton(Piece currentPiece){
        Piece activeKing = findKingOfActivePieces();
        Tile kingTile = activeKing.getPosition();

        if (currentPiece != null){
            currentPiece.calculateNewPos();
            Board.getKillButtons();
            for (Tile killButtonTile : Board.tempKillButtons){
                if (killButtonTile == kingTile) {
                    return true;
                }
            }
        }else {
            // If no specific piece is given, check all enemy pieces
            for (int x = 0; x < 8; x++) {
                for (int y = 0; y < 8; y++) {
                    Piece piece = Board.tiles[x][y].getOccupyingPiece();
                    if (piece != null && piece.isWhite() != activeKing.isWhite()) {
                        piece.calculateNewPos();
                        Board.getKillButtons();
                        for (Tile killButtonTile : Board.tempKillButtons) {
                            if (killButtonTile == kingTile) {
                                resetAllTempPieces();
                                removeFieldButtons();
                                return true;
                            }
                        }
                        resetAllTempPieces();
                        removeFieldButtons();
                    }
                }
            }
        }
        resetAllTempPieces();
        removeFieldButtons();
        return false;
    }

    //fährt array aus CheckCheck() ab und schaut ob eigene Figur Pfade erzeugt, die die Pfade der Angreifer durchkreuzen oder den Angreifer schlagen könnten
    public void chucklesImInDanger(Piece currentPiece){
        almostSaviourTiles.clear();

        for (Piece dangerousPiece : dangerousPieces) {
            if (currentPiece.isWhite() != dangerousPiece.isWhite()){
                fillTempArrays_Check(currentPiece, dangerousPiece);
                findCrossingTiles_Check(false);
                findKillableTiles_Check(dangerousPiece);
                validateAlmostSaviourTilesArray_Check(currentPiece, dangerousPiece);
            }
        }

        resetAllTempPieces();
        removeFieldButtons();
    }

    // Füllt die temporären Arrays mit den möglichen neuen Positionen der aktuellen und der gefährlichen Figur und den Kill-Buttons, die currentPiece generieren kann
    public void fillTempArrays_Check(Piece currentPiece, Piece dangerousPiece) {
        currentPiece.calculateNewPos();
        Board.getFieldButtons(true);
        dangerousPiece.calculateNewPos();
        Board.getFieldButtons(false);
        currentPiece.calculateNewPos();
        Board.getKillButtons();
    }

    // Findet die FieldButtonTiles, die currentPiece und dangerousPiece gemeinsam haben
    public void findCrossingTiles_Check(boolean pieceIsKing) {
        for (Tile defenderTile : Board.tempFieldButtonsDefender) {

            boolean inAttacker = Board.tempFieldButtonsAttacker.contains(defenderTile);
            boolean inAlmostSaviour = almostSaviourTiles.contains(defenderTile);
            boolean inNonSaviour = nonSaviourTiles.contains(defenderTile);

            // Wenn das Feld in nonSaviourTiles ist, entferne es aus almostSaviourTiles
            if (inNonSaviour) {
                almostSaviourTiles.remove(defenderTile);
                continue;
            }

            // Wenn es weder in almost- noch nonSaviour ist
            if (!inAlmostSaviour) {
                if ((pieceIsKing && !inAttacker) || (!pieceIsKing && inAttacker)) {
                    almostSaviourTiles.add(defenderTile);
                } else {
                    nonSaviourTiles.add(defenderTile);
                }
            }
        }
    }


    // Findet die Kill-Buttons, welche von currentPiece generiert wurden, die die gefährliche Figur schlagen können
    public void findKillableTiles_Check(Piece dangerousPiece){
        // wenn mehr als ein gefährliches Piece existiert, bringt das Killen von einem ja nichts
        if (dangerousPieces.size() == 1) {
            for (Tile killButtonTile : Board.tempKillButtons) {
                if (killButtonTile == dangerousPiece.getPosition()) {
                    for (Piece tempPiece : dangerousPieces) {
                        if (tempPiece == dangerousPiece)
                            continue;
                        if (tempPiece != null && stillGeneratingKillButton(tempPiece))
                            nonSaviourTiles.add(killButtonTile);
                    }
                    if (!nonSaviourTiles.contains(killButtonTile)) {
                        saviourTiles.add(killButtonTile); //Muss nicht weiter geprüft werden, da es nur ein gefährliches Piece gibt
                    }
                }
            }

        }
    }

    // Prüft, ob die Tiles von almostSaviourTiles, tatsächlich den König retten können
    public void validateAlmostSaviourTilesArray_Check(Piece currentPiece, Piece dangerousPiece) {
        for (Tile almostSaviourTile : almostSaviourTiles){
            tempMove(currentPiece, almostSaviourTile, currentPiece.getPosition(), () -> {
                if (!stillGeneratingKillButton(null)){
                    saviourTiles.add(almostSaviourTile);
                }else {
                    nonSaviourTiles.add(almostSaviourTile);
                    saviourTiles.remove(almostSaviourTile);
                }
            });
        }
    }

    public void safeTheKing(Piece currentPiece){
        almostSaviourTiles.clear();
        saviourTiles.clear();

        for (Piece dangerousPiece : dangerousPieces) {
            if (currentPiece.isWhite() != dangerousPiece.isWhite()){
                fillTempArrays_Check(currentPiece, dangerousPiece);
                findCrossingTiles_Check(true);
                validateAlmostSaviourTilesArray_Check(currentPiece, dangerousPiece);
            }
        }

        resetAllTempPieces();
        removeFieldButtons();
    }

    // temporarily move a piece to a new tile without updating the game state and between moving it somewhere and back
    // executes a given method (actionBetweenMoves)
    public void tempMove(Piece currentPiece, Tile tempTile, Tile originalTile, Runnable actionBetweenMoves) {

        Board.freezeTextArea(); // freeze txtA so move is not recorded
        //move king to the temp position
        currentPiece.move(tempTile.getX(), tempTile.getY());
        Board.vCounter--; // decrease vCounter because it isn't frozen like txtA

        try {
            actionBetweenMoves.run();
        } finally {
            // move piece back to his original position
            currentPiece.move(originalTile.getX(), originalTile.getY());
            Board.unfreezeTextArea(); // unfreeze txtA
            Board.vCounter--; // decrease vCounter because it isn't frozen like txtA
        }
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

