package studio.roboto.hack24.search;

import android.animation.ArgbEvaluator;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import studio.roboto.hack24.R;
import studio.roboto.hack24.Utils;

import static studio.roboto.hack24.HomeActivity.STATUS_BAR_DARKEN_MULTIPLIER;

/**
 * Created by jordan on 03/04/17.
 */

public class SearchMyQuestionActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener {

    public static final String SEARCH_TERM = "SEARCH_TERM";

    private Toolbar mToolbar;

    private String mSearchTerm;

    private View mViewColourChanger;
    private int[] mColoursForTransitions;
    private ArgbEvaluator mColorTransitioner;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        getArgs();
        findViews();
        initViews();

        setupAndInitColourTransitioner();

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void getArgs() {
        if (getIntent().getExtras() != null) {
            mSearchTerm = getIntent().getExtras().getString(SEARCH_TERM, "");
        }
    }

    private void findViews() {
        mViewColourChanger = findViewById(R.id.viewColourChanger);
    }

    private void initViews() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, SearchMyQuestionFragment.getInstance(mSearchTerm));
        transaction.commit();
    }

    private void setupAndInitColourTransitioner() {
        String[] values = getResources().getStringArray(R.array.colors);
        List<Integer> mColours = new ArrayList<>();
        mColoursForTransitions = new int[values.length];
        for (int i = 0; i <  values.length; i++) {
            mColours.add(Color.parseColor(values[i]));
        }
        Collections.shuffle(mColours);

        for (int i = 0; i < values.length; i++) {
            mColoursForTransitions[i] = mColours.get(i);
        }
        mColorTransitioner = new ArgbEvaluator();
        mViewColourChanger.setBackgroundColor(mColoursForTransitions[0]);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setStatusBarColour(mColoursForTransitions[0]);
        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
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

    //region Callbacks OnPageChangeListener

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
