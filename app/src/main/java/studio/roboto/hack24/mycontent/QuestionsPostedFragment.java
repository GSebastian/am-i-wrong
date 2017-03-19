package studio.roboto.hack24.mycontent;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import studio.roboto.hack24.R;
import studio.roboto.hack24.firebase.models.Question;
import studio.roboto.hack24.questions.QuestionElementDialogFragment;

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

        return v;
    }

    private void findViews(View v) {
        mRlMain = (RecyclerView) v.findViewById(R.id.rlMain);
    }

    private void initViews() {
        mAdapter = new QuestionsIPostedRVAdapter();
        mAdapter.setOnQuestionClickListener(new OnQuestionClickListener() {
            @Override
            public void clickedQuestion(Question question) {
                QuestionElementDialogFragment dialogFragment = new QuestionElementDialogFragment();
                Bundle bundle = new Bundle();

                bundle.putString("QUESTION_ID", question.id);
                bundle.putString("QUESTION_TEXT", question.text);
                bundle.putLong("QUESTION_TIMESTAMP", question.timestamp);
                bundle.putLong("QUESTION_YES", question.yes);
                bundle.putLong("QUESTION_NO", question.no);

                dialogFragment.setArguments(bundle);
                dialogFragment.show(getFragmentManager(), "TEST-TAG");
            }
        });
        mRlMain.setLayoutManager(new LinearLayoutManager(getContext()));
        mRlMain.setAdapter(mAdapter);
    }

    @Override
    public void onDestroy() {
        mAdapter.stop();
        super.onDestroy();
    }

    public interface OnQuestionClickListener {
        void clickedQuestion(Question question);
    }

}
