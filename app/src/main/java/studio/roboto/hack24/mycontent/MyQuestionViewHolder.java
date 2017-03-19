package studio.roboto.hack24.mycontent;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import studio.roboto.hack24.R;
import studio.roboto.hack24.firebase.models.Question;

/**
 * Created by jordan on 19/03/17.
 */

public class MyQuestionViewHolder extends RecyclerView.ViewHolder {

    private TextView mTvTitle;
    private TextView mTvNo;
    private TextView mTvYes;

    public MyQuestionViewHolder(View itemView) {
        super(itemView);
        mTvTitle = (TextView) itemView.findViewById(R.id.tvTitle);
        mTvNo = (TextView) itemView.findViewById(R.id.tvNo);
        mTvYes = (TextView) itemView.findViewById(R.id.tvYes);
    }

    public void bind(Question question) {
        mTvTitle.setText(question.text);
        int total = (int) (question.yes + question.no);
        if (total != 0) {
            mTvNo.setText((int) ((question.no * 100) / total) + "%");
            mTvYes.setText((int) ((question.yes * 100) / total) + "%");
        } else {
            mTvNo.setText("0%");
            mTvYes.setText("0%");
        }
    }
}
