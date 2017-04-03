package studio.roboto.hack24;

import android.app.Application;

import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Logger;

import studio.roboto.hack24.localstorage.SharedPrefsManager;

public class Hack24Application extends Application {

    private static final boolean ENABLE_ADMIN_CONTROLS = false;

    public static final boolean ADMIN_MODE = BuildConfig.DEBUG && ENABLE_ADMIN_CONTROLS;

    @Override
    public void onCreate() {
        super.onCreate();

        SharedPrefsManager.init(getApplicationContext());

        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        FirebaseDatabase.getInstance().setLogLevel(Logger.Level.DEBUG);
    }
}
