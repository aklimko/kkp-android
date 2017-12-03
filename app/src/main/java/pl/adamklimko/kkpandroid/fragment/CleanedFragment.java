package pl.adamklimko.kkpandroid.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import pl.adamklimko.kkpandroid.R;

public class CleanedFragment extends BaseFragment {


    public CleanedFragment() {
        // Required empty public constructor
    }

    public static CleanedFragment newInstance() {
        return new CleanedFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_cleaned, container, false);
    }

    @Override
    public void redrawContent() {

    }

    @Override
    public void hideRefreshing() {

    }
}
