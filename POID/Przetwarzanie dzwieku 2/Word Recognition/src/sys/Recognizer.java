/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sys;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Lukasz
 */
public class Recognizer {

    public List<Word> words;
    public DTW dtw;
    public MFCC mfcc;

    public List<Word> getWords() {
        return words;
    }

    public void setWords(List<Word> words) {
        this.words = words;
    }

    public Recognizer() {
        words = new ArrayList<Word>();
        dtw = new DTW();
        mfcc = new MFCC();
    }

    public Word recognize(File input) {
        double[][] unknown = mfcc.compute(input);
        if (unknown == null) {
            return new Word();
        }
        List<Double> distance = new ArrayList<Double>();
        for (Word word : words) {
            distance.add(dtw.compute(unknown, word.mcff));
        }
        int index = 0;
        double min = Double.MAX_VALUE;
        for (int i = 0; i < distance.size(); i++) {
            if (distance.get(i) < min) {
                min = distance.get(i);
                index = i;
            }
        }
        return words.get(index);
    }
}
