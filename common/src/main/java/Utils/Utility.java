package Utils;

import android.content.ContentResolver;
import android.content.Context;
import android.provider.Settings;

/**
 * Created by gabri on 6/7/2018.
 */

public class Utility {

    public static   float distance(float x1, float y1, float x2, float y2, Context context) {
        float dx = x1 - x2;
        float dy = y1 - y2;
        float distanceInPx = (float) Math.sqrt(dx * dx + dy * dy);
        return pxToDp(distanceInPx, context);
    }

    private static   float pxToDp(float px, Context context) {
        return px / context.getResources().getDisplayMetrics().density;
    }

    public static String getSerialNumber(ContentResolver contentResolver) {
        return Settings.Secure.getString(contentResolver, Settings.Secure.ANDROID_ID);

    }
}
