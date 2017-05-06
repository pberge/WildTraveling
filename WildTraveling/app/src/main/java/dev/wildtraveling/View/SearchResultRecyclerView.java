package dev.wildtraveling.View;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.List;

import dev.wildtraveling.R;
import dev.wildtraveling.Service.FoursquareVenue;

/**
 * Created by pere on 4/25/17.
 */
public class SearchResultRecyclerView extends RecyclerView.Adapter<SearchResultRecyclerView.ViewHolder> {

    private final List<FoursquareVenue> mValues;
    private final Context context;

    public ViewHolder holder;

    public SearchResultRecyclerView(Context context, List<FoursquareVenue> items) {
        mValues = items;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.search_result_layout, parent, false);
        holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.name.setText(mValues.get(position).getName());
        holder.adress.setText(mValues.get(position).getLocation());
        Double price = mValues.get(position).getPriceRank();
        if(price == 0.0){
            holder.ratingBar.setVisibility(View.INVISIBLE);
        } else {
            holder.ratingBar.setRating(Float.parseFloat(price+""));
            holder.ratingBar.setOnTouchListener(new View.OnTouchListener() {
                public boolean onTouch(View v, MotionEvent event) {
                    return true;
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView name;
        public TextView adress;
        public RatingBar ratingBar;

        public ViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.searchResult_name);
            adress = (TextView) view.findViewById(R.id.searchResult_address);
            ratingBar = (RatingBar) view.findViewById(R.id.ratingBar);
        }

        @Override
        public String toString() {
            return super.toString();
        }
    }

}