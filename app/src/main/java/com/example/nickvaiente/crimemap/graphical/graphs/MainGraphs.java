package com.example.nickvaiente.crimemap.graphical.graphs;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.Log;

import com.example.nickvaiente.crimemap.Compare;
import com.example.nickvaiente.crimemap.QPS.QueenslandPoliceService;
import com.example.nickvaiente.crimemap.QPS.entity.offence.OffenceInfo;
import com.example.nickvaiente.crimemap.QPS.entity.offence.Result;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.mikepenz.materialize.color.Material;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.example.nickvaiente.crimemap.R.id.barChart;

public class MainGraphs {

    public MainGraphs(){}

    public void createBarChart(BarChart barChart, Compare compare) {

        Map<String,String[]> offenceType = getGraphData(compare);
        String[] keys = {"1", "8", "14", "17", "21", "27", "28", "29", "30", "35", "39", "45", "47", "51", "52", "54", "55"};
        for(String key : keys){
            if (Integer.parseInt(offenceType.get(key)[1]) == 0){
                offenceType.remove(key);
            }
        }

        //Bar Chart//
        ArrayList barData = new ArrayList();
        int index = 0;
        String[] offenceName = new String[offenceType.size()];
        for (Map.Entry<String,String[]> offence: offenceType.entrySet()){
            offenceName[index] = offence.getValue()[0];
            barData.add(new BarEntry((float)index, Float.parseFloat(offence.getValue()[1])));
            index++;
        }

        BarDataSet Crime = new BarDataSet(barData, compare.getOffenceResult().getResult().get(0).getOffenceInfo().get(0).getSuburb());
        barChart.getLegend().setEnabled(false);

        // Graph Chart
        ArrayList<IBarDataSet> dataSets = new ArrayList<>();
        dataSets.add(Crime);
        BarData Data = new BarData(dataSets);
        Data.setBarWidth(1f);
        barChart.setData(Data);

        // XAxis Settings
        XAxis xAxis = barChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setCenterAxisLabels(false);
        xAxis.setDrawGridLines(false);
        xAxis.setDrawAxisLine(false);
        xAxis.setTextSize(8f);
        xAxis.setGranularity(1f);
        xAxis.setLabelCount(17);
        xAxis.setLabelRotationAngle(-90);
        xAxis.setValueFormatter(new IndexAxisValueFormatter(offenceName));

        // Graph Settings
        Description desc = new Description();
        desc.setText("Number of Offences");     // Set Chart Description
        barChart.setDescription(desc);        //
        Crime.setColors(ColorTemplate.COLORFUL_COLORS); // Color Settings
        barChart.setPinchZoom(true);
        barChart.setDragEnabled(true);
        barChart.setDoubleTapToZoomEnabled(true);
        barChart.setDrawValueAboveBar(true);
        barChart.setEnabled(true);
        barChart.setExtraBottomOffset(100f);
        barChart.invalidate();
    }

    public void createPieChart(PieChart pieChart, Compare compare) {

        // Populate Dataillkj

        Map<String,String[]> offenceType = getGraphData(compare);
        String[] keys = {"1", "8", "14", "17", "21", "27", "28", "29", "30", "35", "39", "45", "47", "51", "52", "54", "55"};
        for(String key : keys){
            if (Integer.parseInt(offenceType.get(key)[1]) == 0){
                offenceType.remove(key);
            }
        }
        float[] yData = new float[offenceType.size()];
        String[] xData = new String[offenceType.size()];
        int index = 0;
        for (Map.Entry<String, String[]> each: offenceType.entrySet()){
            xData[index] = each.getValue()[0];
            yData[index] = Float.parseFloat(each.getValue()[1]);
            index++;
        }

        ArrayList<PieEntry> yAxis = new ArrayList<>();
        ArrayList<String> xAxis = new ArrayList<>();

        for (int i = 0; i < yData.length; i++){
            yAxis.add(new PieEntry(yData[i], i));
        }

        for (int i = 0; i < xData.length; i++){
            xAxis.add(xData[i]);
        }

        // Chart Settings
        PieDataSet dataSet = new PieDataSet(yAxis, "Number Of Offences");

        Description desc = new Description();
        desc.setText("Number of Offences");     // Set Chart DescriptMion
        pieChart.setDescription(desc);
        pieChart.setRotationEnabled(true);
        pieChart.setHoleRadius(20f);
        pieChart.setTransparentCircleAlpha(0);
        dataSet.setColors(ColorTemplate.COLORFUL_COLORS);

        PieData pieData = new PieData(dataSet);
        pieChart.setData(pieData);
        pieChart.invalidate();
    }

    public void createLineChart(final LineChart lineChart, Compare compare1, Compare compare2) {

        //Line Chart//
        // Set Data
        ArrayList<Entry> lineData1 = new ArrayList();
        ArrayList<Entry> lineData2 = new ArrayList();
        String[] dates = new String[compare1.getDates().size()];

        int count = 0;
        for (String key : compare1.getSortedKeys()) {
            Integer value = compare1.getDates().get(key);
            lineData1.add((new Entry((float) count, (float) value)));
            dates[count] = key;
            count++;
        }
        count = 0;
        for (String key : compare2.getSortedKeys()) {
            Integer value = compare2.getDates().get(key);
            lineData2.add((new Entry((float) count, (float) value)));
            count++;
        }

        LineDataSet crime = new LineDataSet(lineData1, compare1.getOffenceResult().getResult().get(0).getOffenceInfo().get(0).getSuburb());
        LineDataSet crime2 = new LineDataSet(lineData2, compare2.getOffenceResult().getResult().get(0).getOffenceInfo().get(0).getSuburb());
        crime.setColor(Color.BLUE);
        crime.setCircleColor(Color.BLUE);
        crime2.setColor(Color.RED);
        crime2.setCircleColor(Color.RED);

//        String[] legendSuburbs = {compare1.getOffenceResult().getResult().get(0).getOffenceInfo().get(0).getSuburb(), compare2.getOffenceResult().getResult().get(0).getOffenceInfo().get(0).getSuburb()};
        lineChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(dates));
//        lineChart.getLegend().setEnabled(false);
        lineChart.setExtraBottomOffset(30f);

