/**
 * This class is responsible of the board gui.
 */

package GUI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.Collections;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.WindowConstants;
import AI.MiniMax;
import AI.MoveStrategy;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;

import Board.Board;
import Board.Helper;
import Board.Move;
import Board.PlayerColor;
import Board.Tile;
import Pieces.Piece;
import Player.MoveTransition;
import Player.Player;

public class BoardGUI extends Observable{

	// Defines the size of the board gui
	private static final Dimension FRAME_SIZE = new Dimension(600, 600);
	private static final Dimension BOARD_PANEL_DIMENSION = new Dimension(400, 350);
	private static final Dimension TILE_PANEL_DIMENSION = new Dimension(10, 10);
	private static final float PERIOD = 5f;
	private Board board;
	private Tile sourceTile;
	private Tile destinationTile;
	private Piece movedPiece;
	private boolean timeMode;
	private int depth;
	private BoardDirection boardDirection;
	private boolean executeHighlightPossibleMoves;
	private boolean settingsAreDone;
	private Move botMove;
	private static String defaultImagesPath = "art/";
	private final static Color lightTileColor = Color.decode("#FFFACD");
	private final static Color darkTileColor = Color.decode("#593E1A");
	
	private JLabel timeLabel;
	private final JFrame gameFrame;
	private JFrame timeFrame;
	private JPanel timePanel;
	// TODO: timeLabel.setText(Float.toString(seconds));
	private boolean showScreenTimeOn;
	private final BoardPanel boardPanel;
	private final MoveLog moveLog;
	public final GameSetup gameSetup;
	
	private static final BoardGUI INSTANCE = new BoardGUI();
	
	/**
	 * Constructor which initializes all the parameters.
	 */
	private BoardGUI() {
		this.gameFrame = new JFrame("Chess");
		this.gameFrame.setLayout(new BorderLayout());
		final JMenuBar menu = createMenuBar();
		this.board = Board.initStandardBoard();
		this.gameFrame.setJMenuBar(menu);
		this.gameFrame.setSize(FRAME_SIZE);
		this.executeHighlightPossibleMoves = true;
		this.boardDirection = BoardDirection.NORMAL;
		this.boardPanel = new BoardPanel();
		this.timeLabel = new JLabel("");
		this.timeLabel.setBounds(50, 50, 50, 50);
		this.timeLabel.setVisible(true);
		this.boardPanel.add(this.timeLabel);
		this.addObserver(new BoardObserver());
		this.moveLog = new MoveLog();
		this.gameSetup = new GameSetup(this.gameFrame, true);
		this.gameFrame.add(this.boardPanel, BorderLayout.CENTER);
		this.settingsAreDone = false;
		this.gameFrame.setVisible(true);
		this.timeMode = false;
		this.showScreenTimeOn = false;
	}
	
	/**
	 * Creates the menu bar above the chess board.
	 * @return the JMenuBar that was created.
	 */
	private JMenuBar createMenuBar() {
		final JMenuBar tableMenuBar = new JMenuBar();
		tableMenuBar.add(createFileTab());
		tableMenuBar.add(createPreferencesMenu());
		tableMenuBar.add(createOptionMenu());
		return tableMenuBar;
	}
	
	/**
	 * This function counts time for a single turn.
	 * When the time is up - the turn is over.
	 */
	public void countTimeForTurn() {
		this.setTimeMode();
		while (true) {
			long start = System.currentTimeMillis();
		    PlayerColor playerColor = board.currentPlayer().getColor();
		    while (timeMode && board.currentPlayer().getColor() == playerColor) {
		    	long sample = System.currentTimeMillis();
		    	float current = (sample - start) / 1000;
		    	float temp = 5f - current;
		    	float val = temp > 0 ? temp : 0;
		    	if (!showScreenTimeOn) {
		    		initializeShowTime();
		    	}
		    	showTime(val);
		    	showScreenTimeOn = true;
		    	if (current > PERIOD) {
		    		changeTurn();
		    		start = System.currentTimeMillis();
		    		showScreenTimeOn = false;
		    	}
		    }
		    if (timeMode && board.currentPlayer().getColor() != playerColor) {
		    	timeLabel.setText(Float.toString(PERIOD));
		    	showScreenTimeOn = true;
		    } else if (playerColor == board.currentPlayer().getColor()) {
		    	hideTime();
		    }
		}
	}
		
