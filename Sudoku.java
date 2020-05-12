import java.lang.reflect.Array;
import java.util.*;

/*
 * Encapsulates a Sudoku grid to be solved.
 * CS108 Stanford.
 */
public class Sudoku {
    // Provided grid data for main/testing
    // The instance variable strategy is up to you.
    private int sudoku[][];
    private int sol[][];
    private ArrayList<Spot> spots;
    private boolean haveSol;
    private int count;
    private long time;

    // Provided easy 1 6 grid
    // (can paste this text into the GUI too)
    public static final int[][] easyGrid = Sudoku.stringsToGrid(
            "1 6 4 0 0 0 0 0 2",
            "2 0 0 4 0 3 9 1 0",
            "0 0 5 0 8 0 4 0 7",
            "0 9 0 0 0 6 5 0 0",
            "5 0 0 1 0 2 0 0 8",
            "0 0 8 9 0 0 0 3 0",
            "8 0 9 0 4 0 2 0 0",
            "0 7 3 5 0 9 0 0 1",
            "4 0 0 0 0 0 6 7 9");


    // Provided medium 5 3 grid
    public static final int[][] mediumGrid = Sudoku.stringsToGrid(
            "530070000",
            "600195000",
            "098000060",
            "800060003",
            "400803001",
            "700020006",
            "060000280",
            "000419005",
            "000080079");

    // Provided hard 3 7 grid
    // 1 solution this way, 6 solutions if the 7 is changed to 0
    public static final int[][] hardGrid = Sudoku.stringsToGrid(
            "3 7 0 0 0 0 0 8 0",
            "0 0 1 0 9 3 0 0 0",
            "0 4 0 7 8 0 0 0 3",
            "0 9 3 8 0 0 0 1 2",
            "0 0 0 0 4 0 0 0 0",
            "5 2 0 0 0 6 7 9 0",
            "6 0 0 0 2 1 0 4 0",
            "0 0 0 5 3 0 9 0 0",
            "0 3 0 0 0 0 0 5 1");

    private static final int[][] allZeros = Sudoku.stringsToGrid(
            "0 0 0 0 0 0 0 0 0",
            "0 0 0 0 0 0 0 0 0",
            "0 0 0 0 0 0 0 0 0",
            "0 0 0 0 0 0 0 0 0",
            "0 0 0 0 0 0 0 0 0",
            "0 0 0 0 0 0 0 0 0",
            "0 0 0 0 0 0 0 0 0",
            "0 0 0 0 0 0 0 0 0",
            "0 0 0 0 0 0 0 0 0");
    public static final int SIZE = 9;  // size of the whole 9x9 puzzle
    public static final int PART = 3;  // size of each 3x3 part
    public static final int MAX_SOLUTIONS = 100;

    // convert data formats to int[][] grid.
    // Provided various static utility methods to
    // Provided -- the deliverable main().
    // You can edit to do easier cases, but turn in
    // solving hardGrid.
    public static void main(String[] args) {
        Sudoku sudo;
        sudo = new Sudoku(allZeros);
        System.out.println(sudo); // print the raw problem
        int count = sudo.solve();
        System.out.println("solutions:" + count);
        System.out.println("elapsed:" + sudo.getElapsed() + "ms");
        System.out.println(sudo.getSolutionText());
    }


    /**
     * Returns a 2-d grid parsed from strings, one string per row.
     * The "..." is a Java 5 feature that essentially
     * makes "rows" a String[] array.
     * (provided utility)
     *
     * @param rows array of row strings
     * @return grid
     */
    public static int[][] stringsToGrid(String... rows) {
        //System.out.println(rows);
        int[][] result = new int[rows.length][];
        for (int row = 0; row < rows.length; row++) {
            result[row] = stringToInts(rows[row]);
        }
        return result;
    }


