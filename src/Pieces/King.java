/**
 *  King piece implementation 
 */
package Pieces;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import Board.Board;
import Board.PlayerColor;
import Board.Helper;
import Board.Move;
import Board.Tile;
import Board.Move.MajorMove;
import Board.Move.MajorAttackMove;

public class King extends Piece {

	// possible offsets
	private final static short[] possibleOffsets = { -9, -8, -7, -1, 1, 7, 8, 9 };
	
	/**
	 *  Constructor for first move
	 * @param color is the given color.
	 * @param position is the given position.
	 */
	public King(final PlayerColor color, final short position) {
		super(position, color, Type.KING, true);
	}
	
	/**
	 * Constructor
	 * @param color is the given color.
	 * @param position is the given position.
	 * @param isFirstMove is a boolean that determines if it is the first move.
	 */
		public King(final PlayerColor color, final short position, final boolean isFirstMove) {
			super(position, color, Type.KING, isFirstMove);
		}

	/**
	 *  Calculate possible moves
	 *  @return the possible moves.
	 */
	@Override
	public Collection<Move> calculatePossibleMoves(final Board board) {
		final List<Move> possibleMoves = new ArrayList<>();
		// iterate possible offsets
		for (final short currentOffset : possibleOffsets) {
			final short tempPosition = (short) (this.position + currentOffset);
			// if the king is outside the board - continue
			if (isKingInEdge(this.position, currentOffset)) {
				continue;
			}
			// if the new position is valid
			if (Helper.isTileValid(tempPosition)) {
				final Tile candidateDestinationTile = board.getTile(tempPosition);
				// if the destination tile is unoccupied - add it 
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
	 * If the king is in the first, second, seventh or eighth columns
	 * it means offsets are'nt valid
	 */ 
	private static boolean isKingInEdge(final short position, final short offset) {
		if (Helper.FIRST_C[position] && (offset == -9 || offset == -1 ||
				  offset == 7)) {
			return true;
		}
		if (Helper.EIGHTH_C[position] && (offset == -7 || offset == 1 ||
				  offset == 9)) {
			return true;
		}
		return false;
	}
	
	/**
	 * to string function.
	 */
	@Override
	public String toString() { return Type.KING.toString(); }
	
	/**
	 * Moves this piece according to the given move.
	 * @param move is the given move
	 * @return the new piece.
	 */
	@Override
	public King movePiece(final Move move) {
		return new King(move.getPiece().getPieceColor(), move.getDestinationPosition());
	}
}
