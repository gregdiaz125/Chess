package objects;
/**
 * @author Gregory Diaz gld55
 * @author Anton Krotenok ak1847
 */
public class Position {

    /**
     * the x and y instance values that correspond to an (x,y) position on the chess board
     */
    public int x;
    public int y;

    public Position(int x, int y) {
        this.x = x;
        this.y =  y;
    }

    /**
     * grabs the x-coordinate of a Position object
     * @return the numerical integer value of the "x" instance variable
     */
    public int getX() {
        return this.x;
    }

    /**
     * grabs the y-coordinate of a Position object
     * @return the numerical integer value of the "y" instance variable
     */
    public int getY() {
        return this.y;
    }

    /**
     * check to see if positon is in bounds.
     * @return
     */
    public boolean isPosOutBound(){
        if (this.getX() < 0 || this.getY() < 0 || this.getX() > 7 || this.getY() > 7){
            return true;
        }
        return false;
    }

}
