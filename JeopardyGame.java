

import java.util.Scanner;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
public class JeopardyGame {
	
	
	final static String CROSSOUT = "XXX  "; //declare a global constant variable for if the user answers an answer correctly (crossing out the slot on jeopardy board)
	final static int KEYWORD_MATCH_PERCENTAGE = 65; //65 percent for the keyword match for the user (if 65 percent of the word user enters is in answer it is correct)
	
	public static void main (String [] args) throws Exception{ //main method
		Scanner myScanner = new Scanner(System.in); //declare scanner
		String isPlayAgain = "Y"; //declare variable called 
		
		/* This application used integer.valueOf() method in some places to convert string to integer 
		 * this method returns the String as an integer. This will only happen if the string has an integer value inside
		 * LINK: https://www.javatpoint.com/java-integer-valueof-method
		*/
		
		// Print out game rules
		//Game rule link: https://tag.rutgers.edu/wp-content/uploads/2014/05/Jeopardy-instructions.pdf
		System.out.println("\r\n"
				+ "░░█ █▀▀ █▀█ █▀█ ▄▀█ █▀█ █▀▄ █▄█\r\n"
				+ "█▄█ ██▄ █▄█ █▀▀ █▀█ █▀▄ █▄▀ ░█░"); //use ascii text art to print for a bigger text size
		System.out.println();
		System.out.println("Rules: There will be a board of different slots of money and different topics the user may choose from. The more money the harder the question (difficulty level). You will play against "
				+  "\nthe AI for three rounds. Each round you and AI both answer the question based on the difficulty level they choose (money). If they do not answer the question right they will get "
				+ "\nmoney deducted from their score. However, if they get the question right they get to answer another question.  The format of the answer will be in letters not numbers and do not "
				+ "\ninclude the \"what is\" in your answer.If there is a money slot with \"XXX\" it means it is crossed out and either you or the AI have already answered and it cannot be chosen again."
				+ "\nAdditionally, if you or the AI answer the question wrong the opponent has a chance to steal the question and answer it. There are also three rounds to this game and you will advance"
				+ "\nif you have a higher score than the AI, the AI difficulty will increase each round, otherwise the game will end if you lose to the AI. The first turn will be randomized and it will"
				+ "\neither the AI or user. At the end of each round there will be a wager between the user and AI (person with less money chooses wager amount), and whichever person answers the question"
				+ "\ncorrectly will win the wager amount while the loser will lose the wager amount. Note: before the wager both the players money have to be above $0. Additionally, every time the user or"
				+ "\nAI answers a question there will be a 5% chance of a daily double where the player chooses a amount to wager and if they are correct they win the amount, however if they are wrong they"
				+ "\nlose the amount");//rules

        // Color code for AI answer
        String text = "GREEN"; //make the ai text colour a string
        String greenText = aiColorOutput(text); //use ai colour output method to make the text green
        System.out.println("AI colored Text: " + greenText); //output the ai text colour to user
		System.out.println(" ");
		System.out.println(" ");
		
		System.out.println("Welcome to the Jeopardy game, what is your name?");// ask user for name 
		String userName = myScanner.nextLine(); //store in string variable userName
		
		// Read champion name file to determine if user is return champion 
		String championName = null; //declare championName as a variable
		String championNameFileName = "championNameFile.txt"; //create another variables for the txt file 
		try {
			File championNameFile = new File (championNameFileName); //declare a new championName file
			Scanner championNameScanner = new Scanner (championNameFile); //declare scanenr for the file
			if (championNameScanner.hasNext()) { //find the string in the file (first line)
				championName = championNameScanner.nextLine(); //store in variable championName
			}
			championNameScanner.close(); //close scanner
		} 
		// First time there is no championNameFile so check for exception
		catch (Exception e ) { 
		}
		if (championName != null && championName.equalsIgnoreCase(userName)) { //if the champion name is not null and is equal to the userName 
			System.out.println("WELCOME BACK RETURNING CHAMPION!"); //output this to let user know they are returning champion
		}
		
		// Play again loop
		while (isPlayAgain.equalsIgnoreCase("Y")) {
			String whoIsFirst = null; //reset who is first variable
			
			//Declare total money for user and AI
			int userTotalMoney = 0;
			int aiTotalMoney = 0;
			
			// Declare wager possibility
			int aiWagerCorrectPossibility = 40;
			int dailyDoubleChance = 5; //declare dailydouble possibility
			
			// Three rounds loop
			for (int round = 1; round <= 3; round++) {
				String [] questions = new String [25]; //declare arrays for the questions in the game
				String [] answers = new String [25]; //declare arrays for the answers in the game
				String [] wrongAnswers = new String [25]; //declare arrays for the wronganswers in the game (AI if ai gets it wrong)
				int [] aiCorrectPossibility = null; //declare array for ai correct possibility
				String [] topics = null; //declare the topics
				
				String [] wagerQuestion = new String [1]; //declare array for the wager question at the end of the round
				String [] wagerAnswer = new String [1]; //declare array for wager answer
				String [] wagerWrongAnswer = new String [1]; // declare array for wrong answer for AI
				

				// Initialize for different rounds
				if (round == 1) { //if statement for round 1
					aiCorrectPossibility = new int []{70,60,50,40,20}; // declare the ai possibilty for round 1 (eg 70 percent for $200 60 percent for $400 etc.)
					topics = new String [] {"Animals  ","Movies   ", "Games    ","Geography","Sports   "}; //declare topics into array (spaces because of the game UI to make it equal)
					loadContentToArray ("gameQuestionsRound1.txt", questions, 25); // use method to load content onto the array from file
					loadContentToArray ("gameAnswersRound1.txt", answers, 25); // use method to load content onto the array from file
					loadContentToArray ("gameWrongAnswersRound1.txt", wrongAnswers, 25); // use method to load content onto the array from file
					
					loadContentToArray ("wagerQuestionsRound1.txt", wagerQuestion, 1);// use method to load content onto the array from file
					loadContentToArray ("wagerAnswersRound1.txt", wagerAnswer, 1);// use method to load content onto the array from file
					loadContentToArray ("wagerWrongAnswersRound1.txt", wagerWrongAnswer, 1);// use method to load content onto the array from file
					
					// Determine who play first by random number between 0 and 1
					int randomFirst = (int)(Math.random() * 2);
					if (randomFirst == 0) { 
						whoIsFirst = "user"; //if the random number is 0 the user goes first
					}
					else { 
						whoIsFirst = "ai"; //if the random number is 1 the ai goes first 
					}
					
					System.out.println(aiColorOutput("\r\n"
						+ "█▀█ █▀█ █░█ █▄░█ █▀▄   ▄█\r\n"
						+ "█▀▄ █▄█ █▄█ █░▀█ █▄▀   ░█")); //use colour method to print out round 1
				}
				
				else if (round == 2) { //if statement for round 2
					aiCorrectPossibility = new int []{80,70,60,50,30}; //declare possibilty ai for round 2( 80% for $200, 70 for $400) ai gets smarter each round	
					topics = new String [] {"Food     ", "Music    ", "History  ", "Writings ", "Science  "}; //declare topics with spaces for the game board to be neat
					loadContentToArray ("gameQuestionsRound2.txt", questions, 25); // use method to load content onto arrays
					loadContentToArray ("gameAnswersRound2.txt", answers, 25); // use method to load content onto arrays
					loadContentToArray ("gameWrongAnswersRound2.txt", wrongAnswers, 25); // use method to load content onto arrays
					
					loadContentToArray ("wagerQuestionsRound2.txt", wagerQuestion, 1);// use method to load content onto arrays
					loadContentToArray ("wagerAnswersRound2.txt", wagerAnswer, 1);// use method to load content onto arrays
					loadContentToArray ("wagerWrongAnswersRound2.txt", wagerWrongAnswer, 1);// use method to load content onto arrays
					
					System.out.println(aiColorOutput("\r\n"
							+ "█▀█ █▀█ █░█ █▄░█ █▀▄   ▀█\r\n"
							+ "█▀▄ █▄█ █▄█ █░▀█ █▄▀   █▄"));//use colour method to print out round 1
				} 
				else { //this else statemnet is for round 3
					aiCorrectPossibility = new int [] {90,80,70,60,50};	//declare possibility for ai as this is now higher as the ai is smarter
					topics = new String [] {"Math     ", "Celebrity", "Space    ", "TV       ", "Art      "}; //declare topics with spaces to make gameboard neat
					loadContentToArray ("gameQuestionsRound3.txt", questions, 25);// use method to load content onto arrays
					loadContentToArray ("gameAnswersRound3.txt", answers, 25);// use method to load content onto arrays
					loadContentToArray ("gameWrongAnswersRound3.txt", wrongAnswers, 25);// use method to load content onto arrays
					
					loadContentToArray ("wagerQuestionsRound3.txt", wagerQuestion, 1);// use method to load content onto arrays
					loadContentToArray ("wagerAnswersRound3.txt", wagerAnswer, 1);// use method to load content onto arrays
					loadContentToArray ("wagerWrongAnswersRound3.txt", wagerWrongAnswer, 1);// use method to load content onto arrays
					
					System.out.println(aiColorOutput("\r\n"
							+ "█▀▀ █ █▄░█ ▄▀█ █░░   █▀█ █▀█ █░█ █▄░█ █▀▄\r\n"
							+ "█▀░ █ █░▀█ █▀█ █▄▄   █▀▄ █▄█ █▄█ █░▀█ █▄▀")); //use ascii art with green text for final round
				}
								
				// Declare different levels money amount
				String [] t1Levels = {"$200 ", "$400 ","$600 ", "$800 ", "$1000"}; 
				String [] t2Levels = {"$200 ", "$400 ","$600 ", "$800 ", "$1000"};
				String [] t3Levels = {"$200 ", "$400 ","$600 ", "$800 ", "$1000"};
				String [] t4Levels = {"$200 ", "$400 ","$600 ", "$800 ", "$1000"};
				String [] t5Levels = {"$200 ", "$400 ","$600 ", "$800 ", "$1000"};
				
				// Loop through all questions
				int totalQuestions = 25; //25 questions for each round
				for (int userSelectionCount = 1; userSelectionCount <= totalQuestions; userSelectionCount++) {
					
					displayGameUI (topics, t1Levels, t2Levels, t3Levels, t4Levels, t5Levels); //declare gameUI using method
					
					if (whoIsFirst.equals("user")) { //if statement for if user goes first
						// Game starts with user
						System.out.println(userName + ", it is your turn"); //output message
						
						System.out.println(userName + ", What is the topic/category you would like to answer from 1 to 5 "
								+ "(1- " + topics[0].trim() + ",  2- " + topics[1].trim() + ", 3- " + topics[2].trim() + ", 4- " + topics[3].trim() + ", 5- " + topics[4].trim() + ")?"); //ask user for the topic they would like to choose (trim because there are spaces in the array)
						int userTopic =0;//declare variable of user topic
						
						// Validate user topic 
				        while (true) {
				        	//use try catch to for exception
				            try {
				                userTopic = myScanner.nextInt(); //get user input and store it in user topic
				                break; //break the loop
				            } catch (Exception e) {
				                System.out.println("Invalid input. Please enter a number from 1 to 5.");// ask user again if they enter the wrong number
				                myScanner.nextLine(); //refresh scanner
				            }
				        }
						while (validateTopicInput(userTopic, t1Levels, t2Levels, t3Levels, t4Levels, t5Levels) ) { 
							while (true) {
								try {
									userTopic = myScanner.nextInt(); //store integer in variable
									break;
									//if the integer is too high there will be an exception
						        } catch (Exception e) {
						        	System.out.println("Invalid input. Please enter a number from 1 to 5.");
						        	myScanner.nextLine();
						        }
							}
						}
						//ask user for the money slot/difficulty level they would like to choose 
						System.out.println("What is the money slot/difficulty level you would like to choose from 1 to 5 (1-$200, 2-$400,3-$600,4-$800,5-$1000)?");
						
						// Validate user level
				        int userLevel =0;
				        while (true) {
				        	//use try and catch for exception
				            try {
				                userLevel = myScanner.nextInt();//store user input in variable
				                break;
				            } catch (Exception e) {
				                System.out.println("Invalid input. Please enter a number from 1 to 5."); //if there is exception ask user to reenter number instead of an exception
				                myScanner.nextLine(); //refresh scanner
				            }
				        }
						while (validateLevelInput(userLevel, userTopic, t1Levels, t2Levels, t3Levels, t4Levels, t5Levels) ) { //validate level input using method							
							while (true) {
								try {
									userLevel = myScanner.nextInt(); //declare the user input and use scanner for the next integer
									break;
									//if the integer is too high there will be an exception
						        } catch (Exception e) {
						        	System.out.println("Invalid input. Please enter a number from 1 to 5.");
						        	myScanner.nextLine();
						        }
							}
						}
						myScanner.nextLine();//refresh scanner
						
						// Daily double
						int dailyDoubleWager = 0; //declare variable
						boolean playDailyWager = false; //declare boolean
						if (userTotalMoney > 0) { //only do daily double if the usertotal money is above 0
							int dailyDoubleRandom = (int)(Math.random() * 100) + 1; //declare daily double random into an integer where a random number is generated
							if (dailyDoubleRandom <= dailyDoubleChance) { //if the random is less than the chance the daily double will appear 
								System.out.println(aiColorOutput("\r\n"
										+ "█▀▄ ▄▀█ █ █░░ █▄█   █▀▄ █▀█ █░█ █▄▄ █░░ █▀▀ █\r\n"
										+ "█▄▀ █▀█ █ █▄▄ ░█░   █▄▀ █▄█ █▄█ █▄█ █▄▄ ██▄ ▄")); // create ascii art to make the font bigger and use method to change colour
								System.out.println();
								//print out to console and explain daily double to user
								System.out.println("Welcome to the daily double, before knowing the question you will choose a dollar amount to wager ranging from $1 to the total money you have accumlated. ");
								System.out.println("Please choose money amount you would like to wager from 1 to " + userTotalMoney + "."); //ask user for the money they would like to wager
								playDailyWager = true; //change the boolean to true
						        while (true) {
						            try {
						                dailyDoubleWager = myScanner.nextInt(); //store user input into variable
						                break;
						                //catch the exception if an exception pops up
						            } catch (Exception e) {
						                System.out.println("Invalid input. Please enter a number from 1 to " + userTotalMoney + "."); //let user know it is invalid input 
						                myScanner.nextLine();//refresh scanner
						            }
						        }
								while (dailyDoubleWager <= 0 || dailyDoubleWager > userTotalMoney ) { // check for invalid input
									System.out.println("Invalid input. Please enter a number from 1 to " + userTotalMoney + ".");
									dailyDoubleWager = myScanner.nextInt(); //get user input and store it again as it is invalid
								}
						        myScanner.nextLine(); //refresh scanner
							}
						}
						
						// Get question and answer
						String question = contentFinder (userTopic, userLevel, questions); //use content finder to find the question 
						System.out.println("Below is the question and please answer.");
						System.out.println(question); //ask the user the question
						String userAnswer = myScanner.nextLine(); //get user input and store in user answer
						String correctAnswer = contentFinder (userTopic, userLevel, answers); //find the correct answer in the content finder
						
						// Get money amount for question
						int levelMoney = 0;
						if ( playDailyWager ) { //set level money to daily wager if it is daily double
							levelMoney = dailyDoubleWager;
						} 
						else {
							levelMoney = getLevelMoney(userTopic, userLevel, t1Levels, t2Levels, t3Levels, t4Levels, t5Levels); //if it is not daily double get the level money using method
						}
						// Determine if answer is correct based on keyword percentage
						boolean isCorrectAnswer = validateAnswerByKeywords(userAnswer, correctAnswer);
						
						// Calculate total money based on if answer is correct
						if (isCorrectAnswer) {
							userTotalMoney = userTotalMoney + levelMoney;
							System.out.println("Your answer is correct, there will be " + levelMoney +" dollars added to your account.");
							System.out.println(userName + ", your total money is " + userTotalMoney + " dollars.");
							System.out.println();
						}
						else {
							userTotalMoney = userTotalMoney - levelMoney; //subtract the money if user gets the question wrong
							System.out.println("Your answer is wrong, there will be " + levelMoney +" dollars deducted from your account.");
							System.out.println(userName + ", your total money is " + userTotalMoney + " dollars.");
							
							// User answer is wrong, ask AI if want to answer the same question
							System.out.println("AI, do you want to answer this question?"); //ask ai if they want to answer this questnio
							
							// Use random number to decide
							int aiChoice = (int)(Math.random() * 2);
							if (aiChoice == 0) { //if random number is 0 they answer the question
								System.out.println(aiColorOutput("Yes I would like to answer this question.")); //use method to change colour
								System.out.println("The question is: \"" + question + "\""
										+ "\nPlease answer the question above");
								
								// Use random possibility for AI to get answer
								int aiPossibility = (int) (Math.random() * 100)+1; //randomize number between 1 and 100 to see if ai answers right or wrong
								String aiAnswer = getAIAnswer(userTopic, userLevel, aiCorrectPossibility, aiPossibility, answers, wrongAnswers); //call the method to find the ai answer
								System.out.println("AI has answered: ");
								System.out.println(aiColorOutput(aiAnswer)); //output ai answer using colour
								
								// Calculate money based on defined AI possibility 
								if (aiPossibility <= aiCorrectPossibility[userLevel-1]) { //if statement for to see if ai gets correct or wrong
									System.out.println("Your answer is correct, there will be " + levelMoney +" dollars added to your account.");
									aiTotalMoney = aiTotalMoney + levelMoney;
									System.out.println("AI, your total money is " + aiTotalMoney + " dollars.");
									
									// Switch AI to answer next question because AI got correct answer
									whoIsFirst = "ai";
								}
								else { //if ai answer is wrong deduct money and output message
									System.out.println("Your answer is wrong, there will be " + levelMoney +" dollars deducted from your account.");
									aiTotalMoney = aiTotalMoney - levelMoney;
									System.out.println("AI, your total money is " + aiTotalMoney + " dollars.");
								}
							}
							else { //if ai does not want to answer question statement
								System.out.println(aiColorOutput("No, I would not like to answer this question"));	 //use method to change colour
							}
						} 	
						// Cross out chosen question
						updateLevelArrays(userTopic, userLevel, t1Levels, t2Levels, t3Levels, t4Levels, t5Levels); //update the level arrays with the cross outs xxx
					}
					else if (whoIsFirst.equals("ai")) { //if the who is first is the AI 
						System.out.println("AI, it is your turn"); 
						System.out.println("AI, What is the topic/category you would like to answer from 1 to 5 "
								+ "(1- " + topics[0].trim() + ",  2- " + topics[1].trim() + ", 3- " + topics[2].trim() + ", 4- " + topics[3].trim() + ", 5- " + topics[4].trim() + ")?");//ask user for the topic between 1 and 5
						
						// AI only can choose topic from left over topics using random number
						String leftOverTopicIndex = aiChooseTopic (t1Levels, t2Levels, t3Levels, t4Levels, t5Levels);
						int aiRandomTopicIndex = (int) (Math.random() * leftOverTopicIndex.length() -1); //randomize the left over topics the ai can choose
						String aiTopicString = leftOverTopicIndex.substring(aiRandomTopicIndex, aiRandomTopicIndex +1); //use substring to find the topic
						int aiTopic = Integer.valueOf(aiTopicString); //convert to an integer
						System.out.println(aiColorOutput(aiTopic)); //use the method to change the colour
						
						// AI only can choose topic from left over levels using random number
						System.out.println("What is the money slot/difficulty level you would like to choose from 1 to 5 (1-$200, 2-$400,3-$600,4-$800,5-$1000)?");
						String aiLevelString = aiChooseLevel (aiTopic,t1Levels, t2Levels, t3Levels, t4Levels, t5Levels); //use to method to find the ai level randomize
						int aiLevel = Integer.valueOf(aiLevelString);//find the integer of the string
						System.out.println(aiColorOutput(aiLevel)); //use the colour to output the ai level
						
						// Daily double
						int dailyDoubleWager = 0; //declare the variable for daily double
						boolean playDailyWager = false; //declare variable for the daily wager for AI
						if (aiTotalMoney > 0) { //only do daily double if money is above 0
							int dailyDoubleRandom = (int)(Math.random() * 100) + 1; //randomize number between 1 and 100 to find the chance
							if (dailyDoubleRandom <= dailyDoubleChance) { // if statement for if ai gets correct (random number is less than chance)
								System.out.println(aiColorOutput("\r\n"
										+ "█▀▄ ▄▀█ █ █░░ █▄█   █▀▄ █▀█ █░█ █▄▄ █░░ █▀▀ █\r\n"
										+ "█▄▀ █▀█ █ █▄▄ ░█░   █▄▀ █▄█ █▄█ █▄█ █▄▄ ██▄ ▄")); //print out daily double with colour method
								System.out.println();
								System.out.println("Welcome to the daily double, before knowing the question you will choose a dollar amount to wager ranging from $1 to the total money you have accumlated. ");
								System.out.println("AI, Please choose money amount you would like to wager from 1 to " + aiTotalMoney + "."); //prompt user for the money they would like to wager 
								playDailyWager = true; //change the boolean variable to true 
								dailyDoubleWager = (int)(Math.random() * aiTotalMoney) + 1; //randomize a number between 1 and the total money to find money AI would wager
								System.out.println(aiColorOutput(dailyDoubleWager)); //use the green colour to print out the money wager for AI
							}
						}
						
						// Get question
						String question = contentFinder (aiTopic, aiLevel, questions); //find the question for the AI using content finder method
						System.out.println("Below is the question and please answer.");
						System.out.println(question); //output the question to AI
						
						// Use random possibility for AI to get answer
						int aiPossibility = (int) (Math.random() * 100)+1;
						String aiAnswer = getAIAnswer(aiTopic, aiLevel, aiCorrectPossibility, aiPossibility, answers, wrongAnswers); //get the ai answer using the ai answer method
						System.out.println("AI has answered: ");
						System.out.println(aiColorOutput(aiAnswer)); //output the answer using the colour method
						
						// Get level money amount
						int levelMoney = 0;
						if ( playDailyWager ) { //if statement for daily double wager
							levelMoney = dailyDoubleWager; //change the level money to the daily double wager to add or deduct the wager money user entered
						} 
						else {
							levelMoney = getLevelMoney(aiTopic, aiLevel, t1Levels, t2Levels, t3Levels, t4Levels, t5Levels); //if it is not the daily double get the level money using method
						}
						
						// Calculate money based on defined AI possibility 
						if (aiPossibility <= aiCorrectPossibility[aiLevel-1]) { //if statement for the correct answer
							//output and add the money to AI balance
							System.out.println("Your answer is correct, there will be " + levelMoney +" dollars added to your account.");
							aiTotalMoney = aiTotalMoney + levelMoney;
							System.out.println("AI, your total money is " + aiTotalMoney + " dollars.");	
						}
						else {
							//subtract and output the money to AI balance
							System.out.println("Your answer is wrong, there will be " + levelMoney +" dollars deducted from your account.");
							aiTotalMoney = aiTotalMoney - levelMoney;
							System.out.println("AI, your total money is " + aiTotalMoney + " dollars.");
							
							// AI answer wrong, user can choose if answer the same question
							System.out.println(userName + ", it is your turn. Do you want to answer this question (Y or N)");
							String userChoice = myScanner.nextLine(); //Get user input and store into string variable
							
							// Validate user input if yes or no
							while (validateYesOrNo (userChoice)) {
								userChoice = myScanner.nextLine();
							}
							if (userChoice.equalsIgnoreCase("y")) { //if statement for if the user wants to answer again{
								System.out.println("Let me repeat the question:"
										+ "\n" + question);
								System.out.println(userName + ", what is your answer?");
								String userAnswer = myScanner.nextLine(); //get user answer for the question and store it in variable
								
								// Get correct answer and using keywords possibility to decide if user answer is correct
								String correctAnswer = contentFinder (aiTopic, aiLevel, answers);
								boolean isCorrectAnswer = validateAnswerByKeywords(userAnswer, correctAnswer);
								
								// Calculate user money
								if (isCorrectAnswer) {
									userTotalMoney = userTotalMoney + levelMoney;
									System.out.println("Your answer is correct, there will be " + levelMoney +" dollars added to your account.");
									System.out.println(userName + ", your total money is " + userTotalMoney + " dollars.");
									System.out.println();
									whoIsFirst = "user";
								}
								else {
									//output and subtract the money from the users balance 
									System.out.println("Your answer is wrong, there will be " + levelMoney +" dollars deducted from your account.");
									userTotalMoney = userTotalMoney - levelMoney;
									System.out.println(userName + ", your total money is " + userTotalMoney + " dollars.");
								}
							}
						}
						// Cross out chosen question
						updateLevelArrays(aiTopic, aiLevel, t1Levels, t2Levels, t3Levels, t4Levels, t5Levels);
					}
				}	
				
				// Wager Section only happens when both user and AI has positive money
				if ( userTotalMoney > 0 && aiTotalMoney > 0 ) {
					if (round == 1) { 
						//print wager round 1 with green for round 1
						System.out.println(aiColorOutput("\r\n"
								+ "█░█░█ ▄▀█ █▀▀ █▀▀ █▀█   █▀█ █▀█ █░█ █▄░█ █▀▄   ▄█\r\n"
								+ "▀▄▀▄▀ █▀█ █▄█ ██▄ █▀▄   █▀▄ █▄█ █▄█ █░▀█ █▄▀   ░█"));
					}
					//print wager round 2 with green for round 2
					else if (round == 2) {
						System.out.println(aiColorOutput("\r\n"
								+ "█░█░█ ▄▀█ █▀▀ █▀▀ █▀█   █▀█ █▀█ █░█ █▄░█ █▀▄   ▀█\r\n"
								+ "▀▄▀▄▀ █▀█ █▄█ ██▄ █▀▄   █▀▄ █▄█ █▄█ █░▀█ █▄▀   █▄"));
					}
					//print final wager round with green for round 3
					else if (round == 3) {
						System.out.println(aiColorOutput("\r\n"
								+ "█░█░█ ▄▀█ █▀▀ █▀▀ █▀█   █▀▀ █ █▄░█ ▄▀█ █░░   █▀█ █▀█ █░█ █▄░█ █▀▄\r\n"
								+ "▀▄▀▄▀ █▀█ █▄█ ██▄ █▀▄   █▀░ █ █░▀█ █▀█ █▄▄   █▀▄ █▄█ █▄█ █░▀█ █▄▀"));
					}
					System.out.println( "The person whose account has less money will choose wager amount" );
					
					// Decide wager money to play
					int wagerAmount = 0;
					if ( userTotalMoney < aiTotalMoney) {
						System.out.println( userName + ", please choose wager from 1 to " + userTotalMoney + "."); //ask user for the the wager money they would like to wager
						wagerAmount = getWagerAmount(myScanner, userTotalMoney);
					} 
					else {
						System.out.println( "AI, please choose wager from 1 to " + aiTotalMoney + "."); //ask ai for the the wager money they would like to wager
						wagerAmount = (int)(Math.random() * aiTotalMoney)+ 1; //randomize number for wager amount
						System.out.println(aiColorOutput(wagerAmount)); //output wager amount with colour method
					}
					
					System.out.println( "Using random number to decide who should answer first" );
					int randomFirst = (int)(Math.random() * 2);

					if (randomFirst == 0) { 
						System.out.println( userName + ", you go first, please answer the wager question" ); //if the random number is 0 the user goes first 
						
						// Get wager question and user answers 
						System.out.println( wagerQuestion[0] );
						String userWagerAnswer = myScanner.nextLine(); //use scanner and store user answer into string
						
						// Validate answer based on keywords possibility 
						boolean isCorrectAnswer = validateAnswerByKeywords(userWagerAnswer, wagerAnswer[0]);

						if ( isCorrectAnswer ) {
							// User answered correctly, add money for user and deduct money for AI
							System.out.println("Your answer is correct, there will be " + wagerAmount +" dollars added to your account.");
							userTotalMoney = userTotalMoney + wagerAmount;
							System.out.println(userName + ", your total money is " + userTotalMoney + " dollars.");
							System.out.println();
							System.out.println( "AI, there will be " + wagerAmount +" dollars deducted from your account."); //deduct money for ai and output it
							aiTotalMoney = aiTotalMoney - wagerAmount;
							System.out.println( "AI, your total money is " + aiTotalMoney + " dollars.");
						} 
						else {
							// User answered wrong, AI can continue answer
							System.out.println("Your answer is wrong.");
							System.out.println("AI, it is your turn to answer this question");
							System.out.println(wagerQuestion[0] );
							
							// Use random possibility to decide AI answer
							int aiWagerPossibility = (int) (Math.random() * 100)+1;
							if ( aiWagerPossibility >= aiWagerCorrectPossibility ) {
								// Both user and AI answered wrong, do nothing with money
								System.out.println(aiColorOutput(wagerWrongAnswer[0]));
								System.out.println("Your answer is wrong too.");
							}
							else {
								// AI answered correct, add money for AI and deduct money for user
								System.out.println(aiColorOutput(wagerAnswer[0]));
								System.out.println("Your answer is correct, there will be " + wagerAmount +" dollars added to your account."); //add money to account because answer is correct
								aiTotalMoney = aiTotalMoney + wagerAmount;
								System.out.println("AI, your total money is " + aiTotalMoney + " dollars.");
								System.out.println( userName +" there will be " + wagerAmount +" dollars deducted from your account."); //subtract money from ai account
								userTotalMoney = userTotalMoney - wagerAmount;
								System.out.println( userName + ", your total money is " + userTotalMoney + " dollars.");
							}
						}
					}
					else { 
						System.out.println( "AI, you go first, please answer the wager question" );
						System.out.println( wagerQuestion[0] );
						
						// Use random possibility to decide AI answer
						int aiWagerPossibility = (int) (Math.random() * 100)+1; //use random number for possibility
						if ( aiWagerPossibility >= aiWagerCorrectPossibility ) {
							System.out.println(aiColorOutput(wagerWrongAnswer[0]));
							System.out.println("Your answer is wrong.");
							
							// AI answered wrong, user can continue answer
							System.out.println(userName + ", it is your turn to answer this question");
							System.out.println( wagerQuestion[0] );
							String userWagerAnswer = myScanner.nextLine(); //get user input and store into userWagerAnswer
							
							// Validate answer based on keywords possibility 
							boolean isCorrectAnswer = validateAnswerByKeywords(userWagerAnswer, wagerAnswer[0]);
							if ( isCorrectAnswer ) {
								// User answered correct, add money for user and deduct money for AI
								System.out.println("Your answer is correct, there will be " + wagerAmount +" dollars added to your account.");
								System.out.println(userName + ", your total money is " + userTotalMoney + " dollars.");
								System.out.println();
								userTotalMoney = userTotalMoney + wagerAmount;
								System.out.println( "AI, there will be " + wagerAmount +" dollars deducted from your account."); //deduct money for ai and output to console
								aiTotalMoney = aiTotalMoney - wagerAmount;
								System.out.println( "AI, your total money is " + aiTotalMoney + " dollars.");
							} 
							else {
								// Do nothing because both user and AI answered wrong 
								System.out.println("Your answer is wrong too.");
							}
						}
						else {
							// AI answered correctly, add money for AI and deduct money for user
							System.out.println(aiColorOutput(wagerAnswer[0]));
							System.out.println("Your answer is correct, there will be " + wagerAmount +" dollars added to your account.");
							aiTotalMoney = aiTotalMoney + wagerAmount;
							System.out.println("AI, your total money is " + aiTotalMoney + " dollars.");
							System.out.println( userName +" there will be " + wagerAmount +" dollars deducted from your account."); //deduct money for user and output to console
							userTotalMoney = userTotalMoney - wagerAmount;
							System.out.println( userName + ", your total money is " + userTotalMoney + " dollars.");
						}
					}
				}
				
				// if user has less money than AI in round 1 or round 2, then quit game. Otherwise, go to next round
				if (round ==1 || round == 2) {
					if (userTotalMoney < aiTotalMoney) {
						break;
					}
					else { 
						System.out.println("You have beaten or tied with the AI as you have a score of "
								+ userTotalMoney + " and AI has a score of " + aiTotalMoney + ". Go to the next round!"); //output winning message to user
					}
				}
			}
			
			// Show final result
			if (userTotalMoney > aiTotalMoney) {
				System.out.println( userName + " have beaten the AI as you have a score of " + userTotalMoney 
						+ " and AI has a score of " + aiTotalMoney + ", you have beat the game");
			}
			else if (userTotalMoney == aiTotalMoney) {
				System.out.println(userName + " have the same score as the AI with a score of " + userTotalMoney);
			}
			else {
				System.out.println("The AI has beaten " + userName + " as " + userName + " have a score of "
							+ userTotalMoney + " and AI has a score of " + aiTotalMoney);
			} 
			
			// Write score ranking to gameScores file, user score after user name
			String fileName = "gameScores.txt";
			PrintWriter scoreWriter = new PrintWriter(new FileWriter(fileName, true)); //create new printwriter
			scoreWriter.println(userName); //print the user name in file
			scoreWriter.println(userTotalMoney); //print the money in file
			scoreWriter.close(); //close printwriter
			
			// Get total scores to determine array size
			int scoreFileLineCount = getFileLineCounts(fileName);
			
			// Put user name and score in different arrays
			String [] userNames = new String [scoreFileLineCount/2];
			int [] userScores = new int [scoreFileLineCount/2];

			
			// Bubble sort the array based on user scores and print out ranking
			sortUserScores(fileName, userNames, userScores);
			System.out.println("\r\n"
					+ "█░░ █▀▀ ▄▀█ █▀▄ █▀▀ █▀█ █▄▄ █▀█ ▄▀█ █▀█ █▀▄ ▀\r\n"
					+ "█▄▄ ██▄ █▀█ █▄▀ ██▄ █▀▄ █▄█ █▄█ █▀█ █▀▄ █▄▀ ▄");
			for (int i = 0; i < userScores.length; i++) {
				System.out.println((i+1) + ". " + userNames [i] + " has a score of $" + userScores[i]); //print out leaderboard with the number place in front
			}
			
			// Write champion user name to champion file
			PrintWriter championNameWriter = new PrintWriter(new FileWriter(championNameFileName, false)); //create a champion name writer with the boolean as false to replace
			championNameWriter.println(userNames[0]); //print the first user name with highest score into the file (winner)
			championNameWriter.close(); //close prinwriter
			
			// Ask if user wants to play again
			System.out.println("Do you want to play again (Y or N)?");
			isPlayAgain = myScanner.nextLine();
			while (validateYesOrNo(isPlayAgain)) { //validate using method
				isPlayAgain = myScanner.nextLine();
			} 
			if (isPlayAgain.equalsIgnoreCase("N")) {
				System.out.println("Game Over!"); //game over if user does not want to play anymore
				break;
			}	
		}
	}
	
