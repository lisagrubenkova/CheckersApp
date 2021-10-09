package CheckersGame;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class Checkers extends Application {

    private static final double boardSize = 650.0;

    @Override
    public void start(Stage primaryStage) {
        Pane root = new Pane();
         Image img = new Image("src/main/java/Images/board.png");
         BackgroundImage board = new BackgroundImage(img, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT,
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
}
