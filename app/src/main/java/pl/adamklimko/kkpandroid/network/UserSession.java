package pl.adamklimko.kkpandroid.network;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import pl.adamklimko.kkpandroid.model.*;
import pl.adamklimko.kkpandroid.util.ProfilePictureUtil;

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
    private static final String USERS_VALID_PICTURES = "users_valid_pictures";
    private static final String HISTORY = "history";
    private static final String MISSING = "missing";
    private static final String DIRTY = "dirty";

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

    public static void resetSession(Context context) {
        deleteUserProfiles(context);
        preferences.edit()
                .clear()
                .apply();
    }

    private static void deleteUserProfiles(Context context) {
        Set<String> validUserProfilePictures = getUsersValidPictures();
        for (String name : validUserProfilePictures) {
            ProfilePictureUtil.deletePictureFromStorage(ProfilePictureUtil.getUserPictureName(name), context);
        }
    }

    public static void setProfileDataInPreferences(Profile profile) {
        final Editor editor = preferences.edit();

        final String fullName = profile.getFullName();
        if (!TextUtils.isEmpty(fullName)) {
            editor.putString(FULL_NAME, fullName);
        }

        final String facebookId = profile.getFacebookId();
        if (!TextUtils.isEmpty(facebookId)) {
            editor.putString(FACEBOOK_ID, facebookId);
        }

        editor.apply();
    }

    public static void setData(Data data) {
        setUsersData(data.getUsersData());
        setHistoryData(data.getHistory());
        setMissingProducts(data.getMissingProducts());
        setDirtyRooms(data.getDirtyRooms());
    }

    public static void setUsersData(List<UserData> usersData) {
        setListInPreferences(usersData, USERS_DATA);
    }

    public static void setHistoryData(List<History> historyData) {
        setListInPreferences(historyData, HISTORY);
    }

    private static void setMissingProducts(Products missingProducts) {
        setMissingInPreferences(missingProducts);
    }

    private static void setDirtyRooms(Rooms dirtyRooms) {
        setDirtyInPreferences(dirtyRooms);
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

    public static List<History> getHistoryData() {
        String history = preferences.getString(HISTORY, null);
        if (history == null) {
            return null;
        }
        final Type listType = new TypeToken<List<History>>(){}.getType();
        return new Gson().fromJson(history, listType);
    }

    private static void setMissingInPreferences(Products missingProducts) {
        final Gson gson = new Gson();
        String data = gson.toJson(missingProducts);
        final Editor editor = preferences.edit();
        editor.putString(MISSING, data);
        editor.apply();
    }

    private static void setDirtyInPreferences(Rooms dirtyRooms) {
        final Gson gson = new Gson();
        String data = gson.toJson(dirtyRooms);
        final Editor editor = preferences.edit();
        editor.putString(DIRTY, data);
        editor.apply();
    }

    public static Products getMissingProducts() {
        String missing = preferences.getString(MISSING, null);
        if (missing == null) {
            return null;
        }
        final Type listType = new TypeToken<Products>(){}.getType();
        return new Gson().fromJson(missing, listType);
    }

    public static Rooms getDirtyRooms() {
        String dirty = preferences.getString(DIRTY, null);
        if (dirty == null) {
            return null;
        }
        final Type listType = new TypeToken<Rooms>(){}.getType();
        return new Gson().fromJson(dirty, listType);
    }

    private static <T> void setListInPreferences(List<T> list, String key) {
        final Gson gson = new Gson();
        String data = gson.toJson(list);
        final Editor editor = preferences.edit();
        editor.putString(key, data);
        editor.apply();
    }

//    private static <T> List<T> getListFromPreferences(List<T> classType, String key) {
//        final Gson gson = new Gson();
//        String data = preferences.getString(key, null);
//        if (data == null) {
//            return null;
//        }
//        // Trick to get generic list type
//        final Type listType = new TypeToken<List<T>>(){}.getType();
//        return gson.fromJson(data, listType);
//    }

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
