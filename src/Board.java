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

    private boolean isPlaceValid(int x, int y, int colour) {
        //podejscie nr 1
        //Pod koniec if ze wszystkich

        //podejscie nr2
        //ciag ifów które sprwdzają warunke, jeśli nie valid to return false
        //na końcu funkcji return true

        boolean isSpaceEmpty = board[y][x] == 0;
        boolean isNextToOtherColor = touchesOtherColor(x, y, colour);
        boolean canRotateSomething = canRotateSomething(x, y, colour);


        if (!isSpaceEmpty) {
            return false;
        }
        return true;
        //todo: add additional validity checks
        //bool is empty DONE
        //bool czy przylega do chociaż jednego nieswojego (xy swój i xy tego nieswojego) -> <- DONE
        //bool czy może coś obrócić
    }

    private boolean touchesOtherColor(int x, int y, int colour) {
        int otherColour = 1;
        if (colour == 1){
            otherColour = 2;
        }

        if (y != 0) { //check row above, if there is one
            if (x != 0) {
                if (board[y-1][x-1] == otherColour) {
                    return true;
                }
            }
            if (board[y-1][x] == otherColour) {
                return true;
            }
            if (x != 8) {
                if (board[y-1][x+1] == otherColour) {
                    return true;
                }
            }
        }

        //check left and right, if piece that is to be placed isn't on the edge
        if (x != 0) {
            if (board[y][x-1] == otherColour) {
                return true;
            }
        }
        if (x != 8) {
            if (board[y][x+1] == otherColour) {
                return true;
            }
        }

        if (y != 8) { //check row below, if there is one
            if (x != 0) {
                if (board[y+1][x-1] == otherColour) {
                    return true;
                }
            }
            if (board[y+1][x] == otherColour) {
                return true;
            }
            if (x != 8) {
                if (board[y+1][x+1] == otherColour) {
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
            if (i != x-1 && i != x+1 && board[y][i] == colour){
                return true;
            }
        }

        for (int i = 0; i < size; i++) {
            if (i != y-1 && i != y+1 && board[i][x] == colour){
                return true;
            }
        }

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if(i != y-1 && i != y+1 && j != x-1 && j != x+1 && board[i][j] == colour){
                    return true;
                }
            }
        }

        return false;
    }

    private void putNewPiece(int x, int y, int colour){
        board[y][x] = colour;
    }


}