import java.awt.Color;
import java.util.Random;
import java.util.Vector;

import javax.swing.JFrame;
import com.googlecode.javacpp.Pointer;
import com.googlecode.javacv.*;
import com.googlecode.javacv.cpp.opencv_core.CvMemStorage;
import com.googlecode.javacv.cpp.opencv_core.CvPoint;
import com.googlecode.javacv.cpp.opencv_core.IplImage;

import static com.googlecode.javacpp.Loader.sizeof;
import static com.googlecode.javacv.cpp.opencv_core.*;
import static com.googlecode.javacv.cpp.opencv_imgproc.*;
import static com.googlecode.javacv.cpp.opencv_highgui.*;


public class cvCirclesFind {
public static void main(String[] args) {
	
	String fileName = "image0125.png";//"Hough_Circle.png";
	
	
	
	IplImage src, src_gray=null,imgHSV=null, histeq=null
			, rgbeq ;
	
	src = cvLoadImage(fileName);
	rgbeq =cvCreateImage( cvSize( src.width(), src.height() ), src.depth(), 3 );
	src_gray = cvCreateImage( cvSize( src.width(), src.height() ), IPL_DEPTH_8U, 1 );
	histeq = cvCreateImage( cvSize( src.width(), src.height() ), IPL_DEPTH_8U, 1 );
	imgHSV = cvCreateImage(cvGetSize(src), 8, 3);
	
	//proba equalizacji zdjecia - ale to jest chyba - najlepiej chyba będzie po prostu histogramowo to zrobić = tak jak robiliśmy na poidzie
	//equlizacje zostaw mnie :P
	cvEqualizeHist( src_gray, histeq );
	cvCvtColor(histeq, rgbeq, 	CV_GRAY2BGR);
	CanvasFrame cir = new CanvasFrame("Circles");
	cir.setCanvasScale(0.4);
	cvAddWeighted(src, 1.5, rgbeq, -0.5, 0, rgbeq);
	cir.showImage(rgbeq);
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
	
	IplImage sx= cvCreateImage( cvSize( src.width(), src.height() ) , IPL_DEPTH_16S, 1 );
//	cvSobel(src_gray, sx, 1, 0, 3);
	
	
	//randomowy gaussik
	cvSmooth(src_gray,src_gray,CV_GAUSSIAN,7,0,0,0);
	
	
	//laplace robi wyostrzenie obrazka
	cvLaplace(src_gray, sx, 3);
	double scale, shift;
    double[] minVal= new double[1] , maxVal= new double[1];
	cvMinMaxLoc(sx,  minVal, maxVal); 
    scale = 255/(maxVal[0] - minVal[0]);
    shift = -minVal[0]*scale;
    IplImage sobel_gray = cvCreateImage( cvSize( src.width(), src.height() ) , IPL_DEPTH_8U, 1 ); 
    cvConvertScale (sx, sobel_gray, scale, shift);
    cvAddWeighted(src_gray, 1.5, sobel_gray, -0.5, 0, sobel_gray);
    src_gray = sobel_gray;
    
    //randomowy gausik ponownie
    cvSmooth(src_gray,src_gray,CV_GAUSSIAN,5,0,0,0);
    
	

	
	
	CvMemStorage circles = cvCreateMemStorage(0);
	CvSeq seq = new CvSeq();
	cvCanny(src_gray, src_gray, 0, 255, 3);
	cvFindContours(src_gray, circles, seq,sizeof(CvContour.class), CV_RETR_EXTERNAL, CV_CHAIN_APPROX_SIMPLE);
	Random rand = new Random();
	
	
	
	for ( CvSeq ptr = seq; ptr != null; ptr = ptr.h_next()) {
		Color randC = new Color(rand.nextFloat(), rand.nextFloat(), rand.nextFloat());
		CvScalar color = CV_RGB(randC.getRed(), randC.getGreen(), randC.getBlue());
		if ( ptr.total() >= 5) {
			
			
		CvBox2D elipse = cvFitEllipse2(ptr);
		
		CvPoint c = cvPointFrom32f(elipse.center());
		CvSize s = new CvSize((int)elipse.size().width(), (int)elipse.size().height());
		//sprawdzamy stosunek promieni elipsy i wielkość tego co znalazło
		if ( Math.abs( 1.0f- (double)(elipse.size().width())/(double)(elipse.size().height())) > 0.1 ||
				s.width()*s.height() < 100) {
			continue;
		}
		
		
		CvPoint pt1 = new CvPoint(c.x()+s.width()/2,c.y()+s.height()/2), 
				pt2 = new CvPoint(c.x()-s.width()/2,c.y()-s.height()/2);
		//rysujemy prostokąta
		cvDrawRect(src, pt1, pt2, color, -1, 8, 0);
		//pobieranie całego konturu - to jest po prostu lista-vector = nie wiem jaki kontener leży na dole CvSeq :)
//		for ( int i=0; i<ptr.total(); ++i) {
//			Pointer point = cvGetSeqElem(ptr, i);
//			CvPoint p = new CvPoint(point);
//			System.out.print( "(" + p.x() + " " + p.y() + ")\t " );
//			
//		}
//		System.out.println();
//		cvDrawContours(src, ptr, color, CV_RGB(0,0,0), -1, CV_FILLED,8 , cvPoint(0, 0));
		}
	}

//	CvSeq seq = cvHoughCircles( src_gray, circles, CV_HOUGH_GRADIENT,1, 50,250, 100, 10, 400 );
	
	
//	for(int i=0; i<seq.total(); i++){
//
//        
//		Pointer circ = cvGetSeqElem(seq, i);
//		CvPoint3D32f p = new CvPoint3D32f(circ);
//
//		double[] xyr =p.get();
//		CvPoint center = new CvPoint((int)xyr[0],(int)xyr[1]);
//        
//        int radius = (int) Math.round(xyr[2]);
//        cvCircle(src_gray, center, 3, CvScalar.BLUE, -1, 8, 0);
//        cvCircle(src_gray, center, radius, CvScalar.RED, 1, 8, 0);
//	}
	
	cir = new CanvasFrame("Circles");
	cir.setCanvasScale(0.4);
	cir.showImage(src);
	cir.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
}
}
