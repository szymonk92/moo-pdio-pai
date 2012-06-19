/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sys;

import java.io.File;

/**
 *
 * @author Lukasz
 */
public class WavCorrection {

    private double[] silenceRemovedSignal;//output
    private int samplingRate;
    private int firstSamples;
    private int samplePerFrame;

    public WavCorrection() {
    }

    private double[] doEndPointDetection(double[] originalSignal) {
        if (originalSignal == null) {
            return new double[0];
        }
        // for identifying each sample whether it is voiced or unvoiced
        float[] voiced = new float[originalSignal.length];
        float sum = 0;
        double sd, m;
        // 1. calculation of mean
        for (int i = 0; i < firstSamples; i++) {
            sum += originalSignal[i];
        }
        m = sum / firstSamples;// mean
        sum = 0;// reuse var for S.D.

        // 2. calculation of Standard Deviation
        for (int i = 0; i < firstSamples; i++) {
            sum += Math.pow((originalSignal[i] - m), 2);
        }
        sd = Math.sqrt(sum / firstSamples);
        // 3. identifying one-dimensional Mahalanobis distance function
        // i.e. |x-u|/s greater than ####3 or not,
        for (int i = 0; i < originalSignal.length; i++) {
            if ((Math.abs(originalSignal[i] - m) / sd) > 3) { //0.3 =THRESHOLD.. adjust value yourself
                voiced[i] = 1;
            } else {
                voiced[i] = 0;
            }
        }
        // 4. calculation of voiced and unvoiced signals
        // mark each frame to be voiced or unvoiced frame
        int frameCount = 0;
        int usefulFramesCount = 1;
        int count_voiced, count_unvoiced;
        int voicedFrame[] = new int[originalSignal.length / samplePerFrame];
        // the following calculation truncates the remainder
        int loopCount = originalSignal.length - (originalSignal.length % samplePerFrame);
        for (int i = 0; i < loopCount; i += samplePerFrame) {
            count_voiced = 0;
            count_unvoiced = 0;
            for (int j = i; j < i + samplePerFrame; j++) {
                if (voiced[j] == 1) {
                    count_voiced++;
                } else {
                    count_unvoiced++;
                }
            }
            if (count_voiced > count_unvoiced) {
                usefulFramesCount++;
                voicedFrame[frameCount++] = 1;
            } else {
                voicedFrame[frameCount++] = 0;
            }
        }
        // 5. silence removal
        silenceRemovedSignal = new double[usefulFramesCount * samplePerFrame];
        int k = 0;
        for (int i = 0; i < frameCount; i++) {
            if (voicedFrame[i] == 1) {
                for (int j = i * samplePerFrame; j < i * samplePerFrame + samplePerFrame; j++) {
                    silenceRemovedSignal[k++] = originalSignal[j];
                }
            }
        }
        // end
        return silenceRemovedSignal;
    }

    public void rewriteWaveFile(File input) {
        double[] newSound = doEndPointDetection(readWavFile(input));
        try {
            int sampleRate = 44100;
            long numFrames = newSound.length;
            WavFile wavFile = WavFile.newWavFile(new File(input.getAbsolutePath()), 1, numFrames, 16, sampleRate);
            wavFile.writeFrames(newSound, newSound.length);
            wavFile.close();
        } catch (Exception e) {
            System.err.println(e);
        }
    }

    private double[] readWavFile(File input) {
        double[] result = null;
        try {
            WavFile wavFile = WavFile.openWavFile(input);
            int numChannels = wavFile.getNumChannels();
            this.samplingRate = (int) wavFile.getSampleRate();
            samplePerFrame = this.samplingRate / 1000;
            firstSamples = samplePerFrame * 200;// according to formula
            result = new double[(int) (wavFile.getNumFrames() * numChannels)];
            wavFile.readFrames(result, (int) wavFile.getNumFrames());
            if (numChannels > 1) {
                double[] newResult = new double[(int) wavFile.getNumFrames()];
                int count = 0;
                for (int i = 0; i < result.length; i++) {
                    if (i % numChannels == 0) {
                        newResult[count] = result[i];
                        count++;
                    }
                }
                result = newResult;
            }
            wavFile.close();
        } catch (Exception e) {
            System.err.println(e);
        }
        return result;
    }
}