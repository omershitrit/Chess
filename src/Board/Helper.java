/**
 * Helper class includes a general methods and information
 * for initializing the board and the players
 */

package Board;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Helper {

	public static final short NUM_TILES = 64;
	public static final short NUM_TILES_PER_ROW = 8;
	// use for the edge-cases in each piece class
	public static final boolean[] FIRST_C = initColumn((short) 0);
	public static final boolean[] SECOND_C = initColumn((short) 1);
	public static final boolean[] SEVENTH_C = initColumn((short) 6);
	public static final boolean[] EIGHTH_C = initColumn((short) 7);
	// initRow receives the index of the tile that starts the desired row
	public static final boolean[] EIGHTH_R = initRow((short) 0);
	public static final boolean[] SEVENTH_R = initRow((short) 8);
	public static final boolean[] SIXTH_R = initRow((short) 16);
	public static final boolean[] FIFTH_R = initRow((short) 24);
	public static final boolean[] FOURTH_R = initRow((short) 32);
	public static final boolean[] THIRD_R = initRow((short) 40);
	public static final boolean[] SECOND_R = initRow((short) 48);
	public static final boolean[] FIRST_R = initRow((short) 56);
	public static final String[] ALGEBRIC_NOTATION = initializeAlgebricNotation();
	public static final Map<String, Short> POSITION_TO_COORDINATE = initializePositionToCoordinate(); 
	
	/**
	 * Make this class a container for useful functions
	 */
	private Helper() { throw new RuntimeException("Cant initiate this object!"); }

	/**
	 * Receives the index of the columns.
	 * @param index is the given index.
	 * @return the boolean array of the column.
	 */
	private static boolean[] initColumn(short index) {
		final boolean[] arr = new boolean[NUM_TILES];
		while(index < NUM_TILES) {
			arr[index] = true;
			index += NUM_TILES_PER_ROW;
		}
		return arr;
	}
	
	/**
	 * Helps to map between positions to coordinates.
	 * @return the map.
	 */
	private static Map<String, Short> initializePositionToCoordinate() {
		final Map<String ,Short> positionToCoordinate = new HashMap<>();
		for (short i = 0; i < NUM_TILES; ++i) {
			positionToCoordinate.put(ALGEBRIC_NOTATION[i], i);
		}
		return Collections.unmodifiableMap(positionToCoordinate);
	}

	/**
	 * Returns an array of the notations of the tiles, for debugging.
	 * @return the array of the tiles.
	 */
	private static String[] initializeAlgebricNotation() {
		return new String[] {
				"a8", "b8", "c8", "d8", "e8", "f8", "g8", "h8",
				"a7", "b7", "c7", "d7", "e7", "f7", "g7", "h7",
				"a6", "b6", "c6", "d6", "e6", "f6", "g6", "h6",
				"a5", "b5", "c5", "d5", "e5", "f5", "g5", "h5",
				"a4", "b4", "c4", "d4", "e4", "f4", "g4", "h4",
				"a3", "b3", "c3", "d3", "e3", "f3", "g3", "h3",
				"a2", "b2", "c2", "d2", "e2", "f2", "g2", "h2",
				"a1", "b1", "c1", "d1", "e1", "f1", "g1", "h1",
		};
	}
	
	/**
	 * Receives the index of the tile that starts the desired row.
	 * @param index is the given index.
	 * @return the array of the tiles.
	 */
	private static boolean[] initRow(short index) {
		final boolean[] arr = new boolean[NUM_TILES];
		short end = (short) (index + 8);
		while (index < end) { arr[index++] = true; }
		return arr;
	}
	
	/**
	 * Checks whether the position is valid.
	 * @param position is the given position.
	 * @return true or false.
	 */
		public static boolean isTileValid(final short position) {
			return position >= 0 && position < NUM_TILES;
		}

		/**
		 * Converts a position to coordinate.
		 * @param position is the given position.
		 * @return the coordinate.
		 */
		public static short getCoordinateAtPosition(final String position) {
			return POSITION_TO_COORDINATE.get(position);
		}
		
		/**
		 * Converts a coordinate to a position.
		 * @param coordinate is the given coordinate.
		 * @return the position.
		 */
		public static String getPositionAtCoordinate(final short coordinate) {
			return ALGEBRIC_NOTATION[coordinate];
		}
}
