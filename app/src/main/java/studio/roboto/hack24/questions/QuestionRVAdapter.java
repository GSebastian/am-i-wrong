package studio.roboto.hack24.questions;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
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
import studio.roboto.hack24.firebase.models.Comment;
import studio.roboto.hack24.firebase.models.Question;
import studio.roboto.hack24.localstorage.SharedPrefsManager;
import studio.roboto.hack24.questions.viewholder.QuestionCommentViewHolder;
import studio.roboto.hack24.questions.viewholder.QuestionTransparentViewHolder;
import studio.roboto.hack24.questions.viewholder.QuestionYesNoViewHolder;
import studio.roboto.hack24.questions.viewholder.YesNoCallback;

/**
 * Created by jordan on 18/03/17.
 */

public class QuestionRVAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements ChildEventListener {

    private Context mContext;
    private List<Comment> comments;

    private Question mQuestion;
    private String mQuestionId;

    private SharedPrefsManager.VOTED mVoted;
    private YesNoCallback mCallback;

    private DatabaseReference mQuestionRef;
    private ValueEventListener mQuestionListener;
    private DatabaseReference mCommentRef;

    public QuestionRVAdapter(Context context, SharedPrefsManager.VOTED voted, YesNoCallback callback, String questionId, Question question) {
        this.mContext = context;
        this.mVoted = voted;
        this.mCallback = callback;
        this.mQuestion = question;
        this.mQuestionId = questionId;
        this.comments = new ArrayList<>();

        setupQuestionListener();
        setupCommentListener();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater i = LayoutInflater.from(parent.getContext());
        if (viewType == 1) {
            return new QuestionYesNoViewHolder(i.inflate(R.layout.comment_yesno, parent, false), mCallback);
        } else if (viewType == 2) {
            return new QuestionCommentViewHolder(i.inflate(R.layout.comment_comment, parent, false));
        } else {
            return new QuestionTransparentViewHolder(i.inflate(R.layout.comment_transparentheader, parent, false));
        }
    }

    @Override
    public int getItemViewType(int position) {
        return (position >= 2 ? 2 : position);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (position == 0) {
            if (mVoted == SharedPrefsManager.VOTED.UNANSWERED) {
                int height = mCallback.heightOfFrag() - mContext.getResources().getDimensionPixelSize(R.dimen.answer_height);
                ((QuestionTransparentViewHolder)holder).init(height);
            } else {
                int height = mCallback.heightOfText();
                ((QuestionTransparentViewHolder)holder).init(height);
            }
        } else if (position == 1) {
            if (mVoted == SharedPrefsManager.VOTED.UNANSWERED) {
                ((QuestionYesNoViewHolder) holder).showOptions();
            } else {
                ((QuestionYesNoViewHolder) holder).showResults((int)mQuestion.yes, (int)mQuestion.no);
            }
        } else {
            ((QuestionCommentViewHolder)holder).bind(comments.get(position - 2));
        }
    }

    @Override
    public int getItemCount() {
        return 2 + comments.size();
    }

    //region Addition of comment method, designed to make smooth animations
    public void add(Comment comment) {
        comments.add(0, comment);
        notifyItemInserted(2);
    }
    //endregion

    //region Stop method to de-register firebase listeners
    public void stop() {
        if (mQuestionRef != null && mQuestionListener != null) {
            mQuestionRef.removeEventListener(mQuestionListener);
        }
        if (mCommentRef != null) {
            mCommentRef.removeEventListener(this);
        }
    }
    //endregion

    //region Setup for the listeners for live updating
    private void setupQuestionListener() {
        mQuestionRef = FirebaseConnector.getQuestion(mQuestionId);
        mQuestionListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot != null && dataSnapshot.getValue() != null) {
                    mQuestion = dataSnapshot.getValue(Question.class);
                }
                if (mVoted != SharedPrefsManager.VOTED.UNANSWERED) {
                    notifyItemChanged(1);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        mQuestionRef.addValueEventListener(mQuestionListener);
    }

    private void setupCommentListener() {
        mCommentRef = FirebaseConnector.getComments(mQuestionId);
        mCommentRef.addChildEventListener(this);
    }
    //endregion

    public void itemClicked(boolean wasYes) {
        mVoted = (wasYes ? SharedPrefsManager.VOTED.YES : SharedPrefsManager.VOTED.NO);
        SharedPrefsManager.sharedInstance.addAnsweredQuestionId(mQuestionId, wasYes);
        notifyItemChanged(0);
        notifyItemChanged(1);
    }

    //region Callbacks for ChildEventListener
    @Override
    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
        if (dataSnapshot != null && dataSnapshot.getValue() != null) {
            Comment comment = dataSnapshot.getValue(Comment.class);
            comment.id = dataSnapshot.getKey();
            add(comment);
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