    /**
     * Given a single string containing 81 numbers, returns a 9x9 grid.
     * Skips all the non-numbers in the text.
     * (provided utility)
     *
     * @param text string of 81 numbers
     * @return grid
     */
    public static int[][] textToGrid(String text) {
        int[] nums = stringToInts(text);
        if (nums.length != SIZE * SIZE) {
            throw new RuntimeException("Needed 81 numbers, but got:" + nums.length);
        }

        int[][] result = new int[SIZE][SIZE];
        int count = 0;
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                result[row][col] = nums[count];
                count++;
            }
        }
        return result;
    }


    /**
     * Given a string containing digits, like "1 23 4",
     * returns an int[] of those digits {1 2 3 4}.
     * (provided utility)
     *
     * @param string string containing ints
     * @return array of ints
     */
    public static int[] stringToInts(String string) {
        int[] a = new int[string.length()];
        int found = 0;
        for (int i = 0; i < string.length(); i++) {
            if (Character.isDigit(string.charAt(i))) {
                a[found] = Integer.parseInt(string.substring(i, i + 1));
                found++;
            }
        }
        int[] result = new int[found];
        System.arraycopy(a, 0, result, 0, found);
        return result;
    }


    private class Spot implements Comparable {

        private int row, col, valid;

        public Spot(int row, int col) {
            this.row = row;
            this.col = col;
            valid = validValues().size();
        }

        public void setValue(int val) {
            sudoku[row][col] = val;
        }

        private ArrayList<Integer> validValues() {
            HashSet<Integer> used = new HashSet<>();
            ArrayList<Integer> res = new ArrayList<>();
            for (int i = 0; i < SIZE; i++) {
                if (sudoku[row][i] != 0)
                    used.add(sudoku[row][i]);
                if (sudoku[i][col] != 0)
                    used.add(sudoku[i][col]);
            }
            removeBlock(used);
            for (int i = 1; i <= 9; i++) {
                if (!used.contains(i))
                    res.add(i);
            }
            return res;
        }

        private void removeBlock(HashSet<Integer> used) {
            for (int i = row - row % PART; i < row - row % PART + PART; i++) {
                for (int j = col - col % PART; j < col - col % PART + PART; j++) {
                    if (sudoku[i][j] != 0)
                        used.add(sudoku[i][j]);
                }
            }
        }

        @Override
        public int compareTo(Object o) {
            Spot spot = (Spot) o;
            int secondValid = spot.valid;
            int currValid = this.valid;
            if (currValid > secondValid)
                return 1;
            else if (currValid < secondValid)
                return -1;
            return 0;
        }

    }

    /**
     * Sets up based on the given ints.
     */
    public Sudoku(int[][] ints) {
        if (ints[0].length != SIZE || ints.length != SIZE)
            throw new RuntimeException("Invalid parameters");
        sudoku = ints;
        spots = fillSpots();
        Collections.sort(spots);
        haveSol = false;
        count = 0;
    }

    public Sudoku(String str) {
        this(textToGrid(str));
    }

    private ArrayList<Spot> fillSpots() {
        ArrayList<Spot> spots = new ArrayList<>();
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (sudoku[i][j] == 0) {
                    spots.add(new Spot(i, j));
                }
            }
        }
        return spots;
    }

    /**
     * Solves the puzzle, invoking the underlying recursive search.
     */
    public int solve() {
        long start = System.currentTimeMillis();
        solveSudoku(0);
        long end = System.currentTimeMillis();
        time = end - start;
        return count;
    }

    private void solveSudoku(int index) {
        if (count == MAX_SOLUTIONS)
            return;
        if (index == spots.size()) {
            if (!haveSol) {
                saveSol();
                haveSol = true;
            }
            count++;
            return;
        }
        Spot spt = spots.get(index);
        ArrayList<Integer> lst = spt.validValues();
        for (int i = 0; i < lst.size(); i++) {
            int val = lst.get(i);
            spt.setValue(val);
            solveSudoku(index + 1);
            spt.setValue(0);
        }
    }

    private void saveSol() {
        sol = new int[SIZE][SIZE];
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                sol[i][j] = sudoku[i][j];
            }
        }
    }

    public String getSolutionText() {
        String res = "";
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                String extra = Integer.toString(sol[i][j]) + " ";
                res += extra;
            }
            res += "\n";
        }
        return res;
    }

    public long getElapsed() {
        return time;
    }

}
