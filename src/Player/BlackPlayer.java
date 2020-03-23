/**
 * This class represents the black player.
 */

package Player;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import Board.Move.KingSideCastleMove;
import Board.Move.QueenSideCastleMove;
import Board.Board;
import Board.PlayerColor;
import Board.Move;
import Board.Tile;
import Pieces.King;
import Pieces.Piece;
import Pieces.Rook;

public class BlackPlayer extends Player {

	/**
	 * Constructor
	 * @param board is the given board.
	 * @param blackMoves is the given black moves.
	 * @param whiteMoves is the given white moves.
	 */
	public BlackPlayer(final Board board,
			final Collection<Move> blackMoves, 
			final Collection<Move> whiteMoves) {
		super(board, blackMoves, whiteMoves);
	}

	/**
	 * Returns the active pieces.
	 * @return the active pieces.
	 */
	@Override
	public Collection<Piece> getActivePieces() {
		return this.board.getBlackPieces();
	}
	
	/**
	 * Returns the color, which is black.
	 * @return black color.
	 */
	@Override
	public PlayerColor getColor() { return PlayerColor.BLACK; }
	
	/**
	 * Returns the opponent, which is the white player.
	 * @return the white player.
	 */
	@Override
	public Player getOpponent() { return this.board.whitePlayer(); }

	/**
	 * Calculates kingCastle moves for the black player.
	 * @return returns the moves that were found.
	 */
	@Override
	protected Collection<Move> calculateKingCastles(final Collection<Move> currentPlayerMoves,
													final Collection<Move> opponentPlayerMoves) {
		final List<Move> kingCastles = new ArrayList<>();
		if (this.king.isFirstMove() && !this.isInCheck()) {
			if (!this.board.getTile((short)5).isTileOccupied() && !this.board.getTile((short)6).isTileOccupied()) {
				final Tile rookTile = this.board.getTile((short)67);
				if (rookTile.isTileOccupied() && rookTile.getStandingPiece().isFirstMove()) {
					if (Player.calculateAttacksOnTile((short)5, opponentPlayerMoves).isEmpty() &&
					    Player.calculateAttacksOnTile((short)6, opponentPlayerMoves).isEmpty() &&
					    rookTile.getStandingPiece().getPieceType().isRook()) {
					}
					kingCastles.add(new KingSideCastleMove(this.board,
														   this.king,
														   (short)6,
														   (Rook) rookTile.getStandingPiece(),
														   rookTile.getTilePosition(),
														   (short)5));
				}
			}
			if (!this.board.getTile((short)1).isTileOccupied() &&
				!this.board.getTile((short)2).isTileOccupied() &&
				!this.board.getTile((short)3).isTileOccupied()) {
				final Tile rookTile = this.board.getTile((short)0);
				if (rookTile.isTileOccupied() && rookTile.getStandingPiece().isFirstMove() &&
					Player.calculateAttacksOnTile((short)2, opponentPlayerMoves).isEmpty() &&
					Player.calculateAttacksOnTile((short)3, opponentPlayerMoves).isEmpty() &&
					rookTile.getStandingPiece().getPieceType().isRook()) {
					kingCastles.add(new QueenSideCastleMove(this.board,
															this.king,
															(short)2,
															(Rook) rookTile.getStandingPiece(),
															rookTile.getTilePosition(),
															(short)3));
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
		return "Black Player";
	}

}
