import java.util.ArrayList;
import java.util.Collections;

public class Solver {
    private ArrayList<Board> visited;
    private ArrayList<Board> frontier;
    private ArrayList<Board> solvePath;

    public Solver() {
        visited = new ArrayList<>();
        frontier = new ArrayList<>();
        solvePath = new ArrayList<>();
    }

    public ArrayList<Board> getSolvePath(Board unsolved) {
        final int nextBoard = 0;
        visited.add(unsolved);
        frontier.add(unsolved);
        Board currentBoard = unsolved;

        while((!currentBoard.isSolved())) {
            visited.add(currentBoard);
            ArrayList<Board> nextMoves = currentBoard.getNextMoves();
            frontier.remove(nextBoard);
            for(Board move : nextMoves) {
                if(!hasBeenVisited(move)) {
                    frontier.add(move);
                }
            }
            Collections.sort(frontier);
            currentBoard = frontier.get(nextBoard);
        }

        while(currentBoard.getPrevious() != null) {
            solvePath.add(currentBoard);
            currentBoard = currentBoard.getPrevious();
        }

        return solvePath;
    }

    private boolean hasBeenVisited(Board board) {
        for(Board move : visited) {
            if(move.equals(board)) {
                return true;
            }
        }
        return false;
    }
}