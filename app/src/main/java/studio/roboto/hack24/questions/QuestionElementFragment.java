package studio.roboto.hack24.questions;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tiagosantos.enchantedviewpager.EnchantedViewPager;

import studio.roboto.hack24.R;

/**
 * Created by jordan on 18/03/17.
 */

public class QuestionElementFragment extends Fragment {

    private int mPosition;

    private TextView mTvQuestionTitle;
    private RecyclerView mRlMain;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_question_element, container, false);

        getArgs(v);
        findViews(v);
        initViews();

        return v;
    }

    private void getArgs(View v) {
        if (getArguments() != null) {
            int position = getArguments().getInt("KEY", -1);
            if (position != -1) {
                mPosition = position;
                v.setTag(EnchantedViewPager.ENCHANTED_VIEWPAGER_POSITION + position);
            }
        }
    }

    private void findViews(View v) {
        mTvQuestionTitle = (TextView) v.findViewById(R.id.tvQuestionTitle);
        mRlMain = (RecyclerView) v.findViewById(R.id.rvMain);
    }

    private void initViews() {

    }
}

