import javax.swing.*;

public class Bishop extends Piece {
    public Bishop(boolean isWhite, Tile position) {
        super(isWhite, position);
    }

    @Override
    // Methode zum Berechnen aller möglichen / erlaubten Bewegungsrichtungen dieses Figurtypen
    public void calculateNewPos() {
        //Oben-Rechts
        Tile currentTile = getPosition();
        boolean shouldBreak;
        int newX;
        int newY;
        for (int i = 1; i < 8; i++) {
            newX = currentTile.getX() + i;
            newY = currentTile.getY() + i;
            shouldBreak = moveLogic(newX, newY);
            if (shouldBreak) {
                break;
            }
        }
        //Oben-Links
        for (int i = 1; i < 8; i++) {
            newX = currentTile.getX() - i;
            newY = currentTile.getY() + i;
            shouldBreak = moveLogic(newX, newY);
            if (shouldBreak) {
                break;
            }
        }
        //Unten-Rechts
        for (int i = 1; i < 8; i++) {
            newX = currentTile.getX() + i;
            newY = currentTile.getY() - i;
            shouldBreak = moveLogic(newX, newY);
            if (shouldBreak) {
                break;
            }
        }
        //Unten-Links
        for (int i = 1; i < 8; i++) {
            newX = currentTile.getX() - i;
            newY = currentTile.getY() - i;
            shouldBreak = moveLogic(newX, newY);
            if (shouldBreak) {
                break;
            }
        }
    }

    @Override
    // getter für den Namen der Klasse
    public String getClassName() {
        return "Läufer";
    }

    @Override
    // getter für das Icon je nach Farbe
    protected ImageIcon getIconPath() {
        return new ImageIcon(isWhite() ? "src/pics/BishopWhite.png" : "src/pics/BishopBlack.png");
    }

    @Override
    // getter für das KillIcon je nach Farbe
    protected ImageIcon getKillIconPath(boolean white){
        return new ImageIcon(isWhite() ? "src/pics/KillTargetIcons/BishopWhiteKill.png" : "src/pics/KillTargetIcons/BishopBlackKill.png");
    }
}

