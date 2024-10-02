package testing;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import src.PointSalad;
/*
1. There can be between 2 and 6 players. 
2.  The deck consists of 108 cards (as specified in the PointSaladManifest.json file published in Canvas). A card 
has two sides, one with the criteria consisting of scoring rules, the other with one of six different vegetables 
(Pepper, Lettuce, Carrot, Cabbage, Onion, Tomato). There are 18 of each vegetable. 
3.  Form the deck so that 
a.  2 Players: Use 6 random vegetable of each (36 cards in total) 
b.  3 Players: Use 9 random vegetable of each (54 cards in total) 
c.  4 Players: Use 12 random vegetable of each (72 cards in total) 
d.  5 Players: Use 15 random vegetable of each (90 cards in total) 
e.  6 Players: Use the entire deck 
Do not reveal which cards were removed 
4.  Shuffle the cards and create three roughly equal draw piles with roughly equal draw piles with point card sides 
visible. 
5.  Flip over two cards from each draw pile to form the vegetable market. 
6.  Randomly choose a start player  
7.  On a player’s turn the player may draft one or more cards and add to the player’s hand. Either: 
a.  One point card from the top of any of the draw piles, or 
b.  Two veggie cards from those available in the veggie market.  
8.  The player may opt to turn a point card to its vegetable side (but not the other way 
around).  
9.  Show the hand to the other players.  
10. Replace the market with cards from the top of the corresponding point card draw pile  
11. If a draw pile runs out of cards, then draw cards from the bottom of the draw pile with 
the most cards instead. 
12. Continue step 7-12 for the next player until there are no more cards in the Point 
Salad Market 
13. Calculate the score for each player according to the point cards in hand.  
14. Announce the winner with the highest score 
*/

public class PointSaladTest {
    
}
