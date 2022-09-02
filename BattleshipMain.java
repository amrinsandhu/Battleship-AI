/* Team D: Amrin, Grace, Nivaethan, Jishnu
 * ICS4U0
 * Wednesday, April 21, 2021
 * Group Project: Battleship
 * Team D has programmed a virtual game of Battleship where the user will be able to vs the AI
 */
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

public class BattleshipMain extends JFrame {

	private static JFrame frame2;

	//declaring all the variables 
	public static JPanel panel, panel2; // public panel
	public static JButton userButtons[] = new JButton[49]; // Sets up array to store user board button squares 
	public static JButton AIButtons[] = new JButton[49]; // Sets up array to store AI board button squares
	public static JButton AIShipGrid[] = new JButton[49]; // Sets up array to store AI boat placement tracker board button squares
	public static JLabel endTurn, unavailable, offBoardLabel, fullShipLabel, ship1Label, fullShipLabelU, firstTurn; 
	public static JButton endTurnButton, restart, help, exit; 
	public static int i, t, numOfShips = 0, position, length = 0, length2 = 0, RNum = 0, NumOfAIShips = 0, spotsChosen = 0, spotsChosen1 = 0, ship1 = 3, ship2 = 2, ship3 = 3, AIship1 = 3, AIship2 = 2, AIship3 = 3, allAIships = 3, hitsAI = 0, previousPosition = -1, originalPosition = -1, ShipsSunk, ShipsSunkU; // i and t need to be saved publicly to set up action listeners  
	public static int surroundingShips []  = new int [50], AIships [] = new int [3], userSpotsChosen [] = new int [49], AIsurroundingShips []  = new int [75], AIspotsChosen [] = new int [300], AIship [] = new int [49];
	public static boolean turn = true, hit, unable = false, one = false, two = false, three = false, four = false, randCheck = false, cond = false, miss = false, f = false, go = false, firstTurn2 = true; //variable for who's turn it is (true means users, false means AI), begins with the user's turn
	public static Ships ships[] = new Ships [6];

	// Main frame
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					BattleshipMain frame = new BattleshipMain();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	//starting the game
	public BattleshipMain() 
	{
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 750, 500);
		panel = new JPanel();
		panel.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(panel);
		panel.setLayout(null);

