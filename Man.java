import doodlepad.*;
import java.util.ArrayList;


public class Man {
    int currentRow;
    int currentCol;
    int tileWidth;
    int tileHeight;
    Oval hitbox;
    boolean king = false;
    public Man(int row, int col, int width, int height) {
        currentRow = row;
        currentCol = col;
        tileWidth = width;
        tileHeight = height;
        hitbox = new Oval(currentRow*tileWidth + 15, currentCol*tileHeight + 15, tileWidth-30, tileHeight-30);

    }

    public int getRow() {
        return currentRow;
    }
    public int getCol() {
        return currentCol;
    }

    public void move(int row, int col) {
        currentRow = row;
        currentCol = col;
    }

    public Oval getHitbox() {
        return hitbox;
    }
}
