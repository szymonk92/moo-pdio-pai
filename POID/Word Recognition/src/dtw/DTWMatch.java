/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dtw;

import gui.MatchPreview;
import java.awt.AlphaComposite;
import java.awt.Color;
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

    

    public static BufferedImage resize(BufferedImage originalImage, int height) {
        double scale = ((double) (height)) / originalImage.getHeight();
        int width = (int) (originalImage.getWidth() * scale);
        BufferedImage resizedImage = new BufferedImage(width, height, originalImage.getType());
        Graphics2D g = resizedImage.createGraphics();
        g.setComposite(AlphaComposite.Src);
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.drawImage(originalImage, 0, 0, width, height, null);
        g.dispose();
        return resizedImage;
    }

    private boolean test(int i, int j, int I, int J, int r) {
        return (Math.abs((i * ((double) I / (double) J)) - j) <= r);
    }

    private boolean checkGlobalConstraints(int i, int j, int I, int J) {

        if (j < 2 * (i - I) + J) {
            return false;
        }
        if (j < 0.5 * (i - 1) + 1) {
            return false;
        }
        if (j > 2 * (i - 1) + 1) {
            return false;
        }
        if (j > 0.5 * (i - I) + J) {
            return false;
        }
        return true;
    }

    public void clear() {
       setImage(null);
       setWarpingDistance(Double.MAX_VALUE);
       setWarpingPath(null);
       setDistanceTabel(null);
       setGlobalConstraints(true);
    }
}
