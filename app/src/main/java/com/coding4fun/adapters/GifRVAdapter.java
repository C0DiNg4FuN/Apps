package com.coding4fun.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.coding4fun.apps.R;
import com.coding4fun.models.GifRowModel;

import java.util.List;

/**
 * Created by coding4fun on 15-Jul-16.
 */

public class GifRVAdapter extends RecyclerView.Adapter <RecyclerView.ViewHolder> {

    private List<GifRowModel> items;
    Context context;
    CoordinatorLayout co;   //needed for snackbar
    private static final int EMPTY_VIEW = 10;
    private static final int GIF_VIEW = 11;
    private static final int LOADING_VIEW = 12;

    public GifRVAdapter(Context context, List<GifRowModel> modelData, CoordinatorLayout co) {
        this.context = context;
    	items = modelData;
        this.co = co;
    }

    //describes an item view, and
    //Contains references for all views that are filled by the data of the entry
    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView name;
        ImageView delete, share;
        LinearLayout ll;

        public MyViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.gif_name);
            delete = (ImageView) itemView.findViewById(R.id.gif_delete);
            share = (ImageView) itemView.findViewById(R.id.gif_share);
            ll = (LinearLayout) itemView.findViewById(R.id.gif_row_ll);
            itemView.setOnClickListener(this);
            delete.setOnClickListener(this);
            share.setOnClickListener(this);
        }

		@Override
		public void onClick(View v) {
            final int index = getAdapterPosition();
            final GifRowModel item = items.get(index);
            if(v.getId() == R.id.gif_delete){
                items.remove(index);
                notifyItemRemoved(index);
                Snackbar.make(co,"GIF removed",Snackbar.LENGTH_LONG)
                        .setAction("UNDO", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                items.add(index,item);
                                notifyItemInserted(index);
                            }
                        }).show();
            } else if(v.getId() == R.id.gif_share){
                Intent shareIntent = new Intent();
                shareIntent.setAction(Intent.ACTION_SEND);
                shareIntent.putExtra(Intent.EXTRA_TEXT, item.getLink());
                shareIntent.setType("text/plain");
                context.startActivity(shareIntent);
            } else {
                /*Intent openIntent = new Intent();
                openIntent.setAction(Intent.ACTION_VIEW);
                openIntent.setData(Uri.parse(item.getLink()));
                context.startActivity(openIntent);*/
                Intent viewIntent = new Intent();
                viewIntent.putExtra("url",item.getLink());
                context.startActivity(viewIntent);
            }
		}
    }

    public class EmptyViewHolder extends RecyclerView.ViewHolder {
        public EmptyViewHolder(View itemView) {
            super(itemView);
        }
    }

    public class LoadingViewHolder extends RecyclerView.ViewHolder {
        ProgressBar pb;
        public LoadingViewHolder(View itemView) {
            super(itemView);
            pb = (ProgressBar) itemView.findViewById(R.id.loading_pb);
        }
    }

    // Return the size of the items list
    @Override
    public int getItemCount() {
        return items.size()>0 ? items.size() : 1;	//otherwise, even empty layout won't appear
    }

    @Override
    public int getItemViewType(int position) {
        if (items.size() == 0)
            return EMPTY_VIEW;
        else if (items.get(position) instanceof GifRowModel)
            return GIF_VIEW;
        else if (items.get(position) == null)
            return LOADING_VIEW;
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
        if (viewType == GIF_VIEW) {
            v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.gif_rv_row, viewGroup, false);
            MyViewHolder evh = new MyViewHolder(v);
            return evh;
        }
        v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.loading, viewGroup, false);
        LoadingViewHolder vh = new LoadingViewHolder(v);
        return vh;
    }

    //display (update) the data at the specified position
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        if(viewHolder instanceof MyViewHolder) {
            MyViewHolder vh = (MyViewHolder) viewHolder;
            GifRowModel item = items.get(position);
            vh.name.setText(item.getName());
            //Bitmap b = BitmapFactory.decodeFile(item.getPath());
            //if(item.getPath())
                //vh.ll.setBackgroundDrawable(new BitmapDrawable(context.getResources(), b));
        }
    }

}