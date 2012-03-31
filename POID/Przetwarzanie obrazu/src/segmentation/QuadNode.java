/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package segmentation;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;

/**
 *
 * @author Lukasz
 */
public class QuadNode {

    static int SW = 0;
    static int SE = 1;
    static int NW = 2;
    static int NE = 3;
    static int maxDepth;
    static QuadTree tree;
    static BufferedImage image;
    public Rectangle area;
    public int[] neighboursSizes = new int[4];
    public int position;
    public int depth;

    public static boolean isPowerOf2(int value) {
        if (((~value) & 1) == 1) {
            return true;
        }
        return false;
    }

    public QuadNode(BufferedImage image) {
        QuadNode.tree = new QuadTree();
        QuadNode.image = image;
        this.position = 0;
        this.depth = 0;
        for (int i = 0; i < 4; i++) {
            neighboursSizes[i] = Integer.MAX_VALUE;
        }
        this.area = new Rectangle(0, 0, image.getWidth(), image.getHeight());
        process();
        int value = 0;
        value += (3 & 0x3) << 4;
        value += (2 & 0x3) << 2;
        value += (1 & 0x3);
        System.out.println((value >> 4) & 0x3);
        System.out.println((value >> 2) & 0x3);
        System.out.println((value) & 0x3);
    }

    public QuadNode(Rectangle area, int position, int depth, int[] neighboursSizes) {
        this.area = area;
        this.position = position;
        this.neighboursSizes = neighboursSizes;
        this.depth = depth;
        process();
    }

    private void process() {
        if (!checkIntegrity()) {
            split();
        } else {
            QuadTree.Add(this);
        }
    }

    private boolean checkIntegrity() {
        return false;
    }

    private void split() {
        int width = area.width / 2;
        int height = area.height / 2;
        QuadNode[] children = new QuadNode[4];
        children[0] = new QuadNode(new Rectangle(area.x, area.y, width, height), QuadNode.generatePosition(this.position, this.depth, NW), this.depth + 1, QuadNode.generateNeighboursSizes(this.neighboursSizes, NW));
        children[1] = new QuadNode(new Rectangle(area.x, area.y + height, width, height), QuadNode.generatePosition(this.position, this.depth, SW), this.depth + 1, QuadNode.generateNeighboursSizes(this.neighboursSizes, SW));
        children[2] = new QuadNode(new Rectangle(area.x + width, area.y, width, height), QuadNode.generatePosition(this.position, this.depth, NE), this.depth + 1, QuadNode.generateNeighboursSizes(this.neighboursSizes, NE));
        children[3] = new QuadNode(new Rectangle(area.x + width, area.y + height, width, height), QuadNode.generatePosition(this.position, this.depth, SE), this.depth + 1, QuadNode.generateNeighboursSizes(this.neighboursSizes, SE));
    }

    private static int generatePosition(int position, int depth, int type) {
        return 0;
    }

    private static int[] generateNeighboursSizes(int[] neighboursSizes, int type) {
        return neighboursSizes;
    }
}
