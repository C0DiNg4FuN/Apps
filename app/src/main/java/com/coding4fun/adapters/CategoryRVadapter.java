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
import com.coding4fun.models.Category;
import com.coding4fun.utils.EditDeleteCallbacks;

import java.util.List;

/**
 * Created by coding4fun on 29-Oct-16.
 */

public class CategoryRVadapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Category> categories;
    Context context;
    EditDeleteCallbacks callbacks;
    private static final int LOADING_VIEW = 9;
    private static final int EMPTY_VIEW = 10;
    private static final int CATEGORY_VIEW = 11;

    public CategoryRVadapter(Context context, List<Category> modelData, EditDeleteCallbacks callbacks) {
        this.context = context;
        categories = modelData;
        this.callbacks = callbacks;
    }

    //describes an item view, and
    //Contains references for all views that are filled by the data of the entry
    public class CategoryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView categoryName;
        ImageView edit,delete;

        public CategoryViewHolder(View itemView) {
            super(itemView);
            categoryName = (TextView) itemView.findViewById(R.id.row_category_name);
            edit = (ImageView) itemView.findViewById(R.id.row_category_edit);
            delete = (ImageView) itemView.findViewById(R.id.row_category_delete);
            edit.setOnClickListener(this);
            delete.setOnClickListener(this);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if(v.getId() == R.id.row_category_edit){
                callbacks.edit(getAdapterPosition());
            } else if(v.getId() == R.id.row_category_delete){
                callbacks.delete(getAdapterPosition());
            } else {
                Toast.makeText(context, categories.get(getAdapterPosition()).getKey(), Toast.LENGTH_LONG).show();
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
        return categories.size()>0 ? categories.size() : 1;	//otherwise, even empty layout won't appear
    }

    @Override
    public int getItemViewType(int position) {
        if (categories.size() == 0)
            return EMPTY_VIEW;
        else if (categories.get(position) == null)
            return LOADING_VIEW;
        else if(categories.get(position) instanceof Category)
            return CATEGORY_VIEW;
        return super.getItemViewType(position);
    }

    // inflate the item (row) layout and create the holder
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View v;
        if (viewType == EMPTY_VIEW) {
            v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.empty, viewGroup, false);
            EmptyViewHolder evh = new EmptyViewHolder(v);
            return evh;
        }
        if (viewType == CATEGORY_VIEW) {
            v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_category, viewGroup, false);
            CategoryViewHolder evh = new CategoryViewHolder(v);
            return evh;
        }
        v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.loading2, viewGroup, false);
        LoadingViewHolder vh = new LoadingViewHolder(v);
        return vh;
    }

    //display (update) the data at the specified position
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        if(viewHolder instanceof CategoryViewHolder) {
            CategoryViewHolder vh = (CategoryViewHolder) viewHolder;
            Category category = categories.get(position);
            vh.categoryName.setText(category.getName());
        } else if(viewHolder instanceof LoadingViewHolder) {
            LoadingViewHolder vh = (LoadingViewHolder) viewHolder;
            vh.wv.setBackgroundColor(Color.TRANSPARENT);
            vh.wv.loadUrl("file:///android_asset/loading.html");
        }
    }

}