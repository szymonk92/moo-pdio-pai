/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package filters;

import com.googlecode.javacv.CanvasFrame;
import static com.googlecode.javacpp.Loader.sizeof;
import static com.googlecode.javacv.cpp.opencv_core.*;
import static com.googlecode.javacv.cpp.opencv_imgproc.*;
import static com.googlecode.javacv.cpp.opencv_core.*;
import static com.googlecode.javacv.cpp.opencv_imgproc.*;
import static com.googlecode.javacv.cpp.opencv_highgui.*;
import java.awt.Color;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import sys.Region;

/**
 *
 * @author Lukasz
 */
public class ExtractionFilter2 {

    private List<Region> regions;
    private WritableRaster imageRaster;
    static int hueLowerR = 160;
    static int hueUpperR = 180;
    static int counter = 0;
    IplImage src, src_gray = null, imgHSV = null, histeq = null, rgbeq;

    public ExtractionFilter2() {
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
        return imgThreshold;
    }

    public List<Rectangle> processImage(BufferedImage inImage) {
        List<Rectangle> result = new ArrayList<Rectangle>();
        src = IplImage.createFrom(inImage);
        rgbeq = cvCreateImage(cvSize(src.width(), src.height()), src.depth(), 3);
        src_gray = cvCreateImage(cvSize(src.width(), src.height()), IPL_DEPTH_8U, 1);
        histeq = cvCreateImage(cvSize(src.width(), src.height()), IPL_DEPTH_8U, 1);
        imgHSV = cvCreateImage(cvGetSize(src), 8, 3);

        //proba equalizacji zdjecia - ale to jest chyba - najlepiej chyba będzie po prostu histogramowo to zrobić = tak jak robiliśmy na poidzie
        //equlizacje zostaw mnie :P
        cvEqualizeHist(src_gray, histeq);
        cvCvtColor(histeq, rgbeq, CV_GRAY2BGR);
        cvAddWeighted(src, 1.5, rgbeq, -0.5, 0, rgbeq);
        cvReleaseImage(src);
        src = rgbeq;


        //konwersja do przestrzeni HSV i progowanie 
        cvCvtColor(src, imgHSV, CV_BGR2HSV);
        // 8-bit 1- color = monochrome

        IplImage imgThreshold = cvCreateImage(cvGetSize(src), 8, 1);
        IplImage imgThreshold2 = cvCreateImage(cvGetSize(src), 8, 1);
        // cvScalar : ( H , S , V, A)
        cvInRangeS(imgHSV, cvScalar(160, 50, 50, 0), cvScalar(180, 255, 255, 0), imgThreshold);
        cvInRangeS(imgHSV, cvScalar(0, 100, 100, 0), cvScalar(5, 255, 255, 0), imgThreshold2);
        cvReleaseImage(histeq);
//     cvReleaseImage(rgbeq);
        cvReleaseImage(imgHSV);
        cvAdd(imgThreshold, imgThreshold2, src_gray, null);

        IplImage sx = cvCreateImage(cvSize(src.width(), src.height()), IPL_DEPTH_16S, 1);
//	cvSobel(src_gray, sx, 1, 0, 3);


        //randomowy gaussik
        cvSmooth(src_gray, src_gray, CV_GAUSSIAN, 7, 0, 0, 0);


        //laplace robi wyostrzenie obrazka
        cvLaplace(src_gray, sx, 3);
        double scale, shift;
        double[] minVal = new double[1], maxVal = new double[1];
        cvMinMaxLoc(sx, minVal, maxVal);
        scale = 255 / (maxVal[0] - minVal[0]);
        shift = -minVal[0] * scale;
        IplImage sobel_gray = cvCreateImage(cvSize(src.width(), src.height()), IPL_DEPTH_8U, 1);
        cvConvertScale(sx, sobel_gray, scale, shift);
        cvAddWeighted(src_gray, 1.5, sobel_gray, -0.5, 0, sobel_gray);
        src_gray = sobel_gray;

        //randomowy gausik ponownie
        cvSmooth(src_gray, src_gray, CV_GAUSSIAN, 5, 0, 0, 0);





        CvMemStorage circles = cvCreateMemStorage(0);
        CvSeq seq = new CvSeq();
        cvCanny(src_gray, src_gray, 0, 255, 3);
        cvFindContours(src_gray, circles, seq, sizeof(CvContour.class), CV_RETR_EXTERNAL, CV_CHAIN_APPROX_SIMPLE);



        for (CvSeq ptr = seq; ptr != null; ptr = ptr.h_next()) {
            if (ptr.total() >= 5) {


                CvBox2D elipse = cvFitEllipse2(ptr);

                CvPoint c = cvPointFrom32f(elipse.center());
                CvSize s = new CvSize((int) elipse.size().width(), (int) elipse.size().height());
                //sprawdzamy stosunek promieni elipsy i wielkość tego co znalazło
                if (Math.abs(1.0f - (double) (elipse.size().width()) / (double) (elipse.size().height())) > 0.1
                        || s.width() * s.height() < 100) {
                    continue;
                }
                Rectangle rect = new Rectangle(c.x() - s.width() / 2, c.y() - s.height() / 2, s.width(), s.height());
                int rectSize = Math.max(rect.width, rect.height);
                int width = Math.min(Math.max(rect.x + rectSize, 0), inImage.getWidth()) - rect.x;
                int height = Math.min(Math.max(rect.y + rectSize, 0), inImage.getHeight()) - rect.y;
                int rectX = Math.min(Math.max(rect.x, 0), inImage.getWidth());
                int rectY = Math.min(Math.max(rect.y, 0), inImage.getHeight());
                rect.setBounds(rectX, rectY, width, height);
                    result.add(rect);

            }
        }
        return result;
    }
}
