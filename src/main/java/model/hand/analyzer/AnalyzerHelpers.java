package model.hand.analyzer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import model.card.Card;


class AnalyzerHelpers {
    static final int PAIR_FREQUENCY = 2;
    static final int TRIPS_FREQUENCY = 3;
    static final int QUADS_FREQUENCY = 4;
    static final int STRAIGHT_LENGTH = 5;
    static final int BROADWAY_LENGTH = 6;

    static Map<Card.Rank, Integer> handToRankMap(List<Card> hand) {
        return hand.stream().collect(Collectors.toMap(Card::getRank, s -> 1, Integer::sum));
    }

    static Map<Card.Suit, Integer> handToSuitMap(List<Card> hand) {
        return hand.stream().collect(Collectors.toMap(Card::getSuit, s -> 1, Integer::sum));
    }

    /**
     * Recurse through {@code fullHand} and returns all the possible combinations of size k.
     *
     * @param fullHand seven card hand to get the combinations from
     * @param k        size of the combinations
     * @return all the combinations of size k
     */
    static List<List<Card>> recurseCombinations(List<Card> fullHand, int k) {
        List<List<Card>> allCombos = new ArrayList<>();
        if (k == 0) {
            allCombos.add(new ArrayList<>());
            return allCombos;
        }
        if (k > fullHand.size()) {
            return allCombos;
        }

        List<Card> cards = new ArrayList<>(fullHand);
        Card lastCard = cards.remove(cards.size() - 1);

        List<List<Card>> combosWithoutLastCard = recurseCombinations(cards, k);
        List<List<Card>> combos = recurseCombinations(cards, k - 1);
        for (List<Card> combo : combos) {
            combo.add(lastCard);
        }
        allCombos.addAll(combosWithoutLastCard);
        allCombos.addAll(combos);
        return allCombos;
    }
}
