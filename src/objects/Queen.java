package objects;

import board.Board;
/**
 * @author Gregory Diaz gld55
 * @author Anton Krotenok ak1847
 */
public class Queen extends Piece {

    /**
     * the x and y instance values that correspond to an (x,y) coordinate value for a Rook object on the chess board
     */
    int x_value;
    int y_value;

    public Queen(int x_value, int y_value,  boolean color) {
        this.x_value = x_value;
        this.y_value = y_value;
        this.color  = color;
        this.type = pieceType.QUEEN;
        this.hasmoved = false;
    }

    public String toString() {
        return "" + (this.color ? 'w' : 'b') + "Q";
    }

    /**
     * checks the validity of the move for pawn object
     * @param startPosition The first input of the users
     * @param endPosition   The second input of the user
     * @return false or true
     */
    public boolean isValid(Position startPosition, Position endPosition) {

        Queen beginPiece = (Queen) Board.getPiece(startPosition);
        Piece finishPiece = Board.getPiece(endPosition);

        if (!(colorCheck(beginPiece, finishPiece))) {
            return false;
        }

        int differenceX = endPosition.getX() - startPosition.getX();
        int differenceY = endPosition.getY() - startPosition.getY();

        int diff;
        if((differenceX == 0 ^ differenceY == 0)) {
            diff = ((Math.abs(differenceX) + Math.abs(differenceY)));
        }
        else if(Math.abs(differenceX) == Math.abs(differenceY)) {
            diff = ((Math.abs(differenceX) + Math.abs(differenceY))) / 2;
        }
        else {
            return false;
        }

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
