package com.coding4fun.adapters;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatRatingBar;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.coding4fun.apps.R;
import com.coding4fun.models.TruckRequest;

import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;

/**
 * Created by coding4fun on 03-Jul-17.
 */

public class TruckRequestRVadapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<TruckRequest> requests;
    private Context context;
    private static final int REQUEST_VIEW = 9;
    private static final int DETAILED_REQUEST_VIEW = 10;
    private static final int EMPTY_VIEW = 11;

    private boolean mEnableTouchIntercept = false;

    public TruckRequestRVadapter(Context context, List<TruckRequest> requests) {
        this.context = context;
        this.requests = requests;
    }

    // Return the size of the items list
    @Override
    public int getItemCount() {
        return requests.size()>0 ? requests.size() : 1;	//otherwise, even empty layout won't appear
    }

    @Override
    public int getItemViewType(int position) {
        if (requests.size() == 0)
            return EMPTY_VIEW;
        /*else if (requests.get(position).getViewStatus().equals(TruckRequest.VIEW_STATUS_NOT_CLICKED))
            return REQUEST_VIEW;
        else if(requests.get(position).getViewStatus().equals(TruckRequest.VIEW_STATUS_LOADING))
            return REQUEST_VIEW;*/
        else if(requests.get(position).getViewStatus().equals(TruckRequest.VIEW_STATUS_CLICKED))
            return DETAILED_REQUEST_VIEW;
        return REQUEST_VIEW;
        //return super.getItemViewType(position);
    }

    //describes an item view, and
    //Contains references for all views that are filled by the data of the entry
    public class TruckRequestViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.row_truck_request_side_view) View sideView;
        @BindView(R.id.row_truck_request_date) TextView requestDate;
        @BindView(R.id.row_truck_request_in_bulk) TextView requestInBulk;
        @BindView(R.id.row_truck_request_from2) TextView requestFrom;
        @BindView(R.id.row_truck_request_to2) TextView requestTo;
        @BindView(R.id.row_truck_request_shipping_company) TextView requestShippingCompany;
        @BindView(R.id.row_truck_request_loading) ProgressBar loading;
        @BindView(R.id.row_truck_request_rating) AppCompatRatingBar ratingBar;

        public TruckRequestViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }

        //@OnClick({R.id.row_truck_request, R.id.row_truck_request_view_button})
        @OnClick(R.id.row_truck_request_view_button)
        public void requestClicked(){
            //presenter.openTagImages(imgurTags.get(getAdapterPosition()));
            Toast.makeText(context, "Request Clicked :)", Toast.LENGTH_SHORT).show();
            //TODO: if loading >> do nothing, if not clicked, set loading & get api date, if clicked, hide details
            //TODO: move this shit to presenter
            final int pos = getAdapterPosition();
            final TruckRequest req = requests.get(pos);
            if(req.getViewStatus().equals(TruckRequest.VIEW_STATUS_NOT_CLICKED)){
                Log.e("truck","req.getSubRequests().size = " + req.getSubRequests().size());
                if(req.getSubRequests().size() > 0) { //data is ready. just show.
                    req.setViewStatus(TruckRequest.VIEW_STATUS_CLICKED);
                    notifyItemChanged(pos);
                } else { //no data yet. get from API
                    req.setViewStatus(TruckRequest.VIEW_STATUS_LOADING);
                    notifyItemChanged(getAdapterPosition());
                    Observable.timer(2, TimeUnit.SECONDS) // emits 0L after a specified delay (3 seconds in this case)
                            //.subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread()) //needed for Timer runs on computation thread by default
                            // (see: http://reactivex.io/documentation/scheduler.html). So to update UI, go back to mainThread.
                            .subscribe(new io.reactivex.functions.Consumer<Long>() {
                                @Override
                                public void accept(@io.reactivex.annotations.NonNull Long aLong) throws Exception {
                                    //Toast.makeText(context, "2 seconds are done", Toast.LENGTH_SHORT).show();
                                    req.setViewStatus(TruckRequest.VIEW_STATUS_CLICKED);
                                    notifyItemChanged(pos);
                                }
                            });
                }
            } /*else if(req.getViewStatus().equals(TruckRequest.VIEW_STATUS_CLICKED)){
                req.setViewStatus(TruckRequest.VIEW_STATUS_NOT_CLICKED);
                notifyItemChanged(getAdapterPosition());
            }*/
        }

    }

    //describes an item view, and
    //Contains references for all views that are filled by the data of the entry
    public class TruckDetailedRequestViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.row_truck_detailed_request_date) TextView requestDate;
        @BindView(R.id.row_truck_detailed_request_in_bulk) TextView requestInBulk;
        @BindView(R.id.row_truck_detailed_request_from2) TextView requestFrom;
        @BindView(R.id.row_truck_detailed_request_to2) TextView requestTo;
        @BindView(R.id.row_truck_detailed_request_shipping_company) TextView requestShippingCompany;
        @BindView(R.id.row_truck_detailed_request_rating) AppCompatRatingBar ratingBar;
        //@BindView(R.id.row_truck_detailed_request_decline_button) Button requestDecline;
        @BindView(R.id.row_truck_detailed_sub_request_list) RecyclerView subRequestsRV;
        @BindView(R.id.row_truck_detailed_request) View background;

        public TruckDetailedRequestViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
            initRV(subRequestsRV);
        }

        public void initRV(RecyclerView rv){
            rv.setLayoutManager(new LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false));
            //rv.setLayoutManager(new CustomLinearLayoutManager(context, LinearLayoutManager.VERTICAL,false));
            rv.setHasFixedSize(false);
            DefaultItemAnimator anim = new DefaultItemAnimator();
            anim.setAddDuration(500);
            anim.setRemoveDuration(500);
            rv.setItemAnimator(anim);
            rv.setNestedScrollingEnabled(false);
            rv.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
                @Override
                public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
                    return true;
                }

                @Override
                public void onTouchEvent(RecyclerView rv, MotionEvent e) {
                    /*if (e.getAction() == MotionEvent.ACTION_DOWN) {
                        // Disable intercepting touch to allow children to scroll
                        mParentListView.setTouchInterceptEnabled(false);
                    } else if (ev.getAction() == MotionEvent.ACTION_UP ||
                            ev.getAction() == MotionEvent.ACTION_CANCEL) {
                        // Re-enable after children handles touch
                        mParentListView.setTouchInterceptEnabled(true);
                    }*/
                }

                @Override
                public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {}
            });
        }

        @OnClick(R.id.row_truck_detailed_request)
        public void requestClicked(){
            requests.get(getAdapterPosition()).setViewStatus(TruckRequest.VIEW_STATUS_NOT_CLICKED);
            notifyItemChanged(getAdapterPosition());
        }

        @OnClick(R.id.row_truck_detailed_request_decline_button)
        public void declineButton(){
            Toast.makeText(context, "Request declined!", Toast.LENGTH_SHORT).show();
        }
    }

    public class EmptyViewHolder extends RecyclerView.ViewHolder {
        public EmptyViewHolder(View itemView) {
            super(itemView);
        }
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
        if (viewType == REQUEST_VIEW) {
            v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_truck_request, viewGroup, false);
            TruckRequestViewHolder evh = new TruckRequestViewHolder(v);
            return evh;
        }
        //DETAILED_REQUEST_VIEW
        v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_truck_detailed_request, viewGroup, false);
        TruckDetailedRequestViewHolder vh = new TruckDetailedRequestViewHolder(v);
        return vh;
    }

    //display (update) the data at the specified position
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        if(viewHolder instanceof TruckRequestViewHolder) {
            TruckRequestViewHolder vh = (TruckRequestViewHolder) viewHolder;
            TruckRequest req = requests.get(position);
            vh.requestDate.setText(req.getDate());
            vh.requestFrom.setText(req.getFrom());
            vh.requestTo.setText(req.getTo());
            vh.requestShippingCompany.setText(req.getCompany());
            vh.ratingBar.setRating(req.getRating());
            vh.loading.setVisibility((req.getViewStatus().equals(TruckRequest.VIEW_STATUS_LOADING)) ? View.VISIBLE : View.INVISIBLE);
            vh.sideView.setBackgroundColor(ContextCompat.getColor(context,(position%2 == 0) ? R.color.gray_dark : R.color.blue_light));
            vh.requestInBulk.setVisibility((req.isInBulk()) ? View.VISIBLE : View.GONE);
        } else if(viewHolder instanceof TruckDetailedRequestViewHolder) {
            TruckDetailedRequestViewHolder vh = (TruckDetailedRequestViewHolder) viewHolder;
            TruckRequest req = requests.get(position);
            vh.requestDate.setText(req.getDate());
            vh.requestFrom.setText(req.getFrom());
            vh.requestTo.setText(req.getTo());
            vh.requestShippingCompany.setText(req.getCompany());
            vh.ratingBar.setRating(req.getRating());
            vh.requestInBulk.setVisibility((req.isInBulk()) ? View.VISIBLE : View.GONE);
            vh.background.setBackgroundColor(ContextCompat.getColor(context,(position%2 == 0) ? R.color.gray_dark : R.color.blue_light));
            //vh.subRequestsRV.setLayoutManager(new CustomLinearLayoutManager(context, LinearLayoutManager.VERTICAL,false));
            vh.subRequestsRV.setLayoutManager(new LinearLayoutManager(context));
            vh.subRequestsRV.setHasFixedSize(false);
            vh.subRequestsRV.setNestedScrollingEnabled(false);
            TruckSubRequestRVadapter adapter = new TruckSubRequestRVadapter(context,req.getSubRequests(),position);
            vh.subRequestsRV.setAdapter(adapter);
        }
    }
}