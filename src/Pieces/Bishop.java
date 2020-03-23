/**
 * Bishop piece implementation
 */
package Pieces;

import Board.PlayerColor;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import Board.Board;
import Board.Move;
import Board.Move.MajorAttackMove;
import Board.Move.MajorMove;
import Board.Tile;
import Board.Helper;

public class Bishop extends Piece{
	
	// possible offsets
	private final static short[] possibleOffsets = { -9, -7, 7, 9 };
	
	/**
	 * Constructor for first move
	 * @param color is the given color.
	 * @param position is the given position.
	 */
	public Bishop(final PlayerColor color, final short position) {
		super(position, color, Type.BISHOP, true);
	}
	
	/**
	 *  Constructor
	 * @param color is the given color.
	 * @param position is the given position
	 * @param isFirstMove is a boolean that determine if it is the first move.
	 */
	public Bishop(final PlayerColor color, final short position, final boolean isFirstMove) {
		super(position, color, Type.BISHOP, isFirstMove);
	}

	/**
	 *  Calculates possible moves
	 *  @return the possivle moves.
	 */
	@Override
	public Collection<Move> calculatePossibleMoves(final Board board) {
		final List<Move> possibleMoves = new ArrayList<>();
		// iterate the possible offsets
		for (final short currentOffset : possibleOffsets) {
			short tempPosition = this.position;
			// as long as the position is valid
			while(Helper.isTileValid(tempPosition)) {
				// break if the bishop is in an edge-position
				if (isBishopInEdge(tempPosition, currentOffset)) {
					break;
				}
				// here the position is not outside the board
				tempPosition += currentOffset;
				// relates to edge-cases
				if (Helper.isTileValid(tempPosition)) {
					final Tile candidateDestinationTile = board.getTile(tempPosition);
					if (!candidateDestinationTile.isTileOccupied()) {
						possibleMoves.add(new MajorMove(board, this, tempPosition));	
					} else { // this tile is occupied
						final Piece pieceAtDestination = candidateDestinationTile.getStandingPiece();
						final PlayerColor occupiedPieceColor = pieceAtDestination.getPieceColor();
						if (this.color != occupiedPieceColor) {
							possibleMoves.add(new MajorAttackMove(board, this, tempPosition, pieceAtDestination));
						}
						break;
					}
				}
			}
		}
		return Collections.unmodifiableList(possibleMoves);
	}
	
	/**
	 *  If the bishop is in the first or eighth columns it means offsets are'nt valid
	 */ 
	private static boolean isBishopInEdge(final short position, final short offset) {
		if (Helper.FIRST_C[position] && (offset == -9 || offset == 7)) {
			return true;
		}
		if (Helper.EIGHTH_C[position] && (offset == -7 || offset == 9)) {
			return true;
		}
		return false;
	}
	
	/**
	 * to string function
	 */
	@Override
	public String toString() { return Type.BISHOP.toString(); }

	/**
	 * Moves this piece according to the given move.
	 * @return the new bishop.
	 */
	@Override
	public Bishop movePiece(final Move move) {
		return new Bishop(move.getPiece().getPieceColor(), move.getDestinationPosition());
	}

}
