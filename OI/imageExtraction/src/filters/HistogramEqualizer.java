/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package filters;

import java.awt.image.BufferedImage;

/**
 *
 * @author Lukasz
 */
public class HistogramEqualizer {
    private static BufferedImage inputImage; //image object of input image
    private static BufferedImage newImage; //image after histogram equalization
    private static BufferedImage greyImage; //grey image of input image

    private short[][] red; // array of red which is extracted from the image data
    private short[][] green; // array of green which is extracted from the image data
    private short[][] blue; // array of blue which is extracted from the image data

    private int[] inputImageData; // data array of input image
    private short[][] greyImageData; // data array of the image after being greyed
    private short[][] equalizedImageData; // data array after histogram equalization
    
    private double[] oldHistogram = new double[256]; // normalized histogram of grey image
    private double[] newHistogram = new double[256]; // normalized histogram of equalized image
    private double[] equalizedHistogram = new double[256]; // equalized histogram of equalized image

    private int rows; // image height
    private int cols; // image width

    /** 
     * Get RGB inputImageData from image and change them into
     * three color arrays, red, green, and blue.
     */
    public void loadRGBArrays() {
        // check if we need to resize the component arrays, i.e.,
        // has the size of the image changed?
        if (inputImage.getHeight() != rows || inputImage.getWidth() != cols) {
            rows = inputImage.getHeight();
            cols = inputImage.getWidth();
            
            red   = new short[rows][cols];
            green = new short[rows][cols];
            blue  = new short[rows][cols];
        }

        // get pixels as ints of the form 0xRRGGBB
        inputImageData = inputImage.getRGB(0, 0, inputImage.getWidth(), 
                                           inputImage.getHeight(), null, 0, 
                                           inputImage.getWidth());

        // extract red, green, and blue components from each pixel
        int index;
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                index = (row * cols) + col;
                unpackPixel(inputImageData[index], red, green, blue, row, col);
            }
        }
    }

    /** Make out a grey image "greyImageData" from original image. */
    public void makeGreyImage() {
        greyImageData = new short[rows][cols];

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                // grey value is average of red, green, and blue components, i.e.,
                // grey = (red + green + blue) / 3
//                greyImageData[row][col] = (short)((  red[row][col]
//						   + green[row][col]
//						   + blue[row][col]) / 3);
              greyImageData[row][col] = (short)(red[row][col]*0.299+green[row][col]*0.587+blue[row][col]*0.144);
              if(greyImageData[row][col]>255)
              	greyImageData[row][col] = 255;
            }
        }	

        greyImage = greyToBufferedImage(greyImageData);
    }

    /** 
     * This method can convert array of short inputImageDatatype into a BufferedImage.
     * After being converted into type of BufferedImage, the image can be showed on
     * GUI.
     * @return a object of BufferedImage which is coverted from the input grey image data array
     * @param inputGreyImageData The short[][] of grey image data
     */
    public BufferedImage greyToBufferedImage(short[][] inputGreyImageData) {
        int[] greyBufferedImageData = new int[rows * cols];
        int index;
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                index = (row * cols) + col;
                greyBufferedImageData[index] = packPixel(inputGreyImageData[row][col],
                                                         inputGreyImageData[row][col],
                                                         inputGreyImageData[row][col]);
            }
        }

        BufferedImage greyImage = new BufferedImage(cols, rows, BufferedImage.TYPE_INT_RGB);
        greyImage.setRGB(0, 0, cols, rows, greyBufferedImageData, 0, cols);

        return greyImage;
    }

    // performEqualization takes the greyImageData and creates
    // the old and new histograms and the new image
    public void performEqualization() {
	createHistograms(greyImageData, oldHistogram, equalizedHistogram);
        equalizedImageData = equalizeImage(greyImageData, equalizedHistogram);
        createHistograms(equalizedImageData, newHistogram, null);
        newImage = greyToBufferedImage(equalizedImageData);
    }

    /** 
     * Return the image data after histogram-equalization.
     * This method applies the algorithm of histogram-equalization
     * on the input image data.
     * @return the image data histogram-equalized
     * @param inputGreyImageData input grey image data need to be histogram-equalized
     * @param equalizedHistogram equalized histogram of inputGreyImageData
     */
    public short[][] equalizeImage(short[][] inputGreyImageData,
				   double[] equalizedHistogram) {
        short[][] data = new short[rows][cols];

        double	s = 0;
        for (int y = 0; y < rows; y++) {
            for (int x = 0; x < cols; x++) {
                //s += normalizedHistogram[inputGreyImageData[y][x]];
                data[y][x] = (short) (equalizedHistogram[inputGreyImageData[y][x]] * 255);
            }
        }
        
        return data;
    }

    /* 
     * Create normalized and equalized histograms of inputGreyImageData
     * @param inputGreyImageData a grey image data
     * @param normalizedHistogram used to return normalized histogram
     * @param equalizedHistogram used to return equalized histogram
     */
    public void createHistograms(short[][] inputGreyImageData,
                                 double[] normalizedHistogram, double[] equalizedHistogram) {
        int[] histogram = new int[256];

        // count the number of occurences of each color
        for (int y = 0; y < rows; y++) {
            for (int x = 0; x < cols; x++) {
                ++histogram[inputGreyImageData[y][x]];
            }
        }

        // normalize and equalize the histogram array
        double sum = 0;
        for (int v = 0; v < 256; v++) {
            if (normalizedHistogram != null) {
                normalizedHistogram[v] = (double) histogram[v] / (cols * rows);
            }
            if (equalizedHistogram != null) {
                sum += histogram[v];
                equalizedHistogram[v] = sum / (cols * rows);
            }
        }
//        for (int y = 0; y < rows; y++) {
//            for (int x = 0; x < cols; x++) {
//                histogram[inputGreyImageData[y][x]]
//						 =(short)normalizedHistogram[inputGreyImageData[y][x]];
//            }
//        }

    }

    // packPixel takes the red, green, and blue components
    // of a color and returns a 24-bit representation of the
    // the color, i.e., 0xRRGGBB
    // red, green, and blue are assumed to be in the range 0 - 255
    private static int packPixel(int red, int green, int blue) {
        return (red << 16) | (green << 8) | blue;
    }
    
    // unpackPixel does the opposite of packPixel;
    // it takes a 24-bit pixel in the form 0xRRGGBB
    // and pulls out the three components, storing
    // them position (row, col) or the red, green,
    // and blue arrays
    private static void unpackPixel(int pixel, short [][] red, short [][] green, short [][] blue, int row, int col) {
        red[row][col]   = (short)((pixel >> 16) & 0xFF);
        green[row][col] = (short)((pixel >>  8) & 0xFF);
        blue[row][col]  = (short)((pixel >>  0) & 0xFF);
    }

    //****************************//
    // Various Accessor Functions //
    //****************************//

    public BufferedImage getInputImage() { return inputImage; }
    public BufferedImage getGreyImage() { return greyImage; }
    public BufferedImage getEqualizedImage() { return newImage; }
    public double [] getOldHistogram() { return oldHistogram; }
    public double [] getNewHistogram() { return newHistogram; }

    public void setInputImage(BufferedImage image) { inputImage = image; }
}
