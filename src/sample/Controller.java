package sample;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import org.apache.commons.math3.ode.FirstOrderIntegrator;
import org.apache.commons.math3.ode.nonstiff.EulerIntegrator;

import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    private static final double u0 = 0;
    private double simulationEndTime = 50;
    private double infinityM;
    private double infinityN;
    private double infinityH;
    private ImplementedEquations implementedEquations;


    @FXML
    private LineChart<Double, Double> m_chart;

    @FXML
    private LineChart<Double, Double> n_chart;

    @FXML
    private LineChart<Double, Double> h_chart;

    @FXML
    private LineChart<Double, Double> u_chart;

    @FXML
    private LineChart<Double, Double> ions_chart;

    @FXML
    private LineChart<Double, Double> stability_chart;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        implementedEquations = new ImplementedEquations(simulationEndTime);
        FirstOrderIntegrator erIntegrator = new EulerIntegrator(0.01);
        ResultsHandler resultsHandler = new ResultsHandler();
        erIntegrator.addStepHandler(resultsHandler);

        infinityM = calculateIonsStability(u0)[0];
        infinityN = calculateIonsStability(u0)[1];
        infinityH = calculateIonsStability(u0)[2];


        double[] xStart = {infinityM, infinityN,infinityH,u0};
        erIntegrator.integrate(implementedEquations,0,xStart,simulationEndTime,xStart);

        XYChart.Series mSeries = new XYChart.Series();
        XYChart.Series nSeries = new XYChart.Series();
        XYChart.Series hSeries = new XYChart.Series();
        XYChart.Series uSeries = new XYChart.Series();
        XYChart.Series mStabilitySeries = new XYChart.Series();
        XYChart.Series nStabilitySeries = new XYChart.Series();
        XYChart.Series hStabilitySeries = new XYChart.Series();
        XYChart.Series membraneCurrentSeries = new XYChart.Series();

        for (int j = 0; j < resultsHandler.getTime().size(); j++) {
            mSeries.getData().add(new XYChart.Data(resultsHandler.getTime().get(j), resultsHandler.getmValuesArray().get(j)));
            nSeries.getData().add(new XYChart.Data(resultsHandler.getTime().get(j), resultsHandler.getnValuesArray().get(j)));
            hSeries.getData().add(new XYChart.Data(resultsHandler.getTime().get(j), resultsHandler.gethValuesArray().get(j)));
            uSeries.getData().add(new XYChart.Data(resultsHandler.getTime().get(j), resultsHandler.getuValuesArray().get(j)));
            membraneCurrentSeries.getData().add(new XYChart.Data(resultsHandler.getTime().get(j), implementedEquations.getMembraneCurrentsArrayList().get(j)));
        }
        m_chart.setCreateSymbols(false);
        n_chart.setCreateSymbols(false);
        h_chart.setCreateSymbols(false);
        u_chart.setCreateSymbols(false);
        ions_chart.setCreateSymbols(false);
        m_chart.getData().add(mSeries);
        n_chart.getData().add(nSeries);
        h_chart.getData().add(hSeries);
        u_chart.getData().add(uSeries);
        ions_chart.getData().add(membraneCurrentSeries);
    }
    public void dependencyBetweenUAndIonsStability(){
        for (int u=-50; u<150; u++){
            calculateIonsStability(u);

        }

    }
    public double [] calculateIonsStability(double u0){

           double m = implementedEquations.alphaM(u0)/(implementedEquations.alphaM(u0)+implementedEquations.betaM(u0));
           double n= implementedEquations.alphaN(u0)/(implementedEquations.alphaN(u0)+implementedEquations.betaN(u0));
           double h = implementedEquations.alphaH(u0)/(implementedEquations.alphaH(u0)+implementedEquations.betaH(u0));
        return new double[]{m,n,h};
    }
}
