/**
 * Instituto Tecnol�gico de Aeron�utica
 * Projeto de CCI36 - Reconhecimento de formas planas simples
 * Douglas Ribeiro | Emil Nakao
 *  
 * Classe que visava detectar circunfer�ncias e mostrar os seus centros
 * 
 */

package filters;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;

public class HoughCircle {

	public int radiusMin; // Find circles with radius grater or equal radiusMin
	public int radiusMax; // Find circles with radius less or equal radiusMax
	public int radiusInc; // Increment used to go from radiusMin to radiusMax
	public int maxCircles; // Numbers of circles to be found
	public int threshold = -1; // An alternative to maxCircles. All circles with
	// a value in the hough space greater then threshold are marked. Higher
	// thresholds
	// results in fewer circles.
	int imageValues[]; // Raw image (returned by ip.getPixels())
	double houghValues[][][]; // Hough Space Values
	public int width; // Hough Space width (depends on image width)
	public int height; // Hough Space heigh (depends on image height)
	public int depth; // Hough Space depth (depends on radius interval)
	public int offset; // Image Width
	public int offx; // ROI x offset
	public int offy; // ROI y offset
	Point centerPoint[]; // Center Points of the Circles Found.
	private int vectorMaxSize = 500;
	boolean useThreshold = false;
	int lut[][][]; // LookUp Table for rsin e rcos values

	BufferedImage src = null;
	BufferedImage dest = null;

	public HoughCircle(BufferedImage src) {
		radiusMin = 50;
		radiusMax = 200;
		radiusInc = 2;
		maxCircles = 1;

		width = src.getWidth();
		height = src.getHeight();

		offset = src.getWidth();
		offx = 0;
		offy = 0;

		depth = ((radiusMax - radiusMin) / radiusInc) + 1;

		setSrc(src);
	}

