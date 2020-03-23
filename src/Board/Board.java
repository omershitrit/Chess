/**
 * This class represents the chess board, which means it includes
 * constructor, functions to calculate the current situation of
 * ongoing game, and an inner class of a builder.
 */

package Board;

import java.util.ArrayList;
import Player.WhitePlayer;
import Player.BlackPlayer;
import Player.Player;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Pieces.Bishop;
import Pieces.King;
import Pieces.Knight;
import Pieces.Pawn;
import Pieces.Piece;
import Pieces.Queen;
import Pieces.Rook;

/*
 * The board class
 */

public class Board {
	
	// list of the tiles of the board
	private List<Tile> tilesList;
	// the board is aware to the players
	private final WhitePlayer whitePlayer;
	private final BlackPlayer blackPlayer;
	private PlayerColor nextTurnPlayer;
	// to track how much white&black pieces are still in the game
	private final Collection<Piece> whitePieces;
	private final Collection<Piece> blackPieces;
	private Player currentPlayer;
	private final Pawn enPassantPawn;
	
	/**
	 * Constructor for board, using a builder.
	 * @param builder is the given builder
	 */
	private Board(final Builder builder) {
		this.tilesList = initBoard(builder);
		this.whitePieces = calculateActivePieces(tilesList, PlayerColor.WHITE);
		this.blackPieces = calculateActivePieces(tilesList, PlayerColor.BLACK);
		this.enPassantPawn = builder.enPassantPawn;
		final Collection<Move> possibleWhiteMoves = calculatePossibleMoves(this.whitePieces); 
		final Collection<Move> possibleBlackMoves = calculatePossibleMoves(this.blackPieces);
		this.whitePlayer = new WhitePlayer(this, possibleWhiteMoves, possibleBlackMoves);
		this.blackPlayer = new BlackPlayer(this, possibleBlackMoves, possibleWhiteMoves);
		this.currentPlayer = builder.getCurrentPlayer().choosePlayer(this.whitePlayer, this.blackPlayer);
		this.nextTurnPlayer = PlayerColor.WHITE;
	}
	
	/**
	 * Returns the black player.
	 * @return the black player.
	 */
	public Player blackPlayer() { return this.blackPlayer; }
	
	/**
	 * Returns all the possible moves of the white player and
	 * the black player together in a list.
	 * @return the list within the possible moves.
	 */
	public Iterable<Move> getAllPossibleMoves() { 
		Collection<Move> moves = this.whitePlayer.getPossibleMoves();
		Collection<Move> blackPlayerMoves = this.blackPlayer.getPossibleMoves();
		moves.addAll(blackPlayerMoves);
		return moves;
	}
	
	/**
	 * returns the enPassantPawn.
	 * @return the enPassantPawn.
	 */
	public Pawn getEnPassantPawn() { return this.enPassantPawn; }
	
	/**
	 * Changes the current player, according to the given player.
	 * @param player is the given player.
	 */
	public void changePlayer(Player player) {
		this.currentPlayer = player;
		this.nextTurnPlayer = player.getColor();
	}
	
	/**
	 * Returns the white player.
	 * @return the white player.
	 */
	public Player whitePlayer() { return this.whitePlayer; }

	/**
	 * Returns the current player.
	 * @return the current player.
	 */
	public Player currentPlayer() { return this.currentPlayer; }
	
	/**
	 * Returns the color of the next player.
	 * @return the color of the next player.
	 */
	protected PlayerColor getNextPlayerTurn() {
		return this.currentPlayer.getOpponent().getColor();
	}
	
