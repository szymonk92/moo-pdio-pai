/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sys;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author Lukasz
 */
public class Recognizer {

    public List<DTWMatch> matchList;
    private DTWMatchComparator DTWMatchComparator;
    public DTW dtw;
    public MFCC mfcc;

    public Recognizer() {
        DTWMatchComparator = new DTWMatchComparator();
        matchList = new ArrayList<DTWMatch>();
        dtw = new DTW();
        mfcc = new MFCC();
    }

    public List<Word> getWords() {
        List<Word> result = new ArrayList<Word>();
        for (DTWMatch match : matchList) {
            result.add(match.getWord());
        }
        return result;
    }

    public void setWords(List<Word> words) {
        matchList = new ArrayList<DTWMatch>();
        for (Word word : words) {
            matchList.add(new DTWMatch(word));
        }
    }

    public DTWMatch recognize(File input) {
        double[][] unknown = mfcc.compute(input);
        if (unknown == null) {
            return null;
        }
        dtw.setUnknown(unknown);
        for (DTWMatch match : matchList) {
            dtw.compute(match);
        }
        Collections.sort(matchList, DTWMatchComparator);
        return matchList.get(0);
    }
}
