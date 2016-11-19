/* 
 * [ConnectFourv2.java]
 * Andi Li, Joshua Huliganga, Paul Shen
 * May 2, 2016
 */

//import GUI
import javax.swing.*;
import java.awt.*;

//import listener
import java.awt.event.*;

/**********************************Start class**********************************/
class ConnectFourv3{
  
  //variables used throughout the game
  //gui elements that change or respond to actions
  static JButton dropButton1;
  static JButton dropButton2;
  static JButton dropButton3;
  static JButton dropButton4;
  static JButton dropButton5;
  static JButton dropButton6;
  static JButton dropButton7;
  static JButton newGame;
  static JButton exitGame;
  static JLabel[][] spotLabel;
  static ImageIcon imageIcon1;
  static ImageIcon imageIcon2;
  static ImageIcon imageIcon3;
  static JLabel eventTextLabel;
  static JLabel playerTimerLabel;
  static JLabel aiTimerLabel;
  static JLabel playerMovesLabel;
  static JLabel aiMovesLabel;
  static JLabel playerWinsLabel;
  static JLabel aiWinsLabel;
  static JLabel tiesLabel;
  static JLabel playerAvgLabel;
  static JLabel aiAvgLabel;
  
  
  //variables used to play game
  static int [][]board;
  static int playerID;
  static boolean playGame;
  public static final int depth1 = 3;
  
  //game statistics
  static long aiTime;
  static long playerTime;
  static long lastAiTime;
  static long lastPlayerTime;
  static int aiMoves;
  static int playerMoves;
  static int playerWins;
  static int aiWins;
  static int numTies;
  static Timer playerTimer;

  /**********************************main method**********************************/
  public static void main(String args[]){
    //tracks whether a game is currently being played
    playGame = false;
    
    //creates an empty board
    board = new int [6][7];
    
    //intializes game statistics
    aiWins = 0;
    playerWins = 0;
    aiTime = 0;
    playerTime = 0;
    
    //SEXY
    try{UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());} catch (Exception e) {}
    
