package ru.maslennikov.thirdProject.SensorApp.util;

import org.knowm.xchart.BitmapEncoder;
import org.knowm.xchart.QuickChart;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.XYChart;
import ru.maslennikov.thirdProject.SensorApp.client.MeasurementGet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DrawGraf {

    public static void main(String[] args) {
        MeasurementGet measurementGet = new MeasurementGet();
        List<Map<String, Object>> measurements = measurementGet.getMeasurements();

        if (measurements == null || measurements.isEmpty()) {
            System.out.println("Нет данных для построения графика.");
            return;
        }

        List<Float> values = new ArrayList<>();
        for (Map<String, Object> measurement : measurements) {
            values.add((Float) measurement.get("value"));
        }

        List<Integer> length = new ArrayList<>();
        for (int i = 0; i < measurements.size(); i++) {
            length.add(i);
        }

        XYChart chart = QuickChart.getChart("График температур", "X", "Y", "y(x)", length, values);
        new SwingWrapper<>(chart).displayChart();

        try {
            BitmapEncoder.saveBitmap(chart, "./chart.png", BitmapEncoder.BitmapFormat.PNG);
        } catch (IOException e) {
            System.err.println("Ошибка сохранения графика: " + e.getMessage());
        }
    }
}
