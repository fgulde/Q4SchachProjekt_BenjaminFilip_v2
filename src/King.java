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
