package dev.wildtraveling.Service;

import java.util.ArrayList;
import java.util.List;

import dev.wildtraveling.Domain.Debt;
import dev.wildtraveling.Domain.Expense;
import dev.wildtraveling.Domain.Note;
import dev.wildtraveling.Domain.Trip;
import dev.wildtraveling.Util.Repository;
import dev.wildtraveling.Util.Service;

/**
 * Created by pere on 5/31/17.
 */
public class NoteService extends Service<Note> {

    private int loaded=0;
    private int loadNeed=1;

    public NoteService(Repository<Note> repository){
        super(repository);
    }

    public Note save(Note item){
        if(repository==null){
            throw new UnsupportedOperationException("Service needs to be created with the DishRepository to function");
        }
        Note note=repository.insert(item);
        return note;
    }

    public List<Note> getNotesByTripId(String tripId){
        List<Note> notes = new ArrayList<>();
        if(!repository.all().isEmpty()) {
            for (Note t : repository.all()) {
                System.out.println("trip id: "+t.getTripId());
                if (t.getTripId().equals(tripId)) {
                    notes.add(t);
                }
            }
        }
        return notes;
    }

    public void update(Note e) {
        repository.update(e);
    }

    public Note getNoteById(String noteId) {
        for (Note t: repository.all()){
            if(t.getId().equals(noteId)){
                return t;
            }
        }
        return null;
    }

    public void deleteNote(String id) {
        for(Note d: repository.all()){
            if(d.getId().equals(id)){
                repository.delete(d.getId());
            }
        }
    }
}