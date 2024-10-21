package game.piles;

import java.util.ArrayList;

public abstract class PileHandler {
    protected ArrayList<Pile> piles;
    int size;

    public PileHandler(int size){
        this.size = size;
        this.piles = new ArrayList<>(this.size);
        for (int i = 0; i < size; i++) {
            this.piles.add(new Pile()); 
        }
    }
    
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
