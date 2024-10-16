package game.piles;


public class CardSalad extends Card<CardSalad.Vegetable> {
    public enum Vegetable {
        PEPPER, LETTUCE, CARROT, CABBAGE, ONION, TOMATO
    }

    public Vegetable vegetable;
    public String criteria;
    public boolean criteriaSideUp = true;

    public CardSalad(Vegetable vegetable, String criteria) {
        this.vegetable = vegetable;
        this.criteria = criteria;
    }

    public void flipSide() {
        criteriaSideUp = !criteriaSideUp;
    }

    @Override
    public String toString() {
        if(criteriaSideUp) {
            return criteria + " (" + vegetable + ")";
        } else {
            return vegetable.toString();
        }
    }

    public boolean criteriaSideUp() {
        return criteriaSideUp;
    }

    @Override
    public Vegetable getCardType() {
        return this.vegetable;
    }
}
