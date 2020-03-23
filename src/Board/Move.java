/**
 * Move class represents all the move types in chess game.
 * This is an abstract class.
 */

package Board;

import Board.Board.Builder;
import Pieces.Pawn;
import Pieces.Piece;
import Pieces.Rook;

public abstract class Move {

	protected final Board board;
	protected final Piece piece;
	protected final short destinationPosition;
	protected final boolean isFirstMove;
	
	/**
	 * Constructor.
	 * @param board is the board.
	 * @param piece is the piece that moves.
	 * @param destinationPosition is the destination of the piece.
	 */
	private Move(final Board board, final Piece piece, final short destinationPosition) {
		this.board = board;
		this.piece = piece;
		this.destinationPosition = destinationPosition;
		if (piece == null) {
			this.isFirstMove = false;
		}
		else {
			this.isFirstMove = piece.isFirstMove();
		}
	}
	
	/**
	 * Constructor for null move.
	 * @param board is the board.
	 * @param destinationPosition is the destination.
	 */
	private Move(final Board board, final short destinationPosition) {
		this.board = board;
		this.piece = null;
		this.destinationPosition = destinationPosition;
		this.isFirstMove = false;
	}
	
	/**
	 * Returns the destination.
	 * @return the destination.
	 */
	public short getDestinationPosition() { return this.destinationPosition; }
	
	/**
	 * Returns the board.
	 * @return
	 */
	public Board getBoard() { return this.board; }
	
	/**
	 * Returns the position of the moved piece.
	 * @return the position of the moved piece.
	 */
	public short getCurrentPosition() { return this.piece.getPosition(); }
	
	/**
	 * Executes the move and returns the board after the move.
	 * @return the board after the change that the move caused.
	 */
	public Board execute() {
		final Board.Builder builder = new Board.Builder();
		// iterate this player's pieces
		for (final Piece piece : this.board.currentPlayer().getActivePieces()) {
			if (!this.piece.equals(piece)) {
				builder.setPiece(piece);
			}
		}
		// iterate opponent player's pieces
		for (final Piece piece : this.board.currentPlayer().getOpponent().getActivePieces()) {
			builder.setPiece(piece);
		}
		// move the piece
		builder.setPiece(this.piece.movePiece(this));
		builder.setTurn(this.board.currentPlayer().getOpponent().getColor());
		return builder.build();
	}
	
	/**
	 * base function for the abstract class
	 * @return false
	 */
	public boolean isAttack() { return false; }
	
	/**
	 * base function for the abstract class
	 * @return false
	 */
	public boolean isCastlingMove() { return false; }
	
	/**
	 * base function for the abstract class
	 * @return NULL
	 */
	public Piece getAttackedPiece() { return null; }
	
	/**
	 * Returns the hashcode
	 * @return the hashcode (int).
	 */
	@Override
	public int hashCode() { 
		final int prime = 31;
		int result = 1;
		result = prime * result + this.destinationPosition;
		result = prime * result + this.piece.hashCode();
		result = prime * result + this.piece.getPosition();
		return result;
	}
	
	/**
	 * Compares between two moves.
	 * @return true if the moves are equals, o.w returns false.
	 */
	@Override
	public boolean equals(final Object other) {
		if (this == other) { return true; }
		if (!(other instanceof Move)) {
			return false;
		}
		final Move otherMove = (Move) other;
		return getCurrentPosition() == otherMove.getCurrentPosition() &&
			   getDestinationPosition() == otherMove.getDestinationPosition() &&
			   this.piece == otherMove.getPiece();
		
	}
	
	/**
	 * Returns the moved piece.
	 * @return the moved piece.
	 */
	public Piece getPiece() { return this.piece; }
	
	/**
	 * A class for a major move (two-step-move of Pawn).
	 * @author Omer Shitrit
	 *
	 */
	public static final class MajorMove extends Move {
		
		/**
		 * Constructor.
		 * @param board is the board.
		 * @param piece is the piece.
		 * @param destinationPosition is the destination.
		 */
		public MajorMove(final Board board, final Piece piece, final short destinationPosition) {
			super(board, piece, destinationPosition);
		}
		
