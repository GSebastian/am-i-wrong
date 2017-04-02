package studio.roboto.hack24;

import android.animation.ArgbEvaluator;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import studio.roboto.hack24.localstorage.SharedPrefsManager;
import studio.roboto.hack24.mycontent.QuestionsPostedFragment;
import studio.roboto.hack24.mycontent.QuestionAnswersFragment;
import studio.roboto.hack24.dialogs.NameDialog;
import studio.roboto.hack24.questions.QuestionFragment;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, ViewPager.OnPageChangeListener, NameDialog.NameConfirmedCallback, View.OnClickListener {

    private static final float STATUS_BAR_DARKEN_MULTIPLIER = 0.85f;

    private View mViewColourChanger;
    private int[] mColoursForTransitions;
    private ArgbEvaluator mColorTransitioner;
    private ImageView mImgHeader;
    private TextView mTvName;
    private NameDialog mNameDialog;
    private ImageButton mEditName;
    private NavigationView mNavView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mViewColourChanger = findViewById(R.id.viewColourChanger);
        mViewColourChanger.setBackgroundColor(Color.LTGRAY);
        setupAndInitColourTransitioner();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        mNavView = (NavigationView) findViewById(R.id.nav_view);
        mNavView.setNavigationItemSelectedListener(this);
        mImgHeader = (ImageView) mNavView.getHeaderView(0).findViewById(R.id.imgHeader);
        mTvName = (TextView) mNavView.getHeaderView(0).findViewById(R.id.name);
        mEditName = (ImageButton) mNavView.getHeaderView(0).findViewById(R.id.imgEditName);
        mEditName.setOnClickListener(this);
        mTvName.setText("Commenting as: " + SharedPrefsManager.sharedInstance.getCurrentName());

        showFragment(QuestionFragment.TAG);
        mNavView.getMenu().getItem(0).setChecked(true);

        mNameDialog = NameDialog.create(this);
        if (SharedPrefsManager.sharedInstance.isFirstOpen()) {
            mNameDialog.getDialog(this).show();
            SharedPrefsManager.sharedInstance.firstOpen();
        }
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_questions) {
            showFragment(QuestionFragment.TAG);
        } else if (id == R.id.nav_myquestions) {
            showFragment(QuestionsPostedFragment.TAG);
        } else if (id == R.id.nav_comments) {
            showFragment(QuestionAnswersFragment.TAG);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
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
    }

    private void showFragment(String tag) {
        if (tag.equals(QuestionFragment.TAG)) {
            setTitle(R.string.app_name);
            showFragment(QuestionFragment.TAG, new QuestionFragment());
            setupAndInitColourTransitioner();
        }
        if (tag.equals(QuestionAnswersFragment.TAG)) {
            setTitle(R.string.questions_ive_answered);
            showFragment(QuestionAnswersFragment.TAG, new QuestionAnswersFragment());
            int colour = ContextCompat.getColor(this, R.color.yes_color);
            mViewColourChanger.setBackgroundColor(colour);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                setStatusBarColour(darkenColour(colour));
            }
            mImgHeader.setBackgroundColor(colour);

        }
        if (tag.equals(QuestionsPostedFragment.TAG)) {
            setTitle(R.string.questions_posted);
            showFragment(QuestionsPostedFragment.TAG, new QuestionsPostedFragment());
            setupAndInitColourTransitioner();
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

        int colPosFrom = position % mColoursForTransitions.length;
        int colPosTo = (position + 1) % mColoursForTransitions.length;
        if (mColoursForTransitions[colPosFrom] == Color.BLACK) {
            colour = (Integer) mColorTransitioner.evaluate(
                    positionOffset,
                    Color.WHITE,
                    mColoursForTransitions[colPosTo]
            );
        }
        if (mColoursForTransitions[colPosTo] == Color.BLACK) {
            colour = (Integer) mColorTransitioner.evaluate(
                    positionOffset,
                    mColoursForTransitions[colPosFrom],
                    Color.WHITE
            );
        }
        mImgHeader.setBackgroundColor(colour);
        Utils.hideKeyboard(this, mViewColourChanger);
    }

    @Override
    public void onPageSelected(int position) {
        supportInvalidateOptionsMenu();
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
    //endregion

    //region Callback Name Confirmed
    @Override
    public void nameConfirmed(String name) {
        mTvName.setText("Commenting as: " + name);
    }
    //endregion

    @Override
    public void onClick(View v) {
        if (v == mEditName) {
            mNameDialog.getDialog(this).show();
        }
    }
}
