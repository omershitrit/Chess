/**
 * This enum is used for both pieces and players
 */

package Board;

import Player.BlackPlayer;
import Player.Player;
import Player.WhitePlayer;

/**
 * Represents the colors in the chess game, which are white and black.
 * @author Omer Shitrit
 *
 */
public enum PlayerColor {
	/**
	 * Represents the white color.
	 */
	WHITE {
		
		/**
		 * Goes up.
		 */
		@Override
		public short getDirection() { return -1; }

		/**
		 * Black color goes down.
		 */
		@Override
		public short getOppositeDirection() { return 1; }
		
		/**
		 * White, so returns true
		 */
		@Override
		public boolean isWhite() { return true; }

		/**
		 * White, so returns false.
		 */
		@Override
		public boolean isBlack() { return false; }

		/**
		 * returns the white player.
		 */
		@Override
		public Player choosePlayer(final WhitePlayer whitePlayer, final BlackPlayer blackPlayer) {
			return whitePlayer;
		}

		/**
		 * Returns the position in the eighth row.
		 */
		@Override
		public boolean isPawnPromotionTile(short position) {
			return Helper.EIGHTH_R[position];
		}
		}	
	,
	/**
	 * Represents the black color.
	 */
	BLACK {
		
		/**
		 * Black goes down.
		 */
		@Override
		public short getDirection() { return 1; }
		
		/**
		 * White goes up.
		 */
		@Override
		public short getOppositeDirection() { return -1; }

		/**
		 * Black, so returns false.
		 */
		@Override
		public boolean isWhite() { return false; }

		/**
		 * Black, so returns true.
		 */
		@Override
		public boolean isBlack() { return true; }

		/**
		 * Returns the black color.
		 */
		@Override
		public Player choosePlayer(final WhitePlayer whitePlayer, final BlackPlayer blackPlayer) {
			return blackPlayer;
		}

		/**
		 * Returns the position in the first row.
		 */
		@Override
		public boolean isPawnPromotionTile(short position) {
			return Helper.FIRST_R[position];
		}
	};
	
	// Those are the abstract function that implemented above
	public abstract short getDirection();
	public abstract short getOppositeDirection();
	public abstract boolean isWhite();
	public abstract boolean isBlack();
	public abstract Player choosePlayer(WhitePlayer whitePlayer, BlackPlayer blackPlayer);
	public abstract boolean isPawnPromotionTile(short position);
	
}
