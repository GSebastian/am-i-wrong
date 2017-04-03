package studio.roboto.hack24.questions;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.tiagosantos.enchantedviewpager.EnchantedViewPager;

import java.util.ArrayList;
import java.util.List;

import studio.roboto.hack24.firebase.models.Question;

public abstract class QuestionFragmentPagerAdapter extends FragmentStatePagerAdapter implements ChildEventListener {

    private Context mContext;
    private List<Question> questions = new ArrayList<>();
    private EnchantedViewPager mViewPager;

    private DatabaseReference mQuestionRef;

    private OnQuestionAddedListener mOnQuestionAddedListener;

    public QuestionFragmentPagerAdapter(FragmentManager manager,
                                        Context context,
                                        EnchantedViewPager enchantedViewPager) {
        super(manager);

        this.mContext = context;
        this.mQuestionRef = getRef();
        this.mQuestionRef.addChildEventListener(this);
        this.mViewPager = enchantedViewPager;
    }

    @Override
    public int getCount() {
        return (showNew() ? 1 : 0) + questions.size();
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0 && showNew()) {
            NewQuestionFragment newQuestionFragment = new NewQuestionFragment();

            Bundle bundle = new Bundle();
            bundle.putInt("KEY", position);
            newQuestionFragment.setArguments(bundle);

            newQuestionFragment.setOnQuestionAddedListener(new OnQuestionAddedListener() {
                @Override
                public void questionAdded(String questionId) {
                    Integer position = null;
                    for (int i = 0; i < questions.size(); i++) {
                        Question question = questions.get(i);
                        if (question.id.equals(questionId)) {
                            position = i;
                            break;
                        }
                    }
                    if (position != null) {
                        mViewPager.setCurrentItem(position + 1);
                    }
                    mOnQuestionAddedListener.questionAdded(questionId);
                }

                @Override
                public void questionAddFailed() {
                    mOnQuestionAddedListener.questionAddFailed();
                }
            });

            return newQuestionFragment;
        } else {
            QuestionElementFragment frag = new QuestionElementFragment();
            Bundle b = new Bundle();
            b.putInt("KEY", position);
            int listPosition = reverseOrder() ?
                    (showNew() ? 1 : 0) :
                    (showNew() ? position - 1 : position);
            b.putString("QUESTION_ID", questions.get(listPosition).id);
            b.putString("QUESTION_TEXT", questions.get(listPosition).text);
            b.putLong("QUESTION_TIMESTAMP", questions.get(listPosition).timestamp);
            b.putLong("QUESTION_YES", questions.get(listPosition).yes);
            b.putLong("QUESTION_NO", questions.get(listPosition).no);
            frag.setArguments(b);

            return frag;
        }
    }

    private boolean contains(Question question) {
        for (Question q : questions) {
            if (q.id.equals(question.id)) {
                return true;
            }
        }
        return false;
    }

    public void reset() {
        questions.clear();
        mQuestionRef.removeEventListener(this);
        notifyDataSetChanged();

//        mQuestionRef.addChildEventListener(this);
    }

    public boolean reverseOrder() {
        return false;
    }

    public void stop() {
        mQuestionRef.removeEventListener(this);
    }

    public void setOnQuestionAddedListener(OnQuestionAddedListener onQuestionAddedListener) {
        this.mOnQuestionAddedListener = onQuestionAddedListener;
    }

    //region Abstract methods
    public abstract boolean shouldAdd(Question question);

    public abstract void added(Question question);

    public abstract boolean showNew();

    public abstract DatabaseReference getRef();
    //endregion

    //region Callbacks ChildEventListener
    @Override
    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
        if (dataSnapshot != null && dataSnapshot.getValue() != null) {
            Question question = dataSnapshot.getValue(Question.class);
            question.id = dataSnapshot.getKey();
            System.out.println("question.id = " + question.id);
            if (shouldAdd(question) && !contains(question)) {
                if (reverseOrder()) {
                    questions.add(0, question);
                } else {
                    questions.add(question);
                }
                notifyDataSetChanged();
                added(question);
            }
        }
    }

    @Override
    public void onChildChanged(DataSnapshot dataSnapshot, String s) {
    }

    @Override
    public void onChildRemoved(DataSnapshot dataSnapshot) {
    }

    @Override
    public void onChildMoved(DataSnapshot dataSnapshot, String s) {
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {
    }
    //endregion
}
