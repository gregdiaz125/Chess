package objects;

import board.*;

/**
 * @author Gregory Diaz gld55
 * @author Anton Krotenok ak1847
 */
public class Rook extends Piece {

    /**
     * the x and y instance values that correspond to an (x,y) coordinate value for a Rook object on the chess board
     */
    int x_value;
    int y_value;

    public Rook(int x_value, int y_value,  boolean color) {
        this.x_value = x_value;
        this.y_value = y_value;
        this.color  = color;
        this.type = pieceType.ROOK;
        this.hasmoved = false;
    }

    public String toString() {
        return "" + (this.color ? 'w' : 'b') + "R";
    }

    /**
     * checks the validity of the move for pawn object
     * @param startPosition The first input of the users
     * @param endPosition   The second input of the user
     * @return false or true
     */
    public boolean isValid(Position startPosition, Position endPosition) {

        Rook beginPiece = (Rook)Board.getPiece(startPosition);
        Piece finishPiece = Board.getPiece(endPosition);

        if (!(colorCheck(beginPiece, finishPiece))) {
            return false;
        }

        int differenceX = endPosition.getX() - startPosition.getX();
        int differenceY = endPosition.getY() - startPosition.getY();

        if(!(differenceX == 0 ^ differenceY == 0)) {
            return false;
        }
        int diff = ((Math.abs(differenceX) + Math.abs(differenceY)));
        differenceX = differenceX / diff;
        differenceY = differenceY / diff;


        for(int i = 1; i < diff; i++) {
            if(Board.getPiece(new Position((startPosition.getX() + (differenceX * i)), (startPosition.getY() + (differenceY*i))))  != null) {
                return false;
            }
        }

        return true;
    }

}
