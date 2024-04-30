import doodlepad.*;

import java.awt.Font;
import java.util.ArrayList;

/**
Checkers.java
@author Ethan Avera
@since 4/10/24
This class handles the functionality of running the checkers game and the logic behind control of the pieces.
*/
public class Checkers {

    enum TileType { // From https://www.w3schools.com/java/java_enums.asp
        EMPTY,
        RED_PLAYER,
        BLACK_PLAYER,
        RED_KING,
        BLACK_KING
    }
    int screenWidth = 800;
    int screenHeight = 800;
    int numTiles = 8;
    int selectedPieceRow;
    int selectedPieceCol;
    String winner;
    Pad p = new Pad(screenWidth, screenHeight);
    int squareWidth = screenWidth/numTiles;;
    int squareHeight= screenHeight/numTiles;;
    ArrayList<Oval> validMoves = new ArrayList<Oval>();
    ArrayList<Man> pieces = new ArrayList<Man>();
    TileType[][] grid = new TileType[numTiles][numTiles];
    Image rulesImg;
    
    /*
    Checkers class constructor
    Constructor sets up the game board
    */
    public Checkers() {
        initiateSetup();
        drawTitleScreen();
    }

    // Function for game setup, called on startup and on reset
    public void initiateSetup() {
        generateGrid();
        generatePieceGrid();
        drawPieces();
    }

    /*
    Resets game, onclick method for play again button
    No return type
    */
    public void resetGame(Shape shp, double x, double y, int button) {
        initiateSetup();
    }

    /*
    Switches to the rules screen, onclick method for rules button when on title screen
    No return type
    */
    public void switchToRulesScreen(Shape shp, double x, double y, int button) {
        drawRulesScreen();
    }

    /*
    Switches to the title screen, onclick method for rules button when on rules screen 
    No return type
    */
    public void switchToTitleScreen(Shape shp, double x, double y, int button) {
        p.clear();
        initiateSetup();
        drawTitleScreen();
    }

    /*
    Draws the background squares of the checkers board 
    No return type
    */
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

    /*
    Generates an empty checkers board.
    No return type
    */
    public void generateGrid() {
        for (int i = 0; i < numTiles; i++) {
            for (int j = 0; j < numTiles; j++) {
                grid[i][j] = TileType.EMPTY;
            }
        }
    }

    /*
    Piece onclick function
    Manages functionality of showing valid moves and updating the screen
    No return type
    */
    public void onPieceClicked(Shape shp, double x, double y, int button) {
        p.clear();
        drawPieces();
        calculateValidMoves(shp.getCenter().getX(), shp.getCenter().getY());
    }

    /*
    Valid move dot onclick function
    Moves the player based on the position of the square clicked
    Also handles king promotions and win checks
    No return type
    */
    public void selectMoveChoice(Shape shp, double x, double y, int button) {
        grid[(int)shp.getCenter().getY()/squareHeight][(int)shp.getCenter().getX()/squareWidth] = grid[selectedPieceRow][selectedPieceCol];
        grid[selectedPieceRow][selectedPieceCol] = TileType.EMPTY;
        if (Math.abs(selectedPieceRow-(int)shp.getCenter().getY()/squareHeight) > 1) {
            grid[(selectedPieceRow+(int)shp.getCenter().getY()/squareHeight)/2][(selectedPieceCol+(int)shp.getCenter().getX()/squareWidth)/2] = TileType.EMPTY;
        }
        checkAndApplyPromotions();
        drawPieces();
        winner = determineWinner();
        if (!winner.equals("")) {
            drawWinScreen(winner);
        }

    }

