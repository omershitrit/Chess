/**
 * This class represents a tile object.
 * Every Chess board includes 64 tiles in it.
 */

package Board;

import java.util.Map;
import java.util.Collections;
import java.util.HashMap;
import Pieces.Piece;

public abstract class Tile {

	// the index of the tile
	protected final short index;
	private static final Map<Short, EmptyTile> EMPTY_TILES = initializeEmptyTiles();
	
	/**
	 * Constructor
	 * @param index is the index of the tile.
	 */
	private Tile(short index) { this.index = index; }
	
	/**
	 * Mapping the the positions to the empty tiles
	 * @return the map
	 */
	private static Map<Short, EmptyTile> initializeEmptyTiles() {
		final Map<Short, EmptyTile> m = new HashMap<>();
		for (short i = 0; i < Helper.NUM_TILES; ++i) {
			m.put(i, new EmptyTile(i));
		}
		// this makes m immutable
		return Collections.unmodifiableMap(m);
	}
	
	/**
	 * Receives an index and a piece and returns a tile
	 * @param index is the index of the tile.
	 * @param p is the piece that stands on the tile.
	 * @return the tile.
	 */
	public static Tile createTile(final short index, final Piece p) {
		return p != null ? new OccupiedTile(index, p) : EMPTY_TILES.get(index);
	}
	
	/**
	 * Returns the index of the tile, which is the position.
	 * @return
	 */
	public short getTilePosition() { return this.index; }
	
	// Abstract functions
	public abstract boolean isTileOccupied();
	public abstract Piece getStandingPiece();
	
	/**
	 * EmptyTile class
	 * @author Omer Shitrit
	 *
	 */
	public static final class EmptyTile extends Tile {
		
		/**
		 * Constructor.
		 * @param index is the index of the tile.
		 */
		private EmptyTile(short index) { super(index); }
		
		/**
		 * Returns false because this is an empty tile.
		 */
		@Override
		public boolean isTileOccupied() { return false; }
		
		/**
		 * Returns null because this is an empty tile.
		 */
		@Override
		public Piece getStandingPiece() { return null; }
		
		/**
		 * to string function.
		 * @return string.
		 */
		@Override
		public String toString() {
			return "-";
		}
	}
	
	/**
	 * OccupiedTile class
	 * @author Omer Shitrit
	 *
	 */
	public static final class OccupiedTile extends Tile {
		
		private Piece standingPiece;
		
		/**
		 * Constructor
		 * @param index is the index of the tile.
		 * @param p is the piece that stands on the tile.
		 */
		private OccupiedTile(final short index, Piece p) {
			super(index);
			this.standingPiece = p;
		}
		
		/**
		 * to string function.
		 * @return string.
		 */
		@Override
		public String toString() {
			return getStandingPiece().getPieceColor().isBlack() ? getStandingPiece().toString().toLowerCase() :
				getStandingPiece().toString();
		}
		
		/**
		 * Returns true because this is an occupied tile.
		 */
		@Override
		public boolean isTileOccupied() { return true; }
		
		/**
		 * Returns the standing piece
		 */
		@Override
		public Piece getStandingPiece() { return standingPiece; }
		
	}
}
