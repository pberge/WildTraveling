package dev.wildtraveling.Activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import com.thebluealliance.spectrum.SpectrumDialog;

import java.util.ArrayList;
import java.util.List;

import dev.wildtraveling.Domain.NoUser;
import dev.wildtraveling.Domain.Note;
import dev.wildtraveling.R;
import dev.wildtraveling.Service.NoteService;
import dev.wildtraveling.Service.TravelerService;
import dev.wildtraveling.Service.TripService;
import dev.wildtraveling.Util.MailSender;
import dev.wildtraveling.Util.ServiceFactory;
import dev.wildtraveling.View.ChooseTravelersView;
import dev.wildtraveling.View.ParticipantsRecyclerView;
import dev.wildtraveling.View.SharedListRecyclerView;

public class newNoteActivity extends AppCompatActivity {

    private EditText title;
    private EditText text;
    private ImageButton create;
    private ImageButton changeColor;
    private ImageButton share;
    private CardView cardView;

    private NoteService noteService;
    private TripService tripService;

    private ChooseTravelersView travelersView;

    private List<String> items;
    private List<NoUser> travelers;
    private List<NoUser> triptravelers;
    private int color;

    public static final String EMPTY_SEARCH = "No results.";
    private TravelerService travelerService;
    private List<String> tags;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_note);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Note");
        setSupportActionBar(toolbar);

        noteService = ServiceFactory.getNoteService(this);
        tripService = ServiceFactory.getTripService(this);
        travelerService = ServiceFactory.getTravelerService(this);

        title = (EditText) findViewById(R.id.noteTitle);
        text = (EditText) findViewById(R.id.noteText);
        changeColor = (ImageButton) findViewById(R.id.changeNoteColor);
        create = (ImageButton) findViewById(R.id.createNote);
        share = (ImageButton) findViewById(R.id.shareNote);
        cardView = (CardView) findViewById(R.id.create_note_cardview);

        changeColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showColorDialog();
            }
        });
        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createNote();
            }
        });
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareNote();
            }
        });

        travelers = new ArrayList<>();
        triptravelers = new ArrayList<>();
        tags = new ArrayList<>();


        for(String s: tripService.getTripById(tripService.getCurrentTrip()).getParticipants()){
            if(travelerService.getTravelerById(s)!=null) {
                travelers.add(travelerService.getTravelerById(s));
            }
        }

        color = Color.WHITE;

    }

    private void shareNote() {
        selectTravelersDialog().show();
    }

    private void showColorDialog() {
        new SpectrumDialog.Builder(this)
                .setColors(R.array.colors)
                .setTitle("Choose color")
                .setSelectedColor(color)
                .setNegativeButtonText("CANCEL")
                .setDismissOnColorSelected(true)
                .setOutlineWidth(2)
                .setOnColorSelectedListener(new SpectrumDialog.OnColorSelectedListener() {
                    @Override
                    public void onColorSelected(boolean positiveResult, @ColorInt int color) {
                        color(color);
                    }
                }).build().show(getSupportFragmentManager(), "dialog_demo_1");
    }

    private void color(int color) {
        this.color = color;
        cardView.setCardBackgroundColor(color);
    }

    private void createNote() {
        if(text.getText().toString().equals("")){
            //Toast
            return;
        }
        if(title.getText().toString().equals("")){
            //Toast
            return;
        }
        Note note = new Note();
        note.setText(text.getText().toString());
        note.setTitle(title.getText().toString());
        note.setColor(color);
        note.setTripId(tripService.getCurrentTrip());
        note.setTravelerTags(tags);

        sendEmail();

        noteService.save(note);

        Intent intent = new Intent(getApplicationContext(), getTripActivity.class);
        intent.putExtra("FRAGMENT", "NOTE");
        startActivity(intent);
    }

    private void sendEmail() {
        for(NoUser u: triptravelers) {
            MailSender sm = new MailSender(this, "alsumo95@gmail.com", "WildTraveling note from "+travelerService.getUserById(travelerService.getCurrentUser()).getName(),title.getText().toString()+"\n\n"+text.getText().toString());
            sm.execute();
        }
    }

    private Dialog selectTravelersDialog()
    {
        AlertDialog.Builder d = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.share_layout, null);
        RecyclerView recyclerView = (RecyclerView) dialogView.findViewById(R.id.shareRecyclerView);
        assert recyclerView != null;
        travelersView = new ChooseTravelersView(getApplicationContext(), travelers);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setAdapter(travelersView);
        d.setView(dialogView);
        d.setTitle("Share your note with:");
        d.setPositiveButton("Done", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                for (NoUser u : travelersView.getSelectedUsersList()) {
                    if (!triptravelers.contains(u.getId())) {
                        triptravelers.add(u);
                        tags.add(u.getId());
                    }
                }
                RecyclerView recyclerView = (RecyclerView) findViewById(R.id.sharedNewNoteRecyclerView);
                assert recyclerView != null;
                SharedListRecyclerView participantsRecyclerView = new SharedListRecyclerView(getApplicationContext(), triptravelers);
                recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                recyclerView.setAdapter(participantsRecyclerView);
            }
        });
        d.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                }

        );

        return d.create();
    }


}
