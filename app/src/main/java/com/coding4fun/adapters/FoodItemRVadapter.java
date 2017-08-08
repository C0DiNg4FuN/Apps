package com.coding4fun.adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.coding4fun.apps.R;
import com.coding4fun.models.FoodItem;
import com.coding4fun.utils.EditDeleteCallbacks;

import java.util.List;


/**
 * Created by coding4fun on 04-Nov-16.
 */

public class FoodItemRVadapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<FoodItem> foodItems;
    Context context;
    EditDeleteCallbacks callbacks;
    private static final int LOADING_VIEW = 9;
    private static final int EMPTY_VIEW = 10;
    private static final int FOODITEM_VIEW = 11;

    public FoodItemRVadapter(Context context, List<FoodItem> modelData, EditDeleteCallbacks callbacks) {
        this.context = context;
        foodItems = modelData;
        this.callbacks = callbacks;
    }

    //describes an item view, and
    //Contains references for all views that are filled by the data of the entry
    public class FoodItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView name,category;
        ImageView image,edit,delete;

        public FoodItemViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.row_food_item_name);
            category= (TextView) itemView.findViewById(R.id.row_food_item_category);
            edit = (ImageView) itemView.findViewById(R.id.row_food_item_edit);
            delete = (ImageView) itemView.findViewById(R.id.row_food_item_delete);
            image = (ImageView) itemView.findViewById(R.id.row_food_item_image);
            edit.setOnClickListener(this);
            delete.setOnClickListener(this);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if(v.getId() == R.id.row_food_item_edit){
                callbacks.edit(getAdapterPosition());
            } else if(v.getId() == R.id.row_food_item_delete){
                callbacks.delete(getAdapterPosition());
            } else {
                Toast.makeText(context, foodItems.get(getAdapterPosition()).getKey(), Toast.LENGTH_LONG).show();
            }
        }

    }

    public class EmptyViewHolder extends RecyclerView.ViewHolder {
        public EmptyViewHolder(View itemView) {
            super(itemView);
        }
    }

    public class LoadingViewHolder extends RecyclerView.ViewHolder {
        android.webkit.WebView wv;
        public LoadingViewHolder(View itemView) {
            super(itemView);
            wv = (android.webkit.WebView) itemView.findViewById(R.id.loading_wv);
        }
    }

    // Return the size of the items list
    @Override
    public int getItemCount() {
        return foodItems.size()>0 ? foodItems.size() : 1;	//otherwise, even empty layout won't appear
    }

    @Override
    public int getItemViewType(int position) {
        if (foodItems.size() == 0)
            return EMPTY_VIEW;
        else if (foodItems.get(position) == null)
            return LOADING_VIEW;
        else if(foodItems.get(position) instanceof FoodItem)
            return FOODITEM_VIEW;
        return super.getItemViewType(position);
    }

    // inflate the item (row) layout and create the holder
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View v;
        if (viewType == EMPTY_VIEW) {
            v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.empty, viewGroup, false);
            FoodItemRVadapter.EmptyViewHolder evh = new FoodItemRVadapter.EmptyViewHolder(v);
            return evh;
        }
        if (viewType == FOODITEM_VIEW) {
            v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_food_item, viewGroup, false);
            FoodItemRVadapter.FoodItemViewHolder evh = new FoodItemRVadapter.FoodItemViewHolder(v);
            return evh;
        }
        v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.loading2, viewGroup, false);
        FoodItemRVadapter.LoadingViewHolder vh = new FoodItemRVadapter.LoadingViewHolder(v);
        return vh;
    }

    //display (update) the data at the specified position
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        if(viewHolder instanceof FoodItemRVadapter.FoodItemViewHolder) {
            FoodItemRVadapter.FoodItemViewHolder vh = (FoodItemRVadapter.FoodItemViewHolder) viewHolder;
            FoodItem foodItem = foodItems.get(position);
            vh.name.setText(foodItem.getName());
            vh.category.setText(foodItem.getCategory());
        } else if(viewHolder instanceof FoodItemRVadapter.LoadingViewHolder) {
            FoodItemRVadapter.LoadingViewHolder vh = (FoodItemRVadapter.LoadingViewHolder) viewHolder;
            vh.wv.setBackgroundColor(Color.TRANSPARENT);
            vh.wv.loadUrl("file:///android_asset/loading.html");
        }
    }

}