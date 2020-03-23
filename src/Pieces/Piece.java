/**
 * This class is an abstract class for a piece.
 */

package Pieces;

import Board.PlayerColor;

import java.util.Collection;

import javax.swing.text.html.HTMLDocument.HTMLReader.IsindexAction;

import Board.Board;
import Board.Move;

public abstract class Piece {

	protected final Type type;
	protected final short position;
	protected PlayerColor color;
	protected final boolean firstMove;
	private final int hashcode;
	
	/**
	 * Constructor
	 * @param position is the given position
	 * @param color is the given color.
	 * @param type is the given type.
	 * @param isFirstMove is a boolean that indicates if it is the first move.
	 */
	Piece(final short position, final PlayerColor color, final Type type, final boolean isFirstMove) {
		this.position = position;
		this.color = color;
		this.firstMove = isFirstMove;
		this.type = type;
		this.hashcode = computeHashCode();
	}
	
	/**
	 * Returns the color of the piece.
	 * @return the color of the piece.
	 */
	public PlayerColor getPieceColor() { return this.color; }
	
	/**
	 * Returns the position of the piece.
	 * @return the position of the piece.
	 */
	public short getPosition() { return this.position; }
	
	/**
	 * Retuens the isFirstMove boolean.
	 * @return the isFirstMove boolean.
	 */
	public boolean isFirstMove() {
		return this.firstMove;
		}
	
	/**
	 * Calculates possible moves.
	 * @param board is the given board.
	 * @return the possible moves.
	 */
	public abstract Collection<Move> calculatePossibleMoves(final Board board);
	
	/**
	 * Moves the piece according to the given move.
	 * @param move is the given move.
	 * @return is the new piece.
	 */
	public abstract Piece movePiece(Move move);
	
	/**
	 * Returns the type of the piece.
	 * @return
	 */
	public Type getPieceType() { return this.type; }
	
	/**
	 * Compares between two pieces.
	 * @return true if they equal, o.w false.
	 */
	@Override
	public boolean equals(final Object other) {
		if (this == other) { return true; }
		if (!(other instanceof Piece)) { return false; }
		final Piece otherPiece = (Piece) other;
		return this.color == otherPiece.getPieceColor() && this.type == otherPiece.getPieceType() &&
				this.position == otherPiece.getPosition() && this.firstMove == otherPiece.isFirstMove();
	}
	
	/**
	 * Calculates and returns a hashcode.
	 * @return hashcode.
	 */
	private int computeHashCode() {
		int result = this.type.hashCode();
		result = 31 * result + this.color.hashCode();
		result = 31 * result + this.position;
		result = 31 * result + (this.firstMove ? 1 : 0);
		return result;
	}
	
	/**
	 * Returns the value of this piece.
	 * @return the value of this piece.
	 */
	public short getPieceValue() {
		return this.type.getValue();
	}
	
	/**
	 * Returns the hashcode of this piece.
	 * @return the hashcode of this piece.
	 */
	@Override
	public int hashCode() { return this.hashcode; }
	
	/**
	 * Enum for the types.
	 * @author Omer Shitrit
	 *
	 */
	public enum Type {
		
		PAWN("P", (short)100) {
			public boolean isKing() { return false; }

			@Override
			public boolean isRook() {
				return false;
			}
		},
		KNIGHT("N",(short)300) {
			public boolean isKing() { return false; }
			
			@Override
			public boolean isRook() {
				return false;
			}
		},
		
		BISHOP("B", (short)300) {
			public boolean isKing() { return false; }
			
			@Override
			public boolean isRook() {
				return false;
			}
		},
		ROOK("R", (short)500) { 
			public boolean isKing() { return false; }
			
			@Override
			public boolean isRook() {
				return true;
			}
		},
		QUEEN("Q", (short)800) {
			public boolean isKing() { return false; }
			
			@Override
			public boolean isRook() {
				return false;
			}
		},
		KING("K", (short)10000) {
			public boolean isKing() { return true; }
			
			@Override
			public boolean isRook() {
				return false;
			}
		};
		
		private String type;
		private short value;
		
		Type(final String type, final short value) {
			this.type = type;
			this.value = value;
		}
		
		/**
		 * to string function.
		 */
		@Override
		public String toString() { return this.type; }
		public short getValue() { return this.value; }
		
		// Two abstract functions.
		public abstract boolean isKing();

		public abstract boolean isRook();
	}
}
