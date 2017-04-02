package studio.roboto.hack24.dialogs;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import studio.roboto.hack24.R;
import studio.roboto.hack24.firebase.FirebaseConnector;
import studio.roboto.hack24.localstorage.SharedPrefsManager;

public class ReportQuestionDialog {

    private Context mContext;
    private String mQuestionId;
    private QuestionHiddenListener mListener;

    private AlertDialog mDialog;

    public interface QuestionHiddenListener {
        void questionHidden(String questionId);
    }

    public static ReportQuestionDialog create(Context context,
                                              String questionId,
                                              @Nullable QuestionHiddenListener listener) {
        ReportQuestionDialog dialog = new ReportQuestionDialog();

        dialog.mContext = context;
        dialog.mQuestionId = questionId;
        dialog.mListener = listener;

        return dialog;
    }

    public AlertDialog getDialog() {
        View view = LayoutInflater.from(mContext).inflate(R.layout.dialog_report_question, null);

        Button btnNoReason = (Button) view.findViewById(R.id.btnNoReason);
        Button btnSpam = (Button) view.findViewById(R.id.btnSpam);

        mDialog = new AlertDialog.Builder(mContext)
                .setView(view)
                .setNegativeButton(R.string.cancel, null)
                .setTitle(R.string.hide_question)
                .create();
        mDialog.setMessage(mContext.getString(R.string.why_do_you_want_to_hide_this_post));
        mDialog.setCancelable(true);
        mDialog.setCanceledOnTouchOutside(true);

        btnNoReason.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPrefsManager.sharedInstance.markQuestionAsRemoved(mQuestionId);
                if (mListener != null) {
                    mListener.questionHidden(mQuestionId);
                }

                mDialog.cancel();
            }
        });

        btnSpam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPrefsManager.sharedInstance.markQuestionAsRemoved(mQuestionId);
                FirebaseConnector.reportQuestion(mQuestionId);
                if (mListener != null) {
                    mListener.questionHidden(mQuestionId);
                }

                mDialog.cancel();
            }
        });

        return mDialog;
    }
}
