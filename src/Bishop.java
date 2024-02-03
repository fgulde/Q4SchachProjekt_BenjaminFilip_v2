import javax.swing.*;
public class Bishop extends Piece {
    public Bishop(boolean isWhite, boolean killed, Tile position) {
        super(isWhite, killed, position);
    }

    @Override
    public void calculateNewPos() {

    }


    @Override
    protected ImageIcon getIconPath() {
        return new ImageIcon(isWhite() ? "src/pics/BishopWhite.png" : "src/pics/BishopBlack.png");
    }
}

