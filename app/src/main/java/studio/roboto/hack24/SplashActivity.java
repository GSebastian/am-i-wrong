package studio.roboto.hack24;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;

/**
 * Created by jordan on 19/03/17.
 */

public class SplashActivity extends AppCompatActivity implements Runnable {

    private Handler mHandler;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_splash);

        mHandler = new Handler();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mHandler.removeCallbacks(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mHandler.postDelayed(this, 1500);
    }

    @Override
    public void run() {
        Intent i = new Intent(this, HomeActivity.class);
        startActivity(i);
        finish();
    }
}
