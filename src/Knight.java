import javax.swing.*;
public class Knight extends Piece {
    public Knight(boolean isWhite, boolean killed, Tile position) {
        super(isWhite, killed, position);
    }

    @Override
    public void calculateNewPos() {

    }

    @Override
    protected ImageIcon getIconPath() {
        return new ImageIcon(isWhite() ? "src/pics/KnightWhite.png" : "src/pics/KnightBlack.png");
    }
}
