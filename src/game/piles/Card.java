package game.piles;

public abstract class Card {
    
    
    // Field to track whether the criteria side is up
    protected boolean criteriaSideUp = true;
    protected String criteria;
    protected Enum type;
    // Abstract method to flip the card, must be implemented by subclasses

    
    // Abstract method to get the card type (enum value)
    public Enum getCardType(){
        return this.type;
    }


    public void flipSide() {
        this.criteriaSideUp = !this.criteriaSideUp;
    }

    // Concrete method to check if the criteria side is up
    public boolean isCriteriaSideUp() {
        return criteriaSideUp;
    }
    public String getCriteria() {
        return criteria;
    }

    


    // Optional concrete method for formatting card details
    @Override
    public abstract String toString();
}
