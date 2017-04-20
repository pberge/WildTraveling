package dev.wildtraveling.View;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import dev.wildtraveling.Domain.Debt;
import dev.wildtraveling.Domain.Destination;
import dev.wildtraveling.R;
import dev.wildtraveling.Service.TravelerService;
import dev.wildtraveling.Util.ServiceFactory;

/**
 * Created by pere on 4/18/17.
 */
public class DebtsRecyclerView extends RecyclerView.Adapter<DebtsRecyclerView.ViewHolder> {

    private final List<Debt> mValues;
    private final Context context;
    private TravelerService travelerService;

    public ViewHolder holder;

    public DebtsRecyclerView(Context context, List<Debt> items) {
        mValues = items;
        this.context = context;
        travelerService = ServiceFactory.getTravelerService(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.debt_list_layout, parent, false);
        holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.name.setText(travelerService.getTravelerById(mValues.get(position).getTravelerId()).getName());
        holder.amount.setText(""+mValues.get(position).getAmount());
        if(mValues.get(position).getPayed()){
            holder.status.setText("SOLVED");
            holder.status.setTextColor(context.getResources().getColor(R.color.green));
        }
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView name;
        public TextView amount;
        public TextView status;

        public ViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.debtDeutor);
            amount = (TextView) view.findViewById(R.id.DebtAmount);
            status = (TextView) view.findViewById(R.id.debtStatus);
        }

        @Override
        public String toString() {
            return super.toString();
        }
    }

}
