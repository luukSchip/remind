package com.cell0.remind.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.cell0.remind.R;
import com.cell0.remind.models.Condition;

import java.util.List;

/**
 * Created by Luuk on 26-8-2015.
 */
public class ConditionAdapter extends RecyclerView.Adapter<ConditionAdapter.ViewHolder>{

    private List<Condition> conditions;

    private static final int FOOTER_VIEW = 1;


    public void setData(List<Condition> conditions) {
        this.conditions = conditions;
    }

    // Define a view holder for Footer view

    public class FooterViewHolder extends ViewHolder {
        public FooterViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Do whatever you want on clicking the item
                }
            });
        }
    }

    // Now define the viewholder for Normal list item
    public class NormalViewHolder extends ViewHolder {
        public NormalViewHolder(View itemView) {
            super(itemView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Do whatever you want on clicking the normal items
                }
            });
        }
    }

// And now in onCreateViewHolder you have to pass the correct view
// while populating the list item.

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v;

        if (viewType == FOOTER_VIEW) {
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_footer, parent, false);

            FooterViewHolder vh = new FooterViewHolder(v);

            return vh;
        }

        v = LayoutInflater.from(parent.getContext()).inflate(R.layout.reminderset_listitem, parent, false);

        NormalViewHolder vh = new NormalViewHolder(v);

        return vh;
    }

    // Now bind the viewholders in onBindViewHolder
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        try {
            if (holder instanceof NormalViewHolder) {
                NormalViewHolder vh = (NormalViewHolder) holder;
                vh.bindView(position);
            } else if (holder instanceof FooterViewHolder) {
                FooterViewHolder vh = (FooterViewHolder) holder;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

// Now the critical part. You have return the exact item count of your list
// I've only one footer. So I returned data.size() + 1
// If you've multiple headers and footers, you've to return total count
// like, headers.size() + data.size() + footers.size()

    @Override
    public int getItemCount() {
        if (conditions == null) {
            return 0;
        }

        if (conditions.size() == 0) {
            //Return 1 here to show nothing
            return 1;
        }

        // Add extra view to show the footer view
        return conditions.size() + 1;
    }

// Now define getItemViewType of your own.

    @Override
    public int getItemViewType(int position) {
        if (position == conditions.size()) {
            // This is where we'll add footer.
            return FOOTER_VIEW;
        }

        return super.getItemViewType(position);
    }


  /*  @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.reminderset_listitem, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }*/

    /*@Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.getTitleTextView().setText(conditions.get(position).getType());
    }

    @Override
    public int getItemCount() {
        if(conditions == null){
            return 0;
        }
        return conditions.size();
    }*/

    public class ViewHolder extends RecyclerView.ViewHolder {
        private View view;
        private TextView titleTextView;
        private TextView descriptionTextView;
        public ViewHolder(View v){
            super(v);
            this.view = v;
            this.titleTextView = (TextView) v.findViewById(R.id.title);
            this.descriptionTextView = (TextView) v.findViewById(R.id.description);
        }

        public void bindView(int position) {
            // bindView() method to implement actions
        }

        public TextView getTitleTextView() {
            return titleTextView;
        }

        public void setTitleTextView(TextView titleTextView) {
            this.titleTextView = titleTextView;
        }

        public TextView getDescriptionTextView() {
            return descriptionTextView;
        }

        public void setDescriptionTextView(TextView descriptionTextView) {
            this.descriptionTextView = descriptionTextView;
        }

        public View getView() {
            return view;
        }

        public void setView(View view) {
            this.view = view;
        }
    }
}
