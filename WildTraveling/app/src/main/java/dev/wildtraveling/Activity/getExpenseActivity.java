package dev.wildtraveling.Activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.List;

import dev.wildtraveling.Domain.Debt;
import dev.wildtraveling.Domain.Expense;
import dev.wildtraveling.R;
import dev.wildtraveling.Service.ExpenseService;
import dev.wildtraveling.Service.TripService;
import dev.wildtraveling.Util.RecyclerItemClickListener;
import dev.wildtraveling.Util.ServiceFactory;
import dev.wildtraveling.Util.Util;
import dev.wildtraveling.View.DebtsRecyclerView;

public class getExpenseActivity extends AppCompatActivity {

    private TextView amount;
    private TextView localization;
    private TextView date;
    private View debtsRecyclerView;
    private ImageButton delete;
    private ImageButton edit;

    private ExpenseService expenseService;
    private TripService tripService;
    private Expense expense;
    List<Debt> debts;
    DebtsRecyclerView part;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_expense);

        Intent intent = getIntent();
        tripService = ServiceFactory.getTripService(this);
        expenseService = ServiceFactory.getExpenseService(this);

        expense = expenseService.getExpenseById(intent.getStringExtra("expenseId"));

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(expense.getItem());
        setSupportActionBar(toolbar);

        debtsRecyclerView = findViewById(R.id.getDebtsRecyclerView);
        amount = (TextView) findViewById(R.id.amountExpense);
        localization = (TextView) findViewById(R.id.expenseLocalization);
        date = (TextView) findViewById(R.id.expenseDate);
        delete = (ImageButton) findViewById(R.id.deleteExpense);
        edit = (ImageButton) findViewById(R.id.editExpense);

        amount.setText(expense.getAmount().toString());
        localization.setText(expense.getLocalization());
        date.setText(Util.obtainDateString(expense.getDate()));
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog d = deleteDialog();
                d.show();
            }
        });
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), editExpenseActivity.class);
                intent.putExtra("idExpense",expense.getId());
                startActivity(intent);
            }
        });

        debts = expenseService.getDebtsByExpense(expense.getId());

        ((RecyclerView) debtsRecyclerView).setLayoutManager(new LinearLayoutManager(this));
        part = new DebtsRecyclerView(getApplicationContext(),debts);
        part.notifyDataSetChanged();
        ((RecyclerView) debtsRecyclerView).setAdapter(part);

        ((RecyclerView) debtsRecyclerView).addOnItemTouchListener(new RecyclerItemClickListener(this, (RecyclerView) debtsRecyclerView, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

            }

            @Override
            public void onItemLongClick(View view, int position) { //Delete dialog
                AlertDialog d = payedDebt(position);
                d.show();
            }
        }));

    }

    private AlertDialog deleteDialog() {
        AlertDialog dialog =new AlertDialog.Builder(this)
                .setTitle("Delete expense")
                .setMessage("Do you want to delete this expense?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        delete();
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

    private void delete() {
        expenseService.deleteDebtsFromExpense(expense.getId());
        expenseService.delete(expense.getId());
        Intent intent = new Intent(getApplicationContext(), expenseListActivity.class);
        startActivity(intent);
    }

    private AlertDialog payedDebt(final int pos)
    {
        AlertDialog dialog =new AlertDialog.Builder(this)
                .setTitle("Payed debt")
                .setMessage("Do you want to mark this debt as payed?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        debts.get(pos).setPayed(true);
                        expenseService.update(debts.get(pos));
                        part.notifyDataSetChanged();
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

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), getTripActivity.class);
        intent.putExtra("FRAGMENT", "EXPENSE");
        startActivity(intent);
    }
}
