package sample;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

import java.util.ArrayList;

/**
 * Calculates parameters of the given signal
 */
public class SignalParameters {

    private DescriptiveStatistics statistics;
    private double max;
    private double mean;
    private double stdDeviation;
    private double frequency;

    public SignalParameters(ArrayList<Double> signal, ArrayList<Double> time) {
        statistics = new DescriptiveStatistics();
        ArrayList<Double> peaks = new ArrayList<>();
        ArrayList<Double> peaksTime = new ArrayList<>();
        ArrayList<Double> signalDerivative = new ArrayList<>();
        for (int i = 1; i < signal.size(); i++) {
            signalDerivative.add(signal.get(i) - signal.get(i - 1));
        }
        time.remove(time.size() - 1);
        for (int i = 1; i < signalDerivative.size(); i++) {
            if (signalDerivative.get(i) <= 0 && signalDerivative.get(i - 1) > 0) {
                peaks.add(signalDerivative.get(i - 1));
                peaksTime.add(time.get(i - 1));
                statistics.addValue(signal.get(i - 1));
            }

        }

        frequency = (1 / ((peaksTime.get(peaksTime.size() - 1) - peaksTime.get(0)) / peaks.size())) * 1000;
        max = statistics.getMax();
        mean = statistics.getMean();
        stdDeviation = statistics.getStandardDeviation();
    }

    /**
     * Returns max value of the signal
     * @return max value of the peak
     */
    public double getMax() {
        return max;
    }

    /**
     * Returns peaks mean of the signal
     * @return peaks mean value
     */
    public double getMean() {
        return mean;
    }

    /**
     * Returns standard deviation of signal peaks
     * @return standard deviation
     */
    public double getStdDeviation() {
        return stdDeviation;
    }

    /**
     * Returns frequency of peaks generating
     * @return frequency of the signal
     */
    public double getFrequency() {
        return frequency;
    }
}
