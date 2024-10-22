package game.score;

import game.piles.Card;
import game.piles.CardSalad;
import game.players.PlayerHand;
import java.util.ArrayList;
import game.players.Player;

public class CalculateScore extends AbstractCalculateScore {
    int score;


     /**
     * The CalculateScore class is responsible for calculating the total score of a player
     * based on the criteria specified on the cards in their hand.
     *
     * @param hand The PlayerHand object representing the player's hand.
     * @param playerID The ID of the player whose score is being calculated.
     * @param players The list of all players in the game.
     *
     * The constructor initializes the calculation by iterating through the criteria cards
     * in the player's hand and applying the scoring rules based on the criteria specified
     * on each card. The total score is then stored in the score attribute.
     */

    public CalculateScore(PlayerHand hand, int playerID, ArrayList<Player> players) {
        super(hand, playerID, players);
        int totalScore = 0;
        Player thisPlayer = players.get(playerID);

        for (Card criteriaCard : hand.getPile(thisPlayer.getPlayerID()).getAll()) {
            try {
                if (criteriaCard.isCriteriaSideUp()) {
                    String criteria = criteriaCard.getCriteria();
                    String[] parts = criteria.split(",");

                    if (criteria.contains("TOTAL") || criteria.contains("TYPE") || criteria.contains("SET")) {
                        totalScore += handleTotalTypeSetCriteria(criteria, hand, thisPlayer, players);
                    } else if (criteria.contains("MOST") || criteria.contains("FEWEST")) {
                        totalScore += handleMostFewestCriteria(criteria, hand, thisPlayer, players);
                    } else if (parts.length > 1 || criteria.contains("+") || parts[0].contains("/")) {
                        totalScore += handleComplexCriteria(criteria, parts, hand, thisPlayer);
                    }
                }
            } catch (Exception e) {
               continue;
            }
        }
        this.score = totalScore;
    }

    private int handleTotalTypeSetCriteria(String criteria, PlayerHand hand, Player thisPlayer, ArrayList<Player> players) {
        int score = 0;
        if (criteria.contains("TOTAL")) {
            score += handleTotalCriteria(criteria, hand, thisPlayer, players);
        }
        if (criteria.contains("TYPE")) {
            score += handleTypeCriteria(criteria, hand, thisPlayer);
        }
        if (criteria.contains("SET")) {
            score += handleSetCriteria(hand, thisPlayer);
        }
        return score;
    }

    private int handleTotalCriteria(String criteria, PlayerHand hand, Player thisPlayer, ArrayList<Player> players) {
        int countVeg = hand.countTotalTypeCard(thisPlayer.getPlayerID());
        int thisHandCount = countVeg;
        for (Player p : players) {
            if (p.getPlayerID() != thisPlayer.getPlayerID()) {
                int playerVeg = hand.countTotalTypeCard(p.getPlayerID());
                if ((criteria.contains("MOST") && playerVeg > countVeg) || (criteria.contains("FEWEST") && playerVeg < countVeg)) {
                    countVeg = playerVeg;
                }
            }
        }
        if (countVeg == thisHandCount) {
            return Integer.parseInt(criteria.substring(criteria.indexOf("=") + 1).trim());
        }
        return 0;
    }

    private int handleTypeCriteria(String criteria, PlayerHand hand, Player thisPlayer) {
        String[] expr = criteria.split("/");
        int addScore = Integer.parseInt(expr[0].trim());
        if (expr[1].contains("MISSING")) {
            int missing = 0;
            for (Card vegetable : hand.getPile(thisPlayer.getPlayerID()).getAll()) {
                if (hand.countTotalofOneTypeCard(thisPlayer.getPlayerID(), vegetable.getCardType()) == 0) {
                    missing++;
                }
            }
            return missing * addScore;
        } else {
            int atLeastPerVegType = Integer.parseInt(expr[1].substring(expr[1].indexOf(">=") + 2).trim());
            int totalType = 0;
            ArrayList<Enum<?>> counted = new ArrayList<>();
            for (Card vegetable : hand.getPile(thisPlayer.getPlayerID()).getAll()) {
                if (!counted.contains(vegetable.getCardType())) {
                    int countVeg = hand.countTotalofOneTypeCard(thisPlayer.getPlayerID(), vegetable.getCardType());
                    if (countVeg >= atLeastPerVegType) {
                        counted.add(vegetable.getCardType());
                        totalType++;
                    }
                }
            }
            return totalType * addScore;
        }
    }