	// Load questions, answers, wrong answers to array 
	public static void loadContentToArray(String fileName, String [] content, int lineNumber ) throws Exception{
		File contentFile = new File (fileName); //create new file 
		Scanner myFileScanner = new Scanner (contentFile);
		while (myFileScanner.hasNext()) { //use scanner to read file
			for (int i = 0; i < lineNumber; i++) {
				content [i] = myFileScanner.nextLine(); //loop the file content and add to array
			}
		}
		myFileScanner.close(); //close scanner
	}
	
	// Display game console
	public static void displayGameUI (String [] topics, String [] t1Levels, String [] t2Levels, String [] t3Levels, String [] t4Levels, String [] t5Levels) {
		System.out.println("_________________________________________________________________________________");
		System.out.println();
		for (int i = 0; i < topics.length; i++) { //add topics into game UI using loop
			System.out.print("|   " + topics[i] + "   ");
		}
		//add levels to game UI using loop
		System.out.print("|");
		System.out.println();
		System.out.println("|_______________|_______________|_______________|_______________|_______________|");
		for (int i = 0; i < topics.length; i++) {
			System.out.println("|_____" + t1Levels[i] + "_____|_____" + t2Levels[i] + "_____|_____"  + t3Levels[i] + "_____|_____" + t4Levels[i] + "_____|_____"+ t5Levels[i] + "_____|");
		}
	}
	
