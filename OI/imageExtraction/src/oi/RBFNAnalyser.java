/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package oi;

import static com.googlecode.javacv.cpp.opencv_core.*;
import static com.googlecode.javacv.cpp.opencv_imgproc.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import weka.classifiers.Classifier;
import weka.core.*;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.Remove;

/**
 *
 * @author Lukasz
 */
public class RBFNAnalyser implements Analyser {
    private BufferedImage image;
    private int imageWidth;
    private int imageHeight;
    
    private static Point[] directions = new Point[]{
        new Point(0, -1),
        new Point(1, -1),
        new Point(1, 0),
        new Point(1, 1)
    };

    private static IplImage hsvThreshold(IplImage orgImg) {

        IplImage imgHSV = cvCreateImage(cvGetSize(orgImg), 8, 3);
        cvCvtColor(orgImg, imgHSV, CV_BGR2HSV);

        IplImage imgThreshold = cvCreateImage(cvGetSize(orgImg), 8, 1);
        IplImage imgThreshold2 = cvCreateImage(cvGetSize(orgImg), 8, 1);
        cvInRangeS(imgHSV, cvScalar(160, 50, 50, 0), cvScalar(180, 255, 255, 0), imgThreshold);
        cvInRangeS(imgHSV, cvScalar(0, 60, 100, 0), cvScalar(6, 255, 255, 0), imgThreshold2);
        cvAdd(imgThreshold, imgThreshold2, imgThreshold, null);

        cvReleaseImage(imgHSV);
        cvSmooth(imgThreshold, imgThreshold, CV_MEDIAN, 5);
        return imgThreshold;
    }

