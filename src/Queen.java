import javax.swing.*;

public class Queen extends Piece {
    public Queen(boolean isWhite, Tile position) {
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
        //Oben-Rechts
        for (int i = 1; i < 8; i++) {
            newY = currentTile.getY() + i;
            newX = currentTile.getX() + i;
            shouldBreak = moveLogic(newX, newY);
            if (shouldBreak) {
                break;
            }
        }
        //Oben-Links
        for (int i = 1; i < 8; i++) {
            newY = currentTile.getY() + i;
            newX = currentTile.getX() - i;
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
        return "Dame";
    }

    @Override
    // getter für das Icon je nach Farbe
    protected ImageIcon getIconPath() {
        return new ImageIcon(isWhite() ? "src/pics/QueenWhite.png" : "src/pics/QueenBlack.png");
    }
    @Override
    // getter für das KillIcon je nach Farbe
    protected ImageIcon getKillIconPath(boolean white){
        return new ImageIcon(isWhite() ? "src/pics/KillTargetIcons/QueenWhiteKill.png" : "src/pics/KillTargetIcons/QueenBlackKill.png");
    }

}
