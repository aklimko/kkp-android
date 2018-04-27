package pl.adamklimko.kkpandroid.view.activity;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;

import butterknife.BindView;
import butterknife.ButterKnife;
import pl.adamklimko.kkpandroid.R;
import pl.adamklimko.kkpandroid.network.NotificationService;
import pl.adamklimko.kkpandroid.view.fragment.BaseFragment;
import pl.adamklimko.kkpandroid.view.fragment.HistoryFragment;
import pl.adamklimko.kkpandroid.view.fragment.ProductsFragment;
import pl.adamklimko.kkpandroid.view.fragment.RoomsFragment;
import pl.adamklimko.kkpandroid.network.UserSession;
import pl.adamklimko.kkpandroid.task.DataTask;
import pl.adamklimko.kkpandroid.task.UsersProfilePicturesTask;
import pl.adamklimko.kkpandroid.util.ToastUtil;

public class MainActivity extends DrawerActivity {

    private ProductsFragment productsFragment;
    private RoomsFragment roomsFragment;
    private HistoryFragment historyFragment;
    private BaseFragment currentFragment;
    private FragmentManager manager;

    NotificationService mNotificationService;
    Intent mServiceIntent;

    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout swipeRefreshLayout;

    private static final String CURRENT_FRAGMENT_TAG = "CURRENT_FRAGMENT";

    public static final String USERS_PROFILE_PICTURES = "users_profile_pictures";
    private final BroadcastReceiver mUsersProfilePicturesReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            currentFragment.redrawContent();
        }
    };

    public static final String USERS_DATA = "users_data";
    private final BroadcastReceiver mUsersDataReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            currentFragment.redrawContent();
            swipeRefreshLayout.setRefreshing(false);
            new UsersProfilePicturesTask(getApplicationContext(), UserSession.getUsersData()).execute();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        productsFragment = ProductsFragment.newInstance();
        roomsFragment = RoomsFragment.newInstance();
        historyFragment = HistoryFragment.newInstance();
        manager = getSupportFragmentManager();

        registerBroadcastReceivers();

        swipeRefreshLayout.setColorSchemeResources(R.color.colorAccent, R.color.red);
        swipeRefreshLayout.setRefreshing(false);
        swipeRefreshLayout.setOnRefreshListener(() -> new DataTask(getApplicationContext()).execute());

        if (savedInstanceState == null) {
            currentFragment = productsFragment;
            switchToFragment(productsFragment);
            getData();
        } else {
            currentFragment = (BaseFragment) manager.findFragmentByTag(CURRENT_FRAGMENT_TAG);
        }

        mNotificationService = new NotificationService(getApplicationContext());
        mServiceIntent = new Intent(getApplicationContext(), mNotificationService.getClass());
        if (!isMyServiceRunning(mNotificationService.getClass())) {
            startService(mServiceIntent);
        }
    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                Log.i ("isMyServiceRunning?", true+"");
                return true;
            }
        }
        Log.i ("isMyServiceRunning?", false+"");
        return false;
    }

    private void showNotification(String message) {

        NotificationManager notif = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notify = new Notification.Builder
                (getApplicationContext()).setContentTitle("siema").setContentText("witaj").
                setContentTitle("lol").setSmallIcon(R.mipmap.ic_launcher).build();

        notify.flags = Notification.FLAG_AUTO_CANCEL;
        notif.notify(0, notify);

    }


    private void registerBroadcastReceivers() {
        LocalBroadcastManager.getInstance(this).registerReceiver(mUsersProfilePicturesReceiver, new IntentFilter(USERS_PROFILE_PICTURES));
        LocalBroadcastManager.getInstance(this).registerReceiver(mUsersDataReceiver, new IntentFilter(USERS_DATA));
    }

    private void unregisterBroadcastReceivers() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mUsersProfilePicturesReceiver);
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mUsersDataReceiver);
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
                currentFragment = productsFragment;
                switchToFragment(productsFragment);
                switchCheckedItem(id);
                break;
            case R.id.nav_cleaned:
                currentFragment = roomsFragment;
                switchToFragment(roomsFragment);
                switchCheckedItem(id);
                break;
            case R.id.nav_history:
                currentFragment = historyFragment;
                switchToFragment(historyFragment);
                switchCheckedItem(id);
                break;
            case R.id.nav_settings:
                startActivity(new Intent(getApplicationContext(), SettingsActivity.class));
                break;
            case R.id.nav_logout:
                switchToLoginActivity();
                UserSession.resetSession(getApplicationContext());
                MainActivity.super.unregisterReceivers();
                ToastUtil.showToastShort("Successful logout", getApplicationContext());
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

    private void getData() {
        new DataTask(getApplicationContext()).execute();
    }

    private void switchToFragment(Fragment fragment) {
        manager.beginTransaction()
                .replace(R.id.fragment_container, fragment, CURRENT_FRAGMENT_TAG)
                .commit();
    }

    @Override
    protected void onDestroy() {
        unregisterBroadcastReceivers();
        stopService(mServiceIntent);
        super.onDestroy();
    }
}
