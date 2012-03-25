/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sys;

import java.awt.image.BufferedImage;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import org.jdesktop.observablecollections.ObservableCollections;
import org.jdesktop.observablecollections.ObservableList;

/**
 *
 * @author Lukasz
 */
public class TabData {

    private ObservableList<IFilter> filters;
    private ObservableList<IView> views;
    private BufferedImage baseImage;
    private BufferedImage baseMiniatureImage;
    private BufferedImage filteredImage;
    private Stopwatch stopWatch;
    private FiltersListener filtersListener;
    private ImageType baseImageType;
    private ImageType filteredImageType;

    public ImageType getBaseImageType() {
        return baseImageType;
    }

    public ImageType getFilteredImageType() {
        return filteredImageType;
    }

    public FiltersListener getFiltersListener() {
        return filtersListener;
    }

    public Stopwatch getStopWatch() {
        return stopWatch;
    }

    public BufferedImage getFilteredImage() {
        return filteredImage;
    }
    private BufferedImage filteredMiniatureImage;

    public BufferedImage getBaseImage() {
        return baseImage;
    }

    public BufferedImage getBaseMiniatureImage() {
        return baseMiniatureImage;
    }

    public BufferedImage getFilteredMiniatureImage() {
        return filteredMiniatureImage;
    }

    public ObservableList<IFilter> getFilters() {
        return filters;
    }

    public ObservableList<IView> getViews() {
        return views;
    }
    private PropertyChangeSupport changeSupport;

    public PropertyChangeSupport getChangeSupport() {
        return changeSupport;
    }

    public TabData(BufferedImage image) {
        baseImage = image;
        baseImageType = BufferedImageHelper.getImageTypes().get(baseImage.getType());
        baseMiniatureImage = BufferedImageHelper.resize(image, 100);
        filteredImage = BufferedImageHelper.copy(image);
        filteredImageType = BufferedImageHelper.getImageTypes().get(filteredImage.getType());
        filteredMiniatureImage = BufferedImageHelper.copy(baseMiniatureImage);
        filters = ObservableCollections.observableList(new ArrayList<IFilter>());
        views = ObservableCollections.observableList(new ArrayList<IView>());
        changeSupport = new PropertyChangeSupport(this);
        stopWatch = new Stopwatch(image.getHeight() * image.getWidth());
        filtersListener = new FiltersListener(this, null);
        filters.addObservableListListener(filtersListener);
    }

    public void setFilteredImage(BufferedImage image) {
        filteredImage = image;
        filteredMiniatureImage = BufferedImageHelper.resize(image, 100);
        filteredImageType = BufferedImageHelper.getImageTypes().get(filteredImage.getType());
        changeSupport.fireIndexedPropertyChange("filteredImage", -1, null, filteredImage);
    }
}
