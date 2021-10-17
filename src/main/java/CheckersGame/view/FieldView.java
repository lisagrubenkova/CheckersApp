package CheckersGame.view;

import CheckersGame.Piece;

public interface FieldView {

    void showPiece(Piece piece);
    void setKingAppearance(Piece piece);

    void movePiece(Piece piece);

    void removePiece(Piece piece);

    void setOnTileSelectedListener(OnTileSelectedListener listener);


    interface OnTileSelectedListener {
        void onTileSelected(int X, int Y);
    }
}
