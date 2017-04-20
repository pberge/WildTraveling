package dev.wildtraveling.View;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import java.util.List;

import dev.wildtraveling.Domain.Debt;
import dev.wildtraveling.Domain.NoUser;
import dev.wildtraveling.R;

/**
 * Created by pere on 4/19/17.
 */
public class EditExpenseRecyclerView extends RecyclerView.Adapter<EditExpenseRecyclerView.ViewHolder> {

    private List<NoUser> mValues;
    private List<Debt> actualDebts;
    private List<Debt> debts;
    private final Context context;

    public ViewHolder holder;

    public EditExpenseRecyclerView(Context context, List<NoUser> travelers, List<Debt> actualDebts) {
        mValues = travelers;
        this.context = context;
        this.actualDebts = actualDebts;
        debts = actualDebts;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.edit_expense_layout, parent, false);
        holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        NoUser t = mValues.get(position);
        if(t != null) {
            holder.name.setText(t.getName());
        }
        holder.amount.setText(actualDebts.get(position).getAmount()+"");
        if(actualDebts.get(position).getPayed()){
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
        public EditText amount;
        public TextView status;

        public ViewHolder(View view) {
            super(view);
            amount = (EditText) view.findViewById(R.id.DebtAmount);
            name = (TextView) view.findViewById(R.id.debtDeutor);
            status = (TextView) view.findViewById(R.id.debtStatus);
            amount.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    if(!amount.getText().toString().equals("")) {
                        Debt d = new Debt();
                        d.setTravelerId(mValues.get(getAdapterPosition()).getId());
                        d.setAmount(Double.parseDouble(amount.getText().toString()));
                        debts.add(getAdapterPosition(), d);
                    }
                }
            });
        }

        @Override
        public String toString() {
            return super.toString();
        }
    }

    public List<Debt> getDebts(){
        return debts;
    }

}
