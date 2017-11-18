package pl.adamklimko.kkpandroid.fragment;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
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

    private List<UserData> usersData;
    private TableLayout boughtProducts;
    private TableRow[] rows;

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
        drawUsersProfiles();
        drawBoughtData();
        drawTotalData();
    }

    public void drawTable() {
        boughtProducts = getView().findViewById(R.id.table_bought);
        boughtProducts.setStretchAllColumns(true);

        GradientDrawable gd = new GradientDrawable(
                GradientDrawable.Orientation.TOP_BOTTOM,
                new int[] {Color.parseColor("#C0C0C0"), Color.parseColor("#505050")});
        gd.setGradientCenter(0.f, 1.f);
        gd.setLevel(2);
        //boughtProducts.setBackgroundDrawable(gd);

        rows = new TableRow[BoughtProducts.getNumberOfProducts() + 2];
        rows[0] = new TableRow(mContext);
        rows[rows.length - 1] = new TableRow(mContext);
        rows[0].addView(new TextView(mContext), new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1.5f));

        usersData = UserSession.getUsersData();
    }

    private void drawUsersProfiles() {
        if (usersData == null) {
            return;
        }
        for (UserData userData : usersData) {
            TextView username = new TextView(mContext);
//            username.setBackgroundDrawable(gd);
            username.setText(userData.getUsername());
            username.setHeight(DynamicSizeUtil.getPixelsFromDp(getContext(), 40));
            username.setGravity(Gravity.CENTER);
            username.setTextSize(11);
            rows[0].addView(username, new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f));
        }
        boughtProducts.addView(rows[0]);
    }

    private void drawBoughtData() {
        String[] productsNames = BoughtProducts.getProductsNames();
        for (int i = 1; i <= productsNames.length; i++) {
            rows[i] = new TableRow(mContext);
            final TableRow row = rows[i];
            row.setGravity(Gravity.CENTER);
            final TextView product = new TextView(mContext);
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

    private void drawTotalData() {
        final TableRow totalRow = rows[rows.length - 1];
        totalRow.setGravity(Gravity.CENTER);
        TextView total = new TextView(mContext);
        total.setText("Total");
        total.setGravity(Gravity.CENTER);
        total.setTypeface(total.getTypeface(), Typeface.BOLD);
        totalRow.addView(total, new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1.5f));

        for (UserData userData : usersData) {
            TextView value = new TextView(mContext);
            value.setText(Integer.toString(userData.getBoughtProducts().getSumValues()));
            value.setGravity(Gravity.CENTER);
            value.setTypeface(value.getTypeface(), Typeface.BOLD);
            totalRow.addView(value, new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f));
        }
        boughtProducts.addView(totalRow);
    }

    @Override
    public void onDestroy() {
        mContext = null;
        super.onDestroy();
    }
}
