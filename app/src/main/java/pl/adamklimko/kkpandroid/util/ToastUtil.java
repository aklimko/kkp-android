package pl.adamklimko.kkpandroid.util;

import android.content.Context;
import android.widget.Toast;

public class ToastUtil {

    public static void showToastShort(String text, Context context) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
    }
}
