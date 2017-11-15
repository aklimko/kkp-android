package pl.adamklimko.kkpandroid.activity;

import android.os.Bundle;
import pl.adamklimko.kkpandroid.R;
import pl.adamklimko.kkpandroid.fragment.MessageFragment;
import pl.adamklimko.kkpandroid.rest.ApiClient;
import pl.adamklimko.kkpandroid.rest.KkpService;

public class MainActivity extends DrawerActivity implements FragmentCommunicator {

    private KkpService kkpService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        kkpService = ApiClient.createServiceWithAuth(KkpService.class, this);

        if (savedInstanceState == null) {
            final MessageFragment messageFragment = MessageFragment.newInstance();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, messageFragment)
                    .commit();
        }
    }

    @Override
    public KkpService getKkpService() {
        return kkpService;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        kkpService = null;
    }
}
