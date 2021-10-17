package CheckersGame.view;

import CheckersGame.Piece;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;

import java.util.HashMap;
import java.util.Map;


public class FieldViewImpl extends GridPane implements FieldView {
    private final double fieldSize;

    private final Map<Piece, PieceViewImpl> pieces = new HashMap<>();

    public FieldViewImpl(double fieldSize) {
        this.fieldSize = fieldSize;
        this.setPrefSize(fieldSize, fieldSize);

        for(int i = 0; i < 8; i++) {
            this.getColumnConstraints().add(new ColumnConstraints(fieldSize / 8.0));
            this.getRowConstraints().add(new RowConstraints(fieldSize / 8.0));
        }
    }

    @Override
    public void showPiece(Piece piece) {
        this.add(getOrAddPieceView(piece), piece.X, piece.Y);
    }

    @Override
    public void setKingAppearance(Piece piece) {
        getOrAddPieceView(piece).showAsKing();
    }

    @Override
    public void movePiece(Piece piece) {
        this.getChildren().remove(getOrAddPieceView(piece));
        this.add(getOrAddPieceView(piece), piece.X, piece.Y);
    }

    @Override
    public void removePiece(Piece piece) {
        this.getChildren().remove(getOrAddPieceView(piece));
    }

    @Override
    public void setOnTileSelectedListener(OnTileSelectedListener listener) {
        setOnMouseClicked((final MouseEvent click) -> {
            int row = (int) (click.getY() / (fieldSize / 8));
            int col = (int) (click.getX() / (fieldSize / 8));

            if (row >= 0 && row <= 7 && col >= 0 && col <= 7) {
                listener.onTileSelected(col, row);
            }
        });
    }

    private PieceViewImpl getOrAddPieceView(Piece piece) {
        if (pieces.containsKey(piece)) return pieces.get(piece);

        PieceViewImpl pieceView = new PieceViewImpl(piece.type, fieldSize / 8);
        pieces.put(piece, pieceView);
        return pieceView;
    }
}
