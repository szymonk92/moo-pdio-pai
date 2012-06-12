/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sys;

import filters.ExtractionFilter;
import gui.MainWindow;
import gui.ViewPanel;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import weka.core.Attribute;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;

/**
 *
 * @author Lukasz
 */
public class LerningSetProcesor extends Thread {

    private List<ViewPanel> panels;
    private String folder;
    private File dir;

    public LerningSetProcesor(List<ViewPanel> panels, String folder, File dir) {
        this.panels = panels;
        this.folder = folder;
        this.dir = dir;
    }

    @Override
    public void run() {
        if (panels != null && !panels.isEmpty()) {
            ExtractionFilter filter = new ExtractionFilter(null);
            BufferedImage image;
            SignWrapper sw;
            int count = 0;
            Random rand = new Random();
            for (ViewPanel panel : panels) {

                panel.Start();
                panel.setProcessName("Ładowanie zdjęcia");
                File imageFile = new File(folder + File.separator + panel.image);

                File xmlFile = null;
                if (panel.xmlFile != null) {
                    xmlFile = new File(folder + File.separator + panel.xmlFile);
                }
                try {
                    image = ImageIO.read(imageFile);
                    panel.SetImage(SignWrapper.resize(image, 64, 64));
                    sw = new SignWrapper(image);
                } catch (IOException ex) {

                    Logger.getLogger(MainWindow.class.getName()).log(Level.SEVERE, null, ex);
                    panel.End();
                    continue;
                }
                panel.addProgress(10);
                if (xmlFile != null) {
                    try {
                        panel.setProcessName("Ładowanie tagów");
                        sw.setXmlFile(new FileInputStream(xmlFile.getAbsolutePath()));
                        panel.setTags(sw.getSigns("znak zakazu"));
                    } catch (FileNotFoundException ex) {
                        Logger.getLogger(MainWindow.class.getName()).log(Level.SEVERE, null, ex);
                        panel.End();
                        continue;
                    }
                } else {
                    panel.End();
                    continue;
                }
                panel.addProgress(10);
                panel.setProcessName("Tworzenie regionów");
                panel.setRegions(filter.processImage(image));
                if (panel.tags != null) {
                    panel.setRegionsResult(SignWrapper.isTheSame(panel.tags, panel.regions));
                }
                panel.addProgress(30);

                panel.setProcessName("Zapisywanie");
                int progressValue = (int) (50f / panel.regions.size());
                int imageCount = 0;
                if (panel.regions != null && !panel.regions.isEmpty()) {

                    if (panel.tags != null && !panel.tags.isEmpty()) {

                        for (Rectangle region : panel.regions) {
                            boolean test = false;
                            for (Rectangle tag : panel.tags) {
                                if (SignWrapper.isTheSame(region, tag)) {
                                    test = true;
                                    break;
                                }
                            }
                            File file = new File(dir + "/image-" + count + ".png");
                            if (test) {
                                file = new File(dir + "/zakaz-" + count + ".png");
                            }
                            try {
                                ImageIO.write(sw.getSubImageFit(filter.imageRegions.get(imageCount)), "png", file);
                                count++;
                                imageCount++;
                            } catch (IOException ex) {
                                Logger.getLogger(ExtractionFilter.class.getName()).log(Level.SEVERE, null, ex);
                            }

                        }
                    } else {
                        for (BufferedImage minImage : filter.imageRegions) {
                            File file = new File(dir + "/image-" + count + ".png");
                            try {
                                ImageIO.write(sw.getSubImageFit(minImage), "png", file);
                                count++;
                            } catch (IOException ex) {
                                Logger.getLogger(ExtractionFilter.class.getName()).log(Level.SEVERE, null, ex);
                            }

                        }
                    }

                    for (int i = 0; i < 10; i++) {
                        int x = rand.nextInt(filter.inImage.getWidth());
                        int y = rand.nextInt(filter.inImage.getHeight());
                        int width = 25 + rand.nextInt(80);
                        boolean test = false;
                        Rectangle region;
                        do {
                            region = filter.getOptimalRegion(x, y, width, width);
                            if (panel.tags != null && !panel.tags.isEmpty()) {

                                for (Rectangle tag : panel.tags) {
                                    if (SignWrapper.isTheSame(region, tag)) {
                                        test = true;
                                        break;
                                    }
                                }
                            }
                        } while (test);
                        File file = new File(dir + "/image-" + count + ".png");

                        try {
                            ImageIO.write(sw.getSubImageFit(filter.produceImageRegion(region, filter.EqualizeImage)), "png", file);
                            count++;
                        } catch (IOException ex) {
                            Logger.getLogger(ExtractionFilter.class.getName()).log(Level.SEVERE, null, ex);
                        }

                    }
                    for (int i = 0; i < 10; i++) {
                        int x = rand.nextInt(filter.inImage.getWidth());
                        int y = rand.nextInt(filter.inImage.getHeight());
                        int width = 25 + rand.nextInt(80);
                        boolean test = false;
                        Rectangle region;
                        do {
                            region = filter.getOptimalRegion(x, y, width, width);
                            if (panel.tags != null && !panel.tags.isEmpty()) {

                                for (Rectangle tag : panel.tags) {
                                    if (SignWrapper.isTheSame(region, tag)) {
                                        test = true;
                                        break;
                                    }
                                }
                            }
                        } while (test);
                        File file = new File(dir + "/image-" + count + ".png");

                        try {
                            ImageIO.write(sw.getSubImageFit(filter.produceImageRegion(region, filter.inImage)), "png", file);
                            count++;
                        } catch (IOException ex) {
                            Logger.getLogger(ExtractionFilter.class.getName()).log(Level.SEVERE, null, ex);
                        }

                    }
                }
                panel.End();
            }
        }

    }

    public static Instances createInstances(List<BufferedImage> images) {
        if (images == null || images.isEmpty()) {
            return null;
        }
        Instances ret = null;
        // Declare the class attribute along with its values
        FastVector fvClassVal = new FastVector(3);
        fvClassVal.addElement("positive0"); //zakaz
        fvClassVal.addElement("positive1"); //odwo�anie ograniczenia
        fvClassVal.addElement("negative"); //ka�dy inny
        Attribute ClassAttribute = new Attribute("theClass", fvClassVal);


        BufferedImage img = images.get(0);
        FastVector vec = new FastVector(img.getHeight() * img.getWidth() + 1);
        for (int i = 0; i < img.getHeight() * img.getWidth(); ++i) {
            vec.addElement(new Attribute("attr" + i));
        }

        vec.addElement(ClassAttribute);

        ret = new Instances("data", vec, 0);

        for (int i = 0; i < images.size(); ++i) {
            img = images.get(i);
            Instance ince = new Instance(img.getHeight() * img.getWidth() + 1);

            int val = 0;
            for (int j = 0; j < img.getWidth(); ++j) {
                for (int k = 0; k < img.getHeight(); ++k) {
                    ince.setValue((Attribute) vec.elementAt(val), (double) img.getRGB(j, k));
                    val++;
                }
            }


            ret.add(ince);
        }


        ret.setClassIndex(ret.numAttributes() - 1);
        return ret;
    }
}
