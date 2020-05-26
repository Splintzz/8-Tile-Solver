import java.awt.Color;
import java.util.ArrayList;
import javax.swing.*;
import java.awt.GridLayout;
import java.util.concurrent.TimeUnit;
import java.awt.Font;

public class GraphicsBoard extends JFrame {
    final private int N = 3;
    private JButton[] buttons;
    private JPanel board;
    private Board unsolvedBoard;
    private Solver solver;
    private ArrayList<Board> solutionPath;

    public GraphicsBoard() {
        super("8 Tile Solver");
        buttons = new JButton[N*N];
        board = new JPanel();
        unsolvedBoard = new Board();
        solver = new Solver();
        solutionPath = solver.getSolvePath(unsolvedBoard);
    }

    public void runSolvingProcess() {
        final int SOLVE_DELAY = 1, END_DELAY = 3;
        final int WITH_SUCCESSFUL_EXIT = 0;

        setUp();

        for(int step = (solutionPath.size()-1); step >= 0; --step) {
            delay(SOLVE_DELAY);
            loadNextStep(solutionPath.get(step));
        }

        delay(SOLVE_DELAY);
        setBoardAsSolved();
        delay(END_DELAY);
        System.exit(WITH_SUCCESSFUL_EXIT);
    }

    private void delay(int delayTime) {
        try {
            TimeUnit.SECONDS.sleep(delayTime);
        }catch(InterruptedException e) {
            final int WITHOUT_SUCCESSFUL_EXIT = -1;
            JOptionPane.showMessageDialog(null, "Something went wrong.", "ERROR", JOptionPane.INFORMATION_MESSAGE);
            System.exit(WITHOUT_SUCCESSFUL_EXIT);
        }
    }

    private void setBoardAsSolved() {
        for(JButton button : buttons) {
            button.setBackground(Color.GREEN);
            button.setOpaque(true);
        }
    }

    private void loadNextStep(Board board) {
        int[] graphicsHelper = board.getGraphicsHelper();

        for(int tile = 0; tile < buttons.length; ++tile) {
            buttons[tile].setText(graphicsHelper[tile]+"");
        }
    }

    private void setUp() {
        final Font buttonFont = new Font("Arial", Font.BOLD, 50);

        setVisible(true);
        setResizable(true);
        setSize(500, 500); //Sets window size to 500 x 500

        board.setVisible(true);
        board.setLayout(new GridLayout(N, N));
        add(board);

        for (int button = 0; button < buttons.length; ++button) {
            buttons[button] = new JButton();
            buttons[button].setFont(buttonFont);
            board.add(buttons[button]);
        }
    }
}