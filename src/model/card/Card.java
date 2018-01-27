package model.card;

import java.util.Comparator;

/**
 * This class describes a Card object, it has a suit and a rank to describe a
 * playing card.
 *
 * @author Emmet Murray
 * @version 3.0
 * @since 9/26/2016
 */
public class Card {

    /**
     * The ranks of playing cards.
     *
     * @author Emmet Murray
     * @version 1.0
     * @since 5/1/2016
     */
    public enum Rank {
        /**
         * One, used only for check for the straight ACE - FIVE.
         */
        ONE(1),

        /**
         * Two.
         */
        TWO(2),

        /**
         * Three.
         */
        THREE(3),

        /**
         * Four.
         */
        FOUR(4),

        /**
         * Five.
         */
        FIVE(5),

        /**
         * Six.
         */
        SIX(6),

        /**
         * Seven.
         */
        SEVEN(7),

        /**
         * Eight.
         */
        EIGHT(8),

        /**
         * Nine.
         */
        NINE(9),

        /**
         * Ten.
         */
        TEN(10),

        /**
         * Jack.
         */
        JACK(11),

        /**
         * Queen.
         */
        QUEEN(12),

        /**
         * King.
         */
        KING(13),

        /**
         * Ace, used for determining high card.
         */
        ACE(14);

        /**
         * The number associated with the Rank, used for comparison.
         */
        private int rankNum;

        /**
         * Constructs a Rank enum value.
         *
         * @param rankNum
         *            the number associated with the enum value
         */
        Rank(int rankNum) {
            this.rankNum = rankNum;
        }

        /**
         * Returns the number associated with the enum value.
         *
         * @return the number assoicated with the enum value
         */
        public int getRankNum() {
            return this.rankNum;
        }
    }

    /**
     * The suits of playing cards.
     *
     * @author Emmet Murray
     * @version 1.0
     * @since 5/1/2016
     */
    public enum Suit {
        /**
         * Hearts.
         */
        HEARTS,

        /**
         * Diamonds.
         */
        DIAMONDS,

        /**
         * Clubs.
         */
        CLUBS,

        /**
         * Spades.
         */
        SPADES;
    }

    /**
     * The {@code Rank} of {@code this}.
     */
    private Rank rank;

    /**
     * The {@code Suit} of {@code this}.
     */
    private Suit suit;

    /**
     * Card constructor accepts a Rank and a Suit and stores them in the rank
     * and suit instance variables when the card is created.
     *
     * @param rank
     *            the rank to give
     * @param suit
     *            the suit of (@code this}
     * @custom.ensures this = the playing card representation of the parameters
     */
    public Card(Rank rank, Suit suit) {
        this.rank = rank;
        this.suit = suit;
    }

    /**
     * Rank getter method.
     *
     * @custom.updates this.rank
     * @return Card's rank
     */
    public final Rank getRank() {
        return this.rank;
    }

    /**
     * Suit getter method.
     *
     * @custom.updates this.suit
     * @return Card's suit
     */
    public final Suit getSuit() {
        return this.suit;
    }

    /**
     * {@inheritDoc}
     *
     * @custom.requires (this &amp;&amp; obj) != null
     */
    @Override
    public final boolean equals(Object obj) {
        boolean equal = true;

        if (this == null || obj == null) {
            return false;
        }

        if (!obj.getClass().isAssignableFrom(this.getClass())) {
            return false;
        }

        final Card otherCard = (Card) obj;
        if (this.getRank() == null || this.getSuit() == null
                || otherCard.getSuit() == null || otherCard.getRank() == null) {
            equal = false;
        } else if (!this.getRank().equals(otherCard.getRank())
                || !this.getSuit().equals(otherCard.getSuit())) {
            equal = false;
        }
        return equal;
    }

    @Override
    public final int hashCode() {
        final int OFF_SET = 100;
        return this.rank.getRankNum() + (this.suit.ordinal() * OFF_SET);
    }

    @Override
    public final String toString() {
        return "{" + this.rank + ", " + this.suit + "}";
    }
    
    public String getImagePath() {
    	return CardViewConstants.cardImages[this.getIndexFromCard()];
    }
    
    /**
     * Returns the index corresponding to the {@code Image} object in
     * {@code this.view.cardImages} of {@code c}.
     *
     * @return the index of the card's representation in
     *         {@code this.view.cardImages}
     */
    private int getIndexFromCard() {
        Suit s = this.getSuit();
        Rank r = this.getRank();
        int i = -1;

        switch (r) {
            case ACE:
                i = 0;
                break;
            case TWO:
                i = 4;
                break;
            case THREE:
                i = 8;
                break;
            case FOUR:
                i = 12;
                break;
            case FIVE:
                i = 16;
                break;
            case SIX:
                i = 20;
                break;
            case SEVEN:
                i = 24;
                break;
            case EIGHT:
                i = 28;
                break;
            case NINE:
                i = 32;
                break;
            case TEN:
                i = 36;
                break;
            case JACK:
                i = 40;
                break;
            case QUEEN:
                i = 44;
                break;
            case KING:
                i = 48;
                break;
            default:
                break;
        }

        switch (s) {
            case DIAMONDS:
                i++;
                break;
            case HEARTS:
                i += 2;
                break;
            case SPADES:
                i += 3;
                break;
            default:
                break;
        }

        return i;
    }

