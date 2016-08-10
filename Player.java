import java.util.Random;
import java.util.Scanner;

/**
 * The Player class represents a player in the Nim game, producing Moves as a 
 * response to a Board state. Each player is initialized with a type, either 
 * human or one of several computer strategies, which defines the move he 
 * produces when given a board in some state. The heuristic strategy of the 
 * player is already implemented. You are required to implement the rest of 
 * the player types according to the exercise description.
 * @author OOP course staff
 */
public class Player {

	//Constants that represent the different players.
	/** The constant integer representing the Random player type. */
	public static final int RANDOM = 1;
	/** The constant integer representing the Heuristic player type. */
	public static final int HEURISTIC = 2;
	/** The constant integer representing the Smart player type. */
	public static final int SMART = 3;
	/** The constant integer representing the Human player type. */
	public static final int HUMAN = 4;
	
	//Used by produceHeuristicMove() for binary representation of board rows.
	private static final int BINARY_LENGTH = 3;
	
	private final int playerType;
	private final int playerId;
	private Scanner scanner;
	public static final int LAST_STICK = 1, EVEN = 0, ODD = 1, 
			THREE_STICKS = 3, FIRST = 1, NONE = -1;

	/**
	 * Initializes a new player of the given type and the given id, and an 
	 * initialized scanner.
	 * @param type The type of the player to create.
	 * @param id The id of the player (either 1 or 2).
	 * @param inputScanner The Scanner object through which to get user input
	 * for the Human player type. 
	 */
	public Player(int type, int id, Scanner inputScanner){		
		// Check for legal player type (we will see better ways to do this 
		//in the future).
		if(type != RANDOM && type != HEURISTIC 
				&& type != SMART && type != HUMAN){
			System.out.println("Received an unknown player type as a parameter"
					+ " in Player constructor. Terminating.");
			System.exit(-1);
		}		
		playerType = type;	
		playerId = id;
		scanner = inputScanner;
	}
	
	/**
	 * @return an integer matching the player type.
	 */	
	public int getPlayerType(){
		return playerType;
	}
	
	/**
	 * @return the players id number.
	 */	
	public int getPlayerId(){
		return playerId;
	}
	
	
	/**
	 * @return a String matching the player type.
	 */
	public String getTypeName(){
		switch(playerType){
			
			case RANDOM:
				return "Random";			    
	
			case SMART: 
				return "Smart";	
				
			case HEURISTIC:
				return "Heuristic";
				
			case HUMAN:			
				return "Human";
		}
		//Because we checked for legal player types in the
		//constructor, this line shouldn't be reachable.
		return "UnkownPlayerType";
	}
	
	/**
	 * This method encapsulates all the reasoning of the player about the game. 
	 * The player is given the board object, and is required to return his 
	 * next move on the board. The choice of the move depends on the type of 
	 * the player: a human player chooses his move manually; the random player
	 * should return some random move; the Smart player can represent any 
	 * reasonable strategy; the Heuristic player uses a strong heuristic to 
	 * choose a move. 
	 * @param board - a Board object representing the current state of the 
	 * game.
	 * @return a Move object representing the move that the current player 
	 * will play according to his strategy.
	 */
	public Move produceMove(Board board){
		
		switch(playerType){
		
			case RANDOM:
				return produceRandomMove(board);				
				    
			case SMART: 
				return produceSmartMove(board);
				
			case HEURISTIC:
				return produceHeuristicMove(board);
				
			case HUMAN:
				return produceHumanMove(board);

			//Because we checked for legal player types in the
			//constructor, this line shouldn't be reachable.
			default: 
				return null;			
		}
	}
	
