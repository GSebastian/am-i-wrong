package studio.roboto.hack24.questions;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.OnScrollListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tiagosantos.enchantedviewpager.EnchantedViewPager;

import studio.roboto.hack24.R;
import studio.roboto.hack24.firebase.models.Question;
import studio.roboto.hack24.localstorage.SharedPrefsManager;
import studio.roboto.hack24.questions.viewholder.YesNoCallback;

/**
 * Created by jordan on 18/03/17.
 */

public class QuestionElementFragment extends Fragment implements YesNoCallback {

    private int mPosition;
    private int totalScroll = 0;
    private Question mQuestion;
    private boolean isUnlocked = false;

    private RelativeLayout mRlMain;
    private EditText mEtComment;
    private View mEtDivider;
    private LinearLayoutManager mLayoutManager;
    private TextView mTvQuestionTitle;
    private RecyclerView mRvMain;
    private QuestionRVAdapter mAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_question_element, container, false);

        if (getArgs(v)) {
            findViews(v);
            initViews();

            return v;
        } else {
            return inflater.inflate(R.layout.fragment_question_element_error, container, false);
        }
    }

    private boolean getArgs(View v) {
        if (getArguments() != null) {
            int position = getArguments().getInt("KEY", -1);
            if (position != -1) {
                mPosition = position;
                v.setTag(EnchantedViewPager.ENCHANTED_VIEWPAGER_POSITION + position);
            }
            String questionId = getArguments().getString("QUESTION_ID", null);
            if (questionId != null) {
                mQuestion = new Question(
                        getArguments().getString("QUESTION_TEXT", null),
                        getArguments().getLong("QUESTION_TIMESTAMP", 0L),
                        getArguments().getLong("QUESTION_YES", 0L),
                        getArguments().getLong("QUESTION_NO", 0L));
                mQuestion.id = questionId;
                return true;
            }
        }
        return false;
    }

    private void findViews(View v) {
        mTvQuestionTitle = (TextView) v.findViewById(R.id.tvQuestionTitle);
        mRvMain = (RecyclerView) v.findViewById(R.id.rvMain);
        mRlMain = (RelativeLayout) v.findViewById(R.id.rlMain);
        mEtComment = (EditText) v.findViewById(R.id.etComment);
        mEtDivider = v.findViewById(R.id.etDivider);
    }

    private void initViews() {
        mAdapter = new QuestionRVAdapter(getContext(), SharedPrefsManager.sharedInstance.whatDidIAnswer(mQuestion.id), this, mQuestion.id, mQuestion);
        mLayoutManager = new LinearLayoutManager(getContext()) {
            @Override
            public boolean canScrollVertically() {
                return isUnlocked && super.canScrollVertically();
            }
        };
        mRvMain.setLayoutManager(mLayoutManager);
        mRvMain.setAdapter(mAdapter);
        mRvMain.addOnScrollListener(new OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                totalScroll += dy;
                float alpha = 1.0f - (2f * ((float)totalScroll / (float)heightOfText()));
                mTvQuestionTitle.setAlpha(alpha);
            }
        });
        mRvMain.addItemDecoration(new SpaceItemDecoration(getResources().getDimensionPixelSize(R.dimen.edit_text_height)));
        mTvQuestionTitle.setText(mQuestion.text);
    }

    public void clickedAnswer(boolean wasYes) {
        mAdapter.itemClicked(wasYes);
        isUnlocked = true;
        mEtComment.setVisibility(View.VISIBLE);
        mEtComment.animate()
                .alpha(1.0f)
                .setDuration(400L)
                .start();
        mEtDivider.setVisibility(View.VISIBLE);
        mEtDivider.animate()
                .alpha(1.0f)
                .setDuration(400L)
                .start();
    }

    @Override
    public void onDestroy() {
        mAdapter.stop();
        super.onDestroy();
    }

    //region Callbacks YesNoCallback
    @Override
    public void clickedYes() {
        clickedAnswer(true);
    }

    @Override
    public void clickedNo() {
        clickedAnswer(false);
    }

    @Override
    public int heightOfFrag() {
        return mRlMain.getHeight();
    }

    @Override
    public int heightOfText() {
        return mTvQuestionTitle.getHeight();
    }
    //endregion
}

