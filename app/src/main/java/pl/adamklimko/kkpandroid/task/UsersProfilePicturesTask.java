package pl.adamklimko.kkpandroid.task;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.v4.content.LocalBroadcastManager;
import pl.adamklimko.kkpandroid.activity.MainActivity;
import pl.adamklimko.kkpandroid.util.ProfilePictureUtil;

import java.util.Map;
import java.util.TreeMap;

public class UsersProfilePicturesTask extends AsyncTask<Void, Void, Map<String, Bitmap>> {
    private final Context mContext;
    private final Map<String, String> usersProfiles;

    public UsersProfilePicturesTask(Context mContext, Map<String, String> usersProfiles) {
        this.mContext = mContext;
        this.usersProfiles = usersProfiles;
    }

    @Override
    protected Map<String, Bitmap> doInBackground(Void... params) {
        final Map<String, Bitmap> usersProfilePictures = new TreeMap<>();
        for (Map.Entry<String, String> entry : usersProfiles.entrySet()) {
            usersProfilePictures.put(entry.getKey(), ProfilePictureUtil.getProfilePicture(entry.getValue()));
        }
        return usersProfilePictures;
    }

    @Override
    protected void onPostExecute(Map<String, Bitmap> usersProfilePictures) {

        updateProfilePictures();
        super.onPostExecute(usersProfilePictures);
    }

    private void updateProfilePictures() {
        final Intent intent = new Intent(MainActivity.USERS_PROFILE_PICTURES);
        LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);
    }
}
