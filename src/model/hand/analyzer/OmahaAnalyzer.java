package model.hand.analyzer;

import java.util.ArrayList;
import java.util.List;

import model.card.Card;

/**
 * An implementation of {@code HandAnalyzer} used to analyze Omaha hands.
 *
 * <p>
 * This implementation analyzes nine card Omaha hands following the rules of
 * Omaha. The best hand a player can have must contain two card's from the
 * player's hand and three from the community cards.
 * </p>
 *
 * @author Emmet Murray
 * @version 1.0
 * @since 9/26/16
 */
public class OmahaAnalyzer extends HandAnalyzer {

    /**
     * The length of the player's hand.
     */
    static final int PLAYER_HAND_LENGTH = 4;

    /**
     * The list of the {@code Card} objects in the player's hand.
     */
    private List<Card> playerHand;

    /**
     * The list of the {@code Card} objects in the community.
     */
    private List<Card> community;

    /**
     * Creates an instance of {@code HandAnalyzer} by passing {@code fullHand}
     * to the super constructor.
     *
     * @param fullHand
     *            the hand to analyze
     * 
     * @custom.requires <hand> = <Player hand> * <Community>
     * 			 the hand was dealt during a Omaha {@code Hand} & the first four
     *           cards of the List are the player's hand and the last five are
     *           community cards
     */
    public OmahaAnalyzer(List<Card> fullHand) {
        super(fullHand);
        this.playerHand = new ArrayList<Card>();
        this.community = new ArrayList<Card>();
        for (int i = 0; i < PLAYER_HAND_LENGTH; i++) {
            this.playerHand.add(super.getFullHand().get(i));
        }
        for (int i = PLAYER_HAND_LENGTH; i < fullHand.size(); i++) {
            this.community.add(super.getFullHand().get(i));
        }
    }

    /**
     * {@inheritDoc}
     *
     * This implementation breaks the full nine card final Omaha hand into its 5
     * card combinations by the rules of Omaha poker, two cards must be from the
     * player's hand, and three must be community cards.
     */
    @Override
    public final List<List<Card>> fiveCardCombinations(List<Card> fullHand) {
        List<List<Card>> combinationsTotal = new ArrayList<List<Card>>();
        List<List<Card>> combinationsPlayer;
        List<List<Card>> combinationsComm;
        final int k3 = 3;
        final int k2 = 2;

        /**
         * All possible ways to choose 3 cards from the community cards.
         */
        combinationsComm = this.recurseCombinations(this.community, k3);

        /**
         * All possible ways to choose 2 cards from the player's hand.
         */
        combinationsPlayer = this.recurseCombinations(this.playerHand, k2);

        /**
         * All possible ways to combine {@code combinationsComm} and
         * {@code combinationsPlayer}.
         */
        for (int i = 0; i < combinationsComm.size(); i++) {
            for (int j = 0; j < combinationsPlayer.size(); j++) {
                List<Card> temp = new ArrayList<Card>();
                temp.addAll(combinationsComm.get(i));
                temp.addAll(combinationsPlayer.get(j));
                combinationsTotal.add(temp);
            }
        }

        return combinationsTotal;
    }
}
