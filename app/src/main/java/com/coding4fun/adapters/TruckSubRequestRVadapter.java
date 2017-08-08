package com.coding4fun.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.coding4fun.apps.R;
import com.coding4fun.models.TruckSubRequest;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by coding4fun on 03-Jul-17.
 */

public class TruckSubRequestRVadapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<TruckSubRequest> subRequests;
    private Context context;
    private int requestIndex;
    //private static final int SUB_REQUEST_VIEW = 9;

    public TruckSubRequestRVadapter(Context context, List<TruckSubRequest> subRequests, int requestIndex) {
        this.context = context;
        this.subRequests = subRequests;
        this.requestIndex = requestIndex;
    }

    //describes an item view, and
    //Contains references for all views that are filled by the data of the entry
    public class TruckSubRequestViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.row_truck_sub_request_container) TextView subRequestContainer;
        @BindView(R.id.row_truck_sub_request_from) TextView subRequestFrom;
        @BindView(R.id.row_truck_sub_request_to) TextView subRequestTo;
        @BindView(R.id.row_truck_sub_request_price) TextView subRequestPrice;
        @BindView(R.id.row_truck_sub_request_weight) TextView subRequestWeight;
        @BindView(R.id.row_truck_sub_request_commodity) TextView subRequestCommodity;

        public TruckSubRequestViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @OnClick(R.id.row_truck_sub_request)
        public void subRequestClicked(){
            Toast.makeText(context, "SubRequest index " + getAdapterPosition() + " of Request index " + requestIndex + " is clicked :)", Toast.LENGTH_SHORT).show();
        }
    }

    // inflate the item (row) layout and create the holder
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_truck_sub_request, viewGroup, false);
        return new TruckSubRequestViewHolder(v);
    }

    //display (update) the data at the specified position
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        TruckSubRequestViewHolder vh = (TruckSubRequestViewHolder) viewHolder;
        TruckSubRequest subReq = subRequests.get(position);
        vh.subRequestContainer.setText(subReq.getContainer());
        vh.subRequestFrom.setText(subReq.getDateTimeFrom());
        vh.subRequestTo.setText(subReq.getDateTimeTo());
        vh.subRequestPrice.setText(String.valueOf(subReq.getPrice()));
        vh.subRequestWeight.setText(String.valueOf(subReq.getWeight()));
        vh.subRequestCommodity.setText(subReq.getCommodity());
    }

    @Override
    public int getItemCount() {
        return subRequests.size();
    }

}