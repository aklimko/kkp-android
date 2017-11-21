package pl.adamklimko.kkpandroid.fragment;

import android.content.Context;
import android.graphics.Bitmap;
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
import android.widget.*;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
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
    GradientDrawable gd;

    private FloatingActionMenu fam;
    private FloatingActionButton fabAddBought;
    private FloatingActionButton fabAddMissing;

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
        kkpService = fragmentCommunicator.getKkpService();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_bought, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getView() == null) {
            return;
        }
        fabAddBought = view.findViewById(R.id.fab_add_bought);
        fabAddMissing = view.findViewById(R.id.fab_add_to_buy);
        fam = view.findViewById(R.id.fab_menu);
        fam.bringToFront();

        //handling menu status (open or close)
        fam.setOnMenuToggleListener(new FloatingActionMenu.OnMenuToggleListener() {
            @Override
            public void onMenuToggle(boolean opened) {
                if (opened) {
//                    showToast("Menu is opened");
                } else {
//                    showToast("Menu is closed");
                }
            }
        });

        //handling each floating action button clicked
        fabAddBought.setOnClickListener(onButtonClick());
        fabAddMissing.setOnClickListener(onButtonClick());

        fam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (fam.isOpened()) {
                    fam.close(true);
                }
            }
        });

        drawWholeTable();
    }

    private View.OnClickListener onButtonClick() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (view == fabAddBought) {
                    showToast("fab bought");
                } else if (view == fabAddMissing) {
                    showToast("fab missing");
                } else {
                    showToast("Button Edit clicked");
                }
                fam.close(true);
            }
        };
    }

    private void showToast(String msg) {
        Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
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

        gd = new GradientDrawable(
                GradientDrawable.Orientation.LEFT_RIGHT,
                new int[]{Color.parseColor("#C0C0C0"), Color.parseColor("#505050")});
        gd.setGradientCenter(0.f, 1.f);
        gd.setLevel(2);

        rows = new TableRow[BoughtProducts.getNumberOfProducts() + 2];
        rows[0] = new TableRow(mContext);
        rows[0].setMinimumHeight(DynamicSizeUtil.getPixelsFromDp(mContext, 45));
        rows[rows.length - 1] = new TableRow(mContext);
        final TextView emptySpace = new TextView(mContext);
        emptySpace.setHeight(DynamicSizeUtil.getPixelsFromDp(mContext, 45));
        rows[0].addView(emptySpace, new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1.5f));
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
                final Bitmap roundedPicture = ProfilePictureUtil.getRoundedCornerBitmap(ProfilePictureUtil.getUserPictureFromStorage(username, mContext));
                profilePicture.setImageBitmap(roundedPicture);

                final TableRow.LayoutParams layoutParams = new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT);
                layoutParams.gravity = Gravity.CENTER;
                rows[0].addView(profilePicture, layoutParams);
            } else {
                TextView usernameText = new TextView(mContext);
                usernameText.setText(userData.getUsername());
                usernameText.setHeight(DynamicSizeUtil.getPixelsFromDp(getContext(), 45));
                usernameText.setGravity(Gravity.CENTER);
                usernameText.setTextSize(11);
                rows[0].addView(usernameText, new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT));
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
                row.addView(value, new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT));
            }

            boughtProducts.addView(row);
        }
    }

    private void drawTotalData() {
        final TableRow totalRow = rows[rows.length - 1];
        totalRow.setGravity(Gravity.CENTER);
        TextView total = new TextView(mContext);
        total.setText("Total");
        total.setHeight(DynamicSizeUtil.getPixelsFromDp(getContext(), 40));
        total.setGravity(Gravity.CENTER);
        total.setTypeface(total.getTypeface(), Typeface.BOLD);
        totalRow.addView(total, new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1.5f));

        for (UserData userData : usersData) {
            TextView value = new TextView(mContext);
            value.setText(Integer.toString(userData.getBoughtProducts().getSumValues()));
            value.setGravity(Gravity.CENTER);
            value.setTypeface(value.getTypeface(), Typeface.BOLD);
            totalRow.addView(value, new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT));
        }
        boughtProducts.addView(totalRow);
    }

    @Override
    public void onDestroy() {
        mContext = null;
        super.onDestroy();
    }
}
