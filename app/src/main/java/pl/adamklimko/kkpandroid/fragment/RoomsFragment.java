package pl.adamklimko.kkpandroid.fragment;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import pl.adamklimko.kkpandroid.R;
import pl.adamklimko.kkpandroid.dialog.UpdateDialog;
import pl.adamklimko.kkpandroid.model.*;
import pl.adamklimko.kkpandroid.model.types.ActionType;
import pl.adamklimko.kkpandroid.model.types.ThingType;
import pl.adamklimko.kkpandroid.rest.UserSession;
import pl.adamklimko.kkpandroid.util.DynamicSizeUtil;
import pl.adamklimko.kkpandroid.util.ProfilePictureUtil;

import java.util.List;
import java.util.Set;

public class RoomsFragment extends BaseFragment {

    private Context mContext;

    private List<UserData> usersData;
    private TableLayout cleanedRoomsTable;
    private TableRow[] rows;

    private FloatingActionMenu fam;
    private FloatingActionButton fabAddCleaned;
    private FloatingActionButton fabAddDirty;

    public RoomsFragment() {}

    public static RoomsFragment newInstance() {
        return new RoomsFragment();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_rooms, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        fabAddCleaned = view.findViewById(R.id.fab_add_cleaned);
        fabAddDirty = view.findViewById(R.id.fab_add_dirty);
        fam = view.findViewById(R.id.fab_menu_rooms);
        fam.bringToFront();

        fabAddCleaned.setOnClickListener(onButtonClick());
        fabAddDirty.setOnClickListener(onButtonClick());

        drawWholeTable();
    }

    @Override
    public void redrawContent() {
        if (cleanedRoomsTable != null) {
            cleanedRoomsTable.removeAllViews();
        }
        drawWholeTable();
    }

    private View.OnClickListener onButtonClick() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (view == fabAddCleaned) {
                    new UpdateDialog(ThingType.ROOMS, ActionType.DONE, mContext).show();
                } else if (view == fabAddDirty) {
                    new UpdateDialog(ThingType.ROOMS, ActionType.TO_BE_DONE, mContext).show();
                }
                fam.close(true);
            }
        };
    }

    private void drawWholeTable() {
        if (getView() == null) {
            return;
        }
        drawTable();
        usersData = UserSession.getUsersData();
        if (usersData != null) {
            drawUsersProfiles();
            drawRoomsData();
            drawTotalData();
        }
    }

    private void drawTable() {
        cleanedRoomsTable = getView().findViewById(R.id.table_cleaned);
        cleanedRoomsTable.setStretchAllColumns(true);

        rows = new TableRow[Products.getNumberOfProducts() + 2];
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
        cleanedRoomsTable.addView(rows[0]);
    }

    private void drawRoomsData() {
        String[] roomsNames = getResources().getStringArray(R.array.rooms_names);
        Rooms dirtyRooms = UserSession.getDirtyRooms();
        for (int i = 1; i <= roomsNames.length; i++) {
            rows[i] = new TableRow(mContext);
            final TableRow row = rows[i];
            row.setGravity(Gravity.CENTER);
            final TextView room = new TextView(mContext);
            if (dirtyRooms != null && dirtyRooms.getFieldValue(i - 1) > 0) {
                room.setTextColor(Color.RED);
            }
            room.setHeight(DynamicSizeUtil.getPixelsFromDp(getContext(), 40));
            room.setGravity(Gravity.CENTER);
            room.setText(roomsNames[i - 1]);
            row.addView(room, new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1.5f));

            for (UserData userData : usersData) {
                TextView value = new TextView(mContext);
                value.setText(Integer.toString(userData.getCleanedRooms().getFieldValue(i - 1)));
                value.setGravity(Gravity.CENTER);
                row.addView(value, new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT));
            }

            cleanedRoomsTable.addView(row);
        }
    }

    private void drawTotalData() {
        final TableRow totalRow = rows[rows.length - 1];
        totalRow.setGravity(Gravity.CENTER);
        TextView total = new TextView(mContext);
        total.setText(R.string.total);
        total.setHeight(DynamicSizeUtil.getPixelsFromDp(getContext(), 40));
        total.setGravity(Gravity.CENTER);
        total.setTypeface(total.getTypeface(), Typeface.BOLD);
        totalRow.addView(total, new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1.5f));

        for (UserData userData : usersData) {
            TextView value = new TextView(mContext);
            value.setText(Integer.toString(userData.getCleanedRooms().getSumValues()));
            value.setGravity(Gravity.CENTER);
            value.setTypeface(value.getTypeface(), Typeface.BOLD);
            totalRow.addView(value, new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT));
        }
        cleanedRoomsTable.addView(totalRow);
    }

    @Override
    public void onDestroy() {
        mContext = null;
        super.onDestroy();
    }
}
