package board;
import objects.*;
import java.util.Scanner;
/**
 * @author Gregory Diaz
 */
public class Board {

    public static Piece[][] board;
    private static boolean isWhiteTurn = true;
    public static final boolean Black = false;
    public static final boolean White = true;
    private static Position previous_move;
    private static Position kingPiece[];
    public enum moveType {MOVE, EMPASSANT, PROMOTION, CASTLE}
    private static moveType current_move;

    public Board() {
        board = new Piece[8][8];

        for(int i = 0; i < 8; i++) {
            board[1][i] = new Pawn(1, i, Black );
        }
        board[0][0] = new Rook(5, 3, Black);
        board[0][7] = new Rook(0, 7, Black);
        board[0][1] = new Knight(0,0, Black);
        board[0][6] = new Knight(0, 6, Black);
        board[0][2] = new Bishop(0, 2, Black);
        board[0][5] = new Bishop(0, 5, Black);
        board[0][3] = new Queen(0, 3, Black);
        board[0][4] = new King(0, 4, Black);

        for(int i = 0; i < 8; i++) {
            board[6][i] = new Pawn(6, i, White);
        }

        board[7][0] = new Rook(7, 0, White);
        board[7][7] = new Rook(7, 7, White);
        board[7][1] = new Knight(7,1, White);
        board[7][6] = new Knight(7, 6, White);
        board[7][2] = new Bishop(7, 2, White);
        board[7][5] = new Bishop(7, 5, White);
        board[7][3] = new Queen(7, 3, White);
        board[7][4] = new King(0, 4, White);

        kingPiece = new Position[]{new Position(4, 7), new Position(4, 0)};
    }

    /**
     *  Starts the game of chess
     *  process the user input for resign and draw
     */
    public void start() {

        Scanner scan = new Scanner(System.in);
        boolean declaredWinner = false;
        boolean offeredDraw = false;
        boolean lastMoveLegal = true;

        while(!declaredWinner) {
            if(lastMoveLegal) {
                printBoard();
            }
            System.out.print( (isTurn() ? "White's" : "Black's") + " move : ");

            String reader = scan.nextLine();
            String[] move = reader.split(" ");
            if(move[0].equals("draw")) {
                if(offeredDraw){
                    return;
                }
                lastMoveLegal = false;
                continue;
            }
            if (move.length == 3){
                if (move[2].equals("draw?") ){
                    offeredDraw = true;
                }
            }
            if(move[0].equals("resign")) {
                declaredWinner = true;
                continue;
            }

            if(move(reader)){
                switchTurn();
                lastMoveLegal = true;
                if(Piece.iskingincheck(getKing(isTurn()), isTurn())){
                    if(Piece.isCheckMate(isTurn())){
                        System.out.println("Checkmate");
                        declaredWinner = true;
                    }else{
                        System.out.println("Check");
                    }
                }
             }else{
                System.out.println("Illegal move, try again");
                lastMoveLegal = false;
            }

        }
        System.out.println((isTurn() ? "Black":"White")+ " wins");
    }

    /**
     *procesess the imput and calls legal move to check to move piece
     * @param input the input of the use
     * @return
     */
    public boolean move(String input) {
        if(input.length() < 5) {
            return false;
        }
        // split the imput into string arru
        String[] move = input.split(" ");
        Position startPosition = getBoardPosition(move[0]);
        Position endPosition = getBoardPosition(move[1]);

        //check if both positions are valid
        if(startPosition == null || endPosition == null) {
            return false;
        }

        //get eh Piece are the starting position
        Piece startPiece = getPiece(startPosition);
        if(startPiece == null) {
            return false;
        }

        // if move has a 3rd then it is a pawn thus set its Promotion char to move[3]
        if(move.length == 3){
            if (move[2].length() == 1){
                ((Pawn)startPiece).SetPromotion( move[2].charAt(0));
            }

        }

        clearMove();

        //check if start end is valid for Piece object
        if(startPiece.islegalmove(startPosition, endPosition)) {
            startPiece.movepiece(startPosition,endPosition);

        }else{
            return false;
        }
        return true;
    }


    /**
     * returns the variable previous_move
     * @return Positon object previous_move of board
     */
    public static Position getPreviousPosition() {
        return previous_move;
    }

    /**
     * Sets the variable previous_move to the Position object pos
     * @param pos
     */
    public static void setPreviousmove(Position pos){
        previous_move = pos;
    }

