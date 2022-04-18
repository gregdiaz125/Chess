package objects;
import board.*;

import java.util.ArrayList;

/**
 * @author Gregory Diaz gld55
 * @author Anton Krotenok ak1847
 */
public abstract class Piece {

    /**
     * x and y values of a piece
     */
    int x;
    int y;

    /**
     * enum that enumerate the different types of pieces
     */
    enum pieceType {KING, QUEEN, BISHOP, KNIGHT, ROOK, PAWN }



    /**
     * the type of the piece one of an enum
     */
    pieceType type;

    /**
     * represents black(false) or white(true) in boolean
     */
    Boolean color;

    /**
     * value to check if pawn has moved by two
     */
    public boolean hasmoved;

    /**
     * get the color of the Piece object
     * @return true for white false for black
     */
    public boolean getColor(){
        return this.color;
    }

    /**
     * checks the validity of the move for piece object
     * @param startPosition The first input of the users
     * @param endPosition   The second input of the user
     * @return false or true
     */
    public abstract boolean isValid(Position startPosition, Position endPosition);

    public boolean islegalmove(Position startPosition, Position endPosition){
        Piece p = Board.getPiece(startPosition);
        Piece tmp = Board.getPiece(endPosition);
        Piece tmp2 = null;
        boolean islegal = true;

        if(!p.isValid(startPosition, endPosition)){
            return false;
        }
        if(Board.isMoveEnpassant()){
            tmp2 = Board.getPiece(endPosition.getX(), startPosition.getY());
            Board.clearPiece(endPosition.getX(), startPosition.getY());
        }

        Board.setPiece(p, endPosition);
        Board.clearPiece(startPosition);
        if(iskingincheck(Board.getKing(p.color), p.color)){
            islegal = false;
        }

        Board.setPiece(p, startPosition);

        if(tmp == null){
            Board.clearPiece(endPosition);
        }else{
            Board.setPiece(tmp, endPosition);
        }

        if(Board.isMoveEnpassant()){
            Board.setPiece(tmp2, new Position(endPosition.getX(), startPosition.getY()));
        }

        return islegal;
    }

    public  boolean ispathclear(Position startPosition, Position endPosition){
        int dX = endPosition.getX() - startPosition.getX();
        int dY = endPosition.getY() - startPosition.getY();
        int diff;
        if((dX == 0 ^ dY == 0)) {
            diff = ((Math.abs(dX) + Math.abs(dY)));
        }
        else if(Math.abs(dX) == Math.abs(dY)) {
            diff = ((Math.abs(dX) + Math.abs(dY))) / 2;
        }
        else {
            return false;
        }

        dX = dX / diff;
        dY = dY / diff;

        for(int i = 1; i < diff; i++) {
            if(Board.getPiece(new Position((startPosition.getX() + (dX * i)), (startPosition.getY() + (dY*i))))  != null) {
                return false;
            }
        }
        return true;
    }

    public static ArrayList<Position> getAttackpath(Position startPosition, Position endPosition){
        ArrayList<Position> attackPath = new ArrayList<Position>();
        int dX = endPosition.getX() - startPosition.getX();
        int dY = endPosition.getY() - startPosition.getY();
        int diff;
        if((dX == 0 ^ dY == 0)) {
            diff = ((Math.abs(dX) + Math.abs(dY)));
        }
        else if(Math.abs(dX) == Math.abs(dY)) {
            diff = ((Math.abs(dX) + Math.abs(dY))) / 2;
        }
        else {
            return attackPath;
        }

        dX = dX / diff;
        dY = dY / diff;

        for(int i = 1; i < diff; i++) {
            Position tmp = new Position((startPosition.getX() + (dX * i)), (startPosition.getY() + (dY*i)));
            attackPath.add(tmp);

        }
        return attackPath;

    }

