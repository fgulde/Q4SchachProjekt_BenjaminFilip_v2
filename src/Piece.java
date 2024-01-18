public abstract class Piece {
    boolean white = true;
    boolean killed = false;

    public Piece(boolean white, boolean killed) {
        this.white = white;
        this.killed = killed;
    }

    public abstract void move();
}
