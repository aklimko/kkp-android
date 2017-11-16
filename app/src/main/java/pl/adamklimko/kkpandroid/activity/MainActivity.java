package pl.adamklimko.kkpandroid.activity;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        kkpService = ApiClient.createServiceWithAuth(KkpService.class, this);



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
