package com.example.stockviewer.ui.main;

import android.content.Context;
import android.widget.TextView;

import com.example.stockviewer.R;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.utils.MPPointF;

import java.text.DecimalFormat;

public class Tooltip extends MarkerView {


    private TextView closeTv;
    private TextView dateTv;

    private MPPointF offset;

    public Tooltip(Context ctx, int resource){
        super(ctx,resource);
        closeTv = findViewById(R.id.close);
//        dateTv  = findViewById(R.id.date);
    }

    @Override
    public void refreshContent(Entry e, Highlight highlight) {
        DecimalFormat decimalFormat = new DecimalFormat("#0.00");
        String v = decimalFormat.format(e.getY());
        closeTv.setText(v);
        super.refreshContent(e,highlight);
//        dateTv.setText("");
    }


    @Override
    public MPPointF getOffset(){

        if(offset == null){
            offset = new MPPointF(-getWidth()/2 , -getHeight());
        }

        return offset;
    }

}