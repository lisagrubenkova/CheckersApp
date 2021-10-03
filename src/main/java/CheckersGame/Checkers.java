package CheckersGame;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.stage.Stage;


import java.io.InputStream;

import static javafx.scene.layout.BackgroundRepeat.NO_REPEAT;

public class Checkers extends Application {

    private static final double boardSize = 650.0;

    public static final int TILE_SIZE = 70;
    public static final int WIDTH = 8;
    public static final int HEIGHT = 8;

    private Tile[][] board = new Tile[WIDTH][HEIGHT];

    private Group tileGroup = new Group();
    private Group pieceGroup = new Group();

    //public Parent createContent() {
    //    Pane root = new Pane();
     //   root.setPrefSize(WIDTH * TILE_SIZE, HEIGHT * TILE_SIZE);
     //   root.getChildren().addAll(tileGroup, pieceGroup);

     //   for (int y = 0; y < HEIGHT; y++) {
     //       for (int x = 0; x < WIDTH; x++) {
     //           CheckersGame.Tile tile = new CheckersGame.Tile((x + y) % 2 == 0, x, y);
     //           board[x][y] = tile;

     //           tileGroup.getChildren().add(tile);

     //           CheckersGame.Piece piece = null;

     //        if (y <= 2 && (x + y) % 2 != 0) {
    //        piece = makePiece(CheckersGame.PieceType.BLACK, x, y);
     //           }

     //           if (y >= 5 && (x + y) % 2 != 0) {
     //               piece = makePiece(CheckersGame.PieceType.WHITE, x, y);
     //           }

     //           if (piece != null) {
     //               tile.setPiece(piece);
     //               pieceGroup.getChildren().add(piece);
     //           }
     //       }
     //   }

     //   return root;
    //}

    @Override
    public void start(Stage primaryStage) {
        Pane root = new Pane();
        Image img = new Image("src/main/java/Images/board.png");
        BackgroundImage board = new BackgroundImage(img, BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT);
        root.setBackground(new Background(board));
        root.getChildren().add(new Field());
        Scene scene = new Scene(root, boardSize, boardSize);
        scene.setOnKeyPressed((KeyEvent key) -> {
            if(key.getCode() == KeyCode.ENTER) {
                root.getChildren().clear();
                root.getChildren().add(new Field());
            }
        });
        primaryStage.setTitle("CheckersGame.Checkers");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    //  private CheckersGame.MoveResult tryMove(CheckersGame.Piece piece, int newX, int newY) {
    //    if (board[newX][newY].hasPiece() || (newX + newY) % 2 == 0) {
    //        return new CheckersGame.MoveResult(CheckersGame.MoveType.NONE);
    //    }

    //    int x0 = toBoard(piece.getOldX());
    //    int y0 = toBoard(piece.getOldY());

    //    if (newY + piece.getType().moveDir == 8 || newY + piece.getType().moveDir == -1) {
    //        piece.king = true;
    //    }
    //    if (Math.abs(newX - x0) == 1 && (newY - y0 == piece.getType().moveDir || piece.king)) {
    //        return new CheckersGame.MoveResult(CheckersGame.MoveType.NORMAL);
    //    } else if (Math.abs(newX - x0) == 2 && (newY - y0 == piece.getType().moveDir * 2 || piece.king)) {

    //        int x1 = x0 + (newX - x0) / 2;
    //        int y1 = y0 + (newY - y0) / 2;

    //        if (board[x1][y1].hasPiece() && board[x1][y1].getPiece().getType() != piece.getType()) {
    //            return new CheckersGame.MoveResult(CheckersGame.MoveType.KILL, board[x1][y1].getPiece());
    //        }
    //    }

    //    return new CheckersGame.MoveResult(CheckersGame.MoveType.NONE);
    //}

    //  private int toBoard(double pixel) {
    //    return (int)(pixel + TILE_SIZE / 2) / TILE_SIZE;
    // }

    //private CheckersGame.Piece makePiece(CheckersGame.PieceType type, int x, int y) {
    //    CheckersGame.Piece piece = new CheckersGame.Piece(type, x, y);

    //    piece.setOnMouseReleased(e -> {
    //        int newX = toBoard(piece.getLayoutX());
     //       int newY = toBoard(piece.getLayoutY());

    //        CheckersGame.MoveResult result;

    //        if (newX < 0 || newY < 0 || newX >= WIDTH || newY >= HEIGHT) {
    //            result = new CheckersGame.MoveResult(CheckersGame.MoveType.NONE);
    //        } else {
    //            result = tryMove(piece, newX, newY);
    //        }

    //        int x0 = toBoard(piece.getOldX());
    //        int y0 = toBoard(piece.getOldY());

    //        switch (result.getType()) {
    //            case NONE:
    //                piece.abortMove();
    //                break;
    //            case NORMAL:
    //                piece.move(newX, newY);
    //                board[x0][y0].setPiece(null);
    //                board[newX][newY].setPiece(piece);
      //              break;
     //           case KILL:
     //               piece.move(newX, newY);
     //               board[x0][y0].setPiece(null);
     //               board[newX][newY].setPiece(piece);

     //               CheckersGame.Piece otherPiece = result.getPiece();
     //               board[toBoard(otherPiece.getOldX())][toBoard(otherPiece.getOldY())].setPiece(null);
     //               pieceGroup.getChildren().remove(otherPiece);
     //               break;
     //       }
     //   });

     //   return piece;
    //}
}
