/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package filters;

import static com.googlecode.javacpp.Loader.sizeof;
import static com.googlecode.javacv.cpp.opencv_core.*;
import static com.googlecode.javacv.cpp.opencv_imgproc.*;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import sys.RGBHelper;
import sys.Region;

/**
 *
 * @author Lukasz
 */
public class ExtractionFilter {

    private File tmpDir;
    private int counter;
    private int count;
    private static int magenta = RGBHelper.fastToPixel(255, 0, 255);
    private static int white = RGBHelper.fastToPixel(255, 255, 255);
    private static int medianFilterSize = 3;
    private BufferedImage inImage;
    private int inImageWidth;
    private int inImageHeight;
    private int inImageMin;
    private double regionExtraSize = 0.1;
    public BufferedImage EqualizeImage;
    public List<BufferedImage> imageRegions;

    public ExtractionFilter(File tmpDir) {
        this.tmpDir = tmpDir;
    }

    public static int[][] imageHistogram(BufferedImage input) {
        int[][] hist = new int[3][256];
        for (int i = 0; i < input.getWidth(); i++) {
            for (int j = 0; j < input.getHeight(); j++) {
                int pixel = input.getRGB(i, j);
                hist[0][(pixel >> 16) & 0xff]++;
                hist[1][(pixel >> 8) & 0xff]++;
                hist[2][pixel & 0xff]++;
            }
        }
        return hist;
    }

    private static BufferedImage histogramEqualization(BufferedImage original) {
        int[][] histLUT = histogramEqualizationLUT(original);
        BufferedImage histogramEQ = new BufferedImage(original.getWidth(), original.getHeight(), original.getType());
        for (int i = 0; i < original.getWidth(); i++) {
            for (int j = 0; j < original.getHeight(); j++) {
                int pixel = original.getRGB(i, j);
                histogramEQ.setRGB(i, j, RGBHelper.fastToPixel(histLUT[0][(pixel >> 16) & 0xff], histLUT[1][(pixel >> 8) & 0xff], histLUT[2][pixel & 0xff]));
            }
        }
        return histogramEQ;
    }

    public static int[][] histogramEqualizationLUT(BufferedImage input) {
        int[][] imageHist = imageHistogram(input);
        int[][] imageLUT = new int[3][256];

        long sumr = 0;
        long sumg = 0;
        long sumb = 0;

        float scale_factor = (float) (255.0 / (input.getWidth() * input.getHeight()));

        for (int i = 0; i < 256; i++) {
            sumr += imageHist[0][i];
            imageLUT[0][i] = (int) (sumr * scale_factor);

            sumg += imageHist[1][i];
            imageLUT[1][i] = (int) (sumg * scale_factor);

            sumb += imageHist[2][i];
            imageLUT[2][i] = (int) (sumb * scale_factor);

        }
        return imageLUT;
    }

    private BufferedImage medianFilter(BufferedImage original) {
        BufferedImage img = new BufferedImage(original.getWidth(), original.getHeight(), original.getType());
        WritableRaster originalRaster = original.getRaster();
        float threshold = (medianFilterSize * medianFilterSize) * 0.5f;
        for (int row = medianFilterSize; row < original.getHeight() - medianFilterSize - 1; row++) {
            for (int col = medianFilterSize; col < original.getWidth() - medianFilterSize - 1; col++) {
                int count2 = 0;
                for (int dRow = -medianFilterSize; dRow < medianFilterSize; dRow++) {
                    for (int dCol = -medianFilterSize; dCol < medianFilterSize; dCol++) {
                        if (originalRaster.getSample(col + dCol, row + dRow, 0) > 0) {
                            count2++;
                        }
                    }
                }
                if (count2 < threshold) {
                    img.setRGB(col, row, Color.BLACK.getRGB());
                } else {
                    img.setRGB(col, row, Color.WHITE.getRGB());
                }
            }
        }
        return img;
    }

