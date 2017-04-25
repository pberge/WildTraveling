package dev.wildtraveling.Util;

import android.content.Context;

import dev.wildtraveling.Repository.DebtRepository;
import dev.wildtraveling.Repository.ExpenseRepository;
import dev.wildtraveling.Repository.PersonRepository;
import dev.wildtraveling.Repository.RegisteredTravelerRepository;
import dev.wildtraveling.Repository.NoUserRepository;
import dev.wildtraveling.Repository.TripRepository;
import dev.wildtraveling.Service.ExpenseService;
import dev.wildtraveling.Service.FourSquareAPIImpl;
import dev.wildtraveling.Service.FoursquareAPI;
import dev.wildtraveling.Service.TravelerService;
import dev.wildtraveling.Service.TripService;

/**
 * Created by pere on 3/31/17.
 */
public final class ServiceFactory {

    private static TripService tripService;
    private static TravelerService travelerService;
    private static ExpenseService expenseService;

    private static Integer numServices = 3;
    private static FourSquareAPIImpl foursquareAPI;

    public static TripService getTripService(Context context) {
        if (tripService == null)
            tripService = new TripService(
                    new TripRepository(context));
        return tripService;
    }

    public static TravelerService getTravelerService(Context context) {
        if (travelerService == null)
            travelerService = new TravelerService(
                    new RegisteredTravelerRepository(context), new PersonRepository(context), new NoUserRepository(context));
        return travelerService;
    }

    public static ExpenseService getExpenseService(Context context) {
        if (expenseService == null)
            expenseService = new ExpenseService(
                    new ExpenseRepository(context), new DebtRepository(context));
        return expenseService;
    }

    public static FourSquareAPIImpl getFoursquareAPI() {
        if (foursquareAPI == null) {
            foursquareAPI = new FourSquareAPIImpl();
        }
        return foursquareAPI;
    }

    public static Integer getServiceCount() {
        return numServices;
    }
}
