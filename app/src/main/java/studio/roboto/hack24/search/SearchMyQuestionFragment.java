package studio.roboto.hack24.search;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tiagosantos.enchantedviewpager.EnchantedViewPager;

import java.util.ArrayList;
import java.util.List;

import studio.roboto.hack24.HomeActivity;
import studio.roboto.hack24.R;

/**
 * Created by jordan on 03/04/17.
 */

public class SearchMyQuestionFragment extends DialogFragment {

    private static final String SEARCH_TERMS = "SEARCH_TERMS";

    private String mSearchTerm;
    private List<String> mWordsToSearch;

    private EnchantedViewPager mEnchVP;
    private QuestionSearchPA mAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_questions, container, false);

        getArgs();
        findViews(v);
        initViews();

        return v;
    }

    private void getArgs() {
        if (getArguments() != null) {
            mWordsToSearch = new ArrayList<>();
            mSearchTerm = getArguments().getString(SEARCH_TERMS, null);
            if (mSearchTerm != null) {
                String[] split = mSearchTerm.split(" ");
                for (String s : split) {
                    mWordsToSearch.add(s);
                }
            }
        }
    }

    private void findViews(View v) {
        mEnchVP = (EnchantedViewPager) v.findViewById(R.id.enchVP);
        mEnchVP.useScale();
        mEnchVP.addOnPageChangeListener((SearchMyQuestionActivity) getActivity());
    }

    private void initViews() {
        mAdapter = new QuestionSearchPA(getFragmentManager(), getContext(), mEnchVP, mWordsToSearch);
        mEnchVP.setAdapter(mAdapter);
    }

    public static SearchMyQuestionFragment getInstance(String searchTerm) {
        SearchMyQuestionFragment frag = new SearchMyQuestionFragment();
        Bundle bundle = new Bundle();
        bundle.putString(SEARCH_TERMS, searchTerm);
        frag.setArguments(bundle);
        return frag;
    }
}