    public void movepiece(Position startPosition, Position endPosition){
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
     * give a position returns true or false if the king of current player would be at check in Posistion pos
     * @param pos
     * @return true or flase
     */

    public static boolean isSheild(Position pos, boolean team){
        // can technically remove
        Position kingPos = Board.getKing(team);
        int x = pos.getX();
        int y = pos.getX();
        int dX = x - kingPos.getY();
        int dY = y - kingPos.getY();
        Piece p;
        if(dX == 0 ^ dY == 0){
            if(dX > 0){
                for(int i = x +1; i < 8; i++ ){
                    p = Board.getPiece(new Position(i , y));
                    if(p != null){
                        if(p.color != team && p.type == pieceType.ROOK || p.type == pieceType.QUEEN){
                            return true;
                        }
                    }
                }
            }else if(dX < 0){
                for(int i = x -1; i >0; i-- ){
                    p = Board.getPiece(new Position(i , y));
                    if(p != null) {
                        if (p.color != team && p.type == pieceType.ROOK || p.type == pieceType.QUEEN) {
                            return true;
                        }
                    }
                }
            } else if(dY > 0){
                for(int i = y +1; i < 8; i++ ){
                    p = Board.getPiece(new Position(x , i));
                    if(p != null){
                        if(p.color != team && p.type == pieceType.ROOK || p.type == pieceType.QUEEN){
                            return true;
                        }
                    }
                }
            }else if(dY < 0){
                for(int i = y -1; i >0; i-- ){
                    p = Board.getPiece(new Position(x , i));
                    if(p != null) {
                        if (p.color != team && p.type == pieceType.ROOK || p.type == pieceType.QUEEN) {
                            return true;
                        }
                    }
                }
            }
        }

        if(Math.abs(dX/dY) == 1){
            dX /= dX;
            dY /= dY;
            for (int i = Math.abs(Math.min(8-x, 8 - y)) ; i < 9 ; i++){
                p =  Board.getPiece(new Position(x - i * dX , y - i * dY));
                if(p.color != team && p.type == pieceType.BISHOP || p.type == pieceType.QUEEN){
                    return true;
                }
            }

        }


        return false;
    }


    public static boolean isCheckMate(boolean team){
        Position kingPosition = Board.getKing(team);
        Piece king = Board.getPiece(kingPosition);
        int x = kingPosition.getX();
        int y = kingPosition.getY();

        /// check to see if we can move the king out of check
        int x_cor[] = {-1, 0 ,1};
        int y_cor[] = {-1, 0 , 1};
        for(int i = 0; i < 3; i++){
            for (int j = 0; j < 3; j++){
                if(i ==1 && j == 1){
                    continue;
                }
                Position p = new Position(x + x_cor[i],y + y_cor[j]);
                if(p.isPosOutBound()){
                    continue;
                }
                if(king.islegalmove(kingPosition,p)){
                    return false;
                }
            }
        }

        //check for
        Position enemyPosition[] = piecesCheckingKing(Board.getKing(team), team);
        ArrayList<Position> attackline= new ArrayList<Position>();
        for ( int s = 0 ; s < enemyPosition.length ; s++ ){
            attackline.add(enemyPosition[s]);
            Piece enemy = Board.getPiece(enemyPosition[s]);
            switch (enemy.type){
                case QUEEN :
                case BISHOP :
                case ROOK :
                    attackline.addAll(getAttackpath(kingPosition, enemyPosition[s]));
                default:
            }
            for( int i = 0 ; i < 8; i++ ){
                for(int j = 0; j< 8; j++  ){
                    Piece p = Board.getPiece(i,j);
                    if(p != null){
                        if(p.color == team && p.type != pieceType.KING){
                            for(int k = 0; k < attackline.size(); k++){
                                if(p.islegalmove(new Position(i,j), attackline.get(k))){
                                    return false;
                                }
                            }
                        }
                    }
                }
            }

        }

        return true;
    }




    public static Position[] piecesCheckingKing(Position pos, boolean team){
        ArrayList<Position> enimeypieces = new ArrayList<Position>();

        int x = pos.getX();
        int y = pos.getY();



        //check to the left of the king
        Position temp =new Position(x -1, (team? y- 1 : y +1) );
        Piece p = Board.getPiece(temp);
        if( p!= null){
            if(p.color != team){
                if(p.type == pieceType.PAWN){
                    enimeypieces.add(temp);
                }
            }
        }
        // check to the right of the king
        temp = new Position(x +1, (team? y- 1 : y +1) );
        p = Board.getPiece(temp);
        if( p!= null){
            if(p.color != team){
                if(p.type == pieceType.PAWN){
                    enimeypieces.add(temp);
                }
            }
        }

        //checking for knights
        Position kight_pos[]= { new Position(x+1,y+2),new Position(y+2,y+1),new Position(y-1,y+2),
                new Position(x-2,y+1), new Position(y+1,y-2),new Position(y+2,y-1),
                new Position(x-1,y-2),new Position(y-2,y-1) };

        for ( int i = 0; i < 8 ; i++){
            p = Board.getPiece(kight_pos[i]);
            if(p != null){
                if(p.color != team && p.type == pieceType.KNIGHT){
                    enimeypieces.add(kight_pos[i]);
                }
            }
        }
        //checking to the right
        for(int i = x+1 ; i <= 7; i ++ ){
            temp = new Position(i,y);
            p = Board.getPiece(temp);
            if( p != null){
                if (p.color != team){
                    if(p.type == pieceType.ROOK || p.type == pieceType.QUEEN){
                        enimeypieces.add(temp);
                    }
                }
                break;
            }
        }
        //checking to the left
        for(int i = x -1 ; i >= 0; i -- ){
            temp = (new Position(i,y));
            p = Board.getPiece(temp);
            if( p != null){
                if (p.color != team){
                    if(p.type == pieceType.ROOK || p.type == pieceType.QUEEN){
                        enimeypieces.add(temp);
                    }

                }
                break;
            }
        }
        //checking down
        for(int i = y+1 ; i <= 7; i ++ ){
            temp = (new Position(x,i));
            p = Board.getPiece(temp);
            if( p != null){
                if (p.color != team){
                    if(p.type == pieceType.ROOK || p.type == pieceType.QUEEN){
                        enimeypieces.add(temp);
                    }
                }
                break;
            }
        }
        //checking up
        for(int i = y -1 ; i >= 0; i-- ){
            temp = (new Position(x,i));
            p = Board.getPiece(temp);
            if( p != null){
                if (p.color != team){
                    if(p.type == pieceType.ROOK || p.type == pieceType.QUEEN){
                        enimeypieces.add(temp);
                    }
                }
                break;
            }
        }

        //check diangonally ++
        for( int i = 1 ; i < Math.min(8-y, 8 - x) ; i ++  ){
            temp = new Position(x+ 1, y+1);
            p = Board.getPiece(temp);
            if(p !=null){
                if (p.color != team){
                    if(p.type == pieceType.BISHOP || p.type == pieceType.QUEEN){
                        enimeypieces.add(temp);
                    }
                }
                break;
            }
        }
        //check diangonally --
        for( int i = 1 ; i < Math.min(y, x) ; i ++  ){
            temp = new Position(x-1 , y-1);

            p = Board.getPiece(temp);
            if(p !=null){
                if (p.color != team){
                    if(p.type == pieceType.BISHOP || p.type == pieceType.QUEEN){
                        enimeypieces.add(temp);
                    }
                }
                break;
            }
        }
        //check diangonally +-
        for( int i = 1 ; i < Math.min(y,8 - x) ; i ++  ){
            temp = new Position(x+ 1, y-1);
            p = Board.getPiece(temp);
            if(p !=null){
                if (p.color != team){
                    if(p.type == pieceType.BISHOP || p.type == pieceType.QUEEN){
                        enimeypieces.add(temp);
                    }
                }
                break;
            }
        }
        //check diangonally -+
        for( int i = 1 ; i < Math.min(8 -y, x) ; i ++  ){
            temp = new Position(x- 1, y+1);
            p = Board.getPiece(temp);
            if(p !=null){
                if (p.color != team){
                    if(p.type == pieceType.BISHOP || p.type == pieceType.QUEEN){
                        enimeypieces.add(temp);
                    }
                }
                break;
            }
        }
        return enimeypieces.toArray(new Position[0]);


    }

    public static boolean iskingincheck(Position pos, boolean team){
       int x = pos.getX();
       int y = pos.getY();

       //check to the left of the king
       Piece p = Board.getPiece(new Position(x -1, (team? y- 1 : y +1) ));
        if( p!= null){
            if(p.color != team){
                if(p.type == pieceType.PAWN){
                    return true;
                }
            }
        }
        // check to the right of the king
        p = Board.getPiece(new Position(x +1, (team? y- 1 : y +1) ));
        if( p!= null){
            if(p.color != team){
                if(p.type == pieceType.PAWN){
                    return true;
                }
            }
        }

        //checking for knights
        Position kight_pos[]= { new Position(x+1,y+2),new Position(y+2,y+1),new Position(y-1,y+2),
                                new Position(x-2,y+1), new Position(y+1,y-2),new Position(y+2,y-1),
                                new Position(x-1,y-2),new Position(y-2,y-1) };

        for ( int i = 0; i < 8 ; i++){
            p = Board.getPiece(kight_pos[i]);
            if(p != null){
                if(p.color != team && p.type == pieceType.KNIGHT){
                    return true;
                }
            }
        }
       //checking to the right
       for(int i = x+1 ; i <= 7; i ++ ){
            p = Board.getPiece((new Position(i,y)));
           if( p != null){
               if (p.color != team){
                   if(p.type == pieceType.ROOK || p.type == pieceType.QUEEN){
                       return true;
                   }
               }
               break;
           }
       }
       //checking to the left
        for(int i = x -1 ; i >= 0; i -- ){
             p = Board.getPiece((new Position(i,y)));
            if( p != null){
                if (p.color != team){
                    if(p.type == pieceType.ROOK || p.type == pieceType.QUEEN){
                        return true;
                    }

                }
                break;
            }
        }
        //checking down
        for(int i = y+1 ; i <= 7; i ++ ){
             p = Board.getPiece((new Position(x,i)));
            if( p != null){
                if (p.color != team){
                    if(p.type == pieceType.ROOK || p.type == pieceType.QUEEN){
                        return true;
                    }
                }
                break;
            }
        }
        //checking up
        for(int i = y -1 ; i >= 0; i-- ){
             p = Board.getPiece((new Position(x,i)));
            if( p != null){
                if (p.color != team){
                    if(p.type == pieceType.ROOK || p.type == pieceType.QUEEN){
                        return true;
                    }
                }
                break;
            }
        }

        //check diangonally ++
        for( int i = 1 ; i < Math.min(8-y, 8 - x) ; i ++  ){
            p = Board.getPiece(x + i, y + i);
            if(p !=null){
                if (p.color != team){
                    if(p.type == pieceType.BISHOP || p.type == pieceType.QUEEN){
                        return true;
                    }
                }
                break;
            }
        }
        //check diangonally --
        for( int i = 1 ; i < Math.min(y, x) ; i ++  ){
            p = Board.getPiece(x - i, y - i);
            if(p !=null){
                if (p.color != team){
                    if(p.type == pieceType.BISHOP || p.type == pieceType.QUEEN){
                        return true;
                    }
                }
                break;
            }
        }
        //check diangonally +-
        for( int i = 1 ; i < Math.min(y,8 - x) ; i ++  ){
            p = Board.getPiece(x + i, y - i);
            if(p !=null){
                if (p.color != team){
                    if(p.type == pieceType.BISHOP || p.type == pieceType.QUEEN){
                        return true;
                    }
                }
                break;
            }
        }
        //check diangonally -+
        for( int i = 1 ; i < Math.min(8 -y, x) ; i ++  ){
            p = Board.getPiece(x - i, y + i);
            if(p !=null){
                if (p.color != team){
                    if(p.type == pieceType.BISHOP || p.type == pieceType.QUEEN){
                        return true;
                    }
                }
                break;
            }
        }
        return false;
    }


    /**
     * Checks to see if the player choose a Piece of its color and if the next location is a pawn of the same color
     * @param firstP
     * the piece of the first location selected by the player;
     * @param secondP
     * the piece of the second location selected by the player;
     * @return True if conditon met else false
     */
    public boolean colorCheck(Piece firstP, Piece secondP){
        //the first piece does not belong to the player
        if(firstP.getColor() != Board.isTurn()){
            return false;
        }
        if( secondP != null) {
            if (firstP.getColor() == secondP.getColor()) {
                return false;
            }
        }
       return true;
    }


}
