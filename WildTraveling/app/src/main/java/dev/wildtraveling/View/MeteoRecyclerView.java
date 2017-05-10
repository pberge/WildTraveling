package dev.wildtraveling.View;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Calendar;
import java.util.List;

import dev.wildtraveling.Domain.dayMeteoPrevision;
import dev.wildtraveling.R;
import dev.wildtraveling.Service.MeteoServiceAdapter;
import dev.wildtraveling.Util.Util;

/**
 * Created by pere on 5/9/17.
 */
public class MeteoRecyclerView extends RecyclerView.Adapter<MeteoRecyclerView.ViewHolder> {

    private final List<dayMeteoPrevision> mValues;
    private final Context context;
    private MeteoServiceAdapter meteoAdapter = new MeteoServiceAdapter();
    private Calendar c;

    public ViewHolder holder;

    public MeteoRecyclerView(Context context, List<dayMeteoPrevision> items) {
        mValues = items;
        c = Calendar.getInstance();
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.meteo_recyclerview_layout, parent, false);
        holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        nextDay();
        holder.day.setText(c.get(Calendar.DAY_OF_MONTH) + " " + Util.getMonthName("" + c.get(Calendar.MONTH)));
        holder.meteo.setText(mValues.get(position).getWeather_type());
        holder.icon.setImageResource(meteoAdapter.setMeteoIcon(mValues.get(position).getWeather_type()));

    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView day;
        public TextView meteo;
        public ImageView icon;

        public ViewHolder(View view) {
            super(view);
            day = (TextView) view.findViewById(R.id.forecast_day);
            meteo = (TextView) view.findViewById(R.id.forecast_main);
            icon = (ImageView) view.findViewById(R.id.forecast_icon);
        }

        @Override
        public String toString() {
            return super.toString();
        }
    }


    private String getDate(String d){
        String[] separated = d.split(" "); //format: AAAA-MM-DD HH:MM:SS
        String date = separated[0]; //AAAA-MM-DD
        String hour = separated[1]; //HH:MM:SS

        String[] separatedDate = date.split("-");
        String day = separatedDate[2];
        String month = separatedDate[1];

        String monthName = Util.getMonthName(month);

        return day+" "+monthName;
    }

    private String getHour(String d){
        String[] separated = d.split(" "); //format: AAAA-MM-DD HH:MM:SS
        String hour = separated[1]; //HH:MM:SS
        return hour;
    }

    private Boolean isToday(String d){
        Calendar c = Calendar.getInstance();
        Integer day = c.get(Calendar.DAY_OF_MONTH);
        String[] s = d.split(" ");
        Integer is = Integer.parseInt(s[0]);
        return is == day;
    }

    private void nextDay(){
        this.c.add(Calendar.DATE, 1);  // number of days to add
    }
}