package objects;
import board.*;

/**
 * @author Gregory Diaz gld55
 * @author Anton Krotenok ak1847
 */
public class Pawn extends Piece {
    /**
     * value to see if pawn has moved before
     */
    private boolean movedtwo;

    /**
     * Char value for the promotion of the pawn
     */
    private char promotion;

    public Pawn(int x_value, int y_value, boolean color) {
        this.color = color;
        this.hasmoved = false;
        this.type = pieceType.PAWN;
        this.movedtwo = false;
        promotion = 'Q';
    }

    public String toString() {
        return "" + (this.color ? 'w' : 'b') + "p";
    }

    /**
     * Checks to see if pawn has moved two
     * @return True if pawn has moved by two or false
     */
    public boolean movedTwo(){
        return this.movedtwo;
    }

    /**
     * sets the movedtwo boolean to true
     */
    public void setMovedtwo(){
        this.movedtwo = true;
    }

    /**
     * Sets the Promotion of Pawn object to specific char value of Piece
     * @param c represents the piece in a character
     */
    public void SetPromotion(char c){
        this.promotion = c;
    }

    /**
     * checks the validity of the move for Pawn object
     * @param startPosition The first input of the users
     * @param endPosition   The second input of the user
     * @return false or true
     */
    public boolean isValid(Position startPosition, Position endPosition) {

        Pawn beginPiece = (Pawn) Board.getPiece(startPosition);
        Piece finishPiece = Board.getPiece(endPosition);
        if (!(colorCheck(beginPiece, finishPiece))) {
            return false;
        }

        int differenceX = startPosition.getX() - endPosition.getX();

        //if white pawn then it should have the endY < startY (pawn's Y is decreasing)
        //if black pawn then it should have the endY > startY (pawn's Y in increasing)
        int differenceY = Board.isTurn() ? startPosition.getY() - endPosition.getY() : endPosition.getY() - startPosition.getY();
        //if y is negative then the pawn is moving back words
        if (0 > differenceY || differenceY > 2 || -1 > differenceX || differenceX > 1) {
            return false;
        }
        //case if pawn moves forward
        if (differenceX == 0){
            if(finishPiece != null){
                return false;
            }
            if(differenceY == 1){
                // white promotes at Y = 0 and Black at Y = 8


                return true;
            }
            if(differenceY == 2 && !(beginPiece.hasmoved)){
                beginPiece.setMovedtwo();
                return true;
            }
            return false;
        }

        //case if pawn is moving diagonal
        if (finishPiece == null){
            return checkenpassant(startPosition,endPosition);
        }
        return true;
    }

    /**
     * check to see if an enpassant is legal
     * @param startPosition the first input of user
     * @param endPosition   the second input of user
     * @return false if enpassant not legal, true and removes enemy pawn
     */
    public  boolean checkenpassant(Position startPosition, Position endPosition){
        if(Board.isMoveEnpassant()){
            return true;
        }

        ///lack and only enpass on y =4
        //White can only enpass on y =3
        if(startPosition.getY() != (Board.isTurn() ? 3 : 4)){ return false;}

        //load the position of last move and the piece
        Position lastmove = Board.getPreviousPosition();
        Piece lastpiece = Board.getPiece(lastmove);

        //check to see is piece type is of Pawn
        if(lastpiece.type != pieceType.PAWN){ return false;}

        //checked if the last pawn has move up by 2
        if(!((Pawn)(lastpiece)).movedTwo()){return false;}

        //checked if the enpassant pawn is on the same X axis as final possiton
        if(lastmove.getX() != endPosition.getX()){ return false; }

        Board.SetMove(Board.moveType.EMPASSANT);
        return true;
    }

    public void enpassant(Position startPosition, Position endPosition){
        Board.clearPiece(endPosition.getX(), startPosition.getY());
    }

    public void movepiece(Position startPosition, Position endPosition){
        if(endPosition.getY() == (Board.isTurn() ? 0 : 7) ){
            pawnPromotion(startPosition);
        }else if (checkenpassant(startPosition,endPosition)){
            enpassant( startPosition, endPosition);
        }
        //record that the peice has moved
        Board.getPiece(startPosition).hasmoved = true;
        //set the previous move to new move
        Board.setPreviousmove(endPosition);
        // set the piece at starting postion to end positon
        //we reget peice for pessant
        Board.setPiece(Board.getPiece(startPosition) , endPosition);
        // set the startpostion to null
        Board.clearPiece(startPosition);
    }

    /**
     *  Replaces pawn about to be promoted with the desired peice
     * @param pos the current position of the pawn to be promoted
     */
    public void pawnPromotion(Position pos) {
        int x = pos.getX();
        int y = pos.getY();
        boolean c = Board.isTurn();
        Piece promoted = switch (this.promotion) {
            case 'R' -> new Rook(x, y, c);
            case 'N' -> new Knight(x, y, c);
            case 'B' -> new Bishop(x, y, c);
            default -> new Queen(x, y, c);
        };
        Board.setPiece(promoted, pos);
    }
}
