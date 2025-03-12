public enum Colour {
    EMPTY(0), WHITE(1), BLACK(2);

    private final int value;

    Colour(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
