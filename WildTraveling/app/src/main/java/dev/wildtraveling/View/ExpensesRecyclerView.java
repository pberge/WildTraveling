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
import dev.wildtraveling.Util.Util;

/**
 * Created by pere on 4/16/17.
 */
public class ExpensesRecyclerView extends RecyclerView.Adapter<ExpensesRecyclerView.ViewHolder> {

    private final List<Expense> mValues;
    private final Context context;

    public ViewHolder holder;

    public ExpensesRecyclerView(Context context, List<Expense> items) {
        mValues = items;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.expenses_list_layout, parent, false);
        holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.amount.setText(mValues.get(position).getAmount().toString());
        holder.motive.setText(mValues.get(position).getItem());
        holder.localization.setText(mValues.get(position).getLocalization());
        holder.date.setText(Util.obtainDateString(mValues.get(position).getDate()));


    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView amount;
        public TextView motive;
        public TextView localization;
        public TextView date;
        public ImageButton delete;

        public ViewHolder(View view) {
            super(view);
            amount = (TextView) view.findViewById(R.id.amountExpense);
            motive = (TextView) view.findViewById(R.id.expenseMotive);
            localization = (TextView) view.findViewById(R.id.expenseLocalization);
            date = (TextView) view.findViewById(R.id.expenseDate);
        }

        @Override
        public String toString() {
            return super.toString();
        }
    }

}