	// Validate topic input as well as if topic has been crossed out
	public static boolean validateTopicInput (int topic, String[] t1Levels, String[] t2Levels, String[] t3Levels, String[] t4Levels, String[] t5Levels){
		//check each of the topics if all the levels are crossed out
		boolean allowT1 = isTopicCrossout(t1Levels);
		boolean allowT2 = isTopicCrossout(t2Levels);
		boolean allowT3 = isTopicCrossout(t3Levels);
		boolean allowT4 = isTopicCrossout(t4Levels);
		boolean allowT5 = isTopicCrossout(t5Levels);
		boolean isWrongInput = false;

		// Check user input if it's 1, 2, 3, 4, or 5
		if ( topic != 1 && topic != 2  && topic != 3 && topic != 4 && topic != 5 ) {
			System.out.println("Please retry, choose from 1 to 5");
			isWrongInput = true;
		} 
		// Check if topic is available
		else if ( (topic == 1 && !allowT1) ||
				(topic == 2 && !allowT2) ||
				(topic == 3 && !allowT3) ||
				(topic == 4 && !allowT4) ||
				(topic == 5 && !allowT5)) {
			System.out.println("Choice is not available, please choose another topic.");
			isWrongInput = true;
		}
		return isWrongInput;
	}
	
	// Check if topic is crossed out
	public static boolean isTopicCrossout(String[] levels) {
		boolean allow = false; 
		for(int i = 0; i < levels.length; i++) { //loop through levels and check if all levels are crossed out
			if (!levels[i].equals(CROSSOUT)) {
				allow = true;
				break;
			}
		}
		return allow;
	}
		
