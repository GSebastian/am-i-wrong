package studio.roboto.hack24;

import android.app.Application;

import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Logger;

import studio.roboto.hack24.localstorage.SharedPrefsManager;

public class Hack24Application extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        SharedPrefsManager.init(getApplicationContext());

        FirebaseDatabase.getInstance().setPersistenceEnabled(false);
        FirebaseDatabase.getInstance().setLogLevel(Logger.Level.DEBUG);
    }
}
