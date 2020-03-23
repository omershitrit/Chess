/**
 * This class represents the MiniMax algorithm, which is the algorithm
 * that guides the bot in my program.
 */

package AI;

import Board.Board;
import Board.Move;
import Player.MoveTransition;

public class MiniMax implements MoveStrategy {

	private final BoardEvaluator boardEvaluator;
	private final int depth;
	
	/**
	 * Constructor
	 * @param depth is the depth of the search algorithm.
	 */
	public MiniMax(final int depth) {
		this.boardEvaluator = new StandardBoardEvaluator();
		this.depth = depth;
	}
	
	/**
	 * to string function
	 */
	@Override
	public String toString() { return "MiniMax"; }
	
	/**
	 * Executes the algorithm, which meaning find the best move the bot is able
	 * to do, considering the depth limitation.
	 * @return the best move the algorithm found.
	 */
	@Override
	public Move execute(Board board) {
		//final long startTime = System.currentTimeMillis();
		Move bestMove = null;
		int highestValue = Integer.MIN_VALUE;
		int lowestValue = Integer.MAX_VALUE;
		int currentValue = 0;
		//int numMoves = board.currentPlayer().getPossibleMoves().size();
		for (final Move move : board.currentPlayer().getPossibleMoves()) {
			final MoveTransition moveTransition = board.currentPlayer().makeMove(move);
			if (moveTransition.getMoveStatus().isDone()) {
				currentValue = board.currentPlayer().getColor().isWhite() ?
						min(moveTransition.getTransitionBoard(), depth - 1) :
						max(moveTransition.getTransitionBoard(), depth - 1);
				if (board.currentPlayer().getColor().isWhite() && currentValue >= highestValue) {
					highestValue = currentValue;
					bestMove = move;
				} else if (board.currentPlayer().getColor().isBlack() && currentValue <= lowestValue) {
					lowestValue = currentValue;
					bestMove = move;
				}
			}
		}
		//final long executionTime = System.currentTimeMillis() - startTime;
		return bestMove;
	}
	
	/**
	 * Checks whether the game ended.
	 * @param board is the given board
	 * @return true if the game ended, o.w false
	 */
	private static boolean isEndGame(final Board board) {
		return board.currentPlayer().isInCheckMate() || board.currentPlayer().isInStaleMate();
	}
	
	/**
	 * Returns the lowest value (with my system that gives value to each chess move).
	 * @param board is the given board.
	 * @param depth is the deepest depth
	 * @return the lowest value.
	 */
	public int min(final Board board, final int depth) {
		// or game over
		if (depth == 0 || isEndGame(board)) { return this.boardEvaluator.evaluate(board, depth); }
		int lowestValue = Integer.MAX_VALUE;
		for (final Move move : board.currentPlayer().getPossibleMoves()) {
			final MoveTransition moveTransition = board.currentPlayer().makeMove(move);
			if (moveTransition.getMoveStatus().isDone()) {
				final int currentValue = max(moveTransition.getTransitionBoard(), depth - 1);
				if (currentValue <= lowestValue) {
					lowestValue = currentValue;
				}
			}
		}
		return lowestValue;
	}
	
	/**
	 * Returns the highest value (with my system that gives value to each chess move).
	 * @param board is the given board.
	 * @param depth is the deepest depth
	 * @return the highest value.
	 */
	public int max(final Board board, final int depth) {
		// or game over
				if (depth == 0) { return this.boardEvaluator.evaluate(board, depth); }
				int highestValue = Integer.MIN_VALUE;
				for (final Move move : board.currentPlayer().getPossibleMoves()) {
					final MoveTransition moveTransition = board.currentPlayer().makeMove(move);
					if (moveTransition.getMoveStatus().isDone()) {
						final int currentValue = min(moveTransition.getTransitionBoard(), depth - 1);
						if (currentValue >= highestValue) {
							highestValue = currentValue;
						}
					}
				}
				return highestValue;
	}

}
