package studio.roboto.hack24.questions;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.tiagosantos.enchantedviewpager.EnchantedViewPager;

import studio.roboto.hack24.HomeActivity;
import studio.roboto.hack24.R;
import studio.roboto.hack24.localstorage.SharedPrefsManager;

/**
 * Created by jordan on 18/03/17.
 */

public class QuestionFragment extends Fragment implements OnQuestionAddedListener {

    public static final String TAG = "QUESTIONS";

    private EnchantedViewPager mEnchVP;
    private QuestionFragmentPagerAdapter mAdapter;

//    private BroadcastReceiver mQuestionHiddenReceiver = new BroadcastReceiver() {
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            mAdapter.setOnQuestionAddedListener(null);
//
//            mAdapter = new QuestionMainPA(getFragmentManager(), getContext(), mEnchVP);
//            mAdapter.setOnQuestionAddedListener(QuestionFragment.this);
//            mEnchVP.setAdapter(mAdapter);
//        }
//    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle
            savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_questions, container, false);

        findViews(v);
        initViews();

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();

//        LocalBroadcastManager
//                .getInstance(getContext())
//                .registerReceiver(mQuestionHiddenReceiver, new IntentFilter(SharedPrefsManager.INTENT_QUESTION_HIDDEN));
    }

    @Override
    public void onPause() {
        super.onPause();

//        LocalBroadcastManager
//                .getInstance(getContext())
//                .unregisterReceiver(mQuestionHiddenReceiver);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.home, menu);
        menu.findItem(R.id.action_add).setVisible(mEnchVP.getCurrentItem() != 0);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_add) {
            mEnchVP.setCurrentItem(0);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDestroy() {
        mAdapter.stop();
        super.onDestroy();
    }

    private void findViews(View v) {
        mEnchVP = (EnchantedViewPager) v.findViewById(R.id.enchVP);
        mEnchVP.useScale();
        mEnchVP.addOnPageChangeListener((HomeActivity) getActivity());
    }

    private void initViews() {
        mAdapter = new QuestionMainPA(getFragmentManager(), getContext(), mEnchVP);
        mAdapter.setOnQuestionAddedListener(this);
        mEnchVP.setAdapter(mAdapter);
    }

    //region OnQuestionAddedListener
    @Override
    public void questionAdded(String questionId) {
        // Do nothing
    }

    @Override
    public void questionAddFailed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setCancelable(false);
        builder.setMessage(R.string.question_add_failed_message);
        builder.setPositiveButton(R.string.dismiss, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
    //endregion
}
