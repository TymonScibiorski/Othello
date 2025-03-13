import java.util.ArrayList;
import java.util.List;

public class Board {
    //0 - nothing; 1 - black; 2 - white.
    //The board is 8x8
    private final int[][] board;
    private final int size = 8;

    public Board() {
        this.board = new int[size][size];
        initialiseBoard();
    }

    void initialiseBoard() {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                board[i][j] = 0;
            }
        }
//        for (int i = 1; i < 7; i++) {
//            board[i][0] = 1;
//            board[0][i] = 1;
//        }
//        board[0][0] = 2;
        setStartingPositions();
    }

    private void setStartingPositions() {
        board[3][3] = 1;
        board[4][4] = 1;
        board[3][4] = 2;
        board[4][3] = 2;
    }

    public boolean performMove(Move move) {
        boolean isSpaceEmpty = board[move.getRow()][move.getColumn()] == 0;
        List<Rotation> rotations = getValidRotations(move);
        if (isSpaceEmpty && !getValidRotations(move).isEmpty()) {
            board[move.getRow()][move.getColumn()] = move.getColour();
            performRotations(move, rotations);
            return true;
        }
        return false;
    }

//    public void doMove(Move move) {
//        this.board[move.getRow()][move.getColumn()] = move.getColour();
//    }

    public ArrayList<Rotation> getValidRotations(Move move) {
        ArrayList<Rotation> rotations = new ArrayList<>();
        for (int yOffset = -1; yOffset <= 1; yOffset++) {
            for (int xOffset = -1; xOffset <= 1; xOffset++) {
                if (yOffset == 0 && xOffset == 0) {
                    continue;
                }
                int rotationLength = checkRotationLength(move, yOffset, xOffset);
                if (rotationLength > 0) {
                    rotations.add(new Rotation(move.getRow(), move.getColumn(), yOffset, xOffset, rotationLength));
                }
            }
        }
        System.out.println(rotations);
        return rotations;
    }

    public int checkRotationLength(Move move, int yOffset, int xOffset) {
        if (isTooCloseToEdge(move, yOffset, xOffset)) {
            return 0;
        }
//        System.out.print("dupa");
        if (isNeighbourNotEmptyAndDifferentColour(move, xOffset, yOffset)) {
            System.out.println(move);
            System.out.print("yo: " + yOffset + " ");
            System.out.println("xo: " + xOffset);
            return walkTheRotation(move, yOffset, xOffset, 1);
        }
        return 0;
    }

    public int walkTheRotation(Move move, int yDirection, int xDirection, int step) {
        int newY = move.getRow() + (yDirection * step);
        int newX = move.getColumn() + (xDirection * step);
        //fixme add check for same color!
        if(newY == board.length || newX == board.length || newY < 0 || newX < 0){
            return -10;
        }
        if (board[newY][newX] == Colour.EMPTY.getValue()) {
            return -10;
        } else if (board[newY][newX] != move.getColour()) {
            return 1 + walkTheRotation(move, yDirection, xDirection, step + 1);
        } else {
            return 0;
        }
    }

    private boolean isNeighbourNotEmptyAndDifferentColour(Move move, int yDirection, int xDirection) {
        int newX = move.getColumn() + yDirection;
        int newY = move.getRow() + xDirection;
        return board[newY][newX] != Colour.EMPTY.getValue() && board[newY][newX] != move.getColour();
    }

    private boolean isTooCloseToEdge(Move move, int yOffset, int xOffset) {
        if (xOffset < 0 && move.isLeftEdge()) {
            return true;
        }
        if (xOffset > 0 && move.isRightEdge()) {
            return true;
        }
        if (yOffset < 0 && move.isUpperEdge()) {
            return true;
        }
        if (yOffset > 0 && move.isLowerEdge()) {
            return true;
        }
        return false;
    }

    private void performRotations(Move move, List<Rotation> rotations) {
        for (Rotation r : rotations) {
            for (int i = 1; i <= r.length(); i++) {
                int offsetX = i * r.directionX();
                int offsetY = i * r.directionY();
                board[move.getRow() + offsetY][move.getColumn() + offsetX] = move.getColour();
            }
        }
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

record Rotation(int y1, int x1, int directionY, int directionX, int length) {
}