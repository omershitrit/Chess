/**
 * This interface is the interface of any type of board that
 * can be evaluated by my program.
 */

package AI;

import Board.Board;

public interface BoardEvaluator {

	int evaluate(Board board, int depth);
	
}
