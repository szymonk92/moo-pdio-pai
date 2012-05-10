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
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.core.*;

/**
 *
 * @author Lukasz
 */
public class ThreadProcesor extends Thread {

    private List<ViewPanel> panels;
    private String folder;
    private File wekaModel;

    public ThreadProcesor(List<ViewPanel> panels, String folder, File wekaModel) {
        this.panels = panels;
        this.folder = folder;
        this.wekaModel = wekaModel;
    }

    @Override
    public void run() {
        if (panels != null && !panels.isEmpty()) {
            ExtractionFilter filter = new ExtractionFilter();
            BufferedImage image;
            SignWrapper sw;
            for (ViewPanel panel : panels) {

                panel.Start();
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
                    break;
                }

                if (xmlFile != null) {
                    try {
                        sw.setXmlFile(new FileInputStream(xmlFile.getAbsolutePath()));
                        panel.setTags(sw.getSigns("znak zakazu"));
                    } catch (FileNotFoundException ex) {
                        Logger.getLogger(MainWindow.class.getName()).log(Level.SEVERE, null, ex);
                        break;
                    }
                }
                panel.setRegions(filter.processImage(image));
                if (panel.tags != null) {
                    panel.setRegionsResult(SignWrapper.isTheSame(panel.tags, panel.regions));
                }
                Classifier classify = null;
                int[] features = null;
                try {

                    classify = (Classifier) SerializationHelper.read(wekaModel.getAbsolutePath());
                    String baseFileName = wekaModel.getName().split("\\.")[0];
                    ObjectInputStream oi = new ObjectInputStream(new BufferedInputStream(new FileInputStream(wekaModel.getAbsolutePath().substring(0, wekaModel.getAbsolutePath().lastIndexOf(File.separator) + 1) + baseFileName + ".features")));
                    features = (int[]) oi.readObject();
                    oi.close();

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    break;
                } catch (IOException e) {
                    e.printStackTrace();
                    break;
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                    break;
                } catch (Exception e) {
                    e.printStackTrace();
                    break;
                }
                List<Rectangle> found = new ArrayList<Rectangle>();
                Instances testData = createInstances(sw.getSubImagesFit(panel.regions));
                if (testData != null) {
                    try {
                        //Instances labeled = new Instances(testData);
                        // label instances
                        for (int i = 0; i < testData.numInstances(); i++) {
                            double clsLabel = classify.classifyInstance(testData.instance(i));
                            //labeled.instance(i).setClassValue(clsLabel);
                            if (clsLabel != 2) {
                                found.add(panel.regions.get(i));
                            }
                        }
                    } catch (Exception ex) {
                        Logger.getLogger(ThreadProcesor.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                panel.setFound(found);
                if (panel.tags != null) {
                    panel.setFoundResult(SignWrapper.isTheSame(panel.tags, panel.found));
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
