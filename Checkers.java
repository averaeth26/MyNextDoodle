import doodlepad.*;
import java.util.ArrayList;

public class Checkers {
    int screenWidth = 800;
    int screenHeight = 800;
    int numTiles = 8;
    int selectedPieceRow;
    int selectedPieceCol;
    Pad p = new Pad(screenWidth, screenHeight);
    int squareWidth = screenWidth/numTiles;;
    int squareHeight= screenHeight/numTiles;;
    ArrayList<Oval> validMoves = new ArrayList<Oval>();
    ArrayList<Man> pieces = new ArrayList<Man>();
    int[][] grid = new int[numTiles][numTiles];
    
    public Checkers() {
        drawBackground();
        generateGrid();
        generatePieceGrid();
        drawPieces();
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
        drawPieces();
        calculateValidMoves(shp.getCenter().getX(), shp.getCenter().getY());
    }

    public void selectMoveChoice(Shape shp, double x, double y, int button) {
        grid[(int)shp.getCenter().getX()/squareHeight][(int)shp.getCenter().getY()/squareHeight] = 1;
        grid[selectedPieceCol][selectedPieceRow] = 0;
        drawPieces();
    } 
    
    // TODO: Need to include jumping over other pieces here.
    public void calculateValidMoves(double pieceX, double pieceY) {
        selectedPieceRow = (int)pieceY/squareHeight;
        selectedPieceCol = (int)pieceX/squareWidth;
        for (int r = 0; r < grid.length; r++) {
            for (int c = 0; c < grid[r].length; c++) {
                if (Math.abs(selectedPieceRow-r) == 1 && Math.abs(selectedPieceCol-c) == 1) {
                    if (grid[c][r] == 0) {
                        validMoves.add(new Oval(c*squareHeight + squareHeight/3, r*squareWidth + squareWidth/3, squareWidth/3, squareHeight/3));
                        validMoves.get(validMoves.size()-1).setFillColor(128, 128, 128, 128);
                        validMoves.get(validMoves.size()-1).setMouseClickedHandler(this::selectMoveChoice);
                    }
                }
            }
        }
    }

    public void generatePieceGrid() {
        for (int i = 0; i < numTiles; i++) {
            System.out.println(((double)numTiles/2.0) - (double)i);
            for (int j = 0; j < numTiles; j++) {
                if (((double)numTiles/2.0) - (double)j < 2 && ((double)numTiles/2.0) - (double)j > -1) {
                    continue;
                }
                if ((i+j)%2 != 0) {
                    grid[i][j] = 1;
                }
            }
        }
    }

    public ArrayList<Man> drawPieces() {
        drawBackground();
        ArrayList<Man> currPieces = new ArrayList<Man>();
        for (int i = 0; i < numTiles; i++) {
            for (int j = 0; j < numTiles; j++) {
                if (grid[i][j] == 1) {
                    currPieces.add(new Man(i, j, squareWidth, squareHeight));
                    currPieces.get(currPieces.size()-1).getHitbox().setMousePressedHandler(this::onPieceClicked);
                }

            }
        }
        return currPieces;


    }

}