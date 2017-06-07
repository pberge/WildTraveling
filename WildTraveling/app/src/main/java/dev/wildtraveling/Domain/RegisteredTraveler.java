package dev.wildtraveling.Domain;

import android.net.Uri;

/**
 * Created by pere on 4/10/17.
 */
public class RegisteredTraveler extends Person {

    private String emergencyContact;
    private Uri photoUrl;

    public RegisteredTraveler() {
    }

    public RegisteredTraveler(String name, String email, String emergencyContact, Uri photoUrl) {
        super(name,email);
        this.emergencyContact = emergencyContact;
        this.photoUrl = photoUrl;
    }


    public String getEmergencyContact(){
        return emergencyContact;
    }

    public void  setEmergencyContact(String p){
        this.emergencyContact = p;
    }

    public Uri getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(Uri photoUrl) {
        this.photoUrl = photoUrl;
    }
}
