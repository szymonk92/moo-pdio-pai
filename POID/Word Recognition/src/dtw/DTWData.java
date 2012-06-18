/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dtw;

import java.io.File;
import java.io.Serializable;

/**
 *
 * @author Lukasz
 */
public class DTWData implements Serializable {

    private String word;
    private double[][] mfcc;
    private int mfccLenght;
    private File file;

    public DTWData() {
    }

    public DTWData(String word, double[][] mfcc, File file) {
        this.word = word;
        this.mfcc = mfcc;
        this.mfccLenght = mfcc.length;
        this.file = file;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public int getMfccLenght() {
        return mfccLenght;
    }

    public void setMfccLenght(int mfccLenght) {
        this.mfccLenght = mfccLenght;
    }

    public double[][] getMfcc() {
        return mfcc;
    }

    public void setMfcc(double[][] mcff) {
        this.mfcc = mcff;
        this.mfccLenght = mfcc.length;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    @Override
    public String toString() {
        return this.word;
    }
    
    
}
