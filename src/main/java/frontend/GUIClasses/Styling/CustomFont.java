package frontend.GUIClasses.Styling;

import java.awt.*;

public class CustomFont {
    public static Font newFont (float size) {
        try (java.io.InputStream is = CustomFont.class.getClassLoader()
                .getResourceAsStream("fonts/Harlow Solid Regular.ttf")) {

            System.out.println(is);

            if (is == null) {
                throw new RuntimeException("Font file not found.");
            }

            Font customFont = Font.createFont(Font.TRUETYPE_FONT, is);
            return customFont.deriveFont(size);

        } catch (Exception e) {
            e.printStackTrace();
            return new Font("SansSerif", Font.BOLD, (int) size);
        }
    }


}
