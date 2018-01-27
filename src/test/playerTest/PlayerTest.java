package test.playerTest;

import org.junit.Test;

import model.card.Card;
import model.card.Card.Rank;
import model.card.Card.Suit;
import model.player.Player;

public class PlayerTest {

    @Test
    public void testAddCard() {
        Player p1 = new Player(0, "");
        p1.addCard(new Card(Rank.ACE, Suit.CLUBS));
        p1.addCard(new Card(Rank.TWO, Suit.DIAMONDS));
        p1.addCard(new Card(Rank.EIGHT, Suit.HEARTS));
        p1.addCard(new Card(Rank.THREE, Suit.SPADES));

        System.out.println(p1.getHand().toString());
    }

}
