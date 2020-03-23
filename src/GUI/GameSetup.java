/**
 * This class represents the "Setup Game" feature in the "Options" tab.
 * It is possible to set the depth of the AI algorithm; deeper meaning smarter.
 * It is also possible to set the properties of the game play, meaning play against
 * a friend or against the computer, or let the computer play against itself.
 */

package GUI;

import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSpinner;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;

import Board.PlayerColor;
import GUI.BoardGUI.PlayerType;
import Player.Player;

/* Panel */

public class GameSetup extends JDialog{
	
	private PlayerType whitePlayerType;
	private PlayerType blackPlayerType;
	private JSpinner searchDepthSpinner;
	
	private static final String HUMAN_TEXT = "Human";
	private static final String COMPUTER_TEXT = "Computer";
	
	/**
	 * Constructor
	 * @param frame is the given frame.
	 * @param model is the given model.
	 */
	GameSetup(final JFrame frame, final boolean model) {
		super(frame, model);
		final JPanel panel = new JPanel(new GridLayout(0, 1));
		final JRadioButton whiteHumanButton = new JRadioButton(HUMAN_TEXT);
		final JRadioButton blackComputerButton = new JRadioButton(COMPUTER_TEXT);
		final JRadioButton blackHumanButton= new JRadioButton(HUMAN_TEXT);
		final JRadioButton whiteComputerButton = new JRadioButton(COMPUTER_TEXT);
		whiteHumanButton.setActionCommand(HUMAN_TEXT);
		final ButtonGroup whiteGroup = new ButtonGroup();
		whiteGroup.add(whiteComputerButton);
		whiteGroup.add(whiteHumanButton);
		whiteHumanButton.setSelected(true);
		
		final ButtonGroup blackGroup = new ButtonGroup();
		blackGroup.add(blackComputerButton);
		blackGroup.add(blackHumanButton);
		blackComputerButton.setSelected(true);
		
		getContentPane().add(panel);
		panel.add(new JLabel("White"));
		panel.add(whiteHumanButton);
		panel.add(whiteComputerButton);
		panel.add(new JLabel("Black"));
		panel.add(blackComputerButton);
		panel.add(blackHumanButton);
		
		panel.add(new JLabel("Search"));
		this.searchDepthSpinner = addLabeledSpinner(panel, "Search Depth", new SpinnerNumberModel(2, 0, Integer.MAX_VALUE, 1));
		final JButton cancleButton = new JButton("Cancle");
		final JButton okButton = new JButton("OK");
		okButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				whitePlayerType = whiteComputerButton.isSelected() ? PlayerType.COMPUTER : PlayerType.HUMAN;
				blackPlayerType = blackComputerButton.isSelected() ? PlayerType.COMPUTER : PlayerType.HUMAN;
				int depth = (int)searchDepthSpinner.getValue();
				BoardGUI.get().setDepth(depth);
				GameSetup.this.setVisible(false);
			}
		});
		
		cancleButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("cancled");
				GameSetup.this.setVisible(false);
			}
		});
		
		panel.add(cancleButton);
		panel.add(okButton);
		setLocationRelativeTo(frame);
		pack();
		repaint();	
	}
	
	/**
	 * Adds a spinner for the depth area.
	 * @param c is the container of the spinner.
	 * @param label is the label of the spinner.
	 * @param model is the model of the spinner.
	 * @return the JSpinner.
	 */
	private JSpinner addLabeledSpinner(Container c, String label, SpinnerModel model) {
		final JLabel l = new JLabel(label);
		c.add(l);
		final JSpinner spinner = new JSpinner (model);
		l.setLabelFor(spinner);
		c.add(spinner);
		return spinner;
	}

	/**
	 * Prompts the user.
	 */
	public void promptUser() { 
		setVisible(true);
		repaint();
	}
	
	/**
	 * Returns true if the given player is a bot, o.w false.
	 * @param player is the given player.
	 * @return true or false.
	 */
	boolean isAIPlayer(final Player player) {
		if (player.getColor() == PlayerColor.WHITE) {
			return this.whitePlayerType == PlayerType.COMPUTER;
		}
		return this.blackPlayerType == blackPlayerType.COMPUTER;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
}
