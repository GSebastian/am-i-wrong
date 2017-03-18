package studio.roboto.hack24.questions.viewholder;

import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import studio.roboto.hack24.R;
import studio.roboto.hack24.firebase.models.Comment;

/**
 * Created by jordan on 18/03/17.
 */

public class QuestionCommentViewHolder extends RecyclerView.ViewHolder {

    private View mViewBar;
    private TextView mTvName;
    private TextView mTvContent;

    public QuestionCommentViewHolder(View itemView) {
        super(itemView);
        mViewBar = itemView.findViewById(R.id.commentView);
        mTvName = (TextView) itemView.findViewById(R.id.tvName);
        mTvContent = (TextView) itemView.findViewById(R.id.tvContent);
    }

    public void bind(Comment comment) {
        mTvName.setText(comment.text);
//        mTvContent.setText(comment.name);
//        if (comment.yes) {
//            mViewBar.setBackgroundColor(ContextCompat.getColor(mViewBar.getContext(), R.color.yes_color));
//        } else {
//            mViewBar.setBackgroundColor(ContextCompat.getColor(mViewBar.getContext(), R.color.no_color));
//        }
    }
}
