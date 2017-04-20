package dev.wildtraveling.View;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import dev.wildtraveling.Domain.Trip;
import dev.wildtraveling.R;
import dev.wildtraveling.Util.Util;

/**
 * Created by pere on 4/12/17.
 */
public class TripsListRecyclerView extends RecyclerView.Adapter<TripsListRecyclerView.ViewHolder> {

private final List<Trip> mValues;
private final Context context;

public ViewHolder holder;

public TripsListRecyclerView(Context context,
        List<Trip> items) {
        mValues = items;
        this.context = context;
        }

@Override
public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
        .inflate(R.layout.trips_list_layout, parent, false);
        holder = new ViewHolder(view);
        return holder;
        }

@Override
public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.name.setText(mValues.get(position).getName());
        holder.destination.setText(mValues.get(position).getDestination());
        holder.initDate.setText(Util.obtainDateString(mValues.get(position).getInitDate()));
        holder.finDate.setText(Util.obtainDateString(mValues.get(position).getFinalDate()));
}

@Override
public int getItemCount() {
        return mValues.size();
        }

public class ViewHolder extends RecyclerView.ViewHolder {
    public TextView name;
    public TextView destination;
    public TextView initDate;
    public TextView finDate;

    public ViewHolder(View view) {
        super(view);
        name = (TextView) view.findViewById(R.id.name_tripsList);
        destination = (TextView) view.findViewById(R.id.destination_tripsList);
        initDate = (TextView) view.findViewById(R.id.dateIni_tripsList);
        finDate = (TextView) view.findViewById(R.id.dateFin_tripsList);
    }

    @Override
    public String toString() {
        return super.toString();
    }
}

}
