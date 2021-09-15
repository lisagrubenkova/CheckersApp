import javafx.stage.Stage;
import org.junit.Assert.*;
import org.junit.Test;
import static org.junit.jupiter.api.Assertions.*;


public class TestCheckers {

    private Piece checker = new Piece(PieceType.WHITE, 0, 2);
    private Tile tile = new Tile(false, 1, 0);

    @Test
    public void checkerMove() {
        checker.move(1, 3);
        assertEquals(70.0, checker.getOldX());
        assertEquals(210.0, checker.getOldY());
    }

    @Test
    public void checkerType() {
        assertEquals(PieceType.WHITE, checker.getType());
    }

    @Test
    public void checkTile() {
        tile.setPiece(checker);
        assertEquals(true, tile.hasPiece());
    }
}