	/**
	 * Shows the board gui.
	 */
	public void show() { 
		BoardGUI.get().getBoardPanel().drawBoard(BoardGUI.get().getBoard());
	}
	
	/**
	 * Creates a window that shows the time until the turn is over.
	 */
	public void initializeShowTime() {
		this.timeFrame = new JFrame("Time Window");
		this.timeFrame.setLayout(new BorderLayout());
		this.timeFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		this.timeFrame.setSize(250,100);
		this.timeFrame.add(this.timeLabel, BorderLayout.NORTH);
		this.timeFrame.setVisible(true);
	}
	
	/**
	 * Shows the window of the time until the turn is over.
	 * @param seconds is the seconds that need to be shown.
	 */
	private void showTime(float seconds) {
		Thread thread = new Thread(){
		    public void run(){
		    	timeLabel.setText(Float.toString(seconds));
				boardPanel.validate();
				boardPanel.repaint();
		    }
		};
		  thread.start();
	}
	
	/**
	 * Hides the time.
	 */
	private void hideTime() { this.timeFrame.setVisible(false); }
	
	/**
	 * Returns the board gui.
	 * @return the board gui
	 */
	public static BoardGUI get() { return INSTANCE; }
	
	/**
	 * Returns the gameSetup.
	 * @return the gameSetup
	 */
	private GameSetup getGameSetup() { return this.gameSetup; }
	
	/**
	 * Returns the board object.
	 * @return the board object
	 */
	private Board getBoard() { return this.board; }
	
	/**
	 * Sets the time mode (switches from on to off, and from off to on)
	 */
	private void setTimeMode() { this.timeMode = !this.timeMode; }
	
	/**
	 * Changes the turn
	 */
	private void changeTurn() {
		Player nextPlayer = this.board.currentPlayer().getOpponent();
		this.board.changePlayer(nextPlayer);
	}
	
	/**
	 * Returns the current time mode.
	 * @return the current time mode, which is boolean type.
	 */
	private boolean getTimeMode() { return this.timeMode; }
	
	/**
	 * Sets the depth for the AI algorithm,
	 * @param depth is the desired depth for the AI algorithm
	 */
	public void setDepth(int depth) { this.depth = depth; }
	
