package dev.wildtraveling.Repository;

import android.content.Context;

import com.google.firebase.database.DataSnapshot;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import dev.wildtraveling.Domain.Expense;
import dev.wildtraveling.Domain.Note;
import dev.wildtraveling.Util.FirebaseRepository;

/**
 * Created by pere on 5/31/17.
 */
public class NoteRepository extends FirebaseRepository<Note> {

    public NoteRepository(Context context) {
        super(context);
    }

    @Override
    protected Note convert(DataSnapshot data) {
        if (data == null) return null;

        Note note = new Note();
        note.setId(data.getKey());

        for (DataSnapshot d : data.getChildren()) {
            if ("title".equals(d.getKey())) {
                note.setTitle(d.getValue(String.class));
            } else if ("text".equals(d.getKey())) {
                note.setText(d.getValue(String.class));
            }else if ("color".equals(d.getKey())) {
                note.setColor(d.getValue(Integer.class));
            }else if (d.getKey().equals("travelerTags")){
                List<String> tags = new ArrayList<>();
                for (DataSnapshot debt : d.getChildren()) {
                    tags.add(debt.getValue(String.class));
                }
                note.setTravelerTags(tags);
            }else if ("tripId".equals(d.getKey())) {
                note.setTripId(d.getValue(String.class));
            }
        }
        return note;
    }

    @Override
    public String getObjectReference() {
        return "Note";
    }
}

