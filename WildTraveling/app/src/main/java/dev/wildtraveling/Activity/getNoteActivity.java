package dev.wildtraveling.Activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.thebluealliance.spectrum.SpectrumDialog;

import java.util.ArrayList;
import java.util.List;

import dev.wildtraveling.Domain.NoUser;
import dev.wildtraveling.Domain.Note;
import dev.wildtraveling.Domain.Person;
import dev.wildtraveling.R;
import dev.wildtraveling.Service.NoteService;
import dev.wildtraveling.Service.TravelerService;
import dev.wildtraveling.Util.ServiceFactory;
import dev.wildtraveling.View.ParticipantsRecyclerView;

public class getNoteActivity extends AppCompatActivity {

    private TextView title;
    private TextView text;
    private ImageButton palette;
    private ImageButton deleteNote;
    private CardView cardView;

    private View tagsRecyclerView;

    private TravelerService travelerService;
    private NoteService noteService;
    private String noteId;
    private Note note;
    private Boolean color = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_note);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setTitle("");
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        noteId = intent.getStringExtra("noteId");

        noteService = ServiceFactory.getNoteService(getApplicationContext());
        travelerService = ServiceFactory.getTravelerService(getApplicationContext());

        title = (TextView) findViewById(R.id.noteText);
        text = (TextView) findViewById(R.id.noteText);
        palette = (ImageButton) findViewById(R.id.changeNoteColor);
        deleteNote = (ImageButton) findViewById(R.id.deleteNote);
        cardView = (CardView) findViewById(R.id.note_cardview);

        note = noteService.getNoteById(noteId);

        title.setText(note.getTitle());
        text.setText(note.getText());
        cardView.setCardBackgroundColor(note.getColor());

        palette.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showColorDialog();
            }
        });

        deleteNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteDialog().show();
            }
        });

        List<Person> travelers = new ArrayList<>();
        List<String> tags = note.getTravelerTags();
        for (String s: tags){
            NoUser n = travelerService.getTravelerById(s);
            travelers.add(n);
        }

        tagsRecyclerView = findViewById(R.id.tagsRecyclerView);
        ((RecyclerView) tagsRecyclerView).setLayoutManager(new LinearLayoutManager(this));
        ParticipantsRecyclerView part = new ParticipantsRecyclerView(getApplicationContext(), travelers);
        part.notifyDataSetChanged();
        ((RecyclerView) tagsRecyclerView).setAdapter(part);
    }

    private AlertDialog deleteDialog() {
        AlertDialog dialog =new AlertDialog.Builder(this)
                .setTitle("Delete expense")
                .setMessage("Do you want to delete this expense?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        deleteNote();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .create();
        return dialog;
    }

    private void deleteNote() {
        noteService.delete(noteId);
        Intent getTrip = new Intent(this, getTripActivity.class);
        getTrip.putExtra("FRAGMENT", "NOTE");
        startActivity(getTrip);
    }

    private void showColorDialog() {
        new SpectrumDialog.Builder(this)
                .setColors(R.array.colors)
                .setTitle("Choose color")
                .setSelectedColor(note.getColor())
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
        note.setColor(color);
        cardView.setCardBackgroundColor(color);
        this.color = true;
    }

    @Override
    public void onBackPressed() {
        Intent getTrip = new Intent(this, getTripActivity.class);
        getTrip.putExtra("FRAGMENT", "NOTE");
        if(color){
            noteService.update(note);
        }
        startActivity(getTrip);
    }
}
