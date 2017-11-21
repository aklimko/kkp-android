package pl.adamklimko.kkpandroid.util;

import android.content.Context;
import android.graphics.*;
import android.text.TextUtils;
import pl.adamklimko.kkpandroid.model.Profile;
import pl.adamklimko.kkpandroid.rest.ApiClient;
import pl.adamklimko.kkpandroid.rest.UserSession;
import pl.adamklimko.kkpandroid.rest.KkpService;
import retrofit2.Call;
import retrofit2.Response;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

public class ProfilePictureUtil {

    public static Bitmap getProfilePicture(String facebookId) {
        try {
            final URL url = new URL("https://graph.facebook.com//v2.10/" + facebookId + "/picture?type=square&height=200&width=200");
            final HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            final InputStream input = connection.getInputStream();
            return BitmapFactory.decodeStream(input);
        } catch (IOException e) {
            return null;
        }
    }

    public static Bitmap getRoundedCornerBitmap(Bitmap bitmap) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);
        final float roundPx = 700;

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;
    }

    public static void saveProfilePicture(Bitmap bitmapImage, String username, Context context) {
        final File path = new File(context.getFilesDir(), getUserPictureName(username));

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(path);
            bitmapImage.compress(Bitmap.CompressFormat.JPEG, 100, fos);
        } catch (Exception e) {
            fos = null;
        } finally {
            try {
                if (fos != null) {
                    fos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void deletePictureFromStorage(String name, Context context) {
        context.deleteFile(name);
    }

    public static Bitmap getUserPictureFromStorage(String username, Context context) {
        try {
            final File profilePicture = new File(context.getFilesDir(), getUserPictureName(username));
            return BitmapFactory.decodeStream(new FileInputStream(profilePicture));
        } catch (FileNotFoundException e) {
            return null;
        }
    }

    public static void getUserProfile(Context context) {
        final KkpService authService = ApiClient.createServiceWithAuth(KkpService.class, context);
        final Call<Profile> profileCall = authService.getProfile();
        final Response<Profile> response;
        try {
            response = profileCall.execute();
        } catch (IOException e) {
            return;
        }
        final Profile profile = response.body();
        if (profile == null) {
            return;
        }
        UserSession.setProfileDataInPreferences(profile);
        final String facebookId = profile.getFacebookId();
        if (TextUtils.isEmpty(facebookId)) {
            return;
        }
        setProfilePicture(facebookId, context);
    }

    private static void setProfilePicture(String facebookId, Context context) {
        final Bitmap profilePicture = ProfilePictureUtil.getProfilePicture(facebookId);
        if (profilePicture == null) {
            return;
        }
        ProfilePictureUtil.saveProfilePicture(profilePicture, UserSession.getUsername(), context);
    }

    public static String getUserPictureName(String username) {
        return username + ".jpg";
    }
}