	public void applyHough() {
		Raster rasterSrc = getSrc().getRaster();
		int w = getSrc().getWidth();
		int h = getSrc().getHeight();
		int temp[] = new int[4 * w * h];
		byte houghPixels[] = new byte[width * height];
		imageValues = rasterSrc.getPixels(0, 0, w, h, temp);
		houghTransform();
		createHoughPixels(houghPixels);

		// Create image View for Marked Circles.

		int[] circlespixels = new int[width * height];

		// Mark the center of the found circles in a new image
		if (useThreshold)
			getCenterPointsByThreshold(threshold);
		else
			getCenterPoints(maxCircles);
		drawCircles(circlespixels);

		setDest(new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB));
		getDest().getWritableTile(0, 0).setDataElements(0, 0, width, height,
				circlespixels);

	}

	public BufferedImage getSrc() {
		return src;
	}

	public void setSrc(BufferedImage src) {
		this.src = src;
	}

	public BufferedImage getDest() {
		return dest;
	}

	public void setDest(BufferedImage dest) {
		this.dest = dest;
	}

	/**
	 * The parametric equation for a circle centered at (a,b) with radius r is:
	 * 
	 * a = x - r*cos(theta) b = y - r*sin(theta)
	 * 
	 * In order to speed calculations, we first construct a lookup table (lut)
	 * containing the rcos(theta) and rsin(theta) values, for theta varying from
	 * 0 to 2*PI with increments equal to 1/8*r. As of now, a fixed increment is
	 * being used for all different radius (1/8*radiusMin). This should be
	 * corrected in the future.
	 * 
	 * Return value = Number of angles for each radius
	 */
	private int buildLookUpTable() {

		int i = 0;
		int incDen = Math.round(8F * radiusMin); // increment denominator

		lut = new int[2][incDen][depth];

		for (int radius = radiusMin; radius <= radiusMax; radius = radius
				+ radiusInc) {
			i = 0;
			for (int incNun = 0; incNun < incDen; incNun++) {
				double angle = (2 * Math.PI * (double) incNun)
						/ (double) incDen;
				int indexR = (radius - radiusMin) / radiusInc;
				int rcos = (int) Math.round((double) radius * Math.cos(angle));
				int rsin = (int) Math.round((double) radius * Math.sin(angle));
				if ((i == 0) | (rcos != lut[0][i][indexR])
						& (rsin != lut[1][i][indexR])) {
					lut[0][i][indexR] = rcos;
					lut[1][i][indexR] = rsin;
					i++;
				}
			}
		}

		return i;
	}

	private void houghTransform() {

		int lutSize = buildLookUpTable();

		houghValues = new double[width][height][depth];

		int k = width - 1;
		int l = height - 1;

		for (int y = 1; y < l; y++) {
			for (int x = 1; x < k; x++) {
				for (int radius = radiusMin; radius <= radiusMax; radius = radius
						+ radiusInc) {
					if (imageValues[(x + offx) + (y + offy) * offset] != 0) {// Edge
																				// pixel
																				// found
						int indexR = (radius - radiusMin) / radiusInc;
						for (int i = 0; i < lutSize; i++) {

							int a = x + lut[1][i][indexR];
							int b = y + lut[0][i][indexR];
							if ((b >= 0) & (b < height) & (a >= 0)
									& (a < width)) {
								houghValues[a][b][indexR] += 1;
							}
						}

					}
				}
			}

		}

	}

	// Convert Values in Hough Space to an Image Space.
	private void createHoughPixels(byte houghPixels[]) {
		double d = -1D;
		for (int j = 0; j < height; j++) {
			for (int k = 0; k < width; k++)
				if (houghValues[k][j][0] > d) {
					d = houghValues[k][j][0];
				}

		}

		for (int l = 0; l < height; l++) {

			for (int i = 0; i < width; i++) {
				houghPixels[i + l * width] = (byte) Math
						.round((houghValues[i][l][0] * 255D) / d);
			}

		}
	}

	// Draw the circles found in the original image.
	public void drawCircles(int[] circlespixels) {

		// Copy original image to the circlespixels image.
		// Changing pixels values to 100, so that the marked
		// circles appears more clear.

		for (int i = 0; i < width * height; ++i) {
			if (imageValues[i] != 0)
				circlespixels[i] = 100;
			else
				circlespixels[i] = 0;
		}

		if (centerPoint == null) {
			if (useThreshold)
				getCenterPointsByThreshold(threshold);
			else
				getCenterPoints(maxCircles);
		}

		byte cor = -1;

		for (int l = 0; l < maxCircles; l++) {

			int i = centerPoint[l].x;
			int j = centerPoint[l].y;

			// Draw a gray cross marking the center of each circle.
			for (int k = -10; k <= 10; ++k) {
				if (!outOfBounds(j + k + offy, i + offx))
					circlespixels[(j + k + offy) * offset + (i + offx)] = cor;
				if (!outOfBounds(j + offy, i + k + offx))
					circlespixels[(j + offy) * offset + (i + k + offx)] = cor;
			}

			for (int k = -2; k <= 2; ++k) {
				if (!outOfBounds(j - 2 + offy, i + k + offx))
					circlespixels[(j - 2 + offy) * offset + (i + k + offx)] = cor;
				if (!outOfBounds(j + 2 + offy, i + k + offx))
					circlespixels[(j + 2 + offy) * offset + (i + k + offx)] = cor;
				if (!outOfBounds(j + k + offy, i - 2 + offx))
					circlespixels[(j + k + offy) * offset + (i - 2 + offx)] = cor;
				if (!outOfBounds(j + k + offy, i + 2 + offx))
					circlespixels[(j + k + offy) * offset + (i + 2 + offx)] = cor;
			}
		}
	}

	private boolean outOfBounds(int y, int x) {
		if (x >= width)
			return (true);
		if (x <= 0)
			return (true);
		if (y >= height)
			return (true);
		if (y <= 0)
			return (true);
		return (false);
	}

	public Point nthMaxCenter(int i) {
		return centerPoint[i];
	}

	/**
	 * Search for a fixed number of circles.
	 * 
	 * @param maxCircles
	 *            The number of circles that should be found.
	 */
	private void getCenterPoints(int maxCircles) {

		centerPoint = new Point[maxCircles];
		int xMax = 0;
		int yMax = 0;
		int rMax = 0;

		for (int c = 0; c < maxCircles; c++) {
			double counterMax = -1;
			for (int radius = radiusMin; radius <= radiusMax; radius = radius
					+ radiusInc) {

				int indexR = (radius - radiusMin) / radiusInc;
				for (int y = 0; y < height; y++) {
					for (int x = 0; x < width; x++) {
						if (houghValues[x][y][indexR] > counterMax) {
							counterMax = houghValues[x][y][indexR];
							xMax = x;
							yMax = y;
							rMax = radius;
						}
					}

				}
			}

			centerPoint[c] = new Point(xMax, yMax);

			clearNeighbours(xMax, yMax, rMax);
		}
	}

	/**
	 * Search circles having values in the hough space higher than a threshold
	 * 
	 * @param threshold
	 *            The threshold used to select the higher point of Hough Space
	 */
	private void getCenterPointsByThreshold(int threshold) {

		centerPoint = new Point[vectorMaxSize];
		int xMax = 0;
		int yMax = 0;
		int countCircles = 0;

		for (int radius = radiusMin; radius <= radiusMax; radius = radius
				+ radiusInc) {
			int indexR = (radius - radiusMin) / radiusInc;
			for (int y = 0; y < height; y++) {
				for (int x = 0; x < width; x++) {

					if (houghValues[x][y][indexR] > threshold) {

						if (countCircles < vectorMaxSize) {

							centerPoint[countCircles] = new Point(x, y);

							clearNeighbours(xMax, yMax, radius);

							++countCircles;
						} else
							break;
					}
				}
			}
		}

		maxCircles = countCircles;
	}

	/**
	 * Clear, from the Hough Space, all the counter that are near (radius/2) a
	 * previously found circle C.
	 * 
	 * @param x
	 *            The x coordinate of the circle C found.
	 * @param x
	 *            The y coordinate of the circle C found.
	 * @param x
	 *            The radius of the circle C found.
	 */
	private void clearNeighbours(int x, int y, int radius) {

		// The following code just clean the points around the center of the
		// circle found.

		double halfRadius = radius / 2.0F;
		double halfSquared = halfRadius * halfRadius;

		int y1 = (int) Math.floor((double) y - halfRadius);
		int y2 = (int) Math.ceil((double) y + halfRadius) + 1;
		int x1 = (int) Math.floor((double) x - halfRadius);
		int x2 = (int) Math.ceil((double) x + halfRadius) + 1;

		if (y1 < 0)
			y1 = 0;
		if (y2 > height)
			y2 = height;
		if (x1 < 0)
			x1 = 0;
		if (x2 > width)
			x2 = width;

		for (int r = radiusMin; r <= radiusMax; r = r + radiusInc) {
			int indexR = (r - radiusMin) / radiusInc;
			for (int i = y1; i < y2; i++) {
				for (int j = x1; j < x2; j++) {
					if (Math.pow(j - x, 2D) + Math.pow(i - y, 2D) < halfSquared) {
						houghValues[j][i][indexR] = 0.0D;
					}
				}
			}
		}

	}

}
