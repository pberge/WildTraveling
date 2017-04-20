package dev.wildtraveling.Domain;

import dev.wildtraveling.Util.Entity;

/**
 * Created by pere on 4/17/17.
 */
public class Debt implements Entity {

    private String id;
    private String travelerId;
    private double amount;
    private String expenseId;
    private Boolean payed;

    public Debt(String travelerId, double amount) {
        this.travelerId = travelerId;
        this.amount = amount;
        this.payed = false;
    }

    public Debt(){
        this.payed = false;
    }

    public String getTravelerId() {
        return travelerId;
    }

    public void setTravelerId(String travelerId) {
        this.travelerId = travelerId;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    public void setExpenseId(String expenseId) {
        this.expenseId = expenseId;
    }

    public String getExpenseId() {
        return expenseId;
    }

    public Boolean getPayed() {
        return payed;
    }

    public void setPayed(Boolean payed) {
        this.payed = payed;
    }
}
