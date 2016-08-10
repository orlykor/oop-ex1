
/**
 *  * The Move class represents a move in the Nim game by a player.
 * A move consists of the row on which it is applied, the left bound 
 * (inclusive) of the sequence of stick to mark, and the right bound 
 * (inclusive) of the same sequence.
 * @author orlykor12
 */
public class Move {
	int row = 0;
	int leftBound = 0;
	int rightBound = 0;
	
	//The class constructor, which receives the parameters defining the move.
	public Move(int inRow, int inLeft, int inRight){
		row = inRow;
		leftBound = inLeft;
		rightBound = inRight;		
	}
	
	//Returns a string representation of the move. 
	public java.lang.String toString(){
		return row+":"+leftBound+"-"+rightBound;
	}
	
	//Returns the row on which the move is performed.
	public int getRow(){
		return row;			
	}
	
	// Returns the left bound of the stick sequence to mark.
	public int getLeftBound(){
		return leftBound;
	}
	
	// Returns the right bound of the stick sequence to mark.
	public int getRightBound(){
		return rightBound;
	}
	
}

