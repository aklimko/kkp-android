package pl.adamklimko.kkpandroid.view.fragment;

import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import pl.adamklimko.kkpandroid.R;
import pl.adamklimko.kkpandroid.model.History;
import pl.adamklimko.kkpandroid.rest.UserSession;

import java.util.List;

public class HistoryRecyclerViewAdapter extends RecyclerView.Adapter<HistoryRecyclerViewAdapter.ViewHolder> {

    private List<History> mValues;

    public HistoryRecyclerViewAdapter(List<History> items) {
        mValues = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_history, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        mValues = UserSession.getHistoryData();
        holder.mItem = mValues.get(position);
        holder.mIdView.setText(mValues.get(position).getTimeToString());
        holder.mIdView.setGravity(Gravity.CENTER);
        holder.mContentView.setText(mValues.get(position).toString());
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        final View mView;
        final TextView mIdView;
        final TextView mContentView;
        History mItem;

        ViewHolder(View view) {
            super(view);
            mView = view;
            mIdView = view.findViewById(R.id.id);
            mContentView = view.findViewById(R.id.content);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}
