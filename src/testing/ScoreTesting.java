package testing;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;

import game.piles.CardSalad;
import game.players.HumanPlayer;
import game.players.Player;
import game.players.PlayerHand;
import game.score.CalculateScore;
import game.piles.Card;

import org.junit.jupiter.api.Test;


public class ScoreTesting {
    private PlayerHand hand;
    private ArrayList<Player> players;
    private Player player1;
    private Player player2;

    @BeforeEach
    public void setUp() {
        
        players = new ArrayList<>();
        player1 = new HumanPlayer(0,null, null, null,false);
        player2 = new HumanPlayer(0,null, null, null,false);
        players.add(player1);
        players.add(player2);
        hand = new PlayerHand(players.size());
    }

    @Test
    public void testCalculateScoreMostLettuce() {
        // Setup player hand with criteria card
        Card criteriaCard = new CardSalad(CardSalad.Vegetable.PEPPER, "MOST LETTUCE = 10");
        hand.addCard(player1.getPlayerID(), criteriaCard);
        hand.addCard(player1.getPlayerID(), new CardSalad(CardSalad.Vegetable.LETTUCE, ""));
        hand.addCard(player1.getPlayerID(), new CardSalad(CardSalad.Vegetable.LETTUCE, ""));

        // Setup other player's hand
        hand.addCard(player2.getPlayerID(), new CardSalad(CardSalad.Vegetable.LETTUCE, ""));

        CalculateScore calculateScore = new CalculateScore(hand, player1.getPlayerID(), players);
        assertEquals(10, calculateScore.returnScore());
    }

    @Test
    public void testCalculateScoreFewestPepper() {
        // Setup player hand with criteria card
        Card criteriaCard = new CardSalad(CardSalad.Vegetable.LETTUCE, "FEWEST PEPPER = 7");
        hand.addCard(player1.getPlayerID(), criteriaCard);

        // Setup other player's hand
        hand.addCard(player2.getPlayerID(), new CardSalad(CardSalad.Vegetable.PEPPER, ""));

        CalculateScore calculateScore = new CalculateScore(hand, player1.getPlayerID(), players);
        assertEquals(7, calculateScore.returnScore());
    }

    @Test
    public void testCalculateScoreEvenOdd() {
        // Setup player hand with criteria card
        Card criteriaCard = new CardSalad(CardSalad.Vegetable.PEPPER, "LETTUCE: EVEN=7, ODD=3");
        hand.addCard(player1.getPlayerID(), criteriaCard);
        hand.addCard(player1.getPlayerID(), new CardSalad(CardSalad.Vegetable.LETTUCE, ""));
        hand.addCard(player1.getPlayerID(), new CardSalad(CardSalad.Vegetable.LETTUCE, ""));

        CalculateScore calculateScore = new CalculateScore(hand, player1.getPlayerID(), players);
        assertEquals(7, calculateScore.returnScore());
    }

    @Test
    public void testCalculateScorePerVeg() {
        // Setup player hand with criteria card
        Card criteriaCard = new CardSalad(CardSalad.Vegetable.PEPPER, "2 / LETTUCE");
        hand.addCard(player1.getPlayerID(), criteriaCard);
        Card pointCardLETTUCE = new CardSalad(CardSalad.Vegetable.LETTUCE, "");
        pointCardLETTUCE.flipSide();
        hand.addCard(player1.getPlayerID(), pointCardLETTUCE);
        hand.addCard(player1.getPlayerID(), pointCardLETTUCE);

        CalculateScore calculateScore = new CalculateScore(hand, player1.getPlayerID(), players);
        assertEquals(4, calculateScore.returnScore());
    }

