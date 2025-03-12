

class BoardTest {



    void testWalkTheRotation() {
        Board board = new Board();
        board.doMove(new Move(0, 0, Colour.BLACK.getValue()));
        board.doMove(new Move(0, 1, Colour.WHITE.getValue()));
        board.doMove(new Move(0, 3, Colour.WHITE.getValue()));
        board.doMove(new Move(0, 4, Colour.WHITE.getValue()));
        board.walkTheRotation(new Move(0, 5, Colour.BLACK.getValue()), 0, -1, 1);

    }
}