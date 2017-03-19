package studio.roboto.hack24.mycontent;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tiagosantos.enchantedviewpager.EnchantedViewPager;

import studio.roboto.hack24.HomeActivity;
import studio.roboto.hack24.R;
import studio.roboto.hack24.questions.QuestionElementDialogFragment;
import studio.roboto.hack24.questions.QuestionFragmentPagerAdapter;
import studio.roboto.hack24.questions.QuestionMinePA;

/**
 * Created by jordan on 18/03/17.
 */

public class QuestionsPostedFragment extends Fragment {

    public static final String TAG = "QUESTIONS_POSTED";

    private EnchantedViewPager mEnchVP;
    private QuestionFragmentPagerAdapter mAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_questions, container, false);

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
        mEnchVP = (EnchantedViewPager) v.findViewById(R.id.enchVP);
        mEnchVP.useScale();
        mEnchVP.addOnPageChangeListener((HomeActivity)getActivity());
    }

    private void initViews() {
        mAdapter = new QuestionMinePA(getFragmentManager(), getContext(), mEnchVP);
        mEnchVP.setAdapter(mAdapter);
    }

    @Override
    public void onDestroy() {
        mAdapter.stop();
        super.onDestroy();
    }
}
