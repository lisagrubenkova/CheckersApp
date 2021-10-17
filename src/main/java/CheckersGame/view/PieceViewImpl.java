package CheckersGame.view;

import CheckersGame.PieceType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class PieceViewImpl extends ImageView implements PieceView {

    private final PieceType pieceType;
    public PieceViewImpl(PieceType pieceType, double size) {
        super(new Image(pieceType == PieceType.WHITE ? "whiteChecker.png" : "blackChecker.png"));
        this.pieceType = pieceType;
        this.setFitHeight(size);
        this.setFitWidth(size);

    }


    @Override
    public void showAsKing() {
        this.setImage(new Image(pieceType == PieceType.WHITE ? "whiteKing.png" : "blackKing.png"));
    }
}
