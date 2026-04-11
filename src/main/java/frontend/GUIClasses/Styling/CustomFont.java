package frontend.GUIClasses.Styling;

import java.awt.*;
import java.io.InputStream;

public class CustomFont {
    private Font regularFont;
    private Font boldFont;

    public CustomFont() {
        regularFont = newFont("fonts/Ubuntu-Regular.ttf", Font.PLAIN, 16f);
        boldFont = newFont("fonts/Ubuntu-Bold.ttf", Font.BOLD, 16f);
    }

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

    public Font regular(float size) {
        return regularFont.deriveFont(size);
    }

    public Font bold(float size) {
        return boldFont.deriveFont(size);
    }
}