		/**
		 * base function for the abstract class
		 * @return false
		 */
		@Override
		public boolean isCastlingMove() { return false; }
		
		/**
		 * Compares between the two moves
		 * @return true if they equals, o.w flase.
		 */
		@Override
		public boolean equals(final Object other) {
			return this == other || other instanceof MajorMove && super.equals(other);
		}
		
		/**
		 * Converts to string.
		 * @return string.
		 */
		@Override
		public String toString() {
			return piece.getPieceType().toString() + Helper.getPositionAtCoordinate(this.destinationPosition);
		}
	}
	
	/**
	 * A class for an attack move.
	 * @author Omer Shitrit
	 *
	 */
	public static class AttackMove extends Move {
		
		final Piece AttackedPiece;
		
		/**
		 * Constructor.
		 * @param board is the board
		 * @param piece is the piece
		 * @param destinationPosition is the destination
		 * @param AttackedPiece is the piece that get attacked
		 */
		public AttackMove(final Board board,
					final Piece piece,
					final short destinationPosition,
					final Piece AttackedPiece) {
			super(board, piece, destinationPosition);
			this.AttackedPiece = AttackedPiece;
		}
		
		/**
		 * base function for the abstract class
		 * @return false
		 */
		@Override
		public boolean isCastlingMove() { return false; }
		
		/**
		 * Calculates hashcode and returns it.
		 * @return the hashcode.
		 */
		@Override
		public int hashCode() { return this.AttackedPiece.hashCode() + super.hashCode(); }
		
		/**
		 * Compares between two moves.
		 * @return true if they equals, o.w false.
		 */
		@Override
		public boolean equals(final Object other) {
			if (this == other) { return true; }
			if (!(other instanceof AttackMove)) { return false; }
			final AttackMove otherAttackMove = (AttackMove) other;
			return super.equals(otherAttackMove) && this.AttackedPiece.equals(otherAttackMove.getAttackedPiece());
		}
		
		/**
		 * base function for the abstract class
		 * @return true
		 */
		@Override
		public boolean isAttack() { return true; }
		
		/**
		 * Returns the piece that get attacked.
		 * @return the piece that get attacked.
		 */
		@Override
		public Piece getAttackedPiece() { return this.AttackedPiece; }
	}
	
	/**
	 * A class for a major move attack.
	 * @author Omer Shitrit
	 *
	 */
	public static class MajorAttackMove extends AttackMove {
		
		/**
		 * Constructor.
		 * @param board is the board
		 * @param piece is the piece
		 * @param destinationPosition is the destination
		 * @param attackedPiece is the piece that get attacked
		 */
		public MajorAttackMove(final Board board, final Piece piece, final short destinationPosition, final Piece attackedPiece) {
			super(board, piece, destinationPosition, attackedPiece);
		}
		
		/**
		 * Compares between two moves
		 * @return true if they equal, o.w false
		 */
		@Override
		public boolean equals(final Object other) { return this == other || other instanceof MajorAttackMove && super.equals(other); };
		
		/**
		 * to string function.
		 * returns string
		 */
		@Override
		public String toString() { return piece.getPieceType() + Helper.getPositionAtCoordinate(this.destinationPosition); }
		
	}
	
	/**
	 * A class for a pawn move
	 * @author Omer Shitrit
	 *
	 */
	public static final class PawnMove extends Move {
		
		public PawnMove(final Board board,
					final Piece piece,
					final short destinationPosition) {
			super(board, piece, destinationPosition);
		}
		
		/**
		 * Compares between two moves
		 * @return true if they equals, o.w false
		 */
		@Override
		public boolean equals(final Object other) { return this == other || other instanceof PawnMove && super.equals(other); }
		
		/**
		 * to string function.
		 * @return string
		 */
		@Override
		public String toString() { return Helper.getPositionAtCoordinate(this.destinationPosition); }
		
		/**
		 * base function for the abstract class
		 * @return false
		 */
		@Override
		public boolean isCastlingMove() { return false; }
	}
	
	/**
	 * A class for a pawn attack move
	 * @author Omer Shitrit
	 *
	 */
	public static class PawnAttackMove extends AttackMove {
		