    @Test
    public void testCalculateScoreAddition() {
        // Setup player hand with criteria card
        Card criteriaCard = new CardSalad(CardSalad.Vegetable.PEPPER, "PEPPER + PEPPER = 5");
        
        hand.addCard(player1.getPlayerID(), criteriaCard);
        Card pointCard = new CardSalad(CardSalad.Vegetable.PEPPER, "");
        pointCard.flipSide();
        hand.addCard(player1.getPlayerID(), pointCard);
        hand.addCard(player1.getPlayerID(), pointCard);

        CalculateScore calculateScore = new CalculateScore(hand, player1.getPlayerID(), players);
        assertEquals(5, calculateScore.returnScore());
    }

    @Test
    public void testCalculateScoreComplexAddition() {
        // Setup player hand with criteria card
        Card criteriaCard = new CardSalad(CardSalad.Vegetable.PEPPER, "CARROT + ONION = 5");
        hand.addCard(player1.getPlayerID(), criteriaCard);
        Card pointCardCarrot = new CardSalad(CardSalad.Vegetable.CARROT, "");
        pointCardCarrot.flipSide();

        Card pointCardOnion = new CardSalad(CardSalad.Vegetable.ONION, "");
        pointCardOnion.flipSide();

        hand.addCard(player1.getPlayerID(), pointCardCarrot);
        hand.addCard(player1.getPlayerID(), pointCardOnion);

        CalculateScore calculateScore = new CalculateScore(hand, player1.getPlayerID(), players);
        assertEquals(5, calculateScore.returnScore());
    }

    @Test
    public void testCalculateScoreMultipleCriteria() {
        // Setup player hand with criteria card
        Card criteriaCard = new CardSalad(CardSalad.Vegetable.PEPPER, "1 / LETTUCE,  1 / ONION");
        hand.addCard(player1.getPlayerID(), criteriaCard);

        Card pointCardLettuce = new CardSalad(CardSalad.Vegetable.LETTUCE, "");
        pointCardLettuce.flipSide();

        Card pointCardOnion = new CardSalad(CardSalad.Vegetable.ONION, "");
        pointCardOnion.flipSide();

        hand.addCard(player1.getPlayerID(), pointCardLettuce);
        hand.addCard(player1.getPlayerID(), pointCardOnion);

        CalculateScore calculateScore = new CalculateScore(hand, player1.getPlayerID(), players);
        assertEquals(2, calculateScore.returnScore());
    }

    @Test
    public void testCalculateScoreTotalVegetable() {
        // Setup player hand with criteria card
        Card criteriaCard = new CardSalad(CardSalad.Vegetable.PEPPER, "MOST TOTAL VEGETABLE = 10");
        hand.addCard(player1.getPlayerID(), criteriaCard);

        Card pointCardLettuce = new CardSalad(CardSalad.Vegetable.LETTUCE, "");
        pointCardLettuce.flipSide();

        Card pointCardOnion = new CardSalad(CardSalad.Vegetable.ONION, "");
        pointCardOnion.flipSide();

        hand.addCard(player1.getPlayerID(), pointCardLettuce);
        hand.addCard(player1.getPlayerID(), pointCardOnion);

        // Setup other player's hand
        hand.addCard(player2.getPlayerID(), pointCardLettuce);

        CalculateScore calculateScore = new CalculateScore(hand, player1.getPlayerID(), players);
        assertEquals(10, calculateScore.returnScore());
    }

