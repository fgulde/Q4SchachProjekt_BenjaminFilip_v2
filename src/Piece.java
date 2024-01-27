import javax.swing.ImageIcon;
import javax.swing.JButton;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public abstract class Piece {
    private boolean white;
    private boolean killed;
    private Tile position;

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
    public abstract void moveLogic();

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
            piece.calculateNewPos();
            System.out.println("Feld (" + (piece.getPosition().getX() + 1)+ ", " + (piece.getPosition().getY() + 1) + ") wurde geklickt");

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
            // Überprüfen Sie, ob das Feld besetzt ist, bevor Sie Aktionen ausführen
            if (piece.getPosition().isOccupied()) {
                piece.moveLogic();
            }
        }
    }


}


