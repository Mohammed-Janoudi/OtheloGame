import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
public class Board {
    public static final int SIZE = 8;
    private Position[][] boardPieces;
    private String name;

    public Board(String saveFile) {
        this.boardPieces = new Position[SIZE][SIZE];
        initializeBoard();
        saveBoardToFile(saveFile ,null , null , null);
    }
    public void saveBoardToFile(String filename, Player first, Player second, Player current) {
        try (PrintWriter writer = new PrintWriter(new File(filename))) {

            if (first != null && second != null && current != null) {
                writer.println(first.getName());
                writer.println(first.getPiece());
                writer.println(second.getName());
                writer.println(second.getPiece());
                writer.println(current.getName());
            }


            for (int i = 0; i < SIZE; i++) {
                for (int j = 0; j < SIZE; j++) {
                    writer.print(boardPieces[i][j].getPiece());
                }
                writer.println();
            }
        } catch (FileNotFoundException e) {
            System.out.println("Error saving the game: " + e.getMessage());
        }
    }

    public void setBoardPieces(Position[][] newBoardPieces) {
        this.boardPieces = newBoardPieces;
    }


    public void initializeBoard() {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                boardPieces[i][j] = new Position();
            }
        }
    }

    public void setStartingPosition(int option) {
        initializeBoard();
        switch (option) {
            case 1:
                boardPieces[3][3].setPiece(Position.WHITE);
                boardPieces[3][4].setPiece(Position.BLACK);
                boardPieces[4][3].setPiece(Position.BLACK);
                boardPieces[4][4].setPiece(Position.WHITE);
                break;
            case 2:
                boardPieces[2][2].setPiece(Position.WHITE);
                boardPieces[2][3].setPiece(Position.BLACK);
                boardPieces[3][2].setPiece(Position.BLACK);
                boardPieces[3][3].setPiece(Position.WHITE);
                break;
            case 3:
                boardPieces[2][4].setPiece(Position.WHITE);
                boardPieces[2][5].setPiece(Position.BLACK);
                boardPieces[3][4].setPiece(Position.BLACK);
                boardPieces[3][5].setPiece(Position.WHITE);
                break;
            case 4:
                boardPieces[4][2].setPiece(Position.WHITE);
                boardPieces[4][3].setPiece(Position.BLACK);
                boardPieces[5][2].setPiece(Position.BLACK);
                boardPieces[5][3].setPiece(Position.WHITE);
                break;
            case 5:
                boardPieces[4][4].setPiece(Position.WHITE);
                boardPieces[4][5].setPiece(Position.BLACK);
                boardPieces[5][4].setPiece(Position.BLACK);
                boardPieces[5][5].setPiece(Position.WHITE);
                break;
            case 6:
                boardPieces[2][2].setPiece(Position.WHITE);
                boardPieces[2][3].setPiece(Position.BLACK);
                boardPieces[2][4].setPiece(Position.WHITE);
                boardPieces[2][5].setPiece(Position.BLACK);

                boardPieces[3][2].setPiece(Position.BLACK);
                boardPieces[3][3].setPiece(Position.WHITE);
                boardPieces[3][4].setPiece(Position.BLACK);
                boardPieces[3][5].setPiece(Position.WHITE);

                boardPieces[4][2].setPiece(Position.WHITE);
                boardPieces[4][3].setPiece(Position.BLACK);
                boardPieces[4][4].setPiece(Position.WHITE);
                boardPieces[4][5].setPiece(Position.BLACK);

                boardPieces[5][2].setPiece(Position.BLACK);
                boardPieces[5][3].setPiece(Position.WHITE);
                boardPieces[5][4].setPiece(Position.BLACK);
                boardPieces[5][5].setPiece(Position.WHITE);
                break;
            default:
                System.out.println("Invalid starting position option.");
        }
    }



    public void drawBoard() {
        int SIZE = 8;
        System.out.print("  ");


        for (int i = 0; i < SIZE; i++) {
            System.out.print(i + " ");
        }
        System.out.println();


        for (int i = 0; i < SIZE; i++) {
            System.out.print(i + " ");
            for (int j = 0; j < SIZE; j++) {
                System.out.print("|" + boardPieces[i][j].getPiece());
            }
            System.out.println("|");
        }


        System.out.print("  ");
        for (int i = 0; i < SIZE; i++) {
            System.out.print("---");
        }
        System.out.println();
    }



    public boolean takeTurn(Player current) {
        Scanner scanner = new Scanner(System.in);
        int row = scanner.nextInt();
        int col = scanner.nextInt();
        if (isValidMove(row, col, current.getPiece())) {
            makeMove(row, col, current.getPiece());
            return true;
        }
        return false;
    }

    private boolean isValidMove(int row, int col, char piece) {
        if (row < 0 || row >= SIZE || col < 0 || col >= SIZE || !boardPieces[row][col].canPlay()) {
            return false;
        }
        return canFlip(row, col, piece);
    }

    private boolean canFlip(int row, int col, char piece) {
        char opponentPiece;
        if (piece == Position.BLACK) {
            opponentPiece = Position.WHITE;
        } else {
            opponentPiece = Position.BLACK;
        }
        int[][] directions = {
                {-1, -1}, {-1, 0}, {-1, 1},{0,-1},{0,1},{1,-1},{1,0},{1,1}
        };

        for (int[] direction : directions) {
            int x = row + direction[0];
            int y = col + direction[1];
            if (x >= 0 && x < SIZE && y >= 0 && y < SIZE && boardPieces[x][y].getPiece() == opponentPiece) {
                x += direction[0];
                y += direction[1];
                while (x >= 0 && x < SIZE && y >= 0 && y < SIZE) {
                    if (boardPieces[x][y].getPiece() == piece) {
                        return true;
                    } else if (boardPieces[x][y].getPiece() == Position.EMPTY) {
                        break;
                    }
                    x += direction[0];
                    y += direction[1];
                }
            }
        }

        return false;
    }

    private void makeMove(int row, int col, char piece) {
        boardPieces[row][col].setPiece(piece);
        flipPiece(row, col, piece);
    }

    private void flipPiece(int row, int col, char piece) {
        char opponentPiece;
        if (piece == Position.BLACK) {
            opponentPiece = Position.WHITE;
        } else {
            opponentPiece = Position.BLACK;
        }

        int[][] directions = {
                {-1, -1}, {-1, 0}, {-1, 1},{0, -1}, {0, 1},{1, -1}, {1, 0},{1, 1}
        };

        for (int[] direction : directions) {
            int x = row + direction[0];
            int y = col + direction[1];


            while (x >= 0 && x < SIZE && y >= 0 && y < SIZE && boardPieces[x][y].getPiece() == opponentPiece) {
                x += direction[0];
                y += direction[1];
            }


            if (x >= 0 && x < SIZE && y >= 0 && y < SIZE && boardPieces[x][y].getPiece() == piece) {

                x -= direction[0];
                y -= direction[1];


                while (x != row || y != col) {
                    boardPieces[x][y].setPiece(piece);
                    x -= direction[0];
                    y -= direction[1];
                }
            }
        }
    }

    public boolean canPlayerMove(Player player) {
        char piece = player.getPiece();
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (isValidMove(i, j, piece)) {
                    return true;
                }
            }
        }
        return false;
    }






    public boolean isGameOver() {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (boardPieces[i][j].getPiece() == Position.EMPTY) {
                    return false;
                }
            }
        }
        return true;
    }




}




    



