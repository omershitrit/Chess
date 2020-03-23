/**
 * This interface is the interface of any AI algorithm that
 * can be use to determine the moves for the bot in my program.
 */

package AI;

import Board.Board;
import Board.Move;

public interface MoveStrategy {

	Move execute(Board board);
	
}
