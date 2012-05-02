package edu.plodz.oi;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class SignWrapper {
	
	public int counter;
	

	File outdir;
	Document con;
	XPath xpath;

	BufferedImage img;

	public SignWrapper(FileInputStream inputImg,
			File outDir, FileInputStream imageContours) {
		counter =0;
		this.outdir = outDir;

		DocumentBuilder docBuild;
		DocumentBuilderFactory docBuildFactory;
		Document domDoc = null;
		BufferedImage image = null;
		try {
			image = ImageIO.read(inputImg);

			docBuildFactory = DocumentBuilderFactory.newInstance();
			docBuildFactory.setNamespaceAware(true);
			docBuild = docBuildFactory.newDocumentBuilder();
			domDoc = docBuild.parse(imageContours);
//			System.out.println(domDoc.getFirstChild().getNodeName());

			xpath = XPathFactory.newInstance().newXPath();

		} catch (IOException ioe) {
			ioe.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
		img = image;
		con = domDoc;
	}
	
	
	public void getSigns(String label, String outname) {
		NodeList nl = getNodesByXPath("//annotationgroup[@label='"+label+"']/paths//path");
		System.out.println(nl.getLength());
		for( int i=0; i<nl.getLength(); ++i) {
			int minx=2048, maxx=0, miny=2048, maxy=0;
			
			NodeList path = nl.item(i).getChildNodes().item(1).getChildNodes();
			if ( path.getLength() < 2) continue;
//			System.out.println(path.getLength());
			for( int j=0; j< path.getLength()-1; ++j) {
//				System.out.println(path.item(j).getNodeName());
				int x =(int) Float.parseFloat(path.item(j).getAttributes().getNamedItem("coord-x").getTextContent());
				int y =(int) Float.parseFloat(path.item(j).getAttributes().getNamedItem("coord-y").getTextContent());
			
				minx = Math.min(minx, x);
				miny = Math.min(miny, y);
				maxx = Math.max(maxx, x);
				maxy = Math.max(maxy, y);
				
//				System.out.println("("+x+","+y+")");
			}
			
			minx = (minx-2)<0?minx:minx-2;
			miny = (miny-2)<0?miny:miny-2;
			int
				width = ((maxx+4)> img.getWidth()) ? (maxx-minx)+ (maxx+4 - img.getWidth())  : (maxx-minx+4),
				height = ((maxy+4)> img.getHeight()) ? (maxy-miny)+ (maxy+4 - img.getHeight())  : (maxy-miny+4);
			System.out.println(img.getWidth()+","+img.getHeight()+";"+minx+","+miny+","+width+","+height);
			BufferedImage out = img.getSubimage(minx, miny, width, height);
			
			out = resize(out, 64, 64);
			try {
				File outf = new File(outdir.getPath()+"/"+outname+counter+".png");
				while ( outf.exists() )
					outf = new File(outdir.getPath()+"/"+outname+(++counter)+".png");
				ImageIO.write(out, "png", outf);
				
			} catch (IOException e) {
				//generic exception handling
				e.printStackTrace();
			}
			
			counter++;
			
			
			
		}
	}
	//DOM walking function
	
	public NodeList getNodesByXPath(String xpaths) {
		NodeList nodes=null;
		try {
			XPathExpression expr = xpath.compile(xpaths);
			Object result = expr.evaluate(con, XPathConstants.NODESET);
			  nodes = (NodeList) result;
		} catch (XPathExpressionException e) {
			e.printStackTrace();
		}
		return nodes;
	}
	
	//hq tinypic

	private BufferedImage blurImage(BufferedImage image) {
		float ninth = 1.0f / 9.0f;
		float[] blurKernel = { ninth, ninth, ninth, ninth, ninth, ninth, ninth,
				ninth, ninth };

		Map map = new HashMap();

		map.put(RenderingHints.KEY_INTERPOLATION,
				RenderingHints.VALUE_INTERPOLATION_BILINEAR);

		map.put(RenderingHints.KEY_RENDERING,
				RenderingHints.VALUE_RENDER_QUALITY);

		map.put(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);

		RenderingHints hints = new RenderingHints(map);
		BufferedImageOp op = new ConvolveOp(new Kernel(3, 3, blurKernel),
				ConvolveOp.EDGE_NO_OP, hints);
		return op.filter(image, null);
	}

	private BufferedImage createCompatibleImage(BufferedImage image) {
		int w = image.getWidth();
		int h = image.getHeight();
		BufferedImage result = new BufferedImage(w, h, Transparency.TRANSLUCENT);
		Graphics2D g2 = result.createGraphics();
		g2.drawRenderedImage(image, null);
		g2.dispose();
		return result;
	}

	private BufferedImage resizeTrick(BufferedImage image, int width, int height) {
		image = createCompatibleImage(image);
		image = resize(image, 100, 100);
		image = blurImage(image);
		image = resize(image, width, height);
		return image;
	}

	private static BufferedImage resize(BufferedImage image, int width,
			int height) {
		int type = image.getType() == 0 ? BufferedImage.TYPE_INT_ARGB : image
				.getType();
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

}
