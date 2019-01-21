package sample;

import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;

import java.util.ArrayList;

public class ImplementedEquations implements FirstOrderDifferentialEquations {

    //given current [uA/cm^2]
    private double i =0;

    private double input_i;

    private double simulationTime;
    //Membrance capacitance [uF/xm^2]
    private  double Cm = 1;

    //Sodium Ion channel variables:

    //Sodium maximum conductances [ms/cm^2]
    private   double gNa = 120;
    //Sodium Nernst reversal potentials [mV]
    private   double eNa = 50;

    //Potassium Ion channel variables:

    //Potassium maximum conductances [ms/cm^2]
    private   double gK = 36;
    //Potassium Nernst reversal potentials [mV]
    private  double eK = -77;

    //Passive leak channel varianles

    //Leak maximum conductances [mS/cm^2]
    private  double gL = -0.3;
    //Leak Nernst reversal potentials [mV]
    private  double eL = -54.387;

    private ArrayList<Double> membraneCurrentsArrayList;

    public ImplementedEquations( double simulationTime,double input_i, double cm, double gNa, double eNa, double gK, double eK, double gL, double eL) {

        this.input_i = input_i;
        this.simulationTime = simulationTime;
        this.Cm = cm;
        this.gNa = gNa;
        this.eNa = eNa;
        this.gK = gK;
        this.eK = eK;
        this.gL = gL;
        this.eL = eL;
        membraneCurrentsArrayList = new ArrayList<>();
    }

    public ImplementedEquations(double simulationEndTime, double input_i) {
        this.input_i = input_i;
        this.simulationTime = simulationEndTime;
        membraneCurrentsArrayList = new ArrayList<>();
    }

    @Override
    public int getDimension() {
        return 4;
    }

    @Override
    public void computeDerivatives(double t, double[] x, double[] dxdt) throws MaxCountExceededException, DimensionMismatchException {


        //membrane current [uA/cm^2]
        double iSum =gNa*Math.pow(x[0],3)*x[2]*(x[3]-eNa)+gK*Math.pow(x[1],4)*(x[0]-eK)+gL*(x[3]-eL);
        if (t>simulationTime*0.15){
            i=input_i;
        }
        double iC = -iSum+i;


        //m
        dxdt[0] = alphaM(x[3])*(1-x[0])-betaM(x[3])*x[0];
        //n
        dxdt[1] = alphaN(x[3])*(1-x[1])-betaN(x[3])*x[1];
        //h
        dxdt[2] = alphaH(x[3])*(1-x[2])-betaH(x[3])*x[2];
        //u
        dxdt[3] = (-(gNa*Math.pow(x[0],3)*x[2]*(x[3]-eNa)+gK*Math.pow(x[1],4)*(x[3]-eK)+gL*(x[3]-eL))+i)/Cm;
        membraneCurrentsArrayList.add(iSum);
        System.out.println(i);
    }

    //equations for a and b parameters
    //Channel gating kinetics. Functions of membrane voltage
    public double alphaM(double u){
        return (0.1*(25-u))/(Math.exp((25-u)/10)-1);
    }
    //Channel gating kinetics. Functions of membrane voltage
    public double betaM(double u){
        return 4*Math.exp(-u/18);
    }
    //Channel gating kinetics. Functions of membrane voltage
    public double alphaN(double u){
        return (0.01*(10-u))/(Math.exp((10-u)/10)-1);
    }
    //Channel gating kinetics. Functions of membrane voltage
    public double betaN(double u){
        return 0.125*Math.exp(-u/80);
    }
    //Channel gating kinetics. Functions of membrane voltage
    public double alphaH(double u){
        return 0.07*Math.exp(-u/20);
    }
    //Channel gating kinetics. Functions of membrane voltage
    public double betaH(double u){
        return (1/(Math.exp((30-u)/10)-1));
    }
    //Returns array list of membrane current
    public ArrayList<Double> getMembraneCurrentsArrayList() {
        return membraneCurrentsArrayList;
    }
}
