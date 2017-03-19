package studio.roboto.hack24.mycontent;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tiagosantos.enchantedviewpager.EnchantedViewPager;

import studio.roboto.hack24.HomeActivity;
import studio.roboto.hack24.R;
import studio.roboto.hack24.questions.QuestionElementDialogFragment;
import studio.roboto.hack24.questions.QuestionFragmentPagerAdapter;

/**
 * Created by jordan on 18/03/17.
 */

public class QuestionsPostedFragment extends Fragment {

    public static final String TAG = "QUESTIONS_POSTED";

    private RecyclerView mRlMain;
    private QuestionsIPostedRVAdapter mAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_questions_me, container, false);

        findViews(v);
        initViews();

        QuestionElementDialogFragment dialogFragment = new QuestionElementDialogFragment();
        Bundle bundle = new Bundle();

        bundle.putString("QUESTION_ID", "-KfYXHQ0GmKL0k0FUaMn");
        bundle.putString("QUESTION_TEXT", "TEST TEXT");
        bundle.putLong("QUESTION_TIMESTAMP", 1489876102);
        bundle.putLong("QUESTION_YES", 5);
        bundle.putLong("QUESTION_NO", 5);

        dialogFragment.setArguments(bundle);
        dialogFragment.show(getFragmentManager(), "TEST-TAG");


        return v;
    }

    private void findViews(View v) {
        mRlMain = (RecyclerView) v.findViewById(R.id.rlMain);
    }

    private void initViews() {
        mAdapter = new QuestionsIPostedRVAdapter();
        mRlMain.setLayoutManager(new LinearLayoutManager(getContext()));
        mRlMain.setAdapter(mAdapter);
    }

    @Override
    public void onDestroy() {
        mAdapter.stop();
        super.onDestroy();
    }

}
