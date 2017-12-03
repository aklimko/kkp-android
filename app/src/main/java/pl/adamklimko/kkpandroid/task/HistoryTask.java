package pl.adamklimko.kkpandroid.task;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.content.LocalBroadcastManager;
import pl.adamklimko.kkpandroid.activity.MainActivity;
import pl.adamklimko.kkpandroid.model.History;
import pl.adamklimko.kkpandroid.rest.ApiClient;
import pl.adamklimko.kkpandroid.rest.KkpService;
import pl.adamklimko.kkpandroid.rest.UserSession;
import retrofit2.Call;
import retrofit2.Response;

import java.io.IOException;
import java.util.List;

public class HistoryTask extends AsyncTask<Void, Void, List<History>> {
    private final Context mContext;

    public HistoryTask(Context context) {
        mContext = context;
    }

    @Override
    protected List<History> doInBackground(Void... voids) {
        final KkpService usersDataService = ApiClient.createServiceWithAuth(KkpService.class, mContext);
        final Call<List<History>> profileCall = usersDataService.getHistory();
        final Response<List<History>> response;
        try {
            response = profileCall.execute();
        } catch (IOException e) {
            return null;
        }
        return response.body();
    }

    @Override
    protected void onPostExecute(List<History> userData) {
        if (userData == null) {
            return;
        }
        setHistoryDataInPreferences(userData);
        informToUpdateUsersDataView();
        super.onPostExecute(userData);
    }

    private void setHistoryDataInPreferences(List<History> userData) {
        UserSession.setHistoryData(userData);
    }

    private void informToUpdateUsersDataView() {
        final Intent intent = new Intent(MainActivity.USERS_DATA);
        LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);
    }
}
