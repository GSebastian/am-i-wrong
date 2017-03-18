package studio.roboto.hack24.questions;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import studio.roboto.hack24.R;
import studio.roboto.hack24.firebase.FirebaseConnector;
import studio.roboto.hack24.firebase.FirebaseManager;
import studio.roboto.hack24.firebase.SharedPrefsConnector;
import studio.roboto.hack24.firebase.models.Comment;
import studio.roboto.hack24.firebase.models.Question;
import studio.roboto.hack24.questions.viewholder.QuestionCommentViewHolder;
import studio.roboto.hack24.questions.viewholder.QuestionTransparentViewHolder;
import studio.roboto.hack24.questions.viewholder.QuestionYesNoViewHolder;
import studio.roboto.hack24.questions.viewholder.YesNoCallback;

/**
 * Created by jordan on 18/03/17.
 */

public class QuestionRVAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private List<Comment> comments;

    private Question mQuestion;

    private VOTED mVoted;
    private YesNoCallback mCallback;

    public enum VOTED {
        UNVOTED,
        YES,
        NO
    };

    public QuestionRVAdapter(Context context, VOTED voted, YesNoCallback callback, Question question) {
        this.mContext = context;
        this.mVoted = voted;
        this.mCallback = callback;
        this.mQuestion = question;

        setupQuestionListener();
        setupCommentListener();
    }

    public void add(Comment comment) {
        comments.add(2, comment);
        notifyItemInserted(2);
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

    private void setupQuestionListener() {

    }

    private void setupCommentListener() {

    }

    public void itemClicked(boolean wasYes) {

    }

    @Override
    public int getItemViewType(int position) {
        return (position >= 2 ? 2 : position);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (position == 0) {
            ((QuestionTransparentViewHolder)holder).init();
        } else if (position == 1) {
            if (mVoted == VOTED.UNVOTED) {
                ((QuestionYesNoViewHolder) holder).showOptions();
            } else {
                ((QuestionYesNoViewHolder) holder).showResults();
            }
        } else {
            ((QuestionCommentViewHolder)holder).bind(comments.get(position - 2));
        }
    }

    @Override
    public int getItemCount() {
        return 2 + comments.size();
    }
}
