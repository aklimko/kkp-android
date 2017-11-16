package pl.adamklimko.kkpandroid.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import pl.adamklimko.kkpandroid.R;
import pl.adamklimko.kkpandroid.fragment.BoughtFragment;
import pl.adamklimko.kkpandroid.model.BoughtProducts;
import pl.adamklimko.kkpandroid.model.CleanedRooms;
import pl.adamklimko.kkpandroid.rest.ApiClient;
import pl.adamklimko.kkpandroid.rest.KkpService;

import java.util.Map;

public class MainActivity extends DrawerActivity implements FragmentCommunicator {

    private KkpService kkpService;
    private Map<String, BoughtProducts> usersBoughtProducts;
    private Map<String, CleanedRooms> usersCleanedRooms;
    public static final String USERS_PROFILE_PICTURES = "users_profile_pictures";
    private BroadcastReceiver mUsersProfilePicturesReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        kkpService = ApiClient.createServiceWithAuth(KkpService.class, this);

        mUsersProfilePicturesReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

            }
        };

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
        super.onDestroy();
    }
}