    @Test
    public void testCalculateScoreWithMinusCriteria() {
        // Add cards to player1's hand
        hand.addCard(player1.getPlayerID(), new CardSalad(CardSalad.Vegetable.PEPPER, "3/LETTUCE, -1/ONION, -1/PEPPER"));
        hand.addCard(player1.getPlayerID(), new CardSalad(CardSalad.Vegetable.LETTUCE, "3/PEPPER, -1/TOMATO, -1/LETTUCE"));
        hand.addCard(player1.getPlayerID(), new CardSalad(CardSalad.Vegetable.CARROT, "3/CABBAGE, -1/LETTUCE, -1/CARROT"));

        Card pointCardLettuce = new CardSalad(CardSalad.Vegetable.LETTUCE, "");
        pointCardLettuce.flipSide();

        Card pointCardOnion = new CardSalad(CardSalad.Vegetable.ONION, "");
        pointCardOnion.flipSide();

        Card pointCardPepper = new CardSalad(CardSalad.Vegetable.PEPPER, "");
        pointCardPepper.flipSide();

        Card pointCardCabbage = new CardSalad(CardSalad.Vegetable.CABBAGE, "");
        pointCardCabbage.flipSide();

        Card pointCardCarrot = new CardSalad(CardSalad.Vegetable.CARROT, "");
        pointCardCarrot.flipSide();


        // Add vegetable cards to player1's hand
        hand.addCard(player1.getPlayerID(), pointCardLettuce);
        hand.addCard(player1.getPlayerID(), pointCardLettuce);
        hand.addCard(player1.getPlayerID(), pointCardOnion);
        hand.addCard(player1.getPlayerID(), pointCardPepper);
        hand.addCard(player1.getPlayerID(), pointCardPepper);
        hand.addCard(player1.getPlayerID(), pointCardPepper);
        hand.addCard(player1.getPlayerID(), pointCardCabbage);
        hand.addCard(player1.getPlayerID(), pointCardCabbage);
        hand.addCard(player1.getPlayerID(), pointCardCabbage);
        hand.addCard(player1.getPlayerID(), pointCardCarrot);
        hand.addCard(player1.getPlayerID(), pointCardCarrot);
        hand.addCard(player1.getPlayerID(), pointCardCarrot);

        // Calculate score for player1
        CalculateScore calculateScore = new CalculateScore(hand, player1.getPlayerID(), players);
        int score = calculateScore.returnScore();

        // Expected score calculation:
        // 3/LETTUCE: 3 * 2 = 6
        // -1/ONION: -1 * 1 = -1
        // -1/PEPPER: -1 * 3 = -3
        // 3/PEPPER: 3 * 3 = 9
        // -1/TOMATO: -1 * 0 = 0
        // -1/LETTUCE: -1 * 2 = -2
        // 3/CABBAGE: 3 * 3 = 9
        // -1/LETTUCE: -1 * 2 = -2
        // -1/CARROT: -1 * 3 = -3
        // Total score: 6 - 1 - 3 + 9 - 2 + 9 - 2 - 3 = 13

        assertEquals(13, score);
    }

    @Test
    public void testCalculateScoreMissingVegetableType() {
        // Setup player hand with criteria card
        Card criteriaCard = new CardSalad(CardSalad.Vegetable.CABBAGE, "5 / MISSING VEGETABLE TYPE");
        hand.addCard(player1.getPlayerID(), criteriaCard);

        Card pointCardLettuce = new CardSalad(CardSalad.Vegetable.LETTUCE, "");
        pointCardLettuce.flipSide();

        Card pointCardOnion = new CardSalad(CardSalad.Vegetable.ONION, "");
        pointCardOnion.flipSide();

        hand.addCard(player1.getPlayerID(), pointCardLettuce);
        hand.addCard(player1.getPlayerID(), pointCardOnion);

        CalculateScore calculateScore = new CalculateScore(hand, player1.getPlayerID(), players);
        assertEquals(5, calculateScore.returnScore());
    }

    @Test
    public void testCalculateScorePerVegetableTypeTwo() {
        // Setup player hand with criteria card
        Card criteriaCard = new CardSalad(CardSalad.Vegetable.ONION, "3 / VEGETABLE TYPE >=2");
        hand.addCard(player1.getPlayerID(), criteriaCard);

        Card pointCardLettuce = new CardSalad(CardSalad.Vegetable.LETTUCE, "");
        pointCardLettuce.flipSide();

        Card pointCardOnion = new CardSalad(CardSalad.Vegetable.ONION, "");
        pointCardOnion.flipSide();

        hand.addCard(player1.getPlayerID(), pointCardLettuce);
        hand.addCard(player1.getPlayerID(), pointCardOnion);
        hand.addCard(player1.getPlayerID(), pointCardOnion);
        
        CalculateScore calculateScore = new CalculateScore(hand, player1.getPlayerID(), players);
        assertEquals(3, calculateScore.returnScore());
    }

