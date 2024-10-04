import java.util.ArrayList;
import java.util.Objects;

public class BlackjackGame {

    ArrayList<Card> playerHand = new ArrayList<>();

    ArrayList<Card> bankerHand = new ArrayList<>();

    BlackjackDealer theDealer = new BlackjackDealer();

    BlackjackGameLogic gameLogic = new BlackjackGameLogic();

    double currentBet = 0;

    double totalWinnings = 0;


    public double evaluateWinnings(){

        if(Objects.equals(gameLogic.whoWon(playerHand, bankerHand), "Dealer")){
                return 0;
        }
        else if(Objects.equals(gameLogic.whoWon(playerHand, bankerHand), "Push")){
            return currentBet;
        }
        else if(Objects.equals(gameLogic.whoWon(playerHand, bankerHand), "Player") && playerHand.size() ==2 && gameLogic.handTotal(playerHand) == 21){
            return currentBet * 2.5;
        }
        else{
            return currentBet*2;
        }
    }


}
