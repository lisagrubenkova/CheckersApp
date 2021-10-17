package CheckersGame;

import CheckersGame.view.FieldViewImpl;
import CheckersGame.view.SelectionViewImpl;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.util.Optional;

public class Checkers extends Application implements OnGameFinishedListener {

    private static final double boardSize = 650.0;

    private Pane root;

    @Override
    public void start(Stage primaryStage) {
        root = new Pane();
        Image img = new Image("board.png");
        BackgroundImage board = new BackgroundImage(
                img,
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.DEFAULT,
                new BackgroundSize(100, 100, true, true, true, true)
        );
        root.setBackground(new Background(board));

        FieldViewImpl fieldView = new FieldViewImpl(boardSize);
        Field field = new Field(fieldView, new SelectionViewImpl(fieldView, boardSize / 8, boardSize / 8));
        field.setOnGameFinishedListener(this);
        root.getChildren().add(fieldView);

        Scene scene = new Scene(root, boardSize, boardSize);
        scene.setOnKeyPressed((KeyEvent key) -> {
            if (key.getCode() == KeyCode.ENTER) {
                restart();
            }
        });
        primaryStage.setTitle("CheckersGame.Checkers");
        primaryStage.setResizable(false);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void onGameFinished(PieceType whoWon) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Game finished");
        alert.setContentText((whoWon == PieceType.BLACK ? "Blacks" : "Whites") + " won! Restart?");

        Optional<ButtonType> option = alert.showAndWait();

        if (option.get() == ButtonType.OK) {
            restart();
        }
    }

    private void restart() {
        root.getChildren().clear();

        FieldViewImpl fieldView = new FieldViewImpl(boardSize);
        Field field = new Field(fieldView, new SelectionViewImpl(fieldView, boardSize / 8, boardSize / 8));
        field.setOnGameFinishedListener(this);
        root.getChildren().add(fieldView);
    }
}