		start(); // Calls on start method to create all frames and begin the game
	}

	//this method creates all the panels, action listeners for our buttons and labels 
	public static void create() {
		panel2 = new JPanel(); // Creates new panel 
		panel2.setBorder(new EmptyBorder(5, 5, 5, 5));
		panel2.setLayout(null);

		frame2 = new JFrame ("AI Ship Tracker"); // instantiate NEW frame with a title
		frame2.setDefaultCloseOperation (JFrame.DO_NOTHING_ON_CLOSE);//making the frame do nothing on close so the user must press the close button to close it 
		frame2.setSize (600, 600);
		frame2.getContentPane ().add (panel2); //add the panel with components to the frame
		frame2.setVisible(false);

		//storing these values as spots the AI cannot choose to hit (since they are located off the board)
		AIspotsChosen[0] = -1;
		spotsChosen = spotsChosen + 1;
		AIspotsChosen[1] = 49;
		spotsChosen = spotsChosen + 1;
		AIspotsChosen[2] = -2;
		spotsChosen = spotsChosen + 1;
		AIspotsChosen[3] = -3;
		spotsChosen = spotsChosen + 1;
		AIspotsChosen[4] = -4;
		spotsChosen = spotsChosen + 1;
		AIspotsChosen[5] = -5;
		spotsChosen = spotsChosen + 1;
		AIspotsChosen[6] = -6;
		spotsChosen = spotsChosen + 1;
		AIspotsChosen[7] = -7;
		spotsChosen = spotsChosen + 1;
		AIspotsChosen[8] = 50;
		spotsChosen = spotsChosen + 1;
		AIspotsChosen[9] = 51;
		spotsChosen = spotsChosen + 1;
		AIspotsChosen[10] = 52;
		spotsChosen = spotsChosen + 1;
		AIspotsChosen[11] = 53;
		spotsChosen = spotsChosen + 1;
		AIspotsChosen[12] = 54;
		spotsChosen = spotsChosen + 1;
		AIspotsChosen[13] = 55;
		spotsChosen = spotsChosen + 1;
		AIspotsChosen[14] = 56;
		spotsChosen = spotsChosen + 1;

		 //setting the label text, location, and adding it to the panel  
		endTurn = new JLabel("Click here to end your turn");
		endTurn.setBounds(50, 431, 367, 16);
		endTurn.setFont(new Font("Serif", Font.PLAIN, 15));
		panel.add(endTurn);
		endTurn.setVisible(false);

		 //setting the button text, location, and adding it to the panel  
		endTurnButton = new JButton("End turn");
		endTurnButton.setBounds(418, 426, 117, 29);
		endTurnButton.setFont(new Font("Arial", Font.PLAIN, 15));
		panel.add(endTurnButton);
		endTurnButton.setVisible(false);

		 //setting the label text, location, and adding it to the panel  
		unavailable = new JLabel("That spot is too close to another ship, choose a new spot");
		unavailable.setBounds(32, 421, 374, 37);
		panel.add(unavailable);
		unavailable.setVisible(false);

		 //setting the label text, location, and adding it to the panel  
		offBoardLabel = new JLabel("Off the board choose a new spot");
		offBoardLabel.setBounds(470, 260, 282, 30);
		panel.add(offBoardLabel);
		offBoardLabel.setVisible (false); 

		 //setting the label text, location, and adding it to the panel  
		fullShipLabel = new JLabel("The AI has sunk " + ShipsSunk +"/3 of your ships");
		fullShipLabel.setFont(new Font("Serif", Font.PLAIN, 15));
		fullShipLabel.setBounds(470, 300, 330, 16);
		panel.add(fullShipLabel);
		fullShipLabel.setVisible (false); 

		 //setting the label text, location, and adding it to the panel  
		fullShipLabelU = new JLabel("You have sunk " + ShipsSunkU +"/3 of the AI's ships");
		fullShipLabelU.setFont(new Font("Serif", Font.PLAIN, 15));
		fullShipLabelU.setBounds(470, 250, 330, 16);
		panel.add(fullShipLabelU);
		fullShipLabelU.setVisible (false); 

		 //setting the label text, location, and adding it to the panel  
		ship1Label = new JLabel("");
		ship1Label.setBounds(450, 230, 282, 30);
		ship1Label.setFont(new Font("Serif", Font.PLAIN, 20));
		panel.add(ship1Label);
		ship1Label.setVisible(false);

		 //setting the button text, location, and adding it to the panel  
		help = new JButton ("Help");
		help.setFont(new Font("Arial", Font.PLAIN, 12));
		help.setBounds(604, 350, 89, 23);
		panel.add(help);

		 //setting the button text, location, and adding it to the panel  
		restart = new JButton ("Restart");
		restart.setFont(new Font("Arial", Font.PLAIN, 15));
		restart.setBounds(604, 390, 89, 23);
		panel.add(restart);
		
		 //setting the label text, location, and adding it to the panel  
		firstTurn = new JLabel("Please choose a square and take your first turn");
		firstTurn.setBounds(50, 431, 367, 16);
		firstTurn.setFont(new Font("Serif", Font.PLAIN, 15));
		panel.add(firstTurn);
		firstTurn.setVisible(false);
		
		 //setting the button text, location, and adding it to the panel  
		exit = new JButton ("Exit"); 
		exit.setEnabled(true);
		exit.setBounds(250, 500, 75, 25);
		exit.setVisible(true);
		panel2.add(exit);

		
		exit.addActionListener (new ActionListener () // Action listener for exit button on help frame
				{
			public void actionPerformed (ActionEvent e)
			{
				frame2.dispose(); // Help frame will become invisible
			}
				});
		
		restart.addActionListener (new ActionListener ()  //adding action listener for restart button
				{
			public void actionPerformed (ActionEvent e)
			{
				restart(); //calls on the restart method to reset all variables, have the AI place its ships again and restart the game 
			}
				});

		// Help button action listener 
		help.addActionListener (new ActionListener () // Nivaethan's help screen
				{
			public void actionPerformed (ActionEvent e)
			{
				JPanel panel3 = new JPanel(); // Creates new panel 
				panel3.setBorder(new EmptyBorder(5, 5, 5, 5));
				panel3.setLayout(null);

				JFrame frame3 = new JFrame ("Help"); // instantiate NEW frame with a title
				frame3.setDefaultCloseOperation (JFrame.DO_NOTHING_ON_CLOSE);
				frame3.setSize (600, 624);
				frame3.getContentPane ().add (panel3); //add the panel with components to the frame
				frame3.setVisible (true);

				//Instantiating a text area 
				JLabel taHelp = new JLabel();
				taHelp.setFont(new Font("Arial", Font.BOLD, 30));
				taHelp.setText("Help");
				taHelp.setBounds(250, 0, 100, 50);

				//Instantiating a text area 
				JTextArea taInfo = new JTextArea();
				taInfo.setEditable(false);
				taInfo.setOpaque(false);
				taInfo.setFont(new Font("Arial", Font.PLAIN, 17));
				taInfo.setLineWrap(true);
				taInfo.setWrapStyleWord(true);
				taInfo.setText("Welcome to the help screen. Here you will find the instructions on how to play Battleship and a legend outlining the various parts of the game.");
				taInfo.setBounds(10, 50, 560, 40);

				// Instantiating a text area
				JTextArea taInst = new JTextArea ();
				taInst.setEditable(false);
				taInst.setOpaque(false);
				taInst.setFont(new Font("Arial", Font.BOLD, 17));
				taInst.setText("Rules");
				taInst.setBounds(250, 100, 50, 17);

				// Instantiating a text area
				JTextArea taRules = new JTextArea ();
				taRules.setEditable(false);
				taRules.setOpaque(false);
				taRules.setFont(new Font("Arial", Font.PLAIN, 15));
				taRules.setLineWrap(true);
				taRules.setWrapStyleWord(true);
				taRules.setText("At the beginning of the game, the player will be prompted to place 3 ships on the 7x7 board: 2 ships with three holes (one horizontal and one vertical) and 1 ship with two holes (horizontal). Your ships appear on the 'AI Board' in pink. The player will take the first turn and click on one of the squares on the 'User board'. If it's a hit, the selected square will turn red and the player will get to pick another sqaure until they miss. If the player succesfully sinks an entire ship, all the red squares apart of that ship will turn a darker red shade to indicate that a ship has been sunk. If their selection ends up being a miss, the sqaure will turn white and the turn will go to the AI. To win the game, the player must be the first to sink all of the AI's ships. You can watch where the AI is trying to hit your ships on the 'AI Board'.");
				taRules.setBounds(5, 120, 580, 200);

				//Instantiating a text area 
				JTextArea taLSymbols = new JTextArea();
				taLSymbols.setEditable(false);
				taLSymbols.setOpaque(false);
				taLSymbols.setFont(new Font("Arial", Font.BOLD, 17));
				taLSymbols.setText("Legend: Symbols");
				taLSymbols.setBounds(110, 315, 139, 29);

				// Instnatiating a button
				JButton buttonHit = new JButton ("HIT");
				buttonHit.setEnabled(false);
				buttonHit.setBounds (10, 340, 75, 75);
				buttonHit.setBackground(Color.RED);

				// Instnatiating a button
				JButton buttonMiss = new JButton ("MISS");
				buttonMiss.setEnabled(false);
				buttonMiss.setBounds (100, 340, 75, 75);
				buttonMiss.setBackground(Color.WHITE);

				// Instnatiating a button
				JButton buttonSink = new JButton ("SUNK");
				buttonSink.setEnabled(false);
				buttonSink.setBounds (190, 340, 125, 75);
				buttonSink.setBackground((new Color(110, 13, 7)));

				// Instnatiating a button
				JButton buttonPlaced = new JButton ("PLACED");
				buttonPlaced.setEnabled(false);
				buttonPlaced.setBounds (330, 340, 85, 75);
				buttonPlaced.setBackground(Color.pink);

				//Instantiating a text area 
				JTextArea taLPieces = new JTextArea();
				taLPieces.setEditable(false);
				taLPieces.setOpaque(false);
				taLPieces.setFont(new Font("Arial", Font.BOLD, 17));
				taLPieces.setText("Legend: Pieces");
				taLPieces.setBounds(110, 430, 139, 29);

				// Instnatiating a button
				JButton button3Hole = new JButton ("3 hole");
				button3Hole.setEnabled(false);
				button3Hole.setBounds (10, 460, 225, 75);
				button3Hole.setBackground(Color.green); // Find out the actual colour for the board

				// Instnatiating a button
				JButton button2Hole = new JButton ("2 hole");
				button2Hole.setEnabled(false);
				button2Hole.setBounds (255, 460, 150, 75);
				button2Hole.setBackground(Color.green);

				// Instnatiating a button
				JButton button3HoleV = new JButton ("3 hole");
				button3HoleV.setEnabled(false);
				button3HoleV.setBounds (430, 320, 75, 225);
				button3HoleV.setBackground(Color.green); // Find out the actual colour for the board

				// Instantiating a button
				JButton buttonExit = new JButton ("Exit"); 
				buttonExit.setEnabled(true);
				buttonExit.setBounds(225, 555, 75, 25);

				// Adding content to panel
				panel3.add(taHelp); 
				panel3.add(taInfo);
				panel3.add(taInst);
				panel3.add(taRules);
				panel3.add(taLSymbols);
				panel3.add(buttonHit);
				panel3.add(buttonMiss);
				panel3.add(buttonSink);
				panel3.add(taLPieces);
				panel3.add(button3Hole);
				panel3.add(button2Hole);
				panel3.add(buttonExit);
				panel3.add(buttonPlaced);
				panel3.add(button3HoleV);

				panel3.repaint();
				panel3.revalidate();

				buttonExit.addActionListener (new ActionListener () // Action listener for exit button on help frame
						{
					public void actionPerformed (ActionEvent e)
					{
						frame3.setVisible(false); // Help frame will become invisible
					}
						}
						);}});


		endTurnButton.addActionListener (new ActionListener ()  // Adds action listener for the end of the user's turn button
				{
			public void actionPerformed (ActionEvent e)
			{
				//while it is still the AI's turn 
				while (turn == false)
				{
					AIintelligence();//calling on the AIintelligence method to choose a spot to hit 
					AIhitDetection (position);//calling on the hit detection method to check if the spot was already chosen and determine whether the spot chosen was a hit or miss 

					//if a ship was hit
					if (hit == true)
					{
						hitsAI = hitsAI + 1; //storing how many times the AI hit
						turn = false; //it is the AI's turn again
					}
					//if a ship was missed
					else if (hit == false) {
						endTurn.setText("Player 2 hit you " + hitsAI + " times. Your turn now");
						hitsAI = 0; //resetting the number of times AI was hit
						//for loop to make all buttons enabled for player's turn
						for (i=0; i<=48; i++) 
						{ 
							userButtons[i].setEnabled(true);
						}
						//for loop to disable all the buttons the user has already chosen 
						for (int k = 0; k < spotsChosen1; k++) {
							userButtons[userSpotsChosen[k]].setEnabled(false);
						}

						turn = true; //exiting the while loop and setting it to the user's turn ;
					}
				}
			}
				}
				);
	}

	//method to call on board methods 
	public static void start() { // Can contain stuff like ask user if they want to start game etc. Right now just calls on board methods 
		panel.setLayout(null); //sets panel layout

		create();//calling on method to create all the panels, action listeners for our buttons and labels 

		// Calls on methods to begin the game (making the game board and having the AI place its ships 
		playerGameBoard();
		AIGameBoard();
		AIGameBoardShips();
		AIShipPlacement();

		//if no ships have been placed 
		if (numOfShips == 0){
			//prompt the user to place their first ship 
			ship1Label.setText("Place your 3 horizontal ship");
			ship1Label.setVisible(true);
		}
	}

	//restart method to restart the game 
	public static void restart() { //Grace
		//Clears and remove panels
		panel.removeAll();
		frame2.dispose();
		
		//Resets all variables
		numOfShips = 0; 
		position=0;
		length = 0; 
		length2 = 0; 
		RNum = 0; 
		NumOfAIShips = 0;
		spotsChosen = 0; 
		spotsChosen1 = 0;
		ship1 = 3; 
		ship2 = 2;
		ship3 = 3;
		AIship1 = 3;
		AIship2 = 2;
		AIship3 = 3;
		allAIships = 3;
		hitsAI = 0;
		previousPosition = -1;
		originalPosition = -1;
		ShipsSunk= 0; 
		ShipsSunkU=0;
		turn = true;
		hit = false;
		unable = false;
		one = false;
		two = false; 
		three = false;
		four = false;
		randCheck = false;
		cond = false;
		miss = false;
		f = false;
		go = false;
		firstTurn2 = true;
		
		//Resets and/or recreates arrays (can be recreated because old ones are never needed again)
		for (int r = 0; r < 49; r++) {
			userButtons[r] = null; // Sets up array to store user board button squares 
			AIButtons[r] = null; // Sets up array to store AI board button squares
			AIShipGrid[r] = null;
		}

		for (int r = 0; r < 6; r++) {
			ships[r] = null;
		}
		surroundingShips  = new int [50] ;
		AIships = new int [3];
		userSpotsChosen = new int [49];
		AIsurroundingShips  = new int [75]; 
		AIspotsChosen = new int [300]; 
		AIship = new int [49];

//Calls on start to start the game  
		start();
		
	}
	
	//playerGameBoard method to create the player's game board
	public static  void playerGameBoard() { // Grace's method 
		int y = 0; // y int variable specific to this method 
		int u = 0; // u int variable specific to this method 
		int BNum; // BNum (button number) int variable specific to this method 
		String B; // String to convert BNum

		// Creates label "User Board"
		JLabel playerBoardLabel = new JLabel("User Board"); 
		playerBoardLabel.setHorizontalAlignment(SwingConstants.CENTER);
		playerBoardLabel.setBounds(81, 15, 282, 30);
		playerBoardLabel.setFont(new Font("Arial", Font.PLAIN, 30));
		panel.add(playerBoardLabel);

		panel.add(help);
		panel.add(restart);

		for (i=0; i<=48; i++) { // generates 49 buttons in an array

			BNum = i + 1; // Set up for adding the numbers 1-49 on the buttons
			B = Integer.toString(BNum); // Converts to string to add on button
			userButtons[i] = new JButton(B); // Creates new buttons and adds numbers
			userButtons[i].setBackground(new Color(2, 133, 247)); // Sets custom blue colour

			// Below is the algorithm that puts the buttons in the right positions
			u = u + 1; // Updates u 
			userButtons[i].setBounds (0 + (53 * u), 45 + y, 53, 53); 
			if (i == 6 || i == 13 ||i == 20 || i == 27 || i == 34 || i == 41) {
				y = y + 53;
				u = 0;
			}
			userButtons[i].setVisible(true);
			panel.add(userButtons[i]); // Adds each button 
		}

		for (i=0; i<=48; i++) { // starts for loop to add action listeners to each button 
			setupUserButtons(i); // Stores i throughout the entire action listener
		}

		panel.repaint();
		panel.revalidate();

	}

	//disabling surrounding buttons method - Amrin's method 
	//this method ensures that the user cannot place a ship directly beside another (they must be at least one spot away)
	public static void surroundingButtons (int position)
	{
		//when placing the first ship (3 hole horizontal)
		if (numOfShips ==1)
		{
			//if it is in the first row 
			if (position > -1 && position < 7)
			{
				//if it is in the 5th column
				if (position == 4)
				{
					//only disable the ones below it and to the left (it is in the first row so none above and in the 5th column so none on the right)
					userButtons [position + 7].setEnabled (false);
					userButtons [position + 8].setEnabled (false);
					userButtons [position + 9].setEnabled (false);
					userButtons [position - 1].setEnabled (false);

					//storing the buttons that a ships cannot be placed at 
					surroundingShips [length] = position - 1; 
					length = length + 1;
					surroundingShips [length] = position + 9; 
					length = length + 1;
					surroundingShips [length] = position + 8; 
					length = length + 1;
					surroundingShips [length] = position + 7; 
					length = length + 1;
				}
				//if it is in the 1st column
				else if (position == 0)
				{
					//only disable the buttons below it and to the right (it is in the first row so none above and in the 1st column so none on the left)
					userButtons [position + 7].setEnabled (false);
					userButtons [position + 8].setEnabled (false);
					userButtons [position + 9].setEnabled (false);
					userButtons [position + 3].setEnabled (false);

					//storing the buttons that a ships cannot be placed at 
					surroundingShips [length] = position + 3; 
					length = length + 1;
					surroundingShips [length] = position + 9; 
					length = length + 1;
					surroundingShips [length] = position + 8; 
					length = length + 1;
					surroundingShips [length] = position + 7; 
					length = length + 1;
				}
				//if it is only in the first row 
				else
				{
					//only disable the buttons below it and not above (there are none above)
					userButtons [position + 7].setEnabled (false);
					userButtons [position + 8].setEnabled (false);
					userButtons [position + 9].setEnabled (false);
					userButtons [position - 1].setEnabled (false);
					userButtons [position + 3].setEnabled (false);

					//storing the buttons that a ships cannot be placed at 
					surroundingShips [length] = position + 3; 
					length = length + 1;
					surroundingShips [length] = position -1; 
					length = length + 1;
					surroundingShips [length] = position + 9; 
					length = length + 1;
					surroundingShips [length] = position + 8; 
					length = length + 1;
					surroundingShips [length] = position + 7; 
					length = length + 1;
				}
			}
			//if it is in the last row
			else if (position >41 && position <51)
			{
				//if it is in the 5th column 
				if (position == 46)
				{
					//only disable the ones above it and to the left (it is in the last row so none below and in the 5th column so none on the right)
					userButtons [position - 7].setEnabled (false);
					userButtons [position - 6].setEnabled (false);
					userButtons [position - 5].setEnabled (false);
					userButtons [position - 1].setEnabled (false);

					//storing the buttons that a ships cannot be placed at 
					surroundingShips [length] = position - 1; 
					length = length + 1;
					surroundingShips [length] = position - 5; 
					length = length + 1;
					surroundingShips [length] = position - 6; 
					length = length + 1;
					surroundingShips [length] = position - 7; 
					length = length + 1;
				}
				//if it is in the 1st column 
				else if (position == 42)
				{
					//only disable the ones above it and to the right  (it is in the last row so none below and in the 1st column so none on the left)
					userButtons [position - 7].setEnabled (false);
					userButtons [position - 6].setEnabled (false);
					userButtons [position - 5].setEnabled (false);
					userButtons [position + 3].setEnabled (false);

					//storing the buttons that a ships cannot be placed at 
					surroundingShips [length] = position + 3; 
					length = length + 1;
					surroundingShips [length] = position - 7; 
					length = length + 1;
					surroundingShips [length] = position - 6; 
					length = length + 1;
					surroundingShips [length] = position - 5; 
					length = length + 1;
				}
				//if it is only in the last row
				else 
				{
					//only disable the buttons above it and now below (there are none below it)
					userButtons [position - 7].setEnabled (false);
					userButtons [position - 6].setEnabled (false);
					userButtons [position - 5].setEnabled (false);
					userButtons [position - 1].setEnabled (false);
					userButtons [position + 3].setEnabled (false);

					//storing the buttons that a ships cannot be placed at 
					surroundingShips [length] = position - 7; 
					length = length + 1;
					surroundingShips [length] = position - 6; 
					length = length + 1;
					surroundingShips [length] = position - 5; 
					length = length + 1;
					surroundingShips [length] = position - 1; 
					length = length + 1;
					surroundingShips [length] = position + 3; 
					length = length + 1;
				}
			}
			//if it is in the 5th column
			else if (position == 11 || position ==18 ||position ==25 || position ==32|| position ==39)
			{
				//don't disable the button on the right (there is no button on the right)
				userButtons [position - 7].setEnabled (false);
				userButtons [position - 6].setEnabled (false);
				userButtons [position - 5].setEnabled (false);
				userButtons [position - 1].setEnabled (false);
				userButtons [position + 7].setEnabled (false);
				userButtons [position + 8].setEnabled (false);
				userButtons [position + 9].setEnabled (false);

				//storing the buttons that a ships cannot be placed at 
				surroundingShips [length] = position - 7; 
				length = length + 1;
				surroundingShips [length] = position - 6; 
				length = length + 1;
				surroundingShips [length] = position - 5; 
				length = length + 1;
				surroundingShips [length] = position - 1; 
				length = length + 1;
				surroundingShips [length] = position + 7; 
				length = length + 1;
				surroundingShips [length] = position + 8; 
				length = length + 1;
				surroundingShips [length] = position + 9; 
				length = length + 1;
			}
			//if it is in the 1st column
			else if (position == 7 || position ==14 ||position ==21 || position ==28|| position ==35)
			{
				//don't disable the button on the left (there is no button on the left)
				userButtons [position - 7].setEnabled (false);
				userButtons [position - 6].setEnabled (false);
				userButtons [position - 5].setEnabled (false);
				userButtons [position + 3].setEnabled (false);
				userButtons [position + 7].setEnabled (false);
				userButtons [position + 8].setEnabled (false);
				userButtons [position + 9].setEnabled (false);

				//storing the buttons that a ships cannot be placed at 
				surroundingShips [length] = position - 7; 
				length = length + 1;
				surroundingShips [length] = position - 6; 
				length = length + 1;
				surroundingShips [length] = position - 5; 
				length = length + 1;
				surroundingShips [length] = position + 3; 
				length = length + 1;
				surroundingShips [length] = position + 7; 
				length = length + 1;
				surroundingShips [length] = position + 8; 
				length = length + 1;
				surroundingShips [length] = position + 9; 
				length = length + 1;
			}
			//if it is a button in the middle 
			else 
			{
				//disable all surrounding buttons on all sides
				userButtons [position + 7].setEnabled (false);
				userButtons [position + 8].setEnabled (false);
				userButtons [position + 9].setEnabled (false);
				userButtons [position - 7].setEnabled (false);
				userButtons [position - 6].setEnabled (false);
				userButtons [position - 5].setEnabled (false);
				userButtons [position - 1].setEnabled (false);
				userButtons [position + 3].setEnabled (false);

				//storing the buttons that a ships cannot be placed at 
				surroundingShips [length] = position - 7; 
				length = length + 1;
				surroundingShips [length] = position - 6; 
				length = length + 1;
				surroundingShips [length] = position - 5; 
				length = length + 1;
				surroundingShips [length] = position + 3; 
				length = length + 1;
				surroundingShips [length] = position + 7; 
				length = length + 1;
				surroundingShips [length] = position + 8; 
				length = length + 1;
				surroundingShips [length] = position + 9; 
				length = length + 1;
				surroundingShips [length] = position - 1; 
				length = length + 1;
			}
		}

		//if placing the second ship (2 hole horizontal ship)
		else  if (numOfShips ==2)
		{
			//if it is in the first row
			if (position > -1 && position < 7)		
			{
				//if it is in the 6th column 
				if (position == 5)
				{
					//only disable the ones to the left and below (none above and to the right)
					userButtons [position - 1].setEnabled (false);
					userButtons [position + 7].setEnabled (false);
					userButtons [position + 8].setEnabled (false);

					//storing the buttons that a ships cannot be placed at 
					surroundingShips [length] = position + 8; 
					length = length + 1;
					surroundingShips [length] = position + 7; 
					length = length + 1;
					surroundingShips [length] = position - 1; 
					length = length + 1;
				}
				//if it is in the 1st column
				else if (position == 0)
				{
					//only disable the ones below and to the right (none above and to the left)
					userButtons [position + 2].setEnabled (false);
					userButtons [position + 7].setEnabled (false);
					userButtons [position + 8].setEnabled (false);

					//storing the buttons that a ships cannot be placed at 
					surroundingShips [length] = position + 8; 
					length = length + 1;
					surroundingShips [length] = position + 7; 
					length = length + 1;
					surroundingShips [length] = position +2; 
					length = length + 1;
				}
				//if it is in the first row only 
				else 
				{
					//don't disable the ones above (there are none above)
					userButtons [position + 2].setEnabled (false);
					userButtons [position + 7].setEnabled (false);
					userButtons [position + 8].setEnabled (false);
					userButtons [position - 1].setEnabled (false);

					//storing the buttons that a ships cannot be placed at 
					surroundingShips [length] = position + 8; 
					length = length + 1;
					surroundingShips [length] = position + 7; 
					length = length + 1;
					surroundingShips [length] = position - 1; 
					length = length + 1;
					surroundingShips [length] = position +2; 
					length = length + 1;
				}	
			}
			//if it is in the last row 
			else if (position >41 && position <51)
			{
				//if it is in the first column 
				if (position == 42 )
				{
					//only disable the ones above it and to the right (none below and to the left)
					userButtons [position + 2].setEnabled (false);
					userButtons [position - 7].setEnabled (false);
					userButtons [position - 6].setEnabled (false);

					//storing the buttons that a ships cannot be placed at 
					surroundingShips [length] = position - 6; 
					length = length + 1;
					surroundingShips [length] = position - 7; 
					length = length + 1;
					surroundingShips [length] = position +2; 
					length = length + 1;

				}
				//if it is in the 6th column 
				else if (position ==47)
				{
					//only disable the ones above it and to the left (none below and to the right)
					userButtons [position - 1].setEnabled (false);
					userButtons [position - 7].setEnabled (false);
					userButtons [position - 6].setEnabled (false);

					//storing the buttons that a ships cannot be placed at 
					surroundingShips [length] = position - 6; 
					length = length + 1;
					surroundingShips [length] = position - 7; 
					length = length + 1;
					surroundingShips [length] = position - 1; 
					length = length + 1;
				}
				//if it is just in the last row 
				else
				{
					//don't disable the ones above (there are none above)
					userButtons [position + 2].setEnabled (false);
					userButtons [position - 7].setEnabled (false);
					userButtons [position - 6].setEnabled (false);
					userButtons [position - 1].setEnabled (false);

					//storing the buttons that a ships cannot be placed at 
					surroundingShips [length] = position - 6; 
					length = length + 1;
					surroundingShips [length] = position - 7; 
					length = length + 1;
					surroundingShips [length] = position +2; 
					length = length + 1;
					surroundingShips [length] = position - 1; 
					length = length + 1;
				}
			}
			//if it is in the 6th column
			else if (position ==12|| position ==19 || position ==26 ||position ==33 || position == 40)
			{
				//don't disable the button to the right (there isn't one to the right)
				userButtons [position + 7].setEnabled (false);
				userButtons [position + 8].setEnabled (false);
				userButtons [position - 7].setEnabled (false);
				userButtons [position - 6].setEnabled (false);
				userButtons [position - 1].setEnabled (false);

				//storing the buttons that a ships cannot be placed at 
				surroundingShips [length] = position - 6; 
				length = length + 1;
				surroundingShips [length] = position - 7; 
				length = length + 1;
				surroundingShips [length] = position + 7; 
				length = length + 1;
				surroundingShips [length] = position + 8; 
				length = length + 1;
				surroundingShips [length] = position - 1; 
				length = length + 1;
			}
			//if it is in the 1st column
			else if (position  == 7|| position ==14 ||position ==21 || position ==28|| position ==35)
			{
				//don't disable the button to the left (there isn't one to the left)
				userButtons [position + 7].setEnabled (false);
				userButtons [position + 8].setEnabled (false);
				userButtons [position - 7].setEnabled (false);
				userButtons [position - 6].setEnabled (false);
				userButtons [position + 2].setEnabled (false);

				//storing the buttons that a ships cannot be placed at 
				surroundingShips [length] = position - 6; 
				length = length + 1;
				surroundingShips [length] = position - 7; 
				length = length + 1;
				surroundingShips [length] = position + 7; 
				length = length + 1;
				surroundingShips [length] = position + 8; 
				length = length + 1;
				surroundingShips [length] = position + 2; 
				length = length + 1;
			}
			//if it is a button in the middle 
			else 
			{	
				//disable all surrounding buttons on all sides 
				userButtons [position + 7].setEnabled (false);
				userButtons [position + 8].setEnabled (false);
				userButtons [position - 7].setEnabled (false);
				userButtons [position - 6].setEnabled (false);
				userButtons [position - 1].setEnabled (false);
				userButtons [position + 2].setEnabled (false);

				//storing the buttons that a ships cannot be placed at 
				surroundingShips [length] = position - 6; 
				length = length + 1;
				surroundingShips [length] = position - 7; 
				length = length + 1;
				surroundingShips [length] = position + 7; 
				length = length + 1;
				surroundingShips [length] = position + 8; 
				length = length + 1;
				surroundingShips [length] = position - 1; 
				length = length + 1;
				surroundingShips [length] = position + 2; 
				length = length + 1;
			}
		}
	}

	//this method ensures that the AI does not place a boat directly beside another (must be placed at least one spot away)
	public static void AIsurroundingButtons (int position) // Amrins method (modified by grace for use by AI)
	{
		//when placing the first ship (3 hole horizontal)
		if (NumOfAIShips == 1)
		{
			//if it is in the first row 
			if (position > -1 && position < 7)
			{
				//if it is in the 5th column
				if (position == 4)
				{
					//storing the buttons that a ships cannot be placed at 
					AIsurroundingShips [length2] = position - 1; 
					length2 = length2 + 1;
					AIsurroundingShips [length2] = position + 9; 
					length2 = length2 + 1;
					AIsurroundingShips [length2] = position + 8; 
					length2 = length2 + 1;
					AIsurroundingShips [length2] = position + 7; 
					length2 = length2 + 1;
				}
				//if it is in the 1st column
				else if (position == 0)
				{
					//storing the buttons that a ships cannot be placed at 
					AIsurroundingShips [length2] = position + 3; 
					length2 = length2 + 1;
					AIsurroundingShips [length2] = position + 9; 
					length2 = length2 + 1;
					AIsurroundingShips [length2] = position + 8; 
					length2 = length2 + 1;
					AIsurroundingShips [length2] = position + 7; 
					length2 = length2 + 1;
				}
				//if it is only in the first row 
				else
				{
					//storing the buttons that a ships cannot be placed at 
					AIsurroundingShips [length2] = position + 3; 
					length2 = length2 + 1;
					AIsurroundingShips [length2] = position -1; 
					length2 = length2 + 1;
					AIsurroundingShips [length2] = position + 9; 
					length2 = length2 + 1;
					AIsurroundingShips [length2] = position + 8; 
					length2 = length2 + 1;
					AIsurroundingShips [length2] = position + 7; 
					length2 = length2 + 1;
				}
			}
			//if it is in the last row
			else if (position >41 && position <51)
			{
				//if it is in the 5th column 
				if (position == 46)
				{
					//storing the buttons that a ships cannot be placed at 
					AIsurroundingShips [length2] = position - 1; 
					length2 = length2 + 1;
					AIsurroundingShips [length2] = position - 5; 
					length2 = length2 + 1;
					AIsurroundingShips [length2] = position - 6; 
					length2 = length2 + 1;
					AIsurroundingShips [length2] = position - 7; 
					length2 = length2 + 1;
				}
				//if it is in the 1st column 
				else if (position == 42)
				{
					//storing the buttons that a ships cannot be placed at 
					AIsurroundingShips [length2] = position + 3; 
					length2 = length2 + 1;
					AIsurroundingShips [length2] = position - 7; 
					length2 = length2 + 1;
					AIsurroundingShips [length2] = position - 6; 
					length2 = length2 + 1;
					AIsurroundingShips [length2] = position - 5; 
					length2 = length2 + 1;
				}
				//if it is only in the last row
				else 
				{
					//storing the buttons that a ships cannot be placed at 
					AIsurroundingShips [length2] = position - 7; 
					length2 = length2 + 1;
					AIsurroundingShips [length2] = position - 6; 
					length2 = length2 + 1;
					AIsurroundingShips [length2] = position - 5; 
					length2 = length2 + 1;
					AIsurroundingShips [length2] = position - 1; 
					length2 = length2 + 1;
					AIsurroundingShips [length2] = position + 3; 
					length2 = length2 + 1;
				}

			}
			//if it is in the 5th column
			else if (position == 11 || position ==18 ||position ==25 || position ==32|| position ==39)
			{
				//storing the buttons that a ships cannot be placed at 
				AIsurroundingShips [length2] = position - 7; 
				length2 = length2 + 1;
				AIsurroundingShips [length2] = position - 6; 
				length2 = length2 + 1;
				AIsurroundingShips [length2] = position - 5; 
				length2 = length2 + 1;
				AIsurroundingShips [length2] = position - 1; 
				length2 = length2 + 1;
				AIsurroundingShips [length2] = position + 7; 
				length2 = length2 + 1;
				AIsurroundingShips [length2] = position + 8; 
				length2 = length2 + 1;
				AIsurroundingShips [length2] = position + 9; 
				length2 = length2 + 1;
			}
			//if it is in the 1st column
			else if (position == 7 || position ==14 ||position ==21 || position ==28|| position ==35)
			{
				//storing the buttons that a ships cannot be placed at 
				AIsurroundingShips [length2] = position - 7; 
				length2 = length2 + 1;
				AIsurroundingShips [length2] = position - 6; 
				length2 = length2 + 1;
				AIsurroundingShips [length2] = position - 5; 
				length2 = length2 + 1;
				AIsurroundingShips [length2] = position + 3; 
				length2 = length2 + 1;
				AIsurroundingShips [length2] = position + 7; 
				length2 = length2 + 1;
				AIsurroundingShips [length2] = position + 8; 
				length2 = length2 + 1;
				AIsurroundingShips [length2] = position + 9; 
				length2 = length2 + 1;
			}
			//if it is a button in the middle 
			else 
			{
				//storing the buttons that a ships cannot be placed at 
				AIsurroundingShips [length2] = position - 7; 
				length2 = length2 + 1;
				AIsurroundingShips [length2] = position - 6; 
				length2 = length2 + 1;
				AIsurroundingShips [length2] = position - 5; 
				length2 = length2 + 1;
				AIsurroundingShips [length2] = position + 3; 
				length2 = length2 + 1;
				AIsurroundingShips [length2] = position + 7; 
				length2 = length2 + 1;
				AIsurroundingShips [length2] = position + 8; 
				length2 = length2 + 1;
				AIsurroundingShips [length2] = position + 9; 
				length2 = length2 + 1;
				AIsurroundingShips [length2] = position - 1; 
				length2 = length2 + 1;
			}
		}

		//if placing the second ship (2 hole horizontal ship)
		else  if (NumOfAIShips ==2)
		{
			//if it is in the first row
			if (position > -1 && position < 7)		
			{
				//if it is in the 6th column 
				if (position == 5)
				{
					//storing the buttons that a ships cannot be placed at 
					AIsurroundingShips [length2] = position + 8; 
					length2 = length2 + 1;
					AIsurroundingShips [length2] = position + 7; 
					length2 = length2 + 1;
					AIsurroundingShips [length2] = position - 1; 
					length2 = length2 + 1;
				}
				//if it is in the 1st column
				else if (position == 0)
				{
					//storing the buttons that a ships cannot be placed at 
					AIsurroundingShips [length2] = position + 8; 
					length2 = length2 + 1;
					AIsurroundingShips [length2] = position + 7; 
					length2 = length2 + 1;
					AIsurroundingShips [length2] = position +2; 
					length2 = length2 + 1;
				}
				//if it is in the first row only 
				else 
				{
					//storing the buttons that a ships cannot be placed at 
					AIsurroundingShips [length2] = position + 8; 
					length2 = length2 + 1;
					AIsurroundingShips [length2] = position + 7; 
					length2 = length2 + 1;
					AIsurroundingShips [length2] = position - 1; 
					length2 = length2 + 1;
					AIsurroundingShips [length2] = position +2; 
					length2 = length2 + 1;
				}	
			}
			//if it is in the last row 
			else if (position >41 && position <51)
			{
				//if it is in the first column 
				if (position == 42 )
				{
					//storing the buttons that a ships cannot be placed at 
					AIsurroundingShips [length2] = position - 6; 
					length2 = length2 + 1;
					AIsurroundingShips [length2] = position - 7; 
					length2 = length2 + 1;
					AIsurroundingShips [length2] = position +2; 
					length2 = length2 + 1;
				}
				//if it is in the 6th column 
				else if (position ==47)
				{
					//storing the buttons that a ships cannot be placed at 
					AIsurroundingShips [length2] = position - 6; 
					length2 = length2 + 1;
					AIsurroundingShips [length2] = position - 7; 
					length2 = length2 + 1;
					AIsurroundingShips [length2] = position - 1; 
					length2 = length2 + 1;
				}
				//if it is just in the last row 
				else
				{
					//storing the buttons that a ships cannot be placed at 
					AIsurroundingShips [length2] = position - 6; 
					length2 = length2 + 1;
					AIsurroundingShips [length2] = position - 7; 
					length2 = length2 + 1;
					AIsurroundingShips [length2] = position +2; 
					length2 = length2 + 1;
					AIsurroundingShips [length2] = position - 1; 
					length2 = length2 + 1;
				}
			}
			//if it is in the 6th column
			else if (position ==12|| position ==19 || position ==26 ||position ==33 || position == 40)
			{
				//storing the buttons that a ships cannot be placed at 
				AIsurroundingShips [length2] = position - 6; 
				length2 = length2 + 1;
				AIsurroundingShips [length2] = position - 7; 
				length2 = length2 + 1;
				AIsurroundingShips [length2] = position + 7; 
				length2 = length2 + 1;
				AIsurroundingShips [length2] = position + 8; 
				length2 = length2 + 1;
				AIsurroundingShips [length2] = position - 1; 
				length2 = length2 + 1;
			}
			//if it is in the 1st column
			else if (position  == 7|| position ==14 ||position ==21 || position ==28|| position ==35)
			{
				//storing the buttons that a ships cannot be placed at 
				AIsurroundingShips [length2] = position - 6; 
				length2 = length2 + 1;
				AIsurroundingShips [length2] = position - 7; 
				length2 = length2 + 1;
				AIsurroundingShips [length2] = position + 7; 
				length2 = length2 + 1;
				AIsurroundingShips [length2] = position + 8; 
				length2 = length2 + 1;
				AIsurroundingShips [length2] = position + 2; 
				length2 = length2 + 1;
			}
			//if it is a button in the middle 
			else 
			{		
				//storing the buttons that a ships cannot be placed at 
				AIsurroundingShips [length2] = position - 6; 
				length2 = length2 + 1;
				AIsurroundingShips [length2] = position - 7; 
				length2 = length2 + 1;
				AIsurroundingShips [length2] = position + 7; 
				length2 = length2 + 1;
				AIsurroundingShips [length2] = position + 8; 
				length2 = length2 + 1;
				AIsurroundingShips [length2] = position - 1; 
				length2 = length2 + 1;
				AIsurroundingShips [length2] = position + 2; 
				length2 = length2 + 1;
			}
		}
		//Added by grace
		//making sure the next ship isn't placed on top of the first 
		AIsurroundingShips[length2] = position;
		length2 = length2 + 1;
		AIsurroundingShips[length2] = position+1;
		length2 = length2 + 1;
		AIsurroundingShips[length2] = position+2;
		length2 = length2 + 1;
	}

	//method to set up each of the user buttons and bulk of game code 
	public static void setupUserButtons (int i) { // Adds action listen to each button and performs bulk of game code
		//action listener for user buttons 
		userButtons[i].addActionListener(new ActionListener()
		{
			public void actionPerformed (ActionEvent e)
			{
				//if no ships have been placed yet (the first ship is being placed)
				if (numOfShips == 0){

					position = i;
					
					//if it is placed off the board
					if(position == 5 || position == 6||position == 13||position == 12||position ==19||position == 20||position == 26||position == 27||position == 33||position == 34||position == 40||position == 41||position == 47||position == 48) { // NIVAETHAN 
						//prompt the user to replace the ship
						offBoardLabel.setVisible(true);
						numOfShips = 0;
					}

					//if it is placed in an acceptable spot 
					else {
						//making the buttons that the ship was placed at green and pink on the respective AI and user boards
						for (int x = 0; x < 3; x++) { // GRACE
							AIButtons[position+x].setBackground(Color.pink);
							userButtons[position+x].setBackground(Color.green);
							userButtons[position+x].setEnabled(false);
							numOfShips = 1;//one ship has been placed
						}
						surroundingButtons (position);//calling on method to store the surrounding buttons so a ship cannot be placed directly beside it 
						ships[0] = new Ships(3, 0, position, false); // Stores ship details in array of records (size, hit, position, sunk)
						offBoardLabel.setVisible(false);
						
						//prompting user to place their second ship
						ship1Label.setText("Place your 2 horizontal ship");
						ship1Label.setVisible(true);
					}


					panel.repaint();
					panel.revalidate();
				}

				//if the first ship has already been placed 
				else if (numOfShips == 1){

					position = i;

					//if it is placed off the board
					if(position == 6||position == 13||position == 20||position == 27||position == 34|| position == 41||position == 48) { // NIVAETHAN 
						//prompt the user to replace the ship
						numOfShips = 1; 
						offBoardLabel.setVisible(true);
						unable = true; 
					}

					//if it is placed in an acceptable spot 
					else {
					
						for (int x = 0; x < 2; x++) {
							//for loop  to check if a ship is placed too close to another
							for (int count = 0; count <length; count ++)//AMRIN
							{
								//checking if the ship cuts into a disabled button
								if (surroundingShips [count] == (position +x)) {
									//telling the user the spot is unavailable 
									unavailable.setVisible(true);
									numOfShips = 1;//resetting number of ships because one wasn't placed
									unable = true; //telling the system the user can't place a ship there
									count = length; //ending the for loop
								}
								//if a ship can be placed there tell the system it can be
								else 
								{
									unable = false; 
								}
							}
						}
						//if a ship can be placed there store the values 
						if (unable == false)
						{
							//making the buttons that the ship was placed at green and pink on the respective AI and user boards
							for (int x = 0; x < 2; x++) { 
								unavailable.setVisible(false);
								AIButtons[position+x].setBackground(Color.pink);
								userButtons[position+x].setBackground(Color.green);
								userButtons[position+x].setEnabled(false);
								numOfShips = 2;//two ships have been placed
							}

							surroundingButtons (position);//calling on the surrounding buttons method to make the surrounding buttons unable to be clicked 
							ships[1] = new Ships(2, 0, position, false); // Stores ship details in array of records (size, hit, position, sunk)
							offBoardLabel.setVisible(false);
							
							//prompting the user to place their third ship 
							ship1Label.setText("Place your 3 vertical ship");
							ship1Label.setVisible(true);
						}

						panel.repaint();
						panel.revalidate();
					}
				}

				//if the second ship has already been placed 
				else if (numOfShips == 2){

					position = i;

					//if the  ship is placed off the board
					if(position == 35 || position == 36||position == 37||position == 38||position ==39||position == 40||position == 41||position == 42||position == 43||position == 44||position == 45||position == 46||position == 47||position == 48) { // NIVAETHAN 

						//prompting the user to choose a new spot 
						numOfShips = 2;
						offBoardLabel.setVisible(true);
						unable = true ;
					}
					
					//if it is placed in an acceptable spot 
					else 
					{
						for (int x = 0; x < 3; x++) { 
							//for loop  to check if a ship is placed too close to another
							for (int counter = 0; counter <length; counter ++)//AMRIN
							{
								//checking if the ship cuts into a disabled button
								if (surroundingShips [counter] == position+(7*x))
								{
									//telling the user the spot is unavailable 
									unavailable.setVisible(true);
									numOfShips = 2;//resetting number of ships because one wasn't placed
									unable = true; //telling the system the user can't place a ship there
									counter = length; //ending the for loop
									x = 3; //ending the first for loop and exiting 
								}
								//if a ship can be placed there tell the system it can be
								else 
								{
									unable = false; 
								}
							}
						}
					}
					//if a ship can be placed there store the values 
					if (unable == false)
					{
						//if it is the first turn of the game 
						if (firstTurn2 == true) {
							firstTurn.setVisible(true);//prompt the user to choose their first spot to hit 
							firstTurn2 = false;
							}

						//making the buttons that the ship was placed at green and pink on the respective AI and user boards
						for (int x = 0; x < 3; x++) { 
							unavailable.setVisible (false);
							AIButtons[position + (7*x)].setBackground(Color.pink);
							userButtons[position+(7*x)].setBackground(Color.green);
							userButtons[position+(7*x)].setEnabled(false);
							numOfShips = 3;//three ships have been placed
						}

						offBoardLabel.setVisible(false);
						ships[2] = new Ships(3, 0, position, false); // Stores ship details in array of records (size, hit, position, sunk)

						ship1Label.setVisible(false);

						for (int r=0; r<=48; r++) { // Resets the game board so that it's clean for turn logic (GRACE)
							userButtons[r].setEnabled(true); 
							userButtons[r].setBackground(new Color(2, 133, 247));

						}
					}
					panel.repaint();
					panel.revalidate();
				}

				//if the ships have been placed already start the rest of the game (choosing a spot to hit)
				else {
						firstTurn.setVisible(false);
					
					position = i;
					turnLogic();//calling on turn logic method to determine whose turn it is and what to do on each turn 

				}
			}
		});}

	//method to set up AI game board
	public static void AIGameBoard() { // Grace's method 
		int y = 0; // y int variable specific to this method 
		int u = 0; // u int variable specific to this method 
		int BNum; // BNum (button number) int variable specific to this method 
		String B; // String to convert BNum

		JLabel AIBoardLabel = new JLabel("AI Board"); 
		AIBoardLabel.setHorizontalAlignment(SwingConstants.CENTER);
		AIBoardLabel.setBounds(425, 195, 282, 30);
		AIBoardLabel.setFont(new Font("Arial", Font.PLAIN, 15));
		panel.add(AIBoardLabel);

		for (t=0; t<=48; t++) { // generates 49 buttons in an array
			BNum = t + 1; // Set up for adding the numbers 1-49 on the buttons
			B = Integer.toString(BNum); // Converts to string to add on button
			AIButtons[t] = new JButton(); // Creates new buttons  and adds numbers
			AIButtons[t].setFont(new Font("Arial", Font.PLAIN, 20));
			AIButtons[t].setBackground(new Color(125, 192, 250)); // Custom background color

			// Below is the algorithm that puts the buttons in the right positions
			u = u + 1;
			AIButtons[t].setBounds (450 + (25 * u), 20 + y, 25, 25);
			if (t == 6 || t == 13 ||t == 20 || t == 27 || t == 34 || t == 41) {
				y = y + 25;
				u = 0;
			}

			panel.add(AIButtons[t]); // Adds each button 
		}

		panel.repaint();
		panel.revalidate();

	}

	//method to create AI game board
	public static void AIGameBoardShips() { // Grace's method 
		int y = 0; // y int variable specific to this method 
		int u = 0; // u int variable specific to this method 
		String B, w;
		int BNum; // BNum (button number) int variable specific to this method 

		for (int k=0; k<=48; k++) { // generates 49 buttons in an array
			BNum = k + 1; // Set up for adding the numbers 1-49 on the buttons
			B = Integer.toString(BNum); // Converts to string to add on button
			w = Integer.toString(k); // Converts to string to add on button
			AIShipGrid[k] = new JButton(B); // Creates new buttons and adds numbers
			AIShipGrid[k].setBackground(new Color(125, 192, 250));

			// Below is the algorithm that puts the buttons in the right positions
			u = u + 1;
			AIShipGrid[k].setBounds (10 + (50 * u), 20 + y, 50, 50);
			if (k == 6 || k == 13 ||k == 20 || k == 27 || k == 34 || k == 41) {
				y = y + 50;
				u = 0;
			}
			AIShipGrid[k].setEnabled(false);

			panel2.add(AIShipGrid[k]); // Adds each button 

		}
		frame2.setVisible(false);
		panel.repaint();
		panel.revalidate();
	}

	//method to determine who's turn it is & for hit/ miss detection
	public static void turnLogic ()//Amrin's method
	{
		UserHitDetection (position); //call on user hit detection method to check if the spot the user chose was a hit 
		
		//if it's the user's turn 
		if (turn == true)
		{
			endTurnButton.setEnabled (false);
			//if a ship was hit 
			if (hit == true)
			{
				endTurn.setText ("You hit player 2's ship. It's your turn again");
				endTurn.setVisible(true);
				turn = true; //user's turn again
			}
			//if a ship was missed
			else if (hit == false)
			{
				endTurn.setText ("You missed Player 2's ship. Click here to end your turn");
				endTurn.setVisible (true);
				endTurnButton.setVisible (true);//when end turn is clicked, the AI takes its turn 
				endTurnButton.setEnabled (true);
				turn = false;//AI's turn
				//for loop to make all buttons unable to be clicked
				for (i=0; i<=48; i++) 
				{ 
					userButtons[i].setEnabled(false);
				}
			}
		}
	}

	//Method for the AI to generate where their ships are generated 
	public static void AIShipPlacement() {

		//while all the ships have not already been placed 
		while (NumOfAIShips < 3) {
			unable = false;
			newRand();//calling on method to get a new random number 

			//if no ships have been placed 
			if (NumOfAIShips == 0){

				//if the ship is placed off the board 
				if(position == 5 || position == 6||position == 13||position == 12||position ==19||position == 20||position == 26||position == 27||position == 33||position == 34||position == 40||position == 41||position == 47||position == 48) { // NIVAETHAN 
					NumOfAIShips = 0;// no ships have been placed 
				}

				//if the ship is placed in an acceptable spot 
				else {
					//for loop to set the colour fo the buttons the ship is placed on the AI game board
					for (int x = 0; x < 3; x++) { 

						AIShipGrid[position+x].setBackground(Color.pink);
						NumOfAIShips = 1;//one ship has been placed 
					}
					AIsurroundingButtons (position);//calling on method to make sure the AI cannot place a ship in the surrounding area
					AIships[NumOfAIShips-1] = position;
					ships[3] = new Ships(3, 0, position, false); // Stores ship details in array of records (size, hit, position, sunk)
				}
				panel.repaint();
				panel.revalidate();

			}
			//if one ship has been placed 
			else if (NumOfAIShips == 1){

				//if it is placed off the board
				if(position == 6||position == 13||position == 20||position == 27||position == 34|| position == 41||position == 48) { 

					NumOfAIShips = 1; 
					unable = true; //telling the system a ship can't be placed there
				}
				//if it is placed in an acceptable spot 
				else {
					for (int x = 0; x < 2; x++) { 
						//for loop  to check if a ship is placed too close to another
						for (int count = 0; count <length2; count ++)//AMRIN
						{
							//checking if the ship cuts into a disabled button
							if (AIsurroundingShips [count] == (position +x) || AIsurroundingShips [count] == (position)) {
								unable = true; //telling the system the user can't place a ship there
								count = length2; //ending the for loop
								NumOfAIShips = 1;//resetting number of ships because one wasn't placed
							}
							//if a ship can be placed there tell the system it can be
							else 
							{
								unable = false; 
							}
						}
					}
					//if a ship can be placed there store the values 
					if (unable == false)
					{
						//for loop to change the button colours of where the ship was placed
						for (int x = 0; x < 2; x++) { 
							AIShipGrid[position+x].setBackground(Color.pink);
							NumOfAIShips = 2;//two ships have been placed
						}

						AIsurroundingButtons (position);//calling on the surrounding buttons method to make the surrounding buttons unable to be clicked
						AIships[NumOfAIShips-1] = position;
						ships[4] = new Ships(2, 0, position, false); // Stores ship details in array of records (size, hit, position, sunk)
					}
					panel.repaint();
					panel.revalidate();
				}
			}

			//if two chips have been placed already 
			else if (NumOfAIShips == 2){

				//if it is placed off the board
				if(position == 35 || position == 36||position == 37||position == 38||position ==39||position == 40||position == 41||position == 42||position == 43||position == 44||position == 45||position == 46||position == 47||position == 48) { // NIVAETHAN 

					NumOfAIShips = 2;
					unable = true ;//telling the system a ship can't be placed there 
				}
				else 
				{
					for (int x = 0; x < 3; x++) { 
						//for loop  to check if a ship is placed too close to another
						for (int counter = 0; counter <length2; counter ++)//AMRIN
						{
							//checking if the ship cuts into a disabled button
							if (AIsurroundingShips [counter] == position+(7*x) || (AIsurroundingShips [counter] == position))
							{
								unable = true; //telling the system the user can't place a ship there
								counter = length2; //ending the for loop
								x = 3; //ending the first for loop and exiting 
								NumOfAIShips = 2;//resetting number of ships because one wasn't placed
							}
							//if a ship can be placed there tell the system it can be
							else 
							{
								unable = false; 
							}
						}
					}
				}
				//if a ship can be placed there store the values 
				if (unable == false)
				{
					//for  loop to change the button  colours where the ship was placed 
					for (int x = 0; x < 3; x++) { 

						AIShipGrid[position + (7*x)].setBackground(Color.pink);

						NumOfAIShips = 3;
					}
					AIsurroundingButtons (position);//storing the places a ship can't be placed at
					AIships[NumOfAIShips-1] = position;
					ships[5] = new Ships(3, 0, position, false); // Stores ship details in array of records (size, hit, position, sunk)

				}
				panel.repaint();
				panel.revalidate();
			}
		}
	}

	//method to get a new random number between 1 and 49
	public static void newRand() {
		int limit, RNum; // Assigns method specific variables 
		Random rand = new Random(); // Sets up random number generator 
		limit = 49;      // Set number generation top limit (0-48)
		RNum = rand.nextInt(limit);
		position = RNum; // Assigns random number to position 
	}

	//Amrin's method - checking if a ship was hit along with full ship and all ships hit detection (for the AI)
	public static void AIhitDetection (int position)
	{ 
		randCheck = false;
		//checking if the spot was already chosen 
		while (randCheck == false){
			//if this is not the first spot being chosen 
			if (spotsChosen != 0) {
				//for loop to check through an array of all spots already chosen 
				for (int a = 0; a < spotsChosen; a ++){

					//if the spot chosen is in the last spot of the array 
					if (position == AIspotsChosen[spotsChosen - 1]) {

						//if it was trying to hit to the right
						if (RNum == 3) {
							four = true;//hit to the left instead
							position = originalPosition - 1;//giving it a new position to hit to the left
							//if it it the 3 horizontal boat, restore original position (so it hits from the new original in the correct direction next time)
							if (originalPosition == ships[0].getPosition() || originalPosition == ships[0].getPosition() + 1 || originalPosition == ships[0].getPosition() +2) {
								originalPosition = position;
							}
						}
						
						//if it was trying to hit to the left
						else if (RNum == 4) {
							three = true;//hit to the right instead
							position = originalPosition + 1;//giving it a new position to hit to the right 
							//if it it the 3 horizontal boat, restore original position (so it hits from the new original in the correct direction next time)
							if (originalPosition == ships[0].getPosition() || originalPosition == ships[0].getPosition() + 1 || originalPosition == ships[0].getPosition() +2) {
								originalPosition = position;
							}
						}
						
						//if it was trying to hit down 
						else if (RNum == 1) {
							RNum = 2;//hit up instead
							//resetting variables 
							one = false;
							two = true;
							f = true;//f = true to go into new logic and hit from previous position only 
							position = originalPosition - 7;//giving it a new position to hit up
							previousPosition = position; //restoring previous position
						}

						//if it was trying to hit up 
						else if (RNum == 2) {
							RNum = 1;//hit down instead
							//resetting variables 
							two = false;
							one = true;
							f = true;//f = true to go into new logic and hit from previous position only 
							position = originalPosition + 7;//giving it a new position to hit down
							previousPosition = position;//restoring previous position
						}
						//if it was none of these get a new random number 
						else {
							newRand();
						}
						randCheck = false;	//check again to see if the new number has not already been chosen 					
					}

					//if the spot was not previously chosen 
					if (a == (spotsChosen -1)) {
						randCheck = true;//exit the loop
					}
					
					//if the spot chosen was selected previously 
					else if (position == AIspotsChosen[a]) {
						
						//if it was trying to hit to the right 
						if (RNum == 3) {
							four = true;//hit left instead
							position = originalPosition - 1;//new position to hit left with 
							//if it is the 3 horizontal boat restore original position
							if (originalPosition == ships[0].getPosition() || originalPosition == ships[0].getPosition() + 1 || originalPosition == ships[0].getPosition() +2) {
								originalPosition = position;
							}
						}

						//if it was trying to hit to the left
						else if (RNum == 4) {
							three = true;//hit to the right instead
							position = originalPosition + 1;//new position to hit right with 
							//if it is the 3 horizontal boat restore original position
							if (originalPosition == ships[0].getPosition() || originalPosition == ships[0].getPosition() + 1 || originalPosition == ships[0].getPosition() +2) {
								originalPosition = position;
							}
						}
						
						//if it was trying to hit down 
						else if (RNum == 1) {
							RNum = 2;//hit up instead
							//resetting variables 
							one = false;
							two = true;
							f = true;//f = true to go into new logic and hit from previous position only 
							position = originalPosition - 7;//new position to hit up with 
							previousPosition = position;//storing new previous position
						}

						//if it was trying to hit up
						else if (RNum == 2) {
							RNum = 1;//hit down instead 
							//resetting variables 
							two = false;
							one = true;
							f = true;//f = true to go into new logic and hit from previous position only 
							position = originalPosition + 7;//new position to hit down with 
							previousPosition = position;//storing new previous position
						}
						
						//if none of the above, generate a new random number 
						else {
							newRand();
						}

						randCheck = false;//check through the loop again to make sure the new position has not already been chosen 
					}
				}
			}
			//if it was not found in the array (a spot was not already chosen) exit the loop
			else {
				randCheck = true;
			}
		}

		//storing a spot that the AI already chose so it can't be chosen again
		AIspotsChosen [spotsChosen] = position; 
		spotsChosen = spotsChosen + 1; 

		//if it hit the first boat
		if (position == ships [0].getPosition () || (position ==(ships [0].getPosition ()) + 1) || (position == (ships [0].getPosition ()) + 2))
		{
			//if the full boat hasn't been hit already
			if (ship1 != 0)
			{
				previousPosition = position;
				AIButtons[position].setBackground(Color.red);//changing the colour so the user can see a shipt was hit
				ship1 = ship1 - 1; //a spot on boat 1 was hit
				hit = true; //it was hit
				miss = false;

			}
			//if the full boat has been hit
			if (ship1 == 0)
			{
				//resetting all variables
				one = false;
				two = false;
				three = false;
				four = false;
				previousPosition = -1;
				originalPosition = -1;
				RNum = 0;
				miss = false;

				//storing all the surrounding positions of the boat hit so the AI doesn't hit them (making the AI smarter since it knows a ship can't be placed directly beside another)
				//if it is in the first row 
				if ((ships [0].getPosition () ) > -1 && (ships [0].getPosition () ) < 7)
				{
					//if it is in the 5th column
					if ((ships [0].getPosition () ) == 4)
					{
						//storing the buttons that a ships cannot be placed at 
						AIspotsChosen [spotsChosen] = (ships [0].getPosition () ) - 1; 
						spotsChosen = spotsChosen + 1;
						AIspotsChosen [spotsChosen] = (ships [0].getPosition () ) + 9; 
						spotsChosen = spotsChosen + 1;
						AIspotsChosen [spotsChosen] = (ships [0].getPosition () ) + 8; 
						spotsChosen = spotsChosen + 1;
						AIspotsChosen [spotsChosen] = (ships [0].getPosition () ) + 7; 
						spotsChosen = spotsChosen + 1;
					}
					//if it is in the 1st column
					else if ((ships [0].getPosition () ) == 0)
					{
						//storing the buttons that a ships cannot be placed at 
						AIspotsChosen [spotsChosen] = (ships [0].getPosition () ) + 3; 
						spotsChosen = spotsChosen + 1;
						AIspotsChosen [spotsChosen] = (ships [0].getPosition () ) + 9; 
						spotsChosen = spotsChosen + 1;
						AIspotsChosen [spotsChosen] = (ships [0].getPosition () ) + 8; 
						spotsChosen = spotsChosen + 1;
						AIspotsChosen [spotsChosen] = (ships [0].getPosition () ) + 7; 
						spotsChosen = spotsChosen + 1;
					}
					//if it is only in the first row 
					else
					{
						//storing the buttons that a ships cannot be placed at 
						AIspotsChosen [spotsChosen] = (ships [0].getPosition () ) + 3; 
						spotsChosen = spotsChosen + 1;
						AIspotsChosen [spotsChosen] = (ships [0].getPosition () ) -1; 
						spotsChosen = spotsChosen + 1;
						AIspotsChosen [spotsChosen] = (ships [0].getPosition () ) + 9; 
						spotsChosen = spotsChosen + 1;
						AIspotsChosen [spotsChosen] = (ships [0].getPosition () ) + 8; 
						spotsChosen = spotsChosen + 1;
						AIspotsChosen [spotsChosen] = (ships [0].getPosition () ) + 7; 
						spotsChosen = spotsChosen + 1;
					}
				}
				//if it is in the last row
				else if ((ships [0].getPosition () ) >41 && (ships [0].getPosition () ) <51)
				{
					//if it is in the 5th column 
					if ((ships [0].getPosition () ) == 46)
					{
						//storing the buttons that a ships cannot be placed at 
						AIspotsChosen [spotsChosen] = (ships [0].getPosition () ) - 1; 
						spotsChosen = spotsChosen + 1;
						AIspotsChosen [spotsChosen] = (ships [0].getPosition () ) - 5; 
						spotsChosen = spotsChosen + 1;
						AIspotsChosen [spotsChosen] = (ships [0].getPosition () ) - 6; 
						spotsChosen = spotsChosen + 1;
						AIspotsChosen [spotsChosen] = (ships [0].getPosition () ) - 7; 
						spotsChosen = spotsChosen + 1;
					}
					//if it is in the 1st column 
					else if ((ships [0].getPosition () ) == 42)
					{
						//storing the buttons that a ships cannot be placed at 
						AIspotsChosen [spotsChosen] = (ships [0].getPosition () ) + 3; 
						spotsChosen = spotsChosen + 1;
						AIspotsChosen [spotsChosen] = (ships [0].getPosition () ) - 7; 
						spotsChosen = spotsChosen + 1;
						AIspotsChosen [spotsChosen] = (ships [0].getPosition () ) - 6; 
						spotsChosen = spotsChosen + 1;
						AIspotsChosen [spotsChosen] = (ships [0].getPosition () ) - 5; 
						spotsChosen = spotsChosen + 1;
					}
					//if it is only in the last row
					else 
					{
						//storing the buttons that a ships cannot be placed at 
						AIspotsChosen [spotsChosen] = (ships [0].getPosition () ) - 7; 
						spotsChosen = spotsChosen + 1;
						AIspotsChosen [spotsChosen] = (ships [0].getPosition () ) - 6; 
						spotsChosen = spotsChosen + 1;
						AIspotsChosen [spotsChosen] = (ships [0].getPosition () ) - 5; 
						spotsChosen = spotsChosen + 1;
						AIspotsChosen [spotsChosen] = (ships [0].getPosition () ) - 1; 
						spotsChosen = spotsChosen + 1;
						AIspotsChosen [spotsChosen] = (ships [0].getPosition () ) + 3; 
						spotsChosen = spotsChosen + 1;
					}

				}
				//if it is in the 5th column
				else if ((ships [0].getPosition () ) == 11 || (ships [0].getPosition () ) ==18 ||(ships [0].getPosition () ) ==25 || (ships [0].getPosition () ) ==32|| (ships [0].getPosition () ) ==39)
				{
					//storing the buttons that a ships cannot be placed at 
					AIspotsChosen [spotsChosen] = (ships [0].getPosition () ) - 7; 
					spotsChosen = spotsChosen + 1;
					AIspotsChosen [spotsChosen] = (ships [0].getPosition () ) - 6; 
					spotsChosen = spotsChosen + 1;
					AIspotsChosen [spotsChosen] = (ships [0].getPosition () ) - 5; 
					spotsChosen = spotsChosen + 1;
					AIspotsChosen [spotsChosen] = (ships [0].getPosition () ) - 1; 
					spotsChosen = spotsChosen + 1;
					AIspotsChosen [spotsChosen] = (ships [0].getPosition () ) + 7; 
					spotsChosen = spotsChosen + 1;
					AIspotsChosen [spotsChosen] = (ships [0].getPosition () ) + 8; 
					spotsChosen = spotsChosen + 1;
					AIspotsChosen [spotsChosen] = (ships [0].getPosition () ) + 9; 
					spotsChosen = spotsChosen + 1;
				}
				//if it is in the 1st column
				else if ((ships [0].getPosition () ) == 7 || (ships [0].getPosition () ) ==14 ||(ships [0].getPosition () ) ==21 || (ships [0].getPosition () ) ==28|| (ships [0].getPosition () ) ==35)
				{
					//storing the buttons that a ships cannot be placed at 
					AIspotsChosen [spotsChosen] = (ships [0].getPosition () ) - 7; 
					spotsChosen = spotsChosen + 1;
					AIspotsChosen [spotsChosen] = (ships [0].getPosition () ) - 6; 
					spotsChosen = spotsChosen + 1;
					AIspotsChosen [spotsChosen] = (ships [0].getPosition () ) - 5; 
					spotsChosen = spotsChosen + 1;
					AIspotsChosen [spotsChosen] = (ships [0].getPosition () ) + 3; 
					spotsChosen = spotsChosen + 1;
					AIspotsChosen [spotsChosen] = (ships [0].getPosition () ) + 7; 
					spotsChosen = spotsChosen + 1;
					AIspotsChosen [spotsChosen] = (ships [0].getPosition () ) + 8; 
					spotsChosen = spotsChosen + 1;
					AIspotsChosen [spotsChosen] = (ships [0].getPosition () ) + 9; 
					spotsChosen = spotsChosen + 1;
				}
				//if it is a button in the middle 
				else 
				{
					//storing the buttons that a ships cannot be placed at 
					AIspotsChosen [spotsChosen] = (ships [0].getPosition () ) - 7; 
					spotsChosen = spotsChosen + 1;
					AIspotsChosen [spotsChosen] = (ships [0].getPosition () ) - 6; 
					spotsChosen = spotsChosen + 1;
					AIspotsChosen [spotsChosen] = (ships [0].getPosition () ) - 5; 
					spotsChosen = spotsChosen + 1;
					AIspotsChosen [spotsChosen] = (ships [0].getPosition () ) + 3; 
					spotsChosen = spotsChosen + 1;
					AIspotsChosen [spotsChosen] = (ships [0].getPosition () ) + 7; 
					spotsChosen = spotsChosen + 1;
					AIspotsChosen [spotsChosen] = (ships [0].getPosition () ) + 8; 
					spotsChosen = spotsChosen + 1;
					AIspotsChosen [spotsChosen] = (ships [0].getPosition () ) + 9; 
					spotsChosen = spotsChosen + 1;
					AIspotsChosen [spotsChosen] = (ships [0].getPosition () ) - 1; 
					spotsChosen = spotsChosen + 1;
				}

				ShipsSunk++;//one more ship was sunk 
				ships[0].setSunk(true);//the full ship was hit
				//setting new colour to a deep red to show the full ship was hit
				AIButtons[ships[0].getPosition()].setBackground((new Color(110, 13, 7)));
				AIButtons[ships[0].getPosition()+1].setBackground((new Color(110, 13, 7)));
				AIButtons[ships[0].getPosition()+2].setBackground((new Color(110, 13, 7)));
				fullShipLabel.setText("The AI has sunk " + ShipsSunk +"/3 of your ships");
				fullShipLabel.setVisible (true); 
			}
		}
		//if it hit the second boat
		else if (position == ships [1].getPosition ()||(position ==(ships [1].getPosition ()) + 1))
		{
			//if the full boat hasn't been hit already
			if (ship2 != 0)
			{
				previousPosition = position;
				ship2 = ship2 - 1; //a spot on boat 2 was hit
				hit = true; //it was hit
				miss = false;
				AIButtons[position].setBackground(Color.red);//changing the colour so the user can see a ship was hit
			}
			//if the full boat has been hit
			if (ship2 == 0) 
			{
				//resetting all the variables 
				one = false;
				two = false;
				three = false;
				four = false;
				previousPosition = -1;
				originalPosition = -1;
				RNum = 0;
				miss = false;
				
				//storing all the surrounding spots of the ship hit  as spots chosen so the AI doesn't hit there (making the AI smarter since it knows a ship can't be placed directly beside another)
				//if it is in the first row
				if ((ships [1].getPosition () ) > -1 && (ships [1].getPosition () ) < 7)		
				{
					//if it is in the 6th column 
					if ((ships [1].getPosition () ) == 5)
					{
						//storing the buttons that a ships cannot be placed at 
						AIspotsChosen [spotsChosen] = (ships [1].getPosition () ) + 8; 
						spotsChosen = spotsChosen + 1;
						AIspotsChosen [spotsChosen] = (ships [1].getPosition () ) + 7; 
						spotsChosen = spotsChosen + 1;
						AIspotsChosen [spotsChosen] = (ships [1].getPosition () ) - 1; 
						spotsChosen = spotsChosen + 1;
					}
					//if it is in the 1st column
					else if ((ships [1].getPosition () ) == 0)
					{					
						//storing the buttons that a ships cannot be placed at 
						AIspotsChosen [spotsChosen] = (ships [1].getPosition () ) + 8; 
						spotsChosen = spotsChosen + 1;
						AIspotsChosen [spotsChosen] = (ships [1].getPosition () ) + 7; 
						spotsChosen = spotsChosen + 1;
						AIspotsChosen [spotsChosen] = (ships [1].getPosition () ) +2; 
						spotsChosen = spotsChosen + 1;
					}
					//if it is in the first row only 
					else 
					{
						//storing the buttons that a ships cannot be placed at 
						AIspotsChosen [spotsChosen] = (ships [1].getPosition () ) + 8; 
						spotsChosen = spotsChosen + 1;
						AIspotsChosen [spotsChosen] = (ships [1].getPosition () ) + 7; 
						spotsChosen = spotsChosen + 1;
						AIspotsChosen [spotsChosen] = (ships [1].getPosition () ) - 1; 
						spotsChosen = spotsChosen + 1;
						AIspotsChosen [spotsChosen] = (ships [1].getPosition () ) +2; 
						spotsChosen = spotsChosen + 1;
					}	

				}
				//if it is in the last row 
				else if ((ships [1].getPosition () ) >41 && (ships [1].getPosition () ) <51)
				{
					//if it is in the first column 
					if ((ships [1].getPosition () ) == 42 )
					{
						//storing the buttons that a ships cannot be placed at 
						AIspotsChosen [spotsChosen] = (ships [1].getPosition () ) - 6; 
						spotsChosen = spotsChosen + 1;
						AIspotsChosen [spotsChosen] = (ships [1].getPosition () ) - 7; 
						spotsChosen = spotsChosen + 1;
						AIspotsChosen [spotsChosen] = (ships [1].getPosition () ) +2; 
						spotsChosen = spotsChosen + 1;

					}
					//if it is in the 6th column 
					else if ((ships [1].getPosition () ) ==47)
					{
						//storing the buttons that a ships cannot be placed at 
						AIspotsChosen [spotsChosen] = (ships [1].getPosition () ) - 6; 
						spotsChosen = spotsChosen + 1;
						AIspotsChosen [spotsChosen] = (ships [1].getPosition () ) - 7; 
						spotsChosen = spotsChosen + 1;
						AIspotsChosen [spotsChosen] = (ships [1].getPosition () ) - 1; 
						spotsChosen = spotsChosen + 1;
					}
					//if it is just in the last row 
					else
					{
						//storing the buttons that a ships cannot be placed at 
						AIspotsChosen [spotsChosen] = (ships [1].getPosition () ) - 6; 
						spotsChosen = spotsChosen + 1;
						AIspotsChosen [spotsChosen] = (ships [1].getPosition () ) - 7; 
						spotsChosen = spotsChosen + 1;
						AIspotsChosen [spotsChosen] = (ships [1].getPosition () ) +2; 
						spotsChosen = spotsChosen + 1;
						AIspotsChosen [spotsChosen] = (ships [1].getPosition () ) - 1; 
						spotsChosen = spotsChosen + 1;
					}
				}
				//if it is in the 6th column
				else if ((ships [1].getPosition () ) ==12|| (ships [1].getPosition () ) ==19 || (ships [1].getPosition () ) ==26 ||(ships [1].getPosition () ) ==33 || (ships [1].getPosition () ) == 40)
				{
					//storing the buttons that a ships cannot be placed at 
					AIspotsChosen [spotsChosen] = (ships [1].getPosition () ) - 6; 
					spotsChosen = spotsChosen + 1;
					AIspotsChosen [spotsChosen] = (ships [1].getPosition () ) - 7; 
					spotsChosen = spotsChosen + 1;
					AIspotsChosen [spotsChosen] = (ships [1].getPosition () ) + 7; 
					spotsChosen = spotsChosen + 1;
					AIspotsChosen [spotsChosen] = (ships [1].getPosition () ) + 8; 
					spotsChosen = spotsChosen + 1;
					AIspotsChosen [spotsChosen] = (ships [1].getPosition () ) - 1; 
					spotsChosen = spotsChosen + 1;
				}
				//if it is in the 1st column
				else if ((ships [1].getPosition () )  == 7|| (ships [1].getPosition () ) ==14 ||(ships [1].getPosition () ) ==21 || (ships [1].getPosition () ) ==28|| (ships [1].getPosition () ) ==35)
				{
					//storing the buttons that a ships cannot be placed at 
					AIspotsChosen [spotsChosen] = (ships [1].getPosition () ) - 6; 
					spotsChosen = spotsChosen + 1;
					AIspotsChosen [spotsChosen] = (ships [1].getPosition () ) - 7; 
					spotsChosen = spotsChosen + 1;
					AIspotsChosen [spotsChosen] = (ships [1].getPosition () ) + 7; 
					spotsChosen = spotsChosen + 1;
					AIspotsChosen [spotsChosen] = (ships [1].getPosition () ) + 8; 
					spotsChosen = spotsChosen + 1;
					AIspotsChosen [spotsChosen] = (ships [1].getPosition () ) + 2; 
					spotsChosen = spotsChosen + 1;
				}
				//if it is a button in the middle 
				else 
				{		
					//storing the buttons that a ships cannot be placed at 
					AIspotsChosen [spotsChosen] = (ships [1].getPosition () ) - 6; 
					spotsChosen = spotsChosen + 1;
					AIspotsChosen [spotsChosen] = (ships [1].getPosition () ) - 7; 
					spotsChosen = spotsChosen + 1;
					AIspotsChosen [spotsChosen] = (ships [1].getPosition () ) + 7; 
					spotsChosen = spotsChosen + 1;
					AIspotsChosen [spotsChosen] = (ships [1].getPosition () ) + 8; 
					spotsChosen = spotsChosen + 1;
					AIspotsChosen [spotsChosen] = (ships [1].getPosition () ) - 1; 
					spotsChosen = spotsChosen + 1;
					AIspotsChosen [spotsChosen] = (ships [1].getPosition () ) + 2; 
					spotsChosen = spotsChosen + 1;
				}

				ShipsSunk++;//one more ship was sunk 
				ships[1].setSunk(true);//the full ship was hit
				//setting new colour as deep red to show the full ship was hit 
				AIButtons[ships[1].getPosition()].setBackground((new Color(110, 13, 7)));
				AIButtons[ships[1].getPosition()+1].setBackground((new Color(110, 13, 7)));
				fullShipLabel.setText ("The AI has sunk " + ShipsSunk +"/3 of your ships");
				fullShipLabel.setVisible (true); 
			}
		}
		//if it hit the third boat
		else if (position == ships [2].getPosition ()||(position ==(ships [2].getPosition ()) + 7) || (position == (ships [2].getPosition ()) + 14))
		{
			//if the full boat hasn't been hit already
			if (ship3 != 0)
			{
				//if original position hasn’t been stored yet
				if (ship3 == 3 ){

					//for loop to check if the boat is on the right edge of the board and the spot beside it was chosen already (so it goes directly to vertical boat random number generation)
					for (int b = 0; b < spotsChosen; b++) {	
						if (position - 1 == AIspotsChosen[b]) {
							if (ships[2].getPosition() == 6 || ships[2].getPosition() == 13 || ships[2].getPosition()  == 20 || ships[2].getPosition()  ==27|| ships[2].getPosition() == 34  ||ships[2].getPosition() == 41 || ships[2].getPosition() == 48) {
								go = true; //go to vertical boat random number generation
							}
						}
					}

					//for loop to check if the boat is on the left edge of the board and the spot beside it was chosen already (so it goes directly to vertical boat random number generation)
					for (int b = 0; b < spotsChosen; b ++) {	
						if (position + 1 == AIspotsChosen[b]) {
							if (ships[2].getPosition() == 0 || ships[2].getPosition() == 7 || ships[2].getPosition() == 14 || ships[2].getPosition()  == 21 || ships[2].getPosition()  == 28 || ships[2].getPosition() == 35  ||ships[2].getPosition() == 42) {
								go = true; //go to vertical boat random number generation
							}
						}
					}

					//if it has not already been assigned as true in the previous for loops 
					if (go == false){
						//for loop to check if both sides of the vertical boat were chosen already so it goes directly to vertical boat random number generation
						for (int b = 0; b < spotsChosen; b ++) {	
							if (position - 1 == AIspotsChosen[b]) {
								for (int p = 0; p < spotsChosen; p ++) {	
									if (position + 1 == AIspotsChosen[p]) {
										go = true;//go to vertical boat random number generation
									}
								}
							}
						}
					}
				}
				previousPosition = position;//storing previous position
				ship3 = ship3 - 1; //a spot on boat 3 was hit
				hit = true; //it was hit
				AIButtons[position].setBackground(Color.red);//changing the colour so the user can see a ship was hit
			}
			//if the full boat has been hit
			if (ship3 == 0)
			{
				//resetting all the variables 
				one = false;
				two = false;
				three = false;
				four = false;
				previousPosition = -1;
				originalPosition = -1;
				RNum = 0;
				f = false;
				go = false;
				
				//storing all the surrounding spots of the ship hit  as spots chosen so the AI doesn't hit there (making the AI smarter since it knows a ship can't be placed directly beside another)
				//if it is in the first row 
				if ((ships [2].getPosition () )  > -1 && (ships [2].getPosition () )  < 7)
				{
					//if it is in the 7th column
					if ((ships [2].getPosition () )  == 6)
					{
						//storing the buttons that a ships cannot be placed at 
						AIspotsChosen [spotsChosen]= (ships [2].getPosition () )  - 1; 
						spotsChosen = spotsChosen + 1;
						AIspotsChosen [spotsChosen]= (ships [2].getPosition () )  + 6; 
						spotsChosen = spotsChosen + 1;
						AIspotsChosen [spotsChosen]= (ships [2].getPosition () )  + 13; 
						spotsChosen = spotsChosen + 1;
						AIspotsChosen [spotsChosen]= (ships [2].getPosition () )  + 21; 
						spotsChosen = spotsChosen + 1;
					}
					//if it is in the 1st column
					else if ((ships [2].getPosition () )  == 0)
					{
						//storing the buttons that a ships cannot be placed at 
						AIspotsChosen [spotsChosen]= (ships [2].getPosition () )  + 1; 
						spotsChosen = spotsChosen + 1;
						AIspotsChosen [spotsChosen]= (ships [2].getPosition () )  + 8; 
						spotsChosen = spotsChosen + 1;
						AIspotsChosen [spotsChosen]= (ships [2].getPosition () )  + 15; 
						spotsChosen = spotsChosen + 1;
						AIspotsChosen [spotsChosen]= (ships [2].getPosition () )  + 21; 
						spotsChosen = spotsChosen + 1;
					}
					//if it is only in the first row 
					else
					{	
						//storing the buttons that a ships cannot be placed at 
						AIspotsChosen [spotsChosen]= (ships [2].getPosition () )  + 1; 
						spotsChosen = spotsChosen + 1;
						AIspotsChosen [spotsChosen]= (ships [2].getPosition () )  -1; 
						spotsChosen = spotsChosen + 1;
						AIspotsChosen [spotsChosen]= (ships [2].getPosition () )  + 6; 
						spotsChosen = spotsChosen + 1;
						AIspotsChosen [spotsChosen]= (ships [2].getPosition () )  + 8; 
						spotsChosen = spotsChosen + 1;
						AIspotsChosen [spotsChosen]= (ships [2].getPosition () )  + 13; 
						spotsChosen = spotsChosen + 1;
						AIspotsChosen [spotsChosen]= (ships [2].getPosition () )  + 15; 
						spotsChosen = spotsChosen + 1;
						AIspotsChosen [spotsChosen]= (ships [2].getPosition () )  + 21; 
						spotsChosen = spotsChosen + 1;
					}
				}
				//if it is in the last row
				else if ((ships [2].getPosition () )  >41 && (ships [2].getPosition () )  <51)
				{
					//if it is in the 7th column 
					if ((ships [2].getPosition () )  == 6)
					{
						//storing the buttons that a ships cannot be placed at 
						AIspotsChosen [spotsChosen]= (ships [2].getPosition () )  - 1; 
						spotsChosen = spotsChosen + 1;
						AIspotsChosen [spotsChosen]= (ships [2].getPosition () )  + 6; 
						spotsChosen = spotsChosen + 1;
						AIspotsChosen [spotsChosen]= (ships [2].getPosition () )  + 13; 
						spotsChosen = spotsChosen + 1;
						AIspotsChosen [spotsChosen]= (ships [2].getPosition () )  -7; 
						spotsChosen = spotsChosen + 1;

					}
					//if it is in the 1st column 
					else if ((ships [2].getPosition () )  == 42)
					{
						//storing the buttons that a ships cannot be placed at 
						AIspotsChosen [spotsChosen]= (ships [2].getPosition () )  + 1; 
						spotsChosen = spotsChosen + 1;
						AIspotsChosen [spotsChosen]= (ships [2].getPosition () )  + 8; 
						spotsChosen = spotsChosen + 1;
						AIspotsChosen [spotsChosen]= (ships [2].getPosition () )  + 15; 
						spotsChosen = spotsChosen + 1;
						AIspotsChosen [spotsChosen]= (ships [2].getPosition () )  - 7; 
						spotsChosen = spotsChosen + 1;
					}
					//if it is only in the last row
					else 
					{
						//storing the buttons that a ships cannot be placed at 
						AIspotsChosen [spotsChosen]= (ships [2].getPosition () )  + 1; 
						spotsChosen = spotsChosen + 1;
						AIspotsChosen [spotsChosen]= (ships [2].getPosition () )  -1; 
						spotsChosen = spotsChosen + 1;
						AIspotsChosen [spotsChosen]= (ships [2].getPosition () )  + 6; 
						spotsChosen = spotsChosen + 1;
						AIspotsChosen [spotsChosen]= (ships [2].getPosition () )  + 8; 
						spotsChosen = spotsChosen + 1;
						AIspotsChosen [spotsChosen]= (ships [2].getPosition () )  + 13; 
						spotsChosen = spotsChosen + 1;
						AIspotsChosen [spotsChosen]= (ships [2].getPosition () )  + 15; 
						spotsChosen = spotsChosen + 1;
						AIspotsChosen [spotsChosen]= (ships [2].getPosition () )  - 7; 
						spotsChosen = spotsChosen + 1;
					}

				}
				//if it is in the 7th column
				else if ((ships [2].getPosition () )  == 34 || (ships [2].getPosition () )  ==13 ||(ships [2].getPosition () )  ==20 || (ships [2].getPosition () )  ==27|| (ships [2].getPosition () )  ==41)
				{
					//storing the buttons that a ships cannot be placed at 
					AIspotsChosen [spotsChosen]= (ships [2].getPosition () )  - 1; 
					spotsChosen = spotsChosen + 1;
					AIspotsChosen [spotsChosen]= (ships [2].getPosition () )  + 6; 
					spotsChosen = spotsChosen + 1;
					AIspotsChosen [spotsChosen]= (ships [2].getPosition () )  + 13; 
					spotsChosen = spotsChosen + 1;
					AIspotsChosen [spotsChosen]= (ships [2].getPosition () )  -7; 
					spotsChosen = spotsChosen + 1;
					AIspotsChosen [spotsChosen]= (ships [2].getPosition () )  + 21; 
					spotsChosen = spotsChosen + 1;
				}
				//if it is in the 1st column
				else if ((ships [2].getPosition () )  == 7 || (ships [2].getPosition () )  ==14 ||(ships [2].getPosition () )  ==21 || (ships [2].getPosition () )  ==28|| (ships [2].getPosition () )  ==35)
				{
					//storing the buttons that a ships cannot be placed at 
					AIspotsChosen [spotsChosen]= (ships [2].getPosition () )  + 1; 
					spotsChosen = spotsChosen + 1;
					AIspotsChosen [spotsChosen]= (ships [2].getPosition () )  + 8; 
					spotsChosen = spotsChosen + 1;
					AIspotsChosen [spotsChosen]= (ships [2].getPosition () )  + 15; 
					spotsChosen = spotsChosen + 1;
					AIspotsChosen [spotsChosen]= (ships [2].getPosition () )  + 21; 
					spotsChosen = spotsChosen + 1;
					AIspotsChosen [spotsChosen]= (ships [2].getPosition () )  -7; 
					spotsChosen = spotsChosen + 1;
				}
				//if it is a button in the middle 
				else 
				{
					//storing the buttons that a ships cannot be placed at 
					AIspotsChosen [spotsChosen]= (ships [2].getPosition () )  + 1; 
					spotsChosen = spotsChosen + 1;
					AIspotsChosen [spotsChosen]= (ships [2].getPosition () )  -1; 
					spotsChosen = spotsChosen + 1;
					AIspotsChosen [spotsChosen]= (ships [2].getPosition () )  + 6; 
					spotsChosen = spotsChosen + 1;
					AIspotsChosen [spotsChosen]= (ships [2].getPosition () )  + 8; 
					spotsChosen = spotsChosen + 1;
					AIspotsChosen [spotsChosen]= (ships [2].getPosition () )  + 13; 
					spotsChosen = spotsChosen + 1;
					AIspotsChosen [spotsChosen]= (ships [2].getPosition () )  + 15; 
					spotsChosen = spotsChosen + 1;
					AIspotsChosen [spotsChosen]= (ships [2].getPosition () )  - 7; 
					spotsChosen = spotsChosen + 1;
					AIspotsChosen [spotsChosen]= (ships [2].getPosition () )  + 21; 
					spotsChosen = spotsChosen + 1;

				}
				ShipsSunk++;//one more ship was sunk
				ships[2].setSunk(true);//the full ship was hit
				AIButtons[ships[2].getPosition()].setBackground((new Color(110, 13, 7)));
				AIButtons[ships[2].getPosition()+7].setBackground((new Color(110, 13, 7)));
				AIButtons[ships[2].getPosition()+14].setBackground((new Color(110, 13, 7)));
				fullShipLabel.setText ("The AI has sunk " + ShipsSunk +"/3 of your ships");
				fullShipLabel.setVisible (true); 
			}
		}
		//if it didn't hit a ship
		else 
		{
			hit = false; //didn't hit a ship
			miss = true;
			AIButtons[position].setBackground(Color.white);//changing the colour so the user can see a ship wasn't hit
		
			//if it hit to the right last time 
			if (RNum == 3) {
				previousPosition = position;
				four = true;//hit to the left
			}
			
			//if it hit to the left last time
			else if (RNum == 4) {
				previousPosition = position;
				three = true;//hit to the right 
			}
			
			//if it hit down last time
			else if (RNum == 1) {
				RNum = 2;//hit up
				//reset variables 
				one = false;
				two = true;
				f = true;//f = true to go into new logic and hit from previous position only 
				previousPosition = originalPosition;//restoring previous position for new logic with f = true where it hits from previous only 
			}
			//if it hit up last time 
			else if (RNum == 2) {
				RNum = 1;//hit down 
				//reset variables 
				two = false;
				one = true;
				f = true;//f = true to go into new logic and hit from previous position only 
				previousPosition = originalPosition;//restoring previous position for new logic with f = true where it hits from previous only
			}
		}
	}

	//Amrin's Method - checking if a ship was hit by the user along with full ship hit and all ships hit detection
	public static void UserHitDetection (int position)
	{
		//storing the spots the user chose (so it can't be chosen again)
		userSpotsChosen [spotsChosen1] = position; 
		spotsChosen1 = spotsChosen1 + 1; 
		userButtons[position].setEnabled(false);

		//if it hit the first boat
		if (position == AIships [0] || (position == (AIships [0]) + 1) || (position == (AIships [0]) + 2))
		{
			//if the full boat hasn't been hit already
			if (AIship1 != 0)
			{
				AIship1 = AIship1 - 1; //a spot on boat 1 was hit
				hit = true; //it was hit
				userButtons[position].setBackground(Color.red);//changing the colour so the user can see a ship was hit
			}
			//if the full boat has been hit
			if (AIship1 == 0)
			{
				allAIships = allAIships -1;//there is one less ship
				ShipsSunkU++;//user sunk onw more ship
				userButtons[ships[3].getPosition()].setBackground((new Color(110, 13, 7)));
				userButtons[ships[3].getPosition()+1].setBackground((new Color(110, 13, 7)));
				userButtons[ships[3].getPosition()+2].setBackground((new Color(110, 13, 7)));
				fullShipLabelU.setText ("You have sunk " + ShipsSunkU +"/3 of the AI's ships");
				fullShipLabelU.setVisible (true); 
			}
		}
		//if it hit the second boat
		else if (position == AIships [1]||(position ==(AIships [1]) + 1))
		{
			//if the full boat hasn't been hit already
			if (AIship2 != 0)
			{
				AIship2 = AIship2 - 1; //a spot on boat 2 was hit
				hit = true; //it was hit
				userButtons[position].setBackground(Color.red);//changing the colour so the user can see a ship was hit
			}
			//if the full boat has been hit
			if (AIship2 == 0) 
			{
				allAIships = allAIships -1; //there is one less ship to be hit
				ShipsSunkU++;//user sunk one more ship
				userButtons[ships[4].getPosition()].setBackground((new Color(110, 13, 7)));
				userButtons[ships[4].getPosition()+1].setBackground((new Color(110, 13, 7)));
				fullShipLabelU.setText ("You have sunk " + ShipsSunkU +"/3 of the AI's ships");
				fullShipLabelU.setVisible (true); 
			}
		}
		//if it hit the third boat
		else if (position == AIships [2]||(position ==(AIships [2]) + 7) || (position == (AIships [2]) + 14))
		{
			//if the full boat hasn't been hit already
			if (AIship3 != 0)
			{
				AIship3 = AIship3 - 1; //a spot on boat 3 was hit
				hit = true; //it was hit
				userButtons[position].setBackground(Color.red);//changing the colour so the user can see a ship was hit
			}
			//if the full boat has been hit
			if (AIship3 == 0)
			{
				allAIships = allAIships -1; //there is one less ship to be hit
				ShipsSunkU++;//user sunk one more ship 
				userButtons[ships[5].getPosition()].setBackground((new Color(110, 13, 7)));
				userButtons[ships[5].getPosition()+7].setBackground((new Color(110, 13, 7)));
				userButtons[ships[5].getPosition()+14].setBackground((new Color(110, 13, 7)));
				fullShipLabelU.setText ("You have sunk " + ShipsSunkU +"/3 of the AI's ships");
				fullShipLabelU.setVisible (true); 
			}
		}
		//if it didn't hit a ship
		else 
		{
			hit = false; //didn't hit a ship
			userButtons[position].setBackground(Color.white);//changing the colour so the user can see a ship wasn't hit
		}

		//if all the ships have been hit
		if (allAIships <= 0)
		{
			turn = true;//turn is the user's 
			win();//calling on win method to bring up you win JFrame 
		}		

	}

	//HBoat method to intelligently choose a spot to hit from left to right (3-4)
	public static void HBoat() {
		cond = false;

		int limit; // Assigns method specific variables 

		//if the two spots beside vertical boat have been hit already or one spot beside and it is on the edge (both checked for in hit detection line 2122-2156
		if (go == true){
			cond = true; //don't go into the other if statements 
			VBoat (); //go into VBoat method to choose a spot to hit from up or down 
		}

		//if it didn't already go into VBoat method, check if it needs to 
		if (cond == false) {
			
			//if it hit the vertical on its first turn 
			if (ships[2].getPosition() == originalPosition || ships[2].getPosition() + 7 == originalPosition || ships[2].getPosition() + 14 == originalPosition) {
				
				//for loop to check if it is on the right edge of the board and the spot beside it was chosen already 
				for (int a = 0; a < spotsChosen; a ++) {	
					if (originalPosition - 1 == AIspotsChosen[a]) {
						if (ships[2].getPosition() == 6 || ships[2].getPosition() == 13 || ships[2].getPosition()  == 20 || ships[2].getPosition()  ==27|| ships[2].getPosition() == 34  ||ships[2].getPosition() == 41 || ships[2].getPosition() == 48) {
							VBoat();//go into VBoat method to choose a spot to hit from up or down 
							cond = true;//don't go into the other if statements 
						}
					}
				}
				
				//for loop to check if it is on the left edge of the board and the spot beside it was chosen already 
				for (int a = 0; a < spotsChosen; a ++) {	
					if (originalPosition + 1 == AIspotsChosen[a]) {
						if (ships[2].getPosition() == 0 || ships[2].getPosition() == 7 || ships[2].getPosition() == 14 || ships[2].getPosition()  == 21 || ships[2].getPosition()  == 28 || ships[2].getPosition() == 35  ||ships[2].getPosition() == 42) {
							VBoat();//go into VBoat method to choose a spot to hit from up or down
							cond = true;//don't go into the other if statements 
						}
					}
				}
			}
		}

		//if it hit the vertical boat on its first turn 
		if (ships[2].getPosition() == originalPosition || ships[2].getPosition() + 7 == originalPosition || ships[2].getPosition() + 14 == originalPosition) {
			if ( originalPosition != -1 && cond == false) {
				//for loop to check if both spots beside the original spot chosen have been selected previously (so it goes straight to VBoat number generation to hit up or down)
				for (int a = 0; a < spotsChosen; a ++) {	
					if (originalPosition - 1 == AIspotsChosen[a]) {
						for (int p = 0; p < spotsChosen; p ++) {	
							if (originalPosition + 1 == AIspotsChosen[p]) {
								cond = true;//don't go into the other if statements 
								VBoat();//go into VBoat method to choose a spot to hit from up or down
							}
						}
					}
				}
			}
		}

		//if it didn't go into VBoat method already do the following 
		if (cond == false) {	
			
			//if the random number generated was 3 (hit to the right)
			if (three == true && previousPosition != -1 && RNum == 3) {
				if (previousPosition != originalPosition) {
					//if it tried hitting right already and didn't hit the full ship hit left instead
					if (four == true) {
						position = originalPosition - 1;//hit left instead
					}
					//if it tried hitting right and it wasn't a miss, keep hitting right 
					else {
						position = previousPosition + 1;//hit right again
						four = true;//next time hit left
						
						//if it is trying to hit in the next row
						if (position == 0 || position == 7 || position == 14 || position == 21 || position == 28|| position == 35 || position == 42) {
							position = position - 3;//hit the opposite way 
						}
					}
				}
			}

			//if the random number generated was 4 (hit to the left)
			else if(four == true && previousPosition != -1 && RNum == 4) {
				if (previousPosition != originalPosition) {
					//if it tried hitting left already and didn't hit the full ship hit right instead
					if (three == true) {
						position = originalPosition + 1;//hit right instead
					}

					//if it tried hitting left and it wasn't a miss, keep hitting left 
					else {
						position = previousPosition - 1;//hit left again
						three = true;//next time hit right
						
						//if it is trying to hit in the next row
						if (position == 6 || position == 13 || position  == 20 || position  ==27|| position == 34  ||position == 41 || position == 48) {
							position = position + 3;//hit the opposite way 
						}
					}
				}
			}

			//if no random number between 3 and 4 had been assigned yet
			else {
				Random rand = new Random(); // Sets up random number generator 
				limit = 2;      // Set number generation top limit (3-4)
				RNum = rand.nextInt(limit);
				RNum = RNum + 3;
				
				//if original position hasn't been assigned yet
				if (originalPosition == -1) {
					originalPosition = position;//assign original position
				}

				//if the position is on the left edge of the board
				if (position == 0 || position == 7 || position == 14 || position == 21 || position == 28|| position == 35 || position == 42) {
					RNum = 3;//hit right only
				}
				
				//if the position is on the right edge of the board
				else if (position == 6 || position == 13 || position  == 20 || position  ==27|| position == 34  ||position == 41 || position == 48) {
					RNum = 4;//hit left only 
				}

				//if it was assigned to hit right
				if (RNum == 3) {
					randCheck = false;
					position = originalPosition + 1;//hit right
					three = true;//hit right next time 
				}
				else {
					randCheck = false;
					position = originalPosition - 1;//hit left
					four = true;//hit left next time 
				}
			}
		}
	}

	
	//VBoat method to choose a number between 1 and 2 to shoot either up or down
	public static void VBoat() {
		int limit; // Assigns method specific variables 

		//if the random number assigned is 1 (hit down)
		if (one == true && previousPosition != -1 && RNum == 1) {
		
			//if in AI hit detection the previous position was already chosen or was a miss, hit from previous position only (in the direction down)
			if (f == true) {
				position = previousPosition + 7;//hit down 
			}

			else if (previousPosition != originalPosition) {
				//if it tried hitting down already and didn't hit the full ship hit up instead
				if (two == true) {
					position = originalPosition - 7;//hit up
				}

				//if it tried hitting down already and it was a hit, keep hitting down 
				else {
					position = previousPosition + 7;//hit down
					two = true;//hit up next time 
				}
			}
		}

		//if the random number assigned is 2 (hit up)
		else if(two == true && previousPosition != -1 && RNum == 2) {
			//if in AI hit detection the previous position was already chosen or was a miss, hit from previous position only (in the direction up)
			if (f == true) {
				position = previousPosition  - 7;//hit up			
			}

			else if (previousPosition != originalPosition) {
				//if it tried hitting up already and didn't hit the full ship hit down instead
				if (one == true) {
					position = originalPosition + 7;//hit down 
				}

				//if it tried hitting up already and it was a hit, keep hitting up 
				else {
					position = previousPosition - 7;//hit up
					one = true;//hit down next time 
				}
			}
		}

		//if a number between 1 and 2 hasn't been generated yet do the following 
		else {
			Random rand = new Random(); // Sets up random number generator 
			limit = 2;      // Set number generation top limit (1-2)
			RNum = rand.nextInt(limit);
			RNum = RNum + 1;
			
			//if original position hasn't been assigned yet
			if (originalPosition == -1) {
				originalPosition = position;//assign original position
			}

			//if the random number generated is 1 (hit down)
			if (RNum == 1) {
				randCheck = false;
				position = originalPosition + 7;//hit down 
				one = true;//hit down next time 
			}
			//if the random number generated is 2 (hit up)
			else {
				randCheck = false;
				position = originalPosition - 7;//hit up
				two = true;//hit up next time
			}
		}
	}

	//AIintelligence method to decide which method to go into (Hboat to hit right and left, or VBoat to hit up and down)
	public static void AIintelligence ()
	{
		//if all the ships have been sunk already don't taken another turn 
		if ((ships [0].getSunk () == true) && (ships [1].getSunk () == true) && ships [2].getSunk () == true)
		{
			turn = true;//end AI's turn 
			lose();//call on lose method to pull up you lost JFrame 
		}
		
		//if it's hit a spot where a ship is 
		if (previousPosition != -1){

			//if all horizontal ships have not been sunk, hit right and left first 
			if (ships [0].getSunk() == false || ships [1].getSunk() == false) {
				HBoat();//call on HBoat method to hit right and left first 
			}
			//if the vertical boat has been sunk already, call on HBoat method to only hit left and right (since only horizontal boats are left)
			else if (ships[2].getSunk() == true) {
				HBoat();//call on HBoat method to hit right and left  
			}
			//if the both horizontal ships have been sunk already and the vertical ship has not been sunk, call on VBoat method to only hit up and down (since only the vertical boat is left)
			else if (ships [0].getSunk() == true && ships [1].getSunk() == true && ships[2].getSunk() == false) {
				VBoat();//call on HBoat method to hit up and down  
			}
		}

		//if it hasn't hit a ship yet, only hit random spots 
		else {
			randCheck = false;
			newRand();//calling on method to generate new random number from 1-49

			//while loop to check if the random number has already been chosen 
			while (randCheck == false){

				//if this is not the first turn for AI (not the first spot chosen)
				if (spotsChosen != 0) {
					
					//for loop to check if the random number is in the last spot of the array 
					for (int a = 0; a < spotsChosen; a ++){
						//if it's been chosen already 
						if (position == AIspotsChosen[spotsChosen-1]) {
							newRand();//generate new random number
							//reset variables to check if new random number has already been chosen
							randCheck = false;
							a = 0;							
						}
						//if the spot was not already chosen 
						else if (a == (spotsChosen -1)) {
							randCheck = true;//exit the loop
						}
						//if the spot has already been chosen 
						else if (position == AIspotsChosen[a]) {
							newRand();//generate new random number 
							//reset variables to check if new random number has already been chosen
							randCheck = false;
							a = 0;
						}
					}
				}
				//if it is the AI's first turn 
				else {
					randCheck = true;//exit loop because the spot can't already have been chosen 
				}
			}
		}
	}

	// Nivaethan's method: if the user were to win the game 
	public static void win ()
	{
		//disable all the buttons 
		for (int x=0; x <49; x++) {
			userButtons[x].setEnabled(false);
		}
		// Instantiating panel
		JPanel panel4 = new JPanel(); // Creates new panel 
		panel4.setBorder(new EmptyBorder(5, 5, 5, 5));
		panel4.setLayout(null);

		// Instantiating frame 
		JFrame frame4 = new JFrame (); // instantiate NEW frame with a title
		frame4.setDefaultCloseOperation (JFrame.DO_NOTHING_ON_CLOSE);
		frame4.setSize (600, 250);
		frame4.getContentPane ().add (panel4); //add the panel with components to the frame
		frame4.setVisible (true);

		// Instantiating a text area 
		JTextArea taWin = new JTextArea();
		taWin.setEditable(false);
		taWin.setBackground(Color.WHITE);
		taWin.setForeground(Color.BLACK);
		taWin.setOpaque(false);
		taWin.setFont(new Font("Arial", Font.BOLD, 20));
		taWin.setText("Winner!");
		taWin.setBounds(265, 0, 80, 28);

		// Instantiating a text area
		JTextArea taMessage = new JTextArea();
		taMessage.setEditable(false);
		taMessage.setBackground(Color.WHITE);
		taMessage.setForeground(Color.RED);
		taMessage.setOpaque(false);
		taMessage.setFont(new Font("Arial", Font.PLAIN, 20));
		taMessage.setText("Congratulations! You have beat the all mighty AI and are now victorious. Click play again to start a new game or click quit to exit the game.");
		taMessage.setWrapStyleWord(true);
		taMessage.setLineWrap(true);
		taMessage.setBounds(0, 50, 600, 80);

		// Instantiating a button
		JButton buttonQuit = new JButton("Quit");
		buttonQuit.setFont(new Font("Arial", Font.BOLD, 12));
		buttonQuit.setBounds(168, 149, 89, 23);

		// Instantiating a button
		JButton buttonPlayAgain = new JButton("Play Again");
		buttonPlayAgain.setFont(new Font("Arial", Font.BOLD, 12));
		buttonPlayAgain.setBounds(286, 149, 94, 23);

		buttonQuit.addActionListener (new ActionListener ()	// Adding action listener for quit button
				{
			public void actionPerformed (ActionEvent e)
			{
				System.exit(0); // Exits the program
			}
				}
				);

		buttonPlayAgain.addActionListener (new ActionListener () // Adding action listener for play again button
				{
			public void actionPerformed (ActionEvent e)
			{
				restart();//calling on restart method to reset the variables and restart the game 
				frame2.dispose();
				frame4.dispose(); // This will only close the winner frame
			}
				}
				);

		// Adding items to the panel
		panel4.add(taWin);
		panel4.add(taMessage);
		panel4.add(buttonQuit);
		panel4.add(buttonPlayAgain);
	}

	// Nivaethan's method: if the user were to lose the game
	public static void lose()
	{
		//disable all the buttons 
		for (int x=0; x <49; x++) {
			userButtons[x].setEnabled(false);
		}

		frame2.setVisible(true);

		// Instantiating panel
		JPanel panel5 = new JPanel(); // Creates new panel 
		panel5.setBorder(new EmptyBorder(5, 5, 5, 5));
		panel5.setLayout(null);

		// Instantiating frame 
		JFrame frame5 = new JFrame (); // instantiate NEW frame with a title
		frame5.setDefaultCloseOperation (JFrame.DO_NOTHING_ON_CLOSE);
		frame5.setSize (600, 250);
		frame5.getContentPane ().add (panel5); //add the panel with components to the frame
		frame5.setVisible (true);

		// Instantiating a text area 
		JTextArea taLose = new JTextArea();
		taLose.setEditable(false);
		taLose.setOpaque(false);
		taLose.setBackground(Color.WHITE);
		taLose.setForeground(Color.BLACK);
		taLose.setFont(new Font("Arial", Font.BOLD, 20));
		taLose.setText("You lost :(");
		taLose.setBounds(265, 0, 98, 28);

		// Instantiating a text area
		JTextArea taMessage = new JTextArea();
		taMessage.setEditable(false);
		taMessage.setBackground(Color.WHITE);
		taMessage.setForeground(Color.RED);
		taMessage.setOpaque(false);
		taMessage.setFont(new Font("Arial", Font.PLAIN, 20));
		taMessage.setText("Oh no, you lost. The AI was victorious this time around. Click play again to try and claim victory or click quit to exit the game. In the window titled AI Ship tracker, you'll find where the AI's ships were located.");
		taMessage.setWrapStyleWord(true);
		taMessage.setLineWrap(true);
		taMessage.setBounds(10, 50, 590, 100);

		// Instantiating a button
		JButton buttonQuit = new JButton("Quit");
		buttonQuit.setFont(new Font("Arial", Font.BOLD, 12));
		buttonQuit.setBounds(168, 149, 89, 23);

		// Instantiating a button
		JButton buttonPlayAgain = new JButton("Play Again");
		buttonPlayAgain.setFont(new Font("Arial", Font.BOLD, 12));
		buttonPlayAgain.setBounds(286, 149, 94, 23);


		buttonQuit.addActionListener (new ActionListener ()	// Adding action listener for quit button
				{
			public void actionPerformed (ActionEvent e)
			{
				System.exit(0); // Exits the program
			}
				}
				);

		buttonPlayAgain.addActionListener (new ActionListener () // Adding action listener for play again button
				{
			public void actionPerformed (ActionEvent e)
			{
restart();//calling on restart method to reset the variables and restart the game 
frame2.setVisible(false);
				frame5.dispose(); // This will only close the winner frame
			}
				}
				);

		// Adding items to the panel
		panel5.add(taLose);
		panel5.add(taMessage);
		panel5.add(buttonQuit);
		panel5.add(buttonPlayAgain);

	}

}
