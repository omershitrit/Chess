/**
 * This class represents the transitions.
 */

package Player;

import Board.Board;
import Board.Move;

public class MoveTransition {

	private final Board transitionBoard;
	private final Move move;
	private final MoveStatus moveStatus;
	
	/**
	 * Constructor
	 * @param board is the given board
	 * @param move is the given move
	 * @param moveStatus is the given moveStatus.
	 */
	public MoveTransition(final Board board,
						  final Move move, 
						  final MoveStatus moveStatus) {
		this.transitionBoard = board;
		this.move = move;
		this.moveStatus = moveStatus;
	}
	
	/**
	 * Returns this transitionBoard.
	 * @return this transitionBoard.
	 */
	public Board getTransitionBoard() { return this.transitionBoard; }
	
	/**
	 * Returns this moveStatus.
	 * @return this moveStatus.
	 */
	public MoveStatus getMoveStatus() { return this.moveStatus; }
}
