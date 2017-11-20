package pl.adamklimko.kkpandroid.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import pl.adamklimko.kkpandroid.R;
import pl.adamklimko.kkpandroid.rest.ApiClient;
import pl.adamklimko.kkpandroid.rest.KkpService;
import pl.adamklimko.kkpandroid.rest.UserSession;
import pl.adamklimko.kkpandroid.task.UsersDataTask;
import pl.adamklimko.kkpandroid.task.UsersProfilePicturesTask;

public class MainActivity extends DrawerActivity implements FragmentCommunicator {

    private KkpService kkpService;
    public static final String USERS_PROFILE_PICTURES = "users_profile_pictures";
    private BroadcastReceiver mUsersProfilePicturesReceiver;

    public static final String USERS_DATA = "users_data";
    private BroadcastReceiver mUsersDataReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        kkpService = ApiClient.createServiceWithAuth(KkpService.class, this);

        mUsersDataReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                MainActivity.super.getBoughtFragment().redrawWholeTable();
                new UsersProfilePicturesTask(getApplicationContext(), UserSession.getUsersData()).execute((Void) null);
            }
        };

        mUsersProfilePicturesReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                //TODO: Rewrite upptask to use userdata list
                MainActivity.super.getBoughtFragment().redrawWholeTable();
            }
        };

        LocalBroadcastManager.getInstance(this).registerReceiver(mUsersProfilePicturesReceiver,
                new IntentFilter(USERS_PROFILE_PICTURES));
        LocalBroadcastManager.getInstance(this).registerReceiver(mUsersDataReceiver,
                new IntentFilter(USERS_DATA));

        if (savedInstanceState == null) {
            getUsersData();
            Fragment boughtFragment = super.getBoughtFragment();
            switchToFragment(boughtFragment);
        }
    }

    private void getUsersData() {
        final UsersDataTask usersDataTask = new UsersDataTask(getApplicationContext());
        usersDataTask.execute((Void) null);
    }

    private void switchToFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
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
        super.onDestroy();
    }
}
