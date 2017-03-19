package studio.roboto.hack24.mycontent;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import studio.roboto.hack24.R;
import studio.roboto.hack24.firebase.FirebaseConnector;
import studio.roboto.hack24.firebase.FirebaseManager;
import studio.roboto.hack24.firebase.models.Question;
import studio.roboto.hack24.localstorage.SharedPrefsManager;

/**
 * Created by jordan on 19/03/17.
 */

public class QuestionsIPostedRVAdapter extends RecyclerView.Adapter<MyQuestionViewHolder> implements ChildEventListener {

    private List<Question> mQuestions;
    private DatabaseReference mDbRef;

    public QuestionsIPostedRVAdapter() {
        mQuestions = new ArrayList<>();
        setupQuestionListener();
    }

    private void setupQuestionListener() {
        mDbRef = FirebaseConnector.getQuestions();
        mDbRef.addChildEventListener(this);
    }

    public void stop() {
        mDbRef.removeEventListener(this);
    }

    @Override
    public MyQuestionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.element_questions_asked, parent, false);
        return new MyQuestionViewHolder(v);
    }

    @Override
    public void onBindViewHolder(MyQuestionViewHolder holder, int position) {
        holder.bind(mQuestions.get(position));
    }

    @Override
    public int getItemCount() {
        return mQuestions.size();
    }

    private int isContainedInList(String key) {
        for (int i = 0; i < mQuestions.size(); i++) {
            if (mQuestions.get(i).id.equals(key)) {
                return i;
            }
        }
        return -1;
    }

    //region ValueChange Listener
    @Override
    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
        if (dataSnapshot != null && dataSnapshot.getValue() != null && dataSnapshot.getKey() != null) {
            if (isContainedInList(dataSnapshot.getKey()) == -1) {
                Question q = dataSnapshot.getValue(Question.class);
                q.id = dataSnapshot.getKey();
                if (SharedPrefsManager.sharedInstance.getMyQuestionIds().contains(q.id)) {
                    mQuestions.add(0, q);
                    notifyItemInserted(0);
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
                q.id = mQuestions.get(pos).id;
                mQuestions.set(pos, q);
                notifyItemChanged(pos);
            }
        }
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
