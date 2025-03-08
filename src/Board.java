public class Board {
    //0 - nothing; 1 - black; 2 - white.
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

    private boolean isPlaceValid(int x, int y) {
        //podejscie nr 1
        //bool is empty
        //bool czy przylega do chociaż jednego nieswojego (xy swój i xy tego nieswojego) -> <-
        //bool czy możesz go obrócić
        //if ze wszystkich

        boolean isOnEmptySpace = board[x][y] != 0;
        boolean isNextToOtherColor;
        boolean isNextToOtherColor2;
        boolean isNextToOtherColor3;
        boolean isNextToOtherColor4;

        //podejscie nr2
        //ciag ifów które sprwdzają warunke, jeśli nie valid to return false
        //na końcu funkcji return true
        if (isOnEmptySpace) {
            return false;
        }
        //is place empty
        return true;
        //todo: add additional validity checks
    }

    private void putNewPiece(int x, int y, int value){
        board[x][y] = value;
    }

}