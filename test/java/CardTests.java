import static org.junit.jupiter.api.Assertions.*;
import org.testng.annotations.Test;

import java.util.ArrayList;


public class CardTests {

    @Test
    void handTotalTest(){
        Card a = new Card("eight",8);
        Card b = new Card("seven",7);
        Card c = new Card("king",10);

        ArrayList<Card> hand = new ArrayList<Card>();

        hand.add(a);
        hand.add(b);
        hand.add(c);

        int total = 0;

        BlackjackGameLogic gameLogic = new BlackjackGameLogic();

        total = gameLogic.handTotal(hand);

        assertEquals(25,total);

    }

    @Test
    void dealerDrawTest(){
        Card a = new Card("two",2);
        Card b = new Card("three",3);
        Card c = new Card("king",10);

        ArrayList<Card> hand = new ArrayList<Card>();

        hand.add(a);
        hand.add(b);
        hand.add(c);

        BlackjackGameLogic gameLogic = new BlackjackGameLogic();

        boolean decision;

        decision = gameLogic.evaluateBankerDraw(hand);

        assertTrue(decision);

        Card d = new Card("five",5);

        hand.add(d);

        decision = gameLogic.evaluateBankerDraw(hand);

        assertFalse(decision);

    }

    @Test
    void newDeckSizeTest(){
        BlackjackDealer dealer = new BlackjackDealer();

        dealer.generateDeck();

        assertEquals(52,dealer.deckSize());

    }

    @Test
    void usedDeckSizeTest(){
        BlackjackDealer dealer = new BlackjackDealer();

        dealer.generateDeck();
        dealer.drawOne();
        dealer.drawOne();
        dealer.drawOne();

        assertEquals(49,dealer.deckSize());
    }

    @Test
    void deckShuffleTest(){
        BlackjackDealer dealer = new BlackjackDealer();


        dealer.shuffleDeck();

        Card i = dealer.drawOne();
        Card s = dealer.drawOne();
        Card a = dealer.drawOne();
        Card b = dealer.drawOne();
        Card c = dealer.drawOne();

        System.out.println(i.suit + i.value);
        System.out.println(s.suit + s.value);
        System.out.println(a.suit + a.value);
        System.out.println(b.suit + b.value);
        System.out.println(c.suit + c.value);

    }

    @Test
    void dealHandTest(){

        BlackjackDealer dealer = new BlackjackDealer();


        dealer.shuffleDeck();

        ArrayList<Card> myHand = dealer.dealHand();

        System.out.println(myHand.get(0).suit + myHand.get(0).value);
        System.out.println(myHand.get(1).suit + myHand.get(1).value);

    }

    @Test
    void whoWonBlackjackTieTest(){
        BlackjackGame i = new BlackjackGame();


        ArrayList<Card> myHand = new ArrayList<>();
        ArrayList<Card> dealerHand = new ArrayList<>();

        Card a = new Card("king",10);
        Card b = new Card("ace", 11);

        myHand.add(a);
        myHand.add(b);

        dealerHand.add(a);
        dealerHand.add(b);

        String result = "no one";

        if(i.gameLogic.handTotal(myHand) == 21 || i.gameLogic.handTotal(dealerHand) == 21){
            result = i.gameLogic.whoWon(myHand,dealerHand);
        }

        assertEquals("push",result);

    }

    @Test
    void whoWonWithWinningsTest(){

        BlackjackGame i = new BlackjackGame();
        String result = "no one";

        i.theDealer.shuffleDeck();
        i.currentBet = 50;

        i.playerHand = i.theDealer.dealHand();
        i.bankerHand= i.theDealer.dealHand();

        if(i.gameLogic.handTotal(i.playerHand) == 21 || i.gameLogic.handTotal(i.bankerHand) == 21){
            result = i.gameLogic.whoWon(i.playerHand,i.bankerHand);
        }

        while(i.gameLogic.evaluateBankerDraw(i.bankerHand)){
            i.bankerHand.add(i.theDealer.drawOne());
        }

        result = i.gameLogic.whoWon(i.playerHand,i.bankerHand);

        for (Card card : i.playerHand) {
            System.out.println(card.suit + card.value);
        }

        System.out.println();
        System.out.println();

        for (Card card : i.bankerHand) {
            System.out.println(card.suit + card.value);
        }

        System.out.println();
        System.out.println();
        System.out.println(result);
        System.out.println(i.evaluateWinnings());

    }


}
