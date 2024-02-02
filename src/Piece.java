import javax.swing.ImageIcon;
import javax.swing.JButton;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public abstract class Piece {
    private boolean white;
    private boolean killed;
    public Tile position;

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
    public abstract void move(int newX, int newY, Tile currentTile);

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
            // Hier können Sie die Logik für das Klicken auf das Feld implementieren
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
            if (piece.getPosition().isOccupied()) {
                piece.move(newTile.getX(), newTile.getY(), newTile);
                }
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


}


