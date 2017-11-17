package pl.adamklimko.kkpandroid.task;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.v4.content.LocalBroadcastManager;
import pl.adamklimko.kkpandroid.activity.DrawerActivity;
import pl.adamklimko.kkpandroid.util.ProfilePictureUtil;

public class ProfilePictureTask extends AsyncTask<String, Void, Bitmap> {
    private Context mContext;
    private String username;

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
        ProfilePictureUtil.saveProfilePicture(mContext, username, bitmap);
        informToRedrawProfilePictureViewInDrawer();
        super.onPostExecute(bitmap);
    }

    private void informToRedrawProfilePictureViewInDrawer() {
        final Intent intent = new Intent(DrawerActivity.REDRAW_PICTURE);
        LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);
    }
}