    private int handleSetCriteria(PlayerHand hand, Player thisPlayer) {
        int addScore = 12;
        for (Card vegetable : hand.getPile(thisPlayer.getPlayerID()).getAll()) {
            int countVeg = hand.countTotalofOneTypeCard(thisPlayer.getPlayerID(), vegetable.getCardType());
            if (countVeg == 0) {
                return 0;
            }
        }
        return addScore;
    }

    private int handleMostFewestCriteria(String criteria, PlayerHand hand, Player thisPlayer, ArrayList<Player> players) {
        int vegIndex = criteria.contains("MOST") ? criteria.indexOf("MOST") + 5 : criteria.indexOf("FEWEST") + 7;
        String veg = criteria.substring(vegIndex, criteria.indexOf("=")).trim();
        CardSalad tmp = new CardSalad(CardSalad.Vegetable.PEPPER, criteria);
        Enum vegEnum = tmp.getCardType(veg);
        int countVeg = hand.countTotalofOneTypeCard(thisPlayer.getPlayerID(), vegEnum);

        int nrVeg = countVeg;
        for (Player p : players) {
            if (p.getPlayerID() != thisPlayer.getPlayerID()) {
                int playerVeg = hand.countTotalofOneTypeCard(p.getPlayerID(), vegEnum);
                if ((criteria.contains("MOST") && playerVeg > nrVeg) || (criteria.contains("FEWEST") && playerVeg < nrVeg)) {
                    nrVeg = playerVeg;
                }
            }
        }
        if (nrVeg == countVeg) {
            return Integer.parseInt(criteria.substring(criteria.indexOf("=") + 1).trim());
        }
        return 0;
    }

    private int handleComplexCriteria(String criteria, String[] parts, PlayerHand hand, Player thisPlayer) {
        int score = 0;
        if (criteria.contains("+")) {
            score += handlePlusCriteria(criteria, hand, thisPlayer);
        } else if (parts[0].contains("=")) {
            score += handleEqualCriteria(parts[0], hand, thisPlayer);
        } else {
            score += handleSlashCriteria(parts, hand, thisPlayer);
        }
        return score;
    }

    private int handlePlusCriteria(String criteria, PlayerHand hand, Player thisPlayer) {
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
            CardSalad tmp = new CardSalad(CardSalad.Vegetable.PEPPER, criteria);
            Enum vegEnum = tmp.getCardType(vegs[0].trim());
            return (hand.countTotalofOneTypeCard(thisPlayer.getPlayerID(), vegEnum) / countSameKind) * Integer.parseInt(criteria.split("=")[1].trim());
        } else {
            for (int i = 0; i < vegs.length; i++) {
                CardSalad tmp = new CardSalad(CardSalad.Vegetable.PEPPER, criteria);
                Enum vegEnum = tmp.getCardType(vegs[i].trim());
                nrVeg[i] = hand.countTotalofOneTypeCard(thisPlayer.getPlayerID(), vegEnum);
            }
            int min = nrVeg[0];
            for (int x = 1; x < nrVeg.length; x++) {
                if (nrVeg[x] < min) {
                    min = nrVeg[x];
                }
            }
            return min * Integer.parseInt(criteria.split("=")[1].trim());
        }
    }

    private int handleEqualCriteria(String part, PlayerHand hand, Player thisPlayer) {
        String veg = part.substring(0, part.indexOf(":"));
        CardSalad tmp = new CardSalad(CardSalad.Vegetable.PEPPER, part);
        Enum vegEnum = tmp.getCardType(veg);
        int countVeg = hand.countTotalofOneTypeCard(thisPlayer.getPlayerID(), vegEnum);
        return (countVeg % 2 == 0) ? 7 : 3;
    }

    private int handleSlashCriteria(String[] parts, PlayerHand hand, Player thisPlayer) {
        int score = 0;
        for (String part : parts) {
            String[] veg = part.split("/");
            CardSalad tmp = new CardSalad(CardSalad.Vegetable.PEPPER, part);
            Enum vegEnum = tmp.getCardType(veg[1].trim());
            int veg1 = hand.countTotalofOneTypeCard(thisPlayer.getPlayerID(), vegEnum);
            score += Integer.parseInt(veg[0].trim()) * veg1;
        }
        return score;
    }

    public int returnScore() {
        return this.score;
    }
}