    /*
    Calculates all possible moves that are on the board based on the selected piece type
    returns an arrayList of integer arrays representing the valid move data.
    */
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
        else if (squareType == TileType.BLACK_KING || squareType == TileType.RED_KING) {
            int[][] directions = {{1, -1}, {1, 1}, {-1, -1}, {-1, 1}};
            for (int i = 0; i < 4; i++) {
                possibleDirections.add(directions[i]);
            }
        }
        for (int[] dV : possibleDirections) {
            if (selectedPieceRow+dV[0] < numTiles && selectedPieceRow+dV[0] >= 0 && selectedPieceCol+dV[1] < numTiles && selectedPieceCol+dV[1] >= 0) {
                int[] temp = {dV[0], dV[1]};
                possibleMoves.add(temp);
            }
        }
        return possibleMoves;
    }

    /*
    Calculates all moves from possible moves above that aren't taken by another piece
    Also handles piece-jumping calculations
    No return type
    */
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

    /*
    Generates a starting piece grid
    No return type
    */
    public void generatePieceGrid() {
        for (int r = 0; r < numTiles; r++) {
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

    /*
    Handles piece promotion logic
    No return type
    */
    public void checkAndApplyPromotions() {
        for (int c = 0; c < numTiles; c++) {
            if (grid[0][c] == TileType.RED_PLAYER) {
                grid[0][c] = TileType.RED_KING;
            }
            else if (grid[numTiles-1][c] == TileType.BLACK_PLAYER) {
                grid[numTiles-1][c] = TileType.BLACK_KING;
            }
        }
    }

    /*
    Checks if a player has won the checkers game
    Returns the winning player
    */
    public String determineWinner() {
        boolean redOver = true;
        boolean blackOver = true;
        for (int i = 0; i < numTiles; i++) {
            for (int j = 0; j < numTiles; j++) {
                if (grid[i][j] == TileType.RED_PLAYER || grid[i][j] == TileType.RED_KING) {
                    redOver = false;
                }
                if (grid[i][j] == TileType.BLACK_PLAYER || grid[i][j] == TileType.BLACK_KING) {
                    blackOver = false;
                }
            }
        }
        if (redOver && !blackOver) {
            return "Black";
        }
        else if (blackOver && !redOver) {
            return "Red";
        }
        return "";

        
    }

    /*
    Draws the winscreen once a player has won the game
    No return type
    */
    public void drawWinScreen(String winPlayer) {
        Rectangle background = new Rectangle(0, 0, screenWidth, screenHeight);
        background.setFillColor(0, 0, 0, 128);
        String textString = winPlayer + " Player Wins!";
        // Text is set to (0, 0) temporarily before it is centered on the screen.
        Text winnerText = new Text(textString, 0, 0, screenWidth/16);
        winnerText.setCenter(screenWidth/2, 5*(screenHeight/16));
        winnerText.setFillColor(220, 220, 0);
        RoundRect playAgainButton = new RoundRect(screenWidth/3, screenHeight/2+screenHeight/100, screenWidth/3, screenWidth/4-screenHeight/50, 10, 10);
        Text playAgainText = new Text("Play Again", 0, 0, screenWidth/16);
        playAgainText.setFillColor(128);
        playAgainText.setCenter(screenWidth/2, 5*(screenHeight/8));
        playAgainButton.setStrokeWidth(screenWidth/200);
        playAgainButton.setStrokeColor(0);
        playAgainButton.setMouseClickedHandler(this::resetGame);
        playAgainText.setMouseClickedHandler(this::resetGame);
    }

    /*
    Draws the title screen
    Called on startup
    No return type
    */
    public void drawTitleScreen () {
        Rectangle background = new Rectangle(0, 0, screenWidth, screenHeight);
        background.setFillColor(0, 0, 0, 128);
        // Text is set to (0, 0) temporarily before it is centered on the screen.
        String titleString = "CHECKERS";
        ArrayList<Text> titleText = new ArrayList<Text>();
        for (int i = 0; i < titleString.length(); i++) {
            titleText.add(new Text(titleString.substring(i, i+1), i*(screenWidth/8), 5*(screenHeight/16)+(screenHeight/40)*(i%2), screenHeight/6));
            if (i % 2 == 0) {
                titleText.get(titleText.size()-1).setFillColor(0);
                titleText.get(titleText.size()-1).setStrokeWidth(screenWidth/400); 
                titleText.get(titleText.size()-1).setStrokeColor(220); 
            }
            else {
                titleText.get(titleText.size()-1).setFillColor(220); 
                titleText.get(titleText.size()-1).setStrokeWidth(screenWidth/400); 
                titleText.get(titleText.size()-1).setStrokeColor(0); 
            }
        }
        
        RoundRect playButton = new RoundRect(screenWidth/3, screenHeight/2+screenHeight/100, screenWidth/3, screenWidth/4-screenHeight/50, 10, 10);
        Text playText = new Text("Play", 0, 0, screenWidth/16);
        playText.setFillColor(128);
        playText.setCenter(screenWidth/2, 5*(screenHeight/8));
        playButton.setStrokeWidth(screenWidth/200);
        playButton.setStrokeColor(0);
        playButton.setMouseClickedHandler(this::resetGame);
        playText.setMouseClickedHandler(this::resetGame);
        RoundRect rulesButton = new RoundRect(5*(screenWidth/6)-10, 10, screenWidth/6, screenHeight/12, 8, 8);
        Text rulesText = new Text("Rules", 0, 0, screenWidth/32);
        rulesText.setCenter(rulesButton.getCenter().getX() + screenWidth/400, rulesButton.getCenter().getY() + screenHeight/125);
        rulesButton.setMouseClickedHandler(this::switchToRulesScreen);
        rulesText.setMouseClickedHandler(this::switchToRulesScreen);




    }
    /*
    Draws the rules screen
    Called when a player clicks on the Rules button
    No return type
    */
    public void drawRulesScreen() {
        rulesImg = new Image("Checkers_Rules.png", screenWidth/8, screenHeight/8, 3*(screenWidth/4), 3*(screenHeight/4));
        RoundRect rulesButton = new RoundRect(5*(screenWidth/6)-10, 10, screenWidth/6, screenHeight/12, 8, 8);
        Text rulesText = new Text("Rules", 0, 0, screenWidth/32);
        rulesText.setCenter(rulesButton.getCenter().getX() + screenWidth/400, rulesButton.getCenter().getY() + screenHeight/125);
        rulesButton.setMouseClickedHandler(this::switchToTitleScreen);
        rulesText.setMouseClickedHandler(this::switchToTitleScreen);

    }

    /*
    Draws the pieces based on the current piece grid
    No return type
    */
    public void drawPieces() {
        drawBackground();
        ArrayList<Man> currPieces = new ArrayList<Man>();
        ArrayList<Image> kingImages = new ArrayList<Image>();
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
                else if (grid[i][j] == TileType.RED_KING) {
                    currPieces.add(new Man(j, i, squareWidth, squareHeight));
                    currPieces.get(currPieces.size()-1).getHitbox().setFillColor(190, 0, 0);
                    kingImages.add(new Image("Checkers_Red.png", j*squareWidth+20, i*squareHeight+20, squareWidth-40, squareHeight-40));
                    kingImages.get(kingImages.size()-1).setMousePressedHandler(this::onPieceClicked);
                }
                else if (grid[i][j] == TileType.BLACK_KING) {
                    currPieces.add(new Man(j, i, squareWidth, squareHeight));
                    currPieces.get(currPieces.size()-1).getHitbox().setFillColor(0);
                    currPieces.get(currPieces.size()-1).getHitbox().setStrokeColor(110);
                    kingImages.add(new Image("Checkers_Black.png", j*squareWidth+20, i*squareHeight+20, squareWidth-40, squareHeight-40));
                    kingImages.get(kingImages.size()-1).setMousePressedHandler(this::onPieceClicked);

                }

            }
        }
    }

}