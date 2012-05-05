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

    public List<Point> pixels;
    private Point[] corners;
    private Rectangle area = null;
    private Rectangle testArea = null;

    public Region(Point point) {
        this();
        pixels.add(point);
    }

    public Region() {
        pixels = new ArrayList<Point>();
        corners = new Point[]{
            new Point(),
            new Point(),
            new Point(),
            new Point()
        };
    }

    public boolean addPoint(Point point) {
        if (!pixels.contains(point)) {
            pixels.add(point);
            updateCorners(point);
            return true;
        }
        return false;
    }

    private void updateCorners(Point point) {
        if (pixels.size() == 1) {
            for (int i = 0; i < 4; i++) {
                corners[i] = point;
            }
            area = null;
            return;
        }
        if (point.x < corners[0].x && point.y < corners[0].y) {
            corners[0] = point;
        }
        if (point.x < corners[1].x && point.y > corners[1].y) {
            corners[1] = point;
        }
        if (point.x > corners[2].x && point.y > corners[2].y) {
            corners[2] = point;
        }
        if (point.x > corners[3].x && point.y < corners[3].y) {
            corners[3] = point;
        }
        area = null;
    }

    public Rectangle getArea() {
        if (area == null) {
            int x = Math.min(corners[0].x, corners[1].x);
            int y = Math.min(corners[0].y, corners[3].y);
            int width = Math.max(corners[3].x - corners[0].x, corners[2].x - corners[1].x);
            int height = Math.max(corners[1].y - corners[0].y, corners[2].y - corners[3].y);
            area = new Rectangle(x, y, width, height);
        }
        return area;
    }

    public Rectangle getTestArea() {
        if (testArea == null) {
            int x = Math.min(corners[0].x, corners[1].x) - 1;
            int y = Math.min(corners[0].y, corners[3].y) - 1;
            int width = Math.max(corners[3].x - corners[0].x, corners[2].x - corners[1].x) + 2;
            int height = Math.max(corners[1].y - corners[0].y, corners[2].y - corners[3].y) + 2;
            testArea = new Rectangle(x, y, width, height);
        }
        return testArea;
    }

    public float getRatio() {
        Rectangle rect = getArea();
        return rect.width == 0 ? 0 : rect.height / rect.width;
    }

    public boolean containsPoint(Point point) {
        Rectangle rect = getArea();
        if (rect.contains(point)) {
            if (pixels.contains(point)) {
                return true;
            }
        }
        return false;
    }
}
