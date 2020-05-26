import java.util.ArrayList;
public class Board implements Comparable<Board> {
    private int[][] board;
    private Board previous;
    private int rowPositionOfBlankTile, columnPositionOfBlankTile;

    private final int N = 3;
    private final int NUMBER_OF_TILES = 9;

    public enum Directions {UP, DOWN, LEFT, RIGHT}

    public Board() {
        board = new int[N][N];
        boolean notSolvable = true;
        while (notSolvable) {
            randomizeBoard();
            notSolvable = !isSolvable();
        }
    }

    private Board(Board oldBoard) {
        this.board = new int[N][N];
        for (int row = 0; row < N; ++row) {
            for (int column = 0; column < N; ++column) {
                this.board[row][column] = oldBoard.board[row][column];
            }
        }
        this.rowPositionOfBlankTile = oldBoard.rowPositionOfBlankTile;
        this.columnPositionOfBlankTile = oldBoard.columnPositionOfBlankTile;
    }

    private void randomizeBoard() {
        final int BLANK_TILE = 0;
        int randomNumber = -1;
        boolean randomNumberGeneratedIsNotNew;

        ArrayList<Integer> numbersUsed = new ArrayList<>(NUMBER_OF_TILES);

        for (int row = 0; row < N; ++row) {
            for (int column = 0; column < N; ++column) {
                randomNumberGeneratedIsNotNew = true;

                while (randomNumberGeneratedIsNotNew) {
                    randomNumber = (int) (Math.random() * NUMBER_OF_TILES);
                    randomNumberGeneratedIsNotNew = numbersUsed.contains(randomNumber);
                }

                numbersUsed.add(randomNumber);
                board[row][column] = randomNumber;

                if (randomNumber == BLANK_TILE) {
                    rowPositionOfBlankTile = row;
                    columnPositionOfBlankTile = column;
                }
            }
        }
    }

    public boolean isSolved() {
        int correctNumber = 0;

        for (int row = 0; row < N; ++row) {
            for (int column = 0; column < N; ++column) {
                if (board[row][column] != correctNumber) {
                    return false;
                }
                ++correctNumber;
            }
        }
        return true;
    }

    private boolean isSolvable() {
        int numberOfInversions = 0;
        int[] inversionChecker = new int[NUMBER_OF_TILES];
        int inversionCheckerIndex = 0;

        for (int boardRow = 0; boardRow < N; ++boardRow) {
            for (int boardColumn = 0; boardColumn < N; ++boardColumn) {
                inversionChecker[inversionCheckerIndex] = board[boardRow][boardColumn];
                ++inversionCheckerIndex;
            }
        }//put the board in a format where it can be checked for inversions

        for (int before = 0; before < (inversionChecker.length - 1); ++before) {
            for (int after = before + 1; after < inversionChecker.length; ++after) {
                if (inversionChecker[before] > inversionChecker[after]) {
                    ++numberOfInversions;
                }
            }
            if (inversionChecker[before] == 0 && before % 2 == 1) {
                ++numberOfInversions;
            }
        }//run through and count inversions

        return (numberOfInversions % 2 == 0);
        //if the number of inversions is even, the board is unsolvable
    }

    public ArrayList<Board> getNextMoves() {
        ArrayList<Board> nextMoves = new ArrayList<>();

        if (moveIsSafe(Directions.UP)) {
            Board newBoard = new Board(this);
            newBoard.previous = this;
            newBoard.makeMove(Directions.UP);
            nextMoves.add(newBoard);
        }
        if (moveIsSafe(Directions.DOWN)) {
            Board newBoard = new Board(this);
            newBoard.previous = this;
            newBoard.makeMove(Directions.DOWN);
            nextMoves.add(newBoard);
        }
        if (moveIsSafe(Directions.RIGHT)) {
            Board newBoard = new Board(this);
            newBoard.previous = this;
            newBoard.makeMove(Directions.RIGHT);
            nextMoves.add(newBoard);
        }
        if (moveIsSafe(Directions.LEFT)) {
            Board newBoard = new Board(this);
            newBoard.previous = this;
            newBoard.makeMove(Directions.LEFT);
            nextMoves.add(newBoard);
        }
        return nextMoves;
    }

    private void makeMove(Directions direction) {
        if (moveIsSafe(direction)) {
            if (direction == Directions.UP) {
                swap(rowPositionOfBlankTile, columnPositionOfBlankTile, --rowPositionOfBlankTile, columnPositionOfBlankTile);
            } else if (direction == Directions.DOWN) {
                swap(rowPositionOfBlankTile, columnPositionOfBlankTile, ++rowPositionOfBlankTile, columnPositionOfBlankTile);
            } else if (direction == Directions.RIGHT) {
                swap(rowPositionOfBlankTile, columnPositionOfBlankTile, rowPositionOfBlankTile, ++columnPositionOfBlankTile);
            } else {
                swap(rowPositionOfBlankTile, columnPositionOfBlankTile, rowPositionOfBlankTile, --columnPositionOfBlankTile);
            }
        }
    }

    private void swap(int currentRow, int currentColumn, int desiredRow, int desiredColumn) {
        board[currentRow][currentColumn] ^= board[desiredRow][desiredColumn];
        board[desiredRow][desiredColumn] ^= board[currentRow][currentColumn];
        board[currentRow][currentColumn] ^= board[desiredRow][desiredColumn];
    }

    private boolean moveIsSafe(Directions direction) {
        final int BAD_POSITION_TO_MOVE_UP_OR_LEFT = 0;
        final int BAD_POSITION_TO_MOVE_DOWN_OR_RIGHT = 2;

        if (direction == Directions.UP) {
            return rowPositionOfBlankTile != BAD_POSITION_TO_MOVE_UP_OR_LEFT;
        } else if (direction == Directions.DOWN) {
            return rowPositionOfBlankTile != BAD_POSITION_TO_MOVE_DOWN_OR_RIGHT;
        } else if (direction == Directions.RIGHT) {
            return columnPositionOfBlankTile != BAD_POSITION_TO_MOVE_DOWN_OR_RIGHT;
        } else {
            return columnPositionOfBlankTile != BAD_POSITION_TO_MOVE_UP_OR_LEFT;
        }
    }

    private int getHeuristicValue() {
        int value = 0;
        for (int row = 0; row < N; ++row) {
            for (int column = 0; column < N; ++column) {
                final int tile = board[row][column];
                value += Math.abs(row - (tile / 3)) + Math.abs(column - (tile % 3));
            }
        }
        return value;
    }

    public boolean equals(Board otherBoard) {
        for (int row = 0; row < N; ++row) {
            for (int column = 0; column < N; ++column) {
                if (board[row][column] != otherBoard.board[row][column]) {
                    return false;
                }
            }
        }
        return true;
    }

    public int compareTo(Board otherBoard) {
        if (this.getHeuristicValue() > otherBoard.getHeuristicValue()) {
            return 1;
        } else if (this.getHeuristicValue() < otherBoard.getHeuristicValue()) {
            return -1;
        }
        return 0;
    }

    public Board getPrevious() {
        return previous;
    }

    public int[] getGraphicsHelper() {
        int[] graphicsHelper = new int[NUMBER_OF_TILES];
        for (int row = 0; row < N; ++row) {
            for (int column = 0; column < N; ++column) {
                graphicsHelper[(N * row) + column] = board[row][column];
            }
        }
        return graphicsHelper;
    }

    public void print() {
        for (int row = 0; row < N; ++row) {
            System.out.print("|");
            for (int column = 0; column < N; ++column) {
                System.out.print(board[row][column] + "|");
            }
            System.out.println();
        }
        System.out.println();
    }
}