	// Validate level input as well as if level has been crossed out
	public static boolean validateLevelInput (int level, int topic, String[] t1Levels, String[] t2Levels, String[] t3Levels, String[] t4Levels, String[] t5Levels){
		boolean isWrongInput = false;

		// Check user input if it is between 1 and 5
		if ( level!= 1 && level!= 2 && level!= 3 && level!= 4 && level!= 5) {
			System.out.println("Please retry, choose from 1 to 5");
			isWrongInput = true;
		} 
		// Check if level is available to see if it not crossed out
		else if ( (topic == 1 && t1Levels[level-1].equals(CROSSOUT) ) ||
				(topic == 2 && t2Levels[level-1].equals(CROSSOUT)) ||
				(topic == 3 && t3Levels[level-1].equals(CROSSOUT)) ||
				(topic == 4 && t4Levels[level-1].equals(CROSSOUT)) ||
				(topic == 5 && t5Levels[level-1].equals(CROSSOUT))) {
			System.out.println("Choice is not available, please choose another difficulty level, please choose from 1 to 5 (1-$200, 2-$400,3-$600,4-$800,5-$1000).");
			isWrongInput = true;
		}
		return isWrongInput;
	}
	
	// Using index to find question, answer and wrong answer based on topic and level
	public static String contentFinder (int topic, int level, String[] contentArray){
		int contentIndex = (topic - 1) * 5 + level - 1; //formula to get content index in the array
		return contentArray [contentIndex];
	}
	
