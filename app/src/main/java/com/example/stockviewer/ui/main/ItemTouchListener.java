package com.example.stockviewer.ui.main;

import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

public class ItemTouchListener implements RecyclerView.OnItemTouchListener {

    public interface ClickListener {

        void onClick(View view, int position);
        void onLongClick(View view, int position);
    }
    private ClickListener clickListener;
    private GestureDetector gestureDetector;

    public ItemTouchListener(Context context, final RecyclerView recyclerView, final ClickListener clickListener){

        this.clickListener=clickListener;
        gestureDetector = new GestureDetector(context,new GestureDetector.SimpleOnGestureListener(){
            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                return true;
            }

            @Override
            public void onLongPress(MotionEvent e) {
                View child = recyclerView.findChildViewUnder(e.getX(),e.getY());
                if(child != null && clickListener != null ){
                    clickListener.onLongClick(child,recyclerView.getChildAdapterPosition(child));
                }
            }
        });

    }
    @Override
    public boolean onInterceptTouchEvent(@NonNull RecyclerView recyclerView, @NonNull MotionEvent motionEvent) {
        View child=recyclerView.findChildViewUnder(motionEvent.getX(),motionEvent.getY());
        if(child!=null && clickListener!=null && gestureDetector.onTouchEvent(motionEvent)){
            clickListener.onClick(child, recyclerView.getChildAdapterPosition(child));
        }
        return false;
    }

    @Override
    public void onTouchEvent(@NonNull RecyclerView recyclerView, @NonNull MotionEvent motionEvent) {

    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean b) {

    }
}
