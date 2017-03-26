package studio.roboto.hack24.mycontent;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import studio.roboto.hack24.R;
import studio.roboto.hack24.firebase.FirebaseConnector;
import studio.roboto.hack24.firebase.models.Question;
import studio.roboto.hack24.localstorage.SharedPrefsManager;
import studio.roboto.hack24.utils.SortedListAdapter;

/**
 * Created by jordan on 19/03/17.
 */

public class QuestionsIPostedRVAdapter extends SortedListAdapter<Question, MyQuestionViewHolder> implements ChildEventListener, SortedListAdapter.iCompare<Question> {

    private DatabaseReference mDbRef;

    private List<Question> mQuestions;
    private QuestionsPostedFragment.OnQuestionClickListener mListener;
    private ChangeNotifier mNotifier;

    public QuestionsIPostedRVAdapter(ChangeNotifier notifier) {
        super(Question.class, new Comparator<Question>() {
            @Override
            public int compare(Question o1, Question o2) {
                return (int) ((o1.timestamp / 1000L) - (o2.timestamp / 1000L));
            }
        });
        this.mNotifier = notifier;
        mQuestions = new ArrayList<>();
        setEqualComparers(this);
        setupQuestionListener();
    }

    private void setupQuestionListener() {
        mDbRef = FirebaseConnector.getQuestions();
        mDbRef.addChildEventListener(this);
    }

    public void stop() {
        mDbRef.removeEventListener(this);
    }

    public List<Question> getQuestions() {
        return mQuestions;
    }

    @Override
    public MyQuestionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.element_questions_asked, parent, false);
        return new MyQuestionViewHolder(v, mListener);
    }

    @Override
    public void onBindViewHolderWithItem(MyQuestionViewHolder holder, int position, Question item) {
        holder.bind(item);
    }

    private int isContainedInList(String key) {
        for (int i = 0; i < getItemCount(); i++) {
            if (get(i).id.equals(key)) {
                return i;
            }
        }
        return -1;
    }

    public void setOnQuestionClickListener(QuestionsPostedFragment.OnQuestionClickListener listener) {
        this.mListener = listener;
    }

    //region ValueChange Listener
    @Override
    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
        if (dataSnapshot != null && dataSnapshot.getValue() != null && dataSnapshot.getKey() != null) {
            if (isContainedInList(dataSnapshot.getKey()) == -1) {
                Question q = dataSnapshot.getValue(Question.class);
                q.id = dataSnapshot.getKey();
                if (SharedPrefsManager.sharedInstance.getMyQuestionIds().contains(q.id)) {
                    add(q);
                    mQuestions.add(q);
                    mNotifier.countChanged(mQuestions.size());
                }
            }
        }
    }

    @Override
    public void onChildChanged(DataSnapshot dataSnapshot, String s) {
        if (dataSnapshot != null && dataSnapshot.getValue() != null) {
            int pos = isContainedInList(dataSnapshot.getKey());
            if (pos != -1) {
                Question q = dataSnapshot.getValue(Question.class);
                get(pos).yes = q.yes;
                get(pos).no = q.no;
                mQuestions.get(pos).yes = q.yes;
                mQuestions.get(pos).no = q.no;
                notifyItemChanged(pos);
            }
        }
    }

    @Override
    public void onChildRemoved(DataSnapshot dataSnapshot) {
        if (dataSnapshot != null && dataSnapshot.getValue() != null) {
            int pos = isContainedInList(dataSnapshot.getKey());
            if (pos != -1) {
                remove(get(pos));
                mQuestions.remove(pos);
                mNotifier.countChanged(mQuestions.size());
            }
        }
    }

    @Override
    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }
    //endregion

    @Override
    public boolean areEqual(Question item1, Question item2) {
        return item1.id.equals(item2.id);
    }

    public interface ChangeNotifier {
        void countChanged(int count);
    }
}
