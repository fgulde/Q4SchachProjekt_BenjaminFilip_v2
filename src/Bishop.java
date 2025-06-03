import javax.swing.*;
import java.util.Arrays;

public class Bishop extends Piece {
    public Bishop(boolean isWhite, Tile position) {
        super(isWhite, position);
    }

    @Override
    // Methode zum Berechnen aller möglichen / erlaubten Bewegungsrichtungen dieses Figurtypen
    public void calculateNewPos() {
        //Oben-Rechts
        Tile currentTile = getPosition();
        int newX;
        int newY;
        for (int i = 1; i < 8; i++) {
            newY = currentTile.getY() + i;
            newX = currentTile.getX() + i;
            if (isValidMove(newX, newY)){
                moveLogic(newX, newY);
            } else if (newX < 8 && newX > -1 && newY < 8 && newY > -1){
                tryKill(newX,newY);
                break;
            } else {
                break;
            }
        }
        //Oben-Links
        for (int i = 1; i < 8; i++) {
            newY = currentTile.getY() + i;
            newX = currentTile.getX() - i;
            if (isValidMove(newX, newY)){
                moveLogic(newX, newY);
            } else if (newX < 8 && newX > -1 && newY < 8 && newY > -1){
                tryKill(newX,newY);
                break;
            } else {
                break;
            }
        }
        //Unten-Rechts
        for (int i = 1; i < 8; i++) {
            newX = currentTile.getX() + i;
            newY = currentTile.getY() - i;
            if (isValidMove(newX, newY)){
                moveLogic(newX, newY);
            } else if (newX < 8 && newX > -1 && newY < 8 && newY > -1){
                tryKill(newX,newY);
                break;
            } else {
                break;
            }
        }
        //Unten-Links
        for (int i = 1; i < 8; i++) {
            newX = currentTile.getX() - i;
            newY = currentTile.getY() - i;
            if (isValidMove(newX, newY)){
                moveLogic(newX, newY);
            } else if (newX < 8 && newX > -1 && newY < 8 && newY > -1){
                tryKill(newX,newY);
                break;
            } else {
                break;
            }
        }
    }

    // Methode zum Erzeugen von FieldButtons
    private void moveLogic(int newX, int newY){
        Tile newTile = Board.tiles[newX][newY];
        JButton newButton = createFieldButton(newTile);
        Board.tiles[newX][newY].getpTile().add(newButton);
        Board.tiles[newX][newY].getpTile().updateUI();
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

