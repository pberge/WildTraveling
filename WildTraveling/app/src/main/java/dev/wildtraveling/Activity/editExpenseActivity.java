package dev.wildtraveling.Activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import dev.wildtraveling.Domain.Debt;
import dev.wildtraveling.Domain.Expense;
import dev.wildtraveling.Domain.NoUser;
import dev.wildtraveling.Domain.Trip;
import dev.wildtraveling.R;
import dev.wildtraveling.Service.ExpenseService;
import dev.wildtraveling.Service.TravelerService;
import dev.wildtraveling.Service.TripService;
import dev.wildtraveling.Util.RecyclerItemClickListener;
import dev.wildtraveling.Util.ServiceFactory;
import dev.wildtraveling.View.EditExpenseRecyclerView;
import dev.wildtraveling.View.NewExpenseRecyclerView;

public class editExpenseActivity extends AppCompatActivity {

    private EditText amount;
    private EditText motive;
    private EditText localization;
    private View recyclerView;
    private View newDebtsRecyclerView;

    private List<NoUser> travelers;
    private List<NoUser> debtsTravelers;
    private Expense expense;
    private Trip trip;
    private TripService tripService;
    private TravelerService travelerService;
    private ExpenseService expenseService;
    private NewExpenseRecyclerView newexpenses;
    private EditExpenseRecyclerView editExpense;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_expense);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Edit expense");
        setSupportActionBar(toolbar);

        tripService = ServiceFactory.getTripService(this);
        travelerService = ServiceFactory.getTravelerService(this);
        expenseService  = ServiceFactory.getExpenseService(this);

        Intent intent = getIntent();
        expense = expenseService.get(intent.getStringExtra("idExpense"));
        trip = tripService.get(tripService.getCurrentTrip());

        //Get participants
        travelers = new ArrayList<>();
        debtsTravelers = new ArrayList<>();

        for(String p: trip.getParticipants()){
            if(!p.equals(travelerService.getCurrentUser())) {
                if(expenseService.hasDebt(expense.getId(),p)){
                    debtsTravelers.add(travelerService.getTravelerById(p));
                }else {
                    travelers.add(travelerService.getTravelerById(p));
                }
            }
        }

        //Falta canviar
        amount = (EditText) findViewById(R.id.editExpenseAmount);
        motive = (EditText) findViewById(R.id.editExpenseMotive);
        localization = (EditText) findViewById(R.id.editExpenseLocalization);
        recyclerView = findViewById(R.id.newExpenseRecyclerView);
        newDebtsRecyclerView = findViewById(R.id.editExpenseRecyclerView);

        amount.setText(expense.getAmount().toString());
        motive.setText(expense.getItem().toString());
        localization.setText(expense.getLocalization().toString());

        ((RecyclerView) recyclerView).setLayoutManager(new LinearLayoutManager(this));
        newexpenses = new NewExpenseRecyclerView(getApplicationContext(),travelers);
        ((RecyclerView) recyclerView).setAdapter(newexpenses);

        ((RecyclerView) newDebtsRecyclerView).setLayoutManager(new LinearLayoutManager(this));
        editExpense = new EditExpenseRecyclerView(getApplicationContext(),debtsTravelers,expenseService.getDebtsByExpense(expense.getId()));
        ((RecyclerView) newDebtsRecyclerView).setAdapter(editExpense);
        ((RecyclerView) newDebtsRecyclerView).addOnItemTouchListener(new RecyclerItemClickListener(this, (RecyclerView) newDebtsRecyclerView, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

            }

            @Override
            public void onItemLongClick(View view, int position) { //Delete dialog
                AlertDialog d = deleteDebt(position);
                d.show();
            }
        }));
    }

    private AlertDialog deleteDebt(final int position) {
        AlertDialog dialog =new AlertDialog.Builder(this)
                .setTitle("Delete expense")
                .setMessage("Do you want to delete this debt?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        delete(position);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .create();
        return dialog;
    }

    private void delete(int position) {
        expenseService.deleteDebt(expenseService.getDebtsByExpense(expense.getId()).get(position).getId());
        newexpenses.notifyDataSetChanged();
    }

    @Override
    public void onBackPressed() {
        updateExpense();
    }

    private void updateExpense(){
        expense.setItem(motive.getText().toString());
        expense.setTripId(tripService.getCurrentTrip());
        expense.setAmount(Double.parseDouble(amount.getText().toString()));
        expense.setLocalization(localization.getText().toString());
        expenseService.update(expense);

        //deutes nous
        for (Debt d: newexpenses.getDebts()){
            //save debt
            d.setExpenseId(expense.getId());
            d.setPayed(false);
            expenseService.save(d);
        }
        //deutes a editar
        int i = 0;
        for (Debt d: expenseService.getDebtsByExpense(expense.getId())){
            //update debt
            d.setAmount(editExpense.getDebts().get(i).getAmount());
            expenseService.update(d);
            i++;
        }

        Toast.makeText(getApplicationContext(), "Expense modified",
                Toast.LENGTH_LONG).show();

        Intent intent = new Intent(getApplicationContext(), getExpenseActivity.class);
        intent.putExtra("expenseId",expense.getId());
        startActivity(intent);
    }
}
