import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;

class SudokuTest {
    @Test
    public void stringToInts() {
        String number = "1234";
        int[] arr = Sudoku.stringToInts(number);
        for (int i = 0; i < arr.length; i++)
            assertEquals(number.charAt(i) - '0', arr[i]);
    }

    @Test
    public void textToGrid() {
        String str = "123456789";
        String s = "";
        for (int i = 0; i < 9; i++)
            s += str;
        int[][] arr = Sudoku.textToGrid(s);
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                assertEquals(str.charAt(j) - '0', arr[i][j]);
            }
        }
    }

    @Test
    public void textToGridInvalidSize() {
        String str = "124141241";
        Assertions.assertThrows(RuntimeException.class, () -> {
            Sudoku.textToGrid(str);
        });
    }

    @Test
    public void stringToGrid() {
        String str = "123456789";
        String[] s = new String[9];
        for (int i = 0; i < 9; i++) {
            s[i] = str;
        }
        int[][] arr = Sudoku.stringsToGrid(s);
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                assertEquals(str.charAt(j) - '0', arr[i][j]);
            }
        }
    }

    @Test
    public void sudokuBadConstructors() {
        int[][] invalidRow = new int[1][Sudoku.SIZE];
        int[][] invalidCol = new int[Sudoku.SIZE][3];
        assertThrows(RuntimeException.class, () -> {
            new Sudoku(invalidCol);
        });
        assertThrows(RuntimeException.class, () -> {
            new Sudoku(invalidRow);
        });
    }

    @Test
    public void sudoku() {
        Sudoku sudo = new Sudoku(Sudoku.hardGrid);
        sudo.solve();
        int[][] hardGrid = Sudoku.stringsToGrid(
                "3 0 0 0 0 0 0 8 0",
                "0 0 1 0 9 3 0 0 0",
                "0 4 0 7 8 0 0 0 3",
                "0 9 3 8 0 0 0 1 2",
                "0 0 0 0 4 0 0 0 0",
                "5 2 0 0 0 6 7 9 0",
                "6 0 0 0 2 1 0 4 0",
                "0 0 0 5 3 0 9 0 0",
                "0 3 0 0 0 0 0 5 1");
        sudo = new Sudoku(hardGrid);
        sudo.solve();

    }

    @Test
    public void allZeros() {
        String[] args = new String[1];
        args[0] = "Anything";
        Sudoku sudo;
        String str = "000000000";
        String allZeros = "";
        for (int i = 0; i < 9; i++) {
            allZeros += str;
        }
        sudo = new Sudoku(allZeros);
        sudo.solve();
    }

    @Test
    public void main() {
        String[] args = new String[1];
        args[0] = "Anything";
        Sudoku sudo = new Sudoku(Sudoku.hardGrid);
        sudo.main(args);
    }


}