public abstract class Piece {
    private boolean white;
    private boolean killed = false;
    private Tile position;


    public Piece(boolean white, Tile position) {
        this.white = white;
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