	// Get money amount integer with topic and level
	public static int getLevelMoney (int topic, int level,  String[] t1Levels, String[] t2Levels, String[] t3Levels, String[] t4Levels, String[] t5Levels) {
		String levelMoneyString=""; //declare variable
		int levelMoney;
		//get money amount string based on topics and level
		if (topic ==1) {
			levelMoneyString =t1Levels[level-1];
		}
		else if (topic ==2) {
			levelMoneyString =t2Levels[level-1];
		}
		else if (topic ==3) {
			levelMoneyString =t3Levels[level-1];
		}
		else if (topic ==4) {
			levelMoneyString =t4Levels[level-1];
		}
		else if (topic ==5) {
			levelMoneyString =t5Levels[level-1];
		}
		
		levelMoney = Integer.valueOf(levelMoneyString.trim().substring(1)); //convert string to integer and trim the spaces
		return levelMoney;
	}
	
	// validate answer with correct answer based on keywords matching percentage
    public static boolean validateAnswerByKeywords(String answer, String correctAnswer) {
        String[] answerArray = getWordsArray(answer); //separate each word in the answer string and store it into array
        String[] correctAnswerArray = getWordsArray(correctAnswer); //separate each word in the correct answer string and store it into array

        int commonWords = countCommonWords(answerArray, correctAnswerArray); //get the common words in both arrays
        double percentage = (double) commonWords / correctAnswerArray.length * 100; //get the percentage of the common words in correct answer

        return percentage > KEYWORD_MATCH_PERCENTAGE; //return boolean if more than 65% 
    }

