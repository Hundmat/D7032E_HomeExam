package game.piles;

import java.util.ArrayList;

public abstract class PileHandler {
    protected ArrayList<Pile> piles;
    
    public Pile getPile(int pileIndex) {
        return piles.get(pileIndex);
    }

    public ArrayList<Pile> getPiles() {
        return piles;
    }

    public boolean isEmpty() {
        return piles.isEmpty();
    }
    
}
