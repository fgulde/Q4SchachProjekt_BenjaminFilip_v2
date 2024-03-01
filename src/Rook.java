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
            Piece tempPiece = Board.tiles[x][y].getOccupyingPiece();
            Board.tiles[x][y].getpTile().remove(0);
            JButton newButton = createFieldButton(Board.tiles[x][y]);
            newButton.setSelected(true);
            newButton.setIcon(tempPiece.getKillIconPath(tempPiece.isWhite()));
            Board.tiles[x][y].getpTile().add(newButton);
            Board.tiles[x][y].getpTile().updateUI();
        }
    }

    public void moveLogic(int newX, int newY){
        Tile newTile = Board.tiles[newX][newY];
        JButton newButton = createFieldButton(newTile);
        Board.tiles[newX][newY].getpTile().add(newButton);
        Board.tiles[newX][newY].getpTile().updateUI();
    }

    @Override
    public String getClassName() {
        return "Turm";
    }

    @Override
    protected ImageIcon getIconPath() {
        return new ImageIcon(isWhite() ? "src/pics/RookWhite.png" : "src/pics/RookBlack.png");
    }

    @Override
    protected ImageIcon getKillIconPath(boolean white){
        return new ImageIcon(isWhite() ? "src/pics/KillTargetIcons/RookWhiteKill.png" : "src/pics/KillTargetIcons/RookBlackKill.png");
    }
}
