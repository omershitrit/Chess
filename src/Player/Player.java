/**
 * Player class represents the player in this game
 */

package Player;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import Board.Board;
import Board.PlayerColor;
import Board.Move;
import Pieces.King;
import Pieces.Piece;

public abstract class Player {

	/* a player needs to be aware to the board, 
	   to its king and to the possible moves */
	protected Board board;
	protected final King king;
	protected final Collection<Move> possibleMoves;
	protected final boolean isInCheck;
	
	/**
	 * Constructor
	 * @param board is the given board.
	 * @param possibleMoves is the given possible moves
	 * @param opponentMoves is the given opponent moves
	 */
	Player(final Board board, 
			final Collection<Move> possibleMoves,
			final Collection<Move> opponentMoves) {
		this.board = board;
		this.king = initializeKing();
		List<Move> l = new ArrayList<>();
		l.addAll(possibleMoves);
		//l.addAll(calculateKingCastles(possibleMoves, opponentMoves));
		this.possibleMoves = l;
		this.isInCheck = !Player.calculateAttacksOnTile(this.king.getPosition(), opponentMoves).isEmpty();
	}
	
	/**
	 * Abstract function for to string function.
	 */
	public abstract String toString();
	
	/**
	 * Calculates attacks on a tile, according to the given position
	 * @param position is the given position
	 * @param moves is the given moves
	 * @return the possible attacks on the tile.
	 */
	protected static Collection<Move> calculateAttacksOnTile(short position, Collection<Move> moves) {
		final List<Move> result = new ArrayList<>();
		for (final Move move : moves) {
			if (position == move.getDestinationPosition()) {
				result.add(move);
			}
		}
		return result;
	}
	
	/**
	 * Sets the king
	 * @return the king.
	 */
	private King initializeKing() {
		for (final Piece piece : getActivePieces()) {
			if (piece.getPieceType().isKing()) {
				return (King) piece;
			}
		}
		throw new RuntimeException("ERROR: there is no board without a king on it!\n");
	}
	
	/**
	 * Determines if the given move is possible
	 * @param move is the given move.
	 * @return true if the move is possible, o.w false.
	 */
	public boolean isMovePossible(Move move) {
		return this.possibleMoves.contains(move);
	}
	
	/**
	 * Returns the isInCheck status.
	 * @return the isInCheck status.
	 */
	public boolean isInCheck() { return this.isInCheck; }
	
	/**
	 * Returns the isInCheckMate status.
	 * @return the isInCheckMate status.
	 */
	public boolean isInCheckMate() { return this.isInCheck && !hasEscapeMoves(); }
	
	/**
	 * Returns true if this player has escapeMoves, o.w false.
	 * @return true if this player has escapeMoves, o.w false.
	 */
	protected boolean hasEscapeMoves() {
		for (final Move move : this.possibleMoves) {
			final MoveTransition transition = makeMove(move);
			if (transition.getMoveStatus().isDone()) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Not in check but also don't have escape moves, because every move leads to a check
	 * @return true or false.
	 */
	public boolean isInStaleMate() { return !this.isInCheck && !hasEscapeMoves(); }
	
	/**
	 * Returns false. Implemented in the concrete classes.
	 * @return false.
	 */
	public boolean isCastled() { return false; }
	
	/**
	 * Makes the given move.
	 * @param move is the given move
	 * @return the moveTransition
	 */
	public MoveTransition makeMove(Move move) {
		if (!isMovePossible(move)) {
			return new MoveTransition(board, move, MoveStatus.ILLEGAL);
		}
		// here move is possible
		final Board afterMoveBoard = move.execute();
		final Collection<Move> attacksOnTheKing = Player.calculateAttacksOnTile(afterMoveBoard.currentPlayer().getOpponent().getKing().getPosition(),
				afterMoveBoard.currentPlayer().getPossibleMoves());
		// meaning we can not make this move
		if (!attacksOnTheKing.isEmpty()) {
			return new MoveTransition(this.board, move, MoveStatus.LEADS_PLAYER_TO_CHECK);
		}
		return new MoveTransition(afterMoveBoard, move, MoveStatus.DONE);
	}
	
	/**
	 * Returns the king.
	 * @return the king.
	 */
	public King getKing() { return this.king; }
	
	/**
	 * Returns the possible moves.
	 * @return the possible moves.
	 */
	public Collection<Move> getPossibleMoves() { return this.possibleMoves; }
	
	/**
	 * Returns the alive pieces
	 * @return the alive pieces.
	 */
	public abstract Collection<Piece> getActivePieces();
	
	/**
	 * Returns the color of the player
	 * @return the color of the player.
	 */
	public abstract PlayerColor getColor();
	
	/**
	 * Returns the opponent of this player
	 * @return the opponent of this player.
	 */
	public abstract Player getOpponent();
	
	/**
	 * Abstract function. Implemented in the concrete classes.
	 * @param currentPlayerMoves is the given current player moves.
	 * @param opponentPlayerMoves is the opponent player's moves.
	 * @return the moves that castling the king
	 */
	protected abstract Collection<Move> calculateKingCastles(Collection<Move> currentPlayerMoves,
															 Collection<Move> opponentPlayerMoves);
}
