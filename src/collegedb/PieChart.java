package collegedb;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.shape.ArcType;
import java.util.HashMap;
import java.util.Map;
import static java.lang.StrictMath.cos;
import static java.lang.StrictMath.PI;
import static java.lang.StrictMath.sin;

public class PieChart {
    public PieChart() {
        hm = DatabaseModification.GPAsAndStudents();
    }
    public int getTotalFrequency() {
        int freq = 0;
        for (Map.Entry<Character, Integer> it : hm.entrySet()) {
            freq += it.getValue();
        }
        return freq;
    }
    public void draw(GraphicsContext gc) {
        double x = gc.getCanvas().getWidth()/2; // x-coordinate of center of canvas
        double y = gc.getCanvas().getHeight()/2; // y-coordinate of center of canvas
        double r = y/2; // radius of pie chart
        int currentColor = 0, totalFreq = getTotalFrequency();
        float currentAngle = 0;
        for (char i = 0; i < 6; i++) {
            gc.setFill(colors[currentColor++].getPaint());
            if (hm.containsKey(GPAs[i])) {
                int currFreq = hm.get(GPAs[i]); // # of students
                float poe = (float)currFreq/totalFreq; // probability of event
                gc.fillArc(x - r,y - r, r * 2, r * 2, currentAngle,poe * 360, ArcType.ROUND);
                gc.setFill(MyColor.BLACK.getPaint()); // change fill for text
                double midOfArcAngle = (currentAngle + (poe * 180)) * PI/180;
                gc.fillText("GPA: " + GPAs[i] + ", " + currFreq + " students",
                        x + (r * cos(midOfArcAngle)) * 1.6,
                        y - (r * sin(midOfArcAngle)) * 1.6); // 1.6 is a scaling factor for no overlap
                currentAngle += poe * 360;
            }
        }
    }
    private HashMap<Character, Integer> hm;
    private static char[] GPAs = new char[]{'A', 'B', 'C', 'D', 'F', 'W'};
    private static MyColor[] colors = new MyColor[]{MyColor.GREEN, MyColor.YELLOWGREEN, MyColor.YELLOW, MyColor.ORANGE,
            MyColor.RED, MyColor.WHITESMOKE};
}
