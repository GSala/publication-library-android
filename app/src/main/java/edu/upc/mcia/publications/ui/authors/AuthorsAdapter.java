package edu.upc.mcia.publications.ui.authors;

import android.content.pm.PackageInfo;
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
import edu.upc.mcia.publications.data.model.Author;

public class AuthorsAdapter extends RecyclerView.Adapter<AuthorsAdapter.ViewHolder> implements View.OnClickListener {

    public static final String TAG = ".RecentsAdapter";
    private static final String BASE_IMAGE_URL = "http://registros.mcia.upc.edu%s";

    private OnItemClickListener onItemClickListener;

    private SortedList<Author> mDataset;

    public AuthorsAdapter() {
        mDataset = new SortedList<>(Author.class, new SortedListAdapterCallback<Author>(this) {
            @Override
            public int compare(Author o1, Author o2) {
                return o1.fullname().compareTo(o2.fullname());
            }

            @Override
            public boolean areContentsTheSame(Author oldItem, Author newItem) {
                return oldItem.equals(newItem);
            }

            @Override
            public boolean areItemsTheSame(Author item1, Author item2) {
                return item1.equals(item2);
            }
        });
    }

    public void setData(List<Author> updates) {
        mDataset.addAll(updates);
    }

    // Create new views (invoked by the layout manager)
    @Override
    public AuthorsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, final int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_author, parent, false);
        return new ViewHolder(v);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // Get elements from dataset
        Author author = mDataset.get(position);

        // Save position in tag and set onClickListener
        holder.root.setTag(author);
        holder.root.setOnClickListener(this);

        // Replace contents of the view
        holder.primaryText.setText(author.fullname());
        holder.secondaryText.setText(author.email());

        Glide.with(holder.root.getContext())
                .load(String.format(BASE_IMAGE_URL, author.photo()))
                .into(holder.image);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset != null ? mDataset.size() : 0;
    }


    @Override
    public void onClick(View v) {
        if (onItemClickListener != null) {
            onItemClickListener.onItemClick(v, (Author) v.getTag());
        }
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(View v, Author author);
    }

    // Provide a reference to the views for each data item
    public static class ViewHolder extends RecyclerView.ViewHolder {

        public View root;
        public TextView primaryText;
        public TextView secondaryText;
        public ImageView image;

        public ViewHolder(View v) {
            super(v);
            root = v.findViewById(R.id.root);
            primaryText = (TextView) v.findViewById(R.id.textPrimary);
            secondaryText = (TextView) v.findViewById(R.id.textSecondary);
            image = (ImageView) v.findViewById(R.id.image);
        }
    }

}