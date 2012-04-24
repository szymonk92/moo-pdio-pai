/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package segmentation;

import java.awt.Color;
import java.awt.Rectangle;
import java.util.HashMap;

/**
 *
 * @author Lukasz
 */
public class QuadNode {

    static int SW = 0, W = 0;
    static int SE = 1, N = 1;
    static int NW = 2, E = 2;
    static int NE = 3, S = 3;
    static int tx = 0;
    static int ty = 0;
    static int EN = 1;
    static int NN = 2;
    static int WN = 0;
    static int SN = 0;
    public QuadTree tree;
    public Rectangle area;
    public int[] neighboursSizes;
    public int position;
    public int depth;
    public Color avrageColor;
    public Region region;
    public HashMap<Integer, QuadNode> NeighboursList;
    public int pixelCount;
    private int addedColors =0;
    private int avrageColorR =0;
    private int avrageColorG =0;
    private int avrageColorB =0;
    public QuadNode(QuadTree tree, Rectangle area, int position, int depth, int[] neighboursSizes) {
        this.tree = tree;
        this.area = area;
        this.position = position;
        this.neighboursSizes = neighboursSizes;
        this.depth = depth;
        this.pixelCount = 0;
        this.NeighboursList = new HashMap<Integer, QuadNode>();
    }

    public HashMap<Integer, QuadNode> split() {
        int width = area.width / 2;
        int height = area.height / 2;
        HashMap<Integer, QuadNode> children = new HashMap<Integer, QuadNode>();
        int childrenPosition = generatePosition(NW);
        children.put(childrenPosition, new QuadNode(this.tree, new Rectangle(area.x, area.y, width, height), childrenPosition, this.depth + 1, generateNeighboursSizes(NW)));
        childrenPosition = generatePosition(SW);
        children.put(childrenPosition, new QuadNode(this.tree, new Rectangle(area.x, area.y + height, width, height), childrenPosition, this.depth + 1, generateNeighboursSizes(SW)));
        childrenPosition = generatePosition(NE);
        children.put(childrenPosition, new QuadNode(this.tree, new Rectangle(area.x + width, area.y, width, height), childrenPosition, this.depth + 1, generateNeighboursSizes(NE)));
        childrenPosition = generatePosition(SE);
        children.put(childrenPosition, new QuadNode(this.tree, new Rectangle(area.x + width, area.y + height, width, height), childrenPosition, this.depth + 1, generateNeighboursSizes(SE)));
        return children;
    }

    private int generatePosition(int type) {
        return position | (type << (2 * (tree.maxDepth - (depth + 1))));
    }

    private int[] generateNeighboursSizes(int type) {
        int[] tmpNeighboursSizes = new int[4];
        if (type == NW || type == SW) {
            if (neighboursSizes[0] == Integer.MAX_VALUE) {
                tmpNeighboursSizes[0] = Integer.MAX_VALUE;
            } else {
                tmpNeighboursSizes[0] = neighboursSizes[0] - 1;
            }
        }
        if (type == NW || type == NE) {
            if (neighboursSizes[1] == Integer.MAX_VALUE) {
                tmpNeighboursSizes[1] = Integer.MAX_VALUE;
            } else {
                tmpNeighboursSizes[1] = neighboursSizes[1] - 1;
            }
        }
        if (type == NE || type == SE) {
            if (neighboursSizes[2] == Integer.MAX_VALUE) {
                tmpNeighboursSizes[2] = Integer.MAX_VALUE;
            } else {
                tmpNeighboursSizes[2] = neighboursSizes[2] - 1;
            }
        }
        if (type == SE || type == SW) {
            if (neighboursSizes[3] == Integer.MAX_VALUE) {
                tmpNeighboursSizes[3] = Integer.MAX_VALUE;
            } else {
                tmpNeighboursSizes[3] = neighboursSizes[3] - 1;
            }
        }
        return tmpNeighboursSizes;
    }

    public int getNeighbour(int type) {
        if (neighboursSizes[type] == Integer.MAX_VALUE) {
            return Integer.MAX_VALUE;
        }
        int n = 0;
        switch (type) {
            case 0:
                n = WN;
                break;
            case 1:
                n = NN;
                break;
            case 2:
                n = EN;
                break;
            case 3:
                n = SN;
                break;
        }

        if (neighboursSizes[type] < 0) {
            int shift = (2 * (tree.maxDepth - depth - neighboursSizes[type]));
            return add((position >> shift) << shift, n << shift);
        } else {
            return add(position, n << (2 * (tree.maxDepth - depth)));
        }
    }

