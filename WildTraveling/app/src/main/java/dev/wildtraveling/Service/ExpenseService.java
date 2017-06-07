package dev.wildtraveling.Service;

import java.util.ArrayList;
import java.util.List;

import dev.wildtraveling.Domain.Debt;
import dev.wildtraveling.Domain.Expense;
import dev.wildtraveling.Util.Repository;
import dev.wildtraveling.Util.Service;

/**
 * Created by pere on 4/17/17.
 */
public class ExpenseService extends Service<Expense> {

private int loaded=0;
private int loadNeed=1;
private String currentUser;
private final Repository<Debt> debtRepository;

public ExpenseService(Repository<Expense>repository, Repository<Debt> debtRepository){
        super(repository);
        this.debtRepository = debtRepository;
    }

    public Expense save(Expense item){
        if(repository==null){
        }
        Expense expense = repository.insert(item);
        return expense;
    }

    public Debt save (Debt item){
        if(debtRepository==null){
        }
        Debt debt = debtRepository.insert(item);
        return debt;
    }


    public List<Expense> getExpenses() {
        return repository.all();
    }

    public List<Debt> getDebts(){
        return debtRepository.all();
    }


    public List<Expense> getExpenseByTripId(String currentTrip) {
        List<Expense> expenses = new ArrayList<>();
        for(Expense e: repository.all()){
            if(e.getTripId().equals(currentTrip)){
                expenses.add(e);
            }
        }
        return expenses;
    }

    public Double getTotalAmountByTrip(String currentTrip){
        Double res = 0.0;
        for(Expense e: getExpenseByTripId(currentTrip)){
            res += e.getAmount();
        }
        return res;
    }

    public Expense getExpenseById(String expenseId) {
        return repository.get(expenseId);
    }

    public List<Debt> getDebtsByExpense(String id) {
        List<Debt> debts = new ArrayList<>();
        for(Debt d: debtRepository.all()){
            if(d.getExpenseId().equals(id)){
                debts.add(d);
            }
        }
        return debts;
    }

    public List<String> getDebtsIdByExpense(String id) {
        List<String> debts = new ArrayList<>();
        for(Debt d: debtRepository.all()){
            if(d.getExpenseId().equals(id)){
                debts.add(d.getId());
            }
        }
        return debts;
    }

    public void update(Debt d) {
        debtRepository.update(d);
    }

    public void update(Expense e) {
        System.out.println("id expense a actualitzar: " + e.getId());
        repository.update(e);
    }

    public void deleteDebtsFromExpense(String id) {
        for (String debtId: getDebtsIdByExpense(id)){
            debtRepository.delete(debtId);
        }
    }

    public boolean hasDebt(String expenseId, String participantId) {
        for(Debt d: getDebtsByExpense(expenseId)){
            if(d.getTravelerId().equals(participantId)){
                return true;
            }
        }
        return false;
    }

    public void deleteDebt(String id) {
        for(Debt d: debtRepository.all()){
            if(d.getId().equals(id)){
                debtRepository.delete(d.getId());
            }
        }
    }

    public void deleteExpensesByTrip(String currentTrip) {
        for(Expense e: repository.all()){
            if (e.getTripId().equals(currentTrip)){
                deleteDebtsFromExpense(e.getId());
                repository.delete(e.getId());
            }
        }
    }
}
