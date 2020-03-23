/**
 * This class represents the white player.
 */

package Player;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import Board.Board;
import Board.PlayerColor;
import Board.Move;
import Board.Tile;
import Board.Move.KingSideCastleMove;
import Board.Move.QueenSideCastleMove;
import Pieces.Piece;
import Pieces.Rook;

public class WhitePlayer extends Player {

	/**
	 * Constructor.
	 * @param board is the given board.
	 * @param whiteMoves is the given white moves.
	 * @param blackMoves is the given black moves.
	 */
	public WhitePlayer(final Board board, 
			final Collection<Move> whiteMoves,
			final Collection<Move> blackMoves) {
		super(board, whiteMoves, blackMoves);
	}

	/**
	 * Returns the active pieces.
	 * @return the active pieces.
	 */
	@Override
	public Collection<Piece> getActivePieces() {
		return this.board.getWhitePieces();
	}
	
	/**
	 * Returns the color, meaning returns white.
	 * @return white.
	 */
	@Override
	public PlayerColor getColor() { return PlayerColor.WHITE; }
	
	/**
	 * Returns the opponent's color, meaning returns black.
	 * @return black.
	 */
	@Override
	public Player getOpponent() { return this.board.blackPlayer(); }

	/**
	 * Calculates the moves that castling the king
	 * @return the moves that castling the king.
	 */
	@Override
	protected Collection<Move> calculateKingCastles(final Collection<Move> currentPlayerMoves,
													final Collection<Move> opponentPlayerMoves) {
		final List<Move> kingCastles = new ArrayList<>();
		if (this.king.isFirstMove() && !this.isInCheck()) {
			if (!this.board.getTile((short)61).isTileOccupied() && !this.board.getTile((short)62).isTileOccupied()) {
				final Tile rookTile = this.board.getTile((short)63);
				if (rookTile.isTileOccupied() && rookTile.getStandingPiece().isFirstMove()) {
					if (Player.calculateAttacksOnTile((short)61, opponentPlayerMoves).isEmpty() &&
					    Player.calculateAttacksOnTile((short)62, opponentPlayerMoves).isEmpty() &&
					    rookTile.getStandingPiece().getPieceType().isRook()) {
					}
					kingCastles.add(new KingSideCastleMove(this.board,
							this.king,
							(short)62,
							(Rook)rookTile.getStandingPiece(),
							rookTile.getTilePosition(),
							(short) 61));
				}
			}
			if (!this.board.getTile((short)59).isTileOccupied() &&
				!this.board.getTile((short)58).isTileOccupied() &&
				!this.board.getTile((short)57).isTileOccupied()) {
				final Tile rookTile = this.board.getTile((short)60);
				if (rookTile.isTileOccupied() && rookTile.getStandingPiece().isFirstMove() &&
					Player.calculateAttacksOnTile((short)58, opponentPlayerMoves).isEmpty() &&
					Player.calculateAttacksOnTile((short)59, opponentPlayerMoves).isEmpty() &&
					rookTile.getStandingPiece().getPieceType().isKing()) {
					kingCastles.add(new QueenSideCastleMove(this.board,
															this.king,
															(short)58,
															(Rook)rookTile.getStandingPiece(),
															rookTile.getTilePosition(),
															(short)59));
				}
			}
		}
		
		return Collections.unmodifiableList(kingCastles);
	}

	/**
	 * to string function.
	 */
	@Override
	public String toString() {
		return "White Player";
	}

}
