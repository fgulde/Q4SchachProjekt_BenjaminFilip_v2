import javax.swing.*;
import java.util.Arrays;

public class Knight extends Piece {
    public Knight(boolean isWhite, Tile position) {
        super(isWhite, position);
    }

    @Override
    // Methode zum Berechnen aller möglichen / erlaubten Bewegungsrichtungen dieses Figurtypen
    public void calculateNewPos() {
        for (int i = -1; i <= 1; i += 2) {
            //Unten
            Tile currentTile = getPosition();
            int newY = currentTile.getY() + 2;
            int newX = currentTile.getX() + i;
            moveLogic(newX, newY);
            //Unten
            newX = currentTile.getX() + i;
            newY = currentTile.getY() - 2;
            moveLogic(newX, newY);
            //Links
            newY = currentTile.getY() + i;
            newX = currentTile.getX() + 2;
            moveLogic(newX, newY);
            //Rechts
            newY = currentTile.getY() + i;
            newX = currentTile.getX() - 2;
            moveLogic(newX, newY);
        }
    }

    // Methode zum Erzeugen von FieldButtons
    private void moveLogic(int newX, int newY){
        if (isValidMove(newX, newY)){
            Tile newTile = Board.tiles[newX][newY];
            JButton newButton = createFieldButton(newTile);
            Board.tiles[newX][newY].getpTile().add(newButton);
            Board.tiles[newX][newY].getpTile().updateUI();
        } else if (newX < 8 && newX > -1 && newY < 8 && newY > -1){
            tryKill(newX,newY);}
    }

    @Override
    // getter für den Namen der Klasse
    public String getClassName() {
        return "Springer";
    }

    @Override
    // getter für das Icon je nach Farbe
    protected ImageIcon getIconPath() {
        return new ImageIcon(isWhite() ? "src/pics/KnightWhite.png" : "src/pics/KnightBlack.png");
    }
    @Override
    // getter für das KillIcon je nach Farbe
    protected ImageIcon getKillIconPath(boolean white){
        return new ImageIcon(isWhite() ? "src/pics/KillTargetIcons/KnightWhiteKill.png" : "src/pics/KillTargetIcons/KnightBlackKill.png");
    }
}
