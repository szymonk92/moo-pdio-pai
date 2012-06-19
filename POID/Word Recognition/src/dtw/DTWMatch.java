/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dtw;

import gui.MatchPreview;
import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.util.List;

/**
 *
 * @author Lukasz
 */
public class DTWMatch {

    private DTWData data;
    private MatchPreview view;
    private double warpingDistance;
    private List<DTWPoint> warpingPath;
    private double[][] distanceTabel;
    private boolean globalConstraints;
    private boolean thresholdLevel;
    
    BufferedImage image;

    public DTWMatch(DTWData data) {
        this.data = data;
        this.globalConstraints = true;
    }

    public boolean isGlobalConstraints() {
        return globalConstraints;
    }

    public void setGlobalConstraints(boolean globalConstraints) {
        this.globalConstraints = globalConstraints;
        getView().setGlobalConstrains(globalConstraints);
    }

    public BufferedImage getImage() {
        return image;
    }

    public void setImage(BufferedImage image) {
        this.image = image;
        getView().setMiniaturImage(image);
    }

    public double[][] getDistanceTabel() {
        return distanceTabel;
    }

    public void setDistanceTabel(double[][] D) {
        this.distanceTabel = D;
    }

    public MatchPreview getView() {
        if (view == null) {
            view = new MatchPreview(this);
        }
        return view;
    }

    public void setView(MatchPreview view) {
        this.view = view;
    }

    public double getWarpingDistance() {
        return warpingDistance;
    }

    public void setWarpingDistance(double warpingDistance) {
        this.warpingDistance = warpingDistance;
        getView().setDistance(warpingDistance);
    }

    public List<DTWPoint> getWarpingPath() {
        return warpingPath;
    }

    public void setWarpingPath(List<DTWPoint> warpingPath) {
        this.warpingPath = warpingPath;
    }

    public DTWData getData() {
        return data;
    }

    public void setData(DTWData data) {
        this.data = data;
        getView().setWord(data.getWord());
    }

    public boolean isThresholdLevel() {
        return thresholdLevel;
    }

    public void setThresholdLevel(boolean thresholdLevel) {
        this.thresholdLevel = thresholdLevel;
        getView().setThresholdLevel(thresholdLevel);
    }

    public void clear() {
       setImage(null);
       setWarpingDistance(Double.MAX_VALUE);
       setWarpingPath(null);
       setDistanceTabel(null);
       setGlobalConstraints(true);
       setThresholdLevel(true);
    }
}
