package studio.roboto.hack24.mycontent;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

import studio.roboto.hack24.R;
import studio.roboto.hack24.firebase.models.Question;
import studio.roboto.hack24.questions.QuestionElementDialogFragment;
import studio.roboto.hack24.utils.SortedListAdapter;

/**
 * Created by jordan on 18/03/17.
 */

public class QuestionsPostedFragment extends Fragment implements QuestionsIPostedRVAdapter.ChangeNotifier {

    public static final String TAG = "QUESTIONS_POSTED";

    private RecyclerView mRlMain;
    private QuestionsIPostedRVAdapter mAdapter;
    private RelativeLayout mRlNoQuestions;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

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
        mRlNoQuestions = (RelativeLayout) v.findViewById(R.id.rlNoQuestions);
    }

    private void initViews() {
        mAdapter = new QuestionsIPostedRVAdapter(this);
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
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.search, menu);

        final MenuItem searchItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        EditText searchEditText = (EditText) searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);
        searchEditText.setHintTextColor(ContextCompat.getColor(getContext(), R.color.my_questions_search_hint));
        searchEditText.setTextColor(ContextCompat.getColor(getContext(), R.color.my_questions_search_text));
        searchView.setOnQueryTextListener(SortedListAdapter.SortedListFilter.Builder.create(Question.class, mAdapter)
                .withRecyclerView(mRlMain)
                .matching(new SortedListAdapter.SortedListMatcher<Question>() {
                    @Override
                    public String matchSearchOn(Question model) {
                        return model.text;
                    }

                    @Override
                    public List<Question> getFullQuestions() {
                        return mAdapter.getQuestions();
                    }
                }));

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onDestroy() {
        mAdapter.stop();
        super.onDestroy();
    }

    //region Callbacks ChangeNotifier
    @Override
    public void countChanged(int count) {
        mRlNoQuestions.setVisibility(count != 0 ? View.GONE : View.VISIBLE);
    }
    //endregion

    public interface OnQuestionClickListener {
        void clickedQuestion(Question question);
    }
}
