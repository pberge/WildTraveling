package dev.wildtraveling.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

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
import dev.wildtraveling.Util.ServiceFactory;
import dev.wildtraveling.View.NewExpenseRecyclerView;

public class newExpenseActivitiy extends AppCompatActivity {

    private EditText amount;
    private EditText motive;
    private EditText localization;
    private ImageButton create;
    private View recyclerView;

    private List<NoUser> travelers;
    private Trip trip;
    private TripService tripService;
    private TravelerService travelerService;
    private ExpenseService expenseService;
    private NewExpenseRecyclerView newexpenses;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_expense_activitiy);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        tripService = ServiceFactory.getTripService(this);
        travelerService = ServiceFactory.getTravelerService(this);
        expenseService  = ServiceFactory.getExpenseService(this);

        trip = tripService.getTripById(tripService.getCurrentTrip());

        //Get participants
        travelers = new ArrayList<>();

        for(String p: trip.getParticipants()){
            if(!p.equals(travelerService.getCurrentUser())) {
                travelers.add(travelerService.getTravelerById(p));
            }
        }

        amount = (EditText) findViewById(R.id.newExpenseAmount);
        motive = (EditText) findViewById(R.id.newExpenseMotive);
        localization = (EditText) findViewById(R.id.newExpenseLocalization);
        recyclerView = findViewById(R.id.newExpenseRecyclerView);

        ((RecyclerView) recyclerView).setLayoutManager(new LinearLayoutManager(this));
        newexpenses = new NewExpenseRecyclerView(getApplicationContext(),travelers);
        ((RecyclerView) recyclerView).setAdapter(newexpenses);

        create = (ImageButton) findViewById(R.id.createExpense);
        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newExpense();
            }
        });
    }

    private void newExpense(){
        Expense expense = new Expense();
        expense.setItem(motive.getText().toString());
        expense.setTripId(tripService.getCurrentTrip());
        expense.setAmount(Double.parseDouble(amount.getText().toString()));
        expense.setLocalization(localization.getText().toString());
        Calendar now = Calendar.getInstance();
        expense.setDate(now.getTime());
        expenseService.save(expense);

        for (Debt d: newexpenses.getDebts()){
            //save debt
            d.setExpenseId(expense.getId());
            d.setPayed(false);
            expenseService.save(d);
        }

        Intent intent = new Intent(getApplicationContext(), expenseListActivity.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), expenseListActivity.class);
        startActivity(intent);
    }
}
