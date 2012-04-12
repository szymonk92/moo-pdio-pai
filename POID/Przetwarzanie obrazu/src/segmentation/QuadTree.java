/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package segmentation;

import java.awt.Color;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Random;

/**
 *
 * @author Lukasz
 */
public class QuadTree {

    BufferedImage image;
    public HashMap<Integer, QuadNode> QuadeNodeList;
    public int maxDepth;
    static Random random = new Random();
    public IPixelComperer pixelComperer; 
    public static boolean isPowerOf2(int value) {
        if (((~value) & 1) == 1) {
            return true;
        }
        return false;
    }

    public QuadTree(BufferedImage image) {
        this.image = image;
        QuadeNodeList = new HashMap<Integer, QuadNode>();
        maxDepth = Math.min(whichPowOf2(image.getHeight()), whichPowOf2(image.getWidth()));
        setTx();
        setTy();
    }

    public void setPixelComperer(IPixelComperer comperer){
       this.pixelComperer = comperer;
       this.pixelComperer.setImage(image);
    }
    public int whichPowOf2(int value) {
        if (value == 0) {
            return -1;
        }
        if (value == 1) {
            return 0;
        }
        if (!isPowerOf2(value)) {
            return -1;
        }
        int count = 0;
        for (int i = value/2; i > 0; i = i / 2) {
            count++;
        }
        return count;
    }

    private void setTx() {
        int tx = 0;
        for (int i = 0; i < maxDepth * 2; i += 2) {
            tx += Math.pow(2, i);
        }
        QuadNode.tx = tx;
        QuadNode.WN = tx;
    }

    private void setTy() {
        int ty = 0;
        for (int i = 1; i < maxDepth * 2; i += 2) {
            ty += Math.pow(2, i);
        }
        QuadNode.ty = ty;
        QuadNode.SN = ty;
    }

    public void process() {
        HashMap<Integer, QuadNode> tmpNodes = new HashMap<Integer, QuadNode>();
        tmpNodes.put(0, new QuadNode(this, new Rectangle(0, 0, image.getWidth(), image.getHeight()), 0, 0, new int[]{Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE}));
        process(tmpNodes, 0);
    }

    private void process(HashMap<Integer, QuadNode> nodes, int currentDepth) {
        HashMap<Integer, QuadNode> tmpNodes = new HashMap<Integer, QuadNode>();
        for (QuadNode node : nodes.values()) {
            if (checkIntegrety(node) || currentDepth == maxDepth - 1) {
                QuadeNodeList.put(node.position, node);
            } else {
                for (int i = 0; i < 4; i++) {
                    QuadNode neighbour = nodes.get(node.getNeighbour(i));
                    if (neighbour != null) {
                        neighbour.neighboursSizes[getOpositNeighbourType(i)] += 1;
                    }
                }
                HashMap<Integer, QuadNode> children = node.split();
                HashMap<Integer, QuadNode> childrenNeighbours = new HashMap<Integer, QuadNode>();
                for (QuadNode child : children.values()) {
                    for (int i = 0; i < 4; i++) {
                        int neighbour = child.getNeighbour(i);
                        if (!children.containsKey(neighbour) && !childrenNeighbours.containsKey(neighbour)) {
                            QuadNode childrenNeighbour = tmpNodes.get(neighbour);
                            if (childrenNeighbour != null) {
                                childrenNeighbours.put(neighbour, childrenNeighbour);
                                childrenNeighbour.neighboursSizes[getOpositNeighbourType(i)] += 1;
                            }
                        }
                    }
                }
                tmpNodes.putAll(children);
            }
        }
        if (!tmpNodes.isEmpty()) {
            process(tmpNodes, currentDepth + 1);
        }
    }

    private boolean checkIntegrety(QuadNode node) {
        if(pixelComperer== null) return false;
        node.avrageColor = new Color(image.getRGB(node.area.x,node.area.y));
        for (int x = node.area.x; x < node.area.x + node.area.width; x++) {
            for (int y = node.area.y; y < node.area.y + node.area.height; y++) {
                if (!pixelComperer.Comppere(x, y, node.avrageColor)) {
                    return false;
                }
                else{
                     node.addToColor(new Color(image.getRGB(x,y)));
                }
            }
        }
        return true;
    }

    private int getOpositNeighbourType(int i) {
        int n = 0;
        switch (i) {
            case 0:
                n = 2;
                break;
            case 1:
                n = 3;
                break;
            case 2:
                n = 0;
                break;
            case 3:
                n = 1;
                break;
        }
        return n;
    }
}
