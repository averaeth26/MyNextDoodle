import doodlepad.*;
import java.util.ArrayList;

import javax.management.openmbean.ArrayType;

public class Checkers {

    enum TileType { // From https://www.w3schools.com/java/java_enums.asp
        EMPTY(0),
        RED_PLAYER(1),
        BLACK_PLAYER(2);
        int currentVal;
        TileType(int val) {
            this.currentVal = val;
        }
    }
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
    TileType[][] grid = new TileType[numTiles][numTiles];
    
    public Checkers() {
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
                    gridSquares.get(gridSquares.size()-1).setFillColor(70, 35, 25);
                }
            }
        }
    }

    public void generateGrid() {
        for (int i = 0; i < numTiles; i++) {
            for (int j = 0; j < numTiles; j++) {
                grid[i][j] = TileType.EMPTY;
            }
        }
    }

    public void onPieceClicked(Shape shp, double x, double y, int button) {
        p.clear();
        drawPieces();
        calculateValidMoves(shp.getCenter().getX(), shp.getCenter().getY());
    }

    public void selectMoveChoice(Shape shp, double x, double y, int button) {
        grid[(int)shp.getCenter().getY()/squareHeight][(int)shp.getCenter().getX()/squareWidth] = grid[selectedPieceRow][selectedPieceCol];
        grid[selectedPieceRow][selectedPieceCol] = TileType.EMPTY;
        if (Math.abs(selectedPieceRow-(int)shp.getCenter().getY()/squareHeight) > 1) {
            System.out.println((selectedPieceRow+(int)shp.getCenter().getY()/squareHeight)/2);
            grid[(selectedPieceRow+(int)shp.getCenter().getY()/squareHeight)/2][(selectedPieceCol+(int)shp.getCenter().getX()/squareWidth)/2] = TileType.EMPTY;
        }
        drawPieces();
    } 

    public ArrayList<int[]> getAdjacentDiagonalMoves(TileType squareType) {
        ArrayList<int[]> possibleMoves = new ArrayList<int[]>();
        ArrayList<int[]> possibleDirections = new ArrayList<int[]>();
        if (squareType == TileType.RED_PLAYER) {
            int[][] directions = {{-1, -1}, {-1, 1}};
            possibleDirections.add(directions[0]);
            possibleDirections.add(directions[1]);
        }
        else if (squareType == TileType.BLACK_PLAYER) {
            int[][] directions = {{1, -1}, {1, 1}};
            possibleDirections.add(directions[0]);
            possibleDirections.add(directions[1]);
        }
        for (int[] dV : possibleDirections) {
            if (selectedPieceRow+dV[0] < numTiles && selectedPieceRow+dV[0] >= 0 && selectedPieceCol+dV[1] < numTiles && selectedPieceCol+dV[1] >= 0) {
                int[] temp = {dV[0], dV[1]};
                possibleMoves.add(temp);
            }
        }
        return possibleMoves;
    }

    
    public void calculateValidMoves(double pieceX, double pieceY) {
        selectedPieceRow = (int)pieceY/squareHeight;
        selectedPieceCol = (int)pieceX/squareWidth;
        for (int[] move : getAdjacentDiagonalMoves(grid[selectedPieceRow][selectedPieceCol])) {
            int moveRow = selectedPieceRow+move[0];
            int moveCol = selectedPieceCol+move[1];
            if (grid[moveRow][moveCol] == TileType.EMPTY) {
                validMoves.add(new Oval(moveCol*squareHeight + squareHeight/3, moveRow*squareWidth + squareWidth/3, squareWidth/3, squareHeight/3));
                validMoves.get(validMoves.size()-1).setFillColor(128, 128, 128, 128);
                validMoves.get(validMoves.size()-1).setMouseClickedHandler(this::selectMoveChoice);
            }
            else if (selectedPieceRow+2*move[0] >= 0 && selectedPieceRow+2*move[0] < 8 && selectedPieceCol+2*move[1] >= 0 && selectedPieceCol+2*move[1] < 8) {
                if (grid[selectedPieceRow+2*move[0]][selectedPieceCol+2*move[1]] == TileType.EMPTY && grid[moveRow][moveCol] != grid[selectedPieceRow][selectedPieceCol]) {
                    validMoves.add(new Oval((selectedPieceCol+2*move[1])*squareHeight + squareHeight/3, (selectedPieceRow+2*move[0])*squareWidth + squareWidth/3, squareWidth/3, squareHeight/3));
                    validMoves.get(validMoves.size()-1).setFillColor(128, 128, 128, 128);
                    validMoves.get(validMoves.size()-1).setMouseClickedHandler(this::selectMoveChoice);  
                }
            }
        }
    }

    public void generatePieceGrid() {
        for (int r = 0; r < numTiles; r++) {
            System.out.println(((double)numTiles/2.0) - (double)r);
            for (int c = 0; c < numTiles; c++) {
                if (((double)numTiles/2.0) - (double)r < 2 && ((double)numTiles/2.0) - (double)r > -1) {
                    continue;
                }
                if ((r+c)%2 != 0) {
                    if (r > numTiles / 2) {
                        grid[r][c] = TileType.RED_PLAYER;
                    }
                    else {
                        grid[r][c] = TileType.BLACK_PLAYER;
                    }
                }
            }
        }
    }

    public ArrayList<Man> drawPieces() {
        drawBackground();
        ArrayList<Man> currPieces = new ArrayList<Man>();
        for (int i = 0; i < numTiles; i++) {
            for (int j = 0; j < numTiles; j++) {
                if (grid[i][j] == TileType.RED_PLAYER) {
                    currPieces.add(new Man(j, i, squareWidth, squareHeight));
                    currPieces.get(currPieces.size()-1).getHitbox().setMousePressedHandler(this::onPieceClicked);
                    currPieces.get(currPieces.size()-1).getHitbox().setFillColor(190, 0, 0);
                }
                else if (grid[i][j] == TileType.BLACK_PLAYER) {
                    currPieces.add(new Man(j, i, squareWidth, squareHeight));
                    currPieces.get(currPieces.size()-1).getHitbox().setMousePressedHandler(this::onPieceClicked);
                    currPieces.get(currPieces.size()-1).getHitbox().setFillColor(0);
                    currPieces.get(currPieces.size()-1).getHitbox().setStrokeColor(110);


                }

            }
        }
        return currPieces;


    }

}