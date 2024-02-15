import javax.swing.*;
public class King extends Piece {
    public King(boolean isWhite, boolean killed, Tile position) {
        super(isWhite, killed, position);
    }

    @Override
    public void calculateNewPos() {
        //Oben
        Tile currentTile = getPosition();
        int newX = currentTile.getX();
        int newY = currentTile.getY() + 1;
        moveLogic(newX,newY);
        //Unten
        newX = currentTile.getX();
        newY = currentTile.getY() - 1;
        moveLogic(newX,newY);
        //Links
        newY = currentTile.getY();
        newX = currentTile.getX() + 1;
        moveLogic(newX,newY);
        //Rechts
        newY = currentTile.getY();
        newX = currentTile.getX() - 1;
        moveLogic(newX,newY);
        //Oben-Rechts
        newY = currentTile.getY() + 1;
        newX = currentTile.getX() + 1;
        moveLogic(newX,newY);
        //Oben-Links
        newY = currentTile.getY() + 1;
        newX = currentTile.getX() - 1;
        moveLogic(newX,newY);
        //Unten-Rechts
        newX = currentTile.getX() + 1;
        newY = currentTile.getY() - 1;
        moveLogic(newX,newY);
        //Unten-Links
        newX = currentTile.getX() - 1;
        newY = currentTile.getY() - 1;
        moveLogic(newX,newY);
    }

    private boolean canKill(int x, int y) {
        if (Board.tiles[x][y].getOccupyingPiece() != null){
            if ((Board.tiles[x][y].getOccupyingPiece().isWhite() && isWhite())
                    || (!Board.tiles[x][y].getOccupyingPiece().isWhite() && !isWhite())) {
                return false;
            } else {
                return x < 8 && y < 8;
            }
        } else {
            return false;
        }
    }

    public void tryKill(int x, int y) {
        if (x >= 0 && x < 8 && canKill(x, y)) {
            JButton newButton = createFieldButton(Board.tiles[x][y]);
            newButton.setSelected(true);
            newButton.setIcon(new ImageIcon("src/pics/KillTarget.png"));
            Board.tiles[x][y].getpTile().add(newButton);
            Board.tiles[x][y].getpTile().updateUI();
        }
    }

    private void moveLogic(int newX, int newY){
        if (isValidMove(newX, newY)){
            Tile newTile = Board.tiles[newX][newY];
            JButton newButton = createFieldButton(newTile);
            Board.tiles[newX][newY].getpTile().add(newButton);
            Board.tiles[newX][newY].getpTile().updateUI();
        } else if (newX < 8 && newX > -1 && newY < 8 && newY > -1){
            tryKill(newX,newY);
        }
    }

    @Override
    protected ImageIcon getIconPath() {
        return new ImageIcon(isWhite() ? "src/pics/KingWhite.png" : "src/pics/KingBlack.png");
    }
}
