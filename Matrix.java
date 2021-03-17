import java.io.File;
import java.util.Scanner;
import java.io.IOException;

/**
 * Matrix class.
 */
public class Matrix {

    /**
     * Fill a matrix of integers from the contents of a file.
     *
     * @param fileName
     * @param matrix
     * @return int[][]
     */
    public static int[][] fill(String fileName, int[][] matrix) {
        try {
            Scanner file = new Scanner(new File(fileName));

            for (int i = 0; i < matrix.length; i++) {
                for (int j = 0; j < matrix[i].length; j++) {
                    matrix[i][j] = file.hasNextInt() ? file.nextInt() : 0;
                }
            }

            file.close();
        } catch (IOException e) {
            Console.out(String.format("There was a problem opening or reading the file: %s", fileName));
        } catch (Exception e) {
            Console.out("Unable to parse values from file. Please make sure the data is intact.");
        }

        return matrix;
    }

    /**
     * Find the maximum value in a given matrix.
     *
     * @param matrix
     * @return int
     */
    public static int findMax(int[][] matrix) {
        return findExtreme(matrix, ExtremeMode.MAX);
    }

    /**
     * Find the minimum value in a given matrix.
     *
     * @param matrix
     * @return int
     */
    public static int findMin(int[][] matrix) {
        return findExtreme(matrix, ExtremeMode.MIN);
    }

    /**
     * Find the maximum value in a given array.
     *
     * @param array
     * @return int
     */
    public static int findMaxOfArray(int[] array) {
        return findExtremeOfArray(array, ExtremeMode.MAX);
    }

    /**
     * Find the minimum value in a given array.
     *
     * @param array
     * @return int
     */
    public static int findMinOfArray(int[] array) {
        return findExtremeOfArray(array, ExtremeMode.MIN);
    }

    /**
     * Find the index of the maximum value in a given matrix.
     *
     * @param matrix
     * @return int[]
     */
    public static int[] findMaxIndex(int[][] matrix) {
        return findExtremeIndex(matrix, ExtremeMode.MAX);
    }

    /**
     * Find the index of the minimum value in a given matrix.
     *
     * @param matrix
     * @return int[]
     */
    public static int[] findMinIndex(int[][] matrix) {
        return findExtremeIndex(matrix, ExtremeMode.MIN);
    }

    /**
     * Find the index of the maximum value in a given array.
     *
     * @param array
     * @return int
     */
    public static int findMaxIndexOfArray(int[] array) {
        return findExtremeIndexOfArray(array, ExtremeMode.MAX);
    }

    /**
     * Find the index of the minimum value in a given array.
     *
     * @param array
     * @return int
     */
    public static int findMinIndexOfArray(int[] array) {
        return findExtremeIndexOfArray(array, ExtremeMode.MIN);
    }

    /**
     * Find the extreme value in a given matrix.
     *
     * @param matrix
     * @param mode
     * @return int
     */
    private static int findExtreme(int[][] matrix, ExtremeMode mode) {
        int extreme = matrix[0][0];

        for (int i = 0; i < matrix.length; i++) {
            extreme = Extreme.find(findExtremeOfArray(matrix[i], mode), extreme, mode);
        }

        return extreme;
    }

    /**
     * Find the extreme value in a given array.
     *
     * @param array
     * @param mode
     * @return int
     */
    private static int findExtremeOfArray(int[] array, ExtremeMode mode) {
        int extreme = array[0];

        for (int i = 0; i < array.length; i++) {
            extreme = Extreme.find(array[i], extreme, mode);
        }

        return extreme;
    }

    /**
     * Find the compound index of the extreme value in a given matrix.
     *
     * @param array
     * @param mode
     * @return int[]
     */
    private static int[] findExtremeIndex(int[][] matrix, ExtremeMode mode) {
        int extremeRow = 0;
        int extremeCol = 0;
        int extremeValue = matrix[extremeRow][extremeCol];

        for (int i = 0; i < matrix.length; i++) {
            int newExtreme = Extreme.find(findExtremeOfArray(matrix[i], mode), extremeValue, mode);

            if (newExtreme != extremeValue) {
                extremeRow = i;
                extremeCol = findExtremeIndexOfArray(matrix[i], mode);
            }

            extremeValue = newExtreme;
        }

        return new int[] { extremeRow, extremeCol };
    }

    /**
     * Find the index of the extreme value in a given array.
     *
     * @param array
     * @param mode
     * @return int
     */
    private static int findExtremeIndexOfArray(int[] array, ExtremeMode mode) {
        int extremeIndex = 0;
        int extremeValue = array[extremeIndex];

        for (int i = 0; i < array.length; i++) {
            int newExtreme = Extreme.find(array[i], extremeValue, mode);

            if (newExtreme != extremeValue) {
                extremeIndex = i;
            }

            extremeValue = newExtreme;
        }

        return extremeIndex;
    }
}
