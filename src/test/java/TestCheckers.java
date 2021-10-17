import CheckersGame.Field;
import CheckersGame.Piece;
import CheckersGame.PieceType;
import CheckersGame.view.FieldView;
import CheckersGame.view.SelectionView;
import org.junit.Test;
import org.junit.jupiter.api.RepeatedTest;

import java.util.Random;

import static org.junit.Assume.assumeFalse;
import static org.junit.Assume.assumeTrue;
import static org.junit.jupiter.api.Assertions.*;


public class TestCheckers extends Field {
    private static FieldView.OnTileSelectedListener onTileSelectedListener;

    private void clickAt(int X, int Y) {
        onTileSelectedListener.onTileSelected(X, Y);
    }

    public TestCheckers() {
        super(new FieldView() {
                  @Override
                  public void showPiece(Piece piece) {

                  }

                  @Override
                  public void setKingAppearance(Piece piece) {

                  }

                  @Override
                  public void movePiece(Piece piece) {

                  }

                  @Override
                  public void removePiece(Piece piece) {

                  }

                  @Override
                  public void setOnTileSelectedListener(OnTileSelectedListener listener) {
                      onTileSelectedListener = listener;
                  }
              },
                new SelectionView() {
                    @Override
                    public void add(int X, int Y) {

                    }

                    @Override
                    public void remove() {

                    }
                });
    }

    private int piecesCount() {
        int count = 0;
        for (Piece[] piecesRow : pieces)
            for (Piece piece : piecesRow) {
                if (piece != null)
                    count++;
            }
        return count;
    }

    @Test
    public void testInitialState() {
        assertEquals(playerSide, PieceType.WHITE);
        assertEquals(piecesCount(), whiteCheckers.size() + blackCheckers.size());

        for (Piece whiteChecker: whiteCheckers) {
            assertTrue(whiteChecker.Y > 4);
            assertNotEquals(whiteChecker.X + whiteChecker.Y, 0);
        }

        for (Piece blackChecker: blackCheckers) {
            assertTrue(blackChecker.Y < 3);
            assertNotEquals(blackChecker.X + blackChecker.Y, 0);
        }
    }

    @Test
    @RepeatedTest(100)
    public void testSelection() {
        int clickedX = new Random().nextInt(8), clickedY = new Random().nextInt(8);
        assumeTrue(tileContainsChecker(clickedX, clickedY));
        assumeTrue(pieces[clickedY][clickedX].type == playerSide);

        clickAt(clickedX, clickedY);
        assertEquals(selection.getTarget(), pieces[clickedY][clickedX]);
    }

    @Test
    @RepeatedTest(100)
    public void testMovement() {
        int clickedX = new Random().nextInt(8), clickedY = new Random().nextInt(8);
        assumeTrue(tileContainsChecker(clickedX, clickedY));
        assumeTrue(pieces[clickedY][clickedX].type == playerSide);

        Piece piece = pieces[clickedY][clickedX];
        clickAt(clickedX, clickedY);

        assumeFalse(mustKill);

        int diffY = playerSide == PieceType.WHITE ? -1 : 1;
        int whereMoveX = clickedX - 1, whereMoveY = clickedY + diffY;
        assumeTrue(tileExists(whereMoveX, whereMoveY));
        assumeFalse(tileContainsChecker(whereMoveX, whereMoveY));


        clickAt(whereMoveX, whereMoveY);

        assertNull(pieces[clickedY][clickedX]);
        assertNotNull(pieces[whereMoveY][whereMoveX]);
        assertEquals(piece, pieces[whereMoveY][whereMoveX]);

        assertEquals(whereMoveY, piece.Y);
        assertEquals(whereMoveX, piece.X);

    }

    @Test
    @RepeatedTest(500)
    public void testMovementsChain() {
        while (true) {
            int clickedX = new Random().nextInt(8), clickedY = new Random().nextInt(8);
            if (!tileContainsChecker(clickedX, clickedY)) return;
            if (pieces[clickedY][clickedX].type != playerSide) return;

            Piece piece = pieces[clickedY][clickedX];
            clickAt(clickedX, clickedY);

            if (mustKill) return;

            int diffY = playerSide == PieceType.WHITE ? -1 : 1;
            int whereMoveX = clickedX - 1, whereMoveY = clickedY + diffY;
            if (!tileExists(whereMoveX, whereMoveY)) return;
            if (tileContainsChecker(whereMoveX, whereMoveY)) return;


            clickAt(whereMoveX, whereMoveY);

            assertNull(pieces[clickedY][clickedX]);
            assertNotNull(pieces[whereMoveY][whereMoveX]);
            assertEquals(piece, pieces[whereMoveY][whereMoveX]);

            assertEquals(whereMoveY, piece.Y);
            assertEquals(whereMoveX, piece.X);
        }
    }

    @Test
    public void testKill() {
        assertEquals(playerSide, PieceType.WHITE);

        clickAt(6 ,5);
        clickAt(5, 4);

        assertEquals(playerSide, PieceType.BLACK);

        clickAt(3, 2);
        clickAt(4, 3);

        assertEquals(playerSide, PieceType.WHITE);

        clickAt(5, 4);
        clickAt(3, 2);

        assertNull(pieces[3][4]);
        assertNotEquals(whiteCheckers.size(), blackCheckers.size());
    }

    @Test
    public void noOneIsKing() {
        for (Piece piece: whiteCheckers) {
            assertFalse(piece.king);
        }

        for (Piece piece: blackCheckers) {
            assertFalse(piece.king);
        }
    }



    @Test
    public void becomeKing() {
        int whites = whiteCheckers.size(), blacks = blackCheckers.size();
        clickAt(6, 5);
        clickAt(5, 4); // 1w

        clickAt(3, 2); // 2b
        clickAt(4, 3);

        clickAt(5, 4); // 3w
        clickAt(3, 2);
        blacks--;

        assertNull(pieces[3][4]);
        assertEquals(blacks, blackCheckers.size());

        clickAt(2, 1); // 4b
        clickAt(4, 3);
        whites--;

        assertNull(pieces[3][2]);
        assertEquals(whites, whiteCheckers.size());

        clickAt(7, 6); // 5w
        clickAt(6, 5);

        clickAt(4, 3); // 6b
        clickAt(5, 4);

        clickAt(6, 5); // 7w
        clickAt(4, 3);
        blacks--;

        assertNull(pieces[4][5]);
        assertEquals(blacks, blackCheckers.size());

        clickAt(5, 2); // 8b
        clickAt(3, 4);
        whites--;

        assertNull(pieces[3][4]);
        assertEquals(whites, whiteCheckers.size());

        clickAt(2, 5); // 9w
        clickAt(4, 3);
        blacks--;

        assertNull(pieces[4][3]);
        assertEquals(blacks, blackCheckers.size());

        clickAt(1, 2); // 10b
        clickAt(2, 3);

        clickAt(4, 3); // 11w
        clickAt(3, 2);

        clickAt(1, 0); // 12b
        clickAt(2, 1);

        clickAt(3, 2); // 13w
        clickAt(1, 0);
        blacks--;

        assertNull(pieces[1][2]);
        assertEquals(blacks, blackCheckers.size());

        assertTrue(pieces[0][1].king);

    }
}
