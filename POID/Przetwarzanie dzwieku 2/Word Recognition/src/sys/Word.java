/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sys;

import java.io.Serializable;

/**
 *
 * @author Lukasz
 */
public class Word implements Serializable  {
    public String word;
    public double[][] mcff;
    public Word(){
        
    }

    public Word(String word, double[][] mcff) {
        this.word = word;
        this.mcff = mcff;
    }

    public double[][] getMcff() {
        return mcff;
    }

    public void setMcff(double[][] mcff) {
        this.mcff = mcff;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

}
