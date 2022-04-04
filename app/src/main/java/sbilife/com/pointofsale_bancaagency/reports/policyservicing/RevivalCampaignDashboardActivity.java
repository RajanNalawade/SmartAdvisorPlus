package sbilife.com.pointofsale_bancaagency.reports.policyservicing;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

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
import sbilife.com.pointofsale_bancaagency.common.CommonMethods;

public class RevivalCampaignDashboardActivity extends AppCompatActivity implements OnChartValueSelectedListener {

    private String revivalPendingCount, revivalDoneCount, revivalPeriodEndCount, revivalPendingLikelyCount,
            revivalPendingHighLikelyCount, revivalPendingModerateCount, revivalPendingUnlikelyCount,
            revivalDoneLikelyCount, revivalDoneHighLikelyCount, revivalDoneModerateCount, revivalDoneUnlikelyCount,
            revivalPeriodEndLikelyCount, revivalPeriodEndHighLikelyCount, revivalPeriodEndModerateCount,
            revivalPeriodEndUnlikelyCount;
    private PieChart allRevivalPiechart, revivalPendingPiechart, revivalDonePiechart, revivalPeriodEndPiechart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.activity_revival_campaign_dashboard);
        //getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.window_title);

        Context context = this;
        CommonMethods commonMethods = new CommonMethods();
        commonMethods.setApplicationToolbarMenu(this, "Dashboard");
        Bundle bundle = getIntent().getExtras();

        if (bundle != null) {
            revivalPendingCount = bundle.getString("revivalPendingCount");
            revivalDoneCount = bundle.getString("revivalDoneCount");
            revivalPeriodEndCount = bundle.getString("revivalPeriodEndCount");

            revivalPendingLikelyCount = bundle.getString("revivalPendingLikelyCount");
            revivalPendingHighLikelyCount = bundle.getString("revivalPendingHighLikelyCount");
            revivalPendingModerateCount = bundle.getString("revivalPendingModerateCount");
            revivalPendingUnlikelyCount = bundle.getString("revivalPendingUnlikelyCount");

            revivalDoneLikelyCount = bundle.getString("revivalDoneLikelyCount");
            revivalDoneHighLikelyCount = bundle.getString("revivalDoneHighLikelyCount");
            revivalDoneModerateCount = bundle.getString("revivalDoneModerateCount");
            revivalDoneUnlikelyCount = bundle.getString("revivalDoneUnlikelyCount");

            revivalPeriodEndLikelyCount = bundle.getString("revivalPeriodEndLikelyCount");
            revivalPeriodEndHighLikelyCount = bundle.getString("revivalPeriodEndHighLikelyCount");
            revivalPeriodEndModerateCount = bundle.getString("revivalPeriodEndModerateCount");
            revivalPeriodEndUnlikelyCount = bundle.getString("revivalPeriodEndUnlikelyCount");
            DisplayMetrics displayMetrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            //int height = displayMetrics.heightPixels;
            int width = displayMetrics.widthPixels;

            TextView tvRevivalPending, tvRevivalDone, tvRevivalPeriodEnd;
            tvRevivalPending = findViewById(R.id.tvRevivalPending);
            tvRevivalDone = findViewById(R.id.tvRevivalDone);
            tvRevivalPeriodEnd = findViewById(R.id.tvRevivalPeriodEnd);

            tvRevivalPending.setText(revivalPendingCount);
            tvRevivalDone.setText(revivalDoneCount);
            tvRevivalPeriodEnd.setText(revivalPeriodEndCount);

            allRevivalPiechart = findViewById(R.id.allRevivalPiechart);
            ViewGroup.LayoutParams layoutParams = allRevivalPiechart.getLayoutParams();
            layoutParams.width = width - 30;
            layoutParams.height = width - 30;
            allRevivalPiechart.setLayoutParams(layoutParams);

            //Revival Pending
            TextView tvRevivalPendingLikely, tvRevivalPendingHighLikely, tvRevivalPendingModerate, tvRevivalPendingUnlikely;
            tvRevivalPendingLikely = findViewById(R.id.tvRevivalPendingLikely);
            tvRevivalPendingHighLikely = findViewById(R.id.tvRevivalPendingHighLikely);
            tvRevivalPendingModerate = findViewById(R.id.tvRevivalPendingModerate);
            tvRevivalPendingUnlikely = findViewById(R.id.tvRevivalPendingUnlikely);

            tvRevivalPendingLikely.setText(revivalPendingLikelyCount);
            tvRevivalPendingHighLikely.setText(revivalPendingHighLikelyCount);
            tvRevivalPendingModerate.setText(revivalPendingModerateCount);
            tvRevivalPendingUnlikely.setText(revivalPendingUnlikelyCount);

            revivalPendingPiechart = findViewById(R.id.revivalPendingPiechart);
            layoutParams = revivalPendingPiechart.getLayoutParams();
            layoutParams.width = width - 30;
            layoutParams.height = width - 30;
            revivalPendingPiechart.setLayoutParams(layoutParams);

            //Revival Done
            TextView tvRevivalDoneLikely, tvRevivalDoneHighLikely, tvRevivalDoneModerate, tvRevivalDoneUnlikely;

            tvRevivalDoneLikely = findViewById(R.id.tvRevivalDoneLikely);
            tvRevivalDoneHighLikely = findViewById(R.id.tvRevivalDoneHighLikely);
            tvRevivalDoneModerate = findViewById(R.id.tvRevivalDoneModerate);
            tvRevivalDoneUnlikely = findViewById(R.id.tvRevivalDoneUnlikely);

            tvRevivalDoneLikely.setText(revivalDoneLikelyCount);
            tvRevivalDoneHighLikely.setText(revivalDoneHighLikelyCount);
            tvRevivalDoneModerate.setText(revivalDoneModerateCount);
            tvRevivalDoneUnlikely.setText(revivalDoneUnlikelyCount);

            revivalDonePiechart = findViewById(R.id.revivalDonePiechart);
            layoutParams = revivalDonePiechart.getLayoutParams();
            layoutParams.width = width - 30;
            layoutParams.height = width - 30;
            revivalDonePiechart.setLayoutParams(layoutParams);

            //Revival Period End
            TextView tvRevivalPeriodEndLikely, tvRevivalPeriodEndHighLikely, tvRevivalPeriodEndModerate, tvRevivalPeriodEndUnlikely;
            tvRevivalPeriodEndLikely = findViewById(R.id.tvRevivalPeriodEndLikely);
            tvRevivalPeriodEndHighLikely = findViewById(R.id.tvRevivalPeriodEndHighLikely);
            tvRevivalPeriodEndModerate = findViewById(R.id.tvRevivalPeriodEndModerate);
            tvRevivalPeriodEndUnlikely = findViewById(R.id.tvRevivalPeriodEndUnlikely);

            tvRevivalPeriodEndLikely.setText(revivalPeriodEndLikelyCount);
            tvRevivalPeriodEndHighLikely.setText(revivalPeriodEndHighLikelyCount);
            tvRevivalPeriodEndModerate.setText(revivalPeriodEndModerateCount);
            tvRevivalPeriodEndUnlikely.setText(revivalPeriodEndUnlikelyCount);

            revivalPeriodEndPiechart = findViewById(R.id.revivalPeriodEndPiechart);
            layoutParams = revivalPeriodEndPiechart.getLayoutParams();
            layoutParams.width = width - 30;
            layoutParams.height = width - 30;
            revivalPeriodEndPiechart.setLayoutParams(layoutParams);

        }

        addDataToAllRevivalPieChart();
        addDataToRevivalPending();
        addDataToRevivalDone();
        addDataToRevivalPeriodEndDate();
    }

    private void addDataToAllRevivalPieChart() {

        //New PIE Chart
        allRevivalPiechart.setUsePercentValues(true);
        allRevivalPiechart.getDescription().setEnabled(false);
        allRevivalPiechart.setExtraOffsets(5, 10, 5, 5);
        allRevivalPiechart.setDragDecelerationFrictionCoef(0.95f);
        allRevivalPiechart.setCenterTextSize(15);
        allRevivalPiechart.setCenterText("Revival");
        allRevivalPiechart.setDrawHoleEnabled(true);
        allRevivalPiechart.setHoleColor(Color.WHITE);
        allRevivalPiechart.setTransparentCircleColor(Color.WHITE);
        allRevivalPiechart.setTransparentCircleAlpha(110);
        allRevivalPiechart.setHoleRadius(58f);
        allRevivalPiechart.setTransparentCircleRadius(61f);
        allRevivalPiechart.setDrawCenterText(true);
        allRevivalPiechart.setRotationAngle(0);
        allRevivalPiechart.setRotationEnabled(true);
        allRevivalPiechart.setHighlightPerTapEnabled(true);
        allRevivalPiechart.setOnChartValueSelectedListener(this);
        allRevivalPiechart.animateY(1400, Easing.EaseInOutQuad);

        Legend l = allRevivalPiechart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setDrawInside(false);
        l.setXEntrySpace(7f);
        l.setYEntrySpace(0f);
        l.setYOffset(0f);

        // entry label styling
        allRevivalPiechart.setEntryLabelColor(Color.BLACK);
        //allRevivalPiechart.setEntryLabelTypeface(tfRegular);
        allRevivalPiechart.setEntryLabelTextSize(14f);

        ArrayList<PieEntry> entries = new ArrayList<>();
        float val1 = Float.parseFloat(revivalPendingCount);
        float val2 = Float.parseFloat(revivalDoneCount);
        float val3 = Float.parseFloat(revivalPeriodEndCount);

        if (val1 != 0) {
            entries.add(new PieEntry(val1,
                    "Revival Pending",
                    getResources().getDrawable(R.drawable.dogh_covid)));
        }
        if (val2 != 0) {
            entries.add(new PieEntry(val2,
                    "Revival Done",
                    getResources().getDrawable(R.drawable.dogh_covid)));
        }

        if (val3 != 0) {
            entries.add(new PieEntry(val3,
                    "Revival Period End",
                    getResources().getDrawable(R.drawable.dogh_covid)));
        }

        PieDataSet dataSet = new PieDataSet(entries, "");

        dataSet.setDrawIcons(false);

        dataSet.setSliceSpace(13f);
        dataSet.setIconsOffset(new MPPointF(0, 40));
        dataSet.setSelectionShift(10f);

        // add a lot of colors

        ArrayList<Integer> colors = new ArrayList<>();

        for (int c : ColorTemplate.PASTEL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.COLORFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.LIBERTY_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.VORDIPLOM_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.JOYFUL_COLORS)
            colors.add(c);


        colors.add(ColorTemplate.getHoloBlue());

        dataSet.setColors(colors);
        /*dataSet.setColors(new int[] {Color.RED, Color.GREEN, R.color.Common_blue, R.color.DarkOrange,
                Color.BLUE,R.color.Brown,R.color.percentage3});*/
        //dataSet.setSelectionShift(0f);

        PieData data = new PieData(dataSet);
        //data.setValueFormatter(new PercentFormatter());
        data.setValueFormatter(new MyValueFormatter(allRevivalPiechart));
        data.setValueTextSize(10f);
        data.setValueTextColor(Color.BLACK);
        //data.setValueTypeface(tfLight);
        allRevivalPiechart.setData(data);

        // undo all highlights
        allRevivalPiechart.highlightValues(null);
        allRevivalPiechart.getDescription().setTextSize(12f);
        allRevivalPiechart.invalidate();

    }

    private void addDataToRevivalPending() {
        //New PIE Chart
        revivalPendingPiechart.setUsePercentValues(true);
        revivalPendingPiechart.getDescription().setEnabled(false);
        revivalPendingPiechart.setExtraOffsets(5, 10, 5, 5);
        revivalPendingPiechart.setDragDecelerationFrictionCoef(0.95f);
        revivalPendingPiechart.setCenterTextSize(15);
        revivalPendingPiechart.setCenterText("Revival Pending");
        revivalPendingPiechart.setDrawHoleEnabled(true);
        revivalPendingPiechart.setHoleColor(Color.WHITE);
        revivalPendingPiechart.setTransparentCircleColor(Color.WHITE);
        revivalPendingPiechart.setTransparentCircleAlpha(110);
        revivalPendingPiechart.setHoleRadius(58f);
        revivalPendingPiechart.setTransparentCircleRadius(61f);
        revivalPendingPiechart.setDrawCenterText(true);
        revivalPendingPiechart.setRotationAngle(0);
        revivalPendingPiechart.setRotationEnabled(true);
        revivalPendingPiechart.setHighlightPerTapEnabled(true);
        revivalPendingPiechart.setOnChartValueSelectedListener(this);
        revivalPendingPiechart.animateY(1400, Easing.EaseInOutQuad);

        Legend l = revivalPendingPiechart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setDrawInside(false);
        l.setXEntrySpace(7f);
        l.setYEntrySpace(0f);
        l.setYOffset(0f);

        // entry label styling
        revivalPendingPiechart.setEntryLabelColor(Color.BLACK);
        //revivalPendingPiechart.setEntryLabelTypeface(tfRegular);
        revivalPendingPiechart.setEntryLabelTextSize(14f);

        ArrayList<PieEntry> entries = new ArrayList<>();
        float val1 = Float.parseFloat(revivalPendingLikelyCount);
        float val2 = Float.parseFloat(revivalPendingHighLikelyCount);
        float val3 = Float.parseFloat(revivalPendingModerateCount);
        float val4 = Float.parseFloat(revivalPendingUnlikelyCount);

        if (val1 != 0) {
            entries.add(new PieEntry(val1,
                    "Likely",
                    getResources().getDrawable(R.drawable.dogh_covid)));
        }
        if (val2 != 0) {
            entries.add(new PieEntry(val2,
                    "High Likely",
                    getResources().getDrawable(R.drawable.dogh_covid)));
        }

        if (val3 != 0) {
            entries.add(new PieEntry(val3,
                    "Moderate",
                    getResources().getDrawable(R.drawable.dogh_covid)));
        }

        if (val4 != 0) {
            entries.add(new PieEntry(val4,
                    "Unlikely",
                    getResources().getDrawable(R.drawable.dogh_covid)));
        }

        PieDataSet dataSet = new PieDataSet(entries, "");

        dataSet.setDrawIcons(false);

        dataSet.setSliceSpace(13f);
        dataSet.setIconsOffset(new MPPointF(0, 40));
        dataSet.setSelectionShift(10f);

        // add a lot of colors

        ArrayList<Integer> colors = new ArrayList<>();

        for (int c : ColorTemplate.LIBERTY_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.VORDIPLOM_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.JOYFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.PASTEL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.COLORFUL_COLORS)
            colors.add(c);


        colors.add(ColorTemplate.getHoloBlue());

        dataSet.setColors(colors);
        //dataSet.setSelectionShift(0f);

        PieData data = new PieData(dataSet);
        //data.setValueFormatter(new PercentFormatter());
        data.setValueFormatter(new MyValueFormatter(revivalPendingPiechart));
        data.setValueTextSize(10f);
        data.setValueTextColor(Color.BLACK);
        //data.setValueTypeface(tfLight);
        revivalPendingPiechart.setData(data);

        // undo all highlights
        revivalPendingPiechart.highlightValues(null);
        revivalPendingPiechart.getDescription().setTextSize(12f);
        revivalPendingPiechart.invalidate();
    }


    private void addDataToRevivalDone() {
        //New PIE Chart
        revivalDonePiechart.setUsePercentValues(true);
        revivalDonePiechart.getDescription().setEnabled(false);
        revivalDonePiechart.setExtraOffsets(5, 10, 5, 5);
        revivalDonePiechart.setDragDecelerationFrictionCoef(0.95f);
        revivalDonePiechart.setCenterTextSize(15);
        revivalDonePiechart.setCenterText("Revival Done");
        revivalDonePiechart.setDrawHoleEnabled(true);
        revivalDonePiechart.setHoleColor(Color.WHITE);
        revivalDonePiechart.setTransparentCircleColor(Color.WHITE);
        revivalDonePiechart.setTransparentCircleAlpha(110);
        revivalDonePiechart.setHoleRadius(58f);
        revivalDonePiechart.setTransparentCircleRadius(61f);
        revivalDonePiechart.setDrawCenterText(true);
        revivalDonePiechart.setRotationAngle(0);
        revivalDonePiechart.setRotationEnabled(true);
        revivalDonePiechart.setHighlightPerTapEnabled(true);
        revivalDonePiechart.setOnChartValueSelectedListener(this);
        revivalDonePiechart.animateY(1400, Easing.EaseInOutQuad);

        Legend l = revivalDonePiechart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setDrawInside(false);
        l.setXEntrySpace(7f);
        l.setYEntrySpace(0f);
        l.setYOffset(0f);

        // entry label styling
        revivalDonePiechart.setEntryLabelColor(Color.BLACK);
        //revivalDonePiechart.setEntryLabelTypeface(tfRegular);
        revivalDonePiechart.setEntryLabelTextSize(14f);

        ArrayList<PieEntry> entries = new ArrayList<>();
        float val1 = Float.parseFloat(revivalDoneLikelyCount);
        float val2 = Float.parseFloat(revivalDoneHighLikelyCount);
        float val3 = Float.parseFloat(revivalDoneModerateCount);
        float val4 = Float.parseFloat(revivalDoneUnlikelyCount);

        if (val1 != 0) {
            entries.add(new PieEntry(val1,
                    "Likely",
                    getResources().getDrawable(R.drawable.dogh_covid)));
        }
        if (val2 != 0) {
            entries.add(new PieEntry(val2,
                    "High Likely",
                    getResources().getDrawable(R.drawable.dogh_covid)));
        }

        if (val3 != 0) {
            entries.add(new PieEntry(val3,
                    "Moderate",
                    getResources().getDrawable(R.drawable.dogh_covid)));
        }

        if (val4 != 0) {
            entries.add(new PieEntry(val4,
                    "Unlikely",
                    getResources().getDrawable(R.drawable.dogh_covid)));
        }

        PieDataSet dataSet = new PieDataSet(entries, "");

        dataSet.setDrawIcons(false);

        dataSet.setSliceSpace(13f);
        dataSet.setIconsOffset(new MPPointF(0, 40));
        dataSet.setSelectionShift(10f);

        // add a lot of colors

        ArrayList<Integer> colors = new ArrayList<>();

        for (int c : ColorTemplate.JOYFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.PASTEL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.LIBERTY_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.VORDIPLOM_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.COLORFUL_COLORS)
            colors.add(c);


        colors.add(ColorTemplate.getHoloBlue());

        dataSet.setColors(colors);
        //dataSet.setSelectionShift(0f);

        PieData data = new PieData(dataSet);
        //data.setValueFormatter(new PercentFormatter());
        data.setValueFormatter(new MyValueFormatter(revivalDonePiechart));
        data.setValueTextSize(10f);
        data.setValueTextColor(Color.BLACK);
        //data.setValueTypeface(tfLight);
        revivalDonePiechart.setData(data);

        // undo all highlights
        revivalDonePiechart.highlightValues(null);
        revivalDonePiechart.getDescription().setTextSize(12f);
        revivalDonePiechart.invalidate();
    }


    private void addDataToRevivalPeriodEndDate() {
        //New PIE Chart
        revivalPeriodEndPiechart.setUsePercentValues(true);
        revivalPeriodEndPiechart.getDescription().setEnabled(false);
        revivalPeriodEndPiechart.setExtraOffsets(5, 10, 5, 5);
        revivalPeriodEndPiechart.setDragDecelerationFrictionCoef(0.95f);
        revivalPeriodEndPiechart.setCenterTextSize(15);
        revivalPeriodEndPiechart.setCenterText("Revival Period End");
        revivalPeriodEndPiechart.setDrawHoleEnabled(true);
        revivalPeriodEndPiechart.setHoleColor(Color.WHITE);
        revivalPeriodEndPiechart.setTransparentCircleColor(Color.WHITE);
        revivalPeriodEndPiechart.setTransparentCircleAlpha(110);
        revivalPeriodEndPiechart.setHoleRadius(58f);
        revivalPeriodEndPiechart.setTransparentCircleRadius(61f);
        revivalPeriodEndPiechart.setDrawCenterText(true);
        revivalPeriodEndPiechart.setRotationAngle(0);
        revivalPeriodEndPiechart.setRotationEnabled(true);
        revivalPeriodEndPiechart.setHighlightPerTapEnabled(true);
        revivalPeriodEndPiechart.setOnChartValueSelectedListener(this);
        revivalPeriodEndPiechart.animateY(1400, Easing.EaseInOutQuad);

        Legend l = revivalPeriodEndPiechart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setDrawInside(false);
        l.setXEntrySpace(7f);
        l.setYEntrySpace(0f);
        l.setYOffset(0f);

        // entry label styling
        revivalPeriodEndPiechart.setEntryLabelColor(Color.BLACK);
        //revivalPeriodEndPiechart.setEntryLabelTypeface(tfRegular);
        revivalPeriodEndPiechart.setEntryLabelTextSize(14f);

        ArrayList<PieEntry> entries = new ArrayList<>();
        float val1 = Float.parseFloat(revivalPeriodEndLikelyCount);
        float val2 = Float.parseFloat(revivalPeriodEndHighLikelyCount);
        float val3 = Float.parseFloat(revivalPeriodEndModerateCount);
        float val4 = Float.parseFloat(revivalPeriodEndUnlikelyCount);

        if (val1 != 0) {
            entries.add(new PieEntry(val1,
                    "Likely",
                    getResources().getDrawable(R.drawable.dogh_covid)));
        }
        if (val2 != 0) {
            entries.add(new PieEntry(val2,
                    "High Likely",
                    getResources().getDrawable(R.drawable.dogh_covid)));
        }

        if (val3 != 0) {
            entries.add(new PieEntry(val3,
                    "Moderate",
                    getResources().getDrawable(R.drawable.dogh_covid)));
        }

        if (val4 != 0) {
            entries.add(new PieEntry(val4,
                    "Unlikely",
                    getResources().getDrawable(R.drawable.dogh_covid)));
        }

        PieDataSet dataSet = new PieDataSet(entries, "");

        dataSet.setDrawIcons(false);

        dataSet.setSliceSpace(13f);
        dataSet.setIconsOffset(new MPPointF(0, 40));
        dataSet.setSelectionShift(10f);

        // add a lot of colors

        ArrayList<Integer> colors = new ArrayList<>();

        for (int c : ColorTemplate.MATERIAL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.LIBERTY_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.VORDIPLOM_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.JOYFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.PASTEL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.COLORFUL_COLORS)
            colors.add(c);


        colors.add(ColorTemplate.getHoloBlue());

        dataSet.setColors(colors);
        /*dataSet.setColors(new int[] {Color.RED, Color.GREEN, R.color.Common_blue, R.color.DarkOrange,
                Color.BLUE,R.color.Brown,R.color.percentage3});*/
        //dataSet.setSelectionShift(0f);

        PieData data = new PieData(dataSet);
        //data.setValueFormatter(new PercentFormatter());
        data.setValueFormatter(new MyValueFormatter(revivalPeriodEndPiechart));
        data.setValueTextSize(10f);
        data.setValueTextColor(Color.BLACK);
        //data.setValueTypeface(tfLight);
        revivalPeriodEndPiechart.setData(data);

        // undo all highlights
        revivalPeriodEndPiechart.highlightValues(null);
        revivalPeriodEndPiechart.getDescription().setTextSize(12f);
        revivalPeriodEndPiechart.invalidate();
    }

    @Override
    public void onValueSelected(Entry e, Highlight h) {

        if (e == null) {
        }
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