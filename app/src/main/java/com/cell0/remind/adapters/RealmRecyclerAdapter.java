package com.cell0.remind.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cell0.remind.R;

import java.util.List;

import io.realm.RealmObject;

/**
 * Created by luuk on 9-9-2015.
 */
public abstract class RealmRecyclerAdapter extends RecyclerView.Adapter<RealmRecyclerAdapter.ViewHolder>{

    public List<? extends RealmObject> data = null;
    private DeleteListener deleteListener;

    public RealmRecyclerAdapter() {
    }

    public void setData(List<? extends RealmObject> details) {
        this.data = details;
    }


    public RealmObject getItem(int position) {
        if (data == null || data.get(position) == null) {
            return null;
        }
        return data.get(position);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(getListLayoutResource() , parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        if(data == null){
            return 0;
        }
        return data.size();
    }

    public int getListLayoutResource() {
        return R.layout.reminderset_listitem;
    }


    private class OnClickDeleteButton implements View.OnClickListener {
        int position;

        public OnClickDeleteButton(int position) {
            this.position = position;
        }

        @Override
        public void onClick(View v) {
            if(deleteListener != null){
                deleteListener.onDeleteReminderSet(position);
            }
        }
    }

    public interface DeleteListener {
        public void onDeleteReminderSet(int position);
    }

    public void setDeleteListener(DeleteListener deleteListener) {
        this.deleteListener = deleteListener;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View itemView) {
            super(itemView);
        }
    }
}
