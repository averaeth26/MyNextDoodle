import doodlepad.*;


public class Man {
    int currentRow;
    int currentCol;
    Oval hitbox;
    boolean king = false;
    public Man(int row, int col) {
        currentRow = row;
        currentCol = col;
        hitbox = new Oval(currentRow*150 + 15, currentCol*150 + 15, 120, 120);
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
