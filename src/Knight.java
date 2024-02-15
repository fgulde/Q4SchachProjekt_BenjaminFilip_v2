import javax.swing.*;
public class Knight extends Piece {
    public Knight(boolean isWhite, boolean killed, Tile position) {
        super(isWhite, killed, position);
    }

    @Override
    public void calculateNewPos() {

    }

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
    protected ImageIcon getIconPath() {
        return new ImageIcon(isWhite() ? "src/pics/KnightWhite.png" : "src/pics/KnightBlack.png");
    }
}
