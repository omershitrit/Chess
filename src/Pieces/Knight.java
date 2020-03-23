/**
 * Knight piece implementation
  */

package Pieces;

import Board.PlayerColor;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import Board.Board;
import Board.Move;
import Board.Tile;
import Board.Helper;
import Board.Move.MajorMove;
import Board.Move.MajorAttackMove;

public class Knight extends Piece{
	
	// possible offsets for a knight piece
	private final static short[] possibleOffsets = { -17, -15, -10, -6, 6, 10, 15, 17 };

	/**
	 * Constructor for first move
	 * @param color is the given color.
	 * @param position is the given position.
	 */
	public Knight(final PlayerColor color, short position) {
		super(position, color, Type.KNIGHT, true);
	}
	
	/**
	 *  Constructor.
	 * @param color is the given color.
	 * @param position is the given position.
	 * @param isFirstMove is a boolean value that indicates if it is the first move.
	 */
		public Knight(final PlayerColor color, short position, final boolean isFirstMove) {
			super(position, color, Type.KNIGHT, isFirstMove);
		}
	
	/**
	 * Calculates possible moves
	 * @return the possible moves.
	 */
	@Override
	public Collection<Move> calculatePossibleMoves(final Board board) {
		final List<Move> possibleMoves = new ArrayList<>();
		// iterate possible offsets
		for (final short currentOffset : possibleOffsets) {
			final short tempPosition = (short) (this.position + currentOffset);
			// if the new position is outside of the board - continue
			if (Helper.isTileValid(tempPosition)) {
				if (isKnightInEdge(this.position, currentOffset)) {
					continue;
				}
				final Tile candidateDestinationTile = board.getTile(tempPosition);
				// if the tile is unoccupied
				if (!candidateDestinationTile.isTileOccupied()) {
					possibleMoves.add(new MajorMove(board, this, tempPosition));	
				} else { // this tile is occupied
					final Piece pieceAtDestination = candidateDestinationTile.getStandingPiece();
					final PlayerColor occupiedPieceColor = pieceAtDestination.getPieceColor();
					if (this.color != occupiedPieceColor) {
						possibleMoves.add(new MajorAttackMove(board, this, tempPosition, pieceAtDestination));
					}
				}
			} 
		}
		return Collections.unmodifiableList(possibleMoves);
	}

	/**
	 * If the knight is in the first, second, seventh or eighth columns
	 * it means offsets are'nt valid
	 */ 
	private static boolean isKnightInEdge(final short position, final short offset) {
		if (Helper.FIRST_C[position] && (offset == -17 || offset == -10
				|| offset == 6 || offset == 15)) {
			return true;
		}
		if (Helper.SECOND_C[position] && (offset == -10 || offset == 6)) {
			return true;
		}
		if (Helper.SEVENTH_C[position] && (offset == -6 || offset == 10)) {
			return true;
		}
		if (Helper.EIGHTH_C[position] && (offset == -15 || offset == -6
				|| offset == 10 ||offset == 17)) {
			return true;
		}
		return false;
	}
	
	/**
	 * to string function
	 */
	@Override
	public String toString() { return Type.KNIGHT.toString(); }
	
	/**
	 * Moves the piece according to the given move.
	 * @param move is the given move.
	 * @return the new piece.
	 */
	@Override
	public Knight movePiece(final Move move) {
		return new Knight(move.getPiece().getPieceColor(), move.getDestinationPosition());
	}
}
