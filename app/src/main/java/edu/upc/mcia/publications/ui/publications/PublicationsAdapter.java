package edu.upc.mcia.publications.ui.publications;

import android.support.v7.util.SortedList;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.util.SortedListAdapterCallback;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import edu.upc.mcia.publications.App;
import edu.upc.mcia.publications.R;
import edu.upc.mcia.publications.data.model.Author;
import edu.upc.mcia.publications.data.model.Publication;
import edu.upc.mcia.publications.data.remote.ApiManager;

public class PublicationsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener {

    private static final int TYPE_PUBLICATION = 1;
    private static final int TYPE_LOADING = 2;

    private OnPublicationClickListener onPublicationClickListener;
    private OnAuthorClickListener onAuthorClickListener;

    private boolean showLoadingMore = false;

    private SortedList<Publication> mDataset;

    public PublicationsAdapter() {
        mDataset = new SortedList<>(Publication.class, new SortedListAdapterCallback<Publication>(this) {
            @Override
            public int compare(Publication o1, Publication o2) {
                return o2.getPublishDate().compareTo(o1.getPublishDate());
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

    public void addData(List<Publication> pubs) {
        mDataset.addAll(pubs);
    }

    public void clearData() {
        mDataset.clear();
    }

    public void showLoadingMore(boolean loading) {
        if (showLoadingMore == loading) return;
        showLoadingMore = loading;
        if (loading) {
            notifyItemInserted(getLoadingMoreItemPosition());
        } else {
            notifyItemRemoved(getLoadingMoreItemPosition());
        }
    }

    private int getLoadingMoreItemPosition() {
        return showLoadingMore ? getItemCount() - 1 : RecyclerView.NO_POSITION;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case TYPE_PUBLICATION:
                View v = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.row_publication, parent, false);
                return new PublicationViewHolder(v);
            case TYPE_LOADING:
                View v2 = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.row_loading, parent, false);
                return new LoadingViewHolder(v2);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case TYPE_PUBLICATION:
                bindPublicationViewHolder((PublicationViewHolder) holder, position);
                break;
            case TYPE_LOADING:
                bindLoadingViewHolder((LoadingViewHolder) holder);
                break;
        }
    }


    // Replace the contents of a view (invoked by the layout manager)
    private void bindPublicationViewHolder(PublicationViewHolder holder, int position) {
        // Get elements from dataset
        Publication pub = mDataset.get(position);
        Author author = pub.getAuthors().get(0);

        // Save position in tag and set onClickListener
        holder.publication.setTag(pub);
        holder.publication.setOnClickListener(this);
        holder.author.setTag(author);
        holder.author.setOnClickListener(this);

        // Replace contents of the view
        holder.primaryText.setText(author.getFullname());
        holder.secondaryText.setText(author.getEmail());
        Glide.with(holder.root.getContext())
                .load(String.format(ApiManager.IMAGE_BASE_URL, author.getPhoto()))
                .dontAnimate()
                .into(holder.image);

        holder.title.setText(pub.getTitle());

        String dateString = DateUtils.formatDateTime(App.getContext(),
                pub.getPublishDate().getTime(),
                DateUtils.FORMAT_NUMERIC_DATE | DateUtils.FORMAT_SHOW_YEAR);
        holder.subtitle.setText(pub.getPublisher().getAcronym() + "  -  " + dateString);
        holder.summary.setText(pub.getSummary());

    }

    private void bindLoadingViewHolder(LoadingViewHolder holder) {
        holder.progress.setVisibility(showLoadingMore ? View.VISIBLE : View.INVISIBLE);
    }

    @Override
    public int getItemCount() {
        return getDataItemCount() + (showLoadingMore ? 1 : 0);
    }

    private int getDataItemCount() {
        return mDataset != null ? mDataset.size() : 0;
    }

    @Override
    public int getItemViewType(int position) {
        if (position < getDataItemCount()) {
            return TYPE_PUBLICATION;
        } else {
            return TYPE_LOADING;
        }
    }

    @Override
    public void onClick(View v) {
        if (onPublicationClickListener != null) {
            switch (v.getId()) {
                case R.id.publication:
                    if (onPublicationClickListener != null) {
                        onPublicationClickListener.onItemClick(v, (Publication) v.getTag());
                    }
                    break;
                case R.id.author:
                    if (onAuthorClickListener != null) {
                        onAuthorClickListener.onAuthorClick(v, (Author) v.getTag());
                    }
                    break;
            }
        }
    }

    public void setOnPublicationClickListener(OnPublicationClickListener
                                                      onPublicationClickListener) {
        this.onPublicationClickListener = onPublicationClickListener;
    }

    public void setOnAuthorClickListener(OnAuthorClickListener onAuthorClickListener) {
        this.onAuthorClickListener = onAuthorClickListener;
    }

    public interface OnPublicationClickListener {
        void onItemClick(View v, Publication author);
    }

    public interface OnAuthorClickListener {
        void onAuthorClick(View v, Author author);
    }

    static class PublicationViewHolder extends RecyclerView.ViewHolder {

        public View root;
        public View publication;
        public View author;
        public TextView primaryText;
        public TextView secondaryText;
        public ImageView image;
        public TextView title;
        public TextView subtitle;
        public TextView summary;

        public PublicationViewHolder(View v) {
            super(v);
            root = v.findViewById(R.id.root);
            publication = v.findViewById(R.id.publication);
            author = v.findViewById(R.id.author);
            primaryText = (TextView) v.findViewById(R.id.textPrimary);
            secondaryText = (TextView) v.findViewById(R.id.textSecondary);
            image = (ImageView) v.findViewById(R.id.image);
            title = (TextView) v.findViewById(R.id.title);
            subtitle = (TextView) v.findViewById(R.id.subtitle);
            summary = (TextView) v.findViewById(R.id.summary);
        }
    }

    static class LoadingViewHolder extends RecyclerView.ViewHolder {

        ProgressBar progress;

        LoadingViewHolder(View itemView) {
            super(itemView);
            progress = (ProgressBar) itemView;
        }

    }

}