		/**
		 * Constructor
		 * @param board is the board
		 * @param piece is the piece
		 * @param destinationPosition is the destination
		 * @param attackPiece is the piece that get attacked
		 */
		public PawnAttackMove(final Board board,
					final Piece piece,
					final short destinationPosition,
					final Piece attackPiece) {
			super(board, piece, destinationPosition, attackPiece);
		}
		
		/**
		 * Compares between two moves.
		 * @return true if they equal, o.w false
		 */
		@Override
		public boolean equals(final Object other) { return this == other || other instanceof PawnAttackMove && super.equals(other);	}
		
		/**
		 * to string function
		 * @return string
		 */
		@Override
		public String toString() {
			return Helper.getPositionAtCoordinate(this.piece.getPosition()).substring(0, 1) + "x" +
			       Helper.getPositionAtCoordinate(this.destinationPosition);
		}
		
	}
	
	/**
	 * A class for a pawn which is enPasant attack move
	 * @author Omer Shitrit
	 *
	 */
	public static final class PawnEnPassantAttackMove extends PawnAttackMove {
		
		/**
		 * Constructor
		 * @param board is the board
		 * @param piece is the piece
		 * @param destinationPosition is the destination
		 * @param attackPiece is the piece that get attacked
		 */
		public PawnEnPassantAttackMove(final Board board,
					final Piece piece,
					final short destinationPosition,
					final Piece attackPiece) {
			super(board, piece, destinationPosition, attackPiece);
		}
		
		/**
		 * Compares between two moves
		 * @return true if they equal, o.w false
		 */
		@Override
		public boolean equals(final Object other) { return this == other || other instanceof PawnEnPassantAttackMove && super.equals(other);	}
		
		/**
		 * Executes the move
		 * @return the board after the move has been executed
		 */
		@Override
		public Board execute() {
			final Builder builder = new Builder();
			for (final Piece otherPiece : this.board.currentPlayer().getActivePieces()) {
				if (!this.piece.equals(otherPiece)) {
					builder.setPiece(otherPiece);
				}
			}
			for (final Piece otherPiece : this.board.currentPlayer().getOpponent().getActivePieces()) {
				if (!otherPiece.equals(this.getAttackedPiece())) {
					builder.setPiece(otherPiece);
				}
			}
			builder.setPiece(this.piece.movePiece(this));
			builder.setTurn(this.board.currentPlayer().getOpponent().getColor());
			return builder.build();
		}
	}

	/**
	 * A class for a pawn promotion
	 * @author Omer Shitrit
	 *
	 */
	public static class PawnPromotion extends Move {
		
		private final Move decoratedMove;
		private final Pawn promotedPawn;
		
		/**
		 * Constructor
		 * @param decoratedMove is the move that is decorated
		 */
		public PawnPromotion(Move decoratedMove) {
			super(decoratedMove.getBoard(), decoratedMove.getPiece(), decoratedMove.getDestinationPosition());
			this.decoratedMove = decoratedMove;
			this.promotedPawn = (Pawn) decoratedMove.getPiece();
		}

		/**
		 * Decorator to a move object
		 * @return the board after the change.
		 */
		@Override
		public Board execute() {
			// implement the decorated move
			final Board boardAfterFirstMove = this.decoratedMove.execute();
			final Builder builder = new Builder();
			// set current player's pieces without the promoted pawn
			for (final Piece piece : boardAfterFirstMove.currentPlayer().getActivePieces()) {
				if (!this.promotedPawn.equals(piece)) {
					builder.setPiece(piece);
				}
			}
			// set the opponent player's pieces
			for (final Piece piece : boardAfterFirstMove.currentPlayer().getOpponent().getActivePieces()) {
				builder.setPiece(piece);
			}
			builder.setPiece(this.promotedPawn.getPromotionChoice().movePiece(this));
			builder.setTurn(boardAfterFirstMove.currentPlayer().getColor());
			return builder.build();
		}
		
		/**
		 * base function for the abstract class
		 * @return false
		 */
		@Override
		public boolean isAttack() { return this.decoratedMove.isAttack(); }
		
		/**
		 * Returns the piece that get attacked
		 * @return the piece that get attacked
		 */
		@Override
		public Piece getAttackedPiece() { return this.decoratedMove.getAttackedPiece(); }
		
