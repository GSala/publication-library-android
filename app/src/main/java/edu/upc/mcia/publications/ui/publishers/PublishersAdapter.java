package edu.upc.mcia.publications.ui.publishers;

import android.support.v7.util.SortedList;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.util.SortedListAdapterCallback;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import edu.upc.mcia.publications.R;
import edu.upc.mcia.publications.data.model.Publisher;

public class PublishersAdapter extends RecyclerView.Adapter<PublishersAdapter.ViewHolder> implements View.OnClickListener {

    private OnItemClickListener onItemClickListener;

    private SortedList<Publisher> mDataset;

    public PublishersAdapter() {
        mDataset = new SortedList<>(Publisher.class, new SortedListAdapterCallback<Publisher>(this) {
            @Override
            public int compare(Publisher o1, Publisher o2) {
                return o1.fullname().compareTo(o2.fullname());
            }

            @Override
            public boolean areContentsTheSame(Publisher oldItem, Publisher newItem) {
                return oldItem.equals(newItem);
            }

            @Override
            public boolean areItemsTheSame(Publisher item1, Publisher item2) {
                return item1.equals(item2);
            }
        });
    }

    public void setData(List<Publisher> updates) {
        mDataset.addAll(updates);
    }

    // Create new views (invoked by the layout manager)
    @Override
    public PublishersAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, final int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_publisher, parent, false);
        return new ViewHolder(v);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // Get elements from dataset
        Publisher publisher = mDataset.get(position);

        // Save position in tag and set onClickListener
        holder.root.setTag(publisher);
        holder.root.setOnClickListener(this);

        // Replace contents of the view
        holder.name.setText(publisher.fullname());
        holder.acronym.setText(publisher.acronym());
        holder.type.setText("#" + publisher.type());


    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset != null ? mDataset.size() : 0;
    }


    @Override
    public void onClick(View v) {
        if (onItemClickListener != null) {
            onItemClickListener.onItemClick(v, (Publisher) v.getTag());
        }
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(View v, Publisher author);
    }

    // Provide a reference to the views for each data item
    public static class ViewHolder extends RecyclerView.ViewHolder {

        public View root;
        public TextView name;
        public TextView acronym;
        public TextView type;

        public ViewHolder(View v) {
            super(v);
            root = v.findViewById(R.id.root);
            name = (TextView) v.findViewById(R.id.name);
            acronym = (TextView) v.findViewById(R.id.acronym);
            type = (TextView) v.findViewById(R.id.type);
        }
    }

}
