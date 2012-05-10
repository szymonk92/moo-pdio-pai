/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sys;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Lukasz
 */
public class Region {

    private static Point[] directions = new Point[]{
        new Point(0, -1),
        new Point(1, -1),
        new Point(1, 0),
        new Point(1, 1)
    };
    public List<Point> pixels;
    private Rectangle area;
    private Rectangle testArea;
    private boolean testAreaComputed = false;

    public Region(Point point) {
        pixels = new ArrayList<Point>();
        area = new Rectangle(point);
        testArea = new Rectangle(point);
        pixels.add(point);
    }

    public void addPoint(Point point) {
        pixels.add(point);
        area.add(point);
        testAreaComputed = false;
    }

    public Rectangle getArea() {
        return area;
    }

    public Rectangle getTestArea() {
        if (!testAreaComputed) {
            testArea.setBounds(area.x - 1, area.y - 1, area.width + 3, area.height + 3);
        }
        return testArea;
    }

    public float getRatio() {
        return area.width == 0 ? 0 : (float) area.height / (float) area.width;
    }

    public boolean containsPoint(Point point) {
        if (getTestArea().contains(point)) {
            List<Point> neigburs = new ArrayList<Point>();
            for (Point direction : directions) {
                neigburs.add(new Point(point.x + direction.x, point.y + direction.y));
            }
            int minY = point.y - 2;
            int minX = point.x - 2;
            for (int i = pixels.size() - 1; i >= 0; i--) {
                Point testPoint = pixels.get(i);
                if (testPoint.y < minY) {
                    return false;
                }
                if (testPoint.x < minX) {
                    continue;
                }
                if (neigburs.contains(testPoint)) {
                    return true;
                }
            }
        }
        return false;
    }

    public void marge(Region region) {
        pixels.addAll(region.pixels);
        area.add(region.area);
        testAreaComputed = false;
    }

    public void Clear() {
        pixels = null;
        area = null;
        testArea = null;
        testAreaComputed = false;
    }
}
