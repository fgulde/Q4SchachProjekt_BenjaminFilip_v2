/*
Die Piece Klasse ist eine Vorlage für alle Spielfigur-Klassen. Sie legt einige universelle Attribute und Methoden fest,
die alle Spielfiguren enthalten müssen. Dadurch wird sichergestellt, dass der Code einheitlich und übersichtlich bleibt.
 */
public abstract class Piece {
    private boolean white; // true = weißes Piece, false = schwarzes Piece
    private boolean killed; // Gibt an, ob die Spielfigur geschlagen wurde
    private Tile position; // Position des Piece, in Form eines Tiles


    public Piece(boolean white, boolean killed, Tile position) {
        this.white = white;
        this.killed = killed;
        this.position = position;
    }

    // Getter und Setter für die Position
    public Tile getPosition() {
        return this.position;
    }

    public void setPosition(Tile position) {
        this.position = position;
    }


    public abstract void move();

}
