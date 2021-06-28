package com.skripsi.apmodasi.ui.activity.kader;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.Toast;

import com.skripsi.apmodasi.R;
import com.skripsi.apmodasi.ui.activity.bunda.DetailBayiActivity;

import java.util.ArrayList;
import java.util.List;

import lecho.lib.hellocharts.listener.LineChartOnValueSelectListener;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.view.LineChartView;

public class DetailBayiKaderActivity extends AppCompatActivity {

    private LineChartView chart_tb;
    private LineChartView chart_bb;

    String[] axisData = {"Jan", "Feb", "Mar", "Apr", "Mey", "Jun", "Jul", "Agu", "Sep", "Okt", "Nov", "Des"};
    int[] yAxisData = {50, 30, 60, 50, 70, 75, 80, 78, 85, 90, 89, 91};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_bayi_kader);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        chart_tb = (LineChartView) findViewById(R.id.chart_tb);
        chart_bb = (LineChartView) findViewById(R.id.chart_bb);

        List yAxisValues = new ArrayList();
        List axisValues = new ArrayList();




        Line line = new Line(yAxisValues).setColor(Color.parseColor("#9C27B0"));

        for (int i = 0; i < axisData.length; i++) {
            axisValues.add(i, new AxisValue(i).setLabel(axisData[i]));
        }

        for (int i = 0; i < yAxisData.length; i++) {
            yAxisValues.add(new PointValue(i, yAxisData[i]));
        }

        List lines = new ArrayList();
        lines.add(line);

        LineChartData data = new LineChartData();
        data.setLines(lines);

        Axis axis = new Axis();
        axis.setValues(axisValues);
        axis.setTextSize(16);
        axis.setTextColor(Color.parseColor("#03A9F4"));
        data.setAxisXBottom(axis);

        Axis yAxis = new Axis();
        yAxis.setTextColor(Color.parseColor("#03A9F4"));
        yAxis.setTextSize(16);
        data.setAxisYLeft(yAxis);

        chart_tb.setLineChartData(data);
        Viewport viewport = new Viewport(chart_tb.getMaximumViewport());
        viewport.top = 110;
        chart_tb.setMaximumViewport(viewport);
        chart_tb.setCurrentViewport(viewport);
        chart_bb.setOnValueTouchListener(new LineChartOnValueSelectListener() {
            @Override
            public void onValueSelected(int i, int i1, PointValue pointValue) {
                Toast.makeText(DetailBayiKaderActivity.this, "Nilai : " + pointValue.getY(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onValueDeselected() {

            }
        });

        chart_bb.setLineChartData(data);
        Viewport viewport_bb = new Viewport(chart_bb.getMaximumViewport());
        viewport_bb.top = 110;
        chart_bb.setMaximumViewport(viewport_bb);
        chart_bb.setCurrentViewport(viewport_bb);

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}