    /**
     * The constant Strings of the paths of all the .png files of the cards used
     * for {@code Card} class.
     *
     * @author Emmet Murray
     */
    private static final class CardViewConstants {
        /**
         * The prefix and suffix of all the Card images.
         */
        private final static String prefix = "data/CardImages/";
        private final static String suffix = ".png";

        /**
         * The image files of all the card images.
         */
        private final static String aceC = prefix + "ace_of_clubs" + suffix;
        private final static String aceD = prefix + "ace_of_diamonds" + suffix;
        private final static String aceH = prefix + "ace_of_hearts" + suffix;
        private final static String aceS = prefix + "ace_of_spades" + suffix;

        private final static String twoC = prefix + "2_of_clubs" + suffix;
        private final static String twoD = prefix + "2_of_diamonds" + suffix;
        private final static String twoH = prefix + "2_of_hearts" + suffix;
        private final static String twoS = prefix + "2_of_spades" + suffix;

        private final static String threeC = prefix + "3_of_clubs" + suffix;
        private final static String threeD = prefix + "3_of_diamonds" + suffix;
        private final static String threeH = prefix + "3_of_hearts" + suffix;
        private final static String threeS = prefix + "3_of_spades" + suffix;

        private final static String fourC = prefix + "4_of_clubs" + suffix;
        private final static String fourD = prefix + "4_of_diamonds" + suffix;
        private final static String fourH = prefix + "4_of_hearts" + suffix;
        private final static String fourS = prefix + "4_of_spades" + suffix;

        private final static String fiveC = prefix + "5_of_clubs" + suffix;
        private final static String fiveD = prefix + "5_of_diamonds" + suffix;
        private final static String fiveH = prefix + "5_of_hearts" + suffix;
        private final static String fiveS = prefix + "5_of_spades" + suffix;

        private final static String sixC = prefix + "6_of_clubs" + suffix;
        private final static String sixD = prefix + "6_of_diamonds" + suffix;
        private final static String sixH = prefix + "6_of_hearts" + suffix;
        private final static String sixS = prefix + "6_of_spades" + suffix;

        private final static String sevenC = prefix + "7_of_clubs" + suffix;
        private final static String sevenD = prefix + "7_of_diamonds" + suffix;
        private final static String sevenH = prefix + "7_of_hearts" + suffix;
        private final static String sevenS = prefix + "7_of_spades" + suffix;

        private final static String eightC = prefix + "8_of_clubs" + suffix;
        private final static String eightD = prefix + "8_of_diamonds" + suffix;
        private final static String eightH = prefix + "8_of_hearts" + suffix;
        private final static String eightS = prefix + "8_of_spades" + suffix;

        private final static String nineC = prefix + "9_of_clubs" + suffix;
        private final static String nineD = prefix + "9_of_diamonds" + suffix;
        private final static String nineH = prefix + "9_of_hearts" + suffix;
        private final static String nineS = prefix + "9_of_spades" + suffix;

        private final static String tenC = prefix + "10_of_clubs" + suffix;
        private final static String tenD = prefix + "10_of_diamonds" + suffix;
        private final static String tenH = prefix + "10_of_hearts" + suffix;
        private final static String tenS = prefix + "10_of_spades" + suffix;

        private final static String jackC = prefix + "jack_of_clubs2" + suffix;
        private final static String jackD = prefix + "jack_of_diamonds2"
                + suffix;
        private final static String jackH = prefix + "jack_of_hearts2" + suffix;
        private final static String jackS = prefix + "jack_of_spades2" + suffix;

        private final static String queenC = prefix + "queen_of_clubs2"
                + suffix;
        private final static String queenD = prefix + "queen_of_diamonds2"
                + suffix;
        private final static String queenH = prefix + "queen_of_hearts2"
                + suffix;
        private final static String queenS = prefix + "queen_of_spades2"
                + suffix;

        private final static String kingC = prefix + "king_of_clubs2" + suffix;
        private final static String kingD = prefix + "king_of_diamonds2"
                + suffix;
        private final static String kingH = prefix + "king_of_hearts2" + suffix;
        private final static String kingS = prefix + "king_of_spades2" + suffix;

        /**
         * The back of card image.
         */
        private final static String cardBack = prefix + "back_of_card" + suffix;

        /**
         * The array containing all of the useful card image files.
         */
        public final static String[] cardImages = { aceC, aceD, aceH, aceS,
                twoC, twoD, twoH, twoS, threeC, threeD, threeH, threeS, fourC,
                fourD, fourH, fourS, fiveC, fiveD, fiveH, fiveS, sixC, sixD,
                sixH, sixS, sevenC, sevenD, sevenH, sevenS, eightC, eightD,
                eightH, eightS, nineC, nineD, nineH, nineS, tenC, tenD, tenH,
                tenS, jackC, jackD, jackH, jackS, queenC, queenD, queenH,
                queenS, kingC, kingD, kingH, kingS, cardBack };
    }

    /**
     * Compares {@code c1} and {@code c2} solely by rank.
     */
    public static final class CardComparator implements Comparator<Card> {
        /**
         * {@inheritDoc}
         *
         * @custom.ensures Returns c1.rank - c2.rank.
         */
        @Override
        public int compare(Card c1, Card c2) {
            return c1.getRank().getRankNum() - c2.getRank().getRankNum();
        }
    }
}