    @Test
    public void testCalculateScoreCompleteSet() {
        // Setup player hand with criteria card
        Card criteriaCard = new CardSalad(CardSalad.Vegetable.TOMATO, "COMPLETE SET = 12");
        hand.addCard(player1.getPlayerID(), criteriaCard);

        Card pointCardLettuce = new CardSalad(CardSalad.Vegetable.LETTUCE, "");
        pointCardLettuce.flipSide();

        Card pointCardOnion = new CardSalad(CardSalad.Vegetable.ONION, "");
        pointCardOnion.flipSide();

        Card pointCardPepper = new CardSalad(CardSalad.Vegetable.PEPPER, "");
        pointCardPepper.flipSide();

        Card pointCardCarrot = new CardSalad(CardSalad.Vegetable.CARROT, "");
        pointCardCarrot.flipSide();

        Card pointCardCabbage = new CardSalad(CardSalad.Vegetable.CABBAGE, "");
        pointCardCabbage.flipSide();

        Card pointCardTomato = new CardSalad(CardSalad.Vegetable.TOMATO, "");
        pointCardTomato.flipSide();

        hand.addCard(player1.getPlayerID(), pointCardLettuce);
        hand.addCard(player1.getPlayerID(), pointCardOnion);
        hand.addCard(player1.getPlayerID(), pointCardPepper);
        hand.addCard(player1.getPlayerID(), pointCardCarrot);
        hand.addCard(player1.getPlayerID(), pointCardCabbage);
        hand.addCard(player1.getPlayerID(), pointCardTomato);

        CalculateScore calculateScore = new CalculateScore(hand, player1.getPlayerID(), players);
        assertEquals(12, calculateScore.returnScore());
    }

    @Test
    public void testCalculateScoreFewestTotalVegetable() {
        // Setup player hand with criteria card
        Card criteriaCard = new CardSalad(CardSalad.Vegetable.LETTUCE, "FEWEST TOTAL VEGETABLE = 7");
        hand.addCard(player1.getPlayerID(), criteriaCard);

        // Setup other player's hand
        hand.addCard(player2.getPlayerID(), new CardSalad(CardSalad.Vegetable.PEPPER, ""));
        hand.addCard(player2.getPlayerID(), new CardSalad(CardSalad.Vegetable.LETTUCE, ""));

        CalculateScore calculateScore = new CalculateScore(hand, player1.getPlayerID(), players);
        assertEquals(7, calculateScore.returnScore());
    }

    @Test
    public void testCalculateScorePerVegetableTypeThree() {
        // Setup player hand with criteria card
        Card criteriaCard = new CardSalad(CardSalad.Vegetable.CARROT, "5 / VEGETABLE TYPE >=3");
        hand.addCard(player1.getPlayerID(), criteriaCard);

        Card pointCardLettuce = new CardSalad(CardSalad.Vegetable.LETTUCE, "");
        pointCardLettuce.flipSide();

        Card pointCardOnion = new CardSalad(CardSalad.Vegetable.ONION, "");
        pointCardOnion.flipSide();

        Card pointCardPepper = new CardSalad(CardSalad.Vegetable.PEPPER, "");
        pointCardPepper.flipSide();

        hand.addCard(player1.getPlayerID(), pointCardLettuce);
        hand.addCard(player1.getPlayerID(), pointCardOnion);
        hand.addCard(player1.getPlayerID(), pointCardPepper);
        hand.addCard(player1.getPlayerID(), pointCardPepper);
        hand.addCard(player1.getPlayerID(), pointCardPepper);

        CalculateScore calculateScore = new CalculateScore(hand, player1.getPlayerID(), players);
        assertEquals(5, calculateScore.returnScore());
    }

   

}
