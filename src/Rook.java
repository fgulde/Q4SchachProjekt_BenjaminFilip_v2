import javax.swing.*;

public class Rook extends Piece {
    public Rook(boolean isWhite, Tile position) {
        super(isWhite, position);
    }

    @Override
    // Methode zum Berechnen aller möglichen / erlaubten Bewegungsrichtungen dieses Figurtypen
    public void calculateNewPos() {
        //Oben
        Tile currentTile = getPosition();
        boolean shouldBreak;
        int newX = currentTile.getX();
        for (int i = 1; i < 8; i++) {
            int newY = currentTile.getY() + i;
            shouldBreak = moveLogic(newX, newY);
            if (shouldBreak) {
                break;
            }
        }
        //Unten
        newX = currentTile.getX();
        for (int i = 1; i < 8; i++) {
            int newY = currentTile.getY() - i;
            shouldBreak = moveLogic(newX, newY);
            if (shouldBreak) {
                break;
            }
        }
        //Links
        int newY = currentTile.getY();
        for (int i = 1; i < 8; i++) {
            newX = currentTile.getX() + i;
            shouldBreak = moveLogic(newX, newY);
            if (shouldBreak) {
                break;
            }
        }
        //Rechts
        newY = currentTile.getY();
        for (int i = 1; i < 8; i++) {
            newX = currentTile.getX() - i;
            shouldBreak = moveLogic(newX, newY);
            if (shouldBreak) {
                break;
            }
        }
    }

    @Override
    // getter für den Namen der Klasse
    public String getClassName() {
        return "Turm";
    }

    @Override
    // getter für das Icon je nach Farbe
    protected ImageIcon getIconPath() {
        return new ImageIcon(isWhite() ? "src/pics/RookWhite.png" : "src/pics/RookBlack.png");
    }

    @Override
    // getter für das KillIcon je nach Farbe
    protected ImageIcon getKillIconPath(boolean white){
        return new ImageIcon(isWhite() ? "src/pics/KillTargetIcons/RookWhiteKill.png" : "src/pics/KillTargetIcons/RookBlackKill.png");
    }
}
