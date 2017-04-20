package dev.wildtraveling.Repository;

import android.content.Context;

import com.google.firebase.database.DataSnapshot;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import dev.wildtraveling.Domain.Expense;
import dev.wildtraveling.Util.FirebaseRepository;

/**
 * Created by pere on 4/17/17.
 */
public class ExpenseRepository extends FirebaseRepository<Expense> {

    public ExpenseRepository(Context context) {
        super(context);
    }

    @Override
    protected Expense convert(DataSnapshot data) {
        if (data == null) return null;

        Expense expense = new Expense();
        expense.setId(data.getKey());

        for (DataSnapshot d : data.getChildren()) {
            if ("date".equals(d.getKey())) {
                expense.setDate(d.getValue(Date.class));
            } else if ("tripId".equals(d.getKey())) {
                expense.setTripId(d.getValue(String.class));
            }else if ("amount".equals(d.getKey())) {
                expense.setAmount(d.getValue(Double.class));
            }else if ("localization".equals(d.getKey())) {
                expense.setLocalization(d.getValue(String.class));
            }else if ("item".equals(d.getKey())) {
                expense.setItem(d.getValue(String.class));
            }else if (d.getKey().equals("debtsIds")){
                List<String> debts = new ArrayList<>();
                for (DataSnapshot debt : d.getChildren()) {
                    debts.add(debt.getValue(String.class));
                }
                expense.setDebtsIds(debts);
            }
        }
        return expense;
    }

    @Override
    public String getObjectReference() {
        return "Expense";
    }
}
