import java.util.ArrayList;

public class BlackjackGameLogic {

    public String whoWon(ArrayList<Card> playerHand1,ArrayList<Card> dealerHand){

        if((handTotal(dealerHand) == 21 && dealerHand.size() == 2) && (handTotal(playerHand1) == 21 && playerHand1.size() == 2)){
            return "Push";
        }
        else if(handTotal(playerHand1) == 21 && playerHand1.size() == 2){
            return "Player";
        }
        else if(handTotal(dealerHand) == 21 && dealerHand.size() == 2){
            return "Dealer";
        }
        else if(handTotal(playerHand1) > 21){
            return "Dealer";
        }
        else if(handTotal(dealerHand) > 21){
            return "Player";
        }
        if(handTotal(playerHand1) == handTotal(dealerHand)){
            return "Push";
        }
        else if(handTotal(playerHand1) > handTotal(dealerHand)){
            return "Player";
        }
        else{
            return "Dealer";
        }
    }

    public int handTotal(ArrayList<Card> hand){

        int total = 0;

            for(Card i : hand){
                total += i.value;
            }

        return total;
    }

    public boolean evaluateBankerDraw(ArrayList<Card> hand){

        return handTotal(hand) <= 16;

    }



}
