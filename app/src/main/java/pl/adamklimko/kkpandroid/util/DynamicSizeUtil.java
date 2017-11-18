package pl.adamklimko.kkpandroid.util;

import android.content.Context;

public class DynamicSizeUtil {
    public static int getPixelsFromDp(Context context, int dp) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }
}
