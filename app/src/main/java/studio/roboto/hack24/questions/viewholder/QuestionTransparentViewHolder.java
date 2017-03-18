package studio.roboto.hack24.questions.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import studio.roboto.hack24.R;

/**
 * Created by jordan on 18/03/17.
 */

public class QuestionTransparentViewHolder extends RecyclerView.ViewHolder {

    private View mView;

    public QuestionTransparentViewHolder(View itemView) {
        super(itemView);
        mView = itemView.findViewById(R.id.view);
    }

    public void init(int size) {
        mView.setLayoutParams(new LinearLayout.LayoutParams(mView.getWidth(), size));
    }
}
