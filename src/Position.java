public class Position {
    public static final char EMPTY = ' ';
    public static final char BLACK = 'B';
    public static final char WHITE = 'W';
    private char piece;




    public Position() {
        piece = EMPTY;
    }
    public Position(char piece) {
        this.piece = piece;
    }


    public boolean canPlay() {
        return piece == EMPTY;
    }

    public char getPiece() {
        return piece;
    }

    public void setPiece(char piece) {
        this.piece = piece;
    }









}
