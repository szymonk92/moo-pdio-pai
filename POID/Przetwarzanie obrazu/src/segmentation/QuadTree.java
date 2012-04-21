/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package segmentation;

import java.awt.Color;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

/**
 *
 * @author Lukasz
 */
public class QuadTree {

    BufferedImage image;
    public HashMap<Integer, QuadNode> QuadeNodeList;
    public List<Region> regions;
    public int maxDepth;
    public int depthLimit;
    static Random random = new Random();
    public IPixelComparer pixelComperer;
    public boolean forceSplit;
    public int minimumPixelRegion;
    public boolean imageGrayScale = false;

    public static boolean isPowerOf2(int value) {
        if (((~value) & 1) == 1) {
            return true;
        }
        return false;
    }

    public QuadTree(BufferedImage image) {
        this.image = image;
        forceSplit = false;
        QuadeNodeList = new HashMap<Integer, QuadNode>();
        maxDepth = Math.min(whichPowOf2(image.getHeight()), whichPowOf2(image.getWidth()));
        depthLimit = maxDepth;
        minimumPixelRegion = 0;
        setTx();
        setTy();
        Raster raster = image.getRaster();
        if (raster.getNumBands() == 1) {
            imageGrayScale = true;
        }
    }

    public void setPixelComperer(IPixelComparer comperer) {
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
        for (int i = value / 2; i > 0; i = i / 2) {
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
        QuadeNodeList = new HashMap<Integer, QuadNode>();
        HashMap<Integer, QuadNode> tmpNodes = new HashMap<Integer, QuadNode>();
        tmpNodes.put(0, new QuadNode(this, new Rectangle(0, 0, image.getWidth(), image.getHeight()), 0, 0, new int[]{Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE}));
        process(tmpNodes, 0);
        marge();
        if (minimumPixelRegion > Math.pow(2, maxDepth - depthLimit)) {
            margeSmallRegions();
        }
    }

    private void process(HashMap<Integer, QuadNode> nodes, int currentDepth) {
        HashMap<Integer, QuadNode> tmpNodes = new HashMap<Integer, QuadNode>();
        for (QuadNode node : nodes.values()) {
            if (checkIntegrety(node, currentDepth) || currentDepth == depthLimit) {
                node.pixelCount = (int) Math.pow(2, maxDepth - node.depth);
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

    private Color getImagePixelColor(int x, int y) {
        Raster raster = image.getRaster();
        if (raster.getNumBands() == 1) {
            int pixelValue = raster.getSample(x, y, 0);
            return new Color(pixelValue, pixelValue, pixelValue);
        } else {
            return new Color(image.getRGB(x, y));
        }
    }

    private boolean checkIntegrety(QuadNode node, int currentDepth) {
        if (pixelComperer == null || (forceSplit && (currentDepth != depthLimit))) {
            return false;
        }
        node.avrageColor = getImagePixelColor(node.area.x, node.area.y);
        for (int x = node.area.x; x < node.area.x + node.area.width; x++) {
            for (int y = node.area.y; y < node.area.y + node.area.height; y++) {
                Color pixelColor = getImagePixelColor(x, y);
                if (!pixelComperer.Compare(pixelColor, node.avrageColor, imageGrayScale) && currentDepth != depthLimit) {
                    return false;
                } else {
                    node.addToColor(pixelColor);
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

    private void marge() {
        regions = new ArrayList<Region>();
        int regionCounter = 0;
        for (QuadNode node : QuadeNodeList.values()) {
            for (int i = 0; i < 4; i++) {
                int value = node.getValidNeighbour(i);
                if (value != Integer.MAX_VALUE) {
                    QuadNode neighbour = QuadeNodeList.get(value);
                    if (neighbour != null) {
                        neighbour.addNeighbur(node);
                        node.addNeighbur(neighbour);

                        if (node.region == null && neighbour.region == null) {
                            if (pixelComperer.Compare(neighbour.avrageColor, node.avrageColor, imageGrayScale)) {
                                node.region = new Region(regionCounter);
                                regionCounter++;
                                node.region.addNode(node);
                                node.region.addNode(neighbour);
                                this.regions.add(node.region);
                            }
                        } else if (node.region == null && neighbour.region != null) {
                            if (pixelComperer.Compare(neighbour.region.avrageColor, node.avrageColor, imageGrayScale)) {
                                neighbour.region.addNode(node);
                            }
                        } else if (node.region != null && neighbour.region == null) {
                            if (pixelComperer.Compare(node.region.avrageColor, neighbour.avrageColor, imageGrayScale)) {
                                node.region.addNode(neighbour);
                            }
                        } else if (node.region != null && neighbour.region != null && !node.region.equals(neighbour.region)) {
                            if (pixelComperer.Compare(node.region.avrageColor, neighbour.region.avrageColor, imageGrayScale)) {
                                if (node.region.QuadeNodeList.size() > neighbour.region.QuadeNodeList.size()) {
                                    Region region = neighbour.region;
                                    node.region.addRegion(region);
                                    this.regions.remove(region);
                                } else {
                                    Region region = node.region;
                                    neighbour.region.addRegion(region);
                                    this.regions.remove(region);
                                }
                            }
                        }
                        if (node.region == null) {
                            node.region = new Region(regionCounter);
                            regionCounter++;
                            node.region.addNode(node);
                            this.regions.add(node.region);
                        }
                        if (neighbour.region == null) {
                            neighbour.region = new Region(regionCounter);
                            regionCounter++;
                            neighbour.region.addNode(neighbour);
                            this.regions.add(neighbour.region);
                        }
                    }
                }
            }
        }
    }

    private void margeSmallRegions() {
        List<Region> tmpRegions = new ArrayList<Region>();
        List<Region> removedRegions = new ArrayList<Region>();
        double currentValue;
        for (Region region : regions) {
            if (!removedRegions.contains(region)) {
                while (region.pixelCount < this.minimumPixelRegion) {
                    Region minimalRegion = null;
                    double value = Double.MAX_VALUE;

                    for (QuadNode node : region.QuadeNodeList.values()) {
                        for (QuadNode neighbur : node.NeighboursList.values()) {
                            if (!neighbur.region.equals(node.region)) {
                                currentValue = pixelComperer.getCompareValue(region.avrageColor, neighbur.region.avrageColor, imageGrayScale);
                                if (currentValue < value) {
                                    minimalRegion = neighbur.region;
                                    value = currentValue;
                                }
                            }
                        }
                    }
                    if (minimalRegion != null) {
                        region.addRegion(minimalRegion);
                        removedRegions.add(minimalRegion);
                        tmpRegions.remove(minimalRegion);
                    } else {
                        break;
                    }
                }
                tmpRegions.add(region);
            }
        }
        this.regions = tmpRegions;
    }
}
