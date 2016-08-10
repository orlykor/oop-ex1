
import java.util.Scanner;

/**
 * The Competition class represents a Nim competition between two players, 
 * consisting of a given number of rounds. 
 * It also keeps track of the number of victories of each player.
 */
public class Competition {
	
	private Player player1, player2;
	boolean message;
	private int score1, score2, playerId;
	private Board board;
	static final int NONE = -1, LAST = 1, PID1 = 1, PID2 = 2, TYPE4 = 4;
	/*
	 * Receives two Player objects, representing the two competing opponents, 
	 * and a flag determining whether messages should be displayed.
	 */
	Competition(Player player1, Player player2, boolean displayMessage)
	{
		this.player1 = player1;
		this.player2 = player2;
		message = displayMessage;
		
	}
	
	//Returns the number of victories of a player. 
	public int getPlayerScore(int playerPosition){
		
		switch(playerPosition)
		{
		case 1:
			return score1;
		case 2:
			return score2;
		}
		return NONE;
	}

	// Runs the competition for "numberOfRounds" rounds.
	public void playMultipleRounds(int numberOfRounds){
		
		for(int i = 0; i<numberOfRounds; i++){
			this.singleRound();	
		}
		
		System.out.println("The results are "+score1+":"+score2);		
	}
	

	/*
	 * Returns the integer representing the type of the player; returns -1 on 
	 * bad input.
	 */
	private static int parsePlayerType(String[] args,int index){
		try{
			return Integer.parseInt(args[index]);
		} catch (Exception E){
			return NONE;
		}
	}
	
	/*
	 * Returns the integer representing the type of player 2; returns -1 on 
	 * bad input.
	*/ 
	private static int parseNumberOfGames(String[] args){
		try{
			return Integer.parseInt(args[2]);
		} catch (Exception E){
			return NONE;
		}
	}

	/*represent the move the player made. considering the valid and invalid
	 * moves.
	 */
	private boolean turn(boolean turn, Player player){
		
		if(message){
			System.out.println("Player "+player.getPlayerId()+
					", it is now your turn!");
		}
		Move move = player.produceMove(board); //now the player makes a move
		
			while(board.markStickSequence(move)!= LAST){
				
				if(message){
					System.out.println("Invalid move. Enter another:");
				}
				move = player.produceMove(board); //makes another move
			}
				
			if(message){ 
				System.out.println("Player "+player.getPlayerId()+
						" made the move: "+move);
			}
			return !turn;
	}
		
	
	//this method makes a single round of the game
	private void singleRound(){	
		
		this.board = new Board(); //initialize a new board
		boolean turn = false;
		this.playerId = PID1;
		
		if(message){
			System.out.println("Welcome to the sticks game!");
		}	
		
		while(board.getNumberOfUnmarkedSticks() > 0){
			if(!turn){
				this.playerId = PID2;
				turn = this.turn(turn,player1);
			}
			else if(turn){
				this.playerId = PID1;
				turn = this.turn(turn,player2);
			}	
		}
		if(message){
			System.out.println("Player "+playerId+" won!");				
			}
		if(this.playerId == PID1){
				score1++;
		}
		else if(this.playerId == PID2){
				score2++;				
			}				
	}


	/**
	 * The method runs a Nim competition between two players according to the 
	 * three user-specified arguments. 
	 * (1) The type of the first player, which is a positive integer between 1
	 * and 4: 1 for a Random computer player, 2 for a Heuristic computer 
	 * player, 3 for a Smart computer player and 4 for a human player.
	 * (2) The type of the second player, which is a positive integer between
	 *  1 and 4.
	 * (3) The number of rounds to be played in the competition.
	 * @param args an array of string representations of the three input 
	 * arguments, as detailed above.
	 */
	public static void main(String[] args) {

		int p1Type = parsePlayerType(args,0);
		int p2Type = parsePlayerType(args,1);
		int numGames = parseNumberOfGames(args);
		Scanner scanner = new Scanner(System.in);
		boolean message = (p1Type == TYPE4 || p2Type == TYPE4);
		Player player1 = new Player(p1Type, 1, scanner);
		Player player2 = new Player(p2Type, 2, scanner);
		Competition competition = new Competition(player1, player2, message);		
		System.out.println("Starting a Nim competition of "+numGames+
				" rounds between a "+player1.getTypeName()+" player and a "
				+player2.getTypeName()+" player.");
		
		competition.playMultipleRounds(numGames);
				
	}	
	
}
