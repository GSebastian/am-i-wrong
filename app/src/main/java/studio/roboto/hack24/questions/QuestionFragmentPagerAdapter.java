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

public  abstract class QuestionFragmentPagerAdapter extends FragmentStatePagerAdapter implements ChildEventListener {

    private Context mContext;
    private List<Question> questions = new ArrayList<>();
    private EnchantedViewPager mViewPager;

    private DatabaseReference mQuestionRef;

    private OnQuestionAddedListener mOnQuestionAddedListener;

    public QuestionFragmentPagerAdapter(FragmentManager manager, Context context, EnchantedViewPager enchantedViewPager) {
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
            position -= (showNew() ? 1 : 0);

            QuestionElementFragment frag = new QuestionElementFragment();
            Bundle b = new Bundle();
            b.putInt("KEY", position + (showNew() ? 1 : 0));
            b.putString("QUESTION_ID", questions.get(position).id);
            b.putString("QUESTION_TEXT", questions.get(position).text);
            b.putLong("QUESTION_TIMESTAMP", questions.get(position).timestamp);
            b.putLong("QUESTION_YES", questions.get(position).yes);
            b.putLong("QUESTION_NO", questions.get(position).no);
            frag.setArguments(b);

            return frag;
        }
    }

    public void stop() {
        mQuestionRef.removeEventListener(this);
    }

	public void setOnQuestionAddedListener(OnQuestionAddedListener onQuestionAddedListener) {
        this.mOnQuestionAddedListener = onQuestionAddedListener;
    }


    public abstract boolean shouldAdd(Question question);

    public abstract void added(Question question);

    public abstract boolean showNew();

    public abstract DatabaseReference getRef();

    //region Callbacks ChildEventListener
    @Override
    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
        if (dataSnapshot != null && dataSnapshot.getValue() != null) {
            Question question = dataSnapshot.getValue(Question.class);
            question.id = dataSnapshot.getKey();
            if (shouldAdd(question)) {
                questions.add(question);
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
