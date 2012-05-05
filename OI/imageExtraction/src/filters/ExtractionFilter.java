/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package filters;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.util.ArrayList;
import java.util.List;
import sys.RGBHelper;
import sys.Region;

/**
 *
 * @author Lukasz
 */
public class ExtractionFilter {

    private static Point[] directions = new Point[]{
        new Point(-1, -1),
        new Point(-1, 0),
        new Point(-1, 1),
        new Point(1, -1),
        new Point(1, 0),
        new Point(1, 1),
        new Point(0, 1),
        new Point(0, -1),};
    private List<Region> regions;
    BufferedImage image;

    float maxValue = 345f / 360f;
    private WritableRaster imageRaster;

    public ExtractionFilter() {
        regions = new ArrayList<Region>();
    }

    public BufferedImage processImage(BufferedImage inImage) {
        
        BufferedImage tmp = new BufferedImage(inImage.getWidth(), inImage.getHeight(), BufferedImage.TYPE_BYTE_BINARY);
        Graphics g = tmp.getGraphics();
        Point testPoint;
        boolean test;
        for (int x = 0; x < inImage.getWidth(); x++) {
            for (int y = 0; y < inImage.getHeight(); y++) {
                if(hsvPixel(inImage.getRGB(x, y))){
                    g.setColor(Color.WHITE);
                    g.fillRect(x, y, 1, 1);
                }
                else{
                    g.setColor(Color.BLACK);
                    g.fillRect(x, y, 1, 1);
                }
            }
          }
        image = tmp;
        imageRaster = tmp.getRaster();
        BufferedImage tmp2 = new BufferedImage(inImage.getWidth(), inImage.getHeight(), BufferedImage.TYPE_3BYTE_BGR);
        g = tmp2.getGraphics();
        g.drawImage(inImage,0,0, null);
        for (int x = 0; x < inImage.getWidth(); x++) {
            for (int y = 0; y < inImage.getHeight(); y++) {
                testPoint = new Point(x, y);
                if (testPixel(testPoint)) {

                    test = false;
                    for (Region region : regions) {
                        if (region.containsPoint(testPoint)) {
                            test = true;
                            break;
                        }
                    }
                    if (test) {
                        continue;
                    }
                    fillRegion(testPoint);

                }
            }
        }
        for (Region region : regions) {
            if(region.pixels.size()>500){
            float ratio = region.getRatio();
            if (ratio >= 0.7 && ratio <= 1.3) {
                for (Point point : region.pixels) {
                    g.setColor(Color.WHITE);
                    g.fillRect(point.x, point.y, 1, 1);
                }
                g.setColor(Color.RED);
                Rectangle rect = region.getArea();
                g.drawRect(rect.x, rect.y, rect.width, rect.height);
                g.drawRect(rect.x-1, rect.y-1, rect.width-1, rect.height-1);
                g.drawRect(rect.x-2, rect.y-2, rect.width-2, rect.height-2);
            }
            }
        }
        return tmp2;
    }

    private boolean testPixel(Point point) {
        if((point.x<0 || point.x>image.getWidth()-1)||(point.y<0 || point.y>image.getHeight()-1)){
            return false;
        }
        return imageRaster.getSample(point.x, point.y, 0)==1;
    }
    private boolean hsvPixel(int pixel) {
        float[] hsv = RGBHelper.getHSV(pixel);
        return (hsv[0] >= maxValue) && hsv[1] >= 0.55;
    }

    private void fillRegion(Point point) {
        Region region = new Region();
        region.addPoint(point);
        regions.add(region);
        List<Point> testPoints = new ArrayList<Point>();
        testPoints.add(point);
        while (!testPoints.isEmpty()) {

            List<Point> tmpTestPoints = new ArrayList<Point>();
            for (Point testPoint : testPoints) {
                for (Point direction : directions) {
                    Point newPoint = new Point(testPoint.x + direction.x, testPoint.y + direction.y);
                    if (testPixel(newPoint) && !region.pixels.contains(newPoint)) {
                        tmpTestPoints.add(newPoint);
                        region.addPoint(newPoint);
                    }
                }
            }
            testPoints = tmpTestPoints;
        }
    }
}
