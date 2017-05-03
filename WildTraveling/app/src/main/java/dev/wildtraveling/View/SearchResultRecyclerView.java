package dev.wildtraveling.View;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
        holder.price.setText(""+mValues.get(position).getPriceRank());
        Double price = mValues.get(position).getPriceRank();
        if(price == 1.0 || price == 1){
            holder.star1.setColorFilter(R.color.colorAccent, PorterDuff.Mode.SRC_ATOP);
            holder.star2.setColorFilter(R.color.colorPrimary);
            holder.star3.setColorFilter(R.color.colorPrimary);
            holder.star4.setColorFilter(R.color.colorPrimary);
        }else if(price == 2.0 || price == 2){
            holder.star1.setColorFilter(R.color.colorAccent);
            holder.star2.setColorFilter(R.color.colorAccent);
            holder.star3.setColorFilter(R.color.colorPrimary);
            holder.star4.setColorFilter(R.color.colorPrimary);
        }else if(price == 3.0 || price == 3){
            holder.star1.setColorFilter(R.color.colorAccent);
            holder.star2.setColorFilter(R.color.colorAccent);
            holder.star3.setColorFilter(R.color.colorAccent);
            holder.star4.setColorFilter(R.color.colorPrimary);
        }else if(price == 4.0 || price == 4){
            holder.star1.setColorFilter(R.color.colorAccent);
            holder.star2.setColorFilter(R.color.colorAccent);
            holder.star3.setColorFilter(R.color.colorAccent);
            holder.star4.setColorFilter(R.color.colorAccent);
        } else{
            holder.stars.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView name;
        public TextView adress;
        public TextView price;
        public ImageView star1;
        public ImageView star2;
        public ImageView star3;
        public ImageView star4;
        public LinearLayout stars;

        public ViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.searchResult_name);
            adress = (TextView) view.findViewById(R.id.searchResult_address);
            price = (TextView) view.findViewById(R.id.searchResult_price);
            star1 = (ImageView) view.findViewById(R.id.star1);
            star2 = (ImageView) view.findViewById(R.id.star2);
            star3 = (ImageView) view.findViewById(R.id.star3);
            star4 = (ImageView) view.findViewById(R.id.star4);
            stars = (LinearLayout) view.findViewById(R.id.starsLayout);

        }

        @Override
        public String toString() {
            return super.toString();
        }
    }

}