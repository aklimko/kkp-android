package pl.adamklimko.kkpandroid.task;

import android.content.Context;
import android.os.AsyncTask;
import pl.adamklimko.kkpandroid.exception.NoNetworkConnectedException;
import pl.adamklimko.kkpandroid.model.types.ActionType;
import pl.adamklimko.kkpandroid.model.Rooms;
import pl.adamklimko.kkpandroid.rest.ApiClient;
import pl.adamklimko.kkpandroid.rest.KkpService;
import pl.adamklimko.kkpandroid.util.ToastUtil;
import retrofit2.Call;
import retrofit2.Response;

import java.io.IOException;
import java.util.List;

public class RoomsTask extends AsyncTask<Void, Void, Boolean> {
    private final List<Integer> rooms;
    private final ActionType actionType;
    private final Context mContext;

    private boolean noNetworkConnection = false;
    private boolean noInternetConnection = false;

    public RoomsTask(List<Integer> products, ActionType actionType, Context context) {
        this.rooms = products;
        this.actionType = actionType;
        this.mContext = context;
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        final Rooms rooms = getRoomsObjectFromIntegers();
        final KkpService kkpService = ApiClient.createServiceWithAuth(KkpService.class);

        final Call<Rooms> roomsCall;
        switch (actionType) {
            case DONE:
                roomsCall = kkpService.addCleanedRooms(rooms);
                break;
            case TO_BE_DONE:
                roomsCall = kkpService.selectRoomsAsDirty(rooms);
                break;
            default:
                roomsCall = kkpService.addCleanedRooms(rooms);
                break;
        }
        final Response<Rooms> response;
        try {
            response = roomsCall.execute();
        } catch (NoNetworkConnectedException e) {
            noNetworkConnection = true;
            return false;
        } catch (IOException e) {
            noInternetConnection = true;
            return false;
        }
        if (!response.isSuccessful()) {
            return false;
        }
        return response.code() == 200;
    }

    private Rooms getRoomsObjectFromIntegers() {
        final Rooms rooms = new Rooms();
        for (Integer roomId : this.rooms) {
            rooms.setFieldValueToOne(roomId);
        }
        return rooms;
    }

    @Override
    protected void onPostExecute(Boolean success) {
        if (noNetworkConnection) {
            ToastUtil.showToastShort("No network connection", mContext);
            return;
        }
        if (noInternetConnection) {
            ToastUtil.showToastShort("Cannot connect to a server", mContext);
            return;
        }
        if (success) {
            ToastUtil.showToastShort("Successfully updated", mContext);
            updateDataInTable();
        } else {
            ToastUtil.showToastShort("Failed to update", mContext);
        }
    }

    private void updateDataInTable() {
        new DataTask(mContext).execute();
    }
}
