A project for my software design class.
Developed in JavaFX this is a BlackJack game application.

Java Folder:
  Contains the 5 java files that come together to run the application.
  
    BlackjackDealer.java: Contains the class BlackjackDealer which has 5 functions. Those being, generatedeck(), dealHand(), drawone(), 
    shuffleDeck(), and deckSize()
    
    BlackjackGame.java: Contains the class BlackjackGame and it's function evaluateWinnings() which calculates
    how much the player has won or lost after the round.

    BlackjackGameLogic.java: Contains the class BlackjackGameLogic and it's functions, whoWon(), handTotal(),
    and evaluateBankerDraw()

    Card.java: Contains the class Card.

    JavaFXTemplate.java: Contains the code needed to setup the UI of the application.
    

Resources Folder:
  Contains the png files for the card images to be used in the application.

test/java Folder:
  Contains the CardTests.java which is a file testing if the result of certain functions is correct.

