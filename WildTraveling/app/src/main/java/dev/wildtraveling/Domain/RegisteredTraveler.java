package dev.wildtraveling.Domain;

/**
 * Created by pere on 4/10/17.
 */
public class RegisteredTraveler extends Person {

    private String emergencyContact;

    public RegisteredTraveler() {
    }

    public RegisteredTraveler(String name, String email, String emergencyContact) {
        super(name,email);
        this.emergencyContact = emergencyContact;
    }


    public String getEmergencyContact(){
        return emergencyContact;
    }

    public void  setEmergencyContact(String p){
        this.emergencyContact = p;
    }
}