    //creates JFrame
    JFrame myWindow = new JFrame("Connect Four!");
    myWindow.setLayout(new BorderLayout());
    myWindow.setSize(1200,680);
    myWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);    
    
    //west panel
    JPanel leftPanel = new JPanel();
    leftPanel.setLayout(new FlowLayout());       
    
    //7x7 grid panel for buttons and each spot
    JPanel gridPanel = new JPanel();
    gridPanel.setLayout(new GridLayout(7,7)); 
    gridPanel.setPreferredSize (new Dimension(609,530));
    
    //Image for button
    ImageIcon buttonImage = new ImageIcon("button image.png");
    
    //buttons to choose which column to drop pieces in
    
    //button 1
    dropButton1 = new JButton(buttonImage);
    dropButton1.addActionListener(new dropButtonListener1());
    gridPanel.add(dropButton1);    
    
    //button 2
    dropButton2 = new JButton(buttonImage);
    dropButton2.addActionListener(new dropButtonListener2());
    gridPanel.add(dropButton2);
    
    //button 3
    dropButton3 = new JButton(buttonImage);
    dropButton3.addActionListener(new dropButtonListener3());
    gridPanel.add(dropButton3);
    
    //button 4
    dropButton4 = new JButton(buttonImage);
    dropButton4.addActionListener(new dropButtonListener4());
    gridPanel.add(dropButton4);
    
    //button 5
    dropButton5 = new JButton(buttonImage);
    dropButton5.addActionListener(new dropButtonListener5());
    gridPanel.add(dropButton5);
    
    //button 6
    dropButton6 = new JButton(buttonImage);
    dropButton6.addActionListener(new dropButtonListener6());
    gridPanel.add(dropButton6);
    
    //button 7
    dropButton7 = new JButton(buttonImage);
    dropButton7.addActionListener(new dropButtonListener7());
    gridPanel.add(dropButton7);
    
    //2d array for each spot
    spotLabel = new JLabel[6][7];
    
    //initializes the pictures for both players and empty
    imageIcon1 = new ImageIcon("blank.png");
    imageIcon2 = new ImageIcon("player 1 piece.png");
    imageIcon3 = new ImageIcon("player 2 piece.png");
    
    //sets each label for each connect 4 spot
    for(int i = 5; i > -1; i--){
      for (int j = 0; j < 7; j++){
        if(board[i][j] == 1){
          spotLabel[i][j] = new JLabel(imageIcon2);
        }else if(board[i][j] == -1){
          spotLabel[i][j] = new JLabel(imageIcon3);
        }else{        
          spotLabel[i][j] = new JLabel(imageIcon1);
        }
        gridPanel.add(spotLabel[i][j]);        
      }        
    }
    
    leftPanel.add(gridPanel);
    myWindow.add(leftPanel,BorderLayout.CENTER);
    
    //east panel
    JPanel rightPanel = new JPanel();
    rightPanel.setLayout(new BoxLayout(rightPanel,BoxLayout.Y_AXIS));
    
    JLabel title = new JLabel("CONNECT FOUR!        ");   
    title.setFont(new Font("Arial",Font.BOLD, 40));
    title.setBorder(BorderFactory.createEmptyBorder(0,100,0,10));
    rightPanel.add(title);
    
    JLabel creators = new JLabel ("By: Andi Li, Paul Shen, Joshua Huliganga");
    creators.setFont (new Font ("Arial",Font.BOLD,20));
    creators.setBorder(BorderFactory.createEmptyBorder(0,70,10,0));
    rightPanel.add (creators);
    
    JLabel instructions1 = new JLabel("Instructions:");
    instructions1.setFont(new Font("Arial",Font.BOLD, 20));
    instructions1.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
    rightPanel.add(instructions1);
    
    JLabel instructions2 = new JLabel("1. When it is your turn, click on the button above a column to drop your piece in it.");
    instructions2.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
    rightPanel.add(instructions2);
    
    JLabel instructions3 = new JLabel("2. Wait for the AI to place its piece, then repeat steps 1-2.");
    instructions3.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
    rightPanel.add(instructions3);
    
    JLabel instructions4 = new JLabel("3. First one to get four in a row horizontally, vertically, or diagonally, wins the game!");
    instructions4.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
    rightPanel.add(instructions4);
    
    JPanel timerPanel = new JPanel();
    timerPanel.setLayout(new BoxLayout(timerPanel, BoxLayout.Y_AXIS));
    timerPanel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

    playerTimerLabel = new JLabel("Human      || 0:00");
    playerTimerLabel.setForeground(Color.black);
    playerTimerLabel.setFont(new Font("Arial",Font.BOLD,30));
     
    aiTimerLabel = new JLabel("Computer || 0:00");
    aiTimerLabel.setForeground(Color.black);
    aiTimerLabel.setBorder(BorderFactory.createEmptyBorder(10,0,0,0));
    aiTimerLabel.setFont(new Font("Arial",Font.BOLD,30));
    
    
    /**********************************Method that tracks player time taken in hundreths of seconds**********************************/
    ActionListener playerTimerLabelListener = new ActionListener (){
      public void actionPerformed(ActionEvent actionEvent) {
        playerTime += System.currentTimeMillis() - lastPlayerTime;
        lastPlayerTime = System.currentTimeMillis();
        playerTimerLabel.setText("Human      || " + secondsToMin (playerTime) + ":" + remainSeconds (playerTime) + "     ");
        if (playerMoves != 0)
          playerAvgLabel.setText ("Time per move: " + secondsToMin (playerTime/playerMoves) + ":" + remainSeconds (playerTime/playerMoves));
      }
    };
    
    //fires each action listener every tenth of a second
    //aiTimer tracks ai time
    //playerTimer tracks human time
    playerTimer = new Timer (100, playerTimerLabelListener);
    
    aiMovesLabel = new JLabel ("Moves made by Computer : " + aiMoves);
    playerMovesLabel = new JLabel ("Moves made by Human : " + playerMoves);
    
    playerAvgLabel = new JLabel ("Time per move: 0:00");
    aiAvgLabel = new JLabel ("Time per move: 0 ms");
    
    aiWinsLabel = new JLabel ("Computer Wins: " + aiWins);    
    playerWinsLabel = new JLabel ("Human Wins: " + playerWins);    
    tiesLabel = new JLabel ("Ties: " + numTies);
    tiesLabel.setBorder(BorderFactory.createEmptyBorder(15,0,10,0));
    
    timerPanel.add(playerTimerLabel);
    timerPanel.add(playerMovesLabel);
    timerPanel.add(playerAvgLabel);
    timerPanel.add(playerWinsLabel);
    timerPanel.add(aiTimerLabel);
    timerPanel.add(aiMovesLabel);
    timerPanel.add(aiAvgLabel);
    timerPanel.add(aiWinsLabel);
    timerPanel.add(tiesLabel);
    
    rightPanel.add(timerPanel);
    
    myWindow.add(rightPanel,BorderLayout.EAST);
    
    //NORTH PANEL//
    JPanel topPanel = new JPanel(); 
    topPanel.setLayout(new FlowLayout());
    topPanel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
    
    newGame = new JButton("New Game");
    newGame.addActionListener (new newGameListener());
    topPanel.add(newGame);
    
    exitGame = new JButton("Exit Game");
    exitGame.addActionListener (new exitGameListener());
    topPanel.add(exitGame);
    
    myWindow.add(topPanel,BorderLayout.NORTH);
    
    //SOUTH PANEL//
    JPanel botPanel = new JPanel();
    botPanel.setLayout(new FlowLayout());
    
    eventTextLabel = new JLabel("Start a new game!");
    eventTextLabel.setFont(new Font("Arial",Font.BOLD,30));
    eventTextLabel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
    botPanel.add(eventTextLabel);
    
    myWindow.add(botPanel,BorderLayout.SOUTH);
    
    myWindow.setResizable(false);
    myWindow.setVisible(true);
  }
  
  /**********************************Method that exits the game**********************************/
  static class exitGameListener implements ActionListener{    
    public void actionPerformed(ActionEvent event){
      System.exit (0);
    }
  }
  
  /**********************************Method that drops token into row 1**********************************/
  static class dropButtonListener1 implements ActionListener{    
    public void actionPerformed(ActionEvent event){
      drop (0);
    }
  }
  
  /**********************************Method that drops token into row 2**********************************/
  static class dropButtonListener2 implements ActionListener{
    public void actionPerformed(ActionEvent event){
      drop (1);
    }
  } 
  
  /**********************************Method that drops token into row 3**********************************/
  static class dropButtonListener3 implements ActionListener{
    public void actionPerformed(ActionEvent event){
      drop (2);
    }
  }
  
  /**********************************Method that drops token into row 4**********************************/
  static class dropButtonListener4 implements ActionListener{
    public void actionPerformed(ActionEvent event){
      drop (3);
    }
  } 
  
  /**********************************Method that drops token into row 5**********************************/
  static class dropButtonListener5 implements ActionListener{
    public void actionPerformed(ActionEvent event){
      drop (4);
    }
  } 
  
  /**********************************Method that drops token into row 6**********************************/
  static class dropButtonListener6 implements ActionListener{
    public void actionPerformed(ActionEvent event){
      drop (5);
    }
  }
  
  /**********************************Method that drops token into row 7**********************************/
  static class dropButtonListener7 implements ActionListener{
    public void actionPerformed(ActionEvent event){
      drop (6);
    }
  }
  
   /**********************************Method that resets game when button is clicked**********************************/
 
  static class newGameListener implements ActionListener{
    public void actionPerformed(ActionEvent event){
      String [] tempArray = new String [3];

      //initialize array
      tempArray [0] = "Computer";
      tempArray [1] = "Cancel";
      tempArray [2] = "Human";
      
      //dialogue box to ask which player is a computer
      int choice = JOptionPane.showOptionDialog(null, "Who makes the first move?", "Hello!", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, tempArray, tempArray[0]);

      if (choice == 0 || choice == 2){
        //resets the board and game stats
        board = new int [6][7];
        printBoard();
        aiTime = 0;
        playerTime = 0;
        aiMoves = 0;
        playerMoves = 0;
        //resets labels
        aiMovesLabel.setText ("Moves made by Computer: " + aiMoves);
        playerMovesLabel.setText ("Moves made by Human: " + playerMoves);
        aiTimerLabel.setText("Computer || " + secondsToMin (aiTime) + ":" + remainSeconds (aiTime));
        //game is currently being played
        playGame = true;
      }
      
      //if computer goes first
      if (choice == 0){
        //sets playerID of computer to 1
        playerID = 1;
        eventTextLabel.setText ("Computer is thinking...");
        aiTimerLabel.setForeground(Color.red);
        playerTimerLabel.setForeground(Color.yellow);
        
        //************************ AI plays first ********************\\
        //starts timer for ai
        lastAiTime = System.currentTimeMillis();
        
        //first move is at middle
        board = playMove (board, 3, playerID);
        //stops timer for ai
        
        aiTime += System.currentTimeMillis() - lastAiTime;
        aiTimerLabel.setText("Computer || " + secondsToMin (aiTime) + ":" + remainSeconds (aiTime));
        
        printBoard();
        
        //updates game stats for ai
        aiMoves++;
        aiMovesLabel.setText ("Moves made by Computer: " + aiMoves);
        
        //starts timer for human
        playerTimer.start();
        lastPlayerTime = System.currentTimeMillis();
        
        
        //sets playerID of human to -1
        playerID = -1;
        eventTextLabel.setText ("Human is thinking...");
      }
      //if player goes first
      else if (choice == 2){
        //sets playerID of human to 1
        playerID = 1;
        eventTextLabel.setText ("Human is thinking...");
        aiTimerLabel.setForeground(Color.yellow);
        playerTimerLabel.setForeground(Color.red);
        
        //starts timer for human
        playerTimer.start();
        lastPlayerTime = System.currentTimeMillis();
      }
    }
  }
  
  /**********************************Method that checks if the top spot is empty in a column (valid move)**********************************/
  public static void drop (int move){
    //check if a valid move, no one has won and there is a game going on
    if (validMove (board, move) & win (board) == 0 & playGame){
      //stop timer when player drops
      playerTimer.stop();
      playerTime += System.currentTimeMillis() - lastPlayerTime;
      
      board = playMove(board, move, playerID);      
      printBoard();
      
      playerMoves++;
      playerMovesLabel.setText ("Moves made by Human: " + playerMoves);
      eventTextLabel.setText ("Computer is thinking...");
      
      //stops the game if player has won before computer makes a move
      if (win(board) == 0){
        playerID = -1*playerID;
        
        lastAiTime = System.currentTimeMillis();
        
        board = playMove (board, alphabetaCaller (board, depth1, playerID, playerID), playerID);
        
        aiTime += System.currentTimeMillis() - lastAiTime;
        aiTimerLabel.setText("Computer || " + secondsToMin (aiTime) + ":" + remainSeconds (aiTime));

       
        playerTimer.start();
        lastPlayerTime = System.currentTimeMillis();
        
        printBoard();
        
        playerID = -1*playerID;
        eventTextLabel.setText ("Human is thinking...");
        aiMoves++;
        aiAvgLabel.setText ("Time per move: " + aiTime/aiMoves + " ms");
        aiMovesLabel.setText ("Moves made by Computer: " + aiMoves);
      }
      
      //detecting if someone wins or ties
      int gameStatus = win(board);
      if (gameStatus !=0){
        lastAiTime = System.currentTimeMillis();
        aiTime += System.currentTimeMillis() - lastAiTime;
        aiTimerLabel.setText("Computer || " + secondsToMin (aiTime) + ":" + remainSeconds (aiTime));
        aiAvgLabel.setText ("Time per move: " +(aiTime/aiMoves) + " ms");
        
        playerTimer.stop();
        playerTime += System.currentTimeMillis() - lastPlayerTime;
      }
      
      if (gameStatus == playerID){
        eventTextLabel.setText ("Human has won");
        playerWins ++;
        playerWinsLabel.setText ("Human Wins: " + playerWins);    
      }
      else if (gameStatus == -1*playerID){
        eventTextLabel.setText ("Computer has won");
        aiWins ++;
        aiWinsLabel.setText ("Computer Wins: " + aiWins);  
      }
      else if (gameStatus == 2){
        eventTextLabel.setText ("It's a tie!");
        numTies ++;
        tiesLabel.setText ("Ties: " + numTies);
      }
    }
  }
  
  
  /**********************************Method that displays the board**********************************/
  public static void printBoard(){
    for(int i = 5; i > -1; i--){
      for (int j = 0; j < 7; j++){
        if(board[i][j] == 1){
          spotLabel[i][j].setIcon(imageIcon2);
        }else if(board[i][j] == -1){
          spotLabel[i][j].setIcon(imageIcon3);
        }else{        
          spotLabel[i][j].setIcon(imageIcon1);
        }
      }        
    }    
  }
  
  /**********************************Method that calls alphabeta method and returns the move that gives the largest value**********************************/
  public static int alphabetaCaller (int [][]board, int depth, int playerID, int turn){
    // array to store values of each possible child of root node
    int [][] scores = new int [numMoves(board)][2];
    // used to iterate through scores array
    int a = 0;
    //for each column at x
    for (int x = 0; x < board[0].length; x ++){
      // only if the column at x is not filled up
      if (validMove (board, x)){
        //put the score of moving to column x into array
        scores [a][0] = alphabeta (playMove (board, x, playerID), depth, -100000, 100000, playerID, turn*-1);
        // store the column of the move
        scores [a][1] = x;
        // move onto next element of array
        a++;
      }
    }
    
    //finds the move that gives the maxiumum score
    int max = 0;
    for (int i = 0; i < scores.length; i ++){
      //to illustrate values of each given move
//      System.out.println ("x: " + scores[i][1] + " value: " + scores [i][0]);
      
      if (scores [i][0] > scores [max][0])
        max = i;
    }
    // if there are multiple maximums, pick the closest one to the middle
    for (int i = 0; i < scores.length; i++){
      if (scores [i][0] == scores [max][0]){
        if (Math.abs ((board[0].length -1)/2 - scores [i][1]) < Math.abs ((board[0].length -1)/2 - scores [max][1]))
          max = i;
      }
    }    
    return scores [max][1];
  }
  
  
  /**********************************Method that gives each move a score (value)**********************************/
  public static int alphabeta (int [][]board, int depth, int alpha, int beta, int playerID, int turn){
    // if it is a leaf node or terminal node (has a winner)
    if (depth == 0 || win (board) != 0)
      return evalBoard (board, playerID);
    
    // if it is maximizing players turn
    if (playerID == turn){
      // initalize at lowest possible score
      int v = -100000;
      //for each possible move at column number x
      for (int x = 0; x < board[0].length; x ++){
        // only do this if the column at x is not filled
        if (validMove (board,x)){
          // the value of this game state is the maximum of possible values of boards where turn player has moved to column x
          v = max (v, alphabeta (playMove(board, x, turn), depth -1, alpha, beta, playerID, turn*-1));
          //alpha value is the smallest score I would choose so far
          alpha = max (alpha, v);
          if (beta <= alpha)
            break;
        }
      }
      //return the value of this node
      return v;
    }
    // it is the minimziing players turn
    else{
      // initalzie value of game state at maximum possible score
      int v = 100000;
      //for each possible move at column x
      for (int x = 0; x < board[0].length; x ++){
        // if column x is not filled up
        if (validMove (board,x)){
          // minimizing player (enemy player) will seek to minimize current player's score
          // therefore the value of this node is the minimum value of its children
          v = min (v, alphabeta (playMove(board, x, turn), depth -1, alpha, beta, playerID, turn*-1));
          // this is the largest value the minizing player would choose so far
          beta = min (beta, v);
          //if the smallest value the maximizing player would choose is larger than the largest value the minimizing player could choose, do not consider further children, as this node will not be selected
          if (beta <= alpha)
            break;
        }
      }
      //return value of this node
      return v;
    }
  }
  
  
  /**********************************Method that finds the maximum of two numbers**********************************/
  public static int max (int num1, int num2){
    if (num1 > num2)
      return num1;
    else
      return num2; 
  }
  
  
  /**********************************Method that finds the minimum of two numbers**********************************/
  public static int min (int num1, int num2){
    if (num1 < num2)
      return num1;
    else
      return num2;
  }
  
  
  /**********************************Method that places token in a column, without checking to see if it is a valid move**********************************/
  public static int [][] playMove (int [][] board, int move, int playerID){
    // create new array, so i dont mess up original board
    int [][] boardCopy = new int [board.length][board[0].length];
    
    //copys tokens over from old array into new array
    for (int x = 0; x < board.length; x ++){
      for (int y = 0; y < board[x].length; y ++){
        boardCopy [x][y] = board [x][y];
      }
    }
    
    //starting from bottom, places my token of value playerID into first empty slot
    for (int y = 0; y < board.length; y++){
      if (board [y][move] == 0){
        boardCopy [y][move] = playerID;
        return boardCopy;
      }
    }
    return boardCopy;
  }
  
  
  /**********************************Method that checks if the top spot is empty in a column (valid move)**********************************/
  public static boolean validMove (int [][] board, int move){
    if (board [board.length-1][move] == 0)
      return true;
    else
      return false;
  }
  
  /**********************************Method that counts the number of valid moves for a given board**********************************/
  public static int numMoves (int [][] board){
    int count = 0;
    for (int i = 0; i < board[0].length; i ++){
      if (board [board.length-1][i] == 0)
        count++;
    }
    return count;
  }
  
  
