import javafx.animation.PauseTransition;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.HashMap;
import java.util.Objects;



public class JavaFXTemplate extends Application{

    Button startButton, next1, next2,hitButton,stayButton, restartButton;
    TextField t1,t2;
    VBox v1,v2,v3,v4,v5,v6,v7,v8,v9;

    HBox h1,h2;

    Text GameTitle = new Text("Blackjack");

    Text startMoney,txt2, currentMoneyTxt, txt3,txt4, gameResult,dealerTotal,playerTotal, moneyWon,txt5;

    EventHandler<ActionEvent> startGame, goToBetInitial, betToGame, hit,stay;

    BlackjackGame game = new BlackjackGame();

    HashMap<String,Scene> sceneHashMap;

    Pane playerRoot, dealerRoot;
    ImageView cardBack, newCard;

    PauseTransition pauseStay = new PauseTransition(Duration.seconds(1));
    PauseTransition toBetScenePause = new PauseTransition(Duration.seconds(4));

    PauseTransition winnerAnnouncement = new PauseTransition(Duration.seconds(2));


    public static void main(String[] args) {
        launch(args);
    }


    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Blackjack Game");

        sceneHashMap = new HashMap<String, Scene>();

        game.theDealer.shuffleDeck();

        startButton = new Button("Start");
        startButton.setPrefSize(100,50);
        startButton.setStyle("-fx-font-size: 25");

        GameTitle.setStyle("-fx-font-size: 80;");
        GameTitle.setFont(Font.font("Book Antiqua"));

