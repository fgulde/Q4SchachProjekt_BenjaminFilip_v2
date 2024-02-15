import javax.swing.*;
public class Rook extends Piece {
    public Rook(boolean isWhite, boolean killed, Tile position) {
        super(isWhite, killed, position);
    }

    @Override
    public void calculateNewPos() {
        //Oben
        Tile currentTile = getPosition();
        int newX = currentTile.getX();
        for (int i = 1; i < 8; i++) {
            int newY = currentTile.getY() + i;
            if (isValidMove(newX, newY)){
                moveLogic(newX, newY);
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
                moveLogic(newX, newY);
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
                moveLogic(newX, newY);
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
                moveLogic(newX, newY);
            } else if (newX < 8 && newX > -1 && newY < 8 && newY > -1){
                tryKill(newX,newY);
                break;
            } else {
                break;
            }
        }
    }

    @Override
    protected ImageIcon getIconPath() {
        return new ImageIcon(isWhite() ? "src/pics/RookWhite.png" : "src/pics/RookBlack.png");
    }
}
