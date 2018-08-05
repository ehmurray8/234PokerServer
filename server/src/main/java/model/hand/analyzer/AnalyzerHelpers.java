package model.hand.analyzer;

import model.card.Card;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;

public class AnalyzerHelpers {
    public static final int PAIR_FREQUENCY = 2, TRIPS_FREQUENCY = 3, QUADS_FREQUENCY = 4,
                            STRAIGHT_LENGTH = 5, BROADWAY_LENGTH = 6;

    public static void handToEnumMaps(List<Card> hand, EnumMap<Card.Rank, Integer> rankMap,
                                      EnumMap<Card.Suit, Integer> suitMap) {
        for (Card card : hand) {
            if (rankMap.size() > 0 && rankMap.containsKey(card.getRank())) {
                Integer oldValue = rankMap.remove(card.getRank());
                rankMap.put(card.getRank(), oldValue + 1);
            } else {
                rankMap.put(card.getRank(), 1);
            }

            if (suitMap.size() > 0 && suitMap.containsKey(card.getSuit())) {
                Integer oldValue = suitMap.remove(card.getSuit());
                suitMap.put(card.getSuit(), oldValue + 1);
            } else {
                suitMap.put(card.getSuit(), 1);
            }
        }
    }

    /**
     * Recurse through {@code fullHand} and returns all the possible combinations of size k.
     *
     * @param fullHand seven card hand to get the combinations from
     * @param k size of the combinations
     * @return all the combinations of size k
     */
    public static List<List<Card>> recurseCombinations(List<Card> fullHand, int k) {
        List<List<Card>> allCombos = new ArrayList<>();
        if (k == 0) {
            allCombos.add(new ArrayList<>());
            return allCombos;
        }
        if (k > fullHand.size()) {
            return allCombos;
        }

        List<Card> groupWithoutC = new ArrayList<>(fullHand);
        Card c = groupWithoutC.remove(groupWithoutC.size() - 1);

        List<List<Card>> combosWithoutC = recurseCombinations(groupWithoutC, k);
        List<List<Card>> combosWithC = recurseCombinations(groupWithoutC, k - 1);
        for (List<Card> combo : combosWithC) {
            combo.add(c);
        }
        allCombos.addAll(combosWithoutC);
        allCombos.addAll(combosWithC);
        return allCombos;
    }
}
