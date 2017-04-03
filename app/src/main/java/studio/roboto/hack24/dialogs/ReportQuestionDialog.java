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

import studio.roboto.hack24.R;
import studio.roboto.hack24.firebase.FirebaseConnector;
import studio.roboto.hack24.localstorage.SharedPrefsManager;

/**
 * Created by jordan on 03/04/17.
 */

public class ReportQuestionDialog extends DialogFragment implements View.OnClickListener {

    private static final String QUESTION_ID = "QUESTION_ID";

    private String mQuestionId;

    private View mView;
    private Button mBtnDontWantToo;
    private Button mBtnOffensive;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = new Dialog(getActivity());
        mView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_report_questions, null);

        getArgs();
        findViews(mView);
        initViews();

        dialog.setContentView(mView);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        return dialog;
    }

    private void getArgs() {
        if (getArguments() != null) {
            mQuestionId = getArguments().getString(QUESTION_ID, null);
        }
    }

    private void findViews(View v) {
        mBtnDontWantToo = (Button) v.findViewById(R.id.btnDontWantToo);
        mBtnOffensive = (Button) v.findViewById(R.id.btnOffensive);
    }

    private void initViews() {
        mBtnDontWantToo.setOnClickListener(this);
        mBtnOffensive.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == mBtnDontWantToo) {
            if (mQuestionId != null) {
                SharedPrefsManager.sharedInstance.markQuestionAsRemoved(mQuestionId);
                dismiss();

                ReportFeedbackDialog dialog = ReportFeedbackDialog.getInstance();
                dialog.show(getFragmentManager(), "FEEDBACK_DIALOG");
            }
        }
        if (v == mBtnOffensive) {
            if (mQuestionId != null) {
                SharedPrefsManager.sharedInstance.markQuestionAsRemoved(mQuestionId);
                FirebaseConnector.reportQuestion(mQuestionId);
                dismiss();

                ReportFeedbackDialog dialog = ReportFeedbackDialog.getInstance();
                dialog.show(getFragmentManager(), "FEEDBACK_DIALOG");
            }
        }
    }

    public static ReportQuestionDialog getInstance(String questionId) {
        ReportQuestionDialog dialog = new ReportQuestionDialog();
        Bundle b = new Bundle();
        b.putString(QUESTION_ID, questionId);
        dialog.setArguments(b);
        return dialog;
    }
}