    /**
     * parses the input into two position
     * @param s
     * a string in the formant of xy xy where x is a-h and y is 0-7
     * @return
     * returns a postion or Null if the position is out of bounds
     */
    public Position getBoardPosition(String s) {
        char c1;
        char c2;
        int x;
        int y;

        c1 = s.charAt(0);
        c2 = s.charAt(1);

        switch(c1) {
            case 'a' : x = 0; break;
            case 'b' : x = 1; break;
            case 'c' : x = 2; break;
            case 'd' : x = 3; break;
            case 'e' : x = 4; break;
            case 'f' : x = 5; break;
            case 'g' : x = 6; break;
            case 'h' : x = 7; break;
            default: return null;
        }
        y = Character.getNumericValue(c2);
        y = 8 - y;

        if(y < 0 || y > 7) {
            return null;
        }
        return new Position(x, y);
    }

    /**
     *
     * @return Boolean value of the color of the current turn
     */
    public static boolean isTurn(){
        return isWhiteTurn;
    }

    /**
     * Switches the Turn to the other color
     */
    public static void switchTurn(){
        isWhiteTurn = !isWhiteTurn;
    }

    /**
     * returns a Piece Object that is at Position pos on the board
     * that details a desired postion on the board
     * @param pos a Position Object
     * @return a Piece Object
     */
    public static Piece getPiece(Position pos){
        if(pos.getX() > 7 || pos.getX() < 0|| pos.getY() < 0 || pos.getY() > 7){
            return null;
        }
        return board[pos.getY()][pos.getX()];
    }

    /**
     * returns a Piece Object that is at Position pos on the board
     * that details a desiredxyn on the board
     * @param x
     * @param y
     * @return piece at location xy on the board
     */
    public static Piece getPiece(int x, int y){
        return board[y][x];
    }

    /**
     * returns the position of the king of the team
     * @param color
     * @return
     */
    public static Position getKing(boolean color){
        return color ? ( kingPiece[0]) : ( kingPiece[1]);
    }

    /**
     * set the cordinate of the king of boolean color
     * @param color
     * @param pos
     */
    public static void setKing(boolean color, Position pos){
        kingPiece[color? 0 : 1] = pos;

    }
    /**
     * sets the Piece on the board at Posistion pos to piece
     * @param piece the piece obejct to set at pos
     * @param pos   the poston on the board
     */
    public static void setPiece(Piece piece, Position pos){
        board[pos.getY()][ pos.getX()] = piece;
    }

    /**
     * sets the peice at the board positon pos to null
     * @param pos the positon to set to null on the board
     */
    public static void clearPiece(Position pos){
        board[pos.getY()][ pos.getX()] = null;
    }

    /**
     * sets the piece at postion x, y to null
     * @param x
     * @param y
     */
    public static void clearPiece(int x, int y){
        board[y][ x] = null;
    }


    /**
     * checks to see if the move is a Castleing move
     * @return
     */
    public static boolean isMoveCastle(){
        if(current_move == moveType.CASTLE){
            return true;
        }
        return false;
    }
    /**
     * checks to see if the move is a Promoting move
     * @return
     */
    public static boolean isMovePromotion(){
        if(current_move == moveType.PROMOTION){
            return true;
        }
        return false;
    }

    /**
     * Checks to see if the move is an Enpassant move
     * @return
     */
    public static boolean isMoveEnpassant(){
        if(current_move == moveType.EMPASSANT){
            return true;
        }
        return false;
    }

    /**
     * clears the current Move
     */
    public void clearMove(){
        current_move = moveType.MOVE;
    }

    /**
     * setst the current move to the move that is provided
     * @param move
     */
    public static void SetMove(moveType move){
        current_move = move;
    }




    /**
     * Prints out in specific format the chess board instance
     */
    public void printBoard() {

        boolean spacer = false;
        System.out.println();
        for (int i = 0; i < 8; i++){
            for (int j = 0; j < 8; j++) {
                if(board[i][j] == null && spacer) {
                    System.out.print("## ");
                }
                else if(board[i][j] == null && !spacer) {
                    System.out.print("   ");
                }
                else {
                    System.out.print(board[i][j] + " ");
                }
                spacer = !spacer;
            }
            System.out.println(8 - i);
            spacer = !spacer;
        }
        System.out.println(" a  b  c  d  e  f  g  h\n");
    }

}
