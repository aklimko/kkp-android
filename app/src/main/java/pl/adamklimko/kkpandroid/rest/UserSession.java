package pl.adamklimko.kkpandroid.rest;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import pl.adamklimko.kkpandroid.model.Profile;
import pl.adamklimko.kkpandroid.model.Token;
import pl.adamklimko.kkpandroid.model.UserData;

import java.lang.reflect.Type;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class UserSession {
    
    private static SharedPreferences preferences;

    private static final String USERNAME = "username";
    public static final String FULL_NAME = "full_name";
    public static final String FACEBOOK_ID = "facebook_id";
    private static final String TOKEN = "token";
    private static final String EXPIRATION_DATE = "expiration_date";
    private static final String USERS_DATA = "users_data";
    public static final String USERS_VALID_PICTURES = "users_valid_pictures";

    private static boolean firstStarted = true;

    public static void initPreferences(Context context) {
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public static void setTokenInPreferences(Token token) {
        final Editor editor = preferences.edit();
        editor.putString(TOKEN, token.getToken());
        editor.putString(EXPIRATION_DATE, token.getExpirationDate());
        editor.apply();
    }

    public static void setUsernameInPreferences(String username) {
        final Editor editor = preferences.edit();
        editor.putString(USERNAME, username);
        editor.apply();
    }

    public static String getUsername() {
        return preferences.getString(USERNAME, "");
    }

    public static String getFullName() {
        return preferences.getString(FULL_NAME, "");
    }

    public static String getFacebookId() {
        return preferences.getString(FACEBOOK_ID, "");
    }

    public static String getToken() {
        return preferences.getString(TOKEN, "");
    }

    public static boolean hasToken() {
         return !TextUtils.isEmpty(preferences.getString(TOKEN, ""));
    }

    public static void resetSession() {
        preferences.edit()
                .clear()
                .apply();
    }

    public static boolean isAppJustStarted() {
        return firstStarted;
    }

    public static void setFirstStarted(boolean firstStarted) {
        UserSession.firstStarted = firstStarted;
    }

    public static void setProfileDataInPreferences(Profile profile) {
        final Editor editor = preferences.edit();

        final String fullName = profile.getFullName();
        if (!TextUtils.isEmpty(fullName)) {
            editor.putString(FULL_NAME, fullName);
        }

        final String facebookId = profile.getFacebookId();
        if (!TextUtils.isEmpty(fullName)) {
            editor.putString(FACEBOOK_ID, facebookId);
        }

        editor.apply();
    }

    public static void setUsersData(List<UserData> usersData) {
        final Gson gson = new Gson();
        String data = gson.toJson(usersData);
        final Editor editor = preferences.edit();
        editor.putString(USERS_DATA, data);
        editor.apply();
    }

    public static List<UserData> getUsersData() {
        final Gson gson = new Gson();
        String data = preferences.getString(USERS_DATA, null);
        if (data == null) {
            return null;
        }
        // Trick to get generic list type
        final Type listType = new TypeToken<List<UserData>>(){}.getType();
        return gson.fromJson(data, listType);
    }

    public static void setUsersValidPictures(Set<String> usersValidPictures) {
        final Editor editor = preferences.edit();
        editor.putStringSet(USERS_VALID_PICTURES, usersValidPictures);
        editor.apply();
    }

    public static Set<String> getUsersValidPictures() {
        Set<String> defaultValue = new HashSet<>();
        return preferences.getStringSet(USERS_VALID_PICTURES, defaultValue);
    }
}
