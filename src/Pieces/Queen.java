/**
 * Queen piece implementation
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

public class Queen extends Piece {

	// possible offsets
	private final static short[] possibleOffsets = { -9, -8, -7, -1, 1, 7, 8, 9 };
	
	/**
	 * Constructor for first move
	 * @param color is the given color.
	 * @param position is the given position.
	 */
	public Queen(final PlayerColor color, final short position) {
		super(position, color, Type.QUEEN, true);
	}
	
	/**
	 * Constructor
	 * @param color is the given color.
	 * @param position is the given position
	 * @param isFirstMove is a boolean value that indicates if it is the first move.
	 */
		public Queen(final PlayerColor color, final short position, final boolean isFirstMove) {
			super(position, color, Type.QUEEN, isFirstMove);
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
			short tempPosition = this.position;
			if (currentOffset == 8 && this.position == 3) {
				int g = 4;
			}
			// as long as the position is valid
			while(Helper.isTileValid(tempPosition)) {
				if (isQueenInEdge(tempPosition, currentOffset)) {
					break;
				}
				// calculates the new position and check it's validation
				tempPosition += currentOffset;
				if (Helper.isTileValid(tempPosition)) {
					final Tile candidateDestinationTile = board.getTile(tempPosition);
					// if the destination tile is unoccupied
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
	private static boolean isQueenInEdge(final short position, final short offset) {
		if (Helper.FIRST_C[position] && (offset == -9 || offset == 7 || offset == -1)) {
			return true;
		}
		if (Helper.EIGHTH_C[position] && (offset == -7 || offset == 9 || offset == 1)) {
			return true;
		}
		return false;
	}
	
	/**
	 * to string function.
	 */
	@Override
	public String toString() { return Type.QUEEN.toString(); }
	
	/**
	 * Moves the piece according to the given move.
	 * @param move is the given move.
	 * @return the new piece.
	 */
	@Override
	public Queen movePiece(final Move move) {
		return new Queen(move.getPiece().getPieceColor(), move.getDestinationPosition());
	}

}
