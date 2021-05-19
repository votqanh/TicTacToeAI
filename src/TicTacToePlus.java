import java.util.*;
import java.lang.Character;
import java.util.concurrent.TimeUnit;

public class TicTacToePlus {
    static boolean aiTurn = false, loop = true;
    static Scanner scanner = new Scanner(System.in);
    static char[][] board = new char[][]{{'1', '2', '3'},
                                         {'4', '5', '6'},
                                         {'7', '8', '9'}};
    static char human, ai;
    static int pos, x, y;
    //static int counter = 0;

    public static void main(String[] args) throws InterruptedException {
        run();
    }

    public static void run() throws InterruptedException {
        start();
        printBoard(board);

        // wipe board
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[i][j] != ' ')
                    board[i][j] = ' ';
            }
        }

        do {
            move();
            printBoard(board);
        } while (!gameOver());

        System.out.println("\nGAME OVER");
        switch (gameResult(board, ai, human)) {
            case -1 -> System.out.println("\nYou won!!");
            case 0 -> System.out.println("\nTie!!");
            case 1 -> System.out.println("\nHA YOU SUCK AI WON!!!");
        }
    }

    public static void move() throws InterruptedException {
        if (!aiTurn) {
            System.out.print("\nPick a position 1-9\t");
            loop = true;
            while (loop) {
                while (loop) {
                    try {
                        pos = scanner.nextInt();
                        loop = false;
                    } catch (InputMismatchException e) {
                        System.out.print("\nTry again. Enter 1-9\t");
                        scanner.nextLine(); //consume token
                    }
                }
                loop = true;
                if (pos < 1 || pos > 9)
                    System.out.print("\nTry again. Enter 1-9\t");
                else if (board[getXY(pos)[0]][getXY(pos)[1]] == human || board[getXY(pos)[0]][getXY(pos)[1]] == ai)
                    System.out.print("\nPosition occupied! Try again\t");
                else
                    loop = false;
            }
            board[getXY(pos)[0]][getXY(pos)[1]] = human;

        } else {
            int[] smartAI = aggressiveAIMove(board);

            if (smartAI[2] == 1)
                board[smartAI[0]][smartAI[1]] = ai;
            else
                board[aiMove(board, aiTurn)[1]][aiMove(board, aiTurn)[2]] = ai;

            delay(2);
        }
        aiTurn = !aiTurn;
    }
    
    public static int[] aggressiveAIMove(char[][] board) {
        //counter++;
        //System.out.println(counter);
        int[] result = new int[3];

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[i][j] == ' ') {
                    board[i][j] = ai;

                    if (gameResult(board, ai, human) == 1) {
                        result[0] = i;
                        result[1] = j;
                        result[2] = 1;
                        return result;
                    }
                    board[i][j] = ' ';
                }
            }
        }
        return result;
    }

    //minimax algorithm
    public static int[] aiMove(char[][] board, boolean aiTurn) {
        //counter++;
        //System.out.println(counter);
        if (gameOver())
            return new int[]{gameResult(board, ai, human), 0, 0};

        int bestX = 0, bestY = 0, score = aiTurn ? -1 : 1;

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[i][j] == ' ') {
                    board[i][j] = aiTurn ? ai : human;
                    int bestScore = aiMove(board, !aiTurn)[0];
                    board[i][j] = ' ';

                    //alpha beta pruning
                    if (bestScore == 1 && aiTurn || //maximizing player
                            bestScore == -1  && !aiTurn) { //minimizing
                        bestX = i;
                        bestY = j;
                        score = bestScore;
                        return new int[]{score, bestX, bestY};
                    }
                }
            }
        }
        return new int[]{score, bestX, bestY};
    }

    public static void start() {
        char input = ' ';
        System.out.println("\nWELCOME TO TIC TAC TOE!!!!!!\t");
        System.out.print("\nWould you like to start first? Enter Yes/No\t");

        while (loop) {
            input = scanner.next().charAt(0);
            if (Character.toLowerCase(input) == 'y' || Character.toLowerCase(input) == 'n')
                loop = false;
            else
                System.out.print("Try again. Y/N\t");
        }

        if (Character.toLowerCase(input) == 'n')
            aiTurn = true;

        System.out.print("\nWould you like to be X or O?\t");

        loop = true;
        while (loop) {
            human = scanner.next().charAt(0);
            if (Character.toLowerCase(human) == 'x' || Character.toLowerCase(human) == 'o')
                loop = false;
            else
                System.out.print("Try again. X or O\t");
        }

        human = Character.toUpperCase(human);
        ai = human == 'X'? 'O':'X';
    }

    public static void printBoard(char[][] board) {
        System.out.print("\n");
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (j < 2)
                    System.out.print(" " + board[i][j] + " |");
                else
                    System.out.println(" " + board[i][j]);
            }
            if (i < 2)
                System.out.println("-----------");
        }
    }

    public static int gameResult(char[][] board, char ai, char human) {
        //check row and column
        for (int i = 0; i < 3; i++) {
            if (board[i][0] == ai && board[i][1] == ai && board[i][2] == ai || board[0][i] == ai && board[1][i] == ai && board[2][i] == ai)
                return 1;
            else if (board[i][0] == human && board[i][1] == human && board[i][2] == human || board[0][i] == human && board[1][i] == human && board[2][i] == human)
                return -1;
        }
        //check diagonals
        if (board[0][0] == ai && board[1][1] == ai && board[2][2] == ai || board[0][2] == ai && board[1][1] == ai && board[2][0] == ai)
            return 1;
        else if (board[0][0] == human && board[1][1] == human && board[2][2] == human || board[0][2] == human && board[1][1] == human && board[2][0] == human)
            return -1;

        return 0; //tie or game in progress
    }

    public static boolean gameOver() {
        if (gameResult(board, ai, human) == 0)
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    if (board[i][j] == ' ')
                        return false; //game in progress
                }
            }
        return true; //tie or someone wins
    }

    public static int[] getXY(int pos) {
        switch (pos) {
            case 1 -> {
                x = 0;
                y = 0;
            }
            case 2 -> {
                x = 0;
                y = 1;
            }
            case 3 -> {
                x = 0;
                y = 2;
            }
            case 4 -> {
                x = 1;
                y = 0;
            }
            case 5 -> {
                x = 1;
                y = 1;
            }
            case 6 -> {
                x = 1;
                y = 2;
            }
            case 7 -> {
                x = 2;
                y = 0;
            }
            case 8 -> {
                x = 2;
                y = 1;
            }
            case 9 -> {
                x = 2;
                y = 2;
            } //no need default break because if statement before this method is called already handled numbers not from 1-9
        }
        return new int[]{x, y};
    }

    public static void delay(int timeout) throws InterruptedException {
        System.out.print("\nAI thinking");
        long start = System.currentTimeMillis();
        long end = start + timeout*1000; // 60 seconds * 1000 ms/sec

        while (System.currentTimeMillis() < end) {
            for (int i = 0; i < 3; i++) {
                System.out.print(".");
                TimeUnit.MILLISECONDS.sleep(400);
            }
            System.out.print("\b\b\b");
            TimeUnit.MILLISECONDS.sleep(300);
        }
        System.out.print("\n");
    }
}
