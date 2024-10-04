import java.util.ArrayList;
import java.util.Collections;
import java.util.Stack;

public class BlackjackDealer {

    Stack<Card> deck = new Stack<Card>();


    public void generateDeck(){

        deck.clear();

        ArrayList<String> suits = new ArrayList<String>();
        ArrayList<String> specialSuits = new ArrayList<String>();


        suits.add("clubs");
        suits.add("hearts");
        suits.add("spades");
        suits.add("diamonds");

        specialSuits.add("jack");
        specialSuits.add("king");
        specialSuits.add("queen");


        for(String i : suits){
            deck.add(new Card("ace_of_"+i,11));
        }

        for(String i : suits){
            for(String s : specialSuits){
                deck.add(new Card(s+"_of_"+i,10));
            }
        }

        for(String s : suits){
            for(int i = 2; i < 11; i++){
                deck.add(new Card(i+"_of_"+s,i));
            }
        }

    }

    public ArrayList<Card> dealHand(){

        ArrayList<Card> hand = new ArrayList<Card>();

        hand.add(drawOne());
        hand.add(drawOne());

        return hand;
    }

    public Card drawOne(){
        return deck.pop();
    }

    public void shuffleDeck(){
        generateDeck();
        Collections.shuffle(deck);
    }

    public int deckSize(){
        return deck.size();
    }


}
