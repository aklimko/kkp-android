package pl.adamklimko.kkpandroid.task;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.content.LocalBroadcastManager;
import pl.adamklimko.kkpandroid.activity.MainActivity;
import pl.adamklimko.kkpandroid.model.Data;
import pl.adamklimko.kkpandroid.rest.ApiClient;
import pl.adamklimko.kkpandroid.rest.KkpService;
import pl.adamklimko.kkpandroid.rest.UserSession;
import retrofit2.Call;
import retrofit2.Response;

import java.io.IOException;

public class DataTask extends AsyncTask<Void, Void, Data> {
    private final Context mContext;

    public DataTask(Context context) {
        mContext = context;
    }

    @Override
    protected Data doInBackground(Void... voids) {
        final KkpService dataService = ApiClient.createServiceWithAuth(KkpService.class);
        final Call<Data> profileCall = dataService.getData();
        final Response<Data> response;
        try {
            response = profileCall.execute();
        } catch (IOException e) {
            return null;
        }
        return response.body();
    }

    @Override
    protected void onPostExecute(Data data) {
        if (data == null) {
            informAboutNoInternetConnection();
            return;
        }
        setDataInPreferences(data);
        informToUpdateUsersDataView();
        super.onPostExecute(data);
    }

    private void informAboutNoInternetConnection() {
        final Intent intent = new Intent(MainActivity.NETWORK_FAIL);
        LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);
    }

    private void setDataInPreferences(Data data) {
        UserSession.setData(data);
    }

    private void informToUpdateUsersDataView() {
        final Intent intent = new Intent(MainActivity.USERS_DATA);
        LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);
    }
}
