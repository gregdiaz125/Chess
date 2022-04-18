package objects;

import board.*;
/**
 * @author Gregory Diaz gld55
 * @author Anton Krotenok ak1847
 */
public class King extends Piece {

    int x_value;
    int y_value;

    public King(int x_value, int y_value,  boolean color) {
        this.x_value = x_value;
        this.y_value = y_value;
        this.color  = color;
        this.type = pieceType.KING;
        this.hasmoved = false;
    }

    public String toString() {
        return "" + (this.color ? 'w' : 'b') + "K";
    }

    /**
     * checks the validity of the move for pawn object
     * @param startPosition The first input of the users
     * @param endPosition   The second input of the user
     * @return false or true
     */
    public  boolean isValid(Position startPosition, Position endPosition) {
        King beginPiece = (King) Board.getPiece(startPosition);
        Piece finishPiece = Board.getPiece(endPosition);
        if (!(colorCheck(beginPiece, finishPiece))) {
            return false;
        }
        //checking for castling
        if( endPosition.getY() == (Board.isTurn() ? 7 : 0) && (endPosition.getX() == 6 || endPosition.getX() == 2) && !beginPiece.hasmoved){
            return isCastle(startPosition, endPosition);
        }
        int dX = endPosition.getX() - startPosition.getX();
        int dY = endPosition.getY() - startPosition.getY();
        // check that the end postion if with in a one step of start position
        if(dX > 1 || dX < -1 || dY >1 || dY < -1 ){
            return false;
        }

        if ((Math.abs(dX) + Math.abs(dY)) /2 > 1){
            return false;
        }

        return true;
    }

    /**
     * Checks to see if the the fesible move is legal in regard with the rules of chess
     * @param startPosition
     * @param endPosition
     * @return
     */
    public boolean islegalmove(Position startPosition, Position endPosition){
        Piece p = Board.getPiece(startPosition);
        Piece tmp = Board.getPiece(endPosition);
        Piece tmp2 = null;
        boolean islegal = true;

        if(!p.isValid(startPosition, endPosition)){
            return false;
        }
        if(Board.isMoveCastle()){
            tmp2 = Board.getPiece(( endPosition.getX() == 6? 7 : 0), endPosition.getY());
            Board.clearPiece(( endPosition.getX() == 6? 7 : 0), endPosition.getY());
            Board.setPiece(tmp2, new Position(( endPosition.getX() == 6? 5 : 3), endPosition.getY()));
        }

        Board.setPiece(p, endPosition);
        Board.clearPiece(startPosition);
        if(iskingincheck(endPosition, p.color)){
            islegal = false;
        }

        Board.setPiece(p, startPosition);

        if(tmp == null){
            Board.clearPiece(endPosition);
        }else{
            Board.setPiece(tmp, endPosition);
        }

        if(Board.isMoveCastle()){
            Board.clearPiece(( endPosition.getX() == 6?  5 : 3), endPosition.getY());
            Board.setPiece(tmp2 , new Position(( endPosition.getX() == 6? 7 : 0), endPosition.getY()));
        }

        return islegal;
    }

    /**
     * performs the movement of the king from Start Position to endPosition
     * @param startPosition
     * @param endPosition
     */
    public void movepiece(Position startPosition, Position endPosition){
        if(isCastle(startPosition, endPosition) ){
            castle(startPosition, endPosition);
        }
        Board.setKing(Board.isTurn(), endPosition);
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
     * Checks to see if the castling condition is true for the king
     * @param startPosition
     * @param endPosition
     * @return
     */
    public boolean isCastle(Position startPosition, Position endPosition){
        if(Board.isMoveEnpassant()){
            return true;
        }
        if(iskingincheck(startPosition, Board.isTurn())){
            return false;
        }
        x = endPosition.getX() == 6? 7 : 0;
        y = endPosition.getY();
        Piece rook = Board.getPiece(x,y);
        if(rook == null ){
            return false;
        }
        if(rook.type != pieceType.ROOK){
            return false;
        }
        if(rook.hasmoved == true){
            return false;
        }
       if(!ispathclear(startPosition, new Position(x ,y))){
           return false;
       }
        Board.SetMove(Board.moveType.EMPASSANT);
       return true;
    }

    /**
     *moves the rock to be castled int the castling position
     * @param startPosition
     * @param endPosition
     */
    public void castle(Position startPosition, Position endPosition){
        y = startPosition.getY();
        x  = endPosition.getX() == 6? 7 : 0;
        Piece rook = Board.getPiece(x,y);
        Board.clearPiece(x,y);
        rook.hasmoved = true;
        x  = endPosition.getX() == 6? 5 : 3;
        Board.setPiece(rook, new Position(x, y));
    }

}
