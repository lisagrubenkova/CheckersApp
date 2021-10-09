package CheckersGame;

import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;

import java.util.HashSet;
import java.util.Set;

public class Field extends GridPane {
    private static final double fieldSize = 560.0;
    private Selection selection = new Selection();
    private Piece[][] pieces = new Piece[8][8];
    private PieceType playerSide = PieceType.WHITE;

    private boolean mustKill = false;
    private boolean multipleKill = false;

    private Set<Piece> whiteCheckers = new HashSet<>();
    private Set<Piece> blackCheckers = new HashSet<>();
    private Set<Piece> capturedCheckers = new HashSet<>();

    Field() {
        Image blackCheckerImg = new Image("src/main/java/Images/blackChecker.png");
        Image whiteCheckerImg = new Image("src/main/java/Images/whiteChecker.png");
        this.setPrefSize(fieldSize, fieldSize);
        this.relocate(38.0, 38.0);
        selection.setFitHeight(fieldSize / 8.0);
        selection.setFitWidth(fieldSize / 8.0);

        for(int i = 0; i < 8; i++) {
            this.getColumnConstraints().add(new ColumnConstraints(fieldSize / 8.0));
            this.getRowConstraints().add(new RowConstraints(fieldSize / 8.0));
        }
        for(int i = 0; i < pieces.length; i++) {
            int j = (i % 2 == 0) ? 1 : 0;
            while(j < pieces.length) {
                if (i < 3) {
                    pieces[i][j] = new Piece(PieceType.BLACK, blackCheckerImg, fieldSize, i, j);
                    this.add(pieces[i][j], j, i);
                    blackCheckers.add(pieces[i][j]);
                }
                else if (i > 4) {
                    pieces[i][j] = new Piece(PieceType.WHITE, whiteCheckerImg, fieldSize, i, j);
                    this.add(pieces[i][j], j, i);
                    whiteCheckers.add(pieces[i][j]);
                }
                j += 2;
            }
            this.setOnMouseClicked((final MouseEvent click) -> {
                int row = (int) (click.getY() * 8 / fieldSize);
                int col = (int) (click.getX() * 8 / fieldSize);
                if (tileContainsChecker(row, col) && !multipleKill)
                    selectChecker(pieces[row][col]);
                else moveType(row, col);
            });
        }
    }

    private void selectChecker(Piece piece) {
        if (piece.hasType(playerSide)) {
            selection.target = piece;
            this.getChildren().remove(selection);
            this.add(selection, piece.X, piece.Y);
        }
    }

    private boolean tileExists(int X, int Y) {
        return X >= 0 && X < 8 && Y >= 0 && Y < 8;
    }

    private  boolean tileContainsChecker(int X, int Y) {
        return pieces[X][Y] != null;
    }

    private void moveType(int X, int Y) {
        if ((X + Y) % 2 == 1 && selection.isSet()) {
            int diffX = Math.abs(X - selection.target.X);
            int diffY = Math.abs(Y - selection.target.Y);
            if (diffX ==1 && diffY == 1) simpleMove(X, Y);
            else if (diffX == 2 && diffY == 2) kill(X, Y);
        }
    }

    private void simpleMove(int X, int Y) {
        if (!mustKill) {
            Piece piece = selection.target;
            boolean moveBack = (playerSide == PieceType.BLACK) != (X - piece.X > 0);
            if (!moveBack || piece.isKing()) {
                moveChecker(piece, X, Y);
                piece.becomeKing();
                changePlayer();
            }
        }
    }

    private void moveChecker(Piece piece, int X, int Y) {
        pieces[piece.X][piece.Y] = null;
        pieces[X][Y] = piece;
        this.getChildren().remove(piece);
        this.add(piece, X, Y);
        piece.X = X;
        piece.Y = Y;
    }

    private void removeCapturedCheckers() {
        for(Piece captured : capturedCheckers) {
            pieces[captured.X][captured.Y] = null;
            if(playerSide == PieceType.WHITE) blackCheckers.remove(captured);
            else whiteCheckers.remove(captured);
            this.getChildren().remove(captured);
        }
    }

    private void kill(int X, int Y) {
        Piece piece = selection.target;
        int capturedX = piece.X + (X - piece.X) / 2;
        int capturedY = piece.Y + (Y - piece.Y) / 2;
        if (tileContainsChecker(capturedX, capturedY)) {
            Piece capturedChecker = pieces[capturedY][capturedX];

            if (!capturedChecker.hasType(playerSide)) {
                moveChecker(piece, X, Y);
                piece.becomeKing();
                if (canKill(piece)) {
                    selectChecker(piece);
                    multipleKill = true;
                } else {
                    removeCapturedCheckers();
                    multipleKill = false;
                    changePlayer();
                }
            }
        }
    }

    private boolean canKill(Piece piece) {
        int rowShift = 1, colShift = 1, X, Y;
        for(int i = 0, c = 1; i < 4; i++, c *= (-1)) {
            rowShift *= c;
            colShift *= -c;
            X = piece.X + rowShift;
            Y = piece.Y + colShift;
            if(tileExists(X, Y) && tileContainsChecker(X, Y)
                    && !pieces[X][Y].hasType(playerSide)
                    && !capturedCheckers.contains(pieces[X][Y])) {
                X += rowShift;
                Y += colShift;
                if(tileExists(X, Y) && !tileContainsChecker(X, Y)) return true;
            }
        }
        return false;
    }

    private void changePlayer() {
        playerSide = playerSide == PieceType.WHITE ? PieceType.BLACK : PieceType.WHITE;
        this.getChildren().remove(selection);
        selection.target = null;
        capturedCheckers.clear();
        Set<Piece> playerCheckers = playerSide == PieceType.WHITE ? whiteCheckers : blackCheckers;
        mustKill = false;
        for(Piece piece : playerCheckers) {
            if(canKill(piece)) {
                mustKill = true;
                break;
            }
        }
    }
}
