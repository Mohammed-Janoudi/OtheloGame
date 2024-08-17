import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
public class Game {
    private static final int SIZE = 8;
    private Board board;
    private Player First;
    private Player Second;
    private Player Current;

    public Game(Player First, Player Second) {
        this.First = First;
        this.Second = Second;
        this.Current = First;
        this.board = new Board("Othello Board");
    }

    public void start() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Choose a starting position:");
        System.out.println("1. A normal starting position.");
        System.out.println("2. A non-standard, offset starting position number 1.");
        System.out.println("3. A non-standard, offset starting position number 2.");
        System.out.println("4. A non-standard, offset starting position number 3.");
        System.out.println("5. A non-standard, offset starting position number 4.");
        System.out.println("6. Four-by-Four Starting Position.");
        int startingChoice = scanner.nextInt();
        board.setStartingPosition(startingChoice);
        play();
    }

    public  void load() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the filename to load: ");
        String filename = scanner.nextLine();

        try (Scanner fileScanner = new Scanner(new File(filename))) {

            String firstPlayerName = fileScanner.nextLine();
            char firstPlayerPiece = fileScanner.nextLine().charAt(0);
            String secondPlayerName = fileScanner.nextLine();
            char secondPlayerPiece = fileScanner.nextLine().charAt(0);
            String currentPlayerName = fileScanner.nextLine();


            this.First = new Player(firstPlayerName, firstPlayerPiece);
            this.Second = new Player(secondPlayerName, secondPlayerPiece);
            if (currentPlayerName.equals(firstPlayerName)) {
                this.Current = this.First;
            } else {
                this.Current = this.Second;
            }



            Position[][] loadedBoard = new Position[SIZE][SIZE];
            for (int i = 0; i < SIZE; i++) {
                String line = fileScanner.nextLine();
                for (int j = 0; j < SIZE; j++) {
                    loadedBoard[i][j] = new Position(line.charAt(j));
                }
            }
            this.board.setBoardPieces(loadedBoard);
        } catch (FileNotFoundException e) {
            System.err.println("Error loading the game: " + e.getMessage());
        }

        play();
    }

    private void switchPlayer() {
        if (Current == First) {
            Current = Second;
        } else {
            Current = First;
        }

    }

    public void play() {
        Scanner scanner = new Scanner(System.in);
        boolean gameOver = false;
        while (!board.isGameOver() && !gameOver) {
            board.drawBoard();
            System.out.println(Current.getName() + "'s turn (" + Current.getPiece() + ")");
            System.out.println("Options: 1. Move 2. Save 3. Concede 4. Forfeit");
            System.out.print("Choose an option: ");
            int option = scanner.nextInt();
            scanner.nextLine();

            switch (option) {
                case 1:
                    System.out.print("Enter row and column (Example, 0 4): ");
                    if (board.takeTurn(Current)) {
                        switchPlayer();
                    } else {
                        System.out.println("Invalid move. Try again.");
                    }
                    break;
                case 2:
                    saveGame();
                    break;
                case 3:
                    System.out.println(Current.getName() + " concedes. " + (Current == First ? Second.getName() : First.getName()) + " wins!");

                    gameOver = true;
                    System.out.println("Game is Over");
                    System.exit(0);

                    return;
                case 4:
                    System.out.println(Current.getName() + " forfeits the turn.");
                    switchPlayer();
                    break;
                default:
                    System.out.println("Invalid option. Try again.");
            }


            if (!board.canPlayerMove(Current) && !gameOver) {
                System.out.println(Current.getName() + " cannot move.");
                System.out.println("Options: 1. Save 2. Concede 3. Forfeit");
                System.out.print("Choose an option: ");
                option = scanner.nextInt();
                scanner.nextLine();

                switch (option) {
                    case 1:
                        saveGame();
                        break;
                    case 2:
                        System.out.println(Current.getName() + " concedes. " + (Current == First ? Second.getName() : First.getName()) + " wins!");
                        gameOver = true;
                        System.out.println("Game is Over");
                        System.exit(0);

                        return;
                    case 3:
                        System.out.println(Current.getName() + " forfeits the turn.");

                        switchPlayer();
                        break;
                    default:
                        System.out.println("Invalid option. Try again.");
                }
            }

        }
        board.drawBoard();
    }
    public void saveGame() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the filename to save the game: ");
        String filename = scanner.nextLine();
        board.saveBoardToFile(filename, First, Second, Current);
        System.out.println("Game saved.");
    }

    public static void main(String[] args){
        Scanner scanner = new Scanner(System.in);
        System.out.println("1. Start a New Game");
        System.out.println("2. Load a Game");
        System.out.println("3. Quit");
        int choice = scanner.nextInt();
        scanner.nextLine();
        Game game = null;
        switch (choice) {
            case 1:
                System.out.print("Enter name for Player 1: ");
                String Player1 = scanner.nextLine();
                System.out.print("Enter name for Player 2: ");
                String Player2 = scanner.nextLine();
                Player First = new Player(Player1, Position.BLACK);
                Player Second = new Player(Player2, Position.WHITE);
                game = new Game(First, Second);
                game.start();
                game.play();
                break;
            case 2:
                game = new Game(null, null);
                game.load();
                game.play();
                break;
            case 3:
                System.exit(0);
        }
    }

    }










