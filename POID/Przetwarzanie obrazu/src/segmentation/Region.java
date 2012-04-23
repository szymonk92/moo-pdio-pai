/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package segmentation;

import java.awt.Color;
import java.util.HashMap;

/**
 *
 * @author Lukasz
 */
public class Region {
    public HashMap<Integer, QuadNode> QuadeNodeList;
    public int id;
    public Color avrageColor;
    public int pixelCount;
    public Region(int id){
        pixelCount = 0;
        this.id = id;
        QuadeNodeList = new HashMap<Integer, QuadNode>();
    }
    public void addNode(QuadNode node){
        pixelCount+=node.pixelCount;
        node.region = this;
        addToColor(node.avrageColor);
        QuadeNodeList.put(node.position, node);
    }
    public void addRegion(Region region){
        pixelCount +=region.pixelCount;
        for(QuadNode node : region.QuadeNodeList.values()){
            this.addNode(node);
        }
    }
    
     public void addToColor(Color color) {
         if(avrageColor== null) {
             avrageColor = new Color(color.getRed(),color.getGreen(),color.getBlue());
             return;
         }
        avrageColor = new Color((avrageColor.getRed() + color.getRed()) / 2, (avrageColor.getGreen() + color.getGreen()) / 2, (avrageColor.getBlue() + color.getBlue()) / 2);
    }
        @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Region other = (Region) obj;
        if (this.id != other.id ) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        return id;
    }
    
    public void Clear(){
        QuadeNodeList = null;
        avrageColor = null;
        pixelCount = 0;
    }
}
