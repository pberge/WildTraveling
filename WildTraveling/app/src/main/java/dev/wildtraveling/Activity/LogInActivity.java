package dev.wildtraveling.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;

import dev.wildtraveling.Domain.RegisteredTraveler;
import dev.wildtraveling.R;
import dev.wildtraveling.Service.ExpenseService;
import dev.wildtraveling.Service.TravelerService;
import dev.wildtraveling.Service.TripService;
import dev.wildtraveling.Util.ServiceFactory;

public class LogInActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener{

    private GoogleApiClient mGoogleApiClient;
    private static final int GOOGLE = 9001;
    private TravelerService travelerService;
    private TripService tripService;
    private ExpenseService expenseService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        //Initialize services
        travelerService = ServiceFactory.getTravelerService(this);
        tripService = ServiceFactory.getTripService(this);
        expenseService  = ServiceFactory.getExpenseService(this);

        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        // Build a GoogleApiClient with access to the Google Sign-In API and the
        // options specified by gso.
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        // Set the dimensions of the sign-in button.
        SignInButton signInButton = (SignInButton) findViewById(R.id.logInGoogle);
        signInButton.setSize(SignInButton.SIZE_STANDARD);
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });
    }

    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, GOOGLE);
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        System.err.println("FAILED CONNECTION");
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == GOOGLE) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }

    private void handleSignInResult(GoogleSignInResult result) {
        if (result.isSuccess()) {
            // Signed in successfully
            GoogleSignInAccount acct = result.getSignInAccount();
            updateUI(true);
            RegisteredTraveler traveler = travelerService.getUserByEmail(acct.getEmail());
            if(travelerService.existingUser(acct.getEmail())){
                travelerService.setCurrentUser(traveler.getId());
            }else {
                traveler = new RegisteredTraveler();
                traveler.setName(acct.getDisplayName());
                traveler.setEmail(acct.getEmail());
                travelerService.save(traveler);
                travelerService.setCurrentUser(traveler.getId());
            }
            //Launch activity
            Intent intent = new Intent(this, tripsListActivity.class);
            startActivity(intent);
        } else {
            // Signed out
            updateUI(false);

        }
    }

    private void updateUI(boolean signedIn) {
        if (signedIn) {
            findViewById(R.id.logInGoogle).setVisibility(View.GONE);
        } else {
            findViewById(R.id.logInGoogle).setVisibility(View.VISIBLE);
        }
    }



}