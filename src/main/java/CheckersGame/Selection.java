package CheckersGame;

import CheckersGame.view.SelectionView;

public class Selection {

    private Piece target = null;
    private final SelectionView view;

    public Selection(SelectionView view) {
        this.view = view;
    }

    public boolean isSet() {
        return target != null;
    }

    public void set() {
        view.set(target.X, target.Y);
    }

    public void remove() {
        view.remove();
    }

    public void setTargetAndSelect(Piece target) {
        this.target = target;
        set();
    }

    public void setTarget(Piece target) {
        this.target = target;
    }

    public Piece getTarget() {
        return this.target;
    }
}
