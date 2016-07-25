package edu.upc.mcia.publications.ui.authors;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import edu.upc.mcia.publications.R;
import edu.upc.mcia.publications.data.model.Author;
import edu.upc.mcia.publications.data.remote.ApiManager;
import rx.Observable;

public class AuthorsAdapter extends RecyclerView.Adapter<AuthorsAdapter.ViewHolder> implements View.OnClickListener, Filterable {

    private OnItemClickListener onItemClickListener;

    private ArrayList<Author> mOriginalData;
    private ArrayList<Author> mFilteredData;
    private AuthorFilter mFilter = new AuthorFilter();

    public AuthorsAdapter() {
        mOriginalData = new ArrayList<>();
        mFilteredData = new ArrayList<>();
    }

    public void setData(List<Author> updates) {
        mOriginalData.clear();
        mOriginalData.addAll(updates);
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
        Author author = mFilteredData.get(position);

        // Save position in tag and set onClickListener
        holder.root.setTag(author);
        holder.root.setOnClickListener(this);

        // Replace contents of the view
        holder.primaryText.setText(author.getFullname());
        holder.secondaryText.setText(author.getEmail());

        Glide.with(holder.root.getContext())
                .load(String.format(ApiManager.IMAGE_BASE_URL, author.getPhoto()))
                .dontAnimate()
                .into(holder.image);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mFilteredData != null ? mFilteredData.size() : 0;
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

    @Override
    public Filter getFilter() {
        return mFilter;
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

    private class AuthorFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            String filterString = constraint.toString().toLowerCase();
            FilterResults results = new FilterResults();

            final ArrayList<Author> newList = new ArrayList<>();
            Observable.from(mOriginalData)
                    .filter(author -> author.getFullname().toLowerCase().contains(filterString))
                    .toSortedList()
                    .subscribe(filteredData -> newList.addAll(filteredData));

            results.values = newList;
            results.count = newList.size();
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            mFilteredData = (ArrayList<Author>) results.values;
            notifyDataSetChanged();
        }

    }

}