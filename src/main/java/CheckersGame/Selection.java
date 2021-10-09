package CheckersGame;

import javafx.scene.image.ImageView;

public class Selection extends ImageView {
    Piece target = null;
    boolean isSet() {
        return target != null;
    }
}
