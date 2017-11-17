package pl.adamklimko.kkpandroid.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import pl.adamklimko.kkpandroid.R;
import pl.adamklimko.kkpandroid.fragment.BoughtFragment;
import pl.adamklimko.kkpandroid.rest.ApiClient;
import pl.adamklimko.kkpandroid.rest.KkpService;
import pl.adamklimko.kkpandroid.rest.UserSession;

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
                // TODO: Draw data on fragment
                UserSession.getUserData();
            }
        };

        mUsersProfilePicturesReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                // TODO: Update pictures icons on fragment
            }
        };

        LocalBroadcastManager.getInstance(this).registerReceiver(mUsersProfilePicturesReceiver,
                new IntentFilter(UPDATE_PROFILE));
        LocalBroadcastManager.getInstance(this).registerReceiver(mUsersDataReceiver,
                new IntentFilter(USERS_DATA));

        if (savedInstanceState == null) {
            final BoughtFragment boughtFragment = BoughtFragment.newInstance();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, boughtFragment)
                    .commit();
        }
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