		/**
		 * to string function.
		 * @return string
		 */
		@Override
		public String toString() { return ""; }
		
		/**
		 * Calculates hashcode.
		 * @return hashcode
		 */
		@Override
		public int hashCode() { return this.decoratedMove.hashCode() + (31 * this.promotedPawn.hashCode()); }
		
		/**
		 * Compares between two moves
		 * @return true if they equal, o.w false
		 */
		@Override
		public boolean equals(final Object other) { return this == other || other instanceof PawnPromotion && (super.equals(other)); }
		
	}
	
	/**
	 * A class for a pawn jump
	 * @author Omer Shitrit
	 *
	 */
	public static final class PawnJump extends Move {
		
		/**
		 * Constructor
		 * @param board is the board
		 * @param piece is the piece
		 * @param destinationPosition is the destination
		 */
		public PawnJump(final Board board,
					final Piece piece,
					final short destinationPosition) {
			super(board, piece, destinationPosition);
		}
		
		/**
		 * Executes the move.
		 * @return the board after the change.
		 */
		@Override
		public Board execute() {
			final Builder builder = new Builder();
			for (final Piece piece : this.board.currentPlayer().getActivePieces()) {
				if (!this.piece.equals(piece)) {
					builder.setPiece(piece);
				}
			}
			for (final Piece piece : this.board.currentPlayer().getOpponent().getActivePieces()) {
				builder.setPiece(piece);
			}
			final Pawn pawnAfterMove = (Pawn) this.piece.movePiece(this);
			builder.setPiece(pawnAfterMove);
			builder.setEnPassantPawn(pawnAfterMove);
			builder.setTurn(this.board.currentPlayer().getOpponent().getColor());
			return builder.build();
		}
		
		/**
		 * to string function
		 * @return string
		 */
		@Override
		public String toString() { return Helper.getPositionAtCoordinate(this.destinationPosition); }
		
	}
	
	/**
	 * A class for a castle move
	 * @author Omer Shitrit
	 *
	 */
	static abstract class CastleMove extends Move {
		
		protected Rook castleRook;
		protected final short castleRookStart;
		protected final short castleRookDestination;
		
		/**
		 * Constructor
		 * @param board is the board
		 * @param piece is the piece
		 * @param destinationPosition is the destination
		 * @param castleRook is the rook for the castle move
		 * @param start is the start destination of the castle move
		 * @param dest is the destination of the castle move
		 */
		public CastleMove(final Board board,
						  final Piece piece,
						  final short destinationPosition,
						  final Rook castleRook,
						  final short start,
						  final short dest) {
			super(board, piece, destinationPosition);
			this.castleRook = castleRook;
			this.castleRookStart = start;
			this.castleRookDestination = dest;
		}
		
		/**
		 * Returns the rook of the castle move
		 * @return the rook
		 */
		public Rook getCastleRook() { return this.castleRook; }
		
		/**
		 * Returns true because its a castle move
		 * @return true
		 */
		@Override
		public boolean isCastlingMove() { return true; }
		
		/**
		 * Executes the move and returns the board after the change.
		 * @return the board after the change
		 */
		@Override
		public Board execute() {
			final Builder builder = new Builder();
			for (final Piece piece : this.board.currentPlayer().getActivePieces()) {
				if (!this.piece.equals(piece) && !this.castleRook.equals(piece)) {
					builder.setPiece(piece);
				}
			}
			for (final Piece piece : this.board.currentPlayer().getOpponent().getActivePieces()) {
				builder.setPiece(piece);
			}
			builder.setPiece(this.piece.movePiece(this));
			// TODO: look into the first move on normal pieces
			builder.setPiece(new Rook(this.castleRook.getPieceColor(), this.castleRookDestination));
			builder.setTurn(this.board.currentPlayer().getOpponent().getColor());
			return builder.build();
		}
		
		/**
		 * Calcualtes the hashcode
		 * @return the hashcode
		 */
		@Override
		public int hashCode() { 
			final int prime = 31;
			int result = super.hashCode();
			result = prime * result + this.castleRook.hashCode();
			result = prime * result + this.castleRookDestination;
			return result;
		}
		
