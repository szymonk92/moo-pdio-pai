package sys;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.*;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class SignWrapper {

    Document con;
    XPath xpath;
    BufferedImage img;
private static int subImageSize = 30;
    public SignWrapper(BufferedImage image) {
        img = image;

    }

    public void setXmlFile(FileInputStream xmlFile) {
        DocumentBuilder docBuild;
        DocumentBuilderFactory docBuildFactory;
        Document domDoc = null;
        try {

            docBuildFactory = DocumentBuilderFactory.newInstance();
            docBuildFactory.setNamespaceAware(true);
            docBuild = docBuildFactory.newDocumentBuilder();
            domDoc = docBuild.parse(xmlFile);
            xpath = XPathFactory.newInstance().newXPath();

        } catch (IOException ioe) {
        } catch (SAXException e) {
        } catch (ParserConfigurationException e) {
        }
        con = domDoc;
    }

    public List<Rectangle> getSigns(String label) {
        List<Rectangle> result = new ArrayList<Rectangle>();
        NodeList nl = getNodesByXPath("//annotationgroup[@label='" + label + "']/paths//path");
        for (int i = 0; i < nl.getLength(); ++i) {
            int minx = 2048, maxx = 0, miny = 2048, maxy = 0;

            NodeList path = nl.item(i).getChildNodes().item(1).getChildNodes();
            if (path.getLength() < 2) {
                continue;
            }
            for (int j = 0; j < path.getLength() - 1; ++j) {
                int x = (int) Float.parseFloat(path.item(j).getAttributes().getNamedItem("coord-x").getTextContent());
                int y = (int) Float.parseFloat(path.item(j).getAttributes().getNamedItem("coord-y").getTextContent());
                minx = Math.min(minx, x);
                miny = Math.min(miny, y);
                maxx = Math.max(maxx, x);
                maxy = Math.max(maxy, y);
            }

            minx = (minx - 2) < 0 ? minx : minx - 2;
            miny = (miny - 2) < 0 ? miny : miny - 2;
            int width = ((maxx + 4) > img.getWidth()) ? (maxx - minx) + (maxx + 4 - img.getWidth()) : (maxx - minx + 4);
            int height = ((maxy + 4) > img.getHeight()) ? (maxy - miny) + (maxy + 4 - img.getHeight()) : (maxy - miny + 4);
            result.add(new Rectangle(minx, miny, width, height));
        }
        return result;
    }

    public List<BufferedImage> getSubImages(List<Rectangle> rectangles) {
        List<BufferedImage> result = new ArrayList<BufferedImage>();
        if (img != null) {
            for (Rectangle rect : rectangles) {
                result.add(img.getSubimage(rect.x, rect.y, rect.width, rect.height));
            }
        }
        return result;
    }

    public List<BufferedImage> getSubImagesFit(List<Rectangle> rectangles) {
        List<BufferedImage> result = new ArrayList<BufferedImage>();
        if (img != null) {
            for (Rectangle rect : rectangles) {
                result.add(resize(img.getSubimage(rect.x, rect.y, rect.width, rect.height), subImageSize,subImageSize));
            }
        }
        return result;
    }
     public BufferedImage getSubImageFit(Rectangle rect) {
        if (img != null) {
                return resize(img.getSubimage(rect.x, rect.y, rect.width, rect.height), subImageSize,subImageSize);
        }
        return null;
    }
     
      public static BufferedImage getSubImageFit(BufferedImage image) {
        if (image != null) {
                return resize(image, 30,30);
        }
        return null;
    }
    //DOM walking function

    public NodeList getNodesByXPath(String xpaths) {
        NodeList nodes = null;
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
        float[] blurKernel = {ninth, ninth, ninth, ninth, ninth, ninth, ninth,
            ninth, ninth};

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

    public static boolean isTheSame(Rectangle first, Rectangle second) {
        if (first.intersects(second)) {
            double firstArea = first.height * first.width;
            double secondArea = second.height * second.width;
            double maxArea = Math.max(firstArea, secondArea);
            double minArea = Math.min(firstArea, secondArea);
            double result = minArea/maxArea;
            if(result>0.4 &&result<1.4){
                Rectangle rect = first.intersection(second);
                double rectArea = rect.height * rect.width;
                double distance = Point.distance(first.getCenterX(), first.getCenterY(), second.getCenterX(), second.getCenterY());
                double  maxRectArea = Math.max(rectArea/firstArea, rectArea/secondArea);
                if (distance<first.height/0.2 && maxRectArea>0.6) {
                    return true;
                }
            }
        }
        return false;
    }

    public static int isTheSame(List<Rectangle> first, List<Rectangle> second) {
        int result = 0;
        for (Rectangle rect : first) {
            for (Rectangle rect2 : second) {
                if (isTheSame(rect, rect2)) {
                    result++;
                    break;
                }
            }
        }
        return result;
    }

    public static BufferedImage resize(BufferedImage image, int width,
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
}
