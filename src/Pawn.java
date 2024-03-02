import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

// Spielfigur: Pawn / Bauer
public class Pawn extends Piece {

    public boolean enPassant = false;

    public Pawn(boolean isWhite, boolean killed, Tile position) {
        super(isWhite, killed, position);
    }

    @Override
    public void calculateNewPos() {
        if (!isMoved()) {
            // Wenn der Bauer noch nicht bewegt wurde, hat er die Option, um zwei Felder zu ziehen
            int direction = isWhite() ? -1 : 1;  // Richtung hängt von der Farbe des Bauern ab
            Tile currentTile = getPosition();
            int newX = currentTile.getX();
            int newY = currentTile.getY() + (2 * direction);
            Tile newTile = Board.tiles[newX][newY];
            int newY1 = currentTile.getY() + direction;
            Tile newTile1 = Board.tiles[newX][newY1];

            // Überprüfen, ob die neuen Positionen innerhalb des Spielbretts liegen
            if (isValidMove(newX, newY) && isValidMove(newX, newY1)) {
                JButton newButton = createFieldButton(newTile);
                Board.tiles[newX][newY].getpTile().add(newButton);
                Board.tiles[newX][newY].getpTile().updateUI();
                newButton.setDefaultCapable(false);
            }
            if (isValidMove(newX, newY1)) {
                JButton newButton1 = createFieldButton(newTile1);
                Board.tiles[newX][newY1].getpTile().add(newButton1);
                Board.tiles[newX][newY].getpTile().updateUI();
                tryKill(newX, newY1);
            }
        } else {
            // Falls der Bauer bereits bewegt wurde, kann er nur noch ein Feld ziehen
            int direction = isWhite() ? -1 : 1;  // Richtung hängt von der Farbe des Bauern ab
            Tile currentTile = getPosition();
            int newX = currentTile.getX();
            int newY = currentTile.getY() + direction;
            Tile newTile = Board.tiles[newX][newY];

            // Überprüfen, ob die neue Position innerhalb des Spielbretts liegt
            if (isValidMove(newX, newY)) {
                JButton newButton = createFieldButton(newTile);
                Board.tiles[newX][newY].getpTile().add(newButton);
                Board.tiles[newX][newY].getpTile().updateUI();
                tryKill(newX,newY);
            } else {
                tryKill(newX, newY);
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
        for (int i : new int[]{-1, 1}) {
            int newX = x + i;
            if (canKill(newX, y)) {
                Piece tempPiece = Board.tiles[newX][y].getOccupyingPiece();
                tempPieces = Arrays.copyOf(tempPieces, tempPieces.length + 1);
                tempPieces[tempPieces.length - 1] = tempPiece;

                JButton newButton = createFieldButton(Board.tiles[newX][y]);
                newButton.setSelected(true);
                newButton.setIcon(tempPiece.getKillIconPath(tempPiece.isWhite()));

                Board.tiles[newX][y].getpTile().remove(0);
                Board.tiles[newX][y].getpTile().add(newButton);
                Board.tiles[newX][y].getpTile().updateUI();
            }
        }
    }

    public void checkEnPassant(int x, int y) {
        for (int i : new int[]{-1, 1}) {
            int newX = x + i;
            int newY = y + (isWhite() ? -1 : 1);

            if (isValidMove(newX, newY) && canKill(newX, y)) {
                Piece tempPiece = Board.tiles[newX][y].getOccupyingPiece();
                System.out.println("yey");
                if (tempPiece instanceof Pawn && ((Pawn) tempPiece).isEnPassant()) {
                    // En Passant capture

                    JButton newButton = createFieldButton(Board.tiles[newX][newY]);
                    newButton.setSelected(true);
                    newButton.setIcon(tempPiece.getKillIconPath(tempPiece.isWhite()));

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
                try {
                    AudioInputStream ais = AudioSystem.getAudioInputStream(new File(promotionSfx));
                    Clip clip = AudioSystem.getClip();
                    clip.open(ais);
                    clip.setFramePosition(0);
                    clip.start();
                } catch (UnsupportedAudioFileException | IOException | LineUnavailableException ex) {
                    throw new RuntimeException(ex);
                }

                // Ausgewähltes Piece erzeugen
                Piece promotedPiece = switch (choice) {
                    case 0 -> new Queen(color, false, getPosition());
                    case 1 -> new Rook(color, false, getPosition());
                    case 2 -> new Bishop(color, false, getPosition());
                    case 3 -> new Knight(color, false, getPosition());
                    default ->
                        // Standardauswahl ist Königin, wenn kein anderes Piece ausgewählt wurde
                            new Queen(color, false, getPosition());
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

    public boolean isEnPassant() {
        return enPassant;
    }

    public void setEnPassant(boolean enPassant) {
        this.enPassant = enPassant;
    }

    @Override
    public String getClassName() {
        return "Bauer";
    }

    @Override
    protected ImageIcon getIconPath () {
        return new ImageIcon(isWhite() ? "src/pics/PawnWhite.png" : "src/pics/PawnBlack.png");
    }

    @Override
    protected ImageIcon getKillIconPath(boolean white){
        return new ImageIcon(white ? "src/pics/KillTargetIcons/PawnWhiteKill.png" : "src/pics/KillTargetIcons/PawnBlackKill.png");
    }
}
