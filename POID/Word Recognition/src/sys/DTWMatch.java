/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sys;

import gui.MatchPreview;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

/**
 *
 * @author Lukasz
 */
public class DTWMatch {
    private Word word;
    private MatchPreview view;
    private double warpingDistance;
    private int[][] warpingPath;
    private double[][] distanceTabel;
    private boolean globalConstraints;
    BufferedImage image;

    public DTWMatch(Word word) {
        this.word = word;
    }

    public boolean isGlobalConstraints() {
        return globalConstraints;
    }

    public void setGlobalConstraints(boolean localLimits) {
        this.globalConstraints = localLimits;
        UpdateView();
    }

    public BufferedImage getImage() {
        return image;
    }

    public void setImage(BufferedImage image) {
        this.image = image;
    }

    public double[][] getDistanceTabel() {
        return distanceTabel;
    }

    public void setDistanceTabel(double[][] D) {
        this.distanceTabel = D;
        image = generateDistanceImage();
        UpdateView();
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
        UpdateView();
    }

    public int[][] getWarpingPath() {
        return warpingPath;
    }

    public void setWarpingPath(int[][] warpingPath) {
        this.warpingPath = warpingPath;
        UpdateView();
    }

    public Word getWord() {
        return word;
    }

    public void setWord(Word word) {
        this.word = word;
        UpdateView();
    }

    private void UpdateView() {
        if (view == null) {
            view = new MatchPreview(this);
        }
        view.Update();
    }

    private BufferedImage generateDistanceImage() {
        BufferedImage result = new BufferedImage( distanceTabel[0].length,distanceTabel.length, BufferedImage.TYPE_3BYTE_BGR);
        double max = Double.MIN_VALUE;
        double min = Double.MAX_VALUE;
        for (int i = 0; i < distanceTabel.length; i++) {
            for (int j = 0; j < distanceTabel[0].length; j++) {
                if (max < distanceTabel[i][j]) {
                    max = distanceTabel[i][j];
                }
                if (min > distanceTabel[i][j]) {
                    min = distanceTabel[i][j];
                }
            }
        }
int jtmp = distanceTabel.length-1;
        double down = max - min;
        for (int i = 0; i < distanceTabel[0].length; i++) {
            for (int j = 0; j < distanceTabel.length; j++) {
                boolean test = false;
                int newj = jtmp-j;
                for(int[] point : this.warpingPath){
                    if(point[0] == newj && point[1] == i){
                        result.setRGB(i, j, Color.RED.getRGB());
                        test =true;
                        break;
                    }
                }
                if(test) continue;
                if(checkGlobalConstraints(i,newj,distanceTabel[0].length,distanceTabel.length)){
                     result.setRGB(i, j, Color.GREEN.getRGB());
                }
                else{
                int value = 255 - (int) (((distanceTabel[newj][i] - min) / down) * 255);
                result.setRGB(i, j, new Color(value, value, value).getRGB());
                }
            }
        }
        return result;
    }

    public static BufferedImage resize(BufferedImage image, int width,
            int height) {
        if(image == null){
            return null;
        }
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
}
