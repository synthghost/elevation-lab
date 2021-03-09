import java.awt.Color;

/**
 * Colors class.
 */
public class Colors {

    /**
     * Define various colors.
     */
    public static final Color background = Color.BLACK;

    public static final Color normalPath = new Color(116, 247, 255, (int) (255 * 0.015));

    public static final Color bestPath = Color.GREEN;

    public static final Color worstPath = Color.RED;

    public static final Color highElevation = new Color(181, 33, 22, 150);

    public static final Color midElevation = new Color(224, 186, 78, 150);

    public static final Color lowElevation = new Color(0, 90, 25, 150);

    /**
     * Interpolate between two colors.
     *
     * Algorithm taken and adapted from:
     * https://harmoniccode.blogspot.com/2011/04/bilinear-color-interpolation.html
     *
     * @param color1
     * @param color2
     * @param blend
     * @return Color
     */
    public static Color interpolateColor(Color color1, Color color2, float blend) {
        float conversion = 1f / 255f;

        float red1 = color1.getRed() * conversion;
        float green1 = color1.getGreen() * conversion;
        float blue1 = color1.getBlue() * conversion;
        float alpha1 = color1.getAlpha() * conversion;

        float red2 = color2.getRed() * conversion;
        float green2 = color2.getGreen() * conversion;
        float blue2 = color2.getBlue() * conversion;
        float alpha2 = color2.getAlpha() * conversion;

        float delta_red = red2 - red1;
        float delta_green = green2 - green1;
        float delta_blue = blue2 - blue1;
        float delta_alpha = alpha2 - alpha1;

        blend = Math.min(Math.max(blend, 0f), 1f);

        float red = red1 + (delta_red * blend);
        float green = green1 + (delta_green * blend);
        float blue = blue1 + (delta_blue * blend);
        float alpha = alpha1 + (delta_alpha * blend);

        red = Math.min(Math.max(red, 0f), 1f);
        green = Math.min(Math.max(green, 0f), 1f);
        blue = Math.min(Math.max(blue, 0f), 1f);
        alpha = Math.min(Math.max(alpha, 0f), 1f);

        return new Color(red, green, blue, alpha);
    }
}
