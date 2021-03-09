/**
 * Extreme class.
 */
public class Extreme {

    /**
     * Find the extreme (minimum or maximum) value between two given values.
     *
     * Set to minimum- or maximum-finding mode by passing an ExtremeMode switch.
     *
     * @param value1
     * @param value2
     * @param mode
     * @return int
     */
    public static int find(int value1, int value2, ExtremeMode mode) {
        int comparison = Integer.compare(value1, value2);

        // Invert the comparison when finding the minimum.
        if (mode == ExtremeMode.MIN) {
            comparison = -comparison;
        }

        // Return a newly found extreme.
        if (comparison > 0) {
            return value1;
        }

        return value2;
    }
}
