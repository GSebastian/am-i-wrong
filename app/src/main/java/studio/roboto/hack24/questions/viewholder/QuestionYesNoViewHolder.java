package studio.roboto.hack24.questions.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import studio.roboto.hack24.R;

/**
 * Created by jordan on 18/03/17.
 */

public class QuestionYesNoViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private YesNoCallback mCallback;
    private RelativeLayout mRlButtons;
    private RelativeLayout mRlPercentages;
    private TextView mTvYes;
    private TextView mTvNo;
    private Button mBtnYes;
    private Button mBtnNo;

    public QuestionYesNoViewHolder(View itemView, YesNoCallback callback) {
        super(itemView);
        this.mCallback = callback;
        this.mRlButtons = (RelativeLayout) itemView.findViewById(R.id.rlButtons);
        this.mRlPercentages = (RelativeLayout) itemView.findViewById(R.id.rlPercentages);
        this.mTvYes = (TextView) itemView.findViewById(R.id.tvYes);
        this.mTvNo = (TextView) itemView.findViewById(R.id.tvNo);
        this.mBtnYes = (Button) itemView.findViewById(R.id.btnYes);
        this.mBtnNo = (Button) itemView.findViewById(R.id.btnNo);
        this.mBtnYes.setOnClickListener(this);
        this.mBtnNo.setOnClickListener(this);
    }

    public void showOptions() {
        this.mRlButtons.setVisibility(View.VISIBLE);
        this.mRlPercentages.setVisibility(View.GONE);
    }

    public void showResults(int yes, int no) {
        this.mRlButtons.setVisibility(View.GONE);
        this.mRlPercentages.setVisibility(View.VISIBLE);
        float total = yes + no;

        mTvNo.setText((int)((no * 100) / total)  + "%");
        mTvYes.setText((int)((yes * 100) / total) + "%");
    }

    @Override
    public void onClick(View v) {
        if (v == mBtnYes) {
            mCallback.clickedYes();
        }
        if (v == mBtnNo) {
            mCallback.clickedNo();
        }
    }
}
