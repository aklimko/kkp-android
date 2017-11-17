package pl.adamklimko.kkpandroid.task;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.content.LocalBroadcastManager;
import pl.adamklimko.kkpandroid.activity.MainActivity;
import pl.adamklimko.kkpandroid.model.UserData;
import pl.adamklimko.kkpandroid.rest.ApiClient;
import pl.adamklimko.kkpandroid.rest.KkpService;
import pl.adamklimko.kkpandroid.rest.UserSession;
import retrofit2.Call;
import retrofit2.Response;

import java.io.IOException;
import java.util.List;

public class UsersDataTask extends AsyncTask<Void, Void, List<UserData>> {
    private final Context mContext;

    public UsersDataTask(Context context) {
        mContext = context;
    }

    @Override
    protected List<UserData> doInBackground(Void... voids) {
        final KkpService usersDataService = ApiClient.createServiceWithAuth(KkpService.class, mContext);
        final Call<List<UserData>> profileCall = usersDataService.getUsersData();
        final Response<List<UserData>> response;
        try {
            response = profileCall.execute();
        } catch (IOException e) {
            return null;
        }
        return response.body();
    }

    @Override
    protected void onPostExecute(List<UserData> userData) {
        if (userData == null) {
            return;
        }
        setUsersDataInPreferences(userData);
        informToUpdateUsersDataView();
        super.onPostExecute(userData);
    }

    private void setUsersDataInPreferences(List<UserData> userData) {
        UserSession.setUsersData(userData);
    }

    private void informToUpdateUsersDataView() {
        final Intent intent = new Intent(MainActivity.USERS_DATA);
        LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);
    }
}
