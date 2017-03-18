package studio.roboto.hack24;

import android.animation.ArgbEvaluator;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;

import studio.roboto.hack24.questions.QuestionFragment;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, ViewPager.OnPageChangeListener {

    private static final float STATUS_BAR_DARKEN_MULTIPLIER = 0.85f;

    private View mViewColourChanger;
    private int[] mColoursForTransitions;
    private ArgbEvaluator mColorTransitioner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        setupAndInitColourTransitioner();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        showFragment(QuestionFragment.TAG);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_questions) {
            showFragment(QuestionFragment.TAG);
        } else if (id == R.id.nav_myquestions) {

        } else if (id == R.id.nav_comments) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void setupAndInitColourTransitioner() {
        mViewColourChanger = findViewById(R.id.viewColourChanger);
        mViewColourChanger.setBackgroundColor(Color.LTGRAY);

        String[] values = getResources().getStringArray(R.array.colors);
        mColoursForTransitions = new int[values.length];
        for (int i = 0; i < values.length; i++) {
            mColoursForTransitions[i] = Color.parseColor(values[i]);
        }
        mColorTransitioner = new ArgbEvaluator();
    }

    private void showFragment(String tag) {
        if (tag.equals(QuestionFragment.TAG)) {
            showFragment(QuestionFragment.TAG, new QuestionFragment());
        }
    }

    private void showFragment(String tag, Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, fragment, tag);
        transaction.commit();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void setStatusBarColour(int colour) {
        Window window = getWindow();
        // clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        // finally change the color
        window.setStatusBarColor(darkenColour(colour));
    }

    private static int darkenColour(int color) {
        int r = Color.red(color);
        int b = Color.blue(color);
        int g = Color.green(color);
        return Color.rgb((int) (r * STATUS_BAR_DARKEN_MULTIPLIER), (int) (g * STATUS_BAR_DARKEN_MULTIPLIER), (int) (b
                * STATUS_BAR_DARKEN_MULTIPLIER));
    }

    //region Callbacks OnPageChangedListener
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        int colour = (Integer) mColorTransitioner.evaluate(
                positionOffset,
                mColoursForTransitions[position % mColoursForTransitions.length],
                mColoursForTransitions[(position + 1) % mColoursForTransitions.length]);
        mViewColourChanger.setBackgroundColor(colour);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setStatusBarColour(colour);
        }
        Utils.hideKeyboard(this, mViewColourChanger);
    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
    //endregion
}
