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

    // prüft, ob auf einem Feld eine gegnerische Figur ist
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

    // erzeugt, wenn möglich killButtons an Positionen, die die Figur schlagen kann
    public void tryKill(int x, int y) {
        if (x >= 0 && x < 8 && canKill(x, y)) {
            Piece tempPiece = Board.tiles[x][y].getOccupyingPiece();
            tempPieces = Arrays.copyOf(tempPieces, tempPieces.length + 1);
            tempPieces[tempPieces.length - 1] = tempPiece;

            JButton newButton = createKillButton(Board.tiles[x][y]);
            newButton.setSelected(true);
            newButton.setIcon(tempPiece.getKillIconPath(tempPiece.isWhite()));

            Board.tiles[x][y].getpTile().remove(0);
            Board.tiles[x][y].getpTile().add(newButton);
            Board.tiles[x][y].getpTile().updateUI();
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
