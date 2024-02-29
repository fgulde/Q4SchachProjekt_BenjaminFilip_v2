import javax.swing.*;
public class King extends Piece {
    public King(boolean isWhite, boolean killed, Tile position) {
        super(isWhite, killed, position);
    }

    public boolean castled = false;

    @Override
    public void calculateNewPos() {
        if (!isMoved()){

        }
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
            Piece tempPiece = Board.tiles[x][y].getOccupyingPiece();
            Board.tiles[x][y].getpTile().remove(0);
            JButton newButton = createFieldButton(Board.tiles[x][y]);
            newButton.setSelected(true);
            newButton.setIcon(tempPiece.getKillIconPath(tempPiece.isWhite()));
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

    public boolean isCastled() {
        return castled;
    }

    public void setCastled(boolean castled) {
        this.castled = castled;
    }

    public void castle(Tile rookTile) {
        // Check conditions for castling
        if (!isMoved() && !rookTile.getOccupyingPiece().isMoved()) {
            // Check if there are no pieces between the king and the rook
            for (int direction = -1; direction <= 1; direction += 2) {
                int startTile = getPosition().getX() + direction; // Is used for Rook Endpos
                int destTile = getPosition().getX() + 2*direction;
                boolean piecesBetween = false;
                int pTile = startTile;
                // Check for Pieces between Rook and King; if true, break
                while (pTile != rookTile.getX()){
                     if (Board.tiles[pTile][getPosition().getY()].getOccupyingPiece() != null) {
                        piecesBetween = true;
                        break;
                        }
                     pTile += direction;
                }
                if (!piecesBetween) {
                    // Check if the king is not in check and the squares are not under attack
                    if (!isInCheck()/* && !isInCheckAfterMove(getPosition().getX(), destTile)*/) {
                        // Display the castling option button
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
    }

    public boolean isInCheck() {
        // Check if the king is attacked by any opponent's piece
        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                if (Board.tiles[x][y].getOccupyingPiece() != null
                        && Board.tiles[x][y].getOccupyingPiece().isWhite() != isWhite()) {
                    if (Board.tiles[x][y].getOccupyingPiece().isValidMove(getPosition().getX(), getPosition().getY())) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    @Override
    protected ImageIcon getIconPath() {
        return new ImageIcon(isWhite() ? "src/pics/KingWhite.png" : "src/pics/KingBlack.png");
    }
    @Override
    protected ImageIcon getKillIconPath(boolean white){
        return new ImageIcon(isWhite() ? "src/pics/KillTargetIcons/KingWhiteKill.png" : "src/pics/KillTargetIcons/KingBlackKill.png");
    }
}
