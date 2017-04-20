package dev.wildtraveling.View;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import dev.wildtraveling.Domain.NoUser;
import dev.wildtraveling.Domain.Person;
import dev.wildtraveling.R;

/**
 * Created by pere on 3/30/17.
 */
public class ParticipantsRecyclerView extends RecyclerView.Adapter<ParticipantsRecyclerView.ViewHolder> {

    private final List<Person> mValues;
    private final Context context;

    public ViewHolder holder;

    public ParticipantsRecyclerView(Context context,
                                    List<Person> items) {
        mValues = items;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.participants_item_list_layout, parent, false);
        holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        System.out.println("id participant: "+mValues.get(position).getId());
        holder.name.setText(mValues.get(position).getName());
        holder.email.setText(mValues.get(position).getEmail());
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView name;
        public TextView email;

        public ViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.name_participant_itemlist);
            email = (TextView) view.findViewById(R.id.email_participant_itemlist);
        }

        @Override
        public String toString() {
            return super.toString();
        }
    }

}
