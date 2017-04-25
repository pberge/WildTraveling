package dev.wildtraveling.View;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.List;

import dev.wildtraveling.Domain.Expense;
import dev.wildtraveling.R;
import dev.wildtraveling.Service.FoursquareVenue;
import dev.wildtraveling.Util.Util;

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
        System.out.println("Entra dins el: " + mValues.get(position).getName());

    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView name;

        public ViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.searchResult_name);
        }

        @Override
        public String toString() {
            return super.toString();
        }
    }

}
