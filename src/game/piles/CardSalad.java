package game.piles;


public class CardSalad extends Card {
    public enum Vegetable {
        PEPPER, LETTUCE, CARROT, CABBAGE, ONION, TOMATO
    }

    public Vegetable vegetable;

    public CardSalad(Vegetable vegetable, String criteria) {
        this.vegetable = vegetable;
        this.criteria = criteria;
    }

    public Enum<Vegetable> getCardType(String type) {
        return Vegetable.valueOf(type);
    }

    public void flipSide() {
        if(this.criteriaSideUp) {
            this.criteriaSideUp = !this.criteriaSideUp;
        }else {
            throw new IllegalStateException("Cannot flip card from type side to criteria side");
        }
    }

    @Override
    public String toString() {
        if(this.criteriaSideUp) {
            return this.criteria + " (" + this.vegetable + ")";
        } else {
            return this.vegetable.toString();
        }
    }

    public boolean criteriaSideUp() {
        return this.criteriaSideUp;
    }

    

    @Override
    public Vegetable getCardType() {
        return this.vegetable;
    }

    public String getCriteria() {
        return this.criteria;
    }
}