/**********************************Method that gives the board state a number**********************************/
  public static int evalBoard (int [][] board, int playerID){
    int win = win (board);
    
    //if this game state has the player winning, return 1000;
    if (win == playerID)
      //game is a win for the player
      return 1000000;
    //if neither player has won
    else if (win == 0){
      return possFours (board, playerID);
    }
    // if the game state has the player losing return -1000;
    else if (win == -1*playerID){
      //game is a loss for the player
      return -1000000;
    }
    else
      // game is a tie
      return 0;
  }
  
  
  /**********************************Method that returns the playerID of the winner**********************************/
  public static int win (int [][] board){
    int width = board[0].length;
    int height = board.length;
    
    //for each square
    for (int x = 0; x < width; x ++){
      for (int y = 0; y < height; y ++){
        int playerID = board [y][x];
        
        //if square is empty, skip checking the rest
        if (playerID == 0)
          continue;
        
        //check to the right if all tokens are the same as current token
        if (x + 3 < width){
          if (board[y][x+1] == playerID &
              board[y][x+2] == playerID &
              board[y][x+3] == playerID)
            return playerID;
        }
        
        if (y + 3 < height){
          //check vertically if all tokens are the same as current token
          if (board [y+1][x] == playerID &
              board [y+2][x] == playerID &
              board [y+3][x] == playerID)
            return playerID;
        }
        
        //check diagonals (/) if all tokens are the same as current token
        if (y + 3 < height & x + 3 < width){
          if (board [y+1][x+1] == playerID &
              board [y+2][x+2] == playerID &
              board [y+3][x+3] == playerID)
            return playerID;
        }
        
        //check diagonals (\) if all tokens are the same as current token
        if (y + 3 < height & x > 2){
          if (board [y+1][x-1] == playerID &
              board [y+2][x-2] == playerID &
              board [y+3][x-3] == playerID)
            return playerID;
          
        }
      }
    }
    //check if it is a tie
    int count = 0;
    for (int x = 0; x < board.length; x ++){
      for (int y = 0; y < board[x].length; y ++){
        if (board [x][y] == 0)
          count++;
      }
    }
    
    if (count == 0)
      return 2;
    else
      return 0;
    
  }
  
  
  /**********************************Method that calculates my possible four in a rows minus possible enemy four in a rows (heuistic to determine intermediate nodes)**********************************/
  public static int possFours (int [][] board, int playerID){
    //track number of possible fours for each player
    int myFour = 0;
    int enemyFour = 0;
    
    //save some array.length accessing
    int width = board[0].length;
    int height = board.length;
    
    //for each possible square
    for (int x = 0; x < width; x ++){
      for (int y = 0; y < height; y ++){
        int token = board[y][x];
        
        //checking squares to the right of (x,y)
        if (x + 3 < width){
          //if it is my token or empty
          if (token == playerID || token == 0){
            // check if enemy tokens are not blocking my 4 in a rows
            if (board[y][x+1] != -1 * playerID &
                board[y][x+2] != -1 * playerID &
                board[y][x+3] != -1 * playerID)
              myFour ++;
          }
          // if it is enemy tokens or empty
          if (token == -1 * playerID || token == 0){
            // check if my tokens tokens are in blocking enemy 4 in a rows
            if (board[y][x+1] != playerID &
                board[y][x+2] != playerID &
                board[y][x+3] != playerID)
              enemyFour ++;
          }
        }
        
        if (y + 3 < height){
          //if it is my token or empty
          if (token == playerID || token == 0){
            // check if enemy tokens are not blocking my 4 in a rows
            if (board [y+1][x] != -1 * playerID &
                board [y+2][x] != -1 * playerID &
                board [y+3][x] != -1 * playerID)
              myFour ++;
          }
          
          // if it is enemy tokens or empty
          if (token == -1 * playerID || token == 0){
            // check if my tokens tokens are in blocking enemy 4 in a rows
            if (board[y+1][x] != playerID &
                board[y+2][x] != playerID &
                board[y+3][x] != playerID)
              enemyFour ++;
          }
        }
        //check diagonals (/)
        if (y + 3 < height & x + 3 < width){
          //if it is my token or empty
          if (token == playerID || token == 0){
            // check if enemy tokens are not blocking my 4 in a rows
            if (board [y+1][x+1] != -1 * playerID &
                board [y+2][x+2] != -1 * playerID &
                board [y+3][x+3] != -1 * playerID)
              myFour ++;
          }
          
          // if it is enemy tokens or empty
          if (token == -1 * playerID || token == 0){
            // check if my tokens tokens are in blocking enemy 4 in a rows
            if (board[y+1][x+1] != playerID &
                board[y+2][x+2] != playerID &
                board[y+3][x+3] != playerID)
              enemyFour ++;
          }
        }
        
        //check diagonals (\)
        if (y + 3 < height & x > 3){
          //if it is my token or empty
          if (token == playerID || token == 0){
            // check if enemy tokens are not blocking my 4 in a rows
            if (board [y+1][x-1] != -1 * playerID &
                board [y+2][x-2] != -1 * playerID &
                board [y+3][x-3] != -1 * playerID)
              myFour ++;
          }
          // if it is enemy tokens or empty
          if (token == -1 * playerID || token == 0){
            // check if my tokens tokens are in blocking enemy 4 in a rows
            if (board[y+1][x-1] != playerID &
                board[y+2][x-2] != playerID &
                board[y+3][x-3] != playerID)
              enemyFour ++;
          }
        }
      }
    }
    return myFour - enemyFour;
  }
 
  //converts time in tenths of seconds to minutes
  public static String secondsToMin (long s){
    return String.valueOf(s/60000);  
  }
  
  //converts time in tenths of seconds to remaining seconds
  public static String remainSeconds (long s){
    if ((s/1000)%60 < 10)
      return ("0" + (s/1000)%60);
    return String.valueOf ((s/1000)%60);   
  }
}


