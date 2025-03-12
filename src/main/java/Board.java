import java.util.ArrayList;

public class Board {
    //0 - nothing; 1 - black; 2 - white.
    //The board is 8x8
    private final int[][] board;
    private final int size = 8;

    public Board() {
        this.board = new int[size][size];
        initialiseBoard();
    }

    private void initialiseBoard() {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                board[i][j] = 0;
            }
        }
        setStartingPositions();
    }

    private void setStartingPositions() {
        board[3][3] = 1;
        board[4][4] = 1;
        board[3][4] = 2;
        board[4][3] = 2;
    }

    public boolean isPlaceValid(Move move) {
        boolean isSpaceEmpty = board[move.getRow()][move.getColumn()] == 0;
//        boolean isNextToDifferentColor = touchesOtherColor(x, y, colour);
//        boolean canRotateSomething = getValidRotations()

        return isSpaceEmpty && !getValidRotations(move).isEmpty();
    }

    public void doMove(Move move) {
        this.board[move.getRow()][move.getColumn()] = move.getColour();
    }

    private ArrayList<Rotation> getValidRotations(Move move) {
        ArrayList<Rotation> rotations = new ArrayList<>();
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                int rotation = canRotate(move, i, j);
                if (rotation > 0) {
                    rotations.add(new Rotation(move.getRow(), move.getColumn(), i, j, rotation));
                }
            }
        }
        System.out.println(rotations);
        return rotations;
    }

    private int canRotate(Move move, int xOffset, int yOffset) {
        if (isTooCloseToEdge(move, xOffset, yOffset)) {
            return 0;
        }
        if (!isNeighbourNotEmptyAndDifferentColour(move, xOffset, yOffset)) {
            return walkTheRotation(move, xOffset, yOffset, 1);
        }
        return 0;
    }

    private int walkTheRotation(Move move, int xOffset, int yOffset, int step) {
        int newX = move.getRow() + (xOffset * step);
        int newY = move.getColumn() + (yOffset * step);
        if (board[newX][newY] == Colour.EMPTY.getValue()) {
            return 0;
        } else if (board[newX][newY] != move.getColour()) {
            return 1 + walkTheRotation(move, xOffset, yOffset, step + 1);
        } else {
            return 1;
        }
    }

    private boolean isNeighbourNotEmptyAndDifferentColour(Move move, int xOffset, int yOffset) {
        int newX = move.getRow() + xOffset;
        int newY = move.getColumn() + yOffset;
        return board[newX][newY] != Colour.EMPTY.getValue() && board[newX][newY] != move.getColour();
    }

    private boolean isTooCloseToEdge(Move move, int xOffset, int yOffset) {
        if (xOffset < 0 && move.isUpperEdge()) {
            return true;
        }
        if (xOffset > 0 && move.isLowerEdge()) {
            return true;
        }
        if (yOffset < 0 && move.isLeftEdge()) {
            return true;
        }
        if (yOffset > 0 && move.isRightEdge()) {
            return true;
        }
        return false;
    }


    private boolean touchesOtherColor(int x, int y, int colour) {
        int otherColour = 1;
        if (colour == 1) {
            otherColour = 2;
        }

        if (y != 0) { //check row above, if there is one
            if (x != 0) {
                if (board[y - 1][x - 1] == otherColour) {
                    return true;
                }
            }
            if (board[y - 1][x] == otherColour) {
                return true;
            }
            if (x != 8) {
                if (board[y - 1][x + 1] == otherColour) {
                    return true;
                }
            }
        }

        //check left and right, if piece that is to be placed isn't on the edge
        if (x != 0) {
            if (board[y][x - 1] == otherColour) {
                return true;
            }
        }
        if (x != 8) { //todo: czy tutaj nie powinno być 7?
            if (board[y][x + 1] == otherColour) {
                return true;
            }
        }

        if (y != 8) { //check row below, if there is one
            if (x != 0) {
                if (board[y + 1][x - 1] == otherColour) {
                    return true;
                }
            }
            if (board[y + 1][x] == otherColour) {
                return true;
            }
            if (x != 8) {
                if (board[y + 1][x + 1] == otherColour) {
                    return true;
                }
            }
        }

        return false;
    }

    private boolean canRotateSomething(int x, int y, int colour) {
        //vertical, horizontal and diagonal
        //searching for the same colour
        //not touching xy

        for (int i = 0; i < size; i++) {
            if (i != x - 1 && i != x + 1 && board[y][i] == colour) {
                return true;
            }
        }

        for (int i = 0; i < size; i++) {
            if (i != y - 1 && i != y + 1 && board[i][x] == colour) {
                return true;
            }
        }

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (i != y - 1 && i != y + 1 && j != x - 1 && j != x + 1 && board[i][j] == colour) {
                    return true;
                }
            }
        }

        return false;
    }

    public int[][] getBoardState() {
        return board;
    }

//    private void putNewPiece(int x, int y, int colour) {
//        if (isPlaceValid(x, y, colour)) {
//            board[y][x] = colour;
//        }
//    }


}

class Rotation {
    private final int x1;
    private final int y1;
    private final int offsetX;
    private final int offsetY;
    private final int length;

    public Rotation(int x1, int y1, int offsetX, int offsetY, int length) {
        this.x1 = x1;
        this.y1 = y1;
        this.offsetX = offsetX;
        this.offsetY = offsetY;
        this.length = length;
    }

    public int getX1() {
        return x1;
    }

    public int getY1() {
        return y1;
    }

    public int getOffsetX() {
        return offsetX;
    }

    public int getOffsetY() {
        return offsetY;
    }

    public int getLength() {
        return length;
    }
}