        // Graph Chart
        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(crime);
        dataSets.add(crime2);
        LineData Data = new LineData(dataSets);
        lineChart.setData(Data);

        // XAxis Settings
        XAxis xAxis = lineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setDrawAxisLine(false);
        xAxis.setTextSize(10f);
        xAxis.setCenterAxisLabels(false);
        xAxis.setGranularity(1f);
        xAxis.setLabelRotationAngle(-90);

        // Graph Settings
        Description desc = new Description(); //
        desc.setText("Number of Offences");     // Set Chart Description
        lineChart.setDescription(desc);        //
//        crime.setColors(ColorTemplate.COLORFUL_COLORS); // Color Settings
//        crime2.setColors(ColorTemplate.JOYFUL_COLORS);
//        lineChart.getLegend().setCustom(ColorTemplate.COLORFUL_COLORS, legendSuburbs);
//        crime.setColor(Color.RED);
//        crime.setCircleColor(Color.RED - 2);
//        crime.(Color.RED - 2);
//        crime2.setColor(Color.BLUE);
//        crime2.setCircleColor(Color.BLUE - 2);
        lineChart.setPinchZoom(true);
        lineChart.setDragEnabled(true);
        lineChart.setDoubleTapToZoomEnabled(true);
        lineChart.setEnabled(true); //
        lineChart.invalidate();
    }

    public void createLineChart(final LineChart lineChart, Compare compare1) {

        //Line Chart//
        // Set Data
        ArrayList<Entry> lineData1 = new ArrayList();
        String[] dates = new String[compare1.getDates().size()];

        int count = 0;
        for (String key : compare1.getSortedKeys()) {
            Integer value = compare1.getDates().get(key);
            lineData1.add((new Entry((float) count, (float) value)));
            dates[count] = key;
            count++;
        }

        LineDataSet crime = new LineDataSet(lineData1, compare1.getOffenceResult().getResult().get(0).getOffenceInfo().get(0).getSuburb());
        crime.setColor(Color.BLUE);
        crime.setCircleColor(Color.BLUE);

//        String[] legendSuburbs = {compare1.getOffenceResult().getResult().get(0).getOffenceInfo().get(0).getSuburb(), compare2.getOffenceResult().getResult().get(0).getOffenceInfo().get(0).getSuburb()};
        lineChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(dates));
//        lineChart.getLegend().setEnabled(false);
        lineChart.setExtraBottomOffset(30f);

        // Graph Chart
        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(crime);
        LineData Data = new LineData(dataSets);
        lineChart.setData(Data);

        // XAxis Settings
        XAxis xAxis = lineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setDrawAxisLine(false);
        xAxis.setTextSize(10f);
        xAxis.setCenterAxisLabels(false);
        xAxis.setGranularity(1f);
        xAxis.setLabelRotationAngle(-90);

        // Graph Settings
        Description desc = new Description(); //
        desc.setText("Number of Offences");     // Set Chart Description
        lineChart.setDescription(desc);        //
        lineChart.setPinchZoom(true);
        lineChart.setDragEnabled(true);
        lineChart.setDoubleTapToZoomEnabled(true);
        lineChart.setEnabled(true); //
        lineChart.invalidate();
    }

    private Map<String, String[]> getGraphData(Compare compare){
        java.util.Map<String,String[]> offenceType = new HashMap<String,String[]>();
        if (compare.getOffenceResult() != null) {
            offenceType.put("1", new String[]{"Homicide", "0"});
            offenceType.put("8", new String[]{"Assault", "0"});
            offenceType.put("14",new String[]{"Robbery", "0"});
            offenceType.put("17",new String[]{"Other Offences Against People", "0"});
            offenceType.put("21",new String[]{"Unlawful Entry", "0"});
            offenceType.put("27",new String[]{"Arson", "0"});
            offenceType.put("28",new String[]{"Other Property Damage", "0"});
            offenceType.put("29",new String[]{"Unlawful Use of Motor Vehicle", "0"});
            offenceType.put("30",new String[]{"Other Theft", "0"});
            offenceType.put("35",new String[]{"Fraud", "0"});
            offenceType.put("39",new String[]{"Handling Stolen Goods", "0"});
            offenceType.put("45",new String[]{"Drug Offence", "0"});
            offenceType.put("47",new String[]{"Liquor (excl. Drunkenness)", "0"});
            offenceType.put("51",new String[]{"Weapons Act Offences", "0"});
            offenceType.put("52",new String[]{"Good Order Offence", "0"});
            offenceType.put("54",new String[]{"Traffic and Related Offences", "0"});
            offenceType.put("55",new String[]{"Other", "0"});
            for (Result result : compare.getOffenceResult().getResult()) {
                for (OffenceInfo offence : result.getOffenceInfo()) {
                    String[] value = offenceType.get(offence.getQpsOffenceCode() + "");
                    value[1] = (Integer.parseInt(value[1]) + 1) + "";
                    offenceType.put(offence.getQpsOffenceCode() + "", value);
                }
            }
        }
        return  offenceType;
    }
}

