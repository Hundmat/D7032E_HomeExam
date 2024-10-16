package game.piles;

public abstract class Card<T extends Enum<T>> {
    
    // Field to track whether the criteria side is up
    protected boolean criteriaSideUp = true;

    // Abstract method to flip the card, must be implemented by subclasses
    public abstract void flipSide();
    
    // Abstract method to get the card type (enum value)
    public abstract T getCardType();

    // Concrete method to check if the criteria side is up
    public boolean isCriteriaSideUp() {
        return criteriaSideUp;
    }

    // Optional concrete method for formatting card details
    @Override
    public abstract String toString();
}