    private BufferedImage hsvThreshold(BufferedImage orgImg) {
        
        BufferedImage mask = new BufferedImage(this.inImageWidth, this.inImageHeight, BufferedImage.TYPE_BYTE_GRAY);
        for (int y = 0; y < this.inImageHeight; y++) {
            for (int x = 0; x < this.inImageWidth; x++) {
                if (hsvPixel(this.EqualizeImage.getRGB(x, y))) {
                    mask.setRGB(x, y, white);
                }
            }
        }
        if (tmpDir != null) {
            try {
                ImageIO.write(mask, "png", new File(tmpDir + "/image" + counter + "_mask.png"));
            } catch (IOException ex) {
                Logger.getLogger(ExtractionFilter.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        BufferedImage result = medianFilter(mask);
        if (tmpDir != null) {
            try {
                ImageIO.write(result, "png", new File(tmpDir + "/image" + counter + "_filter.png"));
            } catch (IOException ex) {
                Logger.getLogger(ExtractionFilter.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return result;
    }

    private List<Region> produceRegions(BufferedImage input){
        WritableRaster imageRaster = input.getRaster();
        List<Region> regions = new ArrayList<Region>();
        List<Region> inRegions = new ArrayList<Region>();
        Point testPoint;
        for (int y = 0; y < this.inImageHeight; y++) {
            for (int x = 0; x < this.inImageWidth; x++) {

                if (imageRaster.getSample(x, y, 0) > 0) {
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
                            }
                        }
                        main.addPoint(testPoint);
                        inRegions.clear();
                    }
                }
            }
        }
        return regions;
    }
    private List<Rectangle> produceImageRegions(List<Region> regions){
         List<Rectangle> result = new ArrayList<Rectangle>();
        for (Region region : regions) {
            Rectangle rect = region.getArea();
            if (rect.height >= 20 && rect.width >= 20) {
                float ratio = region.getRatio();
                if (ratio >= 1.7 && ratio <= 2.5) {
                    result.add(getBiggerRegion(rect.x, rect.y, rect.width, rect.height / 2));
                    result.add(getBiggerRegion(rect.x, rect.y + rect.height / 2, rect.width, rect.height / 2));
                } else if (ratio >= 2.3 && ratio <= 3.7) {
                    result.add(getBiggerRegion(rect.x, rect.y, rect.width, rect.height / 3));
                    result.add(getBiggerRegion(rect.x, rect.y + rect.height * 1 / 3, rect.width, rect.height / 3));
                    result.add(getBiggerRegion(rect.x, rect.y + rect.height * 2 / 3, rect.width, rect.height / 3));
                } else {
                    result.add(getBiggerRegion(rect.x, rect.y, rect.width, rect.height));
                }
            }
        }
        return result;
    }
    private List<Rectangle> filterRegionByElipsse(List<Rectangle> regions, BufferedImage mask){
        List<Rectangle> result = new ArrayList<Rectangle>();
        IplImage src, src_gray;

        src_gray = IplImage.createFrom(mask);
        cvCanny(src_gray, src_gray, 0, 255, 3);
        BufferedImage gray = src_gray.getBufferedImage();
        BufferedImage copyEqualizeImage = copy(this.EqualizeImage);
        new File(tmpDir + "/result").mkdir();
        for (Rectangle rectangle : regions) {
            new File(tmpDir + "/" + counter).mkdir();
            BufferedImage sub = gray.getSubimage(rectangle.x, rectangle.y, rectangle.width, rectangle.height);
            BufferedImage sub2 = copyEqualizeImage.getSubimage(rectangle.x, rectangle.y, rectangle.width, rectangle.height);
            Graphics g = sub2.getGraphics();
            count++;
            src = IplImage.createFrom(sub);
            CvMemStorage circles = cvCreateMemStorage(0);
            CvSeq seq = new CvSeq();
            cvFindContours(src, circles, seq, sizeof(CvContour.class), CV_RETR_EXTERNAL, CV_CHAIN_APPROX_SIMPLE);
            if (seq != null) {
                int count2 = 0;
                for (CvSeq ptr = seq; ptr != null; ptr = ptr.h_next()) {
                    if (ptr.total() >= 5) {
                        CvBox2D elipse = cvFitEllipse2(ptr);
                        CvPoint c = cvPointFrom32f(elipse.center());
                        CvSize s = new CvSize((int) elipse.size().width(), (int) elipse.size().height());
                        if (Math.abs(1.0f - (double) (elipse.size().width()) / (double) (elipse.size().height())) > 0.2 || elipse.size().width() * elipse.size().height() < 300) {
                            g.setColor(Color.YELLOW);
                            g.drawOval(c.x() - s.width() / 2, c.y() - s.height() / 2, s.width(), s.height());
                            continue;
                        }
                        g.setColor(Color.blue);
                        g.drawOval(c.x() - s.width() / 2, c.y() - s.height() / 2, s.width(), s.height());
                        Rectangle resultSmallRect = getOptimalRegion(rectangle.x + c.x() - s.width() / 2, rectangle.y + c.y() - s.height() / 2, s.width(), s.height());
                        Rectangle resultRect = getBiggerRegion(rectangle.x + c.x() - s.width() / 2, rectangle.y + c.y() - s.height() / 2, s.width(), s.height());
                        boolean test = false;
                        for(Rectangle rect : result){
                            if(isTheSame(rect, resultRect)){
                                test = true;
                                break;
                            }         
                        }
                        if(test){
                            continue;
                        }
                        result.add(resultRect);
                        BufferedImage imageRegion = produceImageRegion(new Ellipse2D.Double(rectangle.x + c.x() - s.width() / 2, rectangle.y + c.y() - s.height() / 2, s.width(), s.height()), resultSmallRect);
                        if (tmpDir != null) {
                            try {
                                ImageIO.write(imageRegion, "png", new File(tmpDir + "/" + counter + "/image" + counter + "_" + count + "_" + count2 + "_imageRegion.png"));
                                ImageIO.write(imageRegion, "png", new File(tmpDir + "/result/image" + counter + "_" + count + "_" + count2 + "_imageRegion.png"));
                            } catch (IOException ex) {
                                Logger.getLogger(ExtractionFilter.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                        imageRegions.add(imageRegion);
                        count2++;

                    }
                }
            }
            if (tmpDir != null) {
                try {
                    ImageIO.write(sub, "png", new File(tmpDir + "/" + counter + "/image" + counter + "_" + count + "_sub.png"));
                } catch (IOException ex) {
                    Logger.getLogger(ExtractionFilter.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (tmpDir != null) {
                try {
                    ImageIO.write(sub2, "png", new File(tmpDir + "/" + counter + "/image" + counter + "_" + count + "_subColor.png"));
                } catch (IOException ex) {
                    Logger.getLogger(ExtractionFilter.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return result;
    }
    
    public static boolean isTheSame(Rectangle first, Rectangle second) {
        if (first.intersects(second)) {
            double firstArea = first.height * first.width;
            double secondArea = second.height * second.width;
            double maxArea = Math.max(firstArea, secondArea);
            double minArea = Math.min(firstArea, secondArea);
            double result = minArea/maxArea;
            if(result>0.5 &&result<1.3){
                Rectangle rect = first.intersection(second);
                double rectArea = rect.height * rect.width;
                double distance = Point.distance(first.getCenterX(), first.getCenterY(), second.getCenterX(), second.getCenterY());
                double  maxRectArea = Math.max(rectArea/firstArea, rectArea/secondArea);
                if (distance<first.height/0.5 && maxRectArea>0.6) {
                    return true;
                }
            }
        }
        return false;
    }
    
    public List<Rectangle> processImage(BufferedImage input) {
        this.inImage = input;
        this.inImageWidth = input.getWidth();
        this.inImageHeight = input.getHeight();
        this.inImageMin = Math.min(this.inImageHeight - 1, this.inImageWidth - 1);
        this.imageRegions = new ArrayList<BufferedImage>();
        this.EqualizeImage = this.inImage;
        this.count = 0;
        BufferedImage mask = hsvThreshold(this.EqualizeImage);
        List<Region> regions = produceRegions(mask);
        List<Rectangle> imageRegionsTmp = produceImageRegions(regions);
        List<Rectangle> result = filterRegionByElipsse(imageRegionsTmp, mask);
        this.EqualizeImage = histogramEqualization(this.inImage);
        if (tmpDir != null) {
            try {
                ImageIO.write(this.EqualizeImage, "png", new File(tmpDir + "/image" + counter + "_equalized.png"));
            } catch (IOException ex) {
                Logger.getLogger(ExtractionFilter.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        mask = hsvThreshold(this.EqualizeImage);
        regions = produceRegions(mask);
        imageRegionsTmp = produceImageRegions(regions);
        result.addAll(filterRegionByElipsse(imageRegionsTmp, mask));
        this.EqualizeImage = this.inImage;
        
        counter++;
        return result;
    }

    private Rectangle getBiggerRegion(int x, int y, int width, int height) {
        int rectSize = Math.max(width, height);
        int toAdd = (int) ((float) rectSize * regionExtraSize);
        rectSize = Math.min(Math.max(rectSize + toAdd * 2, 0), inImageMin);
        int rectWidth = Math.min(Math.max(x + rectSize, 0), this.inImageWidth) - x;
        int rectHeight = Math.min(Math.max(y + rectSize, 0), this.inImageHeight) - y;
        int rectX = Math.min(Math.max(x - toAdd, 0), this.inImageWidth);
        int rectY = Math.min(Math.max(y - toAdd, 0), this.inImageHeight);
        return new Rectangle(rectX, rectY, rectWidth, rectHeight);
    }

    private Rectangle getOptimalRegion(int x, int y, int width, int height) {
        int rectWidth = Math.min(Math.max(x + width, 0), this.inImageWidth) - x;
        int rectHeight = Math.min(Math.max(y + height, 0), this.inImageHeight) - y;
        int rectX = Math.min(Math.max(x, 0), this.inImageWidth);
        int rectY = Math.min(Math.max(y, 0), this.inImageHeight);
        return new Rectangle(rectX, rectY, rectWidth, rectHeight);
    }

    private boolean hsvPixel(int pixel) {
        float[] hsv = Color.RGBtoHSB((pixel >> 16) & 0xff, (pixel >> 8) & 0xff, (pixel) & 0xff, null);
        if (hsv[1] > 0.40) {
            if (hsv[2] > 0.35) {
                float hMaxDif = hsv[2] * 0.10f;
                float hMinDif = hsv[2] * 0.06f;
                if (hsv[2] < 0.6f) {
                    hMaxDif += 0.06f;
                    hMinDif += 0.01f;
                }
                return (hsv[0] > 1 - hMaxDif || hsv[0] < hMinDif);
            }
        }
        return false;
    }

    private BufferedImage produceImageRegion(Ellipse2D.Double elipse, Rectangle resultRect) {
        BufferedImage result = new BufferedImage(resultRect.width, resultRect.height, BufferedImage.TYPE_3BYTE_BGR);
        for (int y = 0; y < resultRect.height; y++) {
            for (int x = 0; x < resultRect.width; x++) {
                if (elipse.contains(x + resultRect.x, y + resultRect.y)) {
                    result.setRGB(x, y, this.EqualizeImage.getRGB(x + resultRect.x, y + resultRect.y));//colorReplacer(x + resultRect.x, y + resultRect.y));
                } else {
                    result.setRGB(x, y, magenta);
                }
            }
        }
        return result;
    }

    private int colorReplacer(int x, int y) {
        int pixel = this.EqualizeImage.getRGB(x, y);
        float[] hsv = Color.RGBtoHSB((pixel >> 16) & 0xff, (pixel >> 8) & 0xff, (pixel) & 0xff, null);
        if (hsv[2] < 0.3) {
            return Color.BLACK.getRGB();
        }
        if (hsv[1] < 0.15) {
            return Color.WHITE.getRGB();
        }
        if ((hsv[0] > 0.52 && hsv[0] < 0.77) ) {
            return Color.blue.getRGB();
        }
        if (hsvPixel(pixel)) {
            return Color.RED.getRGB();
        }
        return this.EqualizeImage.getRGB(x, y);
    }

    public static BufferedImage copy(BufferedImage bi) {
        ColorModel cm = bi.getColorModel();
        boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
        WritableRaster raster = bi.copyData(null);
        return new BufferedImage(cm, raster, isAlphaPremultiplied, null);
    }
}
