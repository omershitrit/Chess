/**
 * This class represents the status of the move.
 */

package Player;

/**
 * I used an enum to express the status.
 * @author Omer Shitrit
 *
 */
public enum MoveStatus {

	DONE {
		@Override
		public boolean isDone() { return true; }
	},
	ILLEGAL {
		@Override
		public boolean isDone() { return false; }
	}, LEADS_PLAYER_TO_CHECK {
		@Override
		public boolean isDone() { return false; }
	};
	
	public abstract boolean isDone();
}
