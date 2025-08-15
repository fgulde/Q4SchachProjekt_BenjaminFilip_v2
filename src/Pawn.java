import javax.swing.*;
import java.awt.*;

// Spielfigur: Pawn / Bauer
public class Pawn extends Piece {

    public boolean enPassant = false;

    public Pawn(boolean isWhite, Tile position) {
        super(isWhite, position);
    }

    @Override
    // Methode zum Berechnen aller möglichen / erlaubten Bewegungsrichtungen dieses Figurtypen
    public void calculateNewPos() {
        int direction = isWhite() ? -1 : 1;  // Richtung hängt von der Farbe des Bauern ab
        if (!isMoved()) {
            // Wenn der Bauer noch nicht bewegt wurde, hat er die Option, um zwei Felder zu ziehen
            Tile currentTile = getPosition();
            int newX = currentTile.getX();
            int newY2 = currentTile.getY() + (2 * direction);
            Tile newTile = Board.tiles[newX][newY2];
            int newY1 = currentTile.getY() + direction;
            Tile newTile1 = Board.tiles[newX][newY1];

            // Überprüfen, ob die neuen Positionen valide sind
            if (isEmptyTile(newX, newY1)) {
                JButton newButton1 = createFieldButton(newTile1);
                Board.tiles[newX][newY1].getpTile().add(newButton1);
                Board.tiles[newX][newY2].getpTile().updateUI();
                tryKill(newX, newY1);
                if (isEmptyTile(newX, newY2)) {
                    JButton newButton = createFieldButton(newTile);
                    Board.tiles[newX][newY2].getpTile().add(newButton);
                    Board.tiles[newX][newY2].getpTile().updateUI();
                    newButton.setDefaultCapable(false);

                }
            }
        } else {
            // Falls der Bauer bereits bewegt wurde, kann er nur noch ein Feld ziehen
            Tile currentTile = getPosition();
            int newX = currentTile.getX();
            int newY = currentTile.getY() + direction;
            Tile newTile = Board.tiles[newX][newY];

            // Überprüfen, ob die neue Position innerhalb des Spielbretts liegt
            if (isEmptyTile(newX, newY)) {
                JButton newButton = createFieldButton(newTile);
                Board.tiles[newX][newY].getpTile().add(newButton);
                Board.tiles[newX][newY].getpTile().updateUI();
                tryKill(newX,newY);
            } else {
                tryKill(newX, newY);
            }
        }
    }

    @Override
    // erzeugt, wenn möglich killButtons an Positionen, die die Figur schlagen kann
    public void tryKill(int x, int y) {
        for (int i : new int[]{-1, 1}) {
            int newX = x + i;
            if (newX < 8 && newX >= 0 && canKill(newX, y) /*&& Piece.isMoveSafe(this, newX, y)*/) {
                Piece tempPiece = Board.tiles[newX][y].getOccupyingPiece();
                tempPieces.add(tempPiece);

                spawnKillButton(Board.tiles[newX][y], tempPiece);
            }
        }
    }


    // erzeugt, wenn möglich enPassantButton an Position, die der Bauer schlagen kann
    public void checkEnPassant(int x, int y) {
        for (int i : new int[]{-1, 1}) {
            int newX = x + i;
            int newY = y + (isWhite() ? -1 : 1);

            if (isEmptyTile(newX, newY) && canKill(newX, y) && Board.tiles[newX][y].getOccupyingPiece() instanceof Pawn pawn) {
                if (pawn.isEnPassant()) {
                    // En Passant capture

                    JButton newButton = createEnPassantButton(Board.tiles[newX][newY]);
                    newButton.setSelected(true);
                    newButton.setIcon(pawn.getKillIconPath(pawn.isWhite()));

                    Board.tiles[newX][newY].getpTile().add(newButton);
                    Board.tiles[newX][newY].getpTile().updateUI();

                }
            }
        }
    }