    // Separate words from answer to a array without duplicated
    public static String[] getWordsArray(String input) {
        String[] wordsArray = new String[countWords(input)]; //define array based on how many words are in the input string
        int index = 0; //declare index variable
        boolean inWord = false; //declare boolean
        int start = 0; //declare start index variable
        
        for (int i = 0; i < input.length(); i++) { //loop through input
            char currentChar = input.charAt(i); //get each character from the user answer

            //if character switches from the space to word it is a word 
            if (currentChar == ' ') {
                if (inWord) {
                    addUniqueWord(wordsArray, input.substring(start, i), index); //call method to remove the duplicate
                    index++;
                    inWord = false; //set to false if character is a space
                }
            } 
            else {
                if (!inWord) {
                    start = i;
                    inWord = true; //set to true if character is inside word
                }
            }
        }

        if (inWord) { //add the last word into the array
            addUniqueWord(wordsArray, input.substring(start), index); //call method to remove duplicates
        }

        return wordsArray; //return the array
    }

    // count words in answer use to decide array size, considered multiple spaces as delimiter
    public static int countWords(String input) {
        int count = 0; //declare count variable
        boolean inWord = false; //declare boolean variable if it's in the word

        for (int i = 0; i < input.length(); i++) { //loop through the input length and get each character
            char currentChar = input.charAt(i);

            if (currentChar == ' ') { //not in the word if its a space
                inWord = false;
            } 
            else if (!inWord) { //if it switches between the space and word then add the count 
                count++;
                inWord = true;
            }
        }
        return count;
    }

