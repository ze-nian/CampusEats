package com.example.campuseats.Room;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.campuseats.R;
import com.example.campuseats.ui.slideshow.SlideshowFragment;

import java.util.List;

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.ViewHolder> {

    private List<MainData> dataList;
    private Activity context;
    private RoomDB database;

    public MainAdapter(Activity context, List<MainData> dataList){
        this.context=context;
        this.dataList=dataList;
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_row_main,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        MainData data= dataList.get(position);
        database=RoomDB.getInstance(context);

        holder.textView_name.setText(data.getName());
        holder.textView_price.setText(Integer.toString(data.getPrice()));
        holder.textView_quantity.setText(Integer.toString(data.getQuantity()));

/*
        holder.btDelete.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                MainData d = dataList.get(holder.getAdapterPosition());
                database.mainDao().delete(d.getName());
                int position=holder.getAdapterPosition();
                System.out.println(position);
                dataList.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position,dataList.size());

            }
        });
  */
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView textView_name,textView_price,textView_quantity;
        //ImageView btEdit,btDelete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textView_name=itemView.findViewById(R.id.text_view_name);
            textView_price=itemView.findViewById(R.id.text_view_price);
            textView_quantity=itemView.findViewById(R.id.text_view_quantity);
            //btDelete=itemView.findViewById(R.id.bt_delete);
        }


    }

}
