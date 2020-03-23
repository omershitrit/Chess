/**
 * Rook piece implementation
 */

package Pieces;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import Board.Board;
import Board.PlayerColor;
import Board.Move.MajorMove;
import Board.Move.MajorAttackMove;
import Board.Helper;
import Board.Move;
import Board.Tile;

public class Rook extends Piece{

	// possible offsets
	private final static short[] possibleOffsets = { -8, -1, 1 ,8};
	
	/**
	 * Constructor for first move
	 * @param color is the given color.
	 * @param position is the given position.
	 */
	public Rook(final PlayerColor color, final short position) {
		super(position, color, Type.ROOK, true);
	}
	
	/**
	 * Constructor
	 * @param color is the given color.
	 * @param position is the given position
	 * @param isFirstMove is a boolean that indicates if it is the first move.
	 */
		public Rook(final PlayerColor color, final short position, final boolean isFirstMove) {
			super(position, color, Type.ROOK, isFirstMove);
		}
	
	/**
	 * Calculates the possible moves
	 * @return the possible moves.
	 */
	@Override
	public Collection<Move> calculatePossibleMoves(final Board board) {
		final List<Move> possibleMoves = new ArrayList<>();
		// iterate possible offsets
		for (final short currentOffset : possibleOffsets) {
			short tempPosition = this.position;
			// as long as the position is valid
			while(Helper.isTileValid(tempPosition)) {
				// if the rook is outside the board - break
				if (isRookInEdge(tempPosition, currentOffset)) {
					break;
				}
				tempPosition += currentOffset;
				// if the new position is valid
				if (Helper.isTileValid(tempPosition)) {
					final Tile candidateDestinationTile = board.getTile(tempPosition);
					// if destination tile is unoccupied
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
	 * If the bishop is in the first or eighth columns it means offsets are'nt valid
	 */ 
	private static boolean isRookInEdge(final short position, final short offset) {
		if (Helper.FIRST_C[position] && offset == -1) {
			return true;
		}
		if (Helper.EIGHTH_C[position] && offset == 1) {
			return true;
		}
		return false;
	}
	
	/**
	 * to string function
	 */
	@Override
	public String toString() { return Type.ROOK.toString(); }

	/**
	 * Moves the piece according to the given move.
	 * @param move is the given move.
	 * @return the new piece.
	 */
	@Override
	public Rook movePiece(final Move move) {
		return new Rook(move.getPiece().getPieceColor(), move.getDestinationPosition());
	}

}
