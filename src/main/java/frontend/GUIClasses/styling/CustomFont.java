package frontend.GUIClasses.styling;

import java.awt.*;
import java.io.InputStream;

public class CustomFont {
    private Font regularFont;
    private Font boldFont;

    /** Handles custom fonts for the project, along with boldness and size of text **/
    public CustomFont() {
        regularFont = newFont("fonts/Ubuntu-Regular.ttf", Font.PLAIN, 25f);
        boldFont = newFont("fonts/Ubuntu-Bold.ttf", Font.BOLD, 168f);
    }

    /** Loads and registers a custom font
     * Falls back to default if loading fails
     * @param path The path of the needed font
     * @param fallbackStyle The style of the font. can be either PLAIN, BOLD or ITALIC
     * @param size The size of the new font
     **/
    private Font newFont(String path, int fallbackStyle, float size) {
        try (InputStream is = getClass().getClassLoader().getResourceAsStream(path)) {
            if (is == null) {
                return new Font("SansSerif", fallbackStyle, Math.round(size));
            }

            Font font = Font.createFont(Font.TRUETYPE_FONT, is);
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(font);
            return font.deriveFont(size);

        } catch (Exception e) {
            e.printStackTrace();
            return new Font("SansSerif", fallbackStyle, Math.round(size));
        }
    }
    /**
     * Returns resized regular font
     * @param size The size of the font
     **/
    public Font regular(float size) {
        return regularFont.deriveFont(size);
    }

    /** Returns resized bold font
     * @param size The size of the font
     **/
    public Font bold(float size) {
        return boldFont.deriveFont(size);
    }
}