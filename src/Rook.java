import javax.swing.*;
public class Rook extends Piece {
    public Rook(boolean isWhite, boolean killed, Tile position) {
        super(isWhite, killed, position);
    }

    @Override
    public void calculateNewPos() {

    }

    @Override
    protected ImageIcon getIconPath() {
        return new ImageIcon(isWhite() ? "src/pics/RookWhite.png" : "src/pics/RookBlack.png");
    }
}
