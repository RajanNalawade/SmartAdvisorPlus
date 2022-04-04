
package sbilife.com.pointofsale_bancaagency.home.dashboard;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.MPPointF;

import java.text.DecimalFormat;
import java.util.ArrayList;

import sbilife.com.pointofsale_bancaagency.R;
import sbilife.com.pointofsale_bancaagency.common.CommonForAllProd;

public class RenewalPremiumPieFragment extends Fragment implements OnChartValueSelectedListener {

    private PieChart pieChart;
    private String renewalpremium_trad2 = "0";
    private String renewalpremium_ulip2 = "0";
    private String strtype2 = "";

    public RenewalPremiumPieFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_renewalpremiumpie, container, false);
        Bundle bundle = getArguments();
        if (isAdded()) {
            if (bundle != null) {
                renewalpremium_trad2 = bundle.getString("renewalpremium_trad1");
                renewalpremium_ulip2 = bundle.getString("renewalpremium_ulip1");
                strtype2 = bundle.getString("strtype1");
                if (renewalpremium_trad2 == null) {
                    renewalpremium_trad2 = "0";
                }
                if (renewalpremium_ulip2 == null) {
                    renewalpremium_ulip2 = "0";
                }
            }
            Context context = getActivity();
        }
        CommonForAllProd cfap = new CommonForAllProd();
        pieChart = rootView.findViewById(R.id.piechart);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        //int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;

        ViewGroup.LayoutParams layoutParams = pieChart.getLayoutParams();
        layoutParams.width = width - 30;
        layoutParams.height = width - 30;
        pieChart.setLayoutParams(layoutParams);

        pieChart.setUsePercentValues(true);
        pieChart.getDescription().setEnabled(false);
        pieChart.setExtraOffsets(5, 10, 5, 5);

        pieChart.setDragDecelerationFrictionCoef(0.95f);

        //pieChart.setCenterTextTypeface(tfLight);
        pieChart.setCenterTextSize(15);
        if (strtype2.equals("CIF")) {
            pieChart.setCenterText("Renewal Premium Amount Report");
        } else {
            pieChart.setCenterText("RP Amount Report");
        }
        pieChart.setDrawHoleEnabled(true);
        pieChart.setHoleColor(Color.WHITE);

        pieChart.setTransparentCircleColor(Color.WHITE);
        pieChart.setTransparentCircleAlpha(110);

        pieChart.setHoleRadius(58f);
        pieChart.setTransparentCircleRadius(61f);

        pieChart.setDrawCenterText(true);

        pieChart.setRotationAngle(0);
        // enable rotation of the pieChart by touch
        pieChart.setRotationEnabled(true);
        pieChart.setHighlightPerTapEnabled(true);

        // pieChart.setUnit(" €");
        // pieChart.setDrawUnitsInChart(true);

        // add a selection listener
        pieChart.setOnChartValueSelectedListener(this);

        /*seekBarX.setProgress(4);
        seekBarY.setProgress(10);*/

        pieChart.animateY(1400, Easing.EaseInOutQuad);
        // pieChart.spin(2000, 0, 360);

        Legend l = pieChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setDrawInside(false);
        l.setXEntrySpace(7f);
        l.setYEntrySpace(0f);
        l.setYOffset(0f);

        // entry label styling
        pieChart.setEntryLabelColor(Color.WHITE);
        //pieChart.setEntryLabelTypeface(tfRegular);
        pieChart.setEntryLabelTextSize(14f);

        addDataToPieChart();

        return rootView;
    }

    private void addDataToPieChart() {
        /*ArrayList<Entry> yvalues = new ArrayList<Entry>();
        float val1 = Float.parseFloat(renewalpremium_trad2);
        float val2 = Float.parseFloat(renewalpremium_ulip2);
        yvalues.add(new Entry(val1, 0));
        yvalues.add(new Entry(val2, 1));

        PieDataSet dataSet = new PieDataSet(yvalues, "");

        ArrayList<String> xVals = new ArrayList<String>();
        String NBCount = cfap.getRound(cfap.getStringWithout_E(val1));
        String surableAmount = cfap.getRound(cfap.getStringWithout_E(val2));

        if (strtype2.equals("CIF")) {
            xVals.add("Traditional :" + cfap.getRound(cfap.getStringWithout_E(val1)));
            xVals.add("Ulip:" + cfap.getRound(cfap.getStringWithout_E(val2)));
        } else {
            xVals.add("Renewal Premium Amount MTD:" + NBCount);
            xVals.add("Renewal Premium Amount YTD:" + surableAmount);
        }

        PieData data = new PieData(xVals, dataSet);
        data.setValueFormatter(new PercentFormatter());

        pieChart.setData(data);
        if (strtype2.equals("CIF")) {
            pieChart.setDescription("Renewal Premium Amount Report");
        } else {
            pieChart.setDescription("RP Amount Report");

            if (NBCount.equals("0") && surableAmount.equals("0")) {
                tv_total.setText("No Data Found");
            } else {
                tv_total.setText("");
            }
        }

        pieChart.setDescriptionTextSize(25f);
        pieChart.setDrawHoleEnabled(true);
        pieChart.setTransparentCircleRadius(30f);
        pieChart.setHoleRadius(25f);

        ArrayList<Integer> colors = new ArrayList<Integer>();
        colors.add(0, getResources().getColor(R.color.percentage5));
        colors.add(1, getResources().getColor(R.color.percentage6));
        dataSet.setColors(colors);
        dataSet.setSliceSpace(1);
//    dataSet.setColors(ColorTemplate.VORDIPLOM_COLORS);
        data.setValueTextSize(15f);
        data.setValueTextColor(context.getResources().getColor(R.color.fbblue));

//    pieChart.setOnChartValueSelectedListener(this);

        pieChart.animateXY(1400, 1400);

//    pieChart.animateY(1400, Easing.EasingOption.EaseInOutQuad);
        Legend l = pieChart.getLegend();
//    l .setCustom(new int[] { R.color.percentage1,R.color.percentage2}, new String[] {"Ulip :"+val1+"	", "Traditional :"+val2+"	"});
        l.setPosition(LegendPosition.BELOW_CHART_CENTER);
        l.setXEntrySpace(7);
        l.setYEntrySpace(5);
        l.setTextSize(14);
        l.setTextColor(context.getResources().getColor(R.color.fbblue));*/

        //New PIE Chart

        ArrayList<PieEntry> entries = new ArrayList<>();
        float val1 = Float.parseFloat(renewalpremium_trad2);
        float val2 = Float.parseFloat(renewalpremium_ulip2);
       /* float val1 = Float.parseFloat(7 + "");
        float val2 = Float.parseFloat(9 + "");*/
            /*entries.add(new Entry(val1, 0));
            entries.add(new Entry(val2, 1));*/
        entries.add(new PieEntry(val1,
                "Traditional:" + val1,
                getResources().getDrawable(R.drawable.dogh_covid)));

        entries.add(new PieEntry(val2,
                "Ulip:" + val2,
                getResources().getDrawable(R.drawable.dogh_covid)));


        PieDataSet dataSet = new PieDataSet(entries, "");

        dataSet.setDrawIcons(false);

        dataSet.setSliceSpace(13f);
        dataSet.setIconsOffset(new MPPointF(0, 40));
        dataSet.setSelectionShift(10f);

        // add a lot of colors

        ArrayList<Integer> colors = new ArrayList<>();

        for (int c : ColorTemplate.JOYFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.MATERIAL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.PASTEL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.COLORFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.LIBERTY_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.VORDIPLOM_COLORS)
            colors.add(c);


        colors.add(ColorTemplate.getHoloBlue());

        dataSet.setColors(colors);
        //dataSet.setSelectionShift(0f);

        PieData data = new PieData(dataSet);
        //data.setValueFormatter(new PercentFormatter());
        data.setValueFormatter(new MyValueFormatter(pieChart));
        data.setValueTextSize(15f);
        data.setValueTextColor(Color.WHITE);
        //data.setValueTypeface(tfLight);
        pieChart.setData(data);

        // undo all highlights
        pieChart.highlightValues(null);
        pieChart.getDescription().setTextSize(16f);
        pieChart.invalidate();


    }


    @Override
    public void onValueSelected(Entry e, Highlight h) {

        if (e == null) {
        }
        /*Log.i("VAL SELECTED",
                "Value: " + e.getY() + ", index: " + h.getX()
                        + ", DataSet index: " + h.getDataSetIndex());*/
    }


    @Override
    public void onNothingSelected() {
        Log.i("PieChart", "nothing selected");
    }

    class MyValueFormatter extends PercentFormatter {
        final DecimalFormat mFormat;
        final PieChart mPieChart;

        public MyValueFormatter(PieChart pieChart) {
            mFormat = new DecimalFormat("###,###,##0.0");
            mPieChart = pieChart;
        }


        @Override
        public String getFormattedValue(float value) {
            return mFormat.format(value) + "%";
        }

        @Override
        public String getPieLabel(float value, PieEntry pieEntry) {
            if (mPieChart != null && mPieChart.isUsePercentValuesEnabled()) {
                // Converted to percent
                return getFormattedValue(value);
            } else {
                // raw value, skip percent sign
                return mFormat.format(value);
            }
        }
    }
}
