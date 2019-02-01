package sample;

import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;

import java.util.ArrayList;

public class ImplementedEquations implements FirstOrderDifferentialEquations {

    //given current [uA/cm^2]
    private double i = 0;

    private double input_i;

    private double simulationTime;
    //Membrance capacitance [uF/xm^2]
    private double Cm = 1;

    //Sodium Ion channel variables:

    //Sodium maximum conductances [ms/cm^2]
    private double gNa = 120;
    //Sodium Nernst reversal potentials [mV]
    private double eNa = 115;

    //Potassium Ion channel variables:

    //Potassium maximum conductances [ms/cm^2]
    private double gK = 36;
    //Potassium Nernst reversal potentials [mV]
    private double eK = -12;

    //Passive leak channel varianles

    //Leak maximum conductances [mS/cm^2]
    private double gL = 0.3;
    //Leak Nernst reversal potentials [mV]
    private double eL = 10.6;


    private ArrayList<ArrayList<Double>> membraneCurrentsArrayList;
    private ArrayList<Double> naCurrents;
    private ArrayList<Double> kCurrents;
    private ArrayList<Double> lCurrents;
    private ArrayList<Double> currentsSum;

    public ImplementedEquations(double simulationTime, double input_i, double cm, double gNa, double eNa, double gK, double eK, double gL, double eL) {

        this.input_i = input_i;
        this.simulationTime = simulationTime;
        this.Cm = cm;
        this.gNa = gNa;
        this.eNa = eNa;
        this.gK = gK;
        this.eK = eK;
        this.gL = gL;
        this.eL = eL;


        naCurrents = new ArrayList<>();
        kCurrents = new ArrayList<>();
        lCurrents = new ArrayList<>();
        currentsSum = new ArrayList<>();
        membraneCurrentsArrayList = new ArrayList<>();

        membraneCurrentsArrayList.add(currentsSum);
        membraneCurrentsArrayList.add(naCurrents);
        membraneCurrentsArrayList.add(kCurrents);
        membraneCurrentsArrayList.add(lCurrents);

    }



    @Override
    public int getDimension() {
        return 4;
    }

    @Override
    public void computeDerivatives(double t, double[] x, double[] dxdt) throws MaxCountExceededException, DimensionMismatchException {


        //membrane current [uA/cm^2]
        double iSum = gNa * Math.pow(x[0], 3) * x[2] * (x[3] - eNa) + gK * Math.pow(x[1], 4) * (x[0] - eK) + gL * (x[3] - eL);
        if (t > simulationTime * 0.15) {
            i = input_i;
        }



        //m
        dxdt[0] = alphaM(x[3]) * (1 - x[0]) - betaM(x[3]) * x[0];
        //n
        dxdt[1] = alphaN(x[3]) * (1 - x[1]) - betaN(x[3]) * x[1];
        //h
        dxdt[2] = alphaH(x[3]) * (1 - x[2]) - betaH(x[3]) * x[2];
        //u
        dxdt[3] = (-(gNa * Math.pow(x[0], 3) * x[2] * (x[3] - eNa) + gK * Math.pow(x[1], 4) * (x[3] - eK) + gL * (x[3] - eL)) + i) / Cm;
        currentsSum.add(iSum);
        naCurrents.add(gNa * Math.pow(x[0], 3) * x[2] * (x[3] - eNa));
        kCurrents.add(gK * Math.pow(x[1], 4) * (x[0] - eK));
        lCurrents.add(gL * (x[3] - eL));

    }

    //equations for a and b parameters
    //Channel gating kinetics. Functions of membrane voltage
    public double alphaM(double u) {

        return (0.1 * (25.0 - u)) / (Math.exp((25.0 - u) / 10.0) - 1);
    }

    //Channel gating kinetics. Functions of membrane voltage
    public double betaM(double u) {

        return 4 * Math.exp(-u / 18.0);
    }

    //Channel gating kinetics. Functions of membrane voltage
    public double alphaN(double u) {
        return (0.01 * (10.0 - u)) / (Math.exp(1.0 - (0.1 * u)) - 1.0);
    }

    //Channel gating kinetics. Functions of membrane voltage
    public double betaN(double u) {
        return 0.125 * Math.exp(-u / 80.0);
    }

    //Channel gating kinetics. Functions of membrane voltage
    public double alphaH(double u) {
        return 0.07 * Math.exp(-u / 20.0);
    }

    //Channel gating kinetics. Functions of membrane voltage
    public double betaH(double u) {
        return (1.0 / (Math.exp((30.0 - u) / 10.0) + 1));
    }

    //Returns array list of membrane current
    public ArrayList<ArrayList<Double>> getMembraneCurrentsArrayList() {
        return membraneCurrentsArrayList;
    }
}
