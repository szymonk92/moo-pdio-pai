import java.awt.Color;
import java.nio.ByteBuffer;
import java.util.Random;
import java.util.Vector;

import javax.swing.JFrame;
import com.googlecode.javacpp.Pointer;
import com.googlecode.javacv.*;
import com.googlecode.javacv.cpp.opencv_core.CvMemStorage;
import com.googlecode.javacv.cpp.opencv_core.CvPoint;
import com.googlecode.javacv.cpp.opencv_core.IplImage;
import com.googlecode.javacv.cpp.opencv_ml.CvVectors;

import static com.googlecode.javacpp.Loader.sizeof;
import static com.googlecode.javacv.cpp.opencv_core.*;
import static com.googlecode.javacv.cpp.opencv_imgproc.*;
import static com.googlecode.javacv.cpp.opencv_highgui.*;


public class cvCirclesFind {
	
	public static void rgb_normalize(IplImage img) {
		
		double[][] adj = new double[256][3];
		
		IplImage hsv = cvCreateImage(cvGetSize(img), 8, 3);
		 int[] hist_size = {256};     
		 float range[]={0,256};
		 float[][] ranges = { range };
		 double min[]= new double[3], max[]= new double[3];
		 
		 
		 
		cvCvtColor(img, hsv, CV_BGR2HSV);
//		 cvCopy(img, hsv);
		
		
		//ponoć to powinno lepiej działać, ale w hsv robi sieczke
//		IplImage[] channel ={ cvCreateImage( cvGetSize(img), 8, 1 )};
//		CvHistogram hist;
//		
//		int dim = img.width()*img.height();
//		long lo=(long) (0.02*((double)dim)), fi=(long) (0.98*((double)dim));
//		System.out.println("low"+lo+"high"+fi);
//		for( int i=0; i<3;++i) {
//			 hist=cvCreateHist(1, hist_size, CV_HIST_ARRAY, ranges, 1);
//			 //pobierz histogram
//			 cvSetImageCOI(img,i+1);
//			 cvCopy(img,channel[0]);
//			 
//			 cvResetImageROI(img);
//			 cvCalcHist( channel, hist, 0, null );
//			 
//			 min[i]=255.0;max[i]=0.0;
//			 long hi=0;
//			 for( int h=0; h<256; ++h) {
//				 hi+=(long) cvGetReal1D(hist.bins(),h);
//				 if ( hi<lo) {
//					 min[i]=h;
//				 } else if ( hi<fi){
//					max[i]=h; 
//				 }
//			 }
//			 System.out.println(hi+"="+dim);
//			 			 
//		}
		
		
		
		min[0]=min[1]=min[2]=255.0;
		max[0]=max[1]=max[2]=0.0;
		for(int y=0; y<img.height(); y++){
            for(int x=0; x<img.width(); x++){
            	 CvScalar rgb =  cvGet2D(hsv, y, x);
            	for( int j=0; j<3; ++j) {
            		min[j]=Math.min(min[j],rgb.getVal(j));
            		max[j]=Math.max(max[j],rgb.getVal(j));
            	}
            }
		}
		System.out.println(min[0]+" "+max[0]+"|"+min[1]+" "+max[1]+"|"+min[2]+" "+max[2]);
		
		for( int i=0; i<256; ++i) {
			for ( int j=0; j<3; ++j) {
				adj[i][j]=i;
				adj[i][j]=(adj[i][j]-min[j])*((255-0)/(max[j]-min[j]))+0;
			}
//			System.out.println(i+":"+adj[i][0]);
		}
		
		//PROPAGACJA equalizacji histogramu jasności - dupa blada
//		IplImage gray = cvCreateImage( cvSize( img.width(), img.height() ), IPL_DEPTH_8U, 1 ),
//		histeq = cvCreateImage( cvSize( img.width(), img.height() ), IPL_DEPTH_8U, 1 );
//		cvCvtColor(img, gray, CV_RGB2GRAY);
//		cvEqualizeHist( gray, histeq );
		
		
		for(int y=0; y<img.height(); y++){
            for(int x=0; x<img.width(); x++){
                   CvScalar rgb =  cvGet2D(hsv, y, x);
//                   CvScalar o = cvGet2D(gray, y, x);
//                   CvScalar e = cvGet2D(histeq, y, x);
//                   double ratio = e.getVal(0)/o.getVal(0);
                   
                   double r = rgb.red(), g=rgb.green(), b=rgb.blue();
                   double rratio = r/(g+b);                    
                   //propagowanie equalizehist
//                   if ( rratio > 1.0 ) {
//                	   r= r + r*rratio;
//                	   g= g - g*rratio;
//                	   b= b - b*rratio;
//                   } 
//                   rgb.red(r*ratio);
//                   rgb.green(g*ratio);
//                   rgb.blue(b*ratio);
                 rgb.red(adj[(int)r][0]);
                 rgb.green(adj[(int)g][1]);
                 rgb.blue(adj[(int)b][2]);
                   cvSet2D(hsv, y, x, rgb);
            }
		}	
//		cvReleaseImage(gray);
//		cvReleaseImage(histeq);
		cvCvtColor(hsv, img, CV_HSV2BGR);
//		cvCopy(hsv, img);
		cvReleaseImage(hsv);
		
	}
	
public static void main(String[] args) {
	
	String fileName = "/mnt/free/studia/OI/Workspace/0901-0930/image0909.png";//"Hough_Circle.png";
	
	
	
	IplImage src, src_gray=null,imgHSV=null, histeq=null
			, rgbeq ;
	
	src = cvLoadImage(fileName);
	src_gray = cvCreateImage( cvSize( src.width(), src.height() ), IPL_DEPTH_8U, 1 );
	imgHSV = cvCreateImage(cvGetSize(src), 8, 3);
	
	//proba equalizacji zdjecia - ale to jest chyba - najlepiej chyba będzie po prostu histogramowo to zrobić = tak jak robiliśmy na poidzie
	//equlizacje zostaw mnie :P
//	rgbeq =cvCreateImage( cvSize( src.width(), src.height() ), src.depth(), 3 );
//	cvEqualizeHist( src_gray, histeq );
//	cvCvtColor(histeq, rgbeq, 	CV_GRAY2BGR);
//	cvAddWeighted(src, 1.5, rgbeq, -0.5, 0, rgbeq);
//	cvReleaseImage(src);
//	src = rgbeq;
	CanvasFrame cir = new CanvasFrame("Circles");
	cir.setCanvasScale(0.4);
	cir.showImage(src);
	rgb_normalize(src);
	
	
	
	
	//konwersja do przestrzeni HSV i progowanie 
	 cvCvtColor(src, imgHSV, CV_BGR2HSV);
     // 8-bit 1- color = monochrome

     IplImage imgThreshold = cvCreateImage(cvGetSize(src), 8, 1);
      IplImage imgThreshold2 = cvCreateImage(cvGetSize(src), 8, 1);
     // cvScalar : ( H , S , V, A)
     cvInRangeS(imgHSV, cvScalar(160, 50, 50, 0), cvScalar(180, 255, 255, 0), imgThreshold);
     cvInRangeS(imgHSV, cvScalar(0, 100, 100, 0), cvScalar(5, 255, 255, 0), imgThreshold2);
     cvReleaseImage(histeq);
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