	/**
	 * For testing the board - if the pieces in their correct place
	 */
	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder();
		for (short i = 0; i < Helper.NUM_TILES; ++i) {
			final String tileText = this.tilesList.get(i).toString();
			builder.append(String.format("%3s", tileText));
			if ((i + 1) % Helper.NUM_TILES_PER_ROW == 0) { builder.append("\n"); }
		}
		return builder.toString();
	}
	
	/**
	 * calculates all possible moves for a given collection of pieces
	 * @param pieces is the given collection of pieces.
	 * @return the list of the possible moves.
	 */
	private Collection<Move> calculatePossibleMoves(Collection<Piece> pieces) {
		final List<Move> moves = new ArrayList<>();
		for (final Piece piece : pieces) {
			moves.addAll(piece.calculatePossibleMoves(this));
		}
		return Collections.unmodifiableList(moves);
	}
	
	/**
	 * Returns a collection of the "alive" pieces in the given tiles, of the
	 * given color player.
	 * @param tiles are the tiles
	 * @param color is the given color player.
	 * @return the collection of alive pieces.
	 */
	private static Collection<Piece> calculateActivePieces(final List<Tile> tiles, final PlayerColor color) {
		final List<Piece> result = new ArrayList<>();
		for (final Tile tile : tiles) {
			Piece tempPiece = tile.getStandingPiece();
			if (tempPiece != null && tempPiece.getPieceColor() == color) {
				result.add(tempPiece);
			}
		}
		return Collections.unmodifiableList(result);
	}
	
	/**
	 * Receiving a position and returns the tile in this position
	 * @param position is the given position.
	 * @return the tile in this position.
	 */
	public Tile getTile(final short position) { return tilesList.get(position); }
	// create an immutable list of 64 tiles which basically represents the board
	public static List<Tile> initBoard(final Builder builder) {
		final List<Tile> tileList = new ArrayList<>();
		for (short i = 0; i < Helper.NUM_TILES; ++i) {
			tileList.add(Tile.createTile(i, builder.boardMap.get(i)));
		}
		return Collections.unmodifiableList(tileList);
	}
	
	/**
	 * Initializes a standard board and returns it.
	 * @return the initialized board.
	 */
	public static Board initStandardBoard() {
		final Builder builder = new Builder();
		// set the black pieces
		builder.setPiece(new Rook(PlayerColor.BLACK, (short) 0, true));
		builder.setPiece(new Knight(PlayerColor.BLACK, (short) 1, true));
		builder.setPiece(new Bishop(PlayerColor.BLACK, (short) 2, true));
		builder.setPiece(new Queen(PlayerColor.BLACK, (short) 3, true));
		builder.setPiece(new King(PlayerColor.BLACK, (short) 4, true));
		builder.setPiece(new Bishop(PlayerColor.BLACK, (short) 5, true));
		builder.setPiece(new Knight(PlayerColor.BLACK, (short) 6, true));
		builder.setPiece(new Rook(PlayerColor.BLACK, (short) 7, true));
		builder.setPiece(new Pawn(PlayerColor.BLACK, (short) 8, true));
		builder.setPiece(new Pawn(PlayerColor.BLACK, (short) 9, true));
		builder.setPiece(new Pawn(PlayerColor.BLACK, (short) 10, true));
		builder.setPiece(new Pawn(PlayerColor.BLACK, (short) 11, true));
		builder.setPiece(new Pawn(PlayerColor.BLACK, (short) 12, true));
		builder.setPiece(new Pawn(PlayerColor.BLACK, (short) 13, true));
		builder.setPiece(new Pawn(PlayerColor.BLACK, (short) 14, true));
		builder.setPiece(new Pawn(PlayerColor.BLACK, (short) 15, true));
		// set white pieces
		builder.setPiece(new Pawn(PlayerColor.WHITE, (short) 48, true));
		builder.setPiece(new Pawn(PlayerColor.WHITE, (short) 49, true));
		builder.setPiece(new Pawn(PlayerColor.WHITE, (short) 50, true));
		builder.setPiece(new Pawn(PlayerColor.WHITE, (short) 51, true));
		builder.setPiece(new Pawn(PlayerColor.WHITE, (short) 52, true));
		builder.setPiece(new Pawn(PlayerColor.WHITE, (short) 53, true));
		builder.setPiece(new Pawn(PlayerColor.WHITE, (short) 54, true));
		builder.setPiece(new Pawn(PlayerColor.WHITE, (short) 55, true));
		builder.setPiece(new Rook(PlayerColor.WHITE, (short) 56, true));
		builder.setPiece(new Knight(PlayerColor.WHITE, (short) 57, true));
		builder.setPiece(new Bishop(PlayerColor.WHITE, (short) 58, true));
		builder.setPiece(new Queen(PlayerColor.WHITE, (short) 59, true));
		builder.setPiece(new King(PlayerColor.WHITE, (short) 60, true));
		builder.setPiece(new Bishop(PlayerColor.WHITE, (short) 61, true));
		builder.setPiece(new Knight(PlayerColor.WHITE, (short) 62, true));
		builder.setPiece(new Rook(PlayerColor.WHITE, (short) 63, true));
		// white player is playing first
		builder.setTurn(PlayerColor.WHITE);
		return builder.build();
	}
	
	/**
	 * Returns the black pieces.
	 * @return the black pieces.
	 */
	public Collection<Piece> getBlackPieces() { return this.blackPieces; }
	
	/**
	 * Returns the white pieces.
	 * @return the white pieces.
	 */
	public Collection<Piece> getWhitePieces() { return this.whitePieces; }
	
	/**
	 * Inner class - implements the Builder design pattern.
	 * @author Omer Shitrit
	 *
	 */
	
	public static class Builder {
		// mapping the positions -> pieces
		Map<Short, Piece> boardMap;
		PlayerColor turn;
		Pawn enPassantPawn;
		
		/**
		 * Constructor.
		 */
		public Builder() {
			this.boardMap = new HashMap<>();
		}
		
		/**
		 * Returns the color of the current player.
		 * @return the color of the current player.
		 */
		public PlayerColor getCurrentPlayer() { return this.turn; }
		
		/**
		 * Sets a piece in the map.
		 * @param p is the given p.
		 * @return the builder itself.
		 */
		public Builder setPiece(final Piece p) {
			this.boardMap.put(p.getPosition(), p);
			return this;
		}
		
		/**
		 * Sets the current turn.
		 * @param c is the color of the player that supposed to play.
		 * @return the builder itself.
		 */
		public Builder setTurn(final PlayerColor c) {
			this.turn = c;
			return this;
		}
		
		/**
		 * Builds the board and returns it.
		 * @return the board.
		 */
		public Board build() { return new Board(this); }

		/**
		 * Sets the enPassantPawn.
		 * @param pawn is the given enPassantPawn.
		 */
		public void setEnPassantPawn(Pawn pawn) {
			this.enPassantPawn = pawn;
		}
	}
}
