package dev.wildtraveling.Service;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import java.util.List;

import dev.wildtraveling.Domain.Person;
import dev.wildtraveling.Domain.RegisteredTraveler;
import dev.wildtraveling.Domain.NoUser;
import dev.wildtraveling.Util.Repository;
import dev.wildtraveling.Util.Service;

/**
 * Created by pere on 4/1/17.
 */
public class TravelerService extends Service<RegisteredTraveler> {

    private int loaded=0;
    private int loadNeed=1;
    private String currentUser;
    private final Repository<Person> personRepository;
    private final Repository<NoUser> noUserRepository;

    public TravelerService(Repository<RegisteredTraveler>repository, Repository<Person> personRepository, Repository<NoUser> travelerRepository){
        super(repository);
        this.personRepository = personRepository;
        this.noUserRepository = travelerRepository;
    }

    public RegisteredTraveler save(RegisteredTraveler item){
        if(repository==null){
        }
        RegisteredTraveler traveler = repository.insert(item);
        return traveler;
    }

    public Person save (Person item){
        if(personRepository==null){
        }
        Person traveler = personRepository.insert(item);
        return traveler;
    }

    public NoUser save (NoUser item){
        if(noUserRepository ==null){
        }
        NoUser traveler = noUserRepository.insert(item);
        return traveler;
    }

    public String getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(String currentUser) {
        this.currentUser = currentUser;
    }


    public List<RegisteredTraveler> getRegisteredUsers() {
        return repository.all();
    }

    public RegisteredTraveler getUserByEmail(String email) {
        for(RegisteredTraveler traveler:repository.all()){
            if(traveler.getEmail().equals(email)){
                return traveler;
            }
        }
        return null;
    }

    public RegisteredTraveler getUserById(String id) {
        for(RegisteredTraveler traveler:repository.all()){
            if(traveler.getId().equals(id)){
                return traveler;
            }
        }
        return null;
    }

    public NoUser getTravelerById(String id) {
        for(NoUser traveler: noUserRepository.all()){
            if(traveler.getId().equals(id)){
                return traveler;
            }
        }
        return null;
    }


    public NoUser getTravelerByEmail(String email) {
        for(NoUser traveler: noUserRepository.all()){
            if(traveler.getEmail().equals(email)){
                return traveler;
            }
        }
        return null;
    }

    public Boolean existingUser(String email) {
        for(RegisteredTraveler traveler:repository.all()){
            if(traveler.getEmail().equals(email)){
                return true;
            }
        }
        return false;
    }


    public boolean isCurrentTraveler(String currentUser) {
        NoUser t = getTravelerById(currentUser);
        if(t.getEmail().equals(getUserById(getCurrentUser()).getEmail())){
            return true;
        }
        return false;
    }

    public Person getPersonById(String contactPerson) {
        for(Person person: personRepository.all()){
            if(person.getId().equals(contactPerson)){
                return person;
            }
        }
        return null;
    }
}