    private List<Rectangle> getRegions() {
        List<Region> regions = new ArrayList<Region>();
        WritableRaster imageRaster = hsvThreshold(IplImage.createFrom(image)).getBufferedImage().getRaster();
        List<Region> inRegions = new ArrayList<Region>();
        Point testPoint;
        for (int y = 0; y < imageHeight; y++) {
            for (int x = 0; x < imageWidth; x++) {

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
                            }
                        }
                        main.addPoint(testPoint);
                        inRegions.clear();
                    }
                }
            }
        }
        List<Rectangle> result = new ArrayList<Rectangle>();
        int imageMin = Math.min(imageHeight - 1, imageWidth - 1);
        for (Region region : regions) {
            Rectangle rect = region.getArea();
            if (rect.height >= 20 && rect.width >= 20) {
                float ratio = region.getRatio();
                if (ratio >= 0.5 && ratio <= 1.5) {
                    int rectSize = Math.max(rect.width, rect.height);
                    int toAdd = (int) ((float) rectSize * 0.05f);
                    rectSize = Math.min(Math.max(rectSize + toAdd * 2, 0), imageMin);
                    int width = Math.min(Math.max(rect.x + rectSize, 0), imageWidth) - rect.x;
                    int height = Math.min(Math.max(rect.y + rectSize, 0), imageHeight) - rect.y;
                    int rectX = Math.min(Math.max(rect.x - toAdd, 0), imageWidth);
                    int rectY = Math.min(Math.max(rect.y - toAdd, 0), imageHeight);
                    result.add(new Rectangle(rectX, rectY, width, height));
                }
                if (ratio >= 1.7 && ratio <= 2.3) {

                    result.add(new Rectangle(rect.x, rect.y, rect.width, rect.height / 2));
                    result.add(new Rectangle(rect.x, rect.y + rect.height / 2, rect.width, rect.height / 2));
                }
                if (ratio >= 2.7 && ratio <= 3.3) {

                    result.add(new Rectangle(rect.x, rect.y, rect.width, rect.height / 3));
                    result.add(new Rectangle(rect.x, rect.y + rect.height * 1 / 3, rect.width, rect.height / 3));
                    result.add(new Rectangle(rect.x, rect.y + rect.height * 2 / 3, rect.width, rect.height / 3));
                }
            }
        }
        return result;
    }

    private List<BufferedImage> getSubImagesFit(List<Rectangle> rectangles) {
        List<BufferedImage> result = new ArrayList<BufferedImage>();
        if (image != null) {
            for (Rectangle rect : rectangles) {
                result.add(resize(image.getSubimage(rect.x, rect.y, rect.width, rect.height), 30, 30));
            }
        }
        return result;
    }

    private static BufferedImage resize(BufferedImage image, int width,
            int height) {
        int type = image.getType() == 0 ? BufferedImage.TYPE_INT_ARGB : image.getType();
        BufferedImage resizedImage = new BufferedImage(width, height, type);
        Graphics2D g = resizedImage.createGraphics();
        g.setComposite(AlphaComposite.Src);
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g.setRenderingHint(RenderingHints.KEY_RENDERING,
                RenderingHints.VALUE_RENDER_QUALITY);
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        g.drawImage(image, 0, 0, width, height, null);
        g.dispose();
        return resizedImage;
    }

    private List<Rectangle> classify(List<Rectangle> regions) {
        List<Rectangle> found = new ArrayList<Rectangle>();
        try {
            Classifier classify = (Classifier) SerializationHelper.read(RBFNAnalyser.class.getResource("rbfn.model").getPath());
            ObjectInputStream oi = new ObjectInputStream(new BufferedInputStream(new FileInputStream(RBFNAnalyser.class.getResource("rbfn.features").getPath())));
            int[]  features = (int[]) oi.readObject();
            oi.close();
            Instances testData = createInstances(getSubImagesFit(regions), features);
            if (testData != null) {
                try {
                    for (int i = 0; i < testData.numInstances(); i++) {
                        double clsLabel = classify.classifyInstance(testData.instance(i));
                        if (clsLabel != 2) {
                            found.add(regions.get(i));
                        }
                    }
                } catch (Exception ex) {
                    Logger.getLogger(MLPAnalyser.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        } catch (FileNotFoundException e) {
            Logger.getLogger(MLPAnalyser.class.getName()).log(Level.SEVERE, null, e);
        } catch (IOException e) {
            Logger.getLogger(MLPAnalyser.class.getName()).log(Level.SEVERE, null, e);
        } catch (ClassNotFoundException e) {
            Logger.getLogger(MLPAnalyser.class.getName()).log(Level.SEVERE, null, e);
        } catch (Exception e) {
            Logger.getLogger(MLPAnalyser.class.getName()).log(Level.SEVERE, null, e);
        }

        return found;
    }

    public static Instances createInstances(List<BufferedImage> images, int[] features) {
        if (images == null || images.isEmpty()) {
            return null;
        }
        Instances ret;
        FastVector fvClassVal = new FastVector(3);
        fvClassVal.addElement("positive0");
        fvClassVal.addElement("positive1");
        fvClassVal.addElement("negative");
        Attribute ClassAttribute = new Attribute("theClass", fvClassVal);


        BufferedImage img = images.get(0);
        FastVector vec = new FastVector(img.getHeight() * img.getWidth() + 1);
        for (int i = 0; i < img.getHeight() * img.getWidth(); ++i) {
            vec.addElement(new Attribute("attr" + i));
        }

        vec.addElement(ClassAttribute);

        ret = new Instances("data", vec, 0);

        for (int i = 0; i < images.size(); ++i) {
            img = images.get(i);
            Instance ince = new Instance(img.getHeight() * img.getWidth() + 1);

            int val = 0;
            for (int j = 0; j < img.getWidth(); ++j) {
                for (int k = 0; k < img.getHeight(); ++k) {
                    ince.setValue((Attribute) vec.elementAt(val), (double) img.getRGB(j, k));
                    val++;
                }
            }
            ret.add(ince);
        }


        ret.setClassIndex(ret.numAttributes() - 1);
        return attributeSelection(ret, features);
    }

    public static Instances attributeSelection(Instances data, int[] features) {
        features[features.length - 1] = data.numAttributes() - 1;
        Remove rm = new Remove();
        rm.setAttributeIndicesArray(features);
        rm.setInvertSelection(true);
        try {
            rm.setInputFormat(data);
            return Filter.useFilter(data, rm);
        } catch (Exception ex) {
            Logger.getLogger(MLPAnalyser.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;

    }

    private List<BufferedImage> produceMask(List<Rectangle> found) {
        List<BufferedImage> result = new ArrayList<BufferedImage>();
        if (found != null) {
            BufferedImage mask;
            for (Rectangle rect : found) {
                mask = new BufferedImage(imageWidth, imageHeight, BufferedImage.TYPE_BYTE_BINARY);
                Graphics g = mask.getGraphics();
                g.setColor(Color.BLACK);
                g.fillRect(0, 0, imageWidth, imageHeight);
                g.setColor(Color.WHITE);
                g.fillRect(rect.x, rect.y, rect.width, rect.height);
                result.add(mask);
            }
        }
        return result;

    }

    @Override
    public List<BufferedImage> analyse(BufferedImage image) {
        this.image = image;
        this.imageWidth = image.getWidth();
        this.imageHeight = image.getHeight();
        return produceMask(classify(getRegions()));
    }

    public class Region {

        public List<Point> pixels;
        public Rectangle area;
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
}