    // if duplicated work, not add to array
    public static void addUniqueWord(String[] wordsArray, String word, int index) {
        for (int i = 0; i < index; i++) { //loop through word array
            if (wordsArray[i].equalsIgnoreCase(word)) { //check if there is any duplicates 
                return; //ignore duplicates
            }
        }
        wordsArray[index] = word; //if it is not duplicate add it 
    }

    // Check how many common words in answer and correct answer
    public static int countCommonWords(String[] array1, String[] array2) {
        int count = 0; //declare count variable 

        for (int i = 0; i < array1.length; i++) { //loop through array1 
        	if ( array1[i] != null)  {
	        	for (int j = 0; j < array2.length; j++) { //loop through array 2 
	                if (array2[j] != null && array2[j].equalsIgnoreCase(array1[i])) { //if array one is equal to array two add the count
	                	count++; //add the count by 1
	                	break; //break
	                }
	            }
        	}
        }
        return count;
    }
	
	// Get AI answer based on topic and level as well, either correct or wrong
	public static String getAIAnswer (int topic, int level, int[] aiCorrectPossibility, int aiPossibility, String[] answers, String[] wrongAnswers) {
		String aiAnswer; //declare variable

		int aiAnswerIndex = (topic - 1) * 5 + level -1;  //use formula to find the ai answer from the array
		if (aiPossibility > aiCorrectPossibility[level - 1]){ //if the randomize number is above the possibility then it is wrong
			aiAnswer = wrongAnswers [aiAnswerIndex]; //get answer from wrong answer array
		}
		else {//if the randomize number is below the possibility then it is right
			aiAnswer = answers [aiAnswerIndex]; //get answer from correct answer array
		}
		return aiAnswer; //return the ai answer
	}
	
	// Cross out question based on topic and level
	public static void updateLevelArrays(int topic, int level, String[] t1Levels, String[] t2Levels, String [] t3Levels, String [] t4Levels, String[] t5Levels) {
		//update the game UI and cross the money slot out if the user or AI has already answered the level question
		if (topic ==1) {
			t1Levels[level-1] = CROSSOUT;
		}
		else if (topic ==2) {
			t2Levels[level-1] = CROSSOUT;
		}
		else if (topic ==3) {
			t3Levels[level-1] = CROSSOUT;
		}
		else if (topic ==4) {
			t4Levels[level-1] = CROSSOUT;
		}
		else if (topic ==5) {
			t5Levels[level-1] = CROSSOUT;
		}
	}
	
	// AI chooses topic, considered the topics that has been crossed out, using random index
	public static String aiChooseTopic(String[] t1Levels, String[] t2Levels, String [] t3Levels, String [] t4Levels, String[] t5Levels) {
		String leftOverTopicIndexs = ""; //declare variable
		//loop through each topic to check if all the question levels have been crossed out
		//add all the non-crossed out topics into a string 
		for(int i = 0; i < t1Levels.length; i++) {
			if (!t1Levels[i].equals(CROSSOUT)) { 
				leftOverTopicIndexs = leftOverTopicIndexs + "1";
				break;
			}
		}
		for(int i = 0; i < t2Levels.length; i++) {
			if (!t2Levels[i].equals(CROSSOUT)) {
				leftOverTopicIndexs = leftOverTopicIndexs + "2";
				break;
			}
		}
		for(int i = 0; i < t3Levels.length; i++) {
			if (!t3Levels[i].equals(CROSSOUT)) {
				leftOverTopicIndexs = leftOverTopicIndexs + "3";
				break;
			}
		}
		for(int i = 0; i < t4Levels.length; i++) {
			if (!t4Levels[i].equals(CROSSOUT)) {
				leftOverTopicIndexs = leftOverTopicIndexs + "4";
				break;
			}
		}
		for(int i = 0; i < t5Levels.length; i++) {
			if (!t5Levels[i].equals(CROSSOUT)) {
				leftOverTopicIndexs = leftOverTopicIndexs + "5";
				break;
			}
		}
		return leftOverTopicIndexs;
	}
	
