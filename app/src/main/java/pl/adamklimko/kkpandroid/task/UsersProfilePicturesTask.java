package pl.adamklimko.kkpandroid.task;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import pl.adamklimko.kkpandroid.view.activity.MainActivity;
import pl.adamklimko.kkpandroid.model.UserData;
import pl.adamklimko.kkpandroid.network.UserSession;
import pl.adamklimko.kkpandroid.util.ProfilePictureUtil;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UsersProfilePicturesTask extends AsyncTask<Void, Void, Map<String, Bitmap>> {
    private final WeakReference<Context> mContext;
    private final List<UserData> usersData;

    public UsersProfilePicturesTask(Context context, List<UserData> usersData) {
        this.mContext = new WeakReference<>(context);
        this.usersData = usersData;
    }

    @Override
    protected Map<String, Bitmap> doInBackground(Void... params) {
        final Map<String, Bitmap> usersProfilePictures = new HashMap<>(usersData.size());
        for (UserData userData : usersData) {
            String facebookId = userData.getProfile().getFacebookId();
            if (!TextUtils.isEmpty(facebookId)) {
                final Bitmap profile = ProfilePictureUtil.getProfilePicture(facebookId);
                if (profile != null) {
                    usersProfilePictures.put(userData.getUsername(), profile);
                }
            }
        }
        return usersProfilePictures;
    }

    @Override
    protected void onPostExecute(Map<String, Bitmap> usersProfilePictures) {
        saveProfilePicturesInStorage(usersProfilePictures);
        saveUsersValidPicturesInPrefereces(usersProfilePictures);
        informToUpdateProfilePictures(usersProfilePictures);
        super.onPostExecute(usersProfilePictures);
    }

    private void saveProfilePicturesInStorage(Map<String, Bitmap> usersProfilePictures) {
        for (Map.Entry<String, Bitmap> entry : usersProfilePictures.entrySet()) {
            if (entry.getValue() != null) {
                ProfilePictureUtil.saveProfilePicture(entry.getValue(), entry.getKey(), mContext.get());
            }
        }
    }

    private void saveUsersValidPicturesInPrefereces(Map<String, Bitmap> usersProfilePictures) {
        UserSession.setUsersValidPictures(usersProfilePictures.keySet());
    }

    private void informToUpdateProfilePictures(Map<String, Bitmap> usersProfilePictures) {
        final Intent intent = new Intent(MainActivity.USERS_PROFILE_PICTURES);
        LocalBroadcastManager.getInstance(mContext.get()).sendBroadcast(intent);
    }
}
