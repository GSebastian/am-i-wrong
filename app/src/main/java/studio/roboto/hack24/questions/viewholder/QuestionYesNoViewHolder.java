package studio.roboto.hack24.questions.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by jordan on 18/03/17.
 */

public class QuestionYesNoViewHolder extends RecyclerView.ViewHolder {

    private YesNoCallback mCallback;

    public QuestionYesNoViewHolder(View itemView, YesNoCallback callback) {
        super(itemView);
        this.mCallback = callback;
    }

    public void showOptions() {

    }

    public void showResults(int yes, int no) {

    }
}
