package edu.upc.mcia.publications.ui.publications;

import android.support.v7.util.SortedList;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.util.SortedListAdapterCallback;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import edu.upc.mcia.publications.R;
import edu.upc.mcia.publications.data.model.Publication;

public class PublicationsAdapter extends RecyclerView.Adapter<PublicationsAdapter.ViewHolder> implements View.OnClickListener {

    private OnItemClickListener onItemClickListener;

    private SortedList<Publication> mDataset;

    public PublicationsAdapter() {
        mDataset = new SortedList<>(Publication.class, new SortedListAdapterCallback<Publication>(this) {
            @Override
            public int compare(Publication o1, Publication o2) {
                return o2.publishDate().compareTo(o1.publishDate());
            }

            @Override
            public boolean areContentsTheSame(Publication oldItem, Publication newItem) {
                return oldItem.equals(newItem);
            }

            @Override
            public boolean areItemsTheSame(Publication item1, Publication item2) {
                return item1.equals(item2);
            }
        });
    }

    public void setData(List<Publication> pubs) {
        mDataset.addAll(pubs);
    }

    // Create new views (invoked by the layout manager)
    @Override
    public PublicationsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, final int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_publication, parent, false);
        return new ViewHolder(v);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // Get elements from dataset
        Publication pub = mDataset.get(position);

        // Save position in tag and set onClickListener
        holder.root.setTag(pub);
        holder.root.setOnClickListener(this);

        // Replace contents of the view
        // TODO - Fill with proper author data
        holder.primaryText.setText("Dummy Author");
        holder.secondaryText.setText("+3 others");
        Glide.with(holder.root.getContext())
                .load("http://registros.mcia.upc.edu/photos/default.jpg")
                .into(holder.image);

        holder.title.setText(pub.title());
        holder.subtitle.setText(pub.id()); // TODO - Fill with proper publisher data
        holder.summary.setText(pub.summary());


    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset != null ? mDataset.size() : 0;
    }


    @Override
    public void onClick(View v) {
        if (onItemClickListener != null) {
            onItemClickListener.onItemClick(v, (Publication) v.getTag());
        }
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(View v, Publication author);
    }

    // Provide a reference to the views for each data item
    public static class ViewHolder extends RecyclerView.ViewHolder {

        public View root;
        public TextView primaryText;
        public TextView secondaryText;
        public ImageView image;
        public TextView title;
        public TextView subtitle;
        public TextView summary;

        public ViewHolder(View v) {
            super(v);
            root = v.findViewById(R.id.root);
            primaryText = (TextView) v.findViewById(R.id.textPrimary);
            secondaryText = (TextView) v.findViewById(R.id.textSecondary);
            image = (ImageView) v.findViewById(R.id.image);
            title = (TextView) v.findViewById(R.id.title);
            subtitle = (TextView) v.findViewById(R.id.subtitle);
            summary = (TextView) v.findViewById(R.id.summary);
        }
    }

}