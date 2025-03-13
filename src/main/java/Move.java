import com.fasterxml.jackson.annotation.JsonProperty;

public class Move {
    @JsonProperty("row")
    private int row;
    @JsonProperty("col")
    private int column;
    @JsonProperty("colour")
    private int colour;

    public Move() {
    }

    public Move(int row, int column, int colour) {
        this.row = row;
        this.column = column;
        this.colour = colour;
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }

    public int getColour() {
        return colour;
    }

    public boolean isUpperEdge(){
        return row < 2;
    }

    public boolean isLowerEdge(){
        return row > 5;
    }

    public boolean isLeftEdge(){
        return column < 2;
    }

    public boolean isRightEdge(){
        return column > 5;
    }

    @Override
    public String toString() {
        return "Move{" +
                "row=" + row +
                ", column=" + column +
                ", colour=" + colour +
                '}';
    }
}
