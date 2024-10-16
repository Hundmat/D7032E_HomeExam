package game.piles;


import game.piles.CardSalad.Vegetable;


public class SaladcardFactory {
    // Helper method to create a Point Salad card
    public CardSalad createPointSaladCard(Vegetable veggie, String criteria) {
        return new CardSalad(veggie, criteria);
    }


}
