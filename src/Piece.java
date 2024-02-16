import javax.sound.sampled.*;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

public abstract class Piece {
    private boolean white;
    private boolean killed;
    public Tile position;
    private boolean moved = false;

    public Piece(boolean white, boolean killed, Tile position) {
        this.white = white;
        this.killed = killed;
        this.position = position;
    }

    public Tile getPosition() {
        return this.position;
    }

    public void setPosition(Tile position) {
        this.position = position;
    }

    public boolean isWhite() {
        return this.white;
    }

    public abstract void calculateNewPos();

    // Move method
    public void move(int newX, int newY){
        // Bewege den Bauer auf das neue Feld
        Board.tiles[position.getX()][position.getY()].getpTile().remove(0);
        Board.tiles[position.getX()][position.getY()].getpTile().updateUI();
        Tile newTile = Board.tiles[newX][newY];
        getPosition().setOccupied(false);
        newTile.setOccupied(true);
        newTile.setOccupyingPiece(this);
        JButton pwButton = createPieceButton();
        Board.tiles[newTile.getX()][newTile.getY()].getpTile().remove(0);
        Board.tiles[newTile.getX()][newTile.getY()].getpTile().add(pwButton);
        position.setOccupied(false);
        position.setOccupyingPiece(null);
        setPosition(newTile);
        moved = true;

        // Überprüfe, ob der Bauer die gegnerische Grundreihe erreicht hat
        checkPromotion();
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
    // Button creation method
    public JButton createPieceButton() {
        JButton button = new JButton();
        button.addActionListener(new PieceActionListener(this));
        button.setMargin(new Insets(0, 0, 0, 0));
        button.setIcon(getIconPath());
        button.setOpaque(false);
        button.setContentAreaFilled(false);
        return button;
    }

    public JButton createFieldButton(Tile position) {
        JButton button = new JButton();
        button.addActionListener(new FieldActionListener(position, this));
        button.setMargin(new Insets(0, 0, 0, 0));
        button.setOpaque(false);
        button.setContentAreaFilled(false);
        button.setIcon(new ImageIcon("src/pics/Target.png"));
        button.setSelected(true);
        return button;
    }

    // Abstract method to get the icon path
    protected abstract ImageIcon getIconPath();

    // ActionListener for field clicks
    public static class PieceActionListener implements ActionListener {
        private final Piece piece;

        public PieceActionListener(Piece piece) {
            this.piece = piece;
        }


        @Override
        public void actionPerformed(ActionEvent e) {
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
            removeFieldButtons();
            piece.calculateNewPos();
        }
    }
    public static class FieldActionListener implements ActionListener {
        private final Tile newTile;
        private final Piece piece;

        public FieldActionListener(Tile newTile, Piece piece) {
            this.newTile = newTile;
            this.piece = piece;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
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

            if (piece.getPosition().isOccupied()) {
                piece.move(newTile.getX(), newTile.getY());
            }
            if (Board.status.equals(GameStatus.WHITEMOVE)) {
                Board.changeButtonsEnabled(true);
                Board.setStatus(GameStatus.BLACKMOVE);
            } else if (Board.status.equals(GameStatus.BLACKMOVE)) {
                Board.changeButtonsEnabled(false);
                Board.setStatus(GameStatus.WHITEMOVE);
            }
            Board.lStatus.setText(Board.status.toString());
            removeFieldButtons();
        }
    }

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

    public boolean isMoved() {
        return moved;
    }

    protected static boolean isValidMove(int x, int y) {
        if (x < 8 && x > -1 && y < 8 && y > -1) {
            if (Board.tiles[x][y].getOccupyingPiece() != null) {
                return false;
            } else return true;
        } else return false;
    }

}


