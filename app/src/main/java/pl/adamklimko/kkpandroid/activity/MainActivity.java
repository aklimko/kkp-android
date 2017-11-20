package pl.adamklimko.kkpandroid.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.GravityCompat;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.widget.Toast;
import pl.adamklimko.kkpandroid.R;
import pl.adamklimko.kkpandroid.fragment.BoughtFragment;
import pl.adamklimko.kkpandroid.fragment.CleanedFragment;
import pl.adamklimko.kkpandroid.rest.ApiClient;
import pl.adamklimko.kkpandroid.rest.KkpService;
import pl.adamklimko.kkpandroid.rest.UserSession;
import pl.adamklimko.kkpandroid.task.UsersDataTask;
import pl.adamklimko.kkpandroid.task.UsersProfilePicturesTask;

public class MainActivity extends DrawerActivity implements FragmentCommunicator {

    private KkpService kkpService;

    private BoughtFragment boughtFragment;
    private CleanedFragment cleanedFragment;
    private Fragment currentFragment;
    private FragmentManager manager;

    public static final String USERS_PROFILE_PICTURES = "users_profile_pictures";
    private final BroadcastReceiver mUsersProfilePicturesReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            boughtFragment.redrawWholeTable();
        }
    };

    public static final String USERS_DATA = "users_data";
    private final BroadcastReceiver mUsersDataReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            boughtFragment.redrawWholeTable();
            new UsersProfilePicturesTask(getApplicationContext(), UserSession.getUsersData()).execute((Void) null);
        }
    };

    public static final String REDRAW_PICTURE = "redraw_picture";
    private final BroadcastReceiver mNewProfilePictureSaved = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            MainActivity.super.redrawProfilePicture();
            boughtFragment.redrawWholeTable();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        kkpService = ApiClient.createServiceWithAuth(KkpService.class, this);

        boughtFragment = BoughtFragment.newInstance();
        cleanedFragment = CleanedFragment.newInstance();
        manager = getSupportFragmentManager();

        registerBroadcastReceivers();

        if (savedInstanceState == null) {
            getUsersData();
            Fragment boughtFragment = this.boughtFragment;
            switchToFragment(boughtFragment);
        }
    }

    private void registerBroadcastReceivers() {
        LocalBroadcastManager.getInstance(this).registerReceiver(mUsersProfilePicturesReceiver,
                new IntentFilter(USERS_PROFILE_PICTURES));
        LocalBroadcastManager.getInstance(this).registerReceiver(mUsersDataReceiver,
                new IntentFilter(USERS_DATA));
        LocalBroadcastManager.getInstance(this).registerReceiver(mNewProfilePictureSaved,
                new IntentFilter(REDRAW_PICTURE));
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        final int id = item.getItemId();

        if (isItemChecked(id)) {
            MainActivity.super.getmDrawerLayout().closeDrawer(GravityCompat.START);
            return true;
        }

        switch (id) {
            case R.id.nav_bought:
                switchToFragment(boughtFragment);
                switchCheckedItem(id);
                break;
            case R.id.nav_cleaned:
                switchToFragment(cleanedFragment);
                switchCheckedItem(id);
                break;
            case R.id.nav_settings:
                startActivity(new Intent(getApplicationContext(), SettingsActivity.class));
                break;
            case R.id.nav_logout:
                switchToLoginActivity();
                UserSession.resetSession(getApplicationContext());
                MainActivity.super.unregisterReceivers();
                Toast.makeText(getApplicationContext(), "Successful logout", Toast.LENGTH_SHORT).show();
                break;
        }

        MainActivity.super.getmDrawerLayout().closeDrawer(GravityCompat.START);
        return true;
    }

    private boolean isItemChecked(final int id) {
        return MainActivity.super.getNavigationView().getMenu().findItem(id).isChecked();
    }

    private void switchCheckedItem(int id) {
        uncheckAllCheckedMenuItems();
        MainActivity.super.getNavigationView().getMenu().findItem(id).setChecked(true);
    }

    private void switchToLoginActivity() {
        final Intent login = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(login);
        finish();
    }

    private void uncheckAllCheckedMenuItems() {
        final Menu menu = MainActivity.super.getNavigationView().getMenu();
        for (int i = 0; i < menu.size(); i++) {
            MenuItem item = menu.getItem(i);
            if (item.hasSubMenu()) {
                SubMenu subMenu = item.getSubMenu();
                for (int j = 0; j < subMenu.size(); j++) {
                    MenuItem subMenuItem = subMenu.getItem(j);
                    if (subMenuItem.isChecked()) {
                        subMenuItem.setChecked(false);
                    }
                }
            } else if (item.isChecked()) {
                item.setChecked(false);
            }
        }
    }

    private void getUsersData() {
        final UsersDataTask usersDataTask = new UsersDataTask(getApplicationContext());
        usersDataTask.execute((Void) null);
    }

    private void switchToFragment(Fragment fragment) {
        manager.beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
    }

    @Override
    public KkpService getKkpService() {
        return kkpService;
    }

    @Override
    protected void onDestroy() {
        kkpService = null;
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mUsersProfilePicturesReceiver);
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mUsersDataReceiver);
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mNewProfilePictureSaved);
        super.onDestroy();
    }
}
