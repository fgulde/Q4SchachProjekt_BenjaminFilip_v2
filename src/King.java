import javax.swing.*;
import java.util.Arrays;

public class King extends Piece {
    public King(boolean isWhite, Tile position) {
        super(isWhite, position);
    }

    public boolean castled = false;

    @Override
    // Methode zum Berechnen aller möglichen / erlaubten Bewegungsrichtungen dieses Figurtypen
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
            tryKill(newX,newY);
        }
    }

    // getter für, ob der König schon gecastled wurde
    public boolean isCastled() {
        return castled;
    }

    // setter für, ob der König schon gecastled wurde
    public void setCastled(boolean castled) {
        this.castled = castled;
    }

    // Methode zum castlen
    public void castle(Tile rookTile) {
        // prüft die bedingungen zum castlen
        if (!isMoved() && !rookTile.getOccupyingPiece().isMoved()) {
            // prüft, ob keine Figuren zwischen dem König und Turm sind
            for (int direction = -1; direction <= 1; direction += 2) {
                int startTile = getPosition().getX() + direction; // Endposition des Turms
                int destTile = getPosition().getX() + 2*direction;
                boolean piecesBetween = false;
                int pTile = startTile;
                // Prüft, ob Figuren zwischen Turm und König liegen; wenn ja, break
                while (pTile != rookTile.getX()){
                     if (Board.tiles[pTile][getPosition().getY()].getOccupyingPiece() != null) {
                        piecesBetween = true;
                        break;
                        }
                     pTile += direction;
                }
                if (!piecesBetween) {
                        // erzeugt einen castleButton, wenn erlaubt
                        JButton castleButton = createCastleButton(Board.tiles[destTile][getPosition().getY()]);
                        if (Board.tiles[destTile][getPosition().getY()].getpTile().getComponentCount() > 0) {
                            Board.tiles[destTile][getPosition().getY()].getpTile().remove(0);
                        }
                        Board.tiles[destTile][getPosition().getY()].getpTile().add(castleButton);
                        Board.tiles[destTile][getPosition().getY()].getpTile().updateUI();
                        setCastled(true);

                }
            }
        }
    }

    @Override
    // getter für den Namen der Klasse
    public String getClassName() {
        return "König";
    }

    @Override
    // getter für das Icon je nach Farbe
    protected ImageIcon getIconPath() {
        return new ImageIcon(isWhite() ? "src/pics/KingWhite.png" : "src/pics/KingBlack.png");
    }
    @Override
    // getter für das KillIcon je nach Farbe
    protected ImageIcon getKillIconPath(boolean white){
        return new ImageIcon(isWhite() ? "src/pics/KillTargetIcons/KingWhiteKill.png" : "src/pics/KillTargetIcons/KingBlackKill.png");
    }
}
