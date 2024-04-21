import doodlepad.*;

/**
Man.java
@author Ethan Avera
@since 4/10/24
This class handles the Man (pieces) of the checkers game, and stores their data fur use within the program
*/
public class Man {
    int currentRow;
    int currentCol;
    int tileWidth;
    int tileHeight;
    Oval hitbox;

    /*
    Man class constructor
    Used to create Man objects.
    */
    public Man(int row, int col, int width, int height) {
        currentRow = row;
        currentCol = col;
        tileWidth = width;
        tileHeight = height;
        hitbox = new Oval(currentRow*tileWidth + 15, currentCol*tileHeight + 15, tileWidth-30, tileHeight-30);

    }
    /*
    Getter Method
    Returns the hitbox of a Man object
    */
    public Oval getHitbox() {
        return hitbox;
    }
}
