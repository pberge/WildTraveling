package dev.wildtraveling.View;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import dev.wildtraveling.Domain.NoUser;
import dev.wildtraveling.R;
import dev.wildtraveling.Service.TravelerService;
import dev.wildtraveling.Util.ServiceFactory;

/**
 * Created by pere on 6/5/17.
 */
public class ChooseTravelersView extends RecyclerView.Adapter<ChooseTravelersView.ViewHolder> {

    private final List<NoUser> users;
    private List<NoUser> selected = new ArrayList<>();
    private final Context context;
    private TravelerService travelerService;

    public ViewHolder holder;

    public ChooseTravelersView(Context context,
                               List<NoUser> items) {
        this.users = items;
        this.context = context;
        this.selected = new ArrayList<>();
        travelerService = ServiceFactory.getTravelerService(context);
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.shared_item_list, parent, false);
        holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.userName.setText(users.get(position).getName());
        holder.userEmail.setText(users.get(position).getEmail());

    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public NoUser user;
        public TextView userEmail;
        public TextView userName;

        boolean isSelected = false;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            userName = (TextView) view.findViewById(R.id.name_participant_itemlist);
            userEmail = (TextView) view.findViewById(R.id.email_participant_itemlist);

            mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(!isSelected) {
                        isSelected = true;
                        selected.add(travelerService.getTravelerByEmail(userEmail.getText().toString()));
                        v.setBackgroundColor(context.getResources().getColor(R.color.colorAccentTransparent));
                    }
                    else{
                        v.setBackgroundColor(context.getResources().getColor(R.color.white));
                        int i=0;
                        for (NoUser u : selected){
                            if(u.getEmail().equals(userEmail.getText().toString())){
                                selected.remove(i);
                            }
                            i++;
                        }
                        isSelected = false;
                    }
                }
            });

        }

        @Override
        public String toString() {
            return super.toString();
        }


    }

    public List<NoUser> getSelectedUsersList(){
        return selected;
    }


}