package pl.adamklimko.kkpandroid.task;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.v4.content.LocalBroadcastManager;
import pl.adamklimko.kkpandroid.view.activity.MainActivity;
import pl.adamklimko.kkpandroid.util.ProfilePictureUtil;

public class ProfilePictureTask extends AsyncTask<String, Void, Bitmap> {
    private final Context mContext;
    private final String username;

    public ProfilePictureTask(Context mContext, String username) {
        this.mContext = mContext;
        this.username = username;
    }

    @Override
    protected Bitmap doInBackground(String... params) {
        final String facebookId = params[0];
        return ProfilePictureUtil.getProfilePicture(facebookId);
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        if (bitmap == null) {
            return;
        }
        ProfilePictureUtil.saveProfilePicture(bitmap, username, mContext);
        informToRedrawProfilePictureViewInDrawer();
        super.onPostExecute(bitmap);
    }

    private void informToRedrawProfilePictureViewInDrawer() {
        final Intent intent = new Intent(MainActivity.REDRAW_PICTURE);
        LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);
    }
}
