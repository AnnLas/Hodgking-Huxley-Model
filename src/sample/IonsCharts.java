package sample;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class IonsCharts implements Initializable {

    @FXML
    private LineChart<Double, Double> na_chart;

    @FXML
    private LineChart<Double, Double> k_chart;

    @FXML
    private LineChart<Double, Double> l_chart;


    public void initData(ArrayList<Double> time, ArrayList<Double> na, ArrayList<Double> k, ArrayList<Double> l) {


        addDataToChart(time, na,new XYChart.Series(), na_chart);
        addDataToChart(time, k, new XYChart.Series(), k_chart);
        addDataToChart(time, l, new XYChart.Series(), l_chart);
    }


    private void addDataToChart(ArrayList<Double> x, ArrayList<Double> y, XYChart.Series series, LineChart lineChart) {
        for (int j = 0; j < x.size(); j++) {
            series.getData().add(new XYChart.Data(x.get(j), y.get(j)));
        }
        lineChart.getData().add(series);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {


    }
}
