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

import java.util.ArrayList;
import java.util.List;

import dev.wildtraveling.Domain.Debt;
import dev.wildtraveling.Domain.NoUser;
import dev.wildtraveling.R;

/**
 * Created by pere on 4/17/17.
 */
public class NewExpenseRecyclerView extends RecyclerView.Adapter<NewExpenseRecyclerView.ViewHolder> {

    private List<NoUser> mValues;
    private List<Debt> debts;
    private String expenseId;
    private final Context context;

    public ViewHolder holder;

    public NewExpenseRecyclerView(Context context, List<NoUser> travelers) {
        mValues = travelers;
        this.context = context;
        debts = new ArrayList<>();
        for(NoUser u:mValues){
            Debt d = new Debt();
            d.setTravelerId(u.getId());
            d.setAmount(0.0);
            debts.add(d);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.new_expense_list_layout, parent, false);
        holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        NoUser t = mValues.get(position);
        if(t != null) {
            holder.name.setText(t.getName());
        }
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView name;
        public EditText amount;

        public ViewHolder(View view) {
            super(view);
            amount = (EditText) view.findViewById(R.id.newExpenseDebtAmount);
            name = (TextView) view.findViewById(R.id.newExpenseTraveler);
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
                        /*Debt d = new Debt();
                        d.setTravelerId(mValues.get(getAdapterPosition()).getId());
                        d.setAmount(Double.parseDouble(amount.getText().toString()));
                        debts.add(getAdapterPosition(), d);*/
                        debts.get(getAdapterPosition()).setAmount(Double.parseDouble(amount.getText().toString()));
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
        List<Debt> res = new ArrayList<>();
        for(Debt d: debts){
            if (d.getAmount() != 0.0){
                res.add(d);
            }
        }
        return res;
    }

}
