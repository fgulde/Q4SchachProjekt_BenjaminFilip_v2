import javax.swing.*;
public class King extends Piece {
    public King(boolean isWhite, boolean killed, Tile position) {
        super(isWhite, killed, position);
    }

    @Override
    public void calculateNewPos() {

    }

    @Override
    protected ImageIcon getIconPath() {
        return new ImageIcon(isWhite() ? "src/pics/KingWhite.png" : "src/pics/KingBlack.png");
    }
}
