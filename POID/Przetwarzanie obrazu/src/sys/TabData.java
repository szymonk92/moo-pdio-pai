/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sys;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import org.jdesktop.observablecollections.ObservableCollections;
import org.jdesktop.observablecollections.ObservableList;

/**
 *
 * @author Lukasz
 */
public class TabData {
     public ObservableList<IFilter> filters;
     public ObservableList<IView> views;
     public BufferedImage baseImage;
     public BufferedImage baseMiniatureImage;
     public BufferedImage filteredImage;
     public BufferedImage filteredMiniatureImage;
     
     public TabData(BufferedImage image){
         baseImage = image;
         baseMiniatureImage = BufferedImageHelper.resize(image, 100);
         filteredImage = BufferedImageHelper.copy(image);
         filteredMiniatureImage = BufferedImageHelper.copy(baseMiniatureImage);
         filters =ObservableCollections.observableList(new ArrayList<IFilter>());
         views =ObservableCollections.observableList(new ArrayList<IView>());
     }
}
