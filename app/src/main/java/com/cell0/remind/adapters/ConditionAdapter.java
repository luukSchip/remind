package com.cell0.remind.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.cell0.remind.R;
import com.cell0.remind.models.Condition;
import com.cell0.remind.models.TimeConditionConfiguration;
import com.cell0.remind.utils.Strings;

import java.util.List;

import io.realm.Realm;

/**
 * Created by Luuk on 26-8-2015.
 */
public class ConditionAdapter extends RealmRecyclerAdapter{

    @Override
    public ConditionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_condition_time, parent, false);
        ConditionViewHolder vh = new ConditionViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(RealmRecyclerAdapter.ViewHolder holder, int position) {
        ConditionViewHolder cViewHolder = (ConditionViewHolder) holder;
        Condition condition = (Condition) data.get(position);
        TimeConditionConfiguration tcc = condition.getTimeConditionConfiguration();

        cViewHolder.getTitleTextView().setText("Time Condition");
        cViewHolder.getDescriptionTextView()
                .setText(Strings.convertTimeConditionConfigurationToString(tcc));
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View itemView) {
            super(itemView);
        }
    }

    class ConditionViewHolder extends RealmRecyclerAdapter.ViewHolder{
        TextView titleTextView;
        TextView descriptionTextView;
        public ConditionViewHolder(View itemView) {
            super(itemView);
            titleTextView = (TextView) itemView.findViewById(R.id.title);
            descriptionTextView = (TextView) itemView.findViewById(R.id.description);
        }

        public TextView getTitleTextView() {
            return titleTextView;
        }

        public TextView getDescriptionTextView() {
            return descriptionTextView;
        }
    }
}
