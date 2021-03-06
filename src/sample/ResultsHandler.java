package sample;

import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.ode.sampling.StepHandler;
import org.apache.commons.math3.ode.sampling.StepInterpolator;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Hanlde results of ODE
 */
public class ResultsHandler implements StepHandler {
    private ArrayList<Double> mValuesArray;
    private ArrayList<Double> nValuesArray;
    private ArrayList<Double> hValuesArray;
    private ArrayList<Double> uValuesArray;
    private ArrayList<Double> time;

    public ResultsHandler() {
        mValuesArray = new ArrayList<>();
        nValuesArray = new ArrayList<>();
        hValuesArray = new ArrayList<>();
        uValuesArray = new ArrayList<>();
        time = new ArrayList<>();
    }


    @Override
    public void init(double v, double[] doubles, double v1) {

    }

    @Override
    public void handleStep(StepInterpolator stepInterpolator, boolean isLast) throws MaxCountExceededException {
        double t = stepInterpolator.getCurrentTime();
        double[] x = stepInterpolator.getInterpolatedState();

        time.add(t);
        mValuesArray.add(x[0]);
        nValuesArray.add(x[1]);
        hValuesArray.add(x[2]);
        uValuesArray.add(x[3]);
        System.out.println(t + " " + Arrays.toString(x));
    }

    /**
     * Returns array of m values
     * @return m values arrat
     */
    public ArrayList<Double> getmValuesArray() {
        return mValuesArray;
    }

    /**
     * Returns array of n values
     * @return n values array
     */
    public ArrayList<Double> getnValuesArray() {
        return nValuesArray;
    }
    /**
     * Returns array of h values
     * @return h values array
     */
    public ArrayList<Double> gethValuesArray() {
        return hValuesArray;
    }
    /**
     * Returns array of u values
     * @return u values array
     */
    public ArrayList<Double> getuValuesArray() {
        return uValuesArray;
    }
    /**
     * Returns array of time values
     * @return time values array
     */
    public ArrayList<Double> getTime() {
        return time;
    }
}
