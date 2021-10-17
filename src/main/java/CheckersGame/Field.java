package CheckersGame;

import CheckersGame.view.FieldView;
import CheckersGame.view.SelectionView;

import java.util.HashSet;
import java.util.Set;

public class Field {
    protected OnGameFinishedListener onGameFinishedListener;

    protected final Selection selection;
    protected final Piece[][] pieces = new Piece[8][8];
    protected PieceType playerSide = PieceType.WHITE;

    protected boolean mustKill = false;
    protected boolean multipleKill = false;

    protected final Set<Piece> whiteCheckers = new HashSet<>();
    protected final Set<Piece> blackCheckers = new HashSet<>();

    protected final FieldView view;

    public Field(FieldView view, SelectionView selectionView) {
        this.view = view;
        selection = new Selection(selectionView);

        for(int i = 0; i < pieces.length; i++) {
            int j = (i % 2 == 0) ? 1 : 0;
            while(j < pieces.length) {
                if (i < 3) {
                    pieces[i][j] = new Piece(PieceType.BLACK, j, i);
                    view.showPiece(pieces[i][j]);
                    blackCheckers.add(pieces[i][j]);
                }
                else if (i > 4) {
                    pieces[i][j] = new Piece(PieceType.WHITE, j, i);
                    view.showPiece(pieces[i][j]);
                    whiteCheckers.add(pieces[i][j]);
                }
                j += 2;
            }
            view.setOnTileSelectedListener((X, Y) -> {
                if (tileContainsChecker(X, Y) && !multipleKill) {
                    if (pieces[Y][X].hasType(playerSide))
                        selection.setTargetAndSelect(pieces[Y][X]);
                }
                else moveType(X, Y);
            });
        }
    }


    protected boolean tileExists(int X, int Y) {
        return X >= 0 && X < 8 && Y >= 0 && Y < 8;
    }

    protected boolean tileContainsChecker(int X, int Y) {
        return pieces[Y][X] != null;
    }

    protected void moveType(int X, int Y) {
        if ((X + Y) % 2 == 1 && selection.isSet()) {
            int diffX = Math.abs(X - selection.getTarget().X);
            int diffY = Math.abs(Y - selection.getTarget().Y);

            int signedDiffY = Y - selection.getTarget().Y;
            boolean moveBack = playerSide == PieceType.WHITE ? signedDiffY > 0 : signedDiffY < 0;
            if (moveBack && !selection.getTarget().isKing()) return;

            if (diffX == 1 && diffY == 1) simpleMove(X, Y);
            else if (diffX == 2 && diffY == 2) killWithCheck(X, Y);
        }
    }

    protected void simpleMove(int X, int Y) {
        if (!mustKill) {
            Piece piece = selection.getTarget();
            moveChecker(piece, X, Y);
            becomeKingWithCheck(piece);
            changePlayer();
        }
    }

    protected void moveChecker(Piece piece, int X, int Y) {
        pieces[piece.Y][piece.X] = null;
        pieces[Y][X] = piece;
        piece.X = X;
        piece.Y = Y;

        view.movePiece(piece);

    }

    protected void removeCapturedChecker(Piece captured) {
            pieces[captured.Y][captured.X] = null;
            if(playerSide == PieceType.WHITE) blackCheckers.remove(captured);
            else whiteCheckers.remove(captured);
            view.removePiece(captured);
    }

    protected void killWithCheck(int X, int Y /* куда ходит */ ) {
        Piece piece = selection.getTarget(); /* кто бьет */
        int capturedX = piece.X + (X - piece.X) / 2; // кого бьет
        int capturedY = piece.Y + (Y - piece.Y) / 2; // кого бьет
        if (tileContainsChecker(capturedX, capturedY)) {
            Piece capturedChecker = pieces[capturedY][capturedX]; // кого бьет

            if (!capturedChecker.hasType(playerSide) && canKillAt(piece, X, Y)) {
                moveChecker(piece, X, Y);
                becomeKingWithCheck(piece);
                removeCapturedChecker(capturedChecker);
                if (canKill(piece)) {
                    selection.setTargetAndSelect(piece);
                    multipleKill = true;
                } else {
                    multipleKill = false;
                    if (gameFinished()) {
                        if (onGameFinishedListener != null)
                            onGameFinishedListener.onGameFinished(whiteCheckers.isEmpty() ? PieceType.BLACK : PieceType.WHITE);
                    } else
                        changePlayer();
                }
            }
        }
    }

    protected boolean canKillAt(Piece piece /* кто бьет */, int X, int Y /* куда бьет */) {
        int diffX = (X - piece.X) / 2;
        int diffY = (Y - piece.Y) / 2;

        boolean moveBack = playerSide == PieceType.WHITE ? diffY > 0 : diffY < 0;
        if (moveBack && !piece.isKing()) return false;

        int capturedX = piece.X + diffX;
        int capturedY = piece.Y + diffY;

        return tileContainsChecker(capturedX, capturedY) &&
                !pieces[capturedY][capturedX].hasType(playerSide) &&
                !tileContainsChecker(X, Y);
    }

    protected boolean canKill(Piece piece /* кто бьет */) {
        int rowShift = 1, colShift = 1, X, Y;
        for(int i = 0, c = 1; i < 4; i++, c *= (-1)) {
            rowShift *= c;
            colShift *= -c;
            X = piece.X + colShift; // кого бьем
            Y = piece.Y + rowShift; // кого бьем

            int signedDiffY = Y - piece.Y;
            boolean moveBack = playerSide == PieceType.WHITE ? signedDiffY > 0 : signedDiffY < 0;
            if (moveBack && !piece.isKing()) continue;

            if(tileExists(X, Y) && tileContainsChecker(X, Y)
                    && !pieces[Y][X].hasType(playerSide)
                    && tileExists(X + colShift, Y + rowShift) && !tileContainsChecker(X + colShift, Y + rowShift)
            ) {
                return true;
            }
        }
        return false;
    }

    protected void changePlayer() {
        playerSide = playerSide == PieceType.WHITE ? PieceType.BLACK : PieceType.WHITE;
        selection.remove();
        selection.setTarget(null);
        Set<Piece> playerCheckers = playerSide == PieceType.WHITE ? whiteCheckers : blackCheckers;
        mustKill = false;
        for(Piece piece : playerCheckers) {
            if(canKill(piece)) {
                mustKill = true;
                break;
            }
        }
    }

    protected boolean gameFinished() {
        return (whiteCheckers.isEmpty() || blackCheckers.isEmpty());
    }

    public OnGameFinishedListener getOnGameFinishedListener() {
        return onGameFinishedListener;
    }

    public void setOnGameFinishedListener(OnGameFinishedListener onGameFinishedListener) {
        this.onGameFinishedListener = onGameFinishedListener;
    }

    protected void becomeKingWithCheck(Piece piece) {
        if (piece.Y == (piece.type == PieceType.WHITE ? 0 : 7)) {
            piece.becomeKing();
            view.setKingAppearance(piece);
        }
    }
}
