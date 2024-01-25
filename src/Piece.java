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

    // Move method
    public abstract void move();

    // Button creation method
    public JButton createButton() {
        JButton button = new JButton();
        button.addActionListener(new FeldActionListener(this));
        button.setMargin(new Insets(0, 0, 0, 0));
        button.setIcon(getIconPath());
        button.setOpaque(false);
        button.setContentAreaFilled(false);
        return button;
    }

    // Abstract method to get the icon path
    protected abstract ImageIcon getIconPath();

    // ActionListener for field clicks
    private static class FeldActionListener implements ActionListener {
        private final Piece piece;

        public FeldActionListener(Piece piece) {
            this.piece = piece;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            // Hier können Sie die Logik für das Klicken auf das Feld implementieren
            piece.calculateNewPos();
            System.out.println("Feld (" + (piece.getPosition().getX() + 1)+ ", " + (piece.getPosition().getY() + 1) + ") wurde geklickt");

        }
    }
}