	/**
	 * Creates the Tabs.
	 * @return the JMenu which is the Tabs.
	 */
	private JMenu createFileTab() {
		final JMenu fileMenu = new JMenu("File");
		final JMenuItem guide = new JMenuItem("Guide");
		guide.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JFrame frame = new JFrame("Time Window");
				frame.setLayout(new BorderLayout());
				frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
				frame.setSize(250,100);
				JLabel label = new JLabel("Enter this link and study about the chess pieces & moves:     " +
						"https://www.thesprucecrafts.com/illustrated-guide-to-chess-pieces-611547");
				frame.add(label, BorderLayout.NORTH);
				frame.setVisible(true);
			}
		});
		fileMenu.add(guide);
		
		final JMenuItem exitMenuItem = new JMenuItem("Exit");
		exitMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		fileMenu.add(exitMenuItem);
		return fileMenu;
	}
	
	/**
	 * Represents the board panel in the board gui.
	 * @author Omer Shitrit
	 *
	 */
	public class BoardPanel extends JPanel {
		final List<TilePanel> tiles;
		
		/**
		 * Constructor
		 */
		BoardPanel() {
			super(new GridLayout(8, 8));
			this.tiles = new ArrayList<>();
			for (int i = 0; i < Helper.NUM_TILES; ++i) { 
				final TilePanel currentTilePanel = new TilePanel(this, (short) i);
				this.tiles.add(currentTilePanel);
				add(currentTilePanel);
			}
			setPreferredSize(BOARD_PANEL_DIMENSION);
			validate();
		}
		
		/**
		 * Draws the given board.
		 * @param board is the given board.
		 */
		public void drawBoard(final Board board) {
			removeAll();
			for (final TilePanel tilePanel : boardDirection.swap(tiles)) {
				tilePanel.drawTile(board);
				add(tilePanel); 
			}
			validate();
			repaint();
		}
	}
	
	/**
	 * A class for a move log - not in use.
	 * @author Omer Shitrit
	 *
	 */
	public static class MoveLog {
		
		private final List<Move> moves;
		
		/**
		 * Constructor.
		 */
		MoveLog() {
			this.moves = new ArrayList<>();
		}
		
		/**
		 * Retuens the moves.
		 * @return the moves.
		 */
		public List<Move> getMoves() {
			return this.moves;
		}
		
		/**
		 * Adds the given move.
		 * @param move is the given move.
		 */
		public void addMove(final Move move) {
			this.moves.add(move);
		}
		
		/**
		 * Returns the amount of the moves.
		 * @return the amount of the moves.
		 */
		public int size() { return this.moves.size(); }
		
		/**
		 * Clears the log.
		 */
		public void clear() { this.moves.clear(); }
		
		/**
		 * Removes the move that the given index belong to.
		 * @param index is the given index.
		 * @return the moves without this specific move.
		 */
		public Move removeMove(int index) {
			return this.moves.remove(index);
		}
		
		/**
		 * Removes the given move.
		 * @param move is the given move.
		 * @return the moves without this specific move.
		 */
		public boolean removeMove(final Move move) {
			return this.moves.remove(move);
		}
	}
	
	/**
	 * This class represents the tile panel.
	 * @author Omer Shitrit
	 *
	 */
	public class TilePanel extends JPanel {
		private final short position;
		
		/**
		 * Constructor.
		 * @param boardPanel is the given boardPanel.
		 * @param position is the given position.
		 */
		TilePanel(final BoardPanel boardPanel, final short position) {
			super(new GridBagLayout());
			this.position = position;
			setPreferredSize(TILE_PANEL_DIMENSION);
			assignTileColor();
			addMouseListener(new MouseListener() {
				
				@Override
				public void mouseReleased(final MouseEvent e) {
					
				}
				
				@Override
				public void mousePressed(final MouseEvent e) {
					
				}
				
				@Override
				public void mouseExited(final MouseEvent e) {
					
				}
				
				@Override
				public void mouseEntered(final MouseEvent e) {
					
				}
				
				private void clear() {
					sourceTile = null;
					destinationTile = null;
					movedPiece = null;
				}
				
				// basically adds a listener to each tile
				public void mouseClicked(final MouseEvent e) {
					// undo selection
					if (SwingUtilities.isRightMouseButton(e)) {
						// try use clear instead
						sourceTile = null;
						destinationTile = null;
						movedPiece = null;
					// user makes a move
					} else if (SwingUtilities.isLeftMouseButton(e)) {
						if (sourceTile == null) {
							sourceTile = board.getTile(position);
							movedPiece = sourceTile.getStandingPiece();
							Tile tempTile = sourceTile;
							Piece tempPiece = movedPiece;
							if (movedPiece == null) {
								sourceTile = null;
							}
						} else {
							destinationTile = board.getTile(position);
							Tile tempDestinationTile = destinationTile;
							final Move move = Move.MoveFactory.createMove(board, sourceTile.getTilePosition(), 
									destinationTile.getTilePosition());
							final MoveTransition transition = board.currentPlayer().makeMove(move);
							if (transition.getMoveStatus().isDone()) {
								board = transition.getTransitionBoard();
								moveLog.addMove(move);
							}
							// try use clear instead
							sourceTile = null;
							destinationTile = null;
							movedPiece = null;
						}
						SwingUtilities.invokeLater(new Runnable() {
							@Override
							public void run() { 
								//gameHistoryPanel.redo(board, moveLog);
								//deadPieces.redo(moveLog);
								if (gameSetup.isAIPlayer(board.currentPlayer())) {
									BoardGUI.get().moveMadeUpdate(PlayerType.HUMAN);
								}
								boardPanel.drawBoard(board); 
								}
						});
						boardPanel.drawBoard(board);
					}
				}
			});
			putImageToTile(board);
			validate();
		}

		/**
		 * Draws the tile in the given board.
		 * @param board is the given board.
		 */
    	public void drawTile(final Board board) {
			assignTileColor();
    		putImageToTile(board);
    		markPossibleMoves(board);
    		validate();
    		repaint();
		}
		
    	/**
    	 * Puts an image into a tile (used to show the tools in the chess game).
    	 * @param board is the given board.
    	 */
		private void putImageToTile(final Board board) {
			this.removeAll();
			if (board.getTile(this.position).isTileOccupied()) {
				try {
					Piece p = board.getTile(this.position).getStandingPiece();
					String path = defaultImagesPath + p.getPieceColor()
					.toString().substring(0, 1) + p.toString() + ".gif";
					final BufferedImage image = ImageIO.read(new File(path));
					add(new JLabel(new ImageIcon(image)));
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		/**
		 * Marks the possible moves.
		 * @param board is the given board.
		 */
		private void markPossibleMoves(final Board board) {
			if (executeHighlightPossibleMoves) {
				for (final Move move : piecePossibleMoves(board)) {
					if (move.getDestinationPosition() == this.position) {
						try {
							add(new JLabel(new ImageIcon(ImageIO.read(new File("art/possibleMoveGif.gif")))));
						} catch (Exception e) { System.out.println("An exception occured"); }
					}
				}
			}
		}
		
		/**
		 * Calculates and returns the possible moves for a given piece.
		 * @param board is the given board.
		 * @return the possible moves for a given piece.
		 */
		private Collection<Move> piecePossibleMoves(final Board board) {
			if (movedPiece != null && movedPiece.getPieceColor() == board.currentPlayer().getColor()) {
				return movedPiece.calculatePossibleMoves(board);
			}
			return Collections.emptyList();
		}
		
		/**
		 * Assigns a color to a tile.
		 */
		private void assignTileColor() {
			if (Helper.EIGHTH_R[this.position] ||
					Helper.SIXTH_R[this.position] ||
					Helper.FOURTH_R[this.position] ||
					Helper.SECOND_R[this.position]) {
				setBackground(this.position % 2 == 0 ? lightTileColor : darkTileColor);
			} else if (Helper.SEVENTH_R[this.position] ||
					Helper.FIFTH_R[this.position] ||
					Helper.THIRD_R[this.position] ||
					Helper.FIRST_R[this.position]) {
				setBackground(this.position % 2 == 0 ? darkTileColor : lightTileColor);
			}
		}
	}
	
	/**
	 * Creates the preferences menu.
	 * @return the JMenu of the preferences menu.
	 */
	private JMenu createPreferencesMenu() {
		final JMenu preferencesMenu = new JMenu("Preferences");
		final JMenuItem flipBoardMenuItem = new JMenuItem("Flip Board");
		flipBoardMenuItem.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				boardDirection = boardDirection.opposite();
				boardPanel.drawBoard(board);
				boardDirection = boardDirection.opposite();
				boardPanel.drawBoard(board);
			}
		});
		preferencesMenu.add(flipBoardMenuItem);
		preferencesMenu.addSeparator();
		final JCheckBoxMenuItem highlightPossibleMoves = new JCheckBoxMenuItem("Highlight possible moves", false);
		highlightPossibleMoves.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				executeHighlightPossibleMoves = highlightPossibleMoves.isSelected();
				
			}
		});
		preferencesMenu.add(highlightPossibleMoves);
		return preferencesMenu;
	}
	
	/**
	 * Creates the option menu.
	 * @return the JMenu of the option menu.
	 */
	private JMenu createOptionMenu() {
		final JMenu optionsMenu = new JMenu("Options");
		final JMenuItem setupGameMenuItem = new JMenuItem("Setup Game");
		setupGameMenuItem.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				BoardGUI.get().getGameSetup().promptUser();
				BoardGUI.get().setupUpdate(BoardGUI.get().getGameSetup());
			}
		});
		optionsMenu.add(setupGameMenuItem);
		final JMenuItem timeModeMenuItem = new JMenuItem("Time Mode");
		timeModeMenuItem.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				Thread thread = new Thread(){
				    public void run(){
				    	countTimeForTurn();
				    }
				};
				  thread.start();
				
				BoardGUI.get().setupUpdate(BoardGUI.get().getGameSetup());
			}
		});
		optionsMenu.add(timeModeMenuItem);
		return optionsMenu;
	}
	
	/**
	 * Updates the setup
	 * @param gameSetup is the given setup this needs to be updated.
	 */
	private void setupUpdate(final GameSetup gameSetup) {
		setChanged();
		notifyObservers(gameSetup);
	}
	
	/**
	 * Represents the observer of the board that announces important things like check mate.
	 * @author Omer Shitrit
	 *
	 */
	private static class BoardObserver implements Observer {

		@Override
		public void update(final Observable arg0, final Object arg1) {
			if (BoardGUI.get().getGameSetup().isAIPlayer(BoardGUI.get().getBoard().currentPlayer()) &&
					!BoardGUI.get().getBoard().currentPlayer().isInCheckMate() &&
					!BoardGUI.get().getBoard().currentPlayer().isInStaleMate()) {
				// create an AI thread
				// execute AI work
				final AIBot bot = new AIBot();
				bot.execute();
			}
			if (BoardGUI.get().getBoard().currentPlayer().isInCheckMate()) {
				System.out.println("game over, " + BoardGUI.get().getBoard().currentPlayer() + " is in checkmate!");
			}
			if (BoardGUI.get().getBoard().currentPlayer().isInStaleMate()) {
				System.out.println("game over, " + BoardGUI.get().getBoard().currentPlayer() + " is in stalemate!");
			}
		}
		
	}
	
	/**
	 * Updates the board after a move were implemented.
	 * @param board the board after the change.
	 */
	public void updateBoardAfterBotMove(final Board board) { this.board = board; }
	
	/**
	 * Assigns the move that were found by the AI algorithm to the bot.
	 * @param move is the chosen move.
	 */
	public void updateBotMove(final Move move) { this.botMove = move; }
	
	/**
	 * Returns the board panel.
	 * @return the board panel.
	 */
	private BoardPanel getBoardPanel() { return this.boardPanel; }
	
	/**
	 * Updates when a move was made.
	 * @param playerType
	 */
	private void moveMadeUpdate(final PlayerType playerType) {
		setChanged();
		notifyObservers(playerType);
	}
	
	/**
	 * This class represents the bot that function by the help of the AI algorithm.
	 * @author Omer Shitrit
	 *
	 */
	private static class AIBot extends SwingWorker<Move, String> {
		
		/**
		 * An empty constructor.
		 */
		private AIBot() {
			
		}
		
		/**
		 * Finds the best possible move and returns it.
		 */
		@Override
		protected Move doInBackground() throws Exception {
			final MoveStrategy miniMax = new MiniMax(BoardGUI.get().depth);
			final Move bestMove = miniMax.execute(BoardGUI.get().getBoard());
			return bestMove;
		}
		
		/**
		 * A series of functions that need to be done after a move was made.
		 */
		@Override
		public void done() {
			try {
				final Move bestMove = get();
				BoardGUI.get().updateBotMove(bestMove);
				BoardGUI.get().updateBoardAfterBotMove(BoardGUI.get().getBoard().currentPlayer().makeMove(bestMove).getTransitionBoard());
				BoardGUI.get().getBoardPanel().drawBoard(BoardGUI.get().getBoard());
				BoardGUI.get().moveMadeUpdate(PlayerType.COMPUTER);
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
		
	}
	
	/**
	 * An enum for the flip board feature.
	 * @author Omer Shitrit
	 *
	 */
	public enum BoardDirection {
		NORMAL {
			List<TilePanel> swap(final List<TilePanel> tiles) { return tiles; }
			@Override
			BoardDirection opposite() { return FLIPPED; }
		},
		FLIPPED {
			List<TilePanel> swap(final List<TilePanel> tiles) { 
				Collections.reverse(tiles);
				return tiles;
			}
			BoardDirection opposite() { return NORMAL; }
		};
		abstract List<TilePanel> swap(final List<TilePanel> tiles);
		abstract BoardDirection opposite();
	}
	
	/**
	 * An enum that differs between a human being and a bot.
	 * @author Omer Shitrit
	 *
	 */
	public enum PlayerType {
		HUMAN,
		COMPUTER
	}
	
}
