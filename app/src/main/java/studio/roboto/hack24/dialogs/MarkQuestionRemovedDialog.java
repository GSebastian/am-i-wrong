package studio.roboto.hack24.dialogs;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import studio.roboto.hack24.R;
import studio.roboto.hack24.firebase.FirebaseConnector;
import studio.roboto.hack24.localstorage.SharedPrefsManager;

/**
 * Created by jordan on 03/04/17.
 */

public class MarkQuestionRemovedDialog extends DialogFragment implements View.OnClickListener {

    public static final String QUESTION_ID = "QUESTION_ID";

    private String mQuestionId;

    private View mView;

    private Button mBtnYes;
    private Button mBtnNo;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = new Dialog(getActivity());
        mView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_mark_question_hidden, null);

        getArgs();
        findViews(mView);
        initViews();

        dialog.setContentView(mView);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        return dialog;
    }

    private void getArgs() {
        if (getArguments() != null) {
            mQuestionId = getArguments().getString(QUESTION_ID, null);
        }
    }

    private void findViews(View v) {
        mBtnYes = (Button) v.findViewById(R.id.btnYes);
        mBtnNo = (Button) v.findViewById(R.id.btnNo);
    }

    private void initViews() {
        mBtnNo.setOnClickListener(this);
        mBtnYes.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == mBtnNo) {
            dismiss();
        }
        if (v == mBtnYes) {
            if (mQuestionId != null) {
                SharedPrefsManager.sharedInstance.markQuestionAsRemoved(mQuestionId);
            }
            dismiss();
        }
    }

    public static MarkQuestionRemovedDialog getInstance(String questionId) {
        MarkQuestionRemovedDialog dialog = new MarkQuestionRemovedDialog();
        Bundle b = new Bundle();
        b.putString(QUESTION_ID, questionId);
        dialog.setArguments(b);
        return dialog;
    }
}
