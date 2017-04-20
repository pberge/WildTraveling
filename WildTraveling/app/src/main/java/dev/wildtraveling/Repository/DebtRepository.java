package dev.wildtraveling.Repository;

import android.content.Context;

import com.google.firebase.database.DataSnapshot;

import dev.wildtraveling.Domain.Debt;
import dev.wildtraveling.Util.FirebaseRepository;

/**
 * Created by pere on 4/17/17.
 */
public class DebtRepository extends FirebaseRepository<Debt> {

    public DebtRepository(Context context) {
        super(context);
    }

    @Override
    protected Debt convert(DataSnapshot data) {
        if (data == null) return null;

        Debt debt = new Debt();
        debt.setId(data.getKey());

        for (DataSnapshot d : data.getChildren()) {
            if ("travelerId".equals(d.getKey())) {
                debt.setTravelerId(d.getValue(String.class));
            }else if ("amount".equals(d.getKey())) {
                debt.setAmount(d.getValue(Double.class));
            }else if ("payed".equals(d.getKey())) {
                debt.setPayed(d.getValue(Boolean.class));
            }else if ("expenseId".equals(d.getKey())) {
                debt.setExpenseId(d.getValue(String.class));
            }
        }
        return debt;
    }

    @Override
    public String getObjectReference() {
        return "Debt";
    }
}

