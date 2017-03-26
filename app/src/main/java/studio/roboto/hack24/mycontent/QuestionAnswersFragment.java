package studio.roboto.hack24.mycontent;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.tiagosantos.enchantedviewpager.EnchantedViewPager;

import studio.roboto.hack24.HomeActivity;
import studio.roboto.hack24.R;
import studio.roboto.hack24.questions.QuestionAnswersPA;
import studio.roboto.hack24.questions.QuestionFragmentPagerAdapter;
import studio.roboto.hack24.questions.QuestionMinePA;

/**
 * Created by jordan on 18/03/17.
 */

public class QuestionAnswersFragment extends Fragment {

    public static final String TAG = "QUESTIONS_ANSWERED";

    private EnchantedViewPager mEnchVP;
    private QuestionFragmentPagerAdapter mAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_questions, container, false);

        findViews(v);
        initViews();

        return v;
    }

    private void findViews(View v) {
        mEnchVP = (EnchantedViewPager) v.findViewById(R.id.enchVP);
        mEnchVP.useScale();
        mEnchVP.addOnPageChangeListener((HomeActivity)getActivity());
    }

    private void initViews() {
        mAdapter = new QuestionAnswersPA(getFragmentManager(), getContext(), mEnchVP);
        mEnchVP.setAdapter(mAdapter);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.search_button, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_search) {

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDestroy() {
        mAdapter.stop();
        super.onDestroy();
    }
}