	// Checks if the row has sticks in it or not
	private boolean isRowEmpty(int row, Board board){
		
		int length = board.getRowLength(row);
		for(int stickNum = 1; stickNum<length+1; stickNum++){
			if(board.isStickUnmarked(row, stickNum)){
				return false;				
			}
		}
		return true;
	}	
	
	
	/*
	 * Produces a random move. takes each time a different stick from a 
	 * different row. 
	 */
	private Move produceRandomMove(Board board){
		
		Random myRandom = new Random();
		int row = myRandom.nextInt(board.getNumberOfRows())+1;
		//shuffle and chose a random row
		while(isRowEmpty(row,board)){ 
			row = myRandom.nextInt(board.getNumberOfRows())+1;				
		}//shuffle and chose a random left bound	
		int length = board.getRowLength(row);
		int leftBound = myRandom.nextInt(length)+1;
		while(!board.isStickUnmarked(row, leftBound)){
			leftBound = myRandom.nextInt(length)+1;			
		}//shuffle and chose a random right bound
		int rightBound = myRandom.nextInt(length-leftBound+1)+leftBound;
		while(!board.isStickUnmarked(row, rightBound)){
			rightBound = myRandom.nextInt(length-leftBound+1)+leftBound;						
		}
		int left = leftBound +1;
		while(rightBound > left){			
			if(board.isStickUnmarked(row, left)){
				left++;
			}
			else{
				leftBound = myRandom.nextInt(rightBound)+left+1;
				left++;
			}						
		}
		return new Move(row,leftBound,rightBound);									
	}
			
		
	/*
	 * Produce some intelligent strategy to produce a move
	 */
	private Move produceSmartMove(Board board){	
		int numsticks = board.getNumberOfUnmarkedSticks();
		if(numsticks == LAST_STICK){
			return getStick(board);
		}
		if(numsticks%2 == EVEN){
			return getStick(board);
		}
		if(numsticks%2 == ODD){
			if(numsticks == THREE_STICKS){
				return getStick(board);
			}
			return getTwoSticks(board);			
		}
		return new Move(-1,-1,-1);
	}	
		
