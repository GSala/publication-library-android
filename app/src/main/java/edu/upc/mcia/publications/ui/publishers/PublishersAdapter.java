package edu.upc.mcia.publications.ui.publishers;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.upc.mcia.publications.R;
import edu.upc.mcia.publications.data.model.Publisher;
import rx.Observable;
import rx.observables.GroupedObservable;
import timber.log.Timber;

public class PublishersAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener {

    private static final int TYPE_PUBLISHER = 1;
    private static final int TYPE_HEADER = 2;

    private OnItemClickListener onItemClickListener;

    private List<Publisher> mDataset;
    private Map<Integer, String> mHeaders;

    public PublishersAdapter() {
        mDataset = new ArrayList<>();
        mHeaders = new HashMap<>();
    }

    public void setData(List<Publisher> updates) {
        mDataset.clear();
        mHeaders.clear();

        insertOrderedDataset(updates);
        extractHeaders(mDataset);
        notifyDataSetChanged();
    }

    private void insertOrderedDataset(List<Publisher> updates) {
        Observable.from(updates)
                .toSortedList((p1, p2) -> p1.getType().compareTo(p2.getType()), updates.size())
                .subscribe(list -> mDataset.addAll(list));
    }

    private void extractHeaders(List<Publisher> publishers) {

        Observable<GroupedObservable<String, Publisher>> groups = Observable.from(publishers)
                .groupBy(p -> p.getType());

        Observable<String> headers = groups
                .map(go -> go.getKey().toString());

        Observable<Integer> positions = groups
                .concatMap(g -> g.count())
                .scan(0, (pos, groupSize) -> pos + groupSize + 1);

        Observable.zip(headers, positions, (h, p) -> mHeaders.put(p, h))
                .subscribe();
    }

    // Create new views (invoked by the layout manager)
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, final int viewType) {
        switch (viewType) {
            case TYPE_PUBLISHER:
                View v = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.row_publisher, parent, false);
                return new PublisherViewHolder(v);
            case TYPE_HEADER:
                View v2 = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.row_header, parent, false);
                return new HeaderViewHolder(v2);
        }
        return null;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (holder.getItemViewType()) {
            case TYPE_PUBLISHER:
                bindPublisherViewHolder((PublisherViewHolder) holder, position);
                break;
            case TYPE_HEADER:
                bindHeaderViewHolder((HeaderViewHolder) holder, position);
                break;
        }
    }

    private void bindPublisherViewHolder(PublisherViewHolder holder, int adapterPosition) {
        int position = getDatasetPosition(adapterPosition);

        // Get elements from dataset
        Publisher publisher = mDataset.get(position);

        // Save position in tag and set onClickListener
        holder.root.setTag(publisher);
        holder.root.setOnClickListener(this);

        // Replace contents of the view
        holder.name.setText(publisher.getFullname());
        holder.acronym.setText(publisher.getAcronym());
        holder.type.setText("#" + publisher.getType());
    }

    private void bindHeaderViewHolder(HeaderViewHolder holder, int position) {
        String type = mHeaders.get(position);
        String header = String.format("%s%s%s",
                type.substring(0, 1).toUpperCase(),
                type.substring(1, type.length()),
                "s"
        );
        holder.headerView.setText(header);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return getDataItemCount() + getHeaderCount();
    }

    private int getDataItemCount() {
        return mDataset != null ? mDataset.size() : 0;
    }

    private int getHeaderCount() {
        return mHeaders != null ? mHeaders.size() : 0;
    }

    private int getDatasetPosition(int adapterPosition) {
        int offset = 0;
        for (Integer i : mHeaders.keySet()) {
            if (adapterPosition > i) {
                offset++;
            }
        }
        return adapterPosition - offset;
    }

    @Override
    public int getItemViewType(int position) {
        if (mHeaders.keySet().contains(position)) {
            return TYPE_HEADER;
        } else {
            return TYPE_PUBLISHER;
        }
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
    static class PublisherViewHolder extends RecyclerView.ViewHolder {

        public View root;
        public TextView name;
        public TextView acronym;
        public TextView type;

        public PublisherViewHolder(View v) {
            super(v);
            root = v.findViewById(R.id.root);
            name = (TextView) v.findViewById(R.id.name);
            acronym = (TextView) v.findViewById(R.id.acronym);
            type = (TextView) v.findViewById(R.id.type);
        }
    }

    static class HeaderViewHolder extends RecyclerView.ViewHolder {

        TextView headerView;

        HeaderViewHolder(View itemView) {
            super(itemView);
            headerView = (TextView) itemView;
        }

    }

}