    // Methode zum Umwandeln eines Bauern
    public void promote() {
        if (!Board.status.equals(GameStatus.WHITEWIN) && !Board.status.equals(GameStatus.BLACKWIN)) {
            boolean color = isWhite();
            // Der Bauer erreicht die gegnerische Grundreihe (y = 0 für Weiß, y = 7 für Schwarz)
            // Soundeffekt
            FieldActionListener.NotifySound();
            // Auswahl-UI
            SwingUtilities.invokeLater(() -> {
                ImageIcon[] choices = {new ImageIcon(new ImageIcon(isWhite() ? "src/pics/QueenWhite.png" : "src/pics/QueenBlack.png")
                        .getImage().getScaledInstance(48, 48, Image.SCALE_DEFAULT)),
                        new ImageIcon(new ImageIcon(isWhite() ? "src/pics/RookWhite.png" : "src/pics/RookBlack.png")
                                .getImage().getScaledInstance(48, 48, Image.SCALE_DEFAULT)),
                        new ImageIcon(new ImageIcon(isWhite() ? "src/pics/BishopWhite.png" : "src/pics/BishopBlack.png")
                                .getImage().getScaledInstance(48, 48, Image.SCALE_DEFAULT)),
                        new ImageIcon(new ImageIcon(isWhite() ? "src/pics/KnightWhite.png" : "src/pics/KnightBlack.png")
                                .getImage().getScaledInstance(48, 48, Image.SCALE_DEFAULT))};
                int choice = JOptionPane.showOptionDialog(null, "Wählen Sie eine Figur für die Umwandlung:", "Umwandlung",
                        JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, new ImageIcon("src/pics/Promotion.png"), choices, choices[0]);

                // Soundeffekt
                String promotionSfx = "src/sfx/promotion.wav";
                PieceActionListener.audioPlay(promotionSfx);

                // Ausgewähltes Piece erzeugen
                Piece promotedPiece = switch (choice) {
                    case 0 -> new Queen(color, getPosition());
                    case 1 -> new Rook(color, getPosition());
                    case 2 -> new Bishop(color, getPosition());
                    case 3 -> new Knight(color, getPosition());
                    default ->
                        // Standardauswahl ist Königin, wenn kein anderes Piece ausgewählt wurde
                            new Queen(color, getPosition());
                };

                // Ersetze den Pawn durch die gewählte Figur
                Board.getTiles()[getPosition().getX()][getPosition().getY()].setOccupyingPiece(promotedPiece);
                JButton button = promotedPiece.createPieceButton();
                getPosition().getpTile().remove(0);
                getPosition().getpTile().add(button);
                getPosition().getpTile().updateUI();
                Board.tiles[getPosition().getX()][getPosition().getY()].getButton().setEnabled(false);
                Board.tiles[getPosition().getX()][getPosition().getY()].getButton()
                        .setDisabledIcon(Board.tiles[getPosition().getX()][getPosition().getY()]
                                .getOccupyingPiece().getIconPath());
                getPosition().getpTile().updateUI();
            });
        }
    }

    // getter für enPassant
    public boolean isEnPassant() {
        return enPassant;
    }

    // setter für enPassant
    public void setEnPassant(boolean enPassant) {
        this.enPassant = enPassant;
    }

    @Override
    // getter für den Namen der Klasse
    public String getClassName() {
        return "Bauer";
    }

    @Override
    // getter für das Icon je nach Farbe
    protected ImageIcon getIconPath () {
        return new ImageIcon(isWhite() ? "src/pics/PawnWhite.png" : "src/pics/PawnBlack.png");
    }

    @Override
    // getter für das KillIcon je nach Farbe
    protected ImageIcon getKillIconPath(boolean white){
        return new ImageIcon(white ? "src/pics/KillTargetIcons/PawnWhiteKill.png" : "src/pics/KillTargetIcons/PawnBlackKill.png");
    }
}
