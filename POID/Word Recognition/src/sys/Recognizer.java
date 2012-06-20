/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sys;

import dtw.*;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author Lukasz
 */
public class Recognizer {

    private List<DTWMatch> matchList;
    private DTWMatchComparator DTWMatchComparator;
    private DistanceImageGenerator distanceImageGenerator;
    private DTW dtw;
    private MFCC mfcc;
    public boolean processed;

    public Recognizer() {
        DTWMatchComparator = new DTWMatchComparator();
        distanceImageGenerator = new DistanceImageGenerator();
        matchList = new ArrayList<DTWMatch>();
        dtw = new DTW();
        mfcc = new MFCC();
        distanceImageGenerator.setDtw(dtw);
    }

    public List<DTWData> getWords() {
        List<DTWData> result = new ArrayList<DTWData>();
        for (DTWMatch match : matchList) {
            result.add(match.getData());
        }
        return result;
    }

    public void setWords(List<DTWData> words) {
        matchList = new ArrayList<DTWMatch>();
        for (DTWData word : words) {
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
            match.clear();
            dtw.compute(match);
        }
        Collections.sort(matchList, DTWMatchComparator);
        distanceImageGenerator.generate(matchList.get(0));
        processed = true;
        return matchList.get(0);
    }

    public DTWMatch refresh() {
         for (DTWMatch match : matchList) {
            match.clear();
            dtw.compute(match);
        }
        Collections.sort(matchList, DTWMatchComparator);
        distanceImageGenerator.generate(matchList.get(0));
        processed = true;
        return matchList.get(0);
    }

    public DTWMatchComparator getDTWMatchComparator() {
        return DTWMatchComparator;
    }

    public void setDTWMatchComparator(DTWMatchComparator DTWMatchComparator) {
        this.DTWMatchComparator = DTWMatchComparator;
    }

    public DistanceImageGenerator getDistanceImageGenerator() {
        return distanceImageGenerator;
    }

    public void setDistanceImageGenerator(DistanceImageGenerator distanceImageGenerator) {
        this.distanceImageGenerator = distanceImageGenerator;
    }

    public List<DTWMatch> getMatchList() {
        return matchList;
    }

    public void setMatchList(List<DTWMatch> matchList) {
        this.matchList = matchList;
    }

    public MFCC getMfcc() {
        return mfcc;
    }

    public void setMfcc(MFCC mfcc) {
        this.mfcc = mfcc;
    }

    public DTW getDtw() {
        return dtw;
    }

    public void setDtw(DTW dtw) {
        this.dtw = dtw;
    }
}
