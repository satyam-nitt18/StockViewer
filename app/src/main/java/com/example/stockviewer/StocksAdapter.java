package com.example.stockviewer;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.stockviewer.ui.main.Company;
import com.example.stockviewer.ui.main.ItemTouchListener;

import java.util.ArrayList;
import java.util.List;


public class StocksAdapter extends RecyclerView.Adapter<StocksAdapter.ViewHolder>  {

    private static final String TAG = "StocksAdapter";
    private ArrayList<String> mNames;
    public ArrayList<String> mSymbols;
    private Context mContext;
    public static String companyName, companySymbol;
    public OnClickListener onClickListener=null;
    private SparseBooleanArray selected_items;
    private int current_selected_id=-1;
    public StocksAdapter(){

    }

    public void setOnClickListener(OnClickListener onClickListener){
            this.onClickListener=onClickListener;
    }
    public StocksAdapter(ArrayList<String> mNames, ArrayList<String> mSymbols, Context context){
        this.mSymbols=mSymbols;
        this.mNames=mNames;
        mContext=context;
        selected_items=new SparseBooleanArray();
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(mContext).inflate(R.layout.stock_list, viewGroup, false);
        ViewHolder holder=new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        Log.d(TAG, "onBindViewHolder : called.");

        holder.Name.setText( mNames.get(position));
        holder.symbol.setText("Id: " + mSymbols.get(position));

        holder.parentLayout.setActivated(selected_items.get(position, false));

        holder.parentLayout.setOnClickListener((v)-> {
          if(onClickListener==null)
              return;
          onClickListener.onItemClick(v, position);
        });


                holder.parentLayout.setOnLongClickListener((v)-> {
                   if(onClickListener==null)
                       return false;
                   else{
                       onClickListener.onItemLongClick(v, position);
                       return true;
                   }
                });

        toggleCheckedIcon(holder, position);
    }

    private void toggleCheckedIcon(ViewHolder holder, int position) {
        if(selected_items.get(position, false)) {
            holder.lyt_checked.setVisibility(View.VISIBLE);
            if (current_selected_id == position) resetCurrentIndex();
        }
        else {
            holder.lyt_checked.setVisibility(View.GONE);
            if(current_selected_id==position) resetCurrentIndex();
        }

    }


    public String getItem(int position){
        return mSymbols.get(position);
    }

    @Override
    public int getItemCount() {
        Log.d(TAG, "getItemCount: "+mNames.size());
        return mNames.size();
    }
    public void startIntent(int position){
        final Intent intent = new Intent(mContext, CompanyStockDetails.class);
        companyName=mNames.get(position);
        companySymbol= mSymbols.get(position);
        intent.putExtra("from", "viewStocks");
        mContext.startActivity(intent);
    }
   /* public void setSelectedIds(List<String> selectedIds) {
        this.selectedIds = selectedIds;
        notifyDataSetChanged();
    }*/


   public void toggleSelection(int pos){
       current_selected_id=pos;
       if(selected_items.get(pos, false)){
           selected_items.delete(pos);
       }else{
           selected_items.put(pos, true);
       }
       notifyDataSetChanged();
   }
    public void clearSelection(){
        selected_items.clear();
        notifyDataSetChanged();
    }
    public int getSelectedItemsCount(){return selected_items.size();}
    public List<String> getSelectedItems(){
        List<String> selectedItems=new ArrayList<>(selected_items.size());
        for(int i=0;i<selected_items.size();i++)
            selectedItems.add(mSymbols.get(selected_items.keyAt(i)));
        return selectedItems;
    }
    public void resetCurrentIndex(){
        current_selected_id=-1;
    }
    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView symbol;
        TextView Name;
        RelativeLayout parentLayout, lyt_checked, complete_layout;

        public ViewHolder(View itemView){
            super(itemView);
            symbol=itemView.findViewById(R.id.symbol);
            Name=itemView.findViewById(R.id.name);
            parentLayout=itemView.findViewById(R.id.stocks_parent);
            lyt_checked=itemView.findViewById(R.id.selected);
            //complete_layout=itemView.findViewById(R.id.relativeLayout);
        }
    }

    public void updateList(List<String> newListNames, List<String> newListIds){
        mNames=new ArrayList<>();
        mSymbols=new ArrayList<>();
        mNames.addAll(newListNames);
        mSymbols.addAll(newListIds);
        notifyDataSetChanged();
    }
    public interface OnClickListener{
       void onItemClick(View view, int pos);
       void onItemLongClick(View view, int pos);
    }
}
