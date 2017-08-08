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
import com.coding4fun.models.Ingredient;
import com.coding4fun.utils.EditDeleteCallbacks;

import java.util.List;

/**
 * Created by coding4fun on 29-Oct-16.
 */

public class IngredientRVadapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Ingredient> ingredients;
    Context context;
    EditDeleteCallbacks callbacks;
    private static final int LOADING_VIEW = 9;
    private static final int EMPTY_VIEW = 10;
    private static final int INGREDIENT_VIEW = 11;

    public IngredientRVadapter(Context context, List<Ingredient> modelData, EditDeleteCallbacks callbacks) {
        this.context = context;
        ingredients = modelData;
        this.callbacks = callbacks;
    }

    //describes an item view, and
    //Contains references for all views that are filled by the data of the entry
    public class IngredientViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView ingredientName;
        ImageView edit,delete;

        public IngredientViewHolder(View itemView) {
            super(itemView);
            ingredientName = (TextView) itemView.findViewById(R.id.row_ingredient_name);
            edit = (ImageView) itemView.findViewById(R.id.row_ingredient_edit);
            delete = (ImageView) itemView.findViewById(R.id.row_ingredient_delete);
            //itemView.setOnClickListener(this);
            edit.setOnClickListener(this);
            delete.setOnClickListener(this);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if(v.getId() == R.id.row_ingredient_edit){
                // TODO: show dialog with ingredient name to edit, then edit in FBDB
                callbacks.edit(getAdapterPosition());
            } else if(v.getId() == R.id.row_ingredient_delete){
                // TODO: delete from FBDB
                callbacks.delete(getAdapterPosition());
            } else {
                Toast.makeText(context, ingredients.get(getAdapterPosition()).getKey(), Toast.LENGTH_LONG).show();
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
        return ingredients.size()>0 ? ingredients.size() : 1;	//otherwise, even empty layout won't appear
    }

    @Override
    public int getItemViewType(int position) {
        if (ingredients.size() == 0)
            return EMPTY_VIEW;
        else if (ingredients.get(position) == null)
            return LOADING_VIEW;
        else if(ingredients.get(position) instanceof Ingredient)
            return INGREDIENT_VIEW;
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
        if (viewType == INGREDIENT_VIEW) {
            v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_ingredient, viewGroup, false);
            IngredientViewHolder evh = new IngredientViewHolder(v);
            return evh;
        }
        v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.loading2, viewGroup, false);
        LoadingViewHolder vh = new LoadingViewHolder(v);
        return vh;
    }

    //display (update) the data at the specified position
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        if(viewHolder instanceof IngredientViewHolder) {
            IngredientViewHolder vh = (IngredientViewHolder) viewHolder;
            Ingredient ingredient = ingredients.get(position);
            vh.ingredientName.setText(ingredient.getName());
        } else if(viewHolder instanceof LoadingViewHolder) {
            LoadingViewHolder vh = (LoadingViewHolder) viewHolder;
            vh.wv.setBackgroundColor(Color.TRANSPARENT);
            vh.wv.loadUrl("file:///android_asset/loading.html");
        }
    }



    void add(){
        LayoutInflater inf = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //View v = inf.inflate(R.la)
    }

}
