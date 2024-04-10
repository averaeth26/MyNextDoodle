import doodlepad.*;
import java.util.ArrayList;

public class Checkers {
    int screenWidth = 1200;
    int screenHeight = 1200;
    int numTiles = 8;
    ArrayList<Man> pieces = new ArrayList<Man>();

    public Checkers() {
        drawBackground();
    }

    public void drawBackground() {
        int squareWidth = screenWidth/numTiles;
        int squareHeight = screenHeight/numTiles;
        ArrayList<Rectangle> gridSquares = new ArrayList<Rectangle>();
        for (int i = 0; i < numTiles; i++) {
            for (int j = 0; j < numTiles; j++) {
                gridSquares.add(new Rectangle(i*squareWidth, j*squareHeight, squareWidth, squareHeight));
                if ((i+j)%2 == 0) {
                    gridSquares.get(gridSquares.size()-1).setFillColor(220);
                }
                else {
                    gridSquares.get(gridSquares.size()-1).setFillColor(0);
                }
            }
        }
    }

    public void onPieceClicked(Shape shp, double x, double y, int button) {

    }

    public ArrayList<Man> generatePieces() {
        ArrayList<Man> currPieces = new ArrayList<Man>();
        for (int i = 0; i < numTiles; i++) {
            for (int j = 0; j < numTiles; j++) {
                if ((i+j)%2 != 0) {
                    currPieces.add(new Man(i, j));
                    currPieces.get(currPieces.size()-1).getHitbox().setMousePressedHandler(this::onPieceClicked);
                }
            }
        }
        return currPieces;
    }

}