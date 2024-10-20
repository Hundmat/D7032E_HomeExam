package game.score;

import game.piles.Card;
import game.piles.CardSalad;
import game.piles.Pile; // Add this import statement
import game.players.PlayerHand; // Add this import statement
import java.util.ArrayList;
import game.players.Player;

public class CalculateScore {
    int score;
    public CalculateScore(PlayerHand hand, int playerID, ArrayList<Player> players) {
        
        int totalScore = 0;
        Player thisPlayer = players.get(playerID);
        for (Card criteriaCard : hand.getPile(thisPlayer.getPlayerID()).getAll()) {
            if (criteriaCard.isCriteriaSideUp()) {
                String criteria = criteriaCard.getCriteria();
                String[] parts = criteria.split(",");

                // ID 18
                if (criteria.indexOf("TOTAL") >= 0 || criteria.indexOf("TYPE") >= 0 || criteria.indexOf("SET") >= 0) {
                    if (criteria.indexOf("TOTAL") >= 0) {
                        int countVeg = hand.countTotalTypeCard(thisPlayer.getPlayerID());
                        int thisHandCount = countVeg;
                        for (Player p : players) {
                            if (p.getPlayerID() != thisPlayer.getPlayerID()) {
                                int playerVeg = hand.countTotalTypeCard(p.getPlayerID());
                                if ((criteria.indexOf("MOST") >= 0) && (playerVeg > countVeg)) {
                                    countVeg = hand.countTotalTypeCard(p.getPlayerID());
                                }
                                if ((criteria.indexOf("FEWEST") >= 0) && (playerVeg < countVeg)) {
                                    countVeg = hand.countTotalTypeCard(p.getPlayerID());;
                                }
                            }
                        }
                        if (countVeg == thisHandCount) {
                            // int aScore =
                            // Integer.parseInt(criteria.substring(criteria.indexOf("=")+1).trim());
                            // System.out.print("ID18 MOST/FEWEST: "+aScore + " " );
                            totalScore += Integer.parseInt(criteria.substring(criteria.indexOf("=") + 1).trim());
                        }
                    }
                    if (criteria.indexOf("TYPE") >= 0) {
                        String[] expr = criteria.split("/");
                        int addScore = Integer.parseInt(expr[0].trim());
                        if (expr[1].indexOf("MISSING") >= 0) {
                            int missing = 0;
                            for (Card vegetable : hand.getPile(thisPlayer.getPlayerID()).getAll()) {
                                if (hand.countTotalofOneTypeCard(thisPlayer.getPlayerID(),vegetable.getCardType()) == 0) {
                                    missing++;
                                }
                            }
                            // int aScore = missing * addScore;
                            // System.out.print("ID18 TYPE MISSING: "+aScore + " ");
                            totalScore += missing * addScore;
                        } else {
                            int atLeastPerVegType = Integer
                                    .parseInt(expr[1].substring(expr[1].indexOf(">=") + 2).trim());
                            int totalType = 0;
                            for (Card vegetable : hand.getPile(thisPlayer.getPlayerID()).getAll()) {
                                int countVeg = hand.countTotalofOneTypeCard(thisPlayer.getPlayerID(),vegetable.getCardType());
                                if (countVeg >= atLeastPerVegType) {
                                    totalType++;
                                }
                            }
                            // int aScore = totalType * addScore;
                            // System.out.print("ID18 TYPE >=: "+aScore + " ");
                            totalScore += totalType * addScore;
                        }
                    }
                    if (criteria.indexOf("SET") >= 0) {
                        int addScore = 12;
                        for (Card vegetable : hand.getPile(thisPlayer.getPlayerID()).getAll()) {
                            int countVeg = hand.countTotalofOneTypeCard(thisPlayer.getPlayerID(),vegetable.getCardType());
                            if (countVeg == 0) {
                                addScore = 0;
                                break;
                            }
                        }
                        // System.out.print("ID18 SET: "+addScore + " ");
                        totalScore += addScore;
                    }
                }
                // ID1 and ID2
                else if ((criteria.indexOf("MOST") >= 0) || (criteria.indexOf("FEWEST") >= 0)) { // ID1, ID2
                    int vegIndex = criteria.indexOf("MOST") >= 0 ? criteria.indexOf("MOST") + 5
                            : criteria.indexOf("FEWEST") + 7;
                    String veg = criteria.substring(vegIndex, criteria.indexOf("=")).trim();


                    CardSalad tmp = new CardSalad(CardSalad.Vegetable.PEPPER,criteria);
                    Enum vegEnum = tmp.getCardType(veg);
                    int countVeg = hand.countTotalofOneTypeCard(thisPlayer.getPlayerID(),vegEnum);

                    int nrVeg = countVeg;
                    for (Player p : players) {
                        if (p.getPlayerID() != thisPlayer.getPlayerID()) {
                            int playerVeg = hand.countTotalofOneTypeCard(p.getPlayerID(),vegEnum);
                            if ((criteria.indexOf("MOST") >= 0) && (playerVeg > nrVeg)) {
                                hand.countTotalofOneTypeCard(p.getPlayerID(),vegEnum);
                            }
                            if ((criteria.indexOf("FEWEST") >= 0) && (playerVeg < nrVeg)) {
                                hand.countTotalofOneTypeCard(p.getPlayerID(),vegEnum);
                            }
                        }
                    }
                    if (nrVeg == countVeg) {
                        // System.out.print("ID1/ID2:
                        // "+Integer.parseInt(criteria.substring(criteria.indexOf("=")+1).trim()) + "
                        // ");
                        totalScore += Integer.parseInt(criteria.substring(criteria.indexOf("=") + 1).trim());
                    }
                }

                // ID3, ID4, ID5, ID6, ID7, ID8, ID9, ID10, ID11, ID12, ID13, ID14, ID15, ID16,
                // ID17
                else if (parts.length > 1 || criteria.indexOf("+") >= 0 || parts[0].indexOf("/") >= 0) {
                    if (criteria.indexOf("+") >= 0) { // ID5, ID6, ID7, ID11, ID12, ID13
                        String expr = criteria.split("=")[0].trim();
                        String[] vegs = expr.split("\\+");
                        int[] nrVeg = new int[vegs.length];
                        int countSameKind = 1;
                        for (int j = 1; j < vegs.length; j++) {
                            if (vegs[0].trim().equals(vegs[j].trim())) {
                                countSameKind++;
                            }
                        }
                        if (countSameKind > 1) {
                            // System.out.print("ID5/ID11: "+ ((int)countVegetables(hand,
                            // Card.Vegetable.valueOf(vegs[0].trim()))/countSameKind) *
                            // Integer.parseInt(criteria.split("=")[1].trim()) + " ");
                            CardSalad tmp = new CardSalad(CardSalad.Vegetable.PEPPER,criteria);
                            Enum vegEnum = tmp.getCardType(vegs[0].trim());
                            totalScore += ((int) hand.countTotalofOneTypeCard(thisPlayer.getPlayerID(),vegEnum)
                                    / countSameKind) * Integer.parseInt(criteria.split("=")[1].trim());
                        } else {
                            for (int i = 0; i < vegs.length; i++) {
                                CardSalad tmp = new CardSalad(CardSalad.Vegetable.PEPPER,criteria);
                                Enum vegEnum = tmp.getCardType(vegs[i].trim());
                                nrVeg[i] = hand.countTotalofOneTypeCard(thisPlayer.getPlayerID(),vegEnum);
                            }
                            // find the lowest number in the nrVeg array
                            int min = nrVeg[0];
                            for (int x = 1; x < nrVeg.length; x++) {
                                if (nrVeg[x] < min) {
                                    min = nrVeg[x];
                                }
                            }
                            // System.out.print("ID6/ID7/ID12/ID13: "+min *
                            // Integer.parseInt(criteria.split("=")[1].trim()) + " ");
                            totalScore += min * Integer.parseInt(criteria.split("=")[1].trim());
                        }
                    } else if (parts[0].indexOf("=") >= 0) { // ID3
                        String veg = parts[0].substring(0, parts[0].indexOf(":"));
                        CardSalad tmp = new CardSalad(CardSalad.Vegetable.PEPPER,criteria);
                        Enum vegEnum = tmp.getCardType(veg);
                        int countVeg = hand.countTotalofOneTypeCard(thisPlayer.getPlayerID(),vegEnum);
                        // System.out.print("ID3: "+((countVeg%2==0)?7:3) + " ");
                        totalScore += (countVeg % 2 == 0) ? 7 : 3;
                    } else { // ID4, ID8, ID9, ID10, ID14, ID15, ID16, ID17
                        for (int i = 0; i < parts.length; i++) {
                            String[] veg = parts[i].split("/");
                            // System.out.print("ID4/ID8/ID9/ID10/ID14/ID15/ID16/ID17: " +
                            // Integer.parseInt(veg[0].trim()) * countVegetables(hand,
                            // Card.Vegetable.valueOf(veg[1].trim())) + " ");
                            CardSalad tmp = new CardSalad(CardSalad.Vegetable.PEPPER,criteria);
                            Enum vegEnum = tmp.getCardType(veg[1].trim());
                            int veg1 = hand.countTotalofOneTypeCard(thisPlayer.getPlayerID(),vegEnum);
                            
                            totalScore += Integer.parseInt(veg[0].trim())
                                    * veg1;
                        }
                    }
                }
            }
        }
        this.score = totalScore;
    }

    public int returnScore() {
        return this.score;
    }
}