	// AI chooses topic, considered the leftover levels that has been crossed out, using random index
	public static String aiChooseLevel(int topic, String[] t1Levels, String[] t2Levels, String [] t3Levels, String [] t4Levels, String[] t5Levels) {
		String aiLevelString = ""; //declare variable
		int aiRandomLevelIndex = 0; //declare random level index
		String leftOverLevelIndex = ""; //declare left over index variable
		//for chosen topics get the left over levels string and use random number to get the index of the string
		//use index and substring to get the real level
		if ( topic == 1) {
			leftOverLevelIndex = getLeftOverLevelIndex (t1Levels);
			aiRandomLevelIndex = (int) (Math.random() * leftOverLevelIndex.length() -1);
			aiLevelString = leftOverLevelIndex.substring(aiRandomLevelIndex, aiRandomLevelIndex + 1);
		} 
		else if ( topic == 2) {
			leftOverLevelIndex = getLeftOverLevelIndex (t2Levels);
			aiRandomLevelIndex = (int) (Math.random() * leftOverLevelIndex.length() -1);
			aiLevelString = leftOverLevelIndex.substring(aiRandomLevelIndex, aiRandomLevelIndex + 1);
		} 
		else if ( topic == 3) {
			leftOverLevelIndex = getLeftOverLevelIndex (t3Levels);
			aiRandomLevelIndex = (int) (Math.random() * leftOverLevelIndex.length() -1);
			aiLevelString = leftOverLevelIndex.substring(aiRandomLevelIndex, aiRandomLevelIndex + 1);
		} 
		else if ( topic == 4) {
			leftOverLevelIndex = getLeftOverLevelIndex (t4Levels);
			aiRandomLevelIndex = (int) (Math.random() * leftOverLevelIndex.length() -1);
			aiLevelString = leftOverLevelIndex.substring(aiRandomLevelIndex, aiRandomLevelIndex + 1);
		} 
		else if ( topic == 5) {
			leftOverLevelIndex = getLeftOverLevelIndex (t5Levels);
			aiRandomLevelIndex = (int) (Math.random() * leftOverLevelIndex.length() -1);
			aiLevelString = leftOverLevelIndex.substring(aiRandomLevelIndex, aiRandomLevelIndex + 1);
		} 
		return aiLevelString; //return the random level string 
	}
	
	// get leftover level index
	public static String getLeftOverLevelIndex(String[] levels){
		String leftOverLevelIndex = ""; //declare variable for the left over level index
		for(int i = 0; i < levels.length; i++) { //loop through levels and check if it is not equal to cross out
			if (!levels[i].equals(CROSSOUT)) {
				leftOverLevelIndex = leftOverLevelIndex + (i+1); //add this level into an String 
			}
		}
		return leftOverLevelIndex;
	}
	
	// Validate user input if yes or no
	public static boolean validateYesOrNo(String input) {
		boolean isWrongInput = false; //declare boolean as false
		if (!input.equalsIgnoreCase("y") && !input.equalsIgnoreCase("n")) { //if statement for if the input is not y or n
			System.out.println("Please retry, answer with Y or N"); //prompt user to retry
			isWrongInput = true; //change boolean to true
		}
		return isWrongInput; //return boolean
	}
	
	// get score file line number as array size
	public static int getFileLineCounts (String fileName) throws Exception{
		File fileObject = new File(fileName); //create new file
		Scanner fileScanner = new Scanner(fileObject); //create new scanner for the file
		int lineCount =0; //declare line count
		while (fileScanner.hasNext()) { //scan through file 
			lineCount++; //add the line count number
			fileScanner.nextLine(); //read next line
		}
		fileScanner.close(); //close scanner
		return lineCount; //return the line count
	}
	
	// Bubble sort user score and name
	public static void sortUserScores (String fileName, String[] userNames, int[] userScores) throws Exception{
		File fileObject = new File(fileName); //create new file
		Scanner fileScanner = new Scanner(fileObject); //create new scanner
		int count = 0; //declare counter variable
		while (fileScanner.hasNext()) { //scan through the file
			userNames[count] = fileScanner.nextLine();  //first line is the user name and put into the array
			userScores[count] = Integer.valueOf(fileScanner.nextLine()); //second line is score and convert to integer (store/put into array)
			count++; //add the count
		}
		//switch the user scores and name, if the first score is less than second score switch them as there is a leaderbaord from greatest to least
		for (int i = 1; i < userScores.length; i++) {
			for (int j = 0; j < userScores.length - i; j++) {
				if (userScores[j] < userScores [j+1]) {
					int score = userScores [j];
					userScores[j] = userScores [j+1];
					userScores[j+1] = score;
					String name = userNames[j];
					userNames[j] = userNames[j+1];
					userNames[j+1] = name;
				}
			}
		}
		fileScanner.close(); //close scanner
	}
	
	// Make AI answer green color
	public static String aiColorOutput (String output) {
		//https://stackoverflow.com/questions/5762491/how-to-print-color-in-console-using-system-out-println
		//Colour codes to differentiate the AI and the user, uses the colour codes and output to change the colour of the text
        String greenColorCode = "\u001B[32m";
        String resetColorCode = "\u001B[0m";
		return greenColorCode + output + resetColorCode;
	}
	
	// Make AI answer green color for integer
	public static String aiColorOutput (int output) {
        String greenColorCode = "\u001B[32m";
        String resetColorCode = "\u001B[0m";
		return greenColorCode + output + resetColorCode;
	}

	// Get wager money amount also validate user input
	public static int getWagerAmount(Scanner myScanner, int totalMoney ) {
		int wagerAmount = 0; //declare wager amount
		//use try catch to avoid exception
        while (true) {
            try {
            	wagerAmount = myScanner.nextInt();//store the user input into variable as wager amount
                break;
            } catch (Exception e) {
                System.out.println("Invalid input. Please enter a number from 1 to " + totalMoney + "."); //tell user that it is invalid
                myScanner.nextLine(); //refresh scanner
            }
        }
		while (wagerAmount <= 0 || wagerAmount > totalMoney ) { //check if it is between 1 and the total money amount
			System.out.println("Invalid input. Please enter a number from 1 to " + totalMoney + "."); //invalid if it is not
			wagerAmount = myScanner.nextInt(); //let user input and store into variable
		}
		myScanner.nextLine(); //refresh
		return wagerAmount; //return the wager amount
	}
}