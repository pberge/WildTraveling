package dev.wildtraveling.View;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.List;

import dev.wildtraveling.Domain.Destination;
import dev.wildtraveling.R;

/**
 * Created by pere on 4/18/17.
 */
public class DestinationRecyclerView extends RecyclerView.Adapter<DestinationRecyclerView.ViewHolder> {

    private final List<Destination> mValues;
    private final Context context;

    public ViewHolder holder;

    public DestinationRecyclerView(Context context, List<Destination> items) {
        mValues = items;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.destinations_layout, parent, false);
        holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.name.setText(mValues.get(position).getName());


    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView name;

        public ViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.destination_name);
        }

        @Override
        public String toString() {
            return super.toString();
        }
    }

}
