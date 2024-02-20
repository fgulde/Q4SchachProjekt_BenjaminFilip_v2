import javax.swing.*;
public class King extends Piece {
    public King(boolean isWhite, boolean killed, Tile position) {
        super(isWhite, killed, position);
    }

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

    public void castle(Tile rookTile) {
        // Check conditions for castling
        if (!isMoved() && !rookTile.getOccupyingPiece().isMoved()) {
            // Check if there are no pieces between the king and the rook
            for (int direction = -1; direction <= 1; direction =+ 2) {
                int startCol = getPosition().getX() + direction;
                int destCol = getPosition().getX() + 2*direction;
                int endCol = rookTile.getX();
                boolean piecesBetween = false;
                for (int col = startCol; col != endCol; col += direction) {
                    if (Board.tiles[col][getPosition().getY()].getOccupyingPiece() != null) {
                        piecesBetween = true;
                        break;
                    }
                }
                if (!piecesBetween) {
                    // Check if the king is not in check and the squares are not under attack
                    if (!isInCheck() && !isInCheckAfterMove(startCol, endCol)) {
                        // Display the castling option button
                        JButton castleButton = createCastleButton(Board.tiles[destCol][getPosition().getY()]);
                        if (Board.tiles[destCol][getPosition().getY()].getpTile().getComponentCount() > 0) {
                            Board.tiles[destCol][getPosition().getY()].getpTile().remove(0);
                        }
                        Board.tiles[destCol][getPosition().getY()].getpTile().add(castleButton);
                        Board.tiles[destCol][getPosition().getY()].getpTile().updateUI();
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

    public boolean isInCheckAfterMove(int startX, int endX) {
        // Simulate the move and check if the king is in check
        Piece originalPiece = Board.tiles[endX][getPosition().getY()].getOccupyingPiece();
        Board.tiles[endX][getPosition().getY()].setOccupyingPiece(this);
        Board.tiles[startX][getPosition().getY()].setOccupyingPiece(null);

        boolean inCheck = isInCheck();

        // Undo the move
        Board.tiles[startX][getPosition().getY()].setOccupyingPiece(this);
        Board.tiles[endX][getPosition().getY()].setOccupyingPiece(originalPiece);

        return inCheck;
    }

    @Override
    protected ImageIcon getIconPath() {
        return new ImageIcon(isWhite() ? "src/pics/KingWhite.png" : "src/pics/KingBlack.png");
    }
}
