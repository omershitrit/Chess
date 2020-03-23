/**
 * This class evaluates a standard chess board.
 * By using the BoardEvaluator interface, we are able to add more types
 * of boards, which means this program is close to changes and open for
 * expends.
 */

package AI;

import Board.Board;
import Pieces.Piece;
import Player.Player;

public class StandardBoardEvaluator implements BoardEvaluator {
	
	private static final int CHECK_BONUS = 50;
	private static final int CHECK_MATE_BONUS = 10000;
	private static final int DEPTH_BONUES = 100;
	private static final int CASTLED_BONUS = 60;

	/**
	 * Evaluates the given board.
	 * @param board is the given board.
	 * @return the score.
	 */
	@Override
	public int evaluate(final Board board, final int depth) {
		
		return scorePlayer(board, board.whitePlayer(), depth) -
			   scorePlayer(board, board.blackPlayer(), depth);
	}

	/**
	 * Calculates the score, considering multiple situations.
	 * @param board is the given board.
	 * @param player is the current player.
	 * @param depth is the deepest depth.
	 * @return the score.
	 */
	private int scorePlayer(final Board board, final Player player, final int depth) {
		return pieceValue(player) +
			   mobility(player) +
			   check(player) +
			   checkmate(player, depth) +
			   castled(player);
	}
	
	/**
	 * Returns the score for castled situation.
	 * @param player is the current player.
	 * @return the score.
	 */
	private static int castled(final Player player) {
		return player.isCastled() ? CASTLED_BONUS : 0;
	}

	/**
	 * Returns the score for check mate situation.
	 * @param player is the current player.
	 * @param depth is the deepest depth.
	 * @return the score.
	 */
	private int checkmate(final Player player, final int depth) {
		return player.getOpponent().isInCheckMate() ? CHECK_MATE_BONUS * depthBonues(depth) : 0;
	}

	/**
	 * Returns the bonus for the depth.
	 * @param depth is the depth
	 * @return the bonus
	 */
	private static int depthBonues(int depth) { return depth == 0 ? 1 : DEPTH_BONUES * depth; }
	
	/**
	 * Returns the bonus for the check situation.
	 * @param player is the current player.
	 * @return the bonus.
	 */
	private static int check(final Player player) {
		return player.getOpponent().isInCheck() ? CHECK_BONUS : 0;
	}

	/**
	 * Returns the score for the mobility, which is the amount of possible moves
	 * for the given player.
	 * @param player is the given player.
	 * @return the score.
	 */
	private static int mobility(final Player player) {
		return player.getPossibleMoves().size();
	}

	/**
	 * Returns the score for the pieces of the given player.
	 * @param player is the given player.
	 * @return the score.
	 */
	private static int pieceValue(final Player player) {
		int total = 0;
		for (final Piece piece : player.getActivePieces()) {
			total += piece.getPieceValue();
		}
		return total;
	}

}
