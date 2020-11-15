package collegedb;

import javafx.scene.paint.Color;

public enum MyColor {
    BLACK(0,0,0), GREEN(141,182,0), ORANGE(255, 165, 0), RED(255,0,0),
    WHITESMOKE(245, 245, 245), YELLOW(255,255,0), YELLOWGREEN(154,205,50);
    MyColor(int r, int g, int b) {
        color = Color.rgb(r,g,b);
    }
    public Color getPaint() {
        return color;
    }
    private Color color;
}