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
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import com.mikhaellopez.circularimageview.CircularImageView;
import pl.adamklimko.kkpandroid.R;
import pl.adamklimko.kkpandroid.activity.FragmentCommunicator;
import pl.adamklimko.kkpandroid.activity.MainActivity;
import pl.adamklimko.kkpandroid.model.BoughtProducts;
import pl.adamklimko.kkpandroid.model.UserData;
import pl.adamklimko.kkpandroid.rest.KkpService;
import pl.adamklimko.kkpandroid.rest.UserSession;
import pl.adamklimko.kkpandroid.util.DynamicSizeUtil;
import pl.adamklimko.kkpandroid.util.ProfilePictureUtil;

import java.util.List;
import java.util.Set;

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
        drawWholeTable();
    }

    public void redrawWholeTable() {
        boughtProducts.removeAllViews();
        drawWholeTable();
    }

    private void drawWholeTable() {
        drawTable();
        usersData = UserSession.getUsersData();
        if (usersData != null) {
            drawUsersProfiles();
            drawBoughtData();
            drawTotalData();
        }
    }

    private void drawTable() {
        boughtProducts = getView().findViewById(R.id.table_bought);
        boughtProducts.setStretchAllColumns(true);

        GradientDrawable gd = new GradientDrawable(
                GradientDrawable.Orientation.TOP_BOTTOM,
                new int[]{Color.parseColor("#C0C0C0"), Color.parseColor("#505050")});
        gd.setGradientCenter(0.f, 1.f);
        gd.setLevel(2);
        //boughtProducts.setBackgroundDrawable(gd);

        rows = new TableRow[BoughtProducts.getNumberOfProducts() + 2];
        rows[0] = new TableRow(mContext);
        rows[rows.length - 1] = new TableRow(mContext);
        rows[0].addView(new TextView(mContext), new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1.5f));
    }

    private void drawUsersProfiles() {
        if (usersData == null) {
            return;
        }
        final Set<String> usersWithValidPictures = UserSession.getUsersValidPictures();
        for (UserData userData : usersData) {
            final String username = userData.getUsername();
            if (usersWithValidPictures.contains(username)) {
                ImageView profilePicture = new ImageView(mContext);
                profilePicture.setImageBitmap(ProfilePictureUtil.getUserPictureFromStorage(mContext, username));
                int size = DynamicSizeUtil.getPixelsFromDp(mContext, 35);
                profilePicture.setMaxHeight(size);
                profilePicture.setLayoutParams(new TableRow.LayoutParams(size, size));
                rows[0].addView(profilePicture, new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f));
            } else {
                TextView usernameText = new TextView(mContext);
                usernameText.setText(userData.getUsername());
                usernameText.setHeight(DynamicSizeUtil.getPixelsFromDp(getContext(), 40));
                usernameText.setGravity(Gravity.CENTER);
                usernameText.setTextSize(11);
                rows[0].addView(usernameText, new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f));
            }
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
