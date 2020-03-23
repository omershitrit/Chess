/**
 *  Pawn piece implementation 
 */

package Pieces;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

import javax.swing.JOptionPane;

import Board.Board;
import Board.PlayerColor;
import Board.Move.PawnPromotion;
import Board.Helper;
import Board.Move;
import Board.Move.PawnMove;
import Board.Move.PawnEnPassantAttackMove;

public class Pawn extends Piece {

	// possible offsets for a pawn piece
	private final static short[] possibleOffsets = { 7, 8, 9, 16 };
	
	/**
	 *  Constructor for first move
	 * @param color is the given color.
	 * @param position is the given position
	 */
	public Pawn(final PlayerColor color, final short position) {
		super(position, color, Type.PAWN, true);
	}
	
	/**
	 *  Constructor
	 * @param color is the given color.
	 * @param position is the given position
	 * @param isFirstMove is a boolean value that indicates if it is the first move.
	 */
	public Pawn(final PlayerColor color, final short position, final boolean isFirstMove) {
		super(position, color, Type.PAWN, isFirstMove);
	}

	/**
	 *  Calculates possible moves
	 *  @return the possible moves.
	 */
	@Override
	public Collection<Move> calculatePossibleMoves(final Board board) {
		final List<Move> possibleMoves = new ArrayList<>();
		// iterate the possible offsets
		for (final short currentOffset : possibleOffsets) {
			final short tempPosition = (short) (this.position
					+ (this.getPieceColor().getDirection() * currentOffset));
			// if the new position is outside the board - continue
			if (!Helper.isTileValid(tempPosition)) { continue; }
			// if the move is valid - jump one step
			if (currentOffset == 8 && !board.getTile(tempPosition).isTileOccupied()) {
				if (this.getPieceColor().isPawnPromotionTile(tempPosition)) {
					possibleMoves.add(new PawnPromotion(new PawnMove(board, this, tempPosition)));
				} else {
					possibleMoves.add(new PawnMove(board, this, tempPosition));
				}
			// check if pawn is able to jump 2 steps at once move
			} else if (currentOffset == 16 && this.isFirstMove() &&
					((Helper.SEVENTH_R[this.position] && this.color.isBlack()) ||
					(Helper.SECOND_R[this.position] && this.color.isWhite()))) {
				final short firstStepPosition = (short) (this.position + (this.getPieceColor().getDirection() * 8));
				if (!board.getTile(firstStepPosition).isTileOccupied() &&
						!board.getTile(tempPosition).isTileOccupied()) {
					possibleMoves.add(new Move.PawnJump(board, this, tempPosition));
				}
			// check if this pawn in the edges
			} else if (currentOffset == 7 &&
					!((Helper.EIGHTH_C[this.position] && this.color.isWhite() ||
					(Helper.FIRST_C[this.position] && this.color.isBlack()) ))) {
				if (board.getTile(tempPosition).isTileOccupied()) {
					final Piece pieceAtDestination = board.getTile(tempPosition).getStandingPiece();
					if (this.color != pieceAtDestination.getPieceColor()) {
						if (this.getPieceColor().isPawnPromotionTile(tempPosition)) {
							possibleMoves.add(new PawnPromotion(new Move.PawnAttackMove(board, this, tempPosition, pieceAtDestination)));
						} else {
							possibleMoves.add(new Move.PawnAttackMove(board, this, tempPosition, pieceAtDestination));
						}
					}
				} else if (board.getEnPassantPawn() != null) {
					if (board.getEnPassantPawn().getPosition() == (this.position + (this.color.getOppositeDirection()))) {
						final Piece piece = board.getEnPassantPawn();
						if (this.getPieceColor() != piece.getPieceColor()) {
							possibleMoves.add(new PawnEnPassantAttackMove(board, this, tempPosition, piece));
						}
					}
				}
			// another edge
			} else if (currentOffset == 9 && 
					!((Helper.FIRST_C[this.position] && this.color.isWhite() ||
					 (Helper.EIGHTH_C[this.position] && this.color.isBlack()) ))) {
				if (board.getTile(tempPosition).isTileOccupied()) {
					final Piece pieceAtDestination = board.getTile(tempPosition).getStandingPiece();
					if (this.color != pieceAtDestination.getPieceColor()) {
						if (this.getPieceColor().isPawnPromotionTile(tempPosition)) {
							possibleMoves.add(new PawnPromotion(new Move.PawnAttackMove(board, this, tempPosition, pieceAtDestination)));
						} else {
							possibleMoves.add(new Move.PawnAttackMove(board, this, tempPosition, pieceAtDestination));
						}
					}
				} else if (board.getEnPassantPawn() != null) {
					if (board.getEnPassantPawn().getPosition() == (this.position - (this.color.getOppositeDirection()))) {
						final Piece piece = board.getEnPassantPawn();
						if (this.getPieceColor() != piece.getPieceColor()) {
							possibleMoves.add(new PawnEnPassantAttackMove(board, this, tempPosition, piece));
						}
					}
				}
			}
		}
		return Collections.unmodifiableList(possibleMoves);
	}
	
	/**
	 * to string function
	 */
	@Override
	public String toString() { return Type.PAWN.toString(); }
	
	/**
	 * Moves the piece according to the given move.
	 * @param move is the given move.
	 * @return the new piece.
	 */
	@Override
	public Pawn movePiece(final Move move) {
		return new Pawn(move.getPiece().getPieceColor(), move.getDestinationPosition());
	}
	
	/**
	 * Receives the user's choice according to the text below, and changes the pawn accordingly.
	 * @return the new piece after the change.
	 */
	public Piece getPromotionChoice() { 
		String text = "Insert [1-4] to promote the pawn:\n1 - Queen\n2 - knight\n3 - Rook\n4 - Bishop";
		int choice = Integer.parseInt(JOptionPane.showInputDialog(text));
		switch (choice) {
			case 1:
				return new Queen(this.getPieceColor(), this.position, false);
			case 2:
				return new Knight(this.getPieceColor(), this.position, false);
			case 3:
				return new Rook(this.getPieceColor(), this.position, false);
			case 4:
				return new Bishop(this.getPieceColor(), this.position, false);
		}
		return new Queen(this.getPieceColor(), this.position, false);
		};

}
