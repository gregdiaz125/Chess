package objects;

import board.Board;
/**
 * @author Gregory Diaz gld55
 * @author Anton Krotenok ak1847
 */
public class Knight extends Piece {

    int x_value;
    int y_value;

    public Knight(int x_value, int y_value,  boolean color) {
        this.x_value = x_value;
        this.y_value = y_value;
        this.color  = color;
        this.type = pieceType.KNIGHT;
        this.hasmoved = false;
    }

    public String toString() {
        return "" + (this.color ? 'w' : 'b') + "N";
    }

    /**
     * checks the validity of the move for piece object
     * @param startPosition The first input of the users
     * @param endPosition   The second input of the user
     * @return false or true
     */
    public boolean isValid(Position startPosition, Position endPosition) {

        Knight beginPiece = (Knight) Board.getPiece(startPosition);
        Piece finishPiece = Board.getPiece(endPosition);

        if (!(colorCheck(beginPiece, finishPiece))) {
            return false;
        }

        int differenceX = startPosition.getX() - endPosition.getX();
        int differenceY = Board.isTurn() ? startPosition.getY() - endPosition.getY() : endPosition.getY() - startPosition.getY();

        if(differenceX == 1 && (differenceY == 2 || differenceY == -2)) {
            return true;
        }
        if(differenceX == 2 && (differenceY == 1 || differenceY == -1)) {
            return true;
        }

        if(differenceX == -2 && (differenceY == 1 || differenceY == -1)) {
            return true;
        }

        if(differenceX == -1 && (differenceY == 2 || differenceY == -2)) {
            return true;
        }

        return false;
    }
}
