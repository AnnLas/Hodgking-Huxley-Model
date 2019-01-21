package sample;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.input.MouseEvent;
import org.apache.commons.math3.ode.FirstOrderIntegrator;
import org.apache.commons.math3.ode.nonstiff.EulerIntegrator;

import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    private static final double u0 = 0;
    private double simulationEndTime = 50;
    //Initial values for calculations
    private double infinityM;
    private double infinityN;
    private double infinityH;


    //Data series for charts
    private XYChart.Series mStabilitySeries;
    private XYChart.Series nStabilitySeries;
    private XYChart.Series hStabilitySeries;
    private XYChart.Series nSeries;
    private XYChart.Series mSeries;
    private XYChart.Series hSeries;
    private XYChart.Series uSeries;
    private XYChart.Series membraneCurrentSeries;

    private ResultsHandler resultsHandler;
    private SignalParameters statistics;


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
    private LineChart<Integer, Double> stability_chart;

    @FXML
    private Label peaks_frequency_label;

    @FXML
    private Label peaks_mean_label;

    @FXML
    private Label peaks_max_label;

    @FXML
    private Label peaks_std_dev_label;

    @FXML
    private Label i_label;

    @FXML
    private Slider i_value_slider;

    @FXML
    private Label simulation_time_label;

    @FXML
    private Slider simulation_time_slider;

    @FXML
    private Label c_parameter_text;

    @FXML
    private Slider c_parameter_slider;

    @FXML
    private Label ENa_parameter_text;

    @FXML
    private Slider ENa_parameter_slider;

    @FXML
    private Label EK_parameter_text;

    @FXML
    private Slider EK_parameter_slider;

    @FXML
    private Label EL_parameter_text;

    @FXML
    private Slider EL_parameter_slider;

    @FXML
    private Label gNa_parameter_text;

    @FXML
    private Slider gNa_parameter_slider;

    @FXML
    private Label gK_parameter_text;

    @FXML
    private Slider gK_parameter_slider;

    @FXML
    private Label gL_parameter_text;

    @FXML
    private Slider gL_parameter_slider;


    @FXML
    void simulate(MouseEvent event) {
        clearData();
        calculateModel();
        showStats(statistics);
        addDataToCharts();

    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initLabelsAndSliders();
        seriesInit();
        chartInit();
        calculateModel();
        showStats(statistics);
        addDataToCharts();
        dependencyBetweenUAndIonsStability();

    }

    private void initLabelsAndSliders() {
        i_label.setText(String.format("I:%.2f",i_value_slider.getValue()));
        simulation_time_label.setText(String.format("simulation time:%.2f",simulation_time_slider.getValue()));
        c_parameter_text.setText(String.format("C:%.2f[uF/cm^2]",c_parameter_slider.getValue()));
        EL_parameter_text.setText(String.format("EL:%.2f[mV]",EL_parameter_slider.getValue()));
        EK_parameter_text.setText(String.format("EK:%.2f[mV]",EK_parameter_slider.getValue()));
        ENa_parameter_text.setText(String.format("ENa:%.2f[mV]",ENa_parameter_slider.getValue()));
        gNa_parameter_text.setText(String.format("gNa:%.2f[mS/cm^2]",gNa_parameter_slider.getValue()));
        gK_parameter_text.setText(String.format("gK:%.2f[mS/cm^2]",gK_parameter_slider.getValue()));
        gL_parameter_text.setText(String.format("gL:%.2f[mS/cm^2]",gL_parameter_slider.getValue()));



        i_value_slider.valueProperty().addListener((observableValue, number, t1) ->
                i_label.setText(String.format("I:%.2f[uA/cm^2]",i_value_slider.getValue())));
        simulation_time_slider.valueProperty().addListener((observableValue, number, t1) ->
                simulation_time_label.setText(String.format("simulation time:%.2f[ms]",simulation_time_slider.getValue())));
        c_parameter_slider.valueProperty().addListener((observableValue, number, t1) ->
                c_parameter_text.setText(String.format("C:%.2f[uF/cm^2]",c_parameter_slider.getValue())));
        EL_parameter_slider.valueProperty().addListener((observableValue, number, t1) ->
                EL_parameter_text.setText(String.format("EL:%.2f[mV]",EL_parameter_slider.getValue())));
        EK_parameter_slider.valueProperty().addListener((observableValue, number, t1) ->
                EK_parameter_text.setText(String.format("EK:%.2f[mV]",EK_parameter_slider.getValue())));
        ENa_parameter_slider.valueProperty().addListener((observableValue, number, t1) ->
                ENa_parameter_text.setText(String.format("ENa:%.2f[mV]",ENa_parameter_slider.getValue())));
        gK_parameter_slider.valueProperty().addListener((observableValue, number, t1) ->
                gK_parameter_text.setText(String.format("gK:%.2f[mS/cm^2]",gK_parameter_slider.getValue())));
        gNa_parameter_slider.valueProperty().addListener((observableValue, number, t1) ->
                gNa_parameter_text.setText(String.format("gNa:%.2f[mS/cm^2]",gNa_parameter_slider.getValue())));
        gL_parameter_slider.valueProperty().addListener((observableValue, number, t1) ->
                gL_parameter_text.setText(String.format("gL:%.2f[mS/cm^2]",gL_parameter_slider.getValue())));



    }

    private void seriesInit() {
        mSeries = new XYChart.Series();
        nSeries = new XYChart.Series();
        hSeries = new XYChart.Series();
        uSeries = new XYChart.Series();
        mStabilitySeries = new XYChart.Series();
        nStabilitySeries = new XYChart.Series();
        hStabilitySeries = new XYChart.Series();
        membraneCurrentSeries = new XYChart.Series();
    }


    private void chartInit() {
        m_chart.setCreateSymbols(false);
        n_chart.setCreateSymbols(false);
        h_chart.setCreateSymbols(false);
        u_chart.setCreateSymbols(false);
        ions_chart.setCreateSymbols(false);
        ions_chart.getData().add(membraneCurrentSeries);
    }


    private void showStats(SignalParameters statistics) {
        peaks_frequency_label.setText(String.format("%.2f[Hz]",statistics.getFrequency()));
        peaks_max_label.setText(String.format("%.2f[mV]",statistics.getMax()));
        peaks_std_dev_label.setText(String.format("%.2f[mV]",statistics.getStdDeviation()));
        peaks_mean_label.setText(String.format("%.2f[mV]",statistics.getMean()));
    }


    public void dependencyBetweenUAndIonsStability() {
        for (int u = -50; u < 150; u++) {
            calculateIonsStability(u);
            if (!Double.isNaN(calculateIonsStability(u)[0]) && !Double.isNaN(calculateIonsStability(u)[1]) && !Double.isNaN(calculateIonsStability(u)[2])) {
                mStabilitySeries.getData().add(new XYChart.Data(u, calculateIonsStability(u)[0]));
                nStabilitySeries.getData().add(new XYChart.Data(u, calculateIonsStability(u)[1]));
                hStabilitySeries.getData().add(new XYChart.Data(u, calculateIonsStability(u)[2]));
            }
        }
        mStabilitySeries.setName("m gated parameter");
        nStabilitySeries.setName("n gated parameter");
        hStabilitySeries.setName("h gated parameter");

        stability_chart.getData().add(mStabilitySeries);
        stability_chart.getData().add(nStabilitySeries);
        stability_chart.getData().add(hStabilitySeries);

        stability_chart.setCreateSymbols(false);

    }


    public double[] calculateIonsStability(double u0) {
        double m = implementedEquations.alphaM(u0) / (implementedEquations.alphaM(u0) + implementedEquations.betaM(u0));
        double n = implementedEquations.alphaN(u0) / (implementedEquations.alphaN(u0) + implementedEquations.betaN(u0));
        double h = implementedEquations.alphaH(u0) / (implementedEquations.alphaH(u0) + implementedEquations.betaH(u0));
        return new double[]{m, n, h};
    }

    private void clearData() {
        m_chart.getData().clear();
        n_chart.getData().clear();
        h_chart.getData().clear();
        u_chart.getData().clear();
        ions_chart.getData().clear();
        seriesInit();

    }

    private void calculateModel() {
        implementedEquations = new ImplementedEquations(simulation_time_slider.getValue(), i_value_slider.getValue());
//        implementedEquations = new ImplementedEquations(simulation_time_slider.getValue(), i_value_slider.getValue(),
//                c_parameter_slider.getValue(),gNa_parameter_slider.getValue(),ENa_parameter_slider.getValue(),gK_parameter_slider.getValue(),
//                EK_parameter_slider.getValue(),gL_parameter_slider.getValue(),EL_parameter_slider.getValue());
        FirstOrderIntegrator erIntegrator = new EulerIntegrator(0.01);
        resultsHandler = new ResultsHandler();
        erIntegrator.addStepHandler(resultsHandler);
        infinityM = calculateIonsStability(u0)[0];
        infinityN = calculateIonsStability(u0)[1];
        infinityH = calculateIonsStability(u0)[2];
        double[] xStart = {infinityM, infinityN, infinityH, u0};
        erIntegrator.integrate(implementedEquations, 0, xStart, simulation_time_slider.getValue(), xStart);
        statistics = new SignalParameters(resultsHandler.getuValuesArray(), resultsHandler.getTime());
    }

    private void addDataToCharts() {
        for (int j = 0; j < resultsHandler.getTime().size(); j++) {
            if (!Double.isNaN(resultsHandler.getmValuesArray().get(j))) {
                mSeries.getData().add(new XYChart.Data(resultsHandler.getTime().get(j), resultsHandler.getmValuesArray().get(j)));
            }
            if (!Double.isNaN(resultsHandler.getnValuesArray().get(j))) {
                nSeries.getData().add(new XYChart.Data(resultsHandler.getTime().get(j), resultsHandler.getnValuesArray().get(j)));
            }
            if (!Double.isNaN(resultsHandler.gethValuesArray().get(j))) {
                hSeries.getData().add(new XYChart.Data(resultsHandler.getTime().get(j), resultsHandler.gethValuesArray().get(j)));
            }
            if (!Double.isNaN(resultsHandler.getuValuesArray().get(j))) {
                uSeries.getData().add(new XYChart.Data(resultsHandler.getTime().get(j), resultsHandler.getuValuesArray().get(j)));
            }
            if (!Double.isNaN(implementedEquations.getMembraneCurrentsArrayList().get(j))) {
                membraneCurrentSeries.getData().add(new XYChart.Data(resultsHandler.getTime().get(j), implementedEquations.getMembraneCurrentsArrayList().get(j)));
            }

        }
        m_chart.getData().add(mSeries);
        n_chart.getData().add(nSeries);
        h_chart.getData().add(hSeries);
        u_chart.getData().add(uSeries);
        ions_chart.getData().add(membraneCurrentSeries);


    }
}
