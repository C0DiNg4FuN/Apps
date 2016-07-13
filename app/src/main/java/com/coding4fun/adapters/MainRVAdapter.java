package com.coding4fun.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.coding4fun.apps.R;
import com.coding4fun.models.MainRVRowModel;

import java.util.List;

/**
 * Created by coding4fun on 13-Jul-16.
 */

public class MainRVAdapter extends RecyclerView.Adapter <MainRVAdapter.MyViewHolder> {

    private List<MainRVRowModel> items;
    Context context;

    public MainRVAdapter(Context context, List<MainRVRowModel> modelData) {
        this.context = context;
    	items = modelData;
    }

    //describes an item view, and
    //Contains references for all views that are filled by the data of the entry
    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView title,description;
        ImageView image;

        public MyViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.main_rv_row_title);
            description = (TextView) itemView.findViewById(R.id.main_rv_row_description);
            image = (ImageView) itemView.findViewById(R.id.main_rv_row_image);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            MainRVRowModel item = items.get(getAdapterPosition());
            context.startActivity(new Intent(context,item.get_class()));
        }
    }

    // Return the size of the items list
    @Override
    public int getItemCount() {
        return items.size();
    }

    // inflate the item (row) layout and create the holder
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
    	View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.main_rv_row, viewGroup, false);
        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    //display (update) the data at the specified position
    @Override
    public void onBindViewHolder(MyViewHolder viewHolder, int position) {
    	MainRVRowModel item = items.get(position);
    	viewHolder.title.setText(item.getTitle());
        viewHolder.description.setText(item.getDescription());
        viewHolder.image.setImageResource(item.getImage());
    }

}
