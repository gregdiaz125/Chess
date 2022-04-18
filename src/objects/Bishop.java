package objects;

import board.Board;

public class Bishop extends Piece {

    /**
     * @author Gregory Diaz gld55
     * @author Anton Krotenok ak1847
     */
    int x_value;
    int y_value;

    public Bishop(int x_value, int y_value,  boolean color) {
        this.x_value = x_value;
        this.y_value = y_value;
        this.color  = color;
        this.type = pieceType.BISHOP;
        this.hasmoved = false;
    }

    public String toString() {
        return "" + (this.color ? 'w' : 'b') + "B";
    }


    /**
     * checks the validity of the move for pawn object
     * @param startPosition The first input of the users
     * @param endPosition   The second input of the user
     * @return false or true
     */
    public boolean isValid(Position startPosition, Position endPosition) {

        Bishop beginPiece = (Bishop) Board.getPiece(startPosition);
        Piece finishPiece = Board.getPiece(endPosition);

        if (!(colorCheck(beginPiece, finishPiece))) {
            return false;
        }

        int differenceX = endPosition.getX() - startPosition.getX();
        int differenceY = endPosition.getY() - startPosition.getY();

        //case if bishop moves diagonally in any direction
        if(Math.abs(differenceX) != Math.abs(differenceY)) {
            return false;
        }

        int diff = ((Math.abs(differenceX) + Math.abs(differenceY))) / 2;
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
