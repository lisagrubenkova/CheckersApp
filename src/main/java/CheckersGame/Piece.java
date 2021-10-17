package CheckersGame;

public class Piece {
    public final PieceType type;

    public boolean king = false;
    public int X, Y;
    private final int kingCondition;

    public Piece(PieceType type, int x, int y) {
        this.type = type;
        X = x;
        Y = y;
        kingCondition = type == PieceType.WHITE ? 0 : 7;
    }

    boolean hasType(PieceType newType) {
        return type == newType;
    }

    boolean isKing() { return king; }

    public void becomeKing() {
        if (Y == kingCondition) {
            king = true;
        }
    }

}
