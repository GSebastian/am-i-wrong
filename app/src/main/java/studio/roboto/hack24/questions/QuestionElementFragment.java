package studio.roboto.hack24.questions;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.OnScrollListener;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tiagosantos.enchantedviewpager.EnchantedViewPager;

import studio.roboto.hack24.Hack24Application;
import studio.roboto.hack24.R;
import studio.roboto.hack24.dialogs.MarkQuestionRemovedDialog;
import studio.roboto.hack24.dialogs.ReportFeedbackDialog;
import studio.roboto.hack24.dialogs.ReportQuestionDialog;
import studio.roboto.hack24.firebase.FirebaseConnector;
import studio.roboto.hack24.firebase.models.Question;
import studio.roboto.hack24.localstorage.SharedPrefsManager;
import studio.roboto.hack24.questions.viewholder.YesNoCallback;

/**
 * Created by jordan on 18/03/17.
 */

public class QuestionElementFragment extends Fragment implements YesNoCallback, View.OnClickListener, TextWatcher {

    private static final int MODE_MINE = 0;
    private static final int MODE_OTHERS = 1;

    private int mPosition;
    private int totalScroll = 0;
    private Question mQuestion;
    private boolean isUnlocked = false;

    private int mMode;

    private EditText mEtComment;
    private ImageView mEtDivider;
    private ImageView mEtBackground;
    private Button mEtSend;
    private Button mBtnDelete;
    private RelativeLayout mRlMain;
    private LinearLayoutManager mLayoutManager;
    private TextView mTvQuestionTitle;
    private Button mBtnHide;
    private RelativeLayout mRlHideableHeader;
    private RecyclerView mRvMain;
    private QuestionRVAdapter mAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle
            savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_question_element, container, false);

        if (getArgs(v)) {
            findViews(v);
            initViews();

            if (Hack24Application.ADMIN_MODE) {
                mBtnDelete = (Button) v.findViewById(R.id.btnDelete);
                mBtnDelete.setVisibility(View.VISIBLE);
                mBtnDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        FirebaseConnector.getQuestion(mQuestion.id).setValue(null);
                        FirebaseConnector.getComments(mQuestion.id).setValue(null);
                        Toast.makeText(getContext(), "ADMIN MODE: Deleted question\n[" + mQuestion.text + "]", Toast.LENGTH_SHORT).show();
                    }
                });
            }

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

                mMode = SharedPrefsManager.sharedInstance.isThisMyQuestion(mQuestion.id) ?
                        MODE_MINE :
                        MODE_OTHERS;

                return true;
            }
        }
        return false;
    }

    private void findViews(View v) {
        mTvQuestionTitle = (TextView) v.findViewById(R.id.tvQuestionTitle);
        mRvMain = (RecyclerView) v.findViewById(R.id.rvMain);
        mRlMain = (RelativeLayout) v.findViewById(R.id.rlMain);
        mRlHideableHeader = (RelativeLayout) v.findViewById(R.id.rlHideableHeader);

        mEtComment = (EditText) v.findViewById(R.id.etContent);
        mEtDivider = (ImageView) v.findViewById(R.id.etDivider);
        mEtBackground = (ImageView) v.findViewById(R.id.etBackground);
        mEtSend = (Button) v.findViewById(R.id.tvSend);
        mBtnHide = (Button) v.findViewById(R.id.btnHide);
    }

    private void initViews() {
        mAdapter = new QuestionRVAdapter(
                getContext(),
                SharedPrefsManager.sharedInstance.whatDidIAnswer(mQuestion.id),
                this,
                mQuestion.id,
                mQuestion);
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
                float alpha = 1.0f - (2f * ((float) totalScroll / (float) heightOfHideableHeader()));
                mRlHideableHeader.setAlpha(alpha);
            }
        });
        mTvQuestionTitle.setText(mQuestion.text);

        SharedPrefsManager.VOTED type = SharedPrefsManager.sharedInstance.whatDidIAnswer(mQuestion.id);
        if (type != SharedPrefsManager.VOTED.UNANSWERED) {
            clickedAnswer(type == SharedPrefsManager.VOTED.YES);
            mEtSend.setEnabled(false);
        }

        // Comment text
        mEtSend.setOnClickListener(this);
        mEtComment.addTextChangedListener(this);

        mBtnHide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mMode == MODE_MINE) {
                    showHideDialog();
                } else if (mMode == MODE_OTHERS) {
                    showReportDialog();
                }
            }
        });
    }

    private void showHideDialog() {
        MarkQuestionRemovedDialog dialog = MarkQuestionRemovedDialog.getInstance(mQuestion.id);
        dialog.show(getFragmentManager(), "HIDE_DIALOG");
    }

    private void showReportDialog() {
        ReportQuestionDialog dialog = ReportQuestionDialog.getInstance(mQuestion.id);
        dialog.show(getFragmentManager(), "REPORT_DIALOG");
    }

    public void clickedAnswer(boolean wasYes) {
        mAdapter.itemClicked(wasYes);
        FirebaseConnector.vote(mQuestion.id, wasYes);
        SharedPrefsManager.sharedInstance.addAnsweredQuestionId(mQuestion.id, wasYes);
        isUnlocked = true;

        fadeIn(mEtBackground);
        fadeIn(mEtComment);
        fadeIn(mEtDivider);
        fadeIn(mEtSend);
    }

    private void fadeIn(View v) {
        v.setVisibility(View.VISIBLE);
        v.animate()
                .alpha(1.0f)
                .setDuration(400L)
                .start();
    }

    @Override
    public void onDestroy() {
        mAdapter.stop();
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        if (v == mEtSend) {
            SharedPrefsManager.VOTED vote = SharedPrefsManager.sharedInstance.whatDidIAnswer(mQuestion.id);
            if (vote != SharedPrefsManager.VOTED.UNANSWERED && mEtSend.getText().toString().trim().length() >= 1) {
                FirebaseConnector.addComment(
                        mQuestion.id,
                        mEtComment.getText().toString().trim(),
                        vote == SharedPrefsManager.VOTED.YES);
                mEtComment.setText("");
                mLayoutManager.scrollToPosition(2);
            }
        }
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
    public int heightOfHideableHeader() {
        return mRlHideableHeader.getHeight();
    }
    //endregion

    //region Callbacks TextWatcher
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        mEtSend.setEnabled(s.length() != 0);
    }
    //endregion
}

