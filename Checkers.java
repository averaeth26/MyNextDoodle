import doodlepad.*;
import java.util.ArrayList;

public class Checkers {
    int screenWidth = 1200;
    int screenHeight = 1200;
    int numTiles = 8;
    int squareWidth = screenWidth/numTiles;;
    int squareHeight= screenHeight/numTiles;;
    ArrayList<Oval> validMoves;
    ArrayList<Man> pieces = new ArrayList<Man>();
    int[][] grid = new int[numTiles][numTiles];
    
    public Checkers() {
        drawBackground();
        generateGrid();
        generatePieces();
    }

    public void drawBackground() {
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

    public void generateGrid() {
        for (int i = 0; i < numTiles; i++) {
            for (int j = 0; j < numTiles; j++) {
                grid[i][j] = 0;
            }
        }
    }

    public void onPieceClicked(Shape shp, double x, double y, int button) {
        shp.setFillColor(0, 200, 200);
        // calculateValidMoves(shp.getCenter().getX(), shp.getCenter().getY());
    }  
    
    public void calculateValidMoves(double pieceX, double pieceY) {
        int currentRow = (int)pieceY/squareHeight;
        int currentCol = (int)pieceX/squareWidth;
        System.out.println(currentRow);
        validMoves = new ArrayList<Oval>();
        for (int r = 0; r < grid.length; r++) {
            for (int c = 0; r < grid[r].length; c++) {
                if (Math.abs(currentRow-r) == 1 && Math.abs(currentCol-c) == 1) {
                    if (grid[r][c] == 0) {
                        validMoves.add(new Oval(c*squareWidth, r*squareWidth, squareWidth/3, squareWidth/3));
                        validMoves.get(validMoves.size()-1).setFillColor(128, 128, 128);
                    }
                }
            }
        }
    }

    public ArrayList<Man> generatePieces() {
        ArrayList<Man> currPieces = new ArrayList<Man>();
        for (int i = 0; i < numTiles; i++) {
            System.out.println(((double)numTiles/2.0) - (double)i);
            for (int j = 0; j < numTiles; j++) {
                if (((double)numTiles/2.0) - (double)j < 2 && ((double)numTiles/2.0) - (double)j > -1) {
                    continue;
                }
                if ((i+j)%2 != 0) {
                    currPieces.add(new Man(i, j, squareWidth, squareHeight));
                    grid[i][j] = 1;
                    currPieces.get(currPieces.size()-1).getHitbox().setMousePressedHandler(this::onPieceClicked);
                }
            }
        }
        return currPieces;
    }

}