        startGame = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                primaryStage.setScene(sceneHashMap.get("startingMoney"));
            }
        };


        pauseStay.setOnFinished(event -> {
            dealerRoot.getChildren().add(newCard);
            dealerTotal.setText("Dealer Total: " + game.gameLogic.handTotal(game.bankerHand));
            if(game.gameLogic.handTotal(game.bankerHand) > 21){
                boolean ace = false;
                int index = 0;

                for(int i = 0; i < game.bankerHand.size(); i++){
                    if(game.bankerHand.get(i).value == 11){
                        ace = true;
                        index = i;
                    }

                }

                if(ace){
                    game.bankerHand.get(index).value = 1;
                    dealerTotal.setText("Dealer Total: " + game.gameLogic.handTotal(game.bankerHand));
                }
            }

        });

        toBetScenePause.setOnFinished(event -> {
            String ownedString = txt4.getText();
            String winnings = ownedString.substring(ownedString.lastIndexOf("$") +1);
            game.totalWinnings = Double.parseDouble(winnings);
            if(game.totalWinnings == 0){
                sceneHashMap.put("noMoney",noMoney());
                primaryStage.setScene(sceneHashMap.get("noMoney"));
            }
            else{
                sceneHashMap.put("bet",betMoney(game.totalWinnings));
                primaryStage.setScene(sceneHashMap.get("bet"));
            }
        });

        goToBetInitial = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                game.totalWinnings = Double.parseDouble(t1.getText());
                sceneHashMap.put("bet",betMoney(game.totalWinnings));
                primaryStage.setScene(sceneHashMap.get("bet"));

            }
        };

        betToGame = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                String ownedString = currentMoneyTxt.getText();
                String winnings = ownedString.substring(ownedString.lastIndexOf("$") +1);
                game.totalWinnings = Double.parseDouble(winnings);
                game.currentBet = Double.parseDouble(t2.getText());
                Double originalWinnings = game.totalWinnings;
                game.totalWinnings = game.totalWinnings - game.currentBet;
                if(game.totalWinnings < 0){
                    sceneHashMap.put("bet",betMoney(originalWinnings));
                    primaryStage.setScene(sceneHashMap.get("bet"));
                }
                else{
                    sceneHashMap.put("game",gameContent(game.currentBet, game.totalWinnings));
                    primaryStage.setScene(sceneHashMap.get("game"));
                }

            }
        };

        hit = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                game.playerHand.add(game.theDealer.drawOne());
                int newX = ((game.playerHand.size() * 10) + ((game.playerHand.size()-1) * 90));
                newCard = new ImageView(new Image("/" + game.playerHand.get(game.playerHand.size()-1).suit +".png"));
                newCard.setFitHeight(100);
                newCard.setFitWidth(90);
                newCard.setX(newX);
                newCard.setY(10);
                playerTotal.setText("Player Total: " + game.gameLogic.handTotal(game.playerHand));
                playerRoot.getChildren().add(newCard);

                if(game.gameLogic.handTotal(game.playerHand) > 21){
                    boolean ace = false;
                    int index = 0;

                    for(int i = 0; i < game.playerHand.size(); i++){
                        if(game.playerHand.get(i).value == 11){
                            ace = true;
                            index = i;
                        }
                    }

                    if(ace){
                        game.playerHand.get(index).value = 1;
                        playerTotal.setText("Player Total: " + game.gameLogic.handTotal(game.playerHand));
                    }else{
                        hitButton.setDisable(true);
                        stayButton.setDisable(true);
                        gameResult.setText(game.gameLogic.whoWon(game.playerHand,game.bankerHand) + " Wins");
                        cardBack.setVisible(false);
                        dealerTotal.setText("Dealer Total: " + game.gameLogic.handTotal(game.bankerHand));
                        toBetScenePause.play();
                    }

                }

            }
        };

        winnerAnnouncement.setOnFinished(e -> {
            if(Objects.equals(game.gameLogic.whoWon(game.playerHand, game.bankerHand), "Push")){
                game.totalWinnings = game.totalWinnings + game.evaluateWinnings();
                txt4.setText("Money Left: $" + Double.toString(game.totalWinnings));
                gameResult.setText(game.gameLogic.whoWon(game.playerHand,game.bankerHand));
                toBetScenePause.play();
            }
            else{
                game.totalWinnings = game.totalWinnings + game.evaluateWinnings();
                if(Objects.equals(game.gameLogic.whoWon(game.playerHand, game.bankerHand), "Player")){
                    moneyWon.setText("Won: $" + game.evaluateWinnings());
                }
                txt4.setText("Money Left: $" + Double.toString(game.totalWinnings));

                gameResult.setText(game.gameLogic.whoWon(game.playerHand,game.bankerHand) + " Wins");
                toBetScenePause.play();
            }
        });



        stay = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {

                hitButton.setDisable(true);
                stayButton.setDisable(true);

                cardBack.setVisible(false);
                dealerTotal.setText("Dealer Total: " + game.gameLogic.handTotal(game.bankerHand));

                while(game.gameLogic.evaluateBankerDraw(game.bankerHand)){
                    game.bankerHand.add(game.theDealer.drawOne());
                    int newX = ((game.bankerHand.size() * 10) + ((game.bankerHand.size()-1) * 90));
                    newCard = new ImageView(new Image("/" + game.bankerHand.get(game.bankerHand.size()-1).suit +".png"));
                    newCard.setFitHeight(100);
                    newCard.setFitWidth(90);
                    newCard.setX(newX);
                    newCard.setY(10);

                    pauseStay.play();


                }

                winnerAnnouncement.play();

            }
        };

        sceneHashMap.put("start", startScene());
        sceneHashMap.put("startingMoney", startingMoney());

        primaryStage.setScene(sceneHashMap.get("start"));
        primaryStage.show();
    }

    public Scene startScene(){

        v1 = new VBox(20,GameTitle, startButton);
        startButton.setOnAction(startGame);
        v1.setAlignment(Pos.CENTER);
        v1.setStyle("-fx-background-color: darkgreen");


        return new Scene(v1,1200,500);
    }


    public Scene startingMoney(){

        startMoney = new Text("Choose the starting amount of money");
        startMoney.setStyle("-fx-font-size: 30;");
        startMoney.setFont(Font.font("Book Antiqua"));

        t1 = new TextField();
        t1.setMaxSize(150,100);

        next1 = new Button("Next");
        next1.setPrefSize(120,50);
        next1.setStyle("-fx-font-size: 25");
        next1.setOnAction(goToBetInitial);


        v2 = new VBox(20,startMoney,t1, next1);
        v2.setAlignment(Pos.CENTER);
        v2.setStyle("-fx-background-color: darkgreen");


        return new Scene(v2,1200,500);

    }

    public Scene betMoney(Double totalWinnings){

        if(game.theDealer.deckSize() < 15){
            game.theDealer.shuffleDeck();
        }

        txt2 = new Text("Choose the amount you would like to bet");
        txt2.setStyle("-fx-font-size: 30;");
        txt2.setFont(Font.font("Book Antiqua"));

        currentMoneyTxt = new Text("Money Left: $" + Double.toString(totalWinnings));
        currentMoneyTxt.setStyle("-fx-font-size: 25;");
        currentMoneyTxt.setFont(Font.font("Book Antiqua"));


        t2 = new TextField();
        t2.setMaxSize(150,100);

        next2 = new Button("Next");
        next2.setPrefSize(90,50);
        next2.setStyle("-fx-font-size: 25");
        next2.setOnAction(betToGame);

        v3 = new VBox(20,txt2,currentMoneyTxt, t2, next2);
        v3.setAlignment(Pos.CENTER);
        v3.setStyle("-fx-background-color: darkgreen");


        return new Scene(v3,1200,500);

    }

    public Scene noMoney(){

        txt5 = new Text("No money left!");
        txt5.setStyle("-fx-font-size: 30;");
        txt5.setFont(Font.font("Book Antiqua"));


        restartButton = new Button("Restart Game");
        restartButton.setPrefSize(120,50);
        restartButton.setStyle("-fx-font-size: 15");
        restartButton.setOnAction(startGame);


        v6 = new VBox(20,txt5,restartButton);
        v6.setAlignment(Pos.CENTER);
        v6.setStyle("-fx-background-color: darkgreen");


        return new Scene(v6,1200,500);

    }

    public Scene gameContent(Double bet, Double moneyOwned){

        game.totalWinnings = moneyOwned;
        game.currentBet = bet;

        game.playerHand = game.theDealer.dealHand();
        game.bankerHand = game.theDealer.dealHand();

        playerRoot = new Pane();
        dealerRoot = new Pane();
        moneyWon = new Text();
        moneyWon.setStyle("-fx-font-size: 30");

        cardBack = new ImageView(new Image("/cardBack.png"));
        cardBack.setX(110);
        cardBack.setY(10);
        cardBack.setFitHeight(100);
        cardBack.setFitWidth(90);


        ImageView player1 = new ImageView(new Image("/" + game.playerHand.get(0).suit +".png"));
        player1.setX(10);
        player1.setY(10);
        player1.setFitHeight(100);
        player1.setFitWidth(90);


        ImageView player2 = new ImageView(new Image("/" + game.playerHand.get(1).suit +".png"));
        player2.setX(110);
        player2.setY(10);
        player2.setFitHeight(100);
        player2.setFitWidth(90);

        playerRoot.getChildren().addAll(player1,player2);

        h1 = new HBox(playerRoot);

        hitButton = new Button("Hit");
        hitButton.setPrefSize(90,50);
        hitButton.setStyle("-fx-font-size: 20");
        hitButton.setOnAction(hit);

        stayButton = new Button("Stay");
        stayButton.setPrefSize(90,50);
        stayButton.setStyle("-fx-font-size: 20");
        stayButton.setOnAction(stay);

        gameResult = new Text();
        gameResult.setStyle("-fx-font-size: 30");

        h2 = new HBox(hitButton,stayButton,gameResult, moneyWon);
        h2.setAlignment(Pos.CENTER);
        h2.setSpacing(30);


        ImageView dealer1 = new ImageView(new Image("/" + game.bankerHand.get(0).suit +".png"));
        dealer1.setX(10);
        dealer1.setY(10);
        dealer1.setFitHeight(100);
        dealer1.setFitWidth(90);


        ImageView dealer2 = new ImageView(new Image("/" + game.bankerHand.get(1).suit +".png"));
        dealer2.setX(110);
        dealer2.setY(10);
        dealer2.setFitHeight(100);
        dealer2.setFitWidth(90);

        dealerRoot.getChildren().addAll(dealer1,dealer2,cardBack);


        txt3 = new Text("Current Bet: $" + Double.toString(game.currentBet));
        txt3.setStyle("-fx-font-size: 25;");
        txt3.setFont(Font.font("Book Antiqua"));


        txt4 = new Text("Money Left: $" + Double.toString(game.totalWinnings));
        txt4.setStyle("-fx-font-size: 25;");
        txt4.setFont(Font.font("Book Antiqua"));

        dealerTotal = new Text("Dealer Total: " + Integer.toString(game.bankerHand.get(0).value));
        dealerTotal.setStyle("-fx-font-size: 25;");
        playerTotal = new Text("Player Total: " + Integer.toString(game.gameLogic.handTotal(game.playerHand)));
        playerTotal.setStyle("-fx-font-size: 25;");

        v7 = new VBox(dealerTotal);
        v8 = new VBox(playerTotal);
        v9 = new VBox(txt3,txt4);


        v5 = new VBox(70, v7,v9,v8);
        v5.setAlignment(Pos.CENTER);
        v5.setMinWidth(300);
        v5.setStyle("-fx-background-color: darkgreen");

        v4 = new VBox(100,dealerRoot,h2, playerRoot);
        v4.setAlignment(Pos.CENTER);
        v4.setMinWidth(900);
        v4.setStyle("-fx-background-color: darkgreen");


        BorderPane gamePane = new BorderPane();
        gamePane.setLeft(v4);
        gamePane.setRight(v5);

        if(game.playerHand.size() == 2 && game.gameLogic.handTotal(game.playerHand) == 22){
            boolean ace = false;
            int index =0;
            for(int i = 0; i < game.playerHand.size(); i++){
                if(game.playerHand.get(i).value == 11){
                    ace = true;
                    index = i;
                }

            }
                game.playerHand.get(index).value = 1;
                playerTotal.setText("Dealer Total: " + game.gameLogic.handTotal(game.playerHand));
        }

        if((game.gameLogic.handTotal(game.playerHand) == 21 && game.playerHand.size() == 2)){
            hitButton.setDisable(true);
            stayButton.setDisable(true);
            cardBack.setVisible(false);
            dealerTotal.setText("Dealer Total: " + game.gameLogic.handTotal(game.bankerHand));

            if(Objects.equals(game.gameLogic.whoWon(game.playerHand, game.bankerHand), "Push")){
                game.totalWinnings = game.totalWinnings + game.evaluateWinnings();
                txt4.setText("Money Left: $" + Double.toString(game.totalWinnings));
                gameResult.setText(game.gameLogic.whoWon(game.playerHand,game.bankerHand));
                toBetScenePause.play();
            }
            else{
                game.totalWinnings = game.totalWinnings + game.evaluateWinnings();
                if(Objects.equals(game.gameLogic.whoWon(game.playerHand, game.bankerHand), "Player")){
                    moneyWon.setText("Won: $" + game.evaluateWinnings());
                }
                txt4.setText("Money Left: $" + Double.toString(game.totalWinnings));

                gameResult.setText(game.gameLogic.whoWon(game.playerHand,game.bankerHand) + " Wins");
                toBetScenePause.play();
            }
        }

        return new Scene(gamePane,1200,500);

    }

}
