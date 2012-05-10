/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package filters;

import static com.googlecode.javacv.cpp.opencv_core.*;
import static com.googlecode.javacv.cpp.opencv_imgproc.*;
import static com.googlecode.javacv.cpp.opencv_core.*;
 import static com.googlecode.javacv.cpp.opencv_imgproc.*;
 import static com.googlecode.javacv.cpp.opencv_highgui.*;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.util.ArrayList;
import java.util.List;
import sys.Region;

/**
 *
 * @author Lukasz
 */
public class ExtractionFilter {

    private List<Region> regions;
    private WritableRaster imageRaster;
    static int hueLowerR = 160;
    static int hueUpperR = 180;
static int counter = 0;
    public ExtractionFilter() {
      
    }

    static IplImage hsvThreshold(IplImage orgImg) {
        IplImage imgHSV = cvCreateImage(cvGetSize(orgImg), 8, 3);
        cvCvtColor(orgImg, imgHSV, CV_BGR2HSV);
        IplImage imgThreshold = cvCreateImage(cvGetSize(orgImg), 8, 1);
        IplImage imgThreshold2 = cvCreateImage(cvGetSize(orgImg), 8, 1);
        cvInRangeS(imgHSV, cvScalar(hueLowerR, 50, 50, 0), cvScalar(hueUpperR, 255, 255, 0), imgThreshold);
        cvInRangeS(imgHSV, cvScalar(0, 100, 100, 0), cvScalar(5, 255, 255, 0), imgThreshold2);
        cvAdd(imgThreshold, imgThreshold2, imgThreshold, null);
        cvReleaseImage(imgHSV);
        cvSmooth(imgThreshold, imgThreshold, CV_MEDIAN, 5);
        //cvCanny(imgThreshold, imgThreshold, 250, 120, 3);
        //cvSaveImage("D:\\test\\"+counter+".png",imgThreshold);
        //counter++;
        return imgThreshold;
    }

    public List<Rectangle> processImage(BufferedImage inImage) {
        regions = new ArrayList<Region>();
        imageRaster = hsvThreshold(IplImage.createFrom(inImage)).getBufferedImage().getRaster();
        List<Region> inRegions = new ArrayList<Region>();
        Point testPoint;
        for (int y = 0; y < inImage.getHeight(); y++) {
            for (int x = 0; x < inImage.getWidth(); x++) {

                if (imageRaster.getSample(x, y, 0) > 150) {
                    testPoint = new Point(x, y);
                    for (Region region : regions) {
                        if (region.containsPoint(testPoint)) {
                            inRegions.add(region);
                        }
                    }
                    if (inRegions.isEmpty()) {
                        regions.add(new Region(testPoint));
                    } else {
                        Region main = inRegions.get(0);
                        if (inRegions.size() > 1) {
                            for (int i = 1; i < inRegions.size(); i++) {
                                Region region = inRegions.get(i);
                                main.marge(region);
                                regions.remove(region);
                                region.Clear();
                                region = null;
                            }
                        }
                        main.addPoint(testPoint);
                        inRegions.clear();
                    }
                }
            }
        }
        List<Rectangle> result = new ArrayList<Rectangle>();
        int imageMin = Math.min(inImage.getHeight() - 1, inImage.getWidth() - 1);
        for (Region region : regions) {
            Rectangle rect = region.getArea();
            if (rect.height >= 20 && rect.width >= 20) {
                float ratio = region.getRatio();
                if (ratio >= 0.5 && ratio <= 1.5) {
                    int rectSize = Math.max(rect.width, rect.height);
                    int toAdd = (int) ((float) rectSize * 0.05f);
                    rectSize = Math.min(Math.max(rectSize + toAdd * 2, 0), imageMin);
                    int width = Math.min(Math.max(rect.x + rectSize, 0), inImage.getWidth()) - rect.x;
                    int height = Math.min(Math.max(rect.y + rectSize, 0), inImage.getHeight()) - rect.y;
                    int rectX = Math.min(Math.max(rect.x - toAdd, 0), inImage.getWidth());
                    int rectY = Math.min(Math.max(rect.y - toAdd, 0), inImage.getHeight());
                    result.add(new Rectangle(rectX, rectY, width, height));
                }
                if (ratio >= 1.6 && ratio <= 2.5) {

                    result.add(new Rectangle(rect.x, rect.y, rect.width, rect.height / 2));
                    result.add(new Rectangle(rect.x, rect.y + rect.height / 2, rect.width, rect.height / 2));
                }
            }
        }
        return result;
    }
}
