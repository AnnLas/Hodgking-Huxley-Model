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
        ImplementedEquations implementedEquations = new ImplementedEquations(simulationEndTime);
        FirstOrderIntegrator erIntegrator = new EulerIntegrator(0.01);
        ResultsHandler resultsHandler = new ResultsHandler();
        erIntegrator.addStepHandler(resultsHandler);

        infinityM = implementedEquations.alphaM(u0)/(implementedEquations.alphaM(u0)+implementedEquations.betaM(u0));
        infinityN = implementedEquations.alphaN(u0)/(implementedEquations.alphaN(u0)+implementedEquations.betaN(u0));
        infinityH = implementedEquations.alphaH(u0)/(implementedEquations.alphaH(u0)+implementedEquations.betaH(u0));


        double[] xStart = {infinityM, infinityN,infinityH,u0};
        erIntegrator.integrate(implementedEquations,0,xStart,simulationEndTime,xStart);

        XYChart.Series mSeries = new XYChart.Series();
        XYChart.Series nSeries = new XYChart.Series();
        XYChart.Series hSeries = new XYChart.Series();
        XYChart.Series uSeries = new XYChart.Series();

        for (int j = 0; j < resultsHandler.getTime().size(); j++) {
            mSeries.getData().add(new XYChart.Data(resultsHandler.getTime().get(j), resultsHandler.getmValuesArray().get(j)));
            nSeries.getData().add(new XYChart.Data(resultsHandler.getTime().get(j), resultsHandler.getnValuesArray().get(j)));
            hSeries.getData().add(new XYChart.Data(resultsHandler.getTime().get(j), resultsHandler.gethValuesArray().get(j)));
            uSeries.getData().add(new XYChart.Data(resultsHandler.getTime().get(j), resultsHandler.getuValuesArray().get(j)));
        }
        m_chart.setCreateSymbols(false);
        n_chart.setCreateSymbols(false);
        h_chart.setCreateSymbols(false);
        u_chart.setCreateSymbols(false);
        m_chart.getData().add(mSeries);
        n_chart.getData().add(nSeries);
        h_chart.getData().add(hSeries);
        u_chart.getData().add(uSeries);
    }
}