	// finds the last stick in the board, then returns the row the stick was
	// found in
	private int findStick(Board board){
		int row = FIRST;
		while(row <= board.getNumberOfRows()){
			if(isRowEmpty(row, board)){
				row++;
			}
			else{
				return row;
			}
		}
		return NONE;				
	}
	//returns a move with the chosen stick. 
	private Move getStick(Board board){
		int row = findStick(board);
		if(row != NONE){
			for(int stick = 1; stick<=board.getRowLength(row); stick++){
				if(board.isStickUnmarked(row,stick)){
					return new Move(row,stick,stick);
				}
			}
			return new Move(-1,-1,-1);
		}
		return new Move(-1,-1,-1);
	}
		
	
	//returns a move with the chosen two sticks. 
	private Move getTwoSticks(Board board){
		
		int row = FIRST;
		while(row <= board.getNumberOfRows()){
			if(isRowEmpty(row, board)){
				row++;
			}
			else{
				int stick = findTwoSticks(board, row);
				if(stick== NONE){
					row++;
				}
				else{
					return new Move(row,stick,stick+1);
				}
			}
		}
		return new Move(-1,-1,-1);
	}
	
	
	private int findTwoSticks(Board board,int row){
		
		int length = board.getRowLength(row);
		for(int stick = 1; stick<=length; stick++){
			if(board.isStickUnmarked(row,stick)){
				if(board.isStickUnmarked(row,stick+1)){
					return stick;
				}	
			}
		}
		return NONE;	
	}
		
	
	/*
	 * Interact with the user to produce his move.
	 */
	private Move produceHumanMove(Board board){
		
		boolean flag = true;		
		while(flag){
			System.out.println("Press 1 to display the board. Press 2 to make "
					+ "a move:");
			int input = scanner.nextInt();
			switch(input)
			{
			case 1:
				System.out.println(board);
				break;
				
			case 2:
				System.out.println("Enter the row number:");
				int row = scanner.nextInt();
				System.out.println("Enter the index of the leftmost stick:");
				int leftBound = scanner.nextInt();
				System.out.println("Enter the index of the rightmost stick:");
				int rightBound = scanner.nextInt();
				return new Move(row,leftBound,rightBound);
				
			default: 	
				System.out.println("Unknown input.");
			}
		}
		return null;				
	}
	
	
	/*
	 * Uses a winning heuristic for the Nim game to produce a move.
	 */
	private Move produceHeuristicMove(Board board){

		if(board == null){
			return null;
		}
	
		int numRows = board.getNumberOfRows();
		int[][] bins = new int[numRows][BINARY_LENGTH];
		int[] binarySum = new int[BINARY_LENGTH];
		int bitIndex,higherThenOne=0,totalOnes=0,lastRow=0,lastLeft=0,
				lastSize=0,lastOneRow=0,lastOneLeft=0;
		
		for(bitIndex = 0;bitIndex<BINARY_LENGTH;bitIndex++){
			binarySum[bitIndex] = 0;
		}
		
		for(int k=0;k<numRows;k++){
			
			int curRowLength = board.getRowLength(k+1);
			int i = 0;
			int numOnes = 0;
			
			for(bitIndex = 0;bitIndex<BINARY_LENGTH;bitIndex++){
				bins[k][bitIndex] = 0;
			}
			
			do {
				if(i<curRowLength && board.isStickUnmarked(k+1,i+1) ){
					numOnes++;
				} else {
					
					if(numOnes>0){
						
						String curNum = Integer.toBinaryString(numOnes);
						while(curNum.length()<BINARY_LENGTH){
							curNum = "0" + curNum;
						}//Convert from char to int
						for(bitIndex = 0;bitIndex<BINARY_LENGTH;bitIndex++){
							bins[k][bitIndex] += curNum.charAt(bitIndex)-'0'; 
						}
						
						if(numOnes>1){
							higherThenOne++;
							lastRow = k +1;
							lastLeft = i - numOnes + 1;
							lastSize = numOnes;
						} else {
							totalOnes++;
						}
						lastOneRow = k+1;
						lastOneLeft = i;
						
						numOnes = 0;
					}
				}
				i++;
			}while(i<=curRowLength);
			
			for(bitIndex = 0;bitIndex<BINARY_LENGTH;bitIndex++){
				binarySum[bitIndex] = (binarySum[bitIndex]+bins[k][bitIndex])%2;
			}
		}
		
		
		//We only have single sticks
		if(higherThenOne==0){
			return new Move(lastOneRow,lastOneLeft,lastOneLeft);
		}
		
		//We are at a finishing state				
		if(higherThenOne<=1){
			
			if(totalOnes == 0){
				return new Move(lastRow,lastLeft,lastLeft+(lastSize-1) - 1);
			} else {
				return new Move(lastRow,lastLeft,lastLeft+(lastSize-1)-
						(1-totalOnes%2));
			}
			
		}
		
		for(bitIndex = 0;bitIndex<BINARY_LENGTH-1;bitIndex++){
			
			if(binarySum[bitIndex]>0){
				
				int finalSum = 0,eraseRow = 0,eraseSize = 0,numRemove = 0;
				for(int k=0;k<numRows;k++){
					
					if(bins[k][bitIndex]>0){
						eraseRow = k+1;
						eraseSize = (int)Math.pow(2,BINARY_LENGTH-bitIndex-1);
						
						for(int b2 = bitIndex+1;b2<BINARY_LENGTH;b2++){
							
							if(binarySum[b2]>0){
								
								if(bins[k][b2]==0){
									finalSum = finalSum + 
											(int)Math.pow(2,BINARY_LENGTH-b2-1);
								} else {
									finalSum = finalSum - 
											(int)Math.pow(2,BINARY_LENGTH-b2-1);
								}
								
							}
							
						}
						break;
					}
				}
				
				numRemove = eraseSize - finalSum;
				
				//Now we find that part and remove from it the required piece
				int numOnes=0,i=0;
				//while(numOnes<eraseSize){
				while(numOnes<numRemove && i<board.getRowLength(eraseRow)){

					if(board.isStickUnmarked(eraseRow,i+1)){
						numOnes++;
					} else {
						numOnes=0;
					}
					i++;
					
				}
				
				//This is the case that we cannot perform a smart move because 
				//there are marked Sticks in the middle
				if(numOnes == numRemove){
					return new Move(eraseRow,i-numOnes+1,i-numOnes+numRemove);
				} else {
					return new Move(lastRow,lastLeft,lastLeft);
				}
				
			}
		}
		
		//If we reached here, and the board is not symmetric, then we only 
		//need to erase a single stick
		if(binarySum[BINARY_LENGTH-1]>0){
			return new Move(lastOneRow,lastOneLeft,lastOneLeft);
		}
		
		//If we reached here, it means that the board is already symmetric,
		//and then we simply mark one stick from the last sequence we saw:
		return new Move(lastRow,lastLeft,lastLeft);		
	}
	
	
}
