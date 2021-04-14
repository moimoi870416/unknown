package menu;

import java.awt.*;
import java.util.ArrayList;

public class Theme {
    public static final Theme DEFAULT_THEME = new Theme(
            new Style.StyleRect(400 , 800, true, new BackgroundType.BackgroundColor(new Color(112, 128, 105)))
                    .setTextFont(new Font("TimesRoman", Font.BOLD, 30))
                    .setHaveBorder(true)
                    .setBorderColor(new Color(211, 211, 211))
                    .setBorderThickness(5),
            new Style.StyleRect(400, 800, true, new BackgroundType.BackgroundColor(new Color(169, 169, 169)))
                    .setHaveBorder(true)
                    .setBorderColor(Color.WHITE)
                    .setBorderThickness(5)
                    .setTextFont(new Font("TimesRoman", Font.BOLD, 30)),
            new Style.StyleRect(400, 800, true, new BackgroundType.BackgroundColor(new Color(178, 34, 34)))
                    .setTextFont(new Font("TimesRoman", Font.BOLD, 35))
                    .setTextColor(new Color(255, 228, 255))
                    .setHaveBorder(true)
                    .setBorderColor(new Color(250, 128, 144))
                    .setBorderThickness(5)
    );
    private static ArrayList<Theme> themes;

    private final Style styleNormal;
    private final Style styleHover;
    private final Style styleFocus;

    public Theme(Style styleNormal, Style styleHover, Style styleFocus) {
        this.styleNormal = styleNormal;
        this.styleHover = styleHover;
        this.styleFocus = styleFocus;
    }

    private static ArrayList<Theme> instance() {
        if (themes == null) {
            themes = new ArrayList<>();
        }
        return themes;
    }

    public static void add(Theme theme) {
        instance().add(theme);
    }

    public static Theme get(int index) {
        return instance().get(index);
    }

    public Style normal() {
        return styleNormal;
    }

    public Style hover() {
        return styleHover;
    }

    public Style focus() {
        return styleFocus;
    }

//
}
