package studio.roboto.hack24;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import studio.roboto.hack24.localstorage.SharedPrefsManager;

public class Utils {

    public static long getTimestamp() {
        return System.currentTimeMillis() / 1000;
    }

    public static String dateStringFromTimestamp(long timestamp) {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(timestamp);
        Date d = c.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        return sdf.format(d);
    }

    public static void hideKeyboard(Context context, View view) {
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public static long POST_LIMIT = 5 * 60;

    public static boolean canIPost() {
        return getTimestamp() - SharedPrefsManager.sharedInstance.getLastQuestionTime() > POST_LIMIT;
    }
}
