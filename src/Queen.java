import javax.swing.*;
import java.util.Arrays;

public class Queen extends Piece {
    public Queen(boolean isWhite, Tile position) {
        super(isWhite, position);
    }

    @Override
    // Methode zum Berechnen aller möglichen / erlaubten Bewegungsrichtungen dieses Figurtypen
    public void calculateNewPos() {
        //Oben
        Tile currentTile = getPosition();
        int newX = currentTile.getX();
        for (int i = 1; i < 8; i++) {
            int newY = currentTile.getY() + i;
            if (isValidMove(newX, newY)){
                moveLogic(newX,newY);
            } else if (newX < 8 && newX > -1 && newY < 8 && newY > -1){
                tryKill(newX,newY);
                break;
            } else {
                break;
            }
        }
        //Unten
        newX = currentTile.getX();
        for (int i = 1; i < 8; i++) {
            int newY = currentTile.getY() - i;
            if (isValidMove(newX, newY)){
                moveLogic(newX,newY);
            } else if (newX < 8 && newX > -1 && newY < 8 && newY > -1){
                tryKill(newX,newY);
                break;
            } else {
                break;
            }
        }
        //Links
        int newY = currentTile.getY();
        for (int i = 1; i < 8; i++) {
            newX = currentTile.getX() + i;
            if (isValidMove(newX, newY)){
                moveLogic(newX,newY);
            } else if (newX < 8 && newX > -1 && newY < 8 && newY > -1){
                tryKill(newX,newY);
                break;
            } else {
                break;
            }
        }
        //Rechts
        newY = currentTile.getY();
        for (int i = 1; i < 8; i++) {
            newX = currentTile.getX() - i;
            if (isValidMove(newX, newY)){
                moveLogic(newX,newY);
            } else if (newX < 8 && newX > -1 && newY < 8 && newY > -1){
                tryKill(newX,newY);
                break;
            } else {
                break;
            }
        }
        //Oben-Rechts
        for (int i = 1; i < 8; i++) {
            newY = currentTile.getY() + i;
            newX = currentTile.getX() + i;
            if (isValidMove(newX, newY)){
                moveLogic(newX,newY);
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
                moveLogic(newX,newY);
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
                moveLogic(newX,newY);
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
                moveLogic(newX,newY);
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
