package dev.wildtraveling.View;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import dev.wildtraveling.Domain.Note;
import dev.wildtraveling.R;

/**
 * Created by pere on 6/4/17.
 */
public class NotesRecyclerView extends RecyclerView.Adapter<NotesRecyclerView.ViewHolder> {

    private final List<Note> mValues;
    private final Context context;

    public ViewHolder holder;


    public NotesRecyclerView(Context context, List<Note> items) {
        mValues = items;
        this.context = context;
        System.out.println("mida notes a recyclerView: "+items.size());
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.notes_recycler_view, parent, false);
        holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.title.setText(mValues.get(position).getTitle().toString());
        holder.text.setText(mValues.get(position).getText().toString());
        holder.v.setCardBackgroundColor(mValues.get(position).getColor());
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        public TextView text;
        public CardView v;

        public ViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.noteTitleRecycler);
            text = (TextView) view.findViewById(R.id.noteTextRecycler);
            v = (CardView) view.findViewById(R.id.noteCardView);
            }

        @Override
        public String toString() {
            return super.toString();
        }
    }

}

