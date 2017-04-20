package dev.wildtraveling.View;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TextView;

import java.util.Date;
import java.util.Calendar;
import java.util.List;

import dev.wildtraveling.R;

/**
 * Created by pere on 4/5/17.
 */
public class CalendarRecyclerView extends RecyclerView.Adapter<CalendarRecyclerView.ViewHolder> {

private final Context context;
private List<Date> d;
public ViewHolder holder;

public CalendarRecyclerView(Context context, List<Date> d) {
    this.d = d;
    this.context = context;
}

@Override
public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
        .inflate(R.layout.date_picker_layout, parent, false);
        holder = new ViewHolder(view);
        return holder;
        }

@Override
public void onBindViewHolder(final ViewHolder holder, final int position) {
    holder.datePicker.setMinDate(d.get(position).getTime());

}

@Override
public int getItemCount() {
        return 1;
        }

public class ViewHolder extends RecyclerView.ViewHolder {
    public DatePicker datePicker;

    public ViewHolder(View view) {
        super(view);
        datePicker = (DatePicker) view.findViewById(R.id.datePicker);
    }

    @Override
    public String toString() {
        return super.toString();
    }
}

}