		/**
		 * Compares between two moves.
		 * @return true if they equals, o.w false.
		 */
		@Override
		public boolean equals(final Object other) {
			if (this == other) {  return true; }
			if (!(other instanceof CastleMove)) { return false; }
			final CastleMove otherMove = (CastleMove) other;
			return super.equals(otherMove) && this.castleRook.equals(otherMove.getCastleRook());
		}
	}
	
	/**
	 * A class for a king side castle move
	 * @author Omer Shitrit
	 *
	 */
	public static final class KingSideCastleMove extends CastleMove {
		
		/**
		 * Constructor
		 * @param board is the board
		 * @param piece is the piece
		 * @param destinationPosition is the destination
		 * @param castleRook is the castle rook
		 * @param start is the start position of the castle move
		 * @param dest is the destination of the castle move
		 */
		public KingSideCastleMove(final Board board,
								  final Piece piece,
								  final short destinationPosition,
								  final Rook castleRook,
								  final short start,
								  final short dest) {
			super(board, piece, destinationPosition, castleRook, start, dest);
		}
		
		/**
		 * Compares between two moves
		 * @return true if they equal, o.w false
		 */
		@Override
		public boolean equals(final Object other) { 
			return this == other || other instanceof KingSideCastleMove && super.equals(other);
		}
		
		/**
		 * to string function
		 * @return string
		 */
		@Override
		public String toString() { return "0-0"; }
	}
	
	/**
	 * A class for a queen side castle move
	 * @author Omer Shitrit
	 *
	 */
	public static final class QueenSideCastleMove extends CastleMove {
			
		/**
		 * Constructor
		 * @param board is the board
		 * @param piece is the piece
		 * @param destinationPosition is the destination
		 * @param castleRook is the rook of the castle move
		 * @param start is the start position of the move
		 * @param dest is the destination of the move
		 */
			public QueenSideCastleMove(final Board board,
									   final Piece piece,
									   final short destinationPosition,
									   final Rook castleRook,
									   final short start,
									   final short dest) {
				super(board, piece, destinationPosition, castleRook, start, dest);
			}
			
			/**
			 * to string function
			 * @return string
			 */
			@Override
			public String toString() { return "0-0-0"; }
			
			/**
			 * Compares between two moves
			 * @return true if they equal, o.w false.
			 */
			@Override
			public boolean equals(final Object other) { 
				return this == other || other instanceof QueenSideCastleMove && super.equals(other);
			}
	}
	
	/**
	 * A class for a null move (used when the move wont be execute).
	 * @author Omer Shitrit
	 *
	 */
	public static final class NullMove extends Move {
		
		public static NullMove INSTANCE = null;
		
		/**
		 * Constructor
		 */
		private NullMove() {
			super(null, (short) 65);
		}
		
		public static NullMove getNullMove() {
			if (INSTANCE == null) {
				INSTANCE = new NullMove();
			}
				return INSTANCE;
		}
		
		/**
		 * base function for the abstract class
		 * @return false
		 */
		@Override
		public boolean isCastlingMove() { return false; }
		
		/**
		 * throw exception because this move cannot be executed.
		 */
		@Override
		public Board execute() { throw new RuntimeException("You can not execute the null move!\n"); }
		
		/**
		 * returns -1
		 */
		@Override
		public short getCurrentPosition() { return -1; }
	}
	
	/**
	 * A class for a move factory
	 * @author Omer Shitrit
	 *
	 */
	public static class MoveFactory {
		
		/**
		 * A private constructor that cannot be implemented!
		 */
		private MoveFactory() {
			throw new RuntimeException("You should not get here!\n");
		}
		
		/**
		 * Creates a move
		 * @param board is the board
		 * @param position is the start position of the move
		 * @param destination is the destination of the move
		 * @return the null move
		 */
		public static Move createMove(final Board board,
									  final short position,
									  final short destination) {
			for (final Move move : board.getAllPossibleMoves()) {
				if (move.getCurrentPosition() == position &&
					move.getDestinationPosition() == destination) {
					return move;
				}
			}
			return NullMove.getNullMove();
		}
	}
	
}