    public int getValidNeighbour(int type) {
        if (neighboursSizes[type] == Integer.MAX_VALUE) {
            return Integer.MAX_VALUE;
        }
        int n = 0;
        switch (type) {
            case 0:
                n = WN;
                break;
            case 1:
                n = NN;
                break;
            case 2:
                n = EN;
                break;
            case 3:
                n = SN;
                break;
        }
        if (neighboursSizes[type] < 0) {

            int shift = (2 * (tree.maxDepth - depth - neighboursSizes[type]));
            return add((position >> shift) << shift, n << shift);
        } else if (neighboursSizes[type] == 0) {
            return add(position, n << (2 * (tree.maxDepth - depth)));
        }
        return Integer.MAX_VALUE;
    }

    public static int add(int a, int b) {
        return (((a | ty) + (b & tx)) & tx) | (((a | tx) + (b & ty)) & ty);
    }

    public void addToColor(Color color) {
        avrageColorR+=color.getRed();
        avrageColorG+=color.getGreen();
        avrageColorB+=color.getBlue();
        //avrageColor = new Color((avrageColor.getRed() + color.getRed()), (avrageColor.getGreen() + color.getGreen()), (avrageColor.getBlue() + color.getBlue()));
        addedColors++;
    }
    public void resetTmpAvrageColor(){
        avrageColorR=0;
        avrageColorG=0;
        avrageColorB=0;
        addedColors = 0;
    }
    public void calculateAvrageColor(){
        avrageColor = new Color((avrageColorR/addedColors), (avrageColorG /addedColors), (avrageColorB/addedColors)); 
        resetTmpAvrageColor();
    }
public void addNeighbur(QuadNode node){
    if(!this.NeighboursList.containsKey(node.position)){
        this.NeighboursList.put(node.position, node);
    }
}
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("TreeNode position:");
        sb.append(positionToString());
        sb.append(" depth:");
        sb.append(depth);
        sb.append(" area:");
        sb.append(area.toString());
       // sb.append(" neighburs size:");
       // sb.append(neighboursSizeToString());
        //sb.append(" neighburs:");
        //sb.append(neighboursToString());
         //sb.append(" color:");
         //if(avrageColor!=null){
        //    sb.append(RGBHelper.toRGBA(avrageColor.getRGB())[0]).append(";").append(RGBHelper.toRGBA(avrageColor.getRGB())[1]).append(";").append(RGBHelper.toRGBA(avrageColor.getRGB())[2]);
        // }
        sb.append(" region: ");
        if (region != null) {
            sb.append(region.id);
            sb.append(" size: ");
            sb.append(region.QuadeNodeList.size());
        }
        return sb.toString();
    }

    public String positionToString() {
        return positionToString(position);
    }

    public String positionToString(int tmpPosition) {
        StringBuilder sb = new StringBuilder();
        for (int i = tree.maxDepth - 1; i >= 0; i--) {
            if (tmpPosition != Integer.MAX_VALUE) {
                sb.append(((tmpPosition >> (2 * i)) & 0x3));
            } else {
                sb.append("#");
            }
        }
        return sb.toString();

    }

    public String neighboursSizeToString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (Integer i : neighboursSizes) {
            sb.append(i);
            sb.append(",");
        }
        sb.append("]");
        return sb.toString();

    }

    public String neighboursToString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (int i = 0; i < 4; i++) {
            switch (i) {
                case 0:
                    sb.append("WN:");
                    break;
                case 1:
                    sb.append("NN:");
                    break;
                case 2:
                    sb.append("EN:");
                    break;
                case 3:
                    sb.append("SN:");
                    break;
            }
            sb.append(positionToString(getNeighbour(i)));
            sb.append(",");
        }
        sb.append("]");
        return sb.toString();

    }

    @Override
    public int hashCode() {
        return position;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final QuadNode other = (QuadNode) obj;
        if (this.position != other.position && this.depth != other.depth) {
            return false;
        }
        return true;
    }
}
