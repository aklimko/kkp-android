package pl.adamklimko.kkpandroid.fragment;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import pl.adamklimko.kkpandroid.R;
import pl.adamklimko.kkpandroid.activity.FragmentCommunicator;
import pl.adamklimko.kkpandroid.activity.MainActivity;
import pl.adamklimko.kkpandroid.model.BoughtProducts;
import pl.adamklimko.kkpandroid.model.UserData;
import pl.adamklimko.kkpandroid.rest.KkpService;
import pl.adamklimko.kkpandroid.rest.UserSession;
import pl.adamklimko.kkpandroid.util.DynamicSizeUtil;

import java.util.List;

public class BoughtFragment extends Fragment {

    private Context mContext;

    private KkpService kkpService;
    private FragmentCommunicator fragmentCommunicator;

    public BoughtFragment() {
        // Required empty public constructor
    }

    public static BoughtFragment newInstance() {
        return new BoughtFragment();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
        fragmentCommunicator = (MainActivity) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_bought, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        kkpService = fragmentCommunicator.getKkpService();

        if (getView() == null) {
            return;
        }
        drawTable();
    }

    public void drawTable() {
        final TableLayout boughtProducts = getView().findViewById(R.id.table_bought);
        boughtProducts.setStretchAllColumns(true);

        GradientDrawable gd = new GradientDrawable(
                GradientDrawable.Orientation.TOP_BOTTOM,
                new int[] {Color.parseColor("#C0C0C0"), Color.parseColor("#505050")});
        gd.setGradientCenter(0.f, 1.f);
        gd.setLevel(2);
        //boughtProducts.setBackgroundDrawable(gd);

        final TableRow[] rows = new TableRow[BoughtProducts.getNumberOfProducts() + 1];
        rows[0] = new TableRow(mContext);
        rows[0].addView(new TextView(mContext), new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1.5f));

        final List<UserData> usersData = UserSession.getUsersData();
        if (usersData == null) {
            return;
        }
        for (UserData userData : usersData) {
            TextView username = new TextView(mContext);
            username.setBackgroundDrawable(gd);
            username.setText(userData.getUsername());
            username.setHeight(DynamicSizeUtil.getPixelsFromDp(getContext(), 40));
            username.setGravity(Gravity.CENTER);
            username.setTextSize(11);
            rows[0].addView(username, new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f));
        }
        boughtProducts.addView(rows[0]);

        String[] productsNames = BoughtProducts.getProductsNames();
        for (int i = 1; i <= productsNames.length; i++) {
            rows[i] = new TableRow(mContext);
            final TableRow row = rows[i];
            TableLayout.LayoutParams tableRowParams =
                    new TableLayout.LayoutParams
                            (TableLayout.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.WRAP_CONTENT);

            tableRowParams.setMargins(3, 3, 2, 10);
            row.setLayoutParams(tableRowParams);
            //row.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
            final TextView product = new TextView(mContext);
            product.setBackgroundDrawable(gd);
            product.setHeight(DynamicSizeUtil.getPixelsFromDp(getContext(), 40));
            product.setGravity(Gravity.CENTER);
            product.setText(productsNames[i - 1]);
            row.addView(product, new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1.5f));

            for (UserData userData : usersData) {
                TextView value = new TextView(mContext);
                value.setText(Integer.toString(userData.getBoughtProducts().getFieldValue(i - 1)));
                value.setGravity(Gravity.CENTER);
                row.addView(value, new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f));
            }

            boughtProducts.addView(row);
        }
    }

    @Override
    public void onDestroy() {
        mContext = null;
        super.onDestroy();
    }
}
