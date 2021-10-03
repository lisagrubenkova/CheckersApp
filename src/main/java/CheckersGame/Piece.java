package CheckersGame;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Piece extends ImageView {
    private PieceType type;

    public boolean king = false;
    private double oldX, oldY;
    int X, Y;
    private int kingCondition;

    public Piece(PieceType type, Image img, double fieldSize, int x, int y) {
        super(img);
        this.type = type;
        oldX = x;
        oldY = y;
        kingCondition = type == type.WHITE ? 0 : 7;
        this.setFitHeight(fieldSize / 8.0);
        this.setFitWidth(fieldSize / 8.0);
    }

    boolean hasType(PieceType newType) {
        return type == newType;
    }

    boolean isKing() { return king; }

    public void becomeKing() {
        if (oldX == kingCondition) {
            this.setImage(new Image(
                    type == type.WHITE ? "CheckersApp/src/main/java/Images/whiteKing.png" : "CheckersApp/src/main/java/Images/blackKing.png"
            ));
            king = true;
        }
    }
}
