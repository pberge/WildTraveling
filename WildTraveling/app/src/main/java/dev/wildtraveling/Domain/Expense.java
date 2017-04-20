package dev.wildtraveling.Domain;



import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import dev.wildtraveling.Util.Entity;

/**
 * Created by pere on 4/16/17.
 */
public class Expense implements Entity {

    private String id;
    private Date date;
    private String tripId;
    private Double amount;
    private String item;
    private String localization;
    private List<String> debtsIds;

    public Expense() {
        debtsIds = new ArrayList<>();
    }

    public Expense(String id, Date date, String tripId, Double amount, String item, List<String> debtsIds) {
        this.id = id;
        this.date = date;
        this.tripId = tripId;
        this.amount = amount;
        this.item = item;
        this.debtsIds = debtsIds;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getTripId() {
        return tripId;
    }

    public void setTripId(String tripId) {
        this.tripId = tripId;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public String getLocalization() {
        return localization;
    }

    public void setLocalization(String localization) {
        this.localization = localization;
    }

    public List<String> getDebtsIds() {
        return debtsIds;
    }

    public void setDebtsIds(List<String> debtsIds) {
        this.debtsIds = debtsIds